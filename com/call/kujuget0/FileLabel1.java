package com.call.kujuget0;

public enum FileLabel1 {
	BIDPRICE(true), 
	ASKPRICE(true), 
	LASTPRICE(false), 
	IMPLIEDVOLATILITY(true), 
	DATE(true);
	
	private FileLabel1(boolean needed) {
		neededInFile = needed;
	}
	private boolean neededInFile;
	boolean isNeededInFile() {
		return neededInFile;
	}
}
