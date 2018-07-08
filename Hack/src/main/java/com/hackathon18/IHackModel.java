package com.hackathon18;

/**
 * All ML models must implement this interface
 * @author obiPC
 *
 */
public interface IHackModel {
	public void saveModel(String modelLocation);
	public void learn();
	public void evaluate();
	public void loadDataSet(String dataSetFileName);
}
