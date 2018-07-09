package com.hackathon18;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import weka.core.*;
import weka.core.FastVector;
import weka.classifiers.meta.FilteredClassifier;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

public class BugCrashLearner {
	private String contentToClassify;
	private String optionalFileToClassify;
	/**
	 * Object which stores the instance
	 */
	Instances instances;
	private FilteredClassifier classifier; //maintains our classifier
	
	/**
	 * Loads content to be classified from a file
	 * @param fileName
	 * @throws IOException 
	 */
	public void load(String fileName) throws IOException {
		try(BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
			String line;
			contentToClassify = "";
			while((line = reader.readLine()) != null) {
				contentToClassify = contentToClassify +""+ line;
			}
		}
	}
	
	public void loadModel(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
			Object tmp = in.readObject();
			classifier = (FilteredClassifier) tmp;
			System.out.println("Loaded Classifier Complete");
		}
	}
	
	public void makeInstance() {
		//create attributes
		FastVector fvNominalVal = new FastVector(2);
		fvNominalVal.addElement("spam");
		fvNominalVal.addElement("ham");
		Attribute attribute1 = new Attribute("class", fvNominalVal);
		Attribute attribute2 = new Attribute("text",(FastVector) null);
		// Create list of instances with one element
		FastVector fvWekaAttributes = new FastVector(2);
		fvWekaAttributes.addElement(attribute1);
		fvWekaAttributes.addElement(attribute2);
		instances = new Instances("Test relation", fvWekaAttributes, 1);           
		// Set class index
		instances.setClassIndex(0);
		// Create and add the instance
		DenseInstance instance = new DenseInstance(2);
		instance.setValue(attribute2, contentToClassify);
		// Another way to do it:
		// instance.setValue((Attribute)fvWekaAttributes.elementAt(1), text);
		instances.add(instance);
 		System.out.println("===== Instance created with reference dataset =====");
		System.out.println(instances);
	}
	
	/**
	 * This method performs the classification of the instance.
	 * Output is done at the command-line.
	 */
	public void classify() {
		try {
			double pred = classifier.classifyInstance(instances.instance(0));
			System.out.println("===== Classified instance =====");
			System.out.println("Class predicted: " + instances.classAttribute().value((int) pred));
		}
		catch (Exception e) {
			System.out.println("Problem found when classifying the text");
		}		
	}
	
	/**
	 * Main method. It is an example of the usage of this class.
	 * @param args Command-line arguments: fileData and fileModel.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main (String[] args) throws IOException, ClassNotFoundException {
	
		BugCrashLearner classifier;
		if (args.length < 2)
			System.out.println("Usage: java MyClassifier <fileData> <fileModel>");
		else {
			classifier = new BugCrashLearner();
			classifier.load(args[0]);
			classifier.loadModel(args[1]);
			classifier.makeInstance();
			classifier.classify();
		}
	}

}
