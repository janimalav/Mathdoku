import java.io.BufferedReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Mathdoku {
    HashMap<String, ArrayList<int[]>> cellGroup= new HashMap<>();
    HashMap<String,String[]> operatorGroup= new HashMap<>();
    private List<String> list = new ArrayList<>();
    Grid gd;

    boolean loadPuzzle(BufferedReader stream)
    {
        try
        {
            list=stream.lines().collect(Collectors.toList());
            list.removeAll(Arrays.asList(" ",null,""));
            int dim=list.get(0).length();

            boolean opGrp=makeOperatorGroup(dim);
            boolean makecell=makeCell(dim);

            if(makecell && opGrp)
            {
                return true;
            }
            else {return false;}
            //making the operator and char map
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private boolean makeCell(int dim)
    {
        try {
            char[][] matrix = new char[dim][dim];
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    if (list.get(i).isEmpty()) {
                        return false;
                    }
                    matrix[i][j] = list.get(i).charAt(j);
                }
            }
            //making the group

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

    private boolean makeOperatorGroup(int dim)
    {
        try {
            for(int i=dim;i<list.size();i++) {
                if (list.get(i).isEmpty()) {
                    return false;
                }
                String[] arr = list.get(i).split(" +");

                String[] arrCopy = new String[2];
                arrCopy[0] = arr[1];
                arrCopy[1] = arr[2];
                operatorGroup.put(arr[0], arrCopy);
            }
            return true;
        }
        catch (Exception e)
        {
            return false;
        }

    }

    boolean readyToSolve()
    {
        char[] match={'+','-','*','/','=','–'};
        int count=0;
        for(String key:operatorGroup.keySet())
        {
            try{Integer.parseInt(operatorGroup.get(key)[0]);}
            catch (NumberFormatException e){return false;}

            for(int i=0;i<match.length;i++)
            {
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
        return true;
    }

    private boolean checkSymbols()
    {
        try {
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
        return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private boolean checkMath()
    {
        try {
            int dim=list.get(0).length();
            for(String x: operatorGroup.keySet())
            {
                if(operatorGroup.get(x)[1].equals("-") || operatorGroup.get(x)[1].equals("–") || operatorGroup.get(x)[1].equals("/"))
                {
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

    boolean solve()
    {
        try {
            int dim= list.get(0).length();
            gd= new Grid(dim,operatorGroup,cellGroup);
            if(gd.init())
            {
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

    String print()
    {
        gd.printGrid();
        return null;
    }

    int choices()
    {
        System.out.println(gd.counter);
        return 0;
    }
}

