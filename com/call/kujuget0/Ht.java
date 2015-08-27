package com.call.kujuget0;

public class Ht extends Functional {
    double coef;


    public Ht(Geometry g) {
      super(g);
      coef = g.hx/(4.0*g.ht);
    }


    public double gradient(double[][] u, double[][] g, double[][] w) {
        geometry.clear(g);
        for (int m=1; m<M; m++) {
            double[] ut=u[m-1], up=u[m+1], wt=w[m];
            for (int n=1; n<N; n++) {
                wt[n]=up[n]-ut[n];
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
  // d(w) / d u_ji
  public double g(int m, int n, int j, int i) {
      if (n==i) {
          if (j==m+1) return 1.0;
          if (j==m-1) return -1.0;
      }
      return 0.0;
  }

}