package com.call.kujuget0;

public class Direct {

  Geometry geometry;
  double kappa;
  double hx, ht;
  int N, M;
  int NN, MM;

  public Direct(Geometry geometry) {
    this.geometry=geometry;
    hx=geometry.hx;
    ht=geometry.ht;
    kappa=hx*hx/ht;
    N=geometry.Nx-1;
    M=geometry.Nt-1;

  }

  public void solve(double sigma, double[][] c, double[][] f, double[][] u) {
      if (sigma!=0.0) {
          double coef1 = kappa/sigma, coef2 = (sigma -1.0)/sigma;
          double[] g = new double[N+1];
          double[] d = new double[N+1];
          // for k=0 initial data is already there
          for (int k=1; k<=M; k++) {
              // starting the progonka for v(j-1) + v(j+1) + d(j)v(j) = g(j)
              double[] uk = u[k-1], fk = f[k-1], ck = c[k-1], ckp = c[k];
              for (int j=1; j<N; j++) {
                  double lamda = hx*hx*ckp[j]/sigma, mu=coef2*ckp[j]/ck[j];
                  g[j] = lamda * uk[j]/ht + mu * (uk[j-1] + uk[j+1] - 2.0*uk[j]) + lamda*fk[j] ;
                  d[j] = (-2.0 + coef1 * ckp[j]);
              }
              Util.progonka(g, d, u[k], N);
          }
      } else {
          kappa = 1.0 / kappa;
          for (int k=1; k<=M; k++) {
              // starting explicit calculations
              for (int j=1; j<N; j++) {
                  double lamda = kappa/c[k-1][j];
                  u[k][j] = -lamda * (u[k - 1][j - 1] + u[k - 1][j + 1]) + (1.0 + 2.0 * lamda) * u[k - 1][j] + ht*f[k-1][j];
              };
          }

      }

  }

}