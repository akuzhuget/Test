package com.call.kujuget0;


public class ProgonkaTest {
    final static double Pi = 3.14159265359;

    public static void main(String[] args) {
        /*
        Test for the progonka method for the equation u"=2.0, u(0)=1; u(1)=1
        
         */
        
        int N=11;
        double a=0, b=1, h = (b-a)/(N-1);
        double[] u = new double[N], f= new double[N], d = new double[N], x = new double[N];
        Util.clear(u);
        Util.clear(f);
        Util.clear(d);
        for (int i = 0; i < N; i++) {
            d[i] = -2.0;
            x[i] = a+h*i;
            f[i] = 2.0*h*h;
        }
        u[0]=0.25; u[N-1] = 0.25;
        Util.progonka(f, d, u, N-1);
        // Solving by inversing the matrix
        double[][] matrix = new double[N][N], inverse = new double[N][N];
        Util.clear(matrix);
        matrix[0][0] = 1.0;
        matrix[N-1][N-1] = 1.0;
        for (int i = 1; i < N-1; i++) {
            matrix[i][i] = d[i];
            matrix[i][i-1] = 1.0;
            matrix[i][i+1] = 1.0;
        }
        Util.clear(f);
        f[0] = 0.25;
        f[N-1] = 0.25;
        inverseMatrix(matrix, inverse);
        multMatrixVector(inverse, f, d);
        for (int i = 0; i < N; i++) {
            f[i] = directSolution(x[i]);
        }
        Util.generateTecplotFile(x, f, "exact.dat");
        Util.generateTecplotFile(x, d, "calc.dat");

        assert Util.dist2(u, f, 0, N-1)<1E-14;
        System.out.println("Test passed, Solution diff " + Util.dist2(u, f, 0, N-1));
    }
    public static double directSolution(double x) {
        return (x-0.5)*(x-0.5);
    }

    public static void inverseMatrix(double[][] matrix, double[][] inverse) {
        int N = matrix.length;
        double[][] temp = new double[N][N];
        double sk, sz;
        for (int i = 0; i < N; i++) {
            inverse[i][i] = 1.0;
            for (int j = 0; j < N; j++) {
                temp[i][j] = matrix[i][j];
            }
        }
        for (int i = 0; i < N; i++) {
            if (temp[i][i] == 0) {
                System.out.println(" matrix is singular ");
                break;
            } else sk = 1.0 / temp[i][i];
            for (int j = 0; j < N; j++)
                if (i != j) {
                    sz = sk * temp[j][i];
                    for (int k = 0; k < N; k++) {
                        temp[j][k] -= sz * temp[i][k];
                        inverse[j][k] -= sz * inverse[i][k];
                    }
                }
            for (int k = 0; k < N; k++) {
                temp[i][k] *= sk;
                inverse[i][k] *= sk;
            }
        }
    }
    public static void multMatrixVector(double[][] matrix, double[] x, double[] result) {
        int N = matrix.length;
        for (int i = 0; i < N; i++) {
            double sum=0;
            for (int j = 0; j < N; j++) {
                sum+= matrix[i][j]*x[j];
            }
            result[i] = sum;
        }        
    }
        


}
