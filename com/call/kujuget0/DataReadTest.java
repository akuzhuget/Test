package com.call.kujuget0;

import java.io.IOException;
import java.util.ArrayList;

public class DataReadTest {
    public static void main(String[] args) {
        try {
            StringBuffer stringBuffer = Util.readFromFile(Util.Path + "askprice.csv");
            ArrayList<String> date = new ArrayList();
            ArrayList<Double> value = new ArrayList();
            ArrayList<String> fileStrings = new ArrayList(Util.readStringsFromFile(Util.Path + "askprice.csv"));
            for (int i=0; i<fileStrings.size(); i++) if (i>0) {
                String res = fileStrings.get(i);
                date.add(res.split(",")[0]);
                value.add(Double.parseDouble(res.split(",")[1]));
            }
            System.out.println(value);
            for (int i = 0; i < value.size(); i++) {
				System.out.println(i + " " + value.get(i));
				value.remove(i);
			}
            System.out.println(value);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
