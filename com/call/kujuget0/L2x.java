package com.call.kujuget0;

public class L2x extends Functional {

    double coef;
    int nx;
    double[] val;

    public L2x(Geometry g, double[] v, int nx) {
        super(g);        
        val = new double[M+1];
        g.sett(val, v);
        this.nx = nx;
    }


    public double gradient(double[][] u, double[][] g, double[][] w) {
        geometry.clear(g);
        geometry.clear(w);
        double sum=0;
        for (int m = 1; m < M; m++) {
        	double diff = u[m][nx]-val[m];
        	sum += diff*diff;
        	g[m][nx] = 2.0 * diff * ht;
        }

        return sum * ht;
    }

    public double g(int m, int n, int j, int i) {
        return 0;
    }

}