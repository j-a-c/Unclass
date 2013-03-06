/*
 * Author: Joshua A. Campbell
 *
 */

public class Unclass{

    public static void main(String[] args){
        if(args.length != 1)
            System.out.println("Usage: java -jar Unclass.jar FILE.class");
        else{
            UnclassProject p = new UnclassProject(args[0]);
            p.read();
        }
    }//end main

}//end Unclass
