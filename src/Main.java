import java.io.File;

public class Main {

    public static void main(String[] args) {

        if(args.length > 0){
            double eta = Double.parseDouble(args[0]);
            double lambda = Double.parseDouble(args[1]);
            int iterations = Integer.parseInt(args[2]);

            File stopWords_file = new File("../stopwords.txt");
            File train_spam_dir = new File("../train/spam");
            File train_ham_dir = new File("../train/ham");
            File test_spam_dir = new File("../test/spam");
            File test_ham_dir = new File("../test/ham");

            DataSet training_dataSet = new DataSet(train_spam_dir, train_ham_dir, null, false);
            DataSet training_dataSet_filtered = new DataSet(train_spam_dir, train_ham_dir, stopWords_file, true);

            NaiveBayes NB1 = new NaiveBayes(training_dataSet);
            NB1.trainMultinomialNB();
            double accuracy_NB1 = NB1.applyMultinomialNB(test_spam_dir, test_ham_dir);

            NaiveBayes NB2 = new NaiveBayes(training_dataSet_filtered);
            NB2.trainMultinomialNB();
            double accuracy_NB2 = NB2.applyMultinomialNB(test_spam_dir, test_ham_dir);

            System.out.println("Accuracy using Multinomial Naive Bayes (without filtering):" + accuracy_NB1);
            System.out.println("Accuracy using Multinomial Naive Bayes (with filtering):" + accuracy_NB2);

            LogisticRegression LR1 = new LogisticRegression(training_dataSet, lambda, eta, iterations);
            LR1.trainLogisticRegression();
            double accuracy_LR1 = LR1.applyLogisticRegression(test_spam_dir, test_ham_dir);

            LogisticRegression LR2 = new LogisticRegression(training_dataSet_filtered, lambda, eta, iterations);
            LR2.trainLogisticRegression();
            double accuracy_LR2 = LR2.applyLogisticRegression(test_spam_dir, test_ham_dir);

            System.out.println("Accuracy using Logistic Regression (without filtering):" + accuracy_LR1);
            System.out.println("Accuracy using Logistic Regression (with filtering):" + accuracy_LR2);
        }else{
            System.out.println("Please provide arguments for Logistic Regression, <eta> <lambda> <iterations>");
        }
    }
}
