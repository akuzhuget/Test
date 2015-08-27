package com.call.kujuget0;

public class J extends Functional {

  double[][] v;
  Functional[] functionals;
  double[] gammas;
  int L;


  public J(Geometry g, Functional[] functionals, double[] gammas) {
    super(g);
    this.functionals=functionals;
    this.gammas=gammas;
    L=functionals.length;
    v=new double[g.Nt+1][g.Nx+1];
  }

  public double gradient(double[][] u, double[][] g, double[][] w){
    double q=0;
    geometry.clear(g);
    for (int l=0; l<L; l++) {
      q+=gammas[l]*functionals[l].gradient(u,v,w);
      geometry.add(g,v,gammas[l]);
    }
    return q;
  }

}