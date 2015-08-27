package com.call.kujuget0;

public class L2 extends Functional {

    double coef;

    public L2(Geometry g) {
        super(g);
        coef = (g.ht*g.hx);
    }


    public double gradient(double[][] u, double[][] g, double[][] w) {
        geometry.clear(g);
        geometry.clear(w);
        for (int m=1; m<M; m++) {
            for (int n=1; n<N; n++) {
                w[m][n]=u[m][n];
            }
        }

        for (int j=1; j<M; j++) {
            for (int i=1; i<N; i++) {
                g[j][i]=2.0*u[j][i]*coef;
            }
        }

        return geometry.dot(w,w)*coef;
    }

    public double g(int m, int n, int j, int i) {
        if (m==j && i==n) return 1.0;
        return 0;
    }

}