package com.call.kujuget0;

import java.util.ArrayList;

public interface Predictor {
 	
	// main method for the prediction
	public DataTable predict();
	
	// set the name of the Option
	public void SetName(String name);
	
	//sets the input Data fir the prediction
	public void SetData(ArrayList<String> inputData); 
	
}
