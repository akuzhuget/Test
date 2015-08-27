package com.call.kujuget0;

import java.util.ArrayList;

public class LeastSquareLineTest {
	public static void main(String[] args) {
		double a=2, b=-10;
		int n = 5;
		ArrayList<Double> x = new ArrayList(), y = new ArrayList<Double>(); 
        for (int i = 0; i < n; i++) {
			x.add((double)i);
			y.add(a*i+b);
		}
        double[] result = Util.leastSquareLinear(x, y, n);
        System.out.println(result[0] + " " + result[1]);
        //**********************************************************
        
	}
}
