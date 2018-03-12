import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class DataSet {
    private Set<String> dictionary;
    private Set<String> stopWords;
    private Map<String, Double> spamMap;
    private Map<String, Double> hamMap;
    private double totalSpam;
    private double totalHam;

    //file to class map
    private Map<String, String> fileClasses;

    //file to words in file map
    private Map<String, Map<String, Double>> fileWordsMap;


    public DataSet(File spam_dir, File ham_dir, File stopWords_file, boolean filter_flag){
        dictionary = new HashSet<>();
        spamMap = new HashMap<String, Double>();
        hamMap = new HashMap<String, Double>();
        fileClasses = new HashMap<>();
        fileWordsMap = new HashMap<>();
        addData(spam_dir, "spam");
        addData(ham_dir, "ham");
        if(filter_flag)
            filterData(stopWords_file);
    }


    // adding all words in all the files in the directory to dictionary
    // adding words in spam files to spamMap with count of word as value
    // adding words in ham files to hamMap with count of word as value
    public void addData(File dir, String type){
        try{
            BufferedReader reader;
            for(File file: dir.listFiles()){
                reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                Map<String, Double> fileMap = new HashMap<>();

                while (line != null) {
                    for(String s : line.toLowerCase().trim().split(" ")){
                        s = s.replaceAll("[^a-zA-Z]+", "");
                        if(!s.isEmpty()){
                            dictionary.add(s);
                            if(type.equals("spam")){
                                totalSpam++;
                                if(spamMap.containsKey(s)){
                                    spamMap.put(s, spamMap.get(s)+1.0);
                                }else{
                                    spamMap.put(s, 1.0);
                                }
                            }else if(type.equals("ham")){
                                totalHam++;
                                if(hamMap.containsKey(s)){
                                    hamMap.put(s, hamMap.get(s)+1.0);
                                }else{
                                    hamMap.put(s, 1.0);
                                }
                            }

                            if(fileMap.containsKey(s)){
                                fileMap.put(s, fileMap.get(s)+1.0);
                            }else{
                                fileMap.put(s,1.0);
                            }

                        }
                    }
                    line = reader.readLine();
                }

                fileClasses.put(file.getName(), type);
                fileWordsMap.put(file.getName(), fileMap);
            }

            // removing special symbols
            String[] symbols = {"!","#","%","^","&","*","(",")","!", ":",".","{","}", "[","]",">","<","?","/", "*","~", "@"};
            for(String symbol: symbols){
                remove(symbol);
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Exception in addData: "+ e);
        }
    }

    // removing stop words from all maps and dictionary
    public void filterData(File stopWords_file){
        try{
            stopWords = new HashSet<String>();
            BufferedReader reader = new BufferedReader(new FileReader(stopWords_file));
            String line = reader.readLine();
            while(line!=null){
                stopWords.add(line);
                line = reader.readLine();
            }

            for(String word : stopWords){
                remove(word);
            }
        }catch (Exception e){
            System.out.println("Exception in filterDictionary: "+ e);
        }
    }

    public void remove(String str){
        if(dictionary.contains(str)){
            dictionary.remove(str);
        }

        if(spamMap.containsKey(str) ){
            spamMap.remove(str);
        }

        if(hamMap.containsKey(str) ){
            hamMap.remove(str);
        }

        for (Map.Entry<String, Map<String, Double>> entry: fileWordsMap.entrySet()) {
            if(entry.getValue().containsKey(str))
                entry.getValue().remove(str);
        }
    }


    public Set<String> getDictionary() {
        return dictionary;
    }

    public Set<String> getStopWords() {
        return stopWords;
    }

    public Map<String, Double> getSpamMap() {
        return spamMap;
    }

    public Map<String, Double> getHamMap() {
        return hamMap;
    }

    public double getTotalSpam() {
        return totalSpam;
    }

    public double getTotalHam() {
        return totalHam;
    }

    public Map<String, String> getFileClasses() {
        return fileClasses;
    }

    public Map<String, Map<String, Double>> getFileWordsMap() {
        return fileWordsMap;
    }
}
