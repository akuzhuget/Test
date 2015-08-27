package com.call.kujuget0;

// enum for data labels in file
public enum FileLabel {
	OPTIONBIDPRICE(true), 
	OPTIONASKPRICE(true), 
	OPTIONLASTPRICE(false), 
	IMPLIEDVOLATILITY(true), 
	STOCKASKPRICE(true), 
	STOCKBIDPRICE(true), 
	THETA(false),
	DATE(false);
	
	private FileLabel(boolean needed) {
		neededInFile = needed;
	}
	private boolean neededInFile;
	boolean isNeededInFile() {
		return neededInFile;
	}
}
