package com.call.kujuget0;

public class L2diff extends Functional {

    double coef;
    int col1, col2, row1, row2;
    double[][] diff;

    public L2diff(Geometry g, double[][] v) {
        super(g);
        coef = g.ht * g.hx;
        diff = new double[g.Nt + 1][g.Nx + 1];
    }


    public double gradient(double[][] u, double[][] g, double[][] w) {
        geometry.clear(g);
        geometry.clear(w);
        for (int m = 1; m < M; m++) {
            for (int n = 1; n < N; n++) {
                w[m][n] = u[m][n] - diff[m][n];
            }
        }

        for (int j = 0; j <= M; j++) {
            for (int i = 0; i <= N; i++) {
                g[j][i] = 2.0 * (u[j][i] - diff[j][i]) * coef;
            }
        }

        return geometry.dot(w, w) * coef;
    }

    public double g(int m, int n, int j, int i) {
        if (m == j && i == n) return 1.0;
        return 0;
    }

}