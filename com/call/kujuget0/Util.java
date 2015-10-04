package com.call.kujuget0;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Util {
    
    final static String Path = "D:\\kuzhuget\\csv\\";//"C:\\work\\pic\\";

    final static String GraphPath = "C:\\work\\pic\\";

    final static  String EOL = System.getProperty("line.separator");
    
    public static double[] linearPartition(double min, double max, int num) {
        double[] result = new double[num];
        double h = (max - min)/(num-1);
        for (int i=0; i<num; i++) result[i] = min + h*i;

        return result;
    }
    
    public static double StringToDoubleSafe(String[] input, int index) {
    	if (index>=input.length) {
    		return Double.NaN;
    	} else {
    		return StringToDoubleSafe(input[index]);
    	}
    }
    
    public static double StringToDoubleSafe(String input) {
    	double result = Double.NaN;
    	try {
    		result = Double.parseDouble(input);
    	} finally {
    		return result;
    	}
    	
    }
    
    public static int IndexOfString(String[] Arr, String e) {
    	int result = -1;
    	for (int i = 0; i < Arr.length; i++)  {
    		if (Arr[i].replaceAll("\\s", "").contains(e)) {
    			return i;    			
    		}
		}
    	return result;
    }
    /*
     * Finding the average approximation before the head
     */
    public static double averageLeast(double[] y, int head, int length, int leastPoints, double point) {
    	ArrayList<Double> x = new ArrayList(); 
    	ArrayList<Double> val = new ArrayList(); 
    	for (int i =0; i < leastPoints; i++) {
			x.add((double)head+i);
			val.add(averageRange(y, head+i, head + length+i));
		}
    	double[] fun=leastSquareLinear(x, val, leastPoints);    	
    	return fun[0]*point + fun[1];
    }    

    /*
     * Finding the average approximation before the head
     */
    public static double averageLeast(ArrayList<Double> y, int head, int length, int leastPoints) {
    	ArrayList<Double> x = new ArrayList(); 
    	ArrayList<Double> val = new ArrayList(); 
    	for (int i =0; i < leastPoints; i++) {
			x.add((double)head+i);
			val.add(averageRange(y, head+i, head + length+i));
		}
    	double[] fun=leastSquareLinear(x, val, leastPoints);    	
    	return fun[0]*(head-1) + fun[1];
    }    

    /*
     * Finding the average approximation before the head
     */
    public static double averageLinear(ArrayList<Double> y, int head, int length) {
    	double y1=averageRange(y, head, head + length);
    	double y2=averageRange(y, head+1, head + length+1);
    	return linear(head, y1, head+1, y2, head-1);
    }    

    /*
     * Finding the average
     */
    public static double averageRange(double[] y, int head, int tail) {
    	double sumY=0;
    	
    	for (int i = head; i < tail+1; i++) {
    		sumY += y[i];
		}
    	return sumY/(tail-head+1);
    }    
    
    /*
     * Finding the average
     */
    public static double averageRange(ArrayList<Double> y, int head, int tail) {
    	double sumY=0;
    	
    	for (int i = head; i < tail+1; i++) {
    		sumY += y.get(i);
		}
    	return sumY/(tail-head+1);
    }    
    
    /*
     * Finding the linear function y=ax+b that is the least distanced from the n points
     */
    public static double[] leastSquareLinear(ArrayList<Double> x, ArrayList<Double> y, int n) {
    	double a=0, b=0, sumX=0, sumY=0, sumXY=0, sumX2=0;
    	
    	for (int i = 0; i < n; i++) {
    		double xval = x.get(i), yval = y.get(i);
    		sumX += xval;
    		sumY += yval;
    		sumXY += xval*yval;
    		sumX2 += xval*xval;			
		}
    	a = -(n*sumXY - sumX*sumY)/(sumX*sumX - n*sumX2);
    	b = (sumX*sumXY - sumX2*sumY)/(sumX*sumX - n*sumX2);
    	return new double[] {a,b};
    }    
    /*
     *  Given List and the index of head and tail find the y=ax+b least square approximation
     */
    public static double[] leastSquareForList(ArrayList<Double> x, ArrayList<Double> y, int head, int tail) {
    	double a=0, b=0, sumX=0, sumY=0, sumXY=0, sumX2=0;
    	int n=tail-head+1;
    	for (int i = head; i < tail+1; i++) {
    		double xval = x.get(i), yval = y.get(i);
    		sumX += xval;
    		sumY += yval;
    		sumXY += xval*yval;
    		sumX2 += xval*xval;			
		}
    	a = -(n*sumXY - sumX*sumY)/(sumX*sumX - n*sumX2);
    	b = (sumX*sumXY - sumX2*sumY)/(sumX*sumX - n*sumX2);
    	return new double[] {a,b};
    }
    /*
     *  Given List and the index of head and tail find the y=ax+b least square approximation value at given point xApprox
     */
    public static double leastSquarePredict(ArrayList<Double> x, ArrayList<Double> y, int head, int tail, double xApprox) {
    	double a=0, b=0, sumX=0, sumY=0, sumXY=0, sumX2=0;
    	int n=tail-head+1;
    	for (int i = head; i < tail+1; i++) {
    		double xval = x.get(i), yval = y.get(i);
    		sumX += xval;
    		sumY += yval;
    		sumXY += xval*yval;
    		sumX2 += xval*xval;			
		}
    	a = -(n*sumXY - sumX*sumY)/(sumX*sumX - n*sumX2);
    	b = (sumX*sumXY - sumX2*sumY)/(sumX*sumX - n*sumX2);
    	return a*xApprox + b;
    }
    /*
     *  Given List and the index of head and tail find the y=ax+b least square approximation value at given point xApprox
     */
    public static double leastSquarePredict(ArrayList<Double> x, double[] y, int head, int tail, double xApprox) {
    	double a=0, b=0, sumX=0, sumY=0, sumXY=0, sumX2=0;
    	int n=tail-head+1;
    	for (int i = head; i < tail+1; i++) {
    		double xval = x.get(i), yval = y[i];
    		sumX += xval;
    		sumY += yval;
    		sumXY += xval*yval;
    		sumX2 += xval*xval;			
		}
    	a = -(n*sumXY - sumX*sumY)/(sumX*sumX - n*sumX2);
    	b = (sumX*sumXY - sumX2*sumY)/(sumX*sumX - n*sumX2);
    	return a*xApprox + b;
    }
    
    /*
     *  Smarter version of leastSquarePredict, will use data y in [yMin;yMax] if there is no such data will retunr NaN 
     */
    public static double leastSquarePredictRange(ArrayList<Double> x, ArrayList<Double> y, int head, int tail, double yMin, double yMax, double xApprox) {
    	double a=0, b=0, sumX=0, sumY=0, sumXY=0, sumX2=0;
    	int n=tail-head+1, counter=0;
    	for (int i = head; i < tail+1; i++) if (y.get(i)>=yMin && y.get(i)<=yMax){
    		double xval = x.get(i), yval = y.get(i);
    		sumX += xval;
    		sumY += yval;
    		sumXY += xval*yval;
    		sumX2 += xval*xval;
    		counter++;
		}
    	if (counter<2) return Double.NaN;
    	a = -(n*sumXY - sumX*sumY)/(sumX*sumX - n*sumX2);
    	b = (sumX*sumXY - sumX2*sumY)/(sumX*sumX - n*sumX2);
    	return a*xApprox + b;
    }
    
    /*
    Solving a progonka for equations:
    x[i-1] + d[i] * x[i] + x[i+1] = f[i]
    x[0], x[N] are known
     */
    public static void progonka(double[] f, double[] d, double[] x, int N) {
        double[] a = new double[N+1];
        double[] b = new double[N+1];
        
        // left end coefs
        a[0] = 0; b[0] = x[0];
        for (int i=1; i<N; i++) {
            a[i] = -1.0 / (a[i - 1] + d[i]);
            b[i] = (f[i] - b[i-1]) / (a[i - 1] + d[i]);
        }
        // right end coefs
        a[N] = 0; b[N] = x[N];
        for (int i=N-1; i>0; i--) {
            x[i] = a[i] * x[i+1] + b[i];
        }
    }
    public static void writeToFile(String pFilename, StringBuffer pData) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(pFilename));
        out.write(pData.toString());
        out.flush();
        out.close();
    }
    public static StringBuffer readFromFile(String pFilename) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(pFilename));
        StringBuffer data = new StringBuffer();
        int c = 0;
        while ((c = in.read()) != -1) {
            data.append((char)c);
        }
        in.close();
        return data;
    }
    public static ArrayList<String> readStringsFromFile(String pFilename) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(pFilename));
        ArrayList<String> data = new ArrayList();
        String c = null;
        while ((c = in.readLine()) != null) {
            data.add(c);
        }
        in.close();
        return data;
    }
    public static ArrayList<String> readStringsFromFile(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        ArrayList<String> data = new ArrayList();
        String c = null;
        while ((c = in.readLine()) != null) {
            data.add(c);
        }
        in.close();
        return data;
    }

    public static String listToSrting(List<String> row) {
        String c="";
        for (String a:row) {
        	c = c+a+"\t";
        }
        return c;
    }
    public static String listToStringDelim(List<String> row) {
        String c="";
        for (String a:row) {
        	c = c+a+",";
        }
        if (row.isEmpty()) {
        	return "";
        } else {
        	return c.substring(0, c.length()-1);        	
        }
        
    }
    
    // data format is 1st string like Date,Price
    // 2nd, 3rd etc strings like 2014/09/5, 2.3 etc
    public static ArrayList<Double> readDataFromFile(String pFilename) throws IOException {
        ArrayList<Double> values = new ArrayList();
        ArrayList<String> fileStrings = new ArrayList(Util.readStringsFromFile(Util.Path + pFilename));
        for (int i=0; i<fileStrings.size(); i++) if (i>0) {
            String res = fileStrings.get(i);
            values.add(Double.parseDouble(res.split(",")[1]));
        }
        return values;
    }

    public static boolean generateTecplotFile(double[] var1, double[] var2, double[][] surface, String filename) {
        // Dump the graph of teh surface into tecplot text file
        boolean result = true;
        StringBuffer sBuffer = new StringBuffer("");
        sBuffer.append("VARIABLES = \"X\", \"Y\", \"Z\"" + EOL);
        sBuffer.append("ZONE I= " + var1.length + " J= " + var2.length + EOL);
        for (int i = 0; i < var1.length; i++) {
            for( int j = 0; j<var2.length ; j++) {
                sBuffer.append(var1[i] + " " + var2[j] + " " + surface[i][j] + EOL);
            }
        }
        try {
            writeToFile(GraphPath + filename, sBuffer);
        } catch (IOException e) {
            System.out.println(e.toString());
            result = false;
        }
        finally {
            return result;
        }
    }
    
    public static boolean generateTecplotFile(double[] var, double[] value, String filename) {
        // Dump the graph of teh surface into tecplot text file
        boolean result = true;
        StringBuffer sBuffer = new StringBuffer("");
        sBuffer.append("VARIABLES = \"X\", \"Y\"" + EOL);
        sBuffer.append("ZONE I= " + var.length + EOL);
        for (int i = 0; i < var.length; i++) {
            sBuffer.append(var[i] + " " + value[i] + EOL);
        }
        try {
            writeToFile(GraphPath + filename, sBuffer);
        } catch (IOException e) {
            System.out.println(e.toString());
            result = false;
        }
        finally {
            return result;
        }
    }

    public static boolean generateTecplotFile(ArrayList<Double> value, String filename) {
        // Dump the graph of teh surface into tecplot text file
        boolean result = true;
        StringBuffer sBuffer = new StringBuffer("");
        sBuffer.append("VARIABLES = \"X\", \"Y\"" + EOL);
        sBuffer.append("ZONE I= " + value.size() + EOL);
        for (int i = 0; i < value.size(); i++) {
            sBuffer.append((i+1) + " " + value.get(i) + EOL);
        }
        try {
            writeToFile(GraphPath + filename, sBuffer);
        } catch (IOException e) {
            System.out.println(e.toString());
            result = false;
        }
        finally {
            return result;
        }
    }

    public static void clear(double[] x)  {
        for (int i = 0; i < x.length; i++) {
            x[i] =0;
        }
    }
    public static void clear(double[][] x)  {
        int size1 = x.length;
        int size2 = x[0].length;
        for (int i = 0; i < size1; i++) {
            for (int j = 0; j < size2; j++) {
                x[i][j] = 0;
            }
        }
    }

    public static double dist2(double[] u, double[] v,  int mf, int ml) {
        double q=0;
        for (int m=mf; m<=ml; m++) {
            double qq=u[m]-v[m];
            q+=qq*qq;
        }
        return q;
    }

    public static void clear(double[][] x, int row1, int row2, int col1, int col2)  {
        for (int i = row1; i <= row2; i++) {
            for (int j = col1; j <= col2; j++) {
                x[i][j] = 0;
            }
        }
    }

    public static void scale(double[][] x, double a, int row1, int row2, int col1, int col2)  {
        for (int i = row1; i <= row2; i++) {
            for (int j = col1; j <= col2; j++) {
                x[i][j] *= a;
            }
        }
    }
    
    public static void scalePartial(double[][] x, double[][] indicator, double val, int row1, int row2, int col1, int col2) {
        for (int i = row1; i <= row2; i++) {
            for (int j = col1; j <= col2; j++) if (indicator[i][j]==0) {
                x[i][j] *= val;
            }
        }
    }

    public static double dot(double[][] x,double[][] y, int row1, int row2, int col1, int col2)  {
        double sum =0;
        for (int i = row1; i <= row2; i++) {
            for (int j = col1; j <= col2; j++) {
                sum += x[i][j]*y[i][j];
            }
        }
        return sum;
    }
    public static double dotPartial(double[][] x,double[][] y, double[][] indicator, int row1, int row2, int col1, int col2)  {
        double sum =0;
        for (int i = row1; i <= row2; i++) {
            for (int j = col1; j <= col2; j++) if (indicator[i][j]==0) {
                sum += x[i][j]*y[i][j];
            }
        }
        return sum;
    }

    public static void add(double[][] x, double[][] y, double a, int row1, int row2, int col1, int col2) {

        for (int i = row1; i <= row2; i++) {
            for (int j = col1; j <= col2; j++) {
                x[i][j] += a*y[i][j];
            }
        }
    }
    public static void addPartial(double[][] x, double[][] y, double[][] indicator, double a, int row1, int row2, int col1, int col2) {

        for (int i = row1; i <= row2; i++) {
            for (int j = col1; j <= col2; j++) if (indicator[i][j]==0) {
                x[i][j] += a*y[i][j];
            }
        }
    }

    public static void add(double[][] x, double[][] y, int row1, int row2, int col1, int col2) {

        for (int i = row1; i <= row2; i++) {
            for (int j = col1; j <= col2; j++) {
                x[i][j] += y[i][j];
            }
        }
    }
    public static void addPartial(double[][] x, double[][] y, double[][] indicator, int row1, int row2, int col1, int col2) {

        for (int i = row1; i <= row2; i++) {
            for (int j = col1; j <= col2; j++) if (indicator[i][j]==0) {
                x[i][j] += y[i][j];
            }
        }
    }
    public static void set(double[] x, double val) {

        for (int i = 0; i < x.length; i++) {
            x[i] = val;
        }
    }
    public static void setRow(double[][] x, int row, double val) {

        for (int i = 0; i < x[0].length; i++) {
            x[row][i] = val;
        }
    }
    public static void setColumn(double[][] x, int column, double val) {

        for (int i = 0; i < x.length; i++) {
            x[i][column] = val;
        }
    }
    
    public static void setPartial(double[][] x, double[][] val, double[][] indicator, int row1, int row2, int col1, int col2) {

        for (int i = row1; i <= row2; i++) 
        	for (int j = col1; j <= col2; j++) if (indicator[i][j]!=0) {
        		x[i][j] = val[i][j];        
        	}
    }

    public static double linear(double x1, double y1, double x2, double y2, double x) {
        return y1 + (x-x1)*(y2-y1)/(x2-x1);
    }

    public static double quadratic(double x1, double y1, double x2, double y2, double x3, double y3, double x) {
        return y3*(x-x1)*(x-x2)/((x3-x1)*(x3-x2)) + 
        		y2*(x-x1)*(x-x3)/((x2-x1)*(x2-x3)) +
        		y1*(x-x2)*(x-x3)/((x1-x2)*(x1-x3));
    }
    
    public static double integrate(double[] val, double step) {
    	double sum = 0;
        for (int i = 0; i < val.length-1; i++) {
            sum += val[i]+val[i+1];
        }
        
        return 0.5*sum*step; 
    }
    public static double average(double[] val) {
    	double sum = 0;
        for (int i = 0; i < val.length; i++) {
            sum += val[i];
        }
        
        return sum/val.length; 
    }
    public static double ratioChoice(double[] val, double ratio) {
//    	if (ratio<=0.0) return val[0];
//    	if (ratio>=1.0) return val[val.length-1];
//    	int index = (int)Math.round(ratio*val.length)-1;
//    	if (index>=val.length || index<0) {
//    		index=val.length/2;
//    	}
    	int index=val.length/2;//(int)Math.round(ratio*val.length)-1;
    	//index= (int)(ratio*val.length)-1;
    	if (index<0) index =0;
    	double result;    	
    	if ( !Double.isInfinite(val[index]) && !Double.isNaN(val[index]) && val[index]>=0) {
    		result = val[index];	
    	} else {
    		result = 0.5*(val[0]+val[val.length-1]);
    		//System.out.println("Approximating as " + val[index] +" will be "+result);
    		
    	}
    	//result = integrate(val, 1.0/(val.length-1));
    	return result;
    }
    
    public static double Mean(ArrayList<Double> val) {
    	double sum=0.0;
    	for (int i = 0; i < val.size(); i++) {
			sum += val.get(i);
		}
    	return sum/val.size();
    }
    
    public static double Disperse(ArrayList<Double> val) {    	
    	double sum=0;
    	for (int i = 0; i < val.size(); i++) {
			sum += val.get(i)*val.get(i);
		}
    	double ave = Util.Mean(val);    	
    	return sum/val.size() - ave*ave;
    }

}
