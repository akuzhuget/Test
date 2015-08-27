package com.call.kujuget0;

public class L extends Functional {

    double s, coef1, coef2, coef3;
    double[][] c;
    double[][] rightSide;
    
    public L(Geometry g, double[][] coef, double[][] f) {
        super(g);
        coef1 = ht / (hx * hx);
        coef2 = hx / ht;
        c = new double[M + 1][N + 1];
        g.set(c, coef);
        rightSide = new double[M + 1][N + 1];
        g.set(rightSide, f);
    }

    // Functional value is found as (w,w)
    // minimizing funcitonal of scheme u_t + u_xx/c - F
    public double gradient(double[][] u, double[][] g, double[][] w) {
        geometry.clear(w);
        geometry.clear(g);

        for (int m = 1; m < M; m++) {
            double[] ut = u[m], utp = u[m + 1], utm = u[m - 1], wt = w[m];
            for (int n = 1; n < N; n++) {
                coef3 = coef1 / c[m][n];
                wt[n] = 0.5 * (utp[n] - utm[n]) + coef3 * (ut[n - 1] + ut[n + 1] - 2.0 * ut[n]) - ht * rightSide[m][n];
            }
        }

        for (int j = 0; j <= M; j++) {
            double[] gt = g[j];
            int mf = j - 1, ml = j + 1;
            if (mf < 0) mf = 0;
            if (ml > M) ml = M;
            for (int i = 0; i <= N; i++) {
                double g_ji = 0;
                int nf = i - 1, nl = i + 1;
                if (nf < 0) nf = 0;
                if (nl > N) nl = N;
                for (int m = mf; m <= ml; m++) {
                    double[] wm = w[m];
                    for (int n = nf; n <= nl; n++) {
                        g_ji += g(m, n, j, i) * wm[n];
                    }
                }
                gt[i] = 2.0 * g_ji * coef2;
            }
        }

        return geometry.dot(w, w) * coef2;
    }

    //  g_ji = d(value) / d(u_ji)
    public double g(int m, int n, int j, int i) {
        if (j == m) {
            if (i == n) return -2.0 * coef1 / c[m][n];
            if ((i == n + 1) || (i == n - 1)) return coef1 / c[m][n];
            return 0d;
        }
        if (i == n) {
        	if (j == m + 1) return 0.5;
        	if (j == m - 1) return -0.5;
        }
        return 0d;
    }
}