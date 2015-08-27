package com.call.kujuget0;

import java.util.Arrays;

public class DirectTest {
	
	public static double a=0.1;
	public static final int Nt = 21, Nx = 21;
	public static final double At=0, Bt=0.01, Ax=1, Bx=2;
	
	public static void main(String[] args) {
        // Init the geometry first
        Geometry g = new Geometry(Ax, Bx, At, Bt, Nx, Nt);
        double[][] exact = new double[Nt][Nx], calc = new double[Nt][Nx], rightSide = new double[Nt][Nx], c = new double[Nt][Nx];
        double ht = g.ht, hx = g.hx;
        // Get the exact solution stored in exact
        for (int i = 0; i < Nt; i++) {
            double t = At + ht*i;
            for (int j = 0; j < Nx; j++) {
                double x = Ax + hx * j;
                c[i][j] = coefficient(t, x);
                exact[i][j] = directSolution(t, x);                
            }
        }
        for (int i = 0; i < Nt; i++) {
        	calc[i][0] = exact[i][0];
        	calc[i][Nx-1] = exact[i][Nx-1];        	
        }
        for (int i = 0; i < Nx; i++) {
        	calc[0][i] = exact[0][i];
        	calc[Nt-1][i] = exact[Nt-1][i];
        }
        
        Direct fwd = new Direct(g);
        fwd.solve(0.5, c, rightSide, calc);
        Functional L=new L(g, c, rightSide);
        System.out.println("J exact = " + L.gradient(exact, c, rightSide));
        System.out.println("dJ exact = " + g.dot(c, c));
        System.out.println("J calc = " + L.gradient(calc, c, rightSide));
        System.out.println("dJ calc = " + g.dot(c, c));
        System.out.println("dist exact and calc = " + g.dist2(calc, exact));
        
	}
	
	public static double directSolution(double t, double x) {
	       return (t+1)*(x-1.5)*(x-1.5) + t*t*Math.sin(2.0*x)*Math.sin(2.0*(x-2));
	}
    public static double coefficient(double t, double x) {
        return 2.0/(a*(t+1.0)*(t+1.0)*x*x);
    }
    public static double directSolutionDerT(double t, double x) {
        return (x-1.5)*(x-1.5) + 2.0*t*Math.sin(2.0*x)*Math.sin(2.0*(x-2));
    }
    public static double directSolutionDerXX(double t, double x) {
        return 2.0*(t+1) + 8.0*t*t*Math.cos(4.0*(x-1));
    }
    public static double equationRightSide(double t, double x) {
        return directSolutionDerT(t, x) + directSolutionDerXX(t, x) / coefficient(t,x);
    }
	
}
