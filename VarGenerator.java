/*
 * Author: Joshua A. Campbell
 *
 * Description:
 * Class to generate variables
 * The initial variable 'a' is incremented at each call of generate()
 */

import java.util.ArrayList;

// ascii a = 97, z = 122

public class VarGenerator{

    //current variable name
    ArrayList<Character> var;
    //list of variable names created
    ArrayList<String> varList;

    VarGenerator(){
        var = new ArrayList<Character>();
        var.add('a');
        varList = new ArrayList<String>();
    }

    public void clearVariableList(){
        varList.clear();
    }

    public String generate(){
        String variable = "";
        //save old variable
        for(int i = 0; i < var.size(); i++)
            variable += (char) var.get(i);

        varList.add(variable);

        //generate new variable
        int j = var.size() - 1;
        while(j >= 0 && var.get(j) == 'z')
            var.set(j--,'a');

        if(j == -1)     //prepend new letter
            var.add(0,'a');
        else            //increment letter
            var.set(j, (char)((var.get(j))+1));

        return variable;
    }//end generate()


    //
    // Get method(s)
    //

    public ArrayList<String> getVariables(){
        return varList;
    }
}
