package com.call.kujuget0;

public class L2t extends Functional {

    double coef;
    int nt;
    double[] val;

    public L2t(Geometry g, double[] v, int nt) {
        super(g);
        coef = g.hx;
        val = new double[N + 1];
        g.setx(val, v);
        this.nt = nt;
    }


    public double gradient(double[][] u, double[][] g, double[][] w) {
        geometry.clear(g);
        geometry.clear(w);
        double sum=0;
        for (int n = 1; n < N; n++) {
        	double diff = u[nt][n]-val[n];
        	sum += diff*diff;
        	g[nt][n] = 2.0 * diff * coef;
        }

        return sum * coef;
    }

    public double g(int m, int n, int j, int i) {
        return 0;
    }

}