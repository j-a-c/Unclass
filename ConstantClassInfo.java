public class ConstantClassInfo extends ConstantPoolEntry{

    //constant pool that entry is a member of
    ConstantPoolEntry[] constantPool;
    int tag;
    int name_index;
    String name;
    int[] request;
    boolean built;

    ConstantClassInfo(ConstantPoolEntry[] cp){
        constantPool = cp;
        tag = 7; 
        name_index = 0;
        request = new int[1];
        request[0] = 2;
        built = false;
    }

    //request parameters
    public int[] request(){
        return request;
    }

    public void give(int n){
        name_index = n;
    }

    public int getTag(){
        return tag;
    }

    public boolean isBuilt(){
        return built;
    }

    public void build(){
        if(!built)
            name = constantPool[name_index].toString();
        built = true;
    }

    public String toString(){
        return name;
    }
}
