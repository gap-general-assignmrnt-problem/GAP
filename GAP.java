package org.cloudbus.cloudsim.examples.network.datacenter;


import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmStateHistoryEntry;
import org.cloudbus.cloudsim.Datacenter.MigInfo;
import org.cloudbus.cloudsim.Datacenter.VMtoCloudletEnergy;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GAP {
	public static int[][] xij;
	public static double LowerBound;

   GAP() {
        System.out.println("GAP constructor");
    }
    void GAP() {
        System.out.println("GAP mathod");
    }
    public static void main(String[] args) {
    	long startTime = System.nanoTime();
    	long endTime   = System.nanoTime();
    	
    	WIJList WIJList = new WIJList();
    	WIJList ProfitList = new WIJList();
    	EffList EffList = new EffList();
    	CapcityList CapList = new CapcityList();
    	
    	
    	// Phase 0 Read n(Number of Item) and m(Number of Knapsack/bin/VM)
    	int n = 0;
    	int m = 0;
    	
    	// Phase 1 initial Weight&Profit&Capacity&Eff
    	// The name of the file to open.
        String fileName = "D:\\a05100";

        // This will reference one line at a time
        String line = null;
        String line1 = null;
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);
            int y = 0;
            int x = 0;
            
            line = bufferedReader.readLine();
            Pattern pattern = Pattern.compile("(\\d+)");
			Matcher matcher = pattern.matcher(line);
			while(matcher.find())
			{	if (y==0){
					m = Integer.valueOf(matcher.group());
				}else if (y ==1 ){
					n = Integer.valueOf(matcher.group());
				}
			y++;
			}
			y=0;
			x = 0;
			int z = 0;
            ////
			line = bufferedReader.readLine();
            while((line1 = bufferedReader.readLine()) != null) {
    			Pattern pattern1 = Pattern.compile("(\\d+)"); 
    			Matcher matcher1 = pattern1.matcher(line);
    			
    				while(matcher1.find())
        			{	
    					int temp;
        				if(z==0){
        					temp = Integer.valueOf(matcher1.group());
        					ProfitList.Add(new WIJ(x,y,temp));
        				}else {
        					temp = Integer.valueOf(matcher1.group());
        					WIJList.Add(new WIJ(x,y,temp));
        				}
        			y++;
        			}
    				if(!(y<n)){
    					y = 0;
    					if(z<1){
    						z++;
    					}else{
    						z=0;
    						x++;
    					}
    				}
        		line = line1;	        			
    			}
            	Pattern pattern1 = Pattern.compile("(\\d+)"); 
            	Matcher matcher1 = pattern1.matcher(line);
            	x=0;
            	while(matcher1.find())
            		{	
            		int temp;
					temp = Integer.valueOf(matcher1.group());
					CapList.Add(new Capcity(x,temp));
					CapList.ReminingCap.add(new Capcity(x,temp));
					x++;
            	}
    			System.out.println();
            			
    		}
    		catch(FileNotFoundException ex) {
    			System.out.println(
    					"Unable to open file '" + 
    							fileName + "'");                
    		}
    		catch(IOException ex) {
    			System.out.println(
    					"Error reading file '" 
    							+ fileName + "'");                  
    			// Or we could just do this: 
    			// ex.printStackTrace();
    		}
        Log.printLine("n: "+n+" , m: "+m);
        double AveP =0.0;
        for(int i=0; i < m;i++){
        	for(int j=0;j<n;j++){
            	Log.printLine("P ("+i+" ,"+j+"): "+ ProfitList.get(i, j));
            	//AveP = AveP+ProfitList.get(i, j);
        	}
         }
       	Log.printLine();
       	for(int j =0; j< n;j++){
       		AveP = 0.0;
       		for(int i=0; i< m; i++){
       			AveP = AveP+ProfitList.get(i, j);
       			//Log.print("Profit: "+ AveP);
       		}
       		AveP = AveP/m;
       		//Log.printLine("Ave("+j+"): "+ AveP);
       		AveP = 1/AveP;
       		AveP = AveP*1000;
       		for(int i=0; i< m; i++){
       			ProfitList.Set(i, j, AveP);
       		}
       	}
    	for(int j=0;j<n;j++){
        	Log.printLine("P ("+0+" ,"+j+"): "+ ProfitList.get(0, j));
        	
    	}
       	
       	
        for(int i=0; i < m;i++){
        	for(int j=0;j<n;j++){
            	Log.printLine("W ("+i+" ,"+j+"): "+ WIJList.get(i, j));

        	}
        }
        
        // calculate CI
        double SAveW =0.0;
        for(int i=0; i<n; i++){
        	double SW =0;
        	for(int j =0; j<m; j++){
        		SW = SW + WIJList.get(j, i);
        	}
        	Log.print(SW+" ,");
        	SAveW = SAveW + (SW/m);
        }
        double SC = 0.0;
        Log.printLine();
        Log.printLine("SAveW: "+ SAveW);
        double x = (SAveW /(m));
        for(int i=0; i< m; i++){
        	
        	//double randomDouble = Math.random();
    		//randomDouble = randomDouble * (x/10)*7 + 3*(x/10)+1;
        	Log.printLine("x: "+ x);
        	Random randomGenerator = new Random();
    		int randomInt = (int) (randomGenerator.nextInt( (int) (.6*x)) +(.2*x)+1);
        	CapList.Set(i, randomInt); 
        	SC = SC + CapList.Get(i).C;
        }
        
       	Log.printLine();
        for(int i=0; i < m;i++){
        	Log.printLine("C ("+i+"): "+CapList.Get(i).C );
	
        }
        pressAnyKeyToContinue();
       	Log.printLine();
       	// Initialization of Eff
       	for(int i=0; i < m;i++){
        	for(int j=0;j<n;j++){
        		EffList.Add(new Eff(i,j,(ProfitList.get(i, j)/ WIJList.get(i, j))));
        		//Log.printLine("Eff ("+i+" ,"+j+"): "+ EffList.get(i, j));
        	}
        }
       	EffList.sort();
     	for(int i=0; i < EffList.EffList.size();i++){
        		Log.printLine(EffList.EffList.get(i));
            }
       	
       	
       // start of algorithm
     	//Initiation
       int Index =0;
       double profit =0.0;
       double weight = 0.0;
       LowerBound =0;
       xij = new int[m][n];
       for(int i =0; i < m; i++){
    	   for(int j =0; j < n; j++){
    		   xij[i][j]=0;
    	   }
       }
       double [] y = new double [n];
       for(int i=0;i<n;i++){
     	  y[i] = 0;
       }
      
       
    GAPBB(m,n,Index,y,LowerBound,profit,weight,CapList,EffList,WIJList,ProfitList,xij);
       	Log.printLine("Exact result: "+ LowerBound);
       	for(int i =0; i < m; i++){
       		Log.print("i("+i+"): ");
     	   for(int j =0; j < n; j++){
     	       	Log.print(xij[i][j]+" ,");
     	   }
     	  Log.printLine();
        }
       	long totalTime = endTime - startTime;
    	System.out.println("Total time: "+totalTime);
    	
    }
  private static void GAPBB(int m,int n,int index, double[] yy, double profit, double weight, double LBound, CapcityList capList, EffList effList, WIJList wIJList, WIJList profitList, int[][] xijFinal) {
	  double[] reminingcap = new double[m];
	  double[] reminingcapl = new double[m];

	  for (int i=0; i<m;i++ ){
		  reminingcap[i]= capList.CapcityList.get(i).C;
		  reminingcapl[i]= capList.CapcityList.get(i).C;

	  }
	  //ArrayList<Capcity> reminingCapl ;
	  //reminingCapl = new ArrayList<Capcity> (capList.ReminingCap);
      double [][] yij = new double[m][n];
      double [][] uxij = new double[m][n];

      for(int i =0; i < m; i++){
   	   for(int j =0; j < n; j++){
   		   yij[i][j]=0;
   		   uxij[i][j]=xijFinal[i][j];
   	   }
      }
      //Log.printLine("("+effList.EffList.get(index).I+" , "+effList.EffList.get(index).J+")"+" index:"+index+" profit: "+ profit);
      //pressAnyKeyToContinue();

      //phase 1: UP calculation (LPRelaxation) 
      double UProfit =0.0;
      double [] yj = new double [n];
      for(int i=0;i<n;i++){
    	  yj[i] = yy[i];
      }
      for(int x=index; x < effList.EffList.size();x++){
    	  int i = effList.EffList.get(x).I;
		  int j = effList.EffList.get(x).J;
		  if(yj[j]<1){
    		  if(reminingcap[i] >= ((1-yj[j])*wIJList.get(i, j))){
    			  uxij[i][j]=(1-yj[j]);
    			  reminingcap[i]  = reminingcap[i]  - ((1-yj[j])*wIJList.get(i, j));
    			  yj[j] = 1;
    			  UProfit = UProfit+uxij[i][j]*profitList.get(i, j);
    		  }else { // partially assignment
    			  uxij[i][j]=(reminingcap[i]  / wIJList.get(i, j));
    			  yj[j]= yj[j]+uxij[i][j];
    			  UProfit = UProfit+uxij[i][j]*profitList.get(i, j);
    			  reminingcap[i]  =0;
    		  }
    	  }
      }
      //Log.printLine("Result of uper bound");
      /*
      for(int i =0; i < m; i++){
      	   for(int j =0; j < n; j++){
      		 Log.print("; "+uxij[i][j]);
      	   }
      	   Log.printLine();
         }
      */
      //Log.printLine("Upper bound profit: "+UProfit);
      //pressAnyKeyToContinue();
     // for(int i=0; i < m; i++){
     //     Log.printLine(reminingCap.get(i));
     //  }
      
      // Phase 2: checking upper bound and lower bound
      double [] lyj = new double[n];
      int[][] lxij = new int[m][n];
      double LBound1 =0.0;
      for (int i=0; i<m;i++ ){
		  reminingcapl[i]= capList.CapcityList.get(i).C;
      }
      for(int i =0; i < m; i++){
   	   for(int j =0; j < n; j++){
   		   lyj [j]=yy[j];
   		   lxij[i][j]=xijFinal[i][j];
   	   }
      }
      if((profit+UProfit)>LBound){
    	  // calculate LBound
    	  //Log.printLine("first if");
    	  for(int x=index; x < effList.EffList.size();x++){
    		  //Log.printLine("first for");
        	  int i = effList.EffList.get(x).I;
    		  int j = effList.EffList.get(x).J;
        	  if(lyj[j]<1){
        		 // Log.printLine("Second if");
        		  //Log.print(reminingCapl.get(i).C);
        		  if(reminingcapl[i] >= (wIJList.get(i, j))){
        			  //Log.printLine("Third if");
        			  lxij[i][j]=1;
        			  reminingcapl[i] = reminingcapl[i] - wIJList.get(i, j);
        			  lyj[j] = 1;
        			  LBound1 = LBound1+profitList.get(i, j);
        		  }
        		  }
        	  }
    	  /*
    	  Log.printLine("Result of lower bound");
          for(int i =0; i < m; i++){
          	   for(int j =0; j < n; j++){
          		 Log.print("; "+lxij[i][j]);
          	   }
          	   Log.printLine();
             }
    	  */
         // Log.printLine("Lower bound profit: "+LBound1);
          if((profit+LBound1)>LowerBound){
        	  Log.printLine("lower bound update: Profit: "+ profit+" ,LBoundl: "+LBound1+" , Totally: "+ (profit+LBound1));
        	  LowerBound = (profit+LBound1);
        	  xij=lxij;
          }
          
          }
      //Log.printLine("Profit: "+profit+" ,UProfit: "+UProfit+" Lowerbound: "+ LowerBound );
      //Log.printLine("Eff.Size: "+effList.EffList.size()+" , index: "+index);
      //pressAnyKeyToContinue();
      int i = effList.EffList.get(index).I;
	  int j = effList.EffList.get(index).J;
	  while((effList.EffList.size()>(index+1))&&(!(yy[j]<1))){
		  index++;
		  //Log.printLine("while index: "+ index);
		  i = effList.EffList.get(index).I;
		  j = effList.EffList.get(index).J;
    	  //pressAnyKeyToContinue();

	  }
          if(((profit+UProfit)>LowerBound)&&(effList.EffList.size()>(index+2))){
        	  //reduction algorithm

        	  //end of reduction algorithms
        	  
    		  //Log.printLine("checking recursive calling, yy["+yy[j]+"] "+"< 1");
    		  
        	  
        	
        	  // branching selected
        	  if(capList.CapcityList.get(i).C >= wIJList.get(i, j)){// is it feasible to assign ?
        		  capList.CapcityList.get(i).C = capList.CapcityList.get(i).C-wIJList.get(i, j);
            	  profit = profit + profitList.get(i, j);
            	  xijFinal[i][j]=1;
            	  yy[j]=1;
            	  //Log.printLine("Selecting branch, Index :"+ index);
            	 // Log.printLine("("+i+","+j+"):"+ yy[j]);
            	  GAPBB(m,n,++index,yy,profit,weight,LowerBound,capList,effList,wIJList,profitList,xijFinal);
            	  
            	// branching not selected
            	  //Log.printLine("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                  //Log.printLine("profit before deduction: "+ profit+" profitList.get(i, j): "+profitList.get(i, j));
                  xijFinal[i][j]=0;
            	  yy[j]=0;
            	  capList.CapcityList.get(i).C = capList.CapcityList.get(i).C+wIJList.get(i, j);
            	  profit = profit - profitList.get(i, j);
            	  //Log.printLine("unselecting branching, Index :"+ index);
            	  //Log.printLine("("+i+","+j+"):"+ yy[j]+ "Profit: "+ profit);
            	  //pressAnyKeyToContinue();
         	   	  GAPBB(m,n,++index,yy,profit,weight,LowerBound,capList,effList,wIJList,profitList,xijFinal);
         	   	  
        	  } else {
        	  
        	// branching not selected
        	  //Log.printLine("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
              //Log.printLine("profit before deduction: "+ profit+" profitList.get(i, j): "+profitList.get(i, j));
              xijFinal[i][j]=0;
        	  yy[j]=0;
        	  //Log.printLine("unselecting branching, Index :"+ index);
        	  //Log.printLine("("+i+","+j+"):"+ yy[j]+ "Profit: "+ profit);
        	  //pressAnyKeyToContinue();
     	   	  GAPBB(m,n,++index,yy,profit,weight,LowerBound,capList,effList,wIJList,profitList,xijFinal);
        	  }
     	   	  
          }
	}
  private static void pressAnyKeyToContinue()
  { 
         System.out.println("Press Enter key to continue...");
         try
         {
             System.in.read();
         }  
         catch(Exception e)
         {}  
  }
}







