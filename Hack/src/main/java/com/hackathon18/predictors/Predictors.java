package com.hackathon18.predictors;

import java.util.Vector;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class Predictors {
	public static void main(String[] args) throws Exception {
		
		String modelName = "C:\\Users\\obiPC\\git\\Hack\\Hack\\TestSavedModel\\Modern_SMO.model";
		
		
		String test1 = "The System works quite well";
		String test2 = "My expense entry keeps failing";
		String test3 = "very complicated";
		
		Classifier v = (Classifier) SerializationHelper.read(modelName);
		
		FastVector fvNominalVal = new FastVector(2);
		fvNominalVal.addElement("Crash");
		fvNominalVal.addElement("Bug");
		fvNominalVal.addElement("Both");
		fvNominalVal.addElement("Other");
		
		Attribute attribute1 = new Attribute("class", fvNominalVal);
		Attribute attribute2 = new Attribute("text",(FastVector) null);
		// Create list of instances with one element
		FastVector fvWekaAttributes = new FastVector(2);
		fvWekaAttributes.addElement(attribute1);
		fvWekaAttributes.addElement(attribute2);
		Instances instances = new Instances("Test relation", fvWekaAttributes, 1);           
		// Set class index
		instances.setClassIndex(0);
		// Create and add the instance
		DenseInstance instance = new DenseInstance(2);
		instance.setValue(attribute2, test2);
		// Another way to do it:
		// instance.setValue((Attribute)fvWekaAttributes.elementAt(1), text);
		instances.add(instance);
 		System.out.println("===== Instance created with reference dataset =====");
		System.out.println(instances);
		
		double pred = v.classifyInstance(instances.instance(0));
		System.out.println("===== Classified instance =====");
		System.out.println("Class predicted: " + instances.classAttribute().value((int) pred));
		
		double[] aval = v.distributionForInstance(instances.instance(0));
		System.out.println("Double: " + aval);
		
		/**
		//predict instance class values
		Instances originalTrain= //load or create Instances to predict

		//which instance to predict class value
		int s1=0;  

		//perform your prediction
		double value=cls.classifyInstance(originalTrain.instance(s1));

		//get the name of the class value
		String prediction=originalTrain.classAttribute().value((int)value); 

		System.out.println("The predicted value of instance "+
		                    Integer.toString(s1)+
		                    ": "+prediction); 
	*/
	}
}
