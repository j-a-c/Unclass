/* 
 * Author: Joshua A. Campbell
 * Parse a field descriptor 

http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.3
B	            byte	    signed byte
C	            char	Unicode character code point, encoded with UTF-16
D	            double	    double-precision floating-point value
F	            float	    single-precision floating-point value
I	            int	        integer
J	            long	    long integer
L Classname ;	reference	an instance of class Classname
S	            short	    signed short
Z	            boolean	    true or false
[	            reference	one array dimension

*/

import java.util.ArrayList;

public class DescriptorParser{

    VarGenerator variable;
    ArrayList<String> varList;

    DescriptorParser(VarGenerator v){
        variable = v;
    }

    //parse a descriptor (String d) and DON'T add variables
    public String parseNoVar(String d){
        String ret = "";

        int i = 0;
        int p = 0;

        while(i < d.length()){

            //byte
            if(d.charAt(i) == 'B'){
                ret = ret.substring(0,p) + "byte"
                    + ret.substring(p, ret.length());
                p = ret.length();
            }
            //char
            else if(d.charAt(i) == 'C'){
                ret = ret.substring(0,p) + "char"
                    + ret.substring(p, ret.length());
                p = ret.length();
            }
            //double
            else if(d.charAt(i) == 'D'){
                ret = ret.substring(0,p) + "double"
                    + ret.substring(p, ret.length());
                p = ret.length();;
            }
            //float
            else if(d.charAt(i) == 'F'){
                ret = ret.substring(0,p) + "float" 
                    + ret.substring(p, ret.length());
                p = ret.length();
            }
            //integer
            else if(d.charAt(i) == 'I'){
                ret = ret.substring(0,p) + "int"
                    + ret.substring(p, ret.length());
                p = ret.length();
            }
            //long
            else if(d.charAt(i) == 'J'){
                ret = ret.substring(0,p) + "long"
                    + ret.substring(p, ret.length());
                p = ret.length();
            }
            //instance of class
            else if(d.charAt(i) == 'L'){
                i++;

                while(d.charAt(i) != ';'){
                    if(d.charAt(i) == '/')
                        ret = ret.substring(0,p) + "." 
                            + ret.substring(p, ret.length());
                    else
                        ret = ret.substring(0,p) + d.charAt(i) 
                            + ret.substring(p, ret.length());
                    i++;
                    p = ret.length();               
                }
            }
            //short
            else if(d.charAt(i) == 'S'){
                ret = ret.substring(0,p) + "short"
                    + ret.substring(p, ret.length());
                p = ret.length();
            }
            //void
            else if(d.charAt(i) == 'V'){
                ret = ret.substring(0,p) + "void"
                    + ret.substring(p, ret.length());
                p = ret.length();
            }
            //boolean
            else if(d.charAt(i) == 'Z'){
                ret = ret.substring(0,p) + "boolean"
                    + ret.substring(p, ret.length());
                p = ret.length();
            }
            //array dimension
            else if(d.charAt(i) == '['){
                ret += "[]";
            }

            i++;
        }

        return ret;
    }//end parseNoVar()


    //parse a descriptor (String d) and add variables
    public String parseAddVar(String d){
        variable.clearVariableList();
        varList = new ArrayList<String>();

        String ret = "";

        int i = 0;
        int p = 0;
        boolean addComma = true;
        String v;       //generated variable

        while(i < d.length()){
            addComma = true;

            //byte
            if(d.charAt(i) == 'B'){
                v = variable.generate();
                varList.add(v);
                ret = ret.substring(0,p) + "byte " + v
                    + ret.substring(p, ret.length());
                p = ret.length();
            }
            //char
            else if(d.charAt(i) == 'C'){
                v = variable.generate();
                varList.add(v);
                ret = ret.substring(0,p) + "char " + v
                    + ret.substring(p, ret.length());
                p = ret.length();
            }
            //double
            else if(d.charAt(i) == 'D'){
                v = variable.generate();
                varList.add(v); varList.add(v);
                ret = ret.substring(0,p) + "double " + v
                    + ret.substring(p, ret.length());
                p = ret.length();;
            }
            //float
            else if(d.charAt(i) == 'F'){
                v = variable.generate();
                varList.add(v);
                ret = ret.substring(0,p) + "float " + v
                    + ret.substring(p, ret.length());
                p = ret.length();
            }
            //integer 
            else if(d.charAt(i) == 'I'){
                v = variable.generate();
                varList.add(v);
                ret = ret.substring(0,p) + "int " + v
                    + ret.substring(p, ret.length());
                p = ret.length();
            }
            //long
            else if(d.charAt(i) == 'J'){
                v = variable.generate();
                varList.add(v); varList.add(v);
                ret = ret.substring(0,p) + "long " + v
                    + ret.substring(p, ret.length());
                p = ret.length();
            }
            //instance of class
            else if(d.charAt(i) == 'L'){
                i++;

                while(d.charAt(i) != ';'){
                    if(d.charAt(i) == '/')
                        ret = ret.substring(0,p) + "." 
                            + ret.substring(p, ret.length());
                    else
                        ret = ret.substring(0,p) + d.charAt(i) 
                            + ret.substring(p, ret.length());
                    i++;
                    p = ret.length();               
                }

                v = variable.generate();
                varList.add(v);
                ret = ret.substring(0,p) + " " + v
                    + ret.substring(p, ret.length());
                p = ret.length();
            }
            //short
            else if(d.charAt(i) == 'S'){
                v = variable.generate();
                varList.add(v);
                ret = ret.substring(0,p) + "short " + v
                    + ret.substring(p, ret.length());
                p = ret.length();
            }
            //void
            else if(d.charAt(i) == 'V'){
                v = variable.generate();
                varList.add(v);
                ret = ret.substring(0,p) + "void " + v
                    + ret.substring(p, ret.length());
                p = ret.length();
            }
            //boolean
            else if(d.charAt(i) == 'Z'){
                v = variable.generate();
                varList.add(v);
                ret = ret.substring(0,p) + "boolean " + v
                    + ret.substring(p, ret.length());
                p = ret.length();
            }
            //array dimension
            else if(d.charAt(i) == '['){
                ret += "[]";
                addComma = false;
            }

            i++;
            if(addComma && i < d.length()){
                ret += ", ";
                p += 2;
            }
        }

        return ret;
    }//end parseAddVar()


    //
    // Get method(s)
    //

    public ArrayList<String> getVariables(){
        return varList;
    }
}
