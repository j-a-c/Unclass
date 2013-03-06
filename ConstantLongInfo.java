public class ConstantLongInfo extends ConstantPoolEntry{

    //constant pool that entry is a member of
    ConstantPoolEntry[] constantPool;
    int tag;
    int high_bytes;
    int low_bytes;
    long value;
    int[] request;

    ConstantLongInfo(ConstantPoolEntry[] cp){
        constantPool = cp;
        tag = 5;
        high_bytes = low_bytes = 0;
        value = 0;
        request = new int[2];
        request[0] = 4; request[1] = 4;
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
        return "IMPLELEMTN LONG";
    }
}
