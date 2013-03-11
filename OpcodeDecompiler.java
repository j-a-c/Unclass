/*
 * Author Joshua A. Campbell
 *
 * Opcode.java
 * Used to decompile opcode from AttributeCode.build()
 *
 * @params: opcode array, Unclass project, method params
 *
 */

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

public class OpcodeDecompiler{

    UnclassProject project;
    VarGenerator variableGenerator;
    ConstantPoolEntry[] constantPool;

    //opcode stack
    Stack<String> stack;        
    //branchoffset queue - used to test ifelse/if/for structures
    PriorityQueue<Integer> controlHead;    
    int[] code;         //JVM opcodes

    //variables
    String[] fields;    //class fields
    String[] parameters;    //method parameters
    LinkedList<String> locals; //local variables that arent parameters
    String[] variables; //all variables need to decompile method

    int max_locals;

    //is this method static?
    boolean isStatic;

    //amount of tabs when writing to file
    int tabSize;

    //decompiled code
    String decompiledCode;

    OpcodeDecompiler(UnclassProject p){
        project = p;
        variableGenerator = project.getVariableGenerator();
        max_locals = 0;
        isStatic = false; //default
        tabSize = 2;
    }
    
    public String decompile(){
        //initialize op stack and decompiled code
        stack = new Stack<String>();
        controlHead = new PriorityQueue<Integer>();
        locals = new LinkedList<String>();

        //decompiled code
        decompiledCode = "";
        constantPool = project.getConstantPool();

        variables = new String[max_locals];

        //initialize parameters
        if(isStatic){
            for(int i = 0; i < variables.length; i++){
                if( i < parameters.length )
                    variables[i] = parameters[i];
                else
                    variables[i] = "--";
            }
        }
        else{
            variables[0] = "this";
            for(int i = 0; i < variables.length-1; i++){
                if( i < parameters.length )
                    variables[i + 1] = parameters[i];
                else
                    variables[i + 1] = "--";
            }
        }

        //generate local variables that aren't parameters
        //not all will be used if some turn out to be double/long
        if(max_locals != parameters.length){
            for(int i = parameters.length; i < variables.length; i++)
                locals.offer( variableGenerator.generate() );
        }
        
        //TODO delete
        System.out.println("New method:");

        //decompile JVM bytecode
        //counter
        int i = 0;
        //some temporary vars
        int index; int inc; String ref; String val; String args;
        String val1; String val2;
        //should this control block's tabSize match the previous block's tabsize?
        boolean continueBlock = false;
        //main loop
        while(i < code.length){
            
            //test for control structure's end
            if(controlHead.peek() != null && i == controlHead.peek()){
                controlHead.poll();
                tabSize--;
                //close control structure
                decompiledCode += this.tabs() + "}\n";

                //test for 'else'
                //index = previous goto amount (if any)
                //code[i - 3] should be goto (167 = 0xA7) branches
                index = (short) (code[i-2] << 8) + code[i-1];
                if(code[i-3] == 167 && index > 3){
                    continueBlock = true;
                    //set up for last 'else' statement
                    //the if switches are responsible for changing to 'else if'
                    decompiledCode += this.tabs() + "else{\n";
                    //the next control structure
                    controlHead.offer(i + index - 3);
                }
                else decompiledCode += "\n";
            }

            //TODO delete
            //System.out.println("OP: " + code[i]);

            //case notation: mnemonic opcode(hex)
            //TODO implement getVariable() for all variable requests
            switch(code[i]){
                //nop (00)
                case 0:
                    i++; break;
                //aconst_null (01)
                case 1:
                    stack.push("null");
                    i++; break;
                //iconst_m1 (02)
                case 2:
                    stack.push("-1");
                    i++; break;
                //iconst_0 (03)
                case 3:
                    stack.push("0");
                    i++; break;
                //iconst_1 (04)
                case 4:
                    stack.push("1");
                    i++; break;
                //iconst_2 (05)
                case 5:
                    stack.push("2");
                    i++; break;
                //iconst_3 (06)
                case 6:
                    stack.push("3");
                    i++; break;
                //iconst_4 (07)
                case 7:
                    stack.push("4");
                    i++; break;
                //iconst_5 (08)
                case 8:
                    stack.push("5");
                    i++; break;
                //lconst_0 (09)
                case 9:
                    stack.push("0");
                    i++; break;
                //lconst_1 (0A)
                case 10:
                    stack.push("1");
                    i++; break;
                //fconst_0 (0B)
                case 11:
                    stack.push("0");
                    i++; break;
                //fconst_1 (0C)
                case 12:
                    stack.push("1");
                    i++; break;
                //fconst_2 (0D)
                case 13:
                    stack.push("2");
                    i++; break;
                //dconst_0 (0E)
                case 14:
                    stack.push("0");
                    i++; break;
                //dconst_1 (0F)
                case 15:
                    stack.push("1");
                    i++; break;
                //bipush (10)
                case 16:
                    stack.push("" + code[++i]);
                    i++; break;
                //sipush (11)
                case 17:
                    stack.push( "" + 
                            (short) ((code[++i] << 8) + code[++i]) );
                    i++; break;
                //ldc (12)
                case 18:
                    val = constantPool[ code[++i] ].toString();
                    stack.push(val);
                    i++; break;
                //ldc_w (13)
                case 19:
                    index = (code[++i] << 8) + code[++i];
                    val = constantPool[index].toString();
                    stack.push(val);
                    i++; break;
                //ldc2_w (14)
                case 20:
                    index = (code[++i] << 8) + code[++i];
                    val = constantPool[index].toString();
                    stack.push(val);
                    i++; break;
                //iload (15)
                case 21:
                    stack.push( this.getVariable(code[++i], 5) );
                    i++; break;
                //fload (17)
                case 23:
                    stack.push( this.getVariable(code[++i], 6) );
                    i++; break;
                //dload (18)
                case 24:
                    stack.push( this.getVariable(code[++i], 10) );
                    i++; break;
                //iload_0 (1A)
                case 26:
                    stack.push( this.getVariable(0, 5) );
                    i++; break;
                //iload_1 (1B)
                case 27:
                    stack.push( this.getVariable(1, 5) );
                    i++; break;
                //iload_2 (1C)
                case 28:
                    stack.push( this.getVariable(2, 5) );
                    i++; break;
                //iload_3 (1D)
                case 29:
                    stack.push( this.getVariable(3, 5) );
                    i++; break;
                //dload_0 (26)
                case 38:
                    stack.push( this.getVariable(0,10) );
                    i++; break;
                //dload_1 (27)
                case 39:
                    stack.push( this.getVariable(1,10) );
                    i++; break;
                //dload_2 (28)
                case 40:
                    stack.push( this.getVariable(2,10) );
                    i++; break;
                //dload_3 (29)
                case 41:
                    stack.push( this.getVariable(3,10) );
                    i++; break;
                //aload_0 (2A)
                case 42: 
                    stack.push( variables[0] );
                    i++; break;
                //aload_1 (2C)
                case 43:
                    stack.push( variables[1] );
                    i++; break;
                //aload_2 (2C)
                case 44:
                    stack.push( variables[2] );
                    i++; break;
                //aload_2 (2D)
                case 45:
                    stack.push( variables[3] );
                    i++; break;
                //iaload (2E)
                case 46:
                    val2 = stack.pop();
                    val1 = stack.pop();
                    stack.push( val1 + "[" + val2 + "]" );
                    i++; break;
                //aaload (32)
                case 50:
                    val = stack.pop();
                    stack.push( stack.pop() + "[" + val + "]" );
                    i++; break;
                //istore (36)
                case 54:
                    decompiledCode += this.tabs() +
                        this.getVariable(code[++i],5) + " = "
                        + stack.pop() + ";\n";
                    i++; break;
                //istore_0 (3B)
                case 59:
                    decompiledCode += this.tabs() + 
                        this.getVariable(0,5) + " = " 
                        + stack.pop() + ";\n";
                    i++; break;
                //istore_1 (3C)
                case 60:
                    decompiledCode += this.tabs() +
                        this.getVariable(1,5) + " = " 
                        + stack.pop() + ";\n";
                    i++; break;
                //istore_2 (3D)
                case 61:
                    decompiledCode += this.tabs() + 
                        this.getVariable(2,5) + " = " 
                        + stack.pop() + ";\n";
                    i++; break;
                //istore_3 (3E)
                case 62:
                    decompiledCode += this.tabs() +
                        this.getVariable(3,5) + " = " 
                        + stack.pop() + ";\n";
                    i++; break;
                //lstore_0 (3F)
                case 63:
                    decompiledCode += this.tabs() +
                        this.getVariable(0,9) + " = "
                        + stack.pop() + ";\n";
                    i++; break;
                //lstore_1 (40)
                case 64:
                    decompiledCode += this.tabs() +
                        this.getVariable(1,9) + " = "
                        + stack.pop() + ";\n";
                    i++; break;
                //lstore_2 (41)
                case 65:
                    decompiledCode += this.tabs() +
                        this.getVariable(2,9) + " = "
                        + stack.pop() + ";\n";
                    i++; break;
                //lstore_3 (42)
                case 66:
                    decompiledCode += this.tabs() +
                        this.getVariable(3,9) + " = "
                        + stack.pop() + ";\n";
                    i++; break;
                //dstore_0 (47)
                case 71:
                     decompiledCode += this.tabs() +
                        this.getVariable(0,10) + " = "
                        + stack.pop() + ";\n";
                    i++; break;
                //dstore_1 (48)
                case 72:
                     decompiledCode += this.tabs() +
                        this.getVariable(1,10) + " = "
                        + stack.pop() + ";\n";
                    i++; break;
                //dstore_2 (49)
                case 73:
                     decompiledCode += this.tabs() +
                        this.getVariable(2,10) + " = "
                        + stack.pop() + ";\n";
                    i++; break;
                //dstore_3 (4A)
                case 74:
                    decompiledCode += this.tabs() +
                        this.getVariable(3,10) + " = "
                        + stack.pop() + ";\n";
                    i++; break;
                //astore_0 (4B)
                case 75:
                    decompiledCode += this.tabs() + 
                        this.getVariable(0,7) + " = "
                        + stack.pop() + ";\n";
                    i++; break;
                //astore_1 (4C)
                case 76:
                    decompiledCode += this.tabs() +
                        this.getVariable(1,7) + " = "
                        + stack.pop() + ";\n";
                    i++; break;
                //astore_2 (4D)
                case 77:
                    decompiledCode += this.tabs() +
                        this.getVariable(2,7) + " = "
                        + stack.pop() + ";\n";
                    i++; break;
                //astore_3 (4E)
                case 78:
                    decompiledCode += this.tabs() +
                        this.getVariable(3,7) + " = "
                        + stack.pop() + ";\n";
                    i++; break;
                //aastore (53)
                case 83:
                    //value
                    val1 = stack.pop();
                    //index
                    val2 = stack.pop();
                    decompiledCode += this.tabs() +
                        stack.pop() + "[" + val2 + "] = " + val1 + ";\n";
                    i++; break;
                //pop (57)
                case 87:
                    stack.pop();
                    i++; break; 
                //dup (59)
                case 89:
                    stack.push( stack.peek() );
                    i++; break;
                //dup_x1 (5A)
                case 90:
                    stack.insertElementAt( stack.peek(), 
                            stack.size() - 2 );
                    i++; break;
                //dup2 (5C)
                case 92:
                    val1 = stack.pop();
                    val2 = stack.pop();

                    stack.push(val2);
                    stack.push(val1);
                    stack.push(val2);
                    stack.push(val1);
                    i++; break;
                //dup2_x1 (5D)
                case 93:
                    val1 = stack.pop();
                    val2 = stack.pop();
                    val = stack.pop();

                    stack.push(val2);
                    stack.push(val1);
                    stack.push(val);
                    stack.push(val2);
                    stack.push(val1);
                    i++; break;
                //dup2_x2 (5E)
                case 94:
                    val1 = stack.pop();
                    val2 = stack.pop();
                    val = stack.pop();
                    ref = stack.pop();

                    stack.push(val2);
                    stack.push(val1);
                    stack.push(ref);
                    stack.push(val);
                    stack.push(val2);
                    stack.push(val1);
                    i++; break;
                //swap (5F)
                case 95:
                    val1 = stack.pop();
                    val2 = stack.pop();

                    stack.push(val1);
                    stack.push(val2);
                    i++; break;
                //iadd (60)
                case 96:
                    val2 = stack.pop();
                    val1 = stack.pop();
                    stack.push( val1 + " + " + val2 );
                    i++; break;
                //dadd (63)
                case 99:
                    val2 = stack.pop();
                    val1 = stack.pop();
                    stack.push( val1 + " + " + val2 );
                    i++; break;
                //isub (64)
                case 100:
                    val2 = stack.pop();
                    val1 = stack.pop();
                    stack.push( val1 + " - " + val2 );
                    i++; break;
                //fsub (66)
                case 102:
                    val2 = stack.pop();
                    val1 = stack.pop();
                    stack.push( val1 + " - " + val2 );
                    i++; break;
                //ishl (78)
                case 120:
                    val2 = stack.pop();
                    val1 = stack.pop();
                    //use five low bits of val2
                    index = Integer.parseInt(val2) & 0x1F;

                    stack.push(val1 + " << " + index);

                    i++; break;
                //iushr (7C):
                case 124:
                    val2 = stack.pop();
                    val1 = stack.pop();
                    //use five low bits of val2
                    index = Integer.parseInt(val2) & 0x1F;

                    stack.push(val1 + " >>> " + index);

                    i++; break;
                //iand (7E)
                case 126:
                    val2 = stack.pop();
                    val1 = stack.pop();
                    stack.push( val1 + " & " + val2 );
                    i++; break;
                //ixor (82)
                case 130:
                    val2 = stack.pop();
                    val1 = stack.pop();
                    stack.push( val1 + " ^ " + val2 );
                    i++; break;
                //iinc (84)
                case 132:
                    index = code[++i]; inc = code[++i];
                    if(inc == 1)
                        decompiledCode += this.tabs() + variables[index] 
                            + "++;\n";
                    else
                        decompiledCode += this.tabs() + variables[index] 
                            + " += " + inc + ";\n";
                    i++; break;
                //fcmpl (95)
                case 149:
                    System.out.print("fcmpl (case 149)");
                    val2 = stack.pop();
                    val1 = stack.pop();

                    if(Integer.parseInt(val1) > Integer.parseInt(val2))
                        stack.push("1");
                    else if(Integer.parseInt(val1) < Integer.parseInt(val2))
                        stack.push("-1");
                    else stack.push("0");

                    i++; break;
                //ifeq (99)
                case 153:
                    //branch fail offset
                    inc = (short) ( (code[++i] << 8) + code[++i] );
                    //structure end's goto offset
                    //corresponding goto: code[i + inc - 5]
                    index = (short) (code[i + inc - 4] << 8) + code[i + inc - 3];
                    controlHead.offer(i + inc - 2);
                    
                    //negative goto offset => loop
                    if(index < 0)
                        decompiledCode += this.tabs() + "for(; " +
                            stack.pop() + " != 0 " + " ;){\n";
                    else{
                        if(!continueBlock) decompiledCode += this.tabs();
                        else decompiledCode = decompiledCode.substring(0, decompiledCode.length()-2) + " ";
                        continueBlock = false;
                        decompiledCode += "if(" + 
                            stack.pop() + " != 0 " + "){\n";
                    }
                    
                    tabSize++;

                    i++; break;
                //ifne (9A)
                case 154:
                    //branch fail offset
                    inc = (short) ( (code[++i] << 8) + code[++i] );
                    //structure end's goto offset
                    //corresponding goto: code[i + inc - 5]
                    index = (short) (code[i + inc - 4] << 8) + code[i + inc - 3];
                    controlHead.offer(i + inc -2);
                    
                    //negative goto offset => loop
                    if(index < 0)
                        decompiledCode += this.tabs() + "for(; " +
                            stack.pop() + " == 0 " + " ;){\n";
                    else{
                        if(!continueBlock) decompiledCode += this.tabs();
                        else decompiledCode = decompiledCode.substring(0, decompiledCode.length()-2) + " ";
                        continueBlock = false;
                        decompiledCode += "if(" + 
                            stack.pop() + " == 0 " + "){\n";
                    }
                    
                    tabSize++;

                    i++; break;
                //iflt (9B)
                case 155:
                    //branch fail offset
                    inc = (short) ( (code[++i] << 8) + code[++i] );
                    //structure end's goto offset
                    //corresponding goto: code[i + inc - 5]
                    index = (short) (code[i + inc - 4] << 8) + code[i + inc - 3];
                    controlHead.offer(i + inc -2);
                    
                    //negative goto offset => loop
                    if(index < 0)
                        decompiledCode += this.tabs() + "for(; " +
                            stack.pop() + " >= 0 " + " ;){\n";
                    else{
                        if(!continueBlock) decompiledCode += this.tabs();
                        else decompiledCode = decompiledCode.substring(0, decompiledCode.length()-2) + " ";
                        continueBlock = false;
                        decompiledCode += "if(" + 
                            stack.pop() + " >= 0 " + "){\n";
                    }
                    tabSize++;

                    i++; break;
                //ifge (9C)
                case 156:
                    //branch fail offset
                    inc = (short) ( (code[++i] << 8) + code[++i] );
                    //structure end's goto offset
                    //corresponding goto: code[i + inc - 5]
                    index = (short) (code[i + inc - 4] << 8) + code[i + inc - 3];
                    controlHead.offer(i + inc -2);
                    
                    //negative goto offset => loop
                    if(index < 0)
                        decompiledCode += this.tabs() + "for(; " +
                            stack.pop() + " < 0 " + " ;){\n";
                    else{
                        if(!continueBlock) decompiledCode += this.tabs();
                        else decompiledCode = decompiledCode.substring(0, decompiledCode.length()-2) + " ";
                        continueBlock = false;
                        decompiledCode += "if(" + 
                            stack.pop() + " < 0 " + "){\n";

                    }
                    tabSize++;

                    i++; break;
                //ifgt (9D)
                case 157:
                    //branch fail offset
                    inc = (short) ( (code[++i] << 8) + code[++i] );
                    //structure end's goto offset
                    //corresponding goto: code[i + inc - 5]
                    index = (short) (code[i + inc - 4] << 8) + code[i + inc - 3];
                    controlHead.offer(i + inc -2);
                    
                    //negative goto offset => loop
                    if(index < 0)
                        decompiledCode += this.tabs() + "for(; " +
                            stack.pop() + " <= 0 " + " ;){\n";
                    else{
                        if(!continueBlock) decompiledCode += this.tabs();
                        else decompiledCode = decompiledCode.substring(0, decompiledCode.length()-2) + " ";
                        continueBlock = false;
                        decompiledCode += "if(" + 
                            stack.pop() + " <= 0 " + "){\n";
                    }
                    
                    tabSize++;

                    i++; break;
                //ifle (9E)
                case 158:
                    //branch fail offset
                    inc = (short) ( (code[++i] << 8) + code[++i] );
                    //structure end's goto offset
                    //corresponding goto: code[i + inc - 5]
                    index = (short) (code[i + inc - 4] << 8) + code[i + inc - 3];
                    controlHead.offer(i + inc -2);
                    
                    //negative goto offset => loop
                    if(index < 0)
                        decompiledCode += this.tabs() + "for(; " +
                            stack.pop() + " > 0 " + " ;){\n";
                    else{
                        if(!continueBlock) decompiledCode += this.tabs();
                        else decompiledCode = decompiledCode.substring(0, decompiledCode.length()-2) + " ";
                        continueBlock = false;
                        decompiledCode += "if(" + 
                            stack.pop() + " > 0 " + "){\n";
                    }
                    
                    tabSize++;

                    i++; break;
                //if_icmpeq (9F)
                case 159:
                    //branch fail offset
                    inc = (short) ( (code[++i] << 8) + code[++i] );
                    //structure end's goto offset
                    //corresponding goto: code[i + inc - 5]
                    index = (short) (code[i + inc - 4] << 8) + code[i + inc - 3];
                    controlHead.offer(i + inc -2);
                    
                    val2 = stack.pop();
                    val1 = stack.pop();
                    
                    //negative goto offset => loop
                    if(index < 0)
                        decompiledCode += this.tabs() + "for(; " +
                            val1 + " != " + val2 + " ;){\n";
                    else{
                        if(!continueBlock) decompiledCode += this.tabs();
                        else decompiledCode = decompiledCode.substring(0, decompiledCode.length()-2) + " ";
                        continueBlock = false;
                        decompiledCode += "if(" + 
                            val1 + " != " + val2 + "){\n";
                    }
                    
                    tabSize++;

                    i++; break;
                //if_icmpne (A0)
                case 160:
                    //branch fail offset
                    inc = (short) ( (code[++i] << 8) + code[++i] );
                    //structure end's goto offset
                    //corresponding goto: code[i + inc - 5]
                    index = (short) (code[i + inc - 4] << 8) + code[i + inc - 3];
                    controlHead.offer(i + inc -2);

                    val2 = stack.pop(); val1 = stack.pop();
                    
                    //negative goto offset => loop
                    if(index < 0)
                        decompiledCode += this.tabs() + "for(; " +
                            val1 + " == " + val2 + " ;){\n";
                    else{
                        //handle a continuing control block
                        if(!continueBlock) decompiledCode += this.tabs();
                        else decompiledCode = decompiledCode.substring(0, decompiledCode.length()-2) + " ";
                        continueBlock = false;
                        decompiledCode += "if(" + 
                            val1 + " == " + val2 + "){\n";
                    }
                    
                    tabSize++;

                    i++; break;
                //if_icmplt (A1)
                case 161:
                    //branch fail offset
                    inc = (short) ( (code[++i] << 8) + code[++i] );
                    //structure end's goto offset
                    //corresponding goto: code[i + inc - 5]
                    index = (short) (code[i + inc - 4] << 8) + code[i + inc - 3];
                    controlHead.offer(i + inc -2);

                    val2 = stack.pop(); val1 = stack.pop();
                    
                    //negative goto offset => loop
                    if(index < 0)
                        decompiledCode += this.tabs() + "for(; " +
                            val1 + " >= " + val2 + " ;){\n";
                    else{
                        if(!continueBlock) decompiledCode += this.tabs();
                        else decompiledCode = decompiledCode.substring(0, decompiledCode.length()-2) + " ";
                        continueBlock = false;
                        decompiledCode += "if(" + 
                            val1 + " >= " + val2 + "){\n";
                    }
                    
                    tabSize++;

                    i++; break;
                //if_icmpge (A2)
                case 162:
                    //branch fail offset
                    inc = (short) ( (code[++i] << 8) + code[++i] );
                    //structure end's goto offset
                    //corresponding goto: code[i + inc - 5]
                    index = (short) (code[i + inc - 4] << 8) + code[i + inc - 3];
                    controlHead.offer(i + inc -2);

                    val2 = stack.pop(); val1 = stack.pop();
                    
                    //negative goto offset => loop
                    if(index < 0)
                        decompiledCode += this.tabs() + "for(; " +
                            val1 + " < " + val2 + " ;){\n";
                    else{
                        if(!continueBlock) decompiledCode += this.tabs();
                        else decompiledCode = decompiledCode.substring(0, decompiledCode.length()-2) + " ";
                        continueBlock = false;
                        decompiledCode += "if(" + 
                            val1 + " < " + val2 + "){\n";
                    }
                    
                    tabSize++;
                    
                    i++; break;
                //if_icmpgt (A3)
                case 163:
                    //branch fail offset
                    inc = (short) ( (code[++i] << 8) + code[++i] );
                    //structure end's goto offset
                    //corresponding goto: code[i + inc - 5]
                    index = (short) (code[i + inc - 4] << 8) + code[i + inc - 3];
                    controlHead.offer(i + inc -2);

                    val2 = stack.pop(); val1 = stack.pop();
                    
                    //negative goto offset => loop
                    if(index < 0)
                        decompiledCode += this.tabs() + "for(; " +
                            val1 + " <= " + val2 + " ;){\n";
                    else{
                        if(!continueBlock) decompiledCode += this.tabs();
                        else decompiledCode = decompiledCode.substring(0, decompiledCode.length()-2) + " ";
                        continueBlock = false;
                        decompiledCode += "if(" + 
                            val1 + " <= " + val2 + "){\n";
                    }
                    
                    tabSize++;

                    i++; break;
                //if_icmple (A4)
                case 164:
                    //branch fail offset
                    inc = (short) ( (code[++i] << 8) + code[++i] );
                    //structure end's goto offset
                    //corresponding goto: code[i + inc - 5]
                    index = (short) (code[i + inc - 4] << 8) + code[i + inc - 3];
                    controlHead.offer(i + inc -2);

                    val2 = stack.pop(); val1 = stack.pop();
                    
                    //negative goto offset => loop
                    if(index < 0)
                        decompiledCode += this.tabs() + "for(; " +
                            val1 + " > " + val2 + " ;){\n";
                    else{
                        if(!continueBlock) decompiledCode += this.tabs();
                        else decompiledCode = decompiledCode.substring(0, decompiledCode.length()-2) + " ";
                        continueBlock = false;
                        decompiledCode += "if(" + 
                            val1 + " > " + val2 + "){\n";
                    }
                    
                    tabSize++;

                    i++; break;
                //goto (A7)
                case 167:
                    i += 2;
                    i++; break;
                //ireturn (AC)
                case 172:
                    decompiledCode += this.tabs() + "return " + 
                        stack.pop() +";\n";
                    i++; break;
                //areturn (B0)
                case 176:
                    decompiledCode += this.tabs() + "return " + 
                        stack.pop() + ";\n";
                    i++; break;
                //return (B1)
                case 177:
                    i++; break;
                //getstatic (B2)
                case 178:
                    index = (code[++i] << 8) + code[++i];
                    stack.push( constantPool[index].toString() );
                    i++; break;
                //getfield (B4)
                case 180:
                    index = (code[++i] << 8) + code[++i];
                    stack.push( constantPool[index].toString() );
                    i++; break;
                //putfield (B5)
                case 181:
                    index = (code[++i] << 8) + code[++i];
                    val = stack.pop();
                    ref = stack.pop();
                    if(!ref.equals("this"))
                        val = ref + "." + val;
                    decompiledCode += this.tabs() + 
                        constantPool[index].toString() + 
                        " = " + val + ";\n";
                    i++; break;
                //invokevirtual (B6)
                case 182:
                    index = (code[++i] << 8) + code[++i];
                    //virtual method
                    //constant method ref info
                    ConstantMethodrefInfo attr182 =
                        (ConstantMethodrefInfo) constantPool[index];

                    ref = attr182.toString();
                    int numParams182 = attr182.getNumberOfParameters();


                    val = "";
                    if( numParams182 >= 1 )
                        val = stack.pop();
                    while( --numParams182 > 0)
                        val = stack.pop() + ", " + val;
                    
                    val = stack.pop() + "." + ref + "(" + val + ")";

                    //print decompiled void methods
                    if(attr182.getReturnDescriptor().equals("V"))
                        decompiledCode += this.tabs() + val + ";\n";

                    stack.push( val );
                    
                    i++; break;
                //invokespecial (B7)
                case 183:
                    index = (code[++i] << 8) + code[++i];
                    //virtual method
                    //constant method ref info
                    ConstantMethodrefInfo attr183 =
                        (ConstantMethodrefInfo) constantPool[index];

                    ref = attr183.toString();
                    int numParams183 = attr183.getNumberOfParameters();


                    val = "";
                    if( numParams183 >= 1 )
                        val = stack.pop();
                    while( --numParams183 > 0)
                        val = stack.pop() + ", " + val;

                    val = stack.pop() + "." + ref + "(" + val + ")";

                    //print decompiled void methods
                    if(attr183.getReturnDescriptor().equals("V"))
                        decompiledCode += this.tabs() + val + ";\n";

                    stack.push( val );
                    
                    i++; break;
                //new (BB)
                case 187:
                    index = (code[++i] << 8) + code[++i];
                    stack.push("new " + 
                            constantPool[index].toString() );
                    i++; break;
                //arraylength (BE)
                case 190:
                    stack.push( stack.pop() + ".length" );
                    i++; break;
                //checkcast (C0)
                case 192:
                    System.out.println("Checkcast implemented 192-0xC0");
                    index = (code[++i] << 8) + code[++i];
                    stack.push( "(" + constantPool[index].toString() + 
                            ") " + stack.pop() );
                    i++; break;
                //instanceof (C1)
                case 193:
                    index = (code[++i] << 8) + code[++i];
                    stack.push( stack.pop() + " instanceof " + 
                            constantPool[index].toString() );
                    i++; break;
                //ifnull (C6)
                case 198:
                    //branch fail offset
                    inc = (short) ( (code[++i] << 8) + code[++i] );
                    //structure end's goto offset
                    //corresponding goto: code[i + inc - 5]
                    index = (short) (code[i + inc - 4] << 8) 
                        + code[i + inc - 3];

                    controlHead.offer(i + inc -2);
                    
                    //negative goto offset => loop
                    if(index < 0)
                        decompiledCode += this.tabs() + "for(; " +
                            stack.pop() + " != null ;){\n";
                    else{
                        if(!continueBlock) decompiledCode += this.tabs();
                        else decompiledCode = decompiledCode.substring(0, decompiledCode.length()-2) + " ";
                        continueBlock = false;
                        decompiledCode += "if(" + 
                            stack.pop() + " != null ){\n";
                    }
                    
                    tabSize++;
                    
                    i++; break;
                //ifnonnull (C7)
                case 199:
                    //branch fail offset
                    inc = (short) ( (code[++i] << 8) + code[++i] );
                    //structure end's goto offset
                    //corresponding goto: code[i + inc - 5]
                    index = (short) (code[i + inc - 4] << 8) 
                        + code[i + inc - 3];

                    controlHead.offer(i + inc -2);
                    
                    //negative goto offset => loop
                    if(index < 0)
                        decompiledCode += this.tabs() + "for(; " +
                            stack.pop() + " == null ;){\n";
                    else{
                        if(!continueBlock) decompiledCode += this.tabs();
                        else decompiledCode = decompiledCode.substring(0, decompiledCode.length()-2) + " ";
                        continueBlock = false;
                        decompiledCode += "if(" + 
                            stack.pop() + " == null ){\n";
                    }
                    
                    tabSize++;
                    
                    i++; break;
                //impdep1 (FE) and impdep2 (FF)
                case 254:
                case 255:
                    /*
                    System.out.println("Debug opcodes: " + 
                            (1 + code.length - i));
                    i = code.length;*/
                    i = code.length;
                    break;
                default:
                        System.out.println("Unknown opcode " + code[i]);
                        i++; break;
            }
        }

        return decompiledCode;
    }//end decompile()
    
