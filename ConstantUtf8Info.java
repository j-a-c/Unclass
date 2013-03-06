public class ConstantUtf8Info extends ConstantPoolEntry{

    //constant pool that entry is a member of
    ConstantPoolEntry[] constantPool;
    int tag;
    int length;
    String s;
    int[] request;

    ConstantUtf8Info(ConstantPoolEntry[] cp){
        constantPool = cp;
        tag = 1;
        length = -1;
        s = "";
        request = new int[2];
        request[0] = 2; request[1] = 17;
    }

    //request parameters
    public int[] request(){
        return request;
    }

    public void give(int n){
        //update length and replace replace[0]
        //will be used by UnclassProject to determine String length
        if(length == -1){
            request[0] = length = n;
        }
        else s += (char) n;
    }

    public int getTag(){
        return tag;
    }

    public boolean isBuilt(){
        return true;
    }

    //unimplemented for Uft8
    public void build(){}

    public String toString(){
        return s;
    }
}
