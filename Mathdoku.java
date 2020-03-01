import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Mathdoku {
    //This will store group id as key and all the positions of group in array list of integer array
    HashMap<String, ArrayList<int[]>> cellGroup= new HashMap<>();

    //This will store the group id as key and in string array the result and operator will be stored
    HashMap<String,String[]> operatorGroup= new HashMap<>();

    //list to store the stream data
    private List<String> list = new ArrayList<>();

    //flags for different methods
    private boolean loadPuzzle=false;
    private boolean readyToSolve=false;
    private boolean solved=false;

    //object of grid class
    Grid gd;
    char[][] grid;

    /*This method will take a bufferedReader stream from the main method
    The stream will be converted to list
    After the list is generated it will create two hashmaps using the function makeCell and makeOperatorGroup
    If both methods have enough data provided by stream it will return true
    And the function will wil return true if all the data provided will enough to load puzzle*/
    boolean loadPuzzle(BufferedReader stream)
    {
        try
        {
            int count=0;
            String line=null;
            //copying all the data of stream to list
            while((line=stream.readLine())!=null)
            {
                if(line.split(" +").length==3)
                {
                    list.add(line);
                }
                else if(line.isEmpty() || line.trim().equals(""))
                {
                }
                else {
                    count++;
                    list.add(line.replaceAll(" +",""));
                }
            }
            //removing all the empty data from list
            list.removeAll(Arrays.asList(" ",null,""));
            int dim=list.get(0).length();
            if(count!=dim)
            {
                return false;
            }
            boolean opGrp=makeOperatorGroup(dim);
            boolean makecell=makeCell(dim);

            //it will return true if the puzzle is loaded and cellgroup and operators are entered
            if(makecell && opGrp)
            {
                loadPuzzle=true;
                return true;
            }
            else {return false;}
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /*
    This method will be entering the value of index of the group in the hashmap
    If the dimensions will not match the given data it will return false
    And if the cells are made it will return true
    */
    private boolean makeCell(int dim)
    {
        try {
            //making the matrix
            grid=new char[dim][dim];
            char[][] matrix = new char[dim][dim];
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++)
                {
                    //if the dimensions are inappropriate it will return false
                    if (list.get(i).isEmpty() && list.get(i).length()!=dim) {
                        return false;
                    }
                    //entering the data into matrix
                    matrix[i][j] = list.get(i).charAt(j);
                    grid[i][j]= list.get(i).charAt(j);
                }
            }

            //making the cell group
            for (String key : operatorGroup.keySet()) {
                ArrayList<int[]> temp = new ArrayList<>();
                for (int i = 0; i < dim; i++) {
                    for (int j = 0; j < dim; j++) {
                        char t = matrix[i][j];
                        if (t == key.charAt(0)) {
                            temp.add(new int[]{i, j});
                        }
                    }
                }
                cellGroup.put(key, temp);
            }

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /*This method will get all the group and its operator and expected result respectively to the hashmap
    After successful creation of the hashmap it will return true*/
    private boolean makeOperatorGroup(int dim)
    {
        try {
            //making the operator group
            for(int i=dim;i<list.size();i++)
            {
                if (list.get(i).isEmpty())
                {
                    return false;
                }
                String[] arr = list.get(i).split(" +");
                String[] arrCopy = new String[2];
                arrCopy[0] = arr[1];
                arrCopy[1] = arr[2];
                if(operatorGroup.containsKey(arr[0]))
                {
                    return false;

                }
                operatorGroup.put(arr[0], arrCopy);
            }
            return true;
        }
        catch (Exception e)
        {
            return false;
        }

    }

    /*This method will be returning true if the puzzle is ready to solve
    First it will check weather the given operators matches the required operator or not
    Then it will check if and given group is missing in provided operators
    Other than this multiple methods are used to check the puzzle
    */
    boolean readyToSolve()
    {
        if(!loadPuzzle)
        {
            return false;
        }
        //array of the operator that will be used
        char[] match={'+','-','*','/','=','–'};
        int count=0;
        for(String key:operatorGroup.keySet())
        {
            //try parsing the result to integer
            try{Integer.parseInt(operatorGroup.get(key)[0]);}
            catch (NumberFormatException e){return false;}

            for(int i=0;i<match.length;i++)
            {
                //comparing the operators of group and given operators
                if(operatorGroup.get(key)[1].charAt(0)==match[i])
                {
                    count++;
                }
            }
        }
        if(count!=operatorGroup.size())
        {
            return false;
        }
        else if(!checkSymbols())
        {
            return false;
        }
        else if(!checkMath())
        {
            return false;
        }
        else if(!isValidGrid())
        {
            return false;
        }
        readyToSolve=true;
        return true;
    }

    /*This method will return false if the given grid is uneven that means the grid is having some dimensions error*/
    private boolean isValidGrid()
    {
        int dim=list.get(0).length();
        for (int i = 0; i < dim; i++) {
        if(list.get(i).length()!=dim)
        {
            return false;
        }
        }
        return true;
    }

    /*This method will return false if the given members of the group are not available in operator group*/
    private boolean checkSymbols()
    {
        try {
            //creating the set of cell groups
            Set<Character> smyCount = new HashSet<>();
            int count = list.get(0).length();
            for (String x : list) {
                for (int i = 0; i < list.get(i).length(); i++) {
                    if (count > 0 && i < x.length()) {
                        smyCount.add(x.charAt(i));
                    }
                }
                count--;
            }
            if (operatorGroup.size() != smyCount.size()) {
                return false;
            }
            for(String key:operatorGroup.keySet())
            {
                int check;
                try {
                   check=Integer.parseInt(operatorGroup.get(key)[0]);
                }
                catch (Exception e)
                {
                    return false;
                }
                if(check==0)
                {
                    return false;
                }
            }
        return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /*This method will be checking the maths of subtraction and division and will return false if the result given is greater than dimension*/
    private boolean checkMath()
    {
        try {
            int dim=list.get(0).length();
            for(String x: operatorGroup.keySet())
            {
                if(operatorGroup.get(x)[1].equals("-") || operatorGroup.get(x)[1].equals("–") || operatorGroup.get(x)[1].equals("/"))
                {
                    //checking if the given operator servers the condition or not
                    if((Integer.parseInt(operatorGroup.get(x)[0])>dim))
                    {
                        return false;
                    }
                }
            }
            return true;
        }
        catch (Exception e)
        {
            return false;
        }

    }

    /*solve method will pass the dimension, cellgroup and operator group to another class
    That class will return grid after the solution
    Here the method will check if the solution is appropriate or not and return the value accordingly*/
    public boolean solve()
    {
        if(!loadPuzzle || !readyToSolve)
        {
            return false;
        }
        try {
            int dim= list.get(0).length();
            //intializing the object and pasing data to another class
            gd= new Grid(dim,operatorGroup,cellGroup);
            //getting the grid and checking if the solution is right or not
            if(gd.init())
            {
                int[][] checkMatrix=gd.getGrid();
                for(int i=0;i<gd.getDim();i++)
                {
                    for(int j=0;j<gd.getDim();j++)
                    {
                        if(checkMatrix[i][j]==0)
                        {
                            return false;
                        }
                    }
                }
                solved=true;
                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }


    }

    /*This method will retrieve the grid from another class and store the grid in the string and will return the string if the grid is proper*/
     public String print()
    {
        if(!loadPuzzle)
        {
            return null;
        }
        if(!solved) {
            int dim= list.get(0).length();
            String toPrint="";
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    toPrint+=grid[i][j];
                }
                toPrint+="\n";
            }
            return toPrint;
        }
        try {
            String toPrint="";
            //getting the grid
            int[][] printMatrix=gd.getGrid();
            //checking the grid is proper or not
            for(int i=0;i<gd.getDim();i++)
            {
                for(int j=0;j<gd.getDim();j++)
                {
                    if(printMatrix[i][j]==0)
                    {
                        toPrint+=grid[i][j];
                    }
                    //assigning the data to string
                    else {toPrint+=printMatrix[i][j];}
                }
                toPrint+="\n";
            }
            return toPrint;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /*This method will return number of attempts done to find the right answer*/
    int choices()
    {
        try{
//            //checking the solution is proper or not
//            int[][] checkMatrix=gd.getGrid();
//            for(int i=0;i<gd.getDim();i++)
//            {
//                for(int j=0;j<gd.getDim();j++)
//                {
//                    if(checkMatrix[i][j]==0)
//                    {
//                        return 0;
//                    }
//                }
//            }
            return gd.counter;
        }
        catch (Exception e)
        {
            return 0;
        }
    }
}

