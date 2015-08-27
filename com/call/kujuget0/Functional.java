package com.call.kujuget0;

public abstract class Functional {

 Geometry geometry;
 double lx, ly, kappa;
 double ht, hx, hy;
 int N, M, K;

  public Functional(Geometry geometry) {
    this.geometry=geometry;
    hx=geometry.hx;
    ht=geometry.ht;
    M = geometry.Nt-1;
    N = geometry.Nx-1;    
  }

  public abstract double gradient(double[][] u, double[][] g, double[][] w);

}