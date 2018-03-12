import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class NaiveBayes {
    private DataSet training_dataSet;
    private Map<String, Double> spamProbabilityMap;
    private Map<String, Double> hamProbabilityMap;
    private double spamProb;
    private double hamProb;

    public NaiveBayes(DataSet training_dataSet){
        this.training_dataSet = training_dataSet;
    }


    /* calculating probability of file being a spam or ham given word for all words in the dictionary
       and storing in spamProbabilityMap and hamProbabilityMap */
    public void trainMultinomialNB(){
        spamProb = Math.log(training_dataSet.getTotalSpam()/(training_dataSet.getTotalSpam()+training_dataSet.getTotalHam()));
        hamProb =  Math.log(1.0-spamProb);

        double spamWordCount = 0.0;
        for(Map.Entry<String, Double> entry: training_dataSet.getSpamMap().entrySet()){
            spamWordCount = spamWordCount + entry.getValue();
        }

        double hamWordCount = 0.0;
        for(Map.Entry<String, Double> entry: training_dataSet.getHamMap().entrySet()){
            hamWordCount = hamWordCount + entry.getValue();
        }

        int totalWords =  training_dataSet.getDictionary().size();

        spamProbabilityMap = new HashMap<>();
        hamProbabilityMap = new HashMap<>();

        Map<String, Double> spamMap = training_dataSet.getSpamMap();
        Map<String, Double> hamMap = training_dataSet.getHamMap();
        double word_SP;
        double word_HP;

        for (String word: training_dataSet.getDictionary()) {
            if(spamMap.containsKey(word)){
                word_SP = (spamMap.get(word)+1.0)/(spamWordCount+totalWords);
            }else{
                word_SP = (1.0)/(spamWordCount+totalWords);
            }

            if(hamMap.containsKey(word)){
                word_HP = (hamMap.get(word)+1.0)/(hamWordCount+totalWords);
            }else{
                word_HP = (1.0)/(hamWordCount+totalWords);
            }

            spamProbabilityMap.put(word, Math.log(word_SP));
            hamProbabilityMap.put(word, Math.log(word_HP));
        }
    }

    // applying multinomial Naive Bayes on test spam and ham directories and returning accuracy
    public double applyMultinomialNB(File test_spam_dir, File test_ham_dir){

        double correctlyClassifiedFiles = 0.0;
        double totalFiles = 0.0;
        String fileClass;

        for (File file: test_spam_dir.listFiles()) {
            fileClass = findClass(file);
            if(fileClass.equals("spam")){
                correctlyClassifiedFiles++;
            }
            totalFiles++;
        }

        for (File file: test_ham_dir.listFiles()) {
            fileClass = findClass(file);
            if(fileClass.equals("ham")){
                correctlyClassifiedFiles++;
            }
            totalFiles++;
        }

        return (correctlyClassifiedFiles/totalFiles)*100.0;
    }

    /* calculating the probability of a file being spam or ham based on words in the file using
       the probabilities of word calculated in training Naive Bayes */
    private String findClass(File file){
       try{
           double fileSpamProb = 0.0;
           double fileHamProb = 0.0;

           BufferedReader reader = new BufferedReader(new FileReader(file));
           String line = reader.readLine();
           while(line!=null){
               for(String s : line.toLowerCase().split(" ")){
                   if(training_dataSet.getDictionary().contains(s)){
                       fileSpamProb +=  spamProbabilityMap.get(s);
                       fileHamProb +=  hamProbabilityMap.get(s);
                   }
               }
               line = reader.readLine();
           }
           fileSpamProb += spamProb;
           fileHamProb += hamProb;

           if(Double.compare(fileSpamProb,fileHamProb) > 0)
               return "spam";
           else
               return "ham";

       }catch (Exception e){
           System.out.println("Exception in findClass:"+ e);
           return null;
       }
    }
}
