package com.call.kujuget0;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainPredict {	
	class MyTask implements Runnable {
		String name;
		public MyTask(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			BSPredictor bs = new BSPredictor();
			bs.SetFilePath(name);
			bs.SetName(name);
			//System.out.println(bs.linearStrategy());
			//System.out.println(bs.lastStrategy());
			bs.predict();
			System.out.println(bs.getRealMoney());
		}
	}

	static FileFilter filter = new FileFilter() {
		@Override
		public boolean accept(File pathname) {
			return pathname.getName().toLowerCase().endsWith("csv");
		}
	};

	public void doMain() throws InterruptedException {
		String fileName="OptionData";
		String fileExtension=".csv";
		int startIndex=6, endIndex=6;
		BSPredictor bs = new BSPredictor();
		File dir = new File(Util.Path);
		File[] files = dir.listFiles(filter);
		ExecutorService es = Executors.newFixedThreadPool(1);
		for (int i=0; i<files.length; i++) {
			File file = files[i];
			//String file = Util.Path + fileName + String.valueOf(i) + fileExtension;
			es.submit(new MyTask(file.toString()));
		}
		es.shutdown();
		es.awaitTermination(1, TimeUnit.DAYS);
		System.out.println("Done with upward slopped " + BSPredictor.upward);
	}
    public static void main(String[] args) throws InterruptedException {
		new MainPredict().doMain();
   }
}
