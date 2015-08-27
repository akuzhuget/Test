package com.call.kujuget0;

import java.util.Arrays;

public class InverseTest {
	
	public static double a=0.1;
	public static final int Nt = 11, Nx = 21;
	public static final double At=0, Bt=0.01, Ax=1, Bx=2;
    public static void CalcRightSide(double[][] coef,double[][] bound, double[][] res, int M, int N, double ht, double hx) {
    	
    	for (int i = 1; i < M; i++) {
    		for (int j = 1; j < N; j++) {
    			res[i][j]=(bound[i][0]-bound[i-1][0])/ht + 
    					  (bound[i][N]-bound[i-1][N])*(bound[0][j]-bound[0][0])/(ht*bound[0][N]) +
    					  bound[i][N]*(bound[0][j-1]-2.0*bound[0][j]+bound[0][j+1])/(hx*hx*coef[i][j]*bound[0][N]);				
			}			
		}
    	
    }
	
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
        //Direct fwd = new Direct(g);
        //fwd.solve(0.5, c, rightSide, exact);

        for (int i = 0; i < Nt; i++) {
        	indicator[i][0] = 1; 
        	indicator[i][Nx-1] = 1;
        }
        for (int i = 0; i < Nx; i++) {
        	indicator[0][i] = 1;
        	//indicator[Nt-1][i] = 1;
        }
        double tikhon = 0.001;
        Functional L=new L(g, c, rightSide);
        Functional Ht=new Ht(g);
        Functional Hx=new Hx(g);
        Functional Hxx=new Hxx(g);
        Functional L2=new L2(g);

        Functional J=new J(g,new Functional[]{L,L2,Ht,Hx,Hxx},
                new double[]{1,tikhon,tikhon,tikhon,tikhon});
        Inverse invSolver = new Inverse(g);
        invSolver.SetLog(true);
        invSolver.iteratePartially(J, calc, 200, 1E-4, indicator, exact);
        //invSolver.iterateNormally(J, calc, 1500, 1E-12);
        
        int check=Nt/2;
        
        System.out.println("dist " + g.dist2(calc, exact));
        System.out.println("Exact " + Arrays.toString(exact[check]));
        System.out.println("Calc " + Arrays.toString(calc[check]));
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
