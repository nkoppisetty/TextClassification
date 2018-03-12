import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class LogisticRegression {

    private DataSet training_dataSet;
    private Map<String, Double> wordsWeightMap;
    private double lambda;
    private double eta;
    private int iterations;

    public LogisticRegression(DataSet training_dataSet, double lambda, double eta, int iterations){
        this.training_dataSet = training_dataSet;
        this.lambda = lambda;
        this.eta = eta;
        this.iterations = iterations;
    }


    public void trainLogisticRegression(){
        wordsWeightMap = new HashMap<>();
        estimateWeights();
    }

    // estimating weights using gradient ascent algorithm
    public void estimateWeights(){
        double fileClass = 1.0;
        double wordCount;
        double prob;
        double error;

        for (String word: training_dataSet.getDictionary()) {
            double r =  (Math.random() * (1 -(-1))) + (-1);
            wordsWeightMap.put(word, r);
        }

        Map<String, Double> newWeightsMap = new HashMap<>();

        for(int i = 0; i<iterations ; i++){
            for (String word: training_dataSet.getDictionary()) {
                error = 0.0;
                for(Map.Entry<String, String> entry: training_dataSet.getFileClasses().entrySet()){

                    if(entry.getValue().equals("spam")){
                        fileClass = 0.0;
                    }else {
                        fileClass = 1.0;
                    }

                    wordCount = getWordCount(entry.getKey(), word);
                    prob = getProb(entry.getKey(), entry.getValue());

                    error = error+((fileClass-prob)*wordCount);
                }

                double newWeight = wordsWeightMap.get(word) + (eta*error) -(eta*lambda*wordsWeightMap.get(word));
                newWeightsMap.put(word, newWeight);
            }
            wordsWeightMap.putAll(newWeightsMap);
        }
    }

    public double getWordCount(String file, String word){
        Map<String, Double> wordMap = training_dataSet.getFileWordsMap().get(file);
        if(wordMap.containsKey(word)){
            return wordMap.get(word);
        }else
            return 0.0;
    }

    // calculating the probability of file based on sigmoid
    public double getProb(String file, String fileClass){
        Map<String, Double> wordMap = training_dataSet.getFileWordsMap().get(file);
        double sigmoid = 0.1;

        for (Map.Entry<String, Double> entry: wordMap.entrySet()) {
            sigmoid = sigmoid + (wordsWeightMap.get(entry.getKey())*entry.getValue());
        }

        double expVal = Math.exp(sigmoid);

        double prob = expVal/(1.0 + expVal);

        if(fileClass.equals("spam")){
            return 1.0-prob;
        }else{
            return prob;
        }
    }

    // applying Logistic Regression on test spam and ham directories and returning accuracy
    public double applyLogisticRegression(File test_spam_dir, File test_ham_dir){
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

    // finding class of a file based on sigmoid value
    public String findClass(File file){
        try{
            double sigmoid = 0.1;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            Map<String, Double> wordMap = new HashMap<>();
            while(line!=null){
                for(String s : line.toLowerCase().split(" ")){
                    if(wordMap.containsKey(s)){
                        wordMap.put(s, wordMap.get(s)+1.0);
                    }else{
                        wordMap.put(s,1.0);
                    }
                }
                line = reader.readLine();
            }

            for (Map.Entry<String, Double> entry: wordMap.entrySet()) {
                if(wordsWeightMap.containsKey(entry.getKey()))
                    sigmoid = sigmoid + (wordsWeightMap.get(entry.getKey())*entry.getValue());
            }

            if(Double.compare(sigmoid,0) > 0)
                return "ham";
            else
                return "spam";

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Exception in findClass:"+ e);
            return null;
        }
    }
}
