public class ConstantFloatInfo extends ConstantPoolEntry{

    //constant pool that entry is a member of
    ConstantPoolEntry[] constantPool;
    int tag;
    float value;
    int[] request;

    ConstantFloatInfo(ConstantPoolEntry[] cp){
        constantPool = cp;
        tag = 4;
        value = 0;
        request = new int[1];
        request[0] = 4;
    }

    //request parameters
    public int[] request(){
        return request;
    }

    public void give(int n){
        
    }

    public int getTag(){
        return tag;
    }

    public boolean isBuilt(){
        return true;
    }

    public void build(){}

    public String toString(){
        return "IMPLEMENT FLOAT";
    }
}
