package com.call.kujuget0;

public class Hx extends Functional {
    double coef;


    public Hx(Geometry g) {
        super(g);
        coef = g.ht/(4.0*g.hx);
    }


    public double gradient(double[][] u, double[][] g, double[][] w) {
        geometry.clear(g);
        for (int m=1; m<M; m++) {
            double[] ut=u[m], wt=w[m];
            for (int n=1; n<N; n++) {
                wt[n]=ut[n+1]-ut[n-1];
            }
        }

        for (int j=0; j<=M; j++) {
            double[] gt=g[j];
            int mf=j, ml=j+1;
            if (ml>M) ml=M;
            for (int i=0; i<=N; i++) {
                double g_ji=0;
                for (int m=mf; m<=ml; m++) {
                    double[] wm=w[m];
                    g_ji+=g(m,i,j,i)*wm[i];
                }
                gt[i]=2.0*g_ji*coef;
            }
        }

        return geometry.dot(w,w)*coef;
    }

    public double g(int m, int n, int j, int i) {
        if (m==j) {
            if (i==n+1) return 1.0;
            if (i==n-1) return -1.0;
        }
        return 0.0;
    }

}