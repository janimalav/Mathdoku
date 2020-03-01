import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Grid {
    //counter for number of attempts
    int counter=0;
    private int[][] grid;
    private int dim;
    //operator hashmap
    HashMap<String,String[]> opGroup= new HashMap<String,String[]>();
    //cell group hashmap
    HashMap<String, ArrayList<int[]>> cellGroup= new HashMap<String, ArrayList<int[]>>();

    //parameterised constructor
    Grid(int dim,HashMap<String,String[]> operatorGroup,HashMap<String,ArrayList<int[]>>clGroup)
    {
        this.dim=dim;
        grid=new int[dim][dim];
        opGroup.putAll(operatorGroup);
        cellGroup.putAll(clGroup);
    }

    //getter for grid
    public int[][] getGrid() {
        return grid;
    }

    //getter for dimension
    public int getDim() {
        return dim;
    }

    /*This method will initialize all the cell of grid to 0
    * and also it will assign the value of = operator to the grid
    * After that it will call the solveMathDoku method*/
    boolean init()
    {
        //initialising grid with 0
        for(int i=0;i<dim;i++)
        {
            for (int j=0;j<dim;j++)
            {
                grid[i][j]=0;
            }

        }

        //all the equal sign operators will be assigned with the numbers
         for(String key:cellGroup.keySet())
        {
         if(cellGroup.get(key).size()==1)
         {
             int[] x=cellGroup.get(key).get(0);
             int i=x[0];
             int j=x[1];
             try{grid[i][j]=Integer.parseInt(opGroup.get(key)[0]);
             }
             catch (Exception e){return false;}
         }
        }

        //calling solve method
        if(solveMathDoku(grid))
        {
            return true;
        }
        else {
            return false;
        }
    }

    /*Method for printing the grid*/
    void printGrid()
    {
        for(int i=0;i<dim;i++)
        {
            for (int j=0;j<dim;j++)
            {
                System.out.print(grid[i][j]);
                System.out.print("  ");
            }
            System.out.println();
        }
    }

    /*method to check if the number exist in the row or not*/
    private ArrayList<Integer> isInRow(int row)
    {
        ArrayList<Integer> rowExist= new ArrayList<Integer>();
        ArrayList<Integer> rowMissing= new ArrayList<Integer>();
        //loop to find the existing number in row
        for(int i=0;i<dim;i++)
        {
            if(grid[row][i]!=0)
            {
               rowExist.add(grid[row][i]);
            }
        }
        //loop will make all the missing number in list
        for(int i=1;i<=dim;i++)
        {
            if(!rowExist.contains(i))
            {
                rowMissing.add(i);
            }
        }
        return rowMissing;
    }

    /*method to check if the number exist in the column or not*/
    private ArrayList<Integer> isInCol(int col)
    {
        ArrayList<Integer> colExist= new ArrayList<Integer>();
        ArrayList<Integer> colMissing= new ArrayList<Integer>();
        //loop to find the existing number in column
        for(int i=0;i<dim;i++)
        {
            if(grid[i][col]!=0)
            {
                colExist.add(grid[i][col]);
            }
        }
        //loop will make all the missing number in list
        for(int i=1;i<=dim;i++)
        {
            if(!colExist.contains(i))
            {
                colMissing.add(i);
            }
        }
        return colMissing;
    }

    /*This ,method will return the arraylist of numbers which can be entered after checking the row and column*/
    private ArrayList<Integer> canEnter(int row, int col)
    {
       //all the remaining numbers in column and row will be collected using two methods
       ArrayList<Integer> columns= isInCol(col);
       ArrayList<Integer> rows= isInRow(row);

       ArrayList<Integer> common= new ArrayList<Integer>();
       for(int x:columns)
       {
           if(rows.contains(x))
           {
               common.add(x);
           }
       }
       return common;
    }

    /*This method will return minimun group and which is still to be filled with value*/
    private ArrayList<int[]> solveGroup(int[][] matrix) {
        int min=Integer.MAX_VALUE;
        ArrayList<int[]> cellNumbers = new ArrayList<int[]>();
        //loop will make a list of index of group having lowest cell
        for(String key:cellGroup.keySet())
        {
            if(cellGroup.get(key).size()<min)
            {
                for(int[] positions : cellGroup.get(key))
                {
                    if(matrix[positions[0]][positions[1]]==0)
                    {
                        cellNumbers = cellGroup.get(key);
                        min = cellNumbers.size();
                        break;
                    }
                }
            }
        }
        return cellNumbers;
    }

    /*This is the important method for solving the puzzle and it will be called recursively
     This method will solve the puzzle group wise starting from the smallest group
      After the group is assigned with some value it will check the operator and the result of group
       If the numbers satisfy the need it will be stored in grid */
     public boolean solveMathDoku(int[][] matrix)
     {
        //collect the data of particular cell into list using method
        ArrayList<int[]> cellNumber=solveGroup(matrix);
        if(cellNumber.size()==0)
        {
            return true;
        }

        for(int[] x:cellNumber)
        {
            int row=x[0];
            int col=x[1];

                if(matrix[row][col]==0)
                {
                    //entering the possible values to the group
                    for(int possible : canEnter(row,col))
                    {

                        matrix[row][col] = possible;

                        //checking whether the values entered servers the constrain or not by calling the method
                        if(checkCurrentMatrix(matrix))
                        {

                            //copying the group and whole matrix if the value servers the constrain
                            int[][] temp = new int[dim][dim];
                            for(int r=0;r<dim;r++) {
                                temp[r] = Arrays.copyOf(matrix[r], dim);
                            }

                            if(solveMathDoku(matrix)) {
                                return true;
                            }
                            counter++;
                            //if the value doesn't satisfy the constrain it will be set to initial value
                            for(int r=0;r<dim;r++)
                            {   matrix[r] = Arrays.copyOf(temp[r],dim);}
                        }
                        matrix[row][col] =0;
                    }
                    return false;
                }
        }
        return true;
    }

    /*The method will go group wise and will apply to operation if the group is completely filled with the number
    * It will return false if the group is empty
    * and will return true is the given numbers satisfies the operation*/
    private boolean checkCurrentMatrix(int[][] matrix)
    {
        int max=2;
        //getting the max size amongst all groups
        for(String key:cellGroup.keySet()) {
            if (cellGroup.get(key).size() > max) {
                max = cellGroup.get(key).size();
            }
        }
        //loop will find the filled group and operate it according to constrains and if the constrain is satisfied using that value will return true
        for(int i=2;i<=max;i++)
        {
            for(String key:cellGroup.keySet())
            {
                if(cellGroup.get(key).size()==i)
                {
                    ArrayList<Integer> cellNumbers=new ArrayList<Integer>();
                   int result;
                    try {
                       result=Integer.parseInt(opGroup.get(key)[0]);
                   }
                   catch (Exception e){
                       return false;
                   }
                    char operator=opGroup.get(key)[1].charAt(0);

                    for(int j=0;j<i;j++)
                    {
                        int[] y=cellGroup.get(key).get(j);
                        int row=y[0];
                        int column=y[1];
                        cellNumbers.add(matrix[row][column]);
                    }
                    if(!checkGroupExist(cellNumbers))
                    {
                        break;
                    }

                    //switch will work according to the given operator of the group and will call the method accordingly
                    switch (operator)
                    { case '+':
                        if(!add(result,cellNumbers))
                        {
                            return false;
                        }
                        break;
                        case '-':
                        case '–':
                            if(!sub(result,cellNumbers))
                            {
                                return false;
                            }
                            break;
                        case '*':
                            if(!mul(result,cellNumbers))
                            {
                                return false;
                            }
                            break;
                        case '/':
                            if(!div(result,cellNumbers))
                            {
                                return false;
                            }
                            break;
                        default:
                            return false;
                    }
                }
            }
        }
        return true;
    }

    /*This will check if the group has any unassigned value or not*/
    private boolean checkGroupExist(ArrayList<Integer> cellNumbers)
    {
        for(int x:cellNumbers)
        {
            if(x==0)
            {
                return false;
            }
        }
        return true;
    }
    //method to add numbers of group
    private boolean add(int result,ArrayList<Integer> numbers)
    {
        int sum=0;
        for(int x:numbers)
        {
            sum=sum+x;
        }
        if(sum==result)
        {
            return true;
        }
        return false;
    }
    //method to subtracting numbers of group
    private boolean sub(int result,ArrayList<Integer> numbers)
    {
        Collections.sort(numbers,Collections.reverseOrder());
        int sub=numbers.get(0);
        for(int i=1;i<numbers.size();i++)
        {
            sub=sub-numbers.get(i);
        }
        if(result==sub)
        {
            return true;
        }
        return false;
    }
    //method to multiplication numbers of group
    private boolean mul(int result,ArrayList<Integer> numbers)
    {
        int mul=1;
        for(int x:numbers)
        {
            mul=mul*x;
        }
        if(result==mul)
        {
            return true;
        }
        return false;
    }
    //method to division numbers of group
    private boolean div(int result,ArrayList<Integer> numbers)
    {
        Collections.sort(numbers,Collections.reverseOrder());
        int div=numbers.get(0);
        for(int i=1;i<numbers.size();i++)
        {
            div=div/numbers.get(i);
        }
        if(result==div)
        {
            return true;
        }
        return false;
    }
}