//Start of Weight (battery) class and functions WIJ
class WIJ {
	int I;
	int J;
	double W;
    public WIJ(int i1, int j1, double w1) {
        this.I = i1;
        this.J = j1;
        this.W = w1;
    }
    public WIJ(WIJ temp) {
        this.I = temp.I;
        this.J = temp.J;
        this.W = temp.W;
    }
    public WIJ() {
        
    }
    public String toString() {
        return "W("+I+" , "+J+"): "+W;
    }
}
class WIJList {
	public ArrayList<WIJ> WIJList = new ArrayList<WIJ>();
	
	public void Add(WIJ temp){
    	
		WIJList.add( new WIJ(temp));
	}
	public void Set(int i, int j,double w){
		for(int x=0;x< WIJList.size();x++){
			if((WIJList.get(x).I==i)&&(WIJList.get(x).J==j)){
				WIJList.get(x).W = w;
			}
		}
		}
	
	public double get(int i, int j){
    	
		for(int x=0;x< WIJList.size();x++){
			if((WIJList.get(x).I==i)&&(WIJList.get(x).J==j)){
				return WIJList.get(x).W;
			}
		}
		return -1;
	}
	public WIJ Get(int i){
		return WIJList.get(i);
	}
}

// Finish of Weight class WIJ


