package com.call.kujuget0;

public class Inverse {
  Geometry geometry;
  int N, M, K;
  double[][] g, p, Sp, w, f;
  double pSp, g2;
  boolean log=false;
  
  public Inverse(Geometry geometry) {
    this.geometry=geometry;
    N=geometry.Nx-1;
    M=geometry.Nt-1;
    g=new double[M+1][N+1];
    p=new double[M+1][N+1];
    Sp=new double[M+1][N+1];
    f=new double[M+1][N+1];
    w=new double[M+1][N+1];
  }
  public void SetLog(boolean lvl) {
	  log=lvl;	  
  }
  public void iterateNormally(Functional F, double[][] u, int MaxIterations, double g2min) {
    int k=0;
    geometry.clear(g);
    F.gradient(g,f,w);
    geometry.scale(f,-1d);
    while (true) {
      double J=F.gradient(u,g,w);
      g2=geometry.dot(g,g);
      System.out.print(""+k+":\t J="+J+";\t g2="+g2);
      if (g2<g2min) return;
      if (k>=MaxIterations) return;
      double b= (k==0) ? 0 : geometry.dot(Sp,g)/pSp;
      geometry.scale(p,-b);
      geometry.add(p,g);
      double pg=geometry.dot(p,g);
      System.out.print(";\t pg="+pg);
      F.gradient(p,Sp,w); geometry.add(Sp,f);
      pSp=geometry.dot(p,Sp);
      System.out.print(";\t pSp="+pSp);
      double a=pg/pSp;
      geometry.add(u,p,-a);
      k++;
      System.out.println();
    }
  }

    public void iteratePartially(Functional F, double[][] u, int MaxIterations, double g2min, double[][] ind, double[][] partVal) {
        int k=0;
        double J;
        geometry.clear(g);
        geometry.clear(u);
        Util.setPartial(g, partVal, ind, 0, M, 0, N);
        Util.setPartial(u, partVal, ind, 0, M, 0, N);
        Util.setPartial(p, partVal, ind, 0, M, 0, N);
        F.gradient(g,f,w);
        Util.scalePartial(f,ind,-1d,0,M,0,N);
        while (true) {
            J=F.gradient(u,g,w);
            g2=Util.dotPartial(g,g,ind,0,M,0,N);
            //if (log) System.out.print(""+k+":\t J="+J+";\t g2="+g2);
            if (g2<g2min) break;
            if (k>=MaxIterations) break;
            double b= (k==0) ? 0 : Util.dotPartial(Sp,g,ind,0,M,0,N)/pSp;
            Util.scalePartial(p,ind,-b,0,M,0,N);
            Util.addPartial(p,g,ind,0,M,0,N);
            double pg=Util.dotPartial(p,g,ind,0,M,0,N);
            //if (log) System.out.print(";\t pg="+pg);
            F.gradient(p,Sp,w); 
            Util.addPartial(Sp,f,ind,0,M,0,N);
            pSp=Util.dotPartial(Sp,p,ind,0,M,0,N);
            //if (log) System.out.print(";\t pSp="+pSp);
            double a=pg/pSp;
            Util.addPartial(u,p,ind,-a,0,M,0,N);
            k++;
            //System.out.println();
            //if (log) System.out.println(""+k+":\t J="+J+";\t g2="+g2+";\t pg="+pg+";\t pSp="+pSp +";\t dist "+ geometry.dist2(u, partVal));
        }
        if (log) System.out.println(""+k+":\t J="+J+";\t g2="+g2+";\t pSp="+pSp +";");
    }

}