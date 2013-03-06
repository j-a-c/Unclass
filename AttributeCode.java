public class AttributeCode extends Attribute{

    private OpcodeDecompiler decompiler;

    private String name;
    private int length;
    private int max_stack;
    private int max_locals;
    private int code_length;
    private int exception_table_length;
    private int attributes_count;

    //used to keep track of index when adding code
    private int codeIndex;
    //holds the actual bytes of JVM code
    private int[] code;

    //used to keep track of index when adding attributes
    private int attributesSet;
    //attribute array
    private Attribute[] attributes;


    AttributeCode(){
        codeIndex = 0;
        attributesSet = 0;
        name = "Code";
    }

    public void build(){}

    public int[] getCode(){
        return code;
    }

    public void setLength(int n){
        length = n;
    }

    public void setMaxStack(int n){
        max_stack = n;
    }

    public void setMaxLocals(int n){
        max_locals = n;
    }

    public int getMaxLocals(){
        return max_locals;
    }

    public void setCodeLength(int n){
        code_length = n;
        code = new int[code_length];
    }

    public void addCode(int n){
        code[codeIndex++] = n;
    }

    public void setExceptionTableLength(int n){
        exception_table_length = n;
    }
    
    public void setAttributeCount(int n){
        attributes_count = n;
        attributes = new Attribute[attributes_count];
    }

    public void setAttribute(Attribute a){
        attributes[attributesSet++] = a;
    }

    public void setOpcodeDecompiler(OpcodeDecompiler d){
        decompiler = d;
    }

}