//Start of Profit (Million Instruction) class and functions 
class Profit {
	int I;
	int J;
	double P;
  public Profit(int i1, int j1, double w1) {
      this.I = i1;
      this.J = j1;
      this.P = w1;
  }
  public Profit(Profit temp) {
      this.I = temp.I;
      this.J = temp.J;
      this.P = temp.P;
  }
  public Profit() {
      
  }
  public String toString() {
      return "P("+I+" , "+J+"): "+P;
  }
}
class ProfitList {
	public ArrayList<Profit> ProfitList = new ArrayList<Profit>();
	
	public void Add(Profit temp){
  	
		ProfitList.add( new Profit(temp));
	}
	public double get(int i, int j){
    	
		for(int x=0;x< ProfitList.size();x++){
			if((ProfitList.get(x).I==i)&&(ProfitList.get(x).J==j)){
				return ProfitList.get(x).P;
			}
		}
		return -1;
	}
	public Profit Get(int i){
		return ProfitList.get(i);
	}
}

//Finish of Profit (Million Instruction) class and functions


//Start of Efficiency (Profit / Weight) class and functions 
class Eff {
	int I;
	int J;
	double eff;
public Eff(int i1, int j1, double w1) {
    this.I = i1;
    this.J = j1;
    this.eff = w1;
}
public Eff(Eff temp) {
    this.I = temp.I;
    this.J = temp.J;
    this.eff = temp.eff;
}
public Eff() {
    
}
public String toString() {
    return "Eff("+I+" , "+J+"): "+eff;
}
}
class EffList {
	public ArrayList<Eff> EffList = new ArrayList<Eff>();
	
