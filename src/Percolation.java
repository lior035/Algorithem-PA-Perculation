public class Percolation
{
    /** 
     * @author : Lior Asulin
     * @email : lior031@gmail.com
     * @param N 
     */
    
   private boolean [][] grid; 
   private int size;
   private WeightedQuickUnionUF percolationWquf;
   private WeightedQuickUnionUF percolationWqufNoButtom;
  
   //================================================================================
        
   public Percolation(int N)            
   {
       //part 1: creating NxN grid, with all sites blocked
       size = N;
       int row; // equals col
       
       grid = new boolean [N][];
       for (row = 0; row<N; row++)
       {
           grid[row] = new boolean [N]; // defualt value of boolean is false - meaning all sites block
       }
       
       //part 2: creating the weighted quick union uf objects at size of N^2+2
       
       percolationWquf = new WeightedQuickUnionUF((size*size)+2); // adding two place for virtual points 
       percolationWqufNoButtom = new WeightedQuickUnionUF (size*size + 1);// no bottum virtual
       
       
       this.setupVirtualPoints();
   }
   
   //============================================================================
   
   public void open(int i, int j) throws  java.lang.IndexOutOfBoundsException       
   {
        // open site (row i, column j) if it is not already
       boolean legalIndex;
       
       //validate index are legal
       legalIndex = isLegalIndex( i , j );
       if (!(legalIndex))
           throw  new java.lang.IndexOutOfBoundsException("illegal index");
      
       //if legal - open site and connect to other neighbors site that are opened
       else
       {
           if (!(this.grid[i-1][j-1]))  //this site is close and shell be opened
           {
               openAndConnectNeighbors ( i,  j);
           }                      
       }
   }
   
   //=============================================================================
   
    public boolean isOpen(int i, int j)  throws  java.lang.IndexOutOfBoundsException 
    {
       // is site (row i, column j) open?
       boolean legalIndex;
       
       legalIndex = isLegalIndex( i , j );
       
       if (!(legalIndex))
           throw  new java.lang.IndexOutOfBoundsException("illegal index");
      
       else 
           return this.grid[i-1][j-1];
    }   
    
    //=============================================================================
    public boolean isFull(int i, int j) throws java.lang.IndexOutOfBoundsException  
    {
        // is site (row i, column j) full?
       boolean legalIndex;
       int p;
       
       legalIndex = isLegalIndex( i , j );
       
       if (!(legalIndex))
           throw  new java.lang.IndexOutOfBoundsException("illegal index");
      
       else 
       {
           p = xyTo1D (i,j);
           
           if (this.grid[i-1][j-1]) //check if current cell is open
           {
               if (this.percolationWqufNoButtom.connected(0, p))
                   return true;
                
               return false;
           }
           
           //else - the cell is closed - cetenly not full
           return false;
       }
    }
    
    //============================================================================
    
   public boolean percolates()    
   {
       // does the system percolate?
       if (this.size == 1)
       {
           if (this.isOpen(1, 1))
               return true;
           return false;
       }
       
       if (this.percolationWquf.connected(0, size*size +1)) // means that top virtual connected to buttom virtual
            return true;
               
       return false;
   }
     
   //==============================================================================
   
   private boolean isLegalIndex (int i, int j)
   { 
       if ((i<1)||(i>size))
           return false;
       else if ((j<1)||(j>size))
           return false;
       return true;
   }
   
   //===============================================================================
   
    private int xyTo1D(int i, int j)
    {
        // a scheme for uniquely mapping 2D coordinates to 1D coordinates
        // assumption - both index are legal  
        
        int converted = (i-1)*size + j;
        return converted;
    }
    
    //==============================================================================
    
    private void openAndConnectNeighbors (int i, int j)
    {
        // assumption - both index are legal
      
        //openning site 
        this.grid[i-1][j-1] = true;
       
        //connect other opened neighborads:
        
        openAndConnectNeighborsHelper (i, j, i-1, j); // one cell above
        openAndConnectNeighborsHelper (i, j, i+1, j); // one cell below
        openAndConnectNeighborsHelper (i, j, i, j+1); // one cell to the right
        openAndConnectNeighborsHelper (i, j, i, j-1); // one cell to the left 
   
        // after openning and unioning all nighbors check (in case this is last row) if full - 
        //suppose yes - union with the cell below in protection line
      
  
        
    }
    
    //==============================================================================
    
     private void openAndConnectNeighborsHelper (int i, int j, int nighborI, int nighborJ)
     {         
         //return true if union occures
         
         boolean legalIndex;
         int p = xyTo1D(i,j);
         int q ;
         
        legalIndex = isLegalIndex (nighborI , nighborJ); 
       
        if (legalIndex)
        {
            // union with current nighebor only if the nighebor is not blocked
            if (this.isOpen(nighborI, nighborJ))
            {
                q = xyTo1D (nighborI, nighborJ);
                this.percolationWquf.union(p, q);
                this.percolationWqufNoButtom.union(p, q);
            }
        }
     }
     
     //=============================================================================
     
      private void setupVirtualPoints ()
      {
          //connect each virtual site to the correct row 
          int col;
          int topPoint;
          int buttomPoint; 
          
          int topVirtual = 0; // top virtual index in percolationWquf
          int buttomVirtual = (size*size) + 1; // buttom virtual index in percolationWquf
        
          //connect top virtual to all of the first row pointes in percolationWquf
          //connect buttom virtual to all of the last row pointers in percolationWquf
          
          for (col=1; col <= size; col++)
          {
             topPoint = xyTo1D(1,col);
             buttomPoint = xyTo1D(size,col); // connecting the last row to buttom virtual
             
             this.percolationWquf.union(topVirtual, topPoint);
             this.percolationWquf.union(buttomVirtual, buttomPoint);
             
             this.percolationWqufNoButtom.union(topVirtual, topPoint);
          }
      }
}     
      //==============================================================================
