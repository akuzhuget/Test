package com.call.kujuget0;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class BSPredictor implements Predictor{
	// pastDays is how many days in the past we use for the linear least square interpolation
    public int  Nx = 11, pastDays=2, leastPoints=2, numPredictDays=3, Nt = 1+numPredictDays*10, numIter=(Nx-1)*(Nt-1), holdDays=10;
    public double dt= 1.0/255, At=0, Bt=dt*numPredictDays, errorLevel=0.5, range=0.5, cent=0.02;
	private boolean generateTecplot=false, lastPresent=false;
	private double realMoney = 0, potentialMoney = 0;
	private int realProfit = 0, potentialProfit = 0;
	
	// file with the data
	String filePath = Util.Path + "OptionData.csv";
	// arrays to be used for calculations
	double[] askOpt, bidOpt, lastOpt, volatility, askStock, bidStock, thetaOpt;
    String[] dates;
    ArrayList<String> data=null;
    
	DataTable result;
	String name;
    // constructor
    public BSPredictor(){
    	Reset();
	}
    public void Reset() {
    	askOpt=null;
    	bidOpt=null;
    	lastOpt=null; 
    	volatility=null;
    	askStock=null; 
    	bidStock=null;
    	thetaOpt=null;
    	dates=null;
    	data=null;    	
    }
    
    @Override
    public void SetName(String name) {
    	this.name = name;
    	result = new DataTable("Data Table for the Option: " + name);
    }
    
	public void SetFilePath(String filePath) {
		if (filePath != null) {
			this.filePath = filePath;
		}
	}
	
	@Override
	public void SetData(ArrayList<String> inputData) {
		data = inputData;
	}

	public void SetGenerateTecplot(boolean generate) {
		generateTecplot = generate;
	}
	
    public void InitData(ArrayList<String> data, double[] askOpt,double[] bidOpt,double[] lastOpt,double[] volatility,double[] askStock,double[] bidStock,double[] thetaOpt, String[] dates) {    	
        // Init the data
        // check that all the needed data is there
        int amount = data.size()-1;
        String[] labelArr = data.get(0).toUpperCase().split(",");
        String filelabels = data.get(0).toUpperCase().replaceAll("\\s", "");        
        if (!filelabels.contains(FileLabel.DATE.toString())) dates= null;
        if (!filelabels.contains(FileLabel.OPTIONLASTPRICE.toString())) lastOpt= null;
        if (!filelabels.contains(FileLabel.THETA.toString())) thetaOpt= null;
        for (FileLabel label:FileLabel.values()) {        	
        	if (label.isNeededInFile()  && !filelabels.contains(label.toString())) {
        		System.out.println(" Data file doesnt contain " + label.toString() + "Exiting ... ");
        		System.exit(0);
        	}        	
        }
        lastPresent = !(lastOpt==null);
        for (int i = 0; i < amount; i++) {
        	String[] str = data.get(1+i).split(",");        	
        	askOpt[i] = Util.StringToDoubleSafe(str[Util.IndexOfString(labelArr, FileLabel.OPTIONASKPRICE.toString())]);
        	bidOpt[i] = Util.StringToDoubleSafe(str[Util.IndexOfString(labelArr, FileLabel.OPTIONBIDPRICE.toString())]);
        	askStock[i] = Util.StringToDoubleSafe(str[Util.IndexOfString(labelArr, FileLabel.STOCKASKPRICE.toString())]);
        	bidStock[i] = Util.StringToDoubleSafe(str[Util.IndexOfString(labelArr, FileLabel.STOCKBIDPRICE.toString())]);
        	volatility[i] = Util.StringToDoubleSafe(str[Util.IndexOfString(labelArr, FileLabel.IMPLIEDVOLATILITY.toString())]);
        	if (Double.isNaN(askStock[i])) askStock[i] = askStock[i+1]; 
        	if (Double.isNaN(bidStock[i])) bidStock[i] = bidStock[i+1]; 
        	if (dates!=null) dates[i] = str[Util.IndexOfString(labelArr, FileLabel.DATE.toString())];
        	if (lastOpt!=null) lastOpt[i] = Util.StringToDoubleSafe(str[Util.IndexOfString(labelArr, FileLabel.OPTIONLASTPRICE.toString())]);        	
        	if (thetaOpt!=null) thetaOpt[i] = Util.StringToDoubleSafe(str[Util.IndexOfString(labelArr, FileLabel.THETA.toString())]);        	
        }
        // bidStock < askStock
        for (int i = 0; i < amount; i++) if (askStock[i] < bidStock[i]) {
        	double temp = askStock[i];
        	askStock[i] = bidStock[i];
        	bidStock[i] = temp;
        }

    }
    private double prolongData(double[] y, int index, int indexToPredict) {
    	return y[index];
    	//return prolongData2(y, index);
    	//return Util.averageLeast(y, index, pastDays, leastPoints);
    }
    private double prolongData1(double[] y, int index, double toPredict ) {
    	//return y[index];
    	//return prolongData2(y, index);
    	return Util.linear(index, y[index], index+1, y[index+1], toPredict);
    }
    private double prolongData2(double[] y, int index, double toPredict) {
    	return Util.quadratic(index, y[index], index+1, y[index+1], index+2, y[index+2], toPredict);
    }
    private double prolongData3(double[] y, int index, double toPredict) {
    	return Util.averageLeast(y, index, pastDays, leastPoints, toPredict);
    }
    	
	private DecimalFormat df = new DecimalFormat("#.00");
	
	private String toStr(Double value) {
		return String.valueOf(value);//df.format(value);
	}
	
	@Override
	public DataTable predict() {
		
		result = new DataTable(name);
    			
		realMoney=0;
		potentialMoney=0;
		realProfit=0;
		potentialProfit=0;
        try {
        	if (data==null || data.isEmpty()) data = Util.readStringsFromFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            System.exit(0);
        }
        int amount = data.size()-1;
        askOpt = new double[amount]; bidOpt =new double[amount]; volatility =new double[amount]; askStock =new double[amount]; bidStock =new double[amount]; lastOpt =new double[amount];thetaOpt =new double[amount]; dates =new String[amount];
        InitData(data, askOpt, bidOpt, lastOpt, volatility, askStock, bidStock, thetaOpt, dates);        
        if (!lastPresent) {
    		result.AddHeaders(Arrays.asList("PREDICT DATES","ASK PRICE YESTERDAY","PRICE(PREDICT)","POTENTIAL GAIN"));
        } else {
    		result.AddHeaders(Arrays.asList("PREDICT DATES","ASK PRICE YESTERDAY","PRICE(PREDICT)","PRICE(LAST)","POTENTIAL GAIN","REAL GAIN"));
        }
        Vector<Integer> notSoldOptIndex = new Vector<Integer>();
        ArrayList<Double> error = new ArrayList<Double>();
        ArrayList<Double> profit = new ArrayList<Double>();
        
        double avError = 0;
        double[] linFunc;
        // for the averaging points
        //for (int i = numPredictDays-1; i < amount-(pastDays+1)*(leastPoints-1); i++) if (askStock[i] != bidStock[i]) {
        // for the time reverse flow
        //for (int i = numPredictDays-1; i < amount-(numPredictDays-1); i++) if (askStock[i] != bidStock[i]) {
        //for the time fwd flow
        int mod=(amount-(numPredictDays)-1)%(numPredictDays-1);
        for (int i = amount-(numPredictDays)-1; i >=numPredictDays-1; i--) if (askStock[i] != bidStock[i] && (i%(numPredictDays-1))==mod) {
        	
        	     	
        	
        
            double Ax, Bx;
            Ax = prolongData(bidStock, i, i-numPredictDays);
            Bx = prolongData(askStock, i, i-numPredictDays);
            if (Ax>Bx) {
            	double temp=Ax;
            	Ax=Bx;
            	Bx=temp;
            }
            // Init the geometry first
            Geometry g = new Geometry(0, 1, At, Bt, Nx, Nt);
            double[][] calc = new double[Nt][Nx], rightSide = new double[Nt][Nx], c = new double[Nt][Nx], ind = new double[Nt][Nx], bound = new double[Nt][Nx];
            double ratio, denom, choice, ht = (Bt-At)/(Nt-1), hx = 1.0/(Nx-1);
            Util.setRow(ind, 0, 1);
            Util.setColumn(ind, 0, 1);
            Util.setColumn(ind, Nx-1, 1);
            // c = 2.0/(x*x*vol*vol*(Bx-Ax)*(Bx-Ax))
            //double volPredict = Util.leastSquarePredict(dateIndex, volatility, i, i+pastDays, i-2);
            if (Double.isNaN(volatility[i])||Double.isNaN(volatility[i+1]) || Double.isNaN(volatility[i+2]) ) {
            	continue;
            }

            for (int j = 0; j < Nt; j++) {
            	double vol = prolongData2(volatility, i, i-1.0*numPredictDays*j/(Nt-1));
            	//double vol = Util.linear(dt, volatility[i], 0, volatility[i+1], dt+ht*j);
            	for (int k = 0; k < Nx; k++) {
                    // change of variables x -> (x-Ax)/(Bx-Ax)
                    double x = Ax + (Bx-Ax)*hx*k;                    
                    c[j][k] = 2.0*(Bx-Ax)*(Bx-Ax)/(x*x*vol*dt);					
				}
            }

            // Init the boundary first
            double askPredict = prolongData2(askOpt, i,i-numPredictDays);
            double bidPredict = prolongData2(bidOpt, i,i-numPredictDays);
            
            if (askPredict<bidPredict) {
            	double temp=askPredict;
            	askPredict=bidPredict;
            	bidPredict=askPredict;
            }

            for (int j = 0; j < Nt; j++) {
            	double now = At + ht*j;
//              bound[j][Nx-1] = Util.linear(At, askOpt[i], Bt, askPredict, now);
//              bound[j][0] = Util.linear(At, bidOpt[i], Bt, bidPredict, now);
              bound[j][Nx-1] = Util.linear(At, askOpt[i], Bt, askPredict, now);
              bound[j][0] = Util.linear(At, bidOpt[i], Bt, bidPredict, now);
            }
            for (int j = 0; j < Nx; j++) {
                bound[0][j] = Util.linear(0, bound[0][0], 1, bound[0][Nx-1], hx*j);
                //calc[Nx-1][j] = Util.linear(0, calc[Nt-1][0], 1, calc[Nt-1][Nx-1], g.interval_x[j]);
            }
            //CalcRightSide(c, bound, rightSide, Nt-1, Nx-1, ht, hx);
            double tikhon = 0.01;
            Functional L=new L(g, c, rightSide);
            Functional Ht=new Ht(g);
            Functional Hx=new Hx(g);
            Functional Hxx=new Hxx(g);
            Functional L2=new L2(g);

            Functional J=new J(g,new Functional[]{L,L2,Ht,Hx,Hxx},
                    new double[]{1,tikhon,tikhon,tikhon,tikhon,tikhon});
            Inverse invSolver = new Inverse(g);
            //invSolver.SetLog(true);
            invSolver.iteratePartially(J, calc, numIter, 1E-12, ind,bound);            
            ratio = (lastOpt[i]-bidOpt[i])/(askOpt[i]-bidOpt[i]);
            // Check the option that we bought but didnt sell
            Vector<Integer> removeIndex = new Vector<Integer>();
            for (int j:notSoldOptIndex) if (bidOpt[i]-askOpt[j]>cent) {
        		potentialProfit += 1;
        		realProfit += 1;
                //result.AddRow(Arrays.asList(dates[i], toStr(askOpt[j]), toStr(bidOpt[i]), "Sell unsold from "+dates[j], toStr(bidOpt[i]-askOpt[j]), toStr(bidOpt[i]-askOpt[j])));
                potentialMoney += (bidOpt[i]-askOpt[j]);
                realMoney += (bidOpt[i]-askOpt[j]);
                removeIndex.add(j);
            }
            notSoldOptIndex.removeAll(removeIndex);
            for (int j=0; j<numPredictDays-1; j++) {
            	int now=(1+j)*(Nt-1)/numPredictDays;
                choice = Util.ratioChoice(calc[now], ratio);
                if (lastPresent && !Double.isNaN(lastOpt[i-1-j])) {
                    double choice1 = Math.abs(choice-lastOpt[i-1-j])/lastOpt[i-1-j];
                    if (Double.isNaN(choice1) || errorLevel<Math.abs(choice1)) {
                    	//text.append("******************************************* TOO MUCH NOISE " + dates[i-j] + Util.EOL);            	
                    	continue;
                    }                    
                }

                if (choice-askOpt[i]>cent) {
                	if (lastPresent) {
                		if (Double.isNaN(lastOpt[i-1-j])) {
                			notSoldOptIndex.add(i-1-j);
                			continue;
                		}
                		potentialProfit += 1;
                		//error.add(lastOpt[i-1-j]);
                		avError += Math.abs((lastOpt[i-1-j]-choice)/lastOpt[i-1-j]);
                		if (lastOpt[i-1-j]-askOpt[i]>0) realProfit += 1;
                        //result.AddRow(Arrays.asList(dates[i-j-1], toStr(askOpt[i]), toStr(choice), toStr(lastOpt[i-1-j]), toStr(choice-askOpt[i]), toStr(lastOpt[i-1-j]-askOpt[i])));
                        potentialMoney += (choice-askOpt[i]);
                        realMoney += (lastOpt[i-1-j]-askOpt[i]);                		
                	} else {
                    	//result.AddRow(Arrays.asList(dates[i-j-1], toStr(askOpt[i]), toStr(choice), toStr(choice-askOpt[i])));
                    	potentialMoney += (choice-askOpt[i]);                    	
                    	potentialProfit += 1;                		
                	}
                }

//                if (lastOpt[i-1-j]<bidOpt[i]) {
//                	boolean made = choice<bidOpt[i];
//                	if (made) text.append(dates[i-1-j] + " \t Bid Arbitrage "+ toStr(bidOpt[i]-lastOpt[i-1-j])+ " \t Bid "+ toStr(bidOpt[i]) + " \t Predict NextDay " + toStr(choice) +  " \t Mid NextDay " + toStr(0.5*(calc[now][0]+calc[now][Nx-1])) + " \t Possible Profit "+ made+" \t Last NextDay "+ toStr(lastOpt[i-1-j]) +Util.EOL);
//                }
//                if (lastOpt[i-1-j]>askOpt[i]) {
//                	boolean made=choice>askOpt[i];
//                	if (made) text.append(dates[i-1-j] + " \t Ask Arbitrage "+ toStr(lastOpt[i-1-j]-askOpt[i])+ " \t Ask "+ toStr(askOpt[i]) + " \t Predict NextDay " + toStr(choice) + " \t Mid NextDay " + toStr(0.5*(calc[now][0]+calc[now][Nx-1])) + " \t Possible Profit "+ made+ " \t Last NextDay "+ toStr(lastOpt[i-1-j]) +Util.EOL);
//                }
                
            }            
        }
        int i=0;
        if (Double.isNaN(bidOpt[0])) i=1;
//        for (int j:notSoldOptIndex) {
//        	if (bidOpt[i]-askOpt[j]>cent) {
//        		potentialProfit += 1;
//        		realProfit += 1;
//        	}    		
//            result.AddRow(Arrays.asList(dates[i], toStr(askOpt[j]), toStr(bidOpt[i]), "Sell unsold from "+dates[j], toStr(bidOpt[i]-askOpt[j]), toStr(bidOpt[i]-askOpt[j])));
//            potentialMoney += (bidOpt[i]-askOpt[j]);
//            realMoney += (bidOpt[i]-askOpt[j]);
//            
//        }
        
        if (lastPresent) {
            result.AddRow(Arrays.asList("AVERAGE RELATIVE ERROR", toStr(avError/potentialProfit),"","","","",""));        	
            result.AddRow(Arrays.asList("TOTAL POTENTIAL MONEY", toStr(potentialMoney), "TOTAL REAL MONEY", toStr(realMoney),"","",""));
            result.AddRow(Arrays.asList("TOTAL POTENTIAL GUESS", toStr(1.0*potentialProfit), "TOTAL SUCCESS GUESS", toStr(1.0*realProfit),"","",""));        	
        }        
        for (Integer ind:notSoldOptIndex) {
        	if (lastPresent) {
        		result.AddRow(Arrays.asList(dates[ind], "BOUGHT BUT ","DIDNT SELL OPTION","","","",""));        		
        	} else {
        		result.AddRow(Arrays.asList(dates[ind], "BOUGHT BUT ","DIDNT SELL OPTION",""));
        	}
        	
        }
        //notSoldOptIndex.add(100);
        //System.out.println("Not sold options " + notSoldOptIndex.toString());
        //Util.generateTecplotFile(error, "real6.dat");
        Reset();        	
        return result;       	
		
	}
}