	public void Add(Eff temp){
	
		EffList.add( new Eff(temp));
	}
	
	public double get(int i, int j){
    	
		for(int x=0;x< EffList.size();x++){
			if((EffList.get(x).I==i)&&(EffList.get(x).J==j)){
				return EffList.get(x).eff;
			}
		}
		return -1;
	}
	public Eff Get(int i){
		return EffList.get(i);
	}
	
	public void sort (){
		Log.printLine();
		for (int i=0; i < EffList.size();i++){
			double temp =0.0;
			int index =0;
			for (int j=i; j< EffList.size();j++){
				if (EffList.get(j).eff>temp){
					temp = EffList.get(j).eff;
					index = j;					
				}
				
			}
			//swap
			Eff Efftemp = new Eff();
			Efftemp = EffList.get(i);
			EffList.set(i, new Eff(EffList.get(index)));
			EffList.set(index, new Eff(Efftemp));

			
		}
	}
}

//Finish of Efficiency (Profit / Weight) class and functions

//Start of Capacity class and functions 
class Capcity {
	int I;
	double C;
public Capcity(int i1, double w1) {
  this.I = i1;
  this.C = w1;
}
public Capcity(Capcity temp) {
  this.I = temp.I;
  this.C = temp.C;
}
public Capcity() {
  
}
public String toString() {
  return "Capcity("+I+"): "+C;
}
}
class CapcityList {
	public ArrayList<Capcity> CapcityList = new ArrayList<Capcity>();
	public ArrayList<Capcity> ReminingCap = new ArrayList<Capcity>();

	public void Add(Capcity temp){
	
		CapcityList.add( new Capcity(temp));
	}
	
	public void Set(int i, double c){
		
		CapcityList.set(i, new Capcity(i,c));
	}
	
	public Capcity Get(int i){
		return CapcityList.get(i);
	}
}

//Finish of Capacity class and functions