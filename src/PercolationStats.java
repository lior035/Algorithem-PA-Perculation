import java.lang.*;
public class PercolationStats 
{
    private double [] meanVariable;
    private final double MUL = 1.96;
    private int numExperiences; 
    
    //==================================================================================
    
   public PercolationStats(int N, int T) throws java.lang.IllegalArgumentException 
   {
       // perform T independent computational experiments on an N-by-N grid
       
       int i = 1;
       
       if ((N <= 0)||(T<=0))
           throw new java.lang.IllegalArgumentException ("Bad input");
       
       numExperiences = T;
       meanVariable = new double [T];
       
       while (i <= T)
       {
           meanVariable[i-1] = test (N);
           i++;
       }
   }
   
   //================================================================================
   
    public double mean()                
    {
        // sample mean of percolation threshold
        
        return StdStats.mean(meanVariable);
    }

    //================================================================================
    
    public double stddev()  
    {
        // sample standard deviation of percolation threshold
       
        return StdStats.stddev(meanVariable);
    }
        
    //==============================================================================
    
    public double confidenceLo() 
    {
           // returns lower bound of the 95% confidence interval
         double meanVar = this.mean();
         double stddivVar = this.stddev();
         
         if (stddivVar == Double.NaN)
             return Double.NaN;
         
         double coLoVar; 
         
         coLoVar = meanVar - ((MUL*stddivVar) / (Math.sqrt(this.numExperiences)));
         return coLoVar;
    }
     
    //===========================================================================
    
    public double confidenceHi()           
    {
           // returns upper bound of the 95% confidence interval
          
         double meanVar = this.mean();
         double stddivVar = this.stddev();
         
         if (stddivVar == Double.NaN)
             return Double.NaN;
         
         double coHighVar; 
         
         coHighVar = meanVar + ((MUL*stddivVar) / (Math.sqrt(this.numExperiences)));
         return coHighVar;
    }
     
    //============================================================================
    
    public static void main(String[] args) throws java.lang.IllegalArgumentException
    {
        // test client, get two arguments - N and T, and preforms T expiremnts of NxN grids and calculate
        //  and print all data
        
        if (args.length != 2)
            throw new java.lang.IllegalArgumentException("Wrong number of arguments");
        
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        
        PercolationStats myParcolationExp = new PercolationStats (N, T) ;
        
        System.out.println("Mean : " + myParcolationExp.mean());
        System.out.println("Stddev : " + myParcolationExp.stddev());
        System.out.println("95% confidence interval : " + myParcolationExp.confidenceLo() + " , " + myParcolationExp.confidenceHi());
    }
         
    //===========================================================================
       
    private double test (int N)
    {
        Percolation currentPercolationTest = new Percolation (N);
        int countOpenSite = 0;
        int i, j;
           
        while (!(currentPercolationTest.percolates()))
        {
            i = StdRandom.uniform(N) + 1;
            j = StdRandom.uniform(N) + 1;
                
            if (!(currentPercolationTest.isOpen(i, j)))
            {
                countOpenSite++;
                currentPercolationTest.open(i, j);
            } 
        }
           
        double result = (double)(countOpenSite) / (double) (N*N);
        return result;
    }
}