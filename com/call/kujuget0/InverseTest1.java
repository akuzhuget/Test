package com.call.kujuget0;

import java.util.Arrays;

public class InverseTest1 {
	
	public static double a=0.1;
	public static final int Nt = 21, Nx = 21;
	public static final double At=0, Bt=0.02, Ax=1, Bx=2;
	
	public static void main(String[] args) {
        // Init the geometry first
        Geometry g = new Geometry(Ax, Bx, At, Bt, Nx, Nt);
        System.out.println(g.ht + " " + g.hx);
        System.out.println(g.ht + " " + g.hx);
        double[][] exact = new double[Nt][Nx], calc = new double[Nt][Nx], rightSide = new double[Nt][Nx], c = new double[Nt][Nx], indicator= new double[Nt][Nx];
        double[] ux0 = new double[Nt], ux1 = new double[Nt], ut0 = new double[Nx], ut1 = new double[Nx];  
        double ht = g.ht, hx = g.hx;
        // Get the exact solution stored in exact
        for (int i = 0; i < Nt; i++) {
            double t = At + ht*i;
            for (int j = 0; j < Nx; j++) {
                double x = Ax + hx * j;
                c[i][j] = coefficient(t, x);
                exact[i][j] = directSolution(t, x);
                rightSide[i][j] = equationRightSide(t, x);
            }
        }
        Direct fwd = new Direct(g);
        fwd.solve(0, c, rightSide, exact);
        //g.set(calc, exact);
        for (int i = 0; i < Nt; i++) {
        	indicator[i][0] = 1; 
        	indicator[i][Nx-1] = 1;
        	calc[i][0] = exact[i][0];
        	calc[i][Nx-1] = exact[i][Nx-1];
        	ux0[i] = exact[i][0];
        	ux1[i] = exact[i][Nx-1];        	
        }
        for (int i = 0; i < Nx; i++) {
        	indicator[0][i] = 1;
        	indicator[Nt-1][i] = 1;
        	calc[0][i] = exact[0][i];
        	calc[Nt-1][i] = exact[Nt-1][i];
        	ut0[i] = exact[0][i];
        	ut1[i] = exact[Nt-1][i];
        }
        double tikhon = 0.01;
        Functional L=new L(g, c, rightSide);

        Functional Lx0=new L2x(g, ux0, 0);
        Functional Lx1=new L2x(g, ux1, Nx-1);
        Functional Lt0=new L2t(g, ut0, 0);
        Functional Lt1=new L2t(g, ut1, Nt-1);
        
        Functional J=new J(g,new Functional[]{L, Lx0, Lx1, Lt0},
                new double[]{1, 1, 1, 1});
        Inverse invSolver = new Inverse(g);
        
        //invSolver.iteratePartially(J, calc, 4, 1E-20, indicator, exact);
        invSolver.iterateNormally(J, calc, 550, 1E-12);
        
        int check=Nt-3;
        
        System.out.println("dist " + g.dist2(calc, exact));
        System.out.println("Exact " + Arrays.toString(exact[check]));
        System.out.println("Calc " + Arrays.toString(calc[check]));
	}
	public static double directSolution(double t, double x) {
//	       return (t+1)*(x-1.5)*(x-1.5) + t*t*Math.sin(2.0*x)*Math.sin(2.0*(x-2));
	       return t*t*Math.sin(2.0*x)*Math.sin(2.0*(x-2));
	}
 public static double coefficient(double t, double x) {
     return 2.0/(a*(t+1)*(t+1)*x*x);
 }
 public static double directSolutionDerT(double t, double x) {
//     return (x-1.5)*(x-1.5) + 2.0*t*Math.sin(2.0*x)*Math.sin(2.0*(x-2));
     return 2.0*t*Math.sin(2.0*x)*Math.sin(2.0*(x-2));

 }
 public static double directSolutionDerXX(double t, double x) {
     return 8.0*t*t*Math.cos(4.0*(x-1));
//     return 2.0*(t+1) + 8.0*t*t*Math.cos(4.0*(x-1));
 }
 public static double equationRightSide(double t, double x) {
     return directSolutionDerT(t, x) - directSolutionDerXX(t, x) / coefficient(t,x);
 }
	
}
