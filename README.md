# Text Classification

Implemented the multinomial Naive Bayes algorithm and MCAP Logistic Regression for classifying the text files as spam or non spam messgaes. <br />

The training data set is reduced in two ways: <br />
1. Unfiltered data set: <br />
- A dicitonary is created which contains all the words in all spam and ham files, excluding special symbols and numbers. <br />
- All special symbols and numbers are removed from all the documents. <br />


2. Filtered data set: <br />
- A dictionary contains all the words in all spam and ham files, excluding special symbols, numbers and all the stop words (which does'nt help in the clasification) such as "the" "of" and "for" from all documents. <br />
- All special symbols, numbers and stop words are removed from all the documents. <br />

Trained multinomial Naive Bayes with add-one Laplace smoothing using each of the unfiltered and filtered data sets. Applied the algorithm on the test data set and reported accuracies for both unfiltered and filtered data sets. <br />

MCAP Logistic Regression algorithm is implemented with L2 regularization and using gradient ascent to estimate the weights of all the words in the dictionary. Applied the algorthim on test data set and reported accuracies for both unfiltered and filtered data sets. <br />

## Datasets
The data set is divided into two sets: training set and test set. Each set has two directories: spam and ham. All files in the spam folders are spam messages and all files in the ham folder are legitimate (non spam) messages. 

## Getting Started:
Step 1:  Change the directory to the src folder
```
cd TexClassification/src
```
Step 2:  Compile Main.java file
```
javac Main.java
```
Step 3:  Run Main.java using the below parameters as command line arguments <br />
- eta: Learning Rate <br />
- lambda: Regularization parameter used in logistic regression <br /> 
- iterations: Hard limit on the number of iterations for gradient ascent <br /> 
```
java Main <eta> <lambda> <iterations>
```

## Output:
The accuracies on classifying the test directory files using: <br /> 
1. Multinomial Naive Bayes with and without filtering stop words <br /> 
2. Logistic Regression with and without filtering stop words <br /> 

## References:
Naive Baye's for Text Classification: https://nlp.stanford.edu/IR-book/pdf/13bayes.pdf <br /> 
MCAP Logistic Regression algorithm: Machine Learning by Tom Mitchell <br /> 
Stop words: http://www.ranks.nl/resources/stopwords.html <br /> 

## Prerequisites
Java Version 9.0.1
