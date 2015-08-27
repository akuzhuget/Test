package com.call.kujuget0;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainPredict {	
	
	static FileFilter filter = new FileFilter() {
		@Override
		public boolean accept(File pathname) {
			return pathname.getName().toLowerCase().endsWith("csv");
		}
	};
     
    public static void main(String[] args) {
    	String fileName="OptionData";
    	String fileExtension=".csv";    	
    	int startIndex=6, endIndex=6;
    	BSPredictor bs = new BSPredictor();
    	File dir = new File(Util.Path);
    	File[] files = dir.listFiles(filter);
    	for (File file:files) {
    		//String file = Util.Path + fileName + String.valueOf(i) + fileExtension;
    		bs.SetFilePath(file.toString());
    		bs.SetName(file.toString());    		
			System.out.println(bs.predict().toStringBuffer());
		}
    	System.out.println("Done");
    }    
}