    //get a variable from variable[]
    //if variable is not initialized, initialize it 
    //  then insert locals.poll() into spot indicated
    //index (parameter):
    //  index of variables[]
    //type (parameter) values:
    //  1: boolean
    //  2: byte
    //  3: char
    //  4: short
    //  5: int
    //  6: float
    //  7: reference
    //  8: returnAddress
    //  9: long
    // 10: double
    public String getVariable(int i, int type){
        if( !variables[i].equals("--") )
            return variables[i];
        
        //get variable from LinkedList locals
        //initialize variable in decompiledCode
        //initialize variable in variable[]
        //return variable
        String v;
        switch (type){
            case 1:
                v = locals.poll();
                variables[i] = v;
                return "boolean " + v;
            case 2:
                v = locals.poll();
                variables[i] = v;
                return "byte " + v;
            case 3:
                v = locals.poll();
                variables[i] = v;
                return "char " + v;
            case 4:
                v = locals.poll();
                variables[i] = v;
                return "short " + v;
            case 5:
                v = locals.poll();
                variables[i] = v;
                return "int " + v;
            case 6:
                v = locals.poll();
                variables[i] = v;
                return "float " + v;
            case 7:
                v = locals.poll();
                variables[i] = v;
                return "REFERENCE " + v;
            case 8:
                v = locals.poll();
                variables[i] = v;
                return "RETURNADDRESS " + v;
            case 9:
                v = locals.poll();
                variables[i] = v;
                variables[i+1] = v;
                return "long " + v;
            case 10:
                v = locals.poll();
                variables[i] = v;
                variables[i+1] = v;
                return "double " + v;
            default:
                System.out.println("Unknown index in getVariable: " + i);
                return "ERROR IN GETVARIABLE";
        }
    }

    //create amount of tabs needed for writing to file
    //uses class field tabSize
    public String tabs(){
        String tab = "";
        for(int i = 0; i < tabSize; i++)
            tab += "\t";
        return tab;
    }

    //
    // Set methods
    //
    
    public void setFields(){
        String[] f = project.getFieldVariables();
        fields = new String[f.length + 1];
        fields[0] = "this";

        for(int i = 0; i < f.length; i++)
            fields[i+1] = f[i];
    }
    
    public void setParameters(String[] p){
        parameters = p; 
    }

    public void setCode(int[] c){
        code = c;
    }

    public void setMaxLocals(int n){
        max_locals = n;
    }

    public void setStatic(boolean b){
        isStatic = b;
    }

}//end OpcodeDecompiler
