public class ConstantStringInfo extends ConstantPoolEntry{

    //constant pool that entry is a member of
    ConstantPoolEntry[] constantPool;
    int tag;
    int string_index;
    String stringIndex;
    int request[];
    boolean built;

    ConstantStringInfo(ConstantPoolEntry[] cp){
        constantPool = cp;
        tag = 8;
        string_index = 0;
        request = new int[1];
        request[0] = 2;
        built = false;
    }

    //request parameters
    public int[] request(){
        return request;
    }

    public void give(int n){
        string_index = n;
    }

    public int getTag(){
        return tag;
    }

    public boolean isBuilt(){
        return built;
    }

    public void build(){
        if(!built){
            if(!constantPool[string_index].isBuilt())
                constantPool[string_index].build();
            stringIndex = constantPool[string_index].toString();
            built = true;
        }
    }

    public String toString(){
        return "\"" + stringIndex + "\"";    
    }
}
