package com.hackathon18;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.stopwords.WordsFromFile;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.Filter;
import weka.filters.MultiFilter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class BugCrashClassifier implements IHackModel {
	private Instances trainingData;
	private MultiFilter chainedFilters;
	private StringToWordVector filter;
	private AttributeSelection attributeSelectionFilter;
	private FilteredClassifier classifier;
	private final String CLASSIFIER_TOKENIZER_DELIMITERS = " \r\n\t.,;:\"\'()?!-¿¡+*&#$%\\/=<>[]_`@" ;
	private final Integer DEFAULT_NUMBER_OF_WORDS_TO_KEEP = 1000000;
	
	private void setAttributeSelectionFilter() {
		attributeSelectionFilter = new AttributeSelection();
		
		//Initialize and set attribute finders
		//we start with an information tehoretic approach
		InfoGainAttributeEval informationGainEvaluator = new InfoGainAttributeEval();
		//Smoothing parameter for distributiing across unseen words
		informationGainEvaluator.setMissingMerge(true);
		
		//We will also set a search the algorithm to be used in searching across word vectors
		//We adopt a ranking approach here..other approaches are included for teting purposes
		Ranker ranker = new Ranker();
		//We start of by setting our threshold at 0 ...but can increment to improve model performance
		ranker.setThreshold(0);
	
		attributeSelectionFilter.setEvaluator(informationGainEvaluator);
		attributeSelectionFilter.setSearch(ranker);
	}
	
	private void createAttributeSelectionFilter() {
		chainedFilters = new MultiFilter();
		Filter[] chain = new Filter[]{filter, attributeSelectionFilter};
		chainedFilters.setFilters(chain);
		//chainedFilters.useFilter(data, chainedFilters);//an alternative approach to be done one after the other
	}
	
	private void setUpStringToWordFilter() {
		//here we chain our features for this processor
		//Max number of words and min term frequency is not enforced on a per class basis
		//but based on the document in each class
		filter.setDoNotOperateOnPerClassBasis(true);
		//we are interested in using words uniformly
		filter.setLowerCaseTokens(true);
		//we will use a simple word tokenizer
		WordTokenizer wordTokenizer = new WordTokenizer();
		wordTokenizer.setDelimiters(CLASSIFIER_TOKENIZER_DELIMITERS);
		filter.setTokenizer(wordTokenizer);	
		//We want to apply the filter on an element in the dataset
		//In this case we apply the filter on the last attribute
		filter.setAttributeIndices("last");
		
	}
	
	/**
	 * This method eliminates stopwords and might affect the performance of the model
	 * We check this during evaluation
	 * 
	 */
	private void applyStopWordsOnStringToWordVectorFilter(String stopWordsFile) {
		if(filter!= null) {
			WordsFromFile stopWordTokenizer = new WordsFromFile();
			stopWordTokenizer.setStopwords(new File(stopWordsFile));
			filter.setStopwordsHandler(stopWordTokenizer);
		}
		
	}
	
	/**
	 * 
	 * @param n
	 */
	public void setWordsToKeepForFilter() {
		if(filter != null) {
			filter.setWordsToKeep(DEFAULT_NUMBER_OF_WORDS_TO_KEEP);
		}
	}
	/**
	 * Loads the training data
	 * @param trainingDataFile
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void loadTrainingData(String trainingDataFile) throws FileNotFoundException, IOException {
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(trainingDataFile))) {
			ArffReader arffFile = new ArffReader(reader);
			trainingData = arffFile.getData();
			System.out.println("Importing training data for Bug-Crash classifier from " + trainingDataFile);
		}
	}
	
	/**
	 * 
	 * @param modelLocation
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void save(String modelLocation) throws FileNotFoundException, IOException {
		try(ObjectOutputStream outputModelStream = new ObjectOutputStream(new FileOutputStream(modelLocation))) {
			outputModelStream.writeObject(classifier);
		}
	}
	
	@Override
	public void saveModel(String modelLocation) {
		// TODO Auto-generated method stub
		try {
			save(modelLocation);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * The learning algorithm
	 */
	@Override
	public void learn() {
		//we assume the class of the dataset is the first attribute in the dataset
		trainingData.setClassIndex(0);
		
		//We must now chain our filters and preprocessors
		//We begin with a string to word vector
		filter = new StringToWordVector();
		
		//apply stopwords
		applyStopWordsOnStringToWordVectorFilter("");
		
		//set number of words to keep
		setWordsToKeepForFilter();
		
		//Chain additional attribute selection filters
//		
//		//here we chain our features for this processor
//		//Max number of words and min term frequency is not enforced on a per class basis
//		//but based on the document in each class
//		filter.setDoNotOperateOnPerClassBasis(true);
//		//we are interested in using words uniformly
//		filter.setLowerCaseTokens(true);
//		//we will use a simple word tokenizer
//		WordTokenizer wordTokenizer = new WordTokenizer();
//		wordTokenizer.setDelimiters(CLASSIFIER_TOKENIZER_DELIMITERS);
//		filter.setTokenizer(wordTokenizer);
//		//We want to apply the filter on an element in the dataset
//		//In this case we apply the filter on the last attribute
//		filter.setAttributeIndices("last");
		
		//Now we create the filteredClassifier
		classifier = new FilteredClassifier();
		//we now chain the filter to the classifier
		classifier.setFilter(chainedFilters);
		//classifier.setFilter(filter);
		//we also want to append our classifier following filtering
		//In this case, we use a NaiveBayes classifier
		classifier.setClassifier(new NaiveBayes());
	}

	@Override
	public void evaluate() {

		
	}

	@Override
	public void loadDataSet(String dataSetFileName) {
		// TODO Auto-generated method stub
		try {
			loadTrainingData(dataSetFileName);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
