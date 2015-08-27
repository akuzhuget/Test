package com.call.kujuget0;

import java.io.*;

public class Geometry implements Serializable {

  int Nx, Nt;
  double Ax, Bx, At, Bt, ht, hx;
  double[] interval_t, interval_x;


  public Geometry(double Ax, double Bx, double At, double Bt, int Nx, int Nt) {
      this.Ax=Ax;
      this.Bx=Bx;
      this.At=At;
      this.Bt=Bt;
      this.Nt=Nt;
      this.Nx=Nx;
      ht=(Bt-At)/(Nt-1);
      hx=(Bx-Ax)/(Nx-1);
      interval_t = new double[Nt];
      interval_x = new double[Nx];
      for (int i = 0; i < interval_t.length; i++) {
          interval_t[i] = At + ht*i;
      }
      for (int i = 0; i < interval_x.length; i++) {
          interval_x[i] = Ax + hx*i;
      }

  }

  public double dist2(double[][] u, double[][] v) {
    return dist2(u,v,0,Nt-1,0,Nx-1);
  }

  public double dot(double[][] u, double[][] v) {
    return dot(u,v,0,Nt-1,0,Nx-1);
  }

  public void sub(double[][] u, double[][] v) {
    sub(u,v,0,Nt-1,0,Nx-1);
  }


  public void add(double[][] u, double[][] v) {
    add(u,v,0,Nt-1,0,Nx-1);
  }

  public void clear(double[][] u) {
    clear(u,0,Nt-1,0,Nx-1);
  }

  public void add(double[][] u, double[][] v, double s) {
    add(u,v,s,0,Nt-1,0,Nx-1);
  }

  public void scale(double[][] u, double s) {
    scale(u,s,0,Nt-1,0,Nx-1);
  }

    public void set(double[][] u, double[][] v) {
        set(u,v,0,Nt-1,0,Nx-1);
    }

    public void setx(double[] u, double[] v) {
        set(u,v,0,Nx-1);
    }
    public void sett(double[] u, double[] v) {
        set(u,v,0,Nt-1);
    }

  public static double dist2(double[][] u, double[][] v,  int mf, int ml, int nf, int nl) {
    double q=0;
    for (int m=mf; m<=ml; m++) {
      double[] um=u[m], vm=v[m];
      for (int n=nf; n<=nl; n++) {
        double qq=um[n]-vm[n];
        q+=qq*qq;
      }
    }
    return q;
  }


  public static double dot(double[][] u, double[][] v,
    int kf, int kl, int mf, int ml) {
    double q=0;
    for (int k=kf; k<=kl; k++) {
      double[] uk=u[k], vk=v[k];
      for (int m=mf; m<=ml; m++) q+=uk[m]*vk[m];
    }
    return q;
  }

  public static void sub(double[][] u, double[][] v, int mf, int ml, int nf, int nl) {
      for (int m=mf; m<=ml; m++) {
        double[] ukm=u[m], vkm=v[m];
        for (int n=nf; n<=nl; n++) ukm[n]-=vkm[n];
      }
  }

  public static void add(double[][] u, double[][] v, int mf, int ml, int nf, int nl) {
      for (int m=mf; m<=ml; m++) {
        double[] ukm=u[m], vkm=v[m];
        for (int n=nf; n<=nl; n++) ukm[n]+=vkm[n];
      }
  }


  public static void add(double[][] u, double[][] v, double s, int mf, int ml, int nf, int nl) {
        for (int m=mf; m<=ml; m++) {
            double[] ukm=u[m], vkm=v[m];
            for (int n=nf; n<=nl; n++) ukm[n]+=s*vkm[n];
        }
   }

  public static void scale(double[][] u, double s, int mf, int ml, int nf, int nl) {
      for (int m=mf; m<=ml; m++) {
        double[] ukm=u[m];
        for (int n=nf; n<=nl; n++) ukm[n]*=s;
      }
  }

  public static void clear(double[][] u, int mf, int ml, int nf, int nl) {
      for (int m=mf; m<=ml; m++) {
        double[] ukm=u[m];
        for (int n=nf; n<=nl; n++) ukm[n]=0;
      }
  }

    public static void set(double[][] u, double[][] v,int mf, int ml, int nf, int nl) {

        for (int m=mf; m<=ml; m++) {
            double[] ukm=u[m], vkm=v[m];
            for (int n=nf; n<=nl; n++) ukm[n]=vkm[n];
        }

    }

    public static void set(double[] u, double[] v, int nf, int nl) {
        for (int n=nf; n<=nl; n++) u[n]=v[n];
    }

  public static void add(boolean [][] chi,
    int mf, int ml, int nf, int nl, boolean value) {
    for (int m=mf; m<=ml; m++) {
      boolean[] chim=chi[m];
      for (int n=nf; n<=nl; n++) chim[n]= chim[n] | value;
    }
  }

  public static void set(boolean [][] chi,
    int mf, int ml, int nf, int nl, boolean value) {
    for (int m=mf; m<=ml; m++) {
      boolean[] chim=chi[m];
      for (int n=nf; n<=nl; n++) chim[n]= value;
    }
  }

    public static void linear(double [][] u, int m1, int n1, int m2, int n2) {
        if (m1==m2) {
            for (int n=n1; n<=n2; n++) u[m1][n] = u[m1][n1] + u[m2][n2]*(n-n1)/(n2-n1);
        } else if (n1==n2) {
            for (int m=m1; m<=m2; m++) u[m][n1] = u[m1][n1] + u[m2][n2]*(m-m1)/(m2-m1);
        }
    }


}