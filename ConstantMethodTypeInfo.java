public class ConstantMethodTypeInfo extends ConstantPoolEntry{

    //constant pool that entry is a member of
    ConstantPoolEntry[] constantPool;
    int tag;
    int descriptor_index;
    String descriptor;
    int[] request;
    boolean built;

    ConstantMethodTypeInfo(ConstantPoolEntry[] cp){
        constantPool = cp;
        tag = 16;
        descriptor_index = 0;
        request = new int[1];
        request[0] = 2;
        built = false;
    }

    //request parameters
    public int[] request(){
        return request;
    }

    public void give(int n){
        descriptor_index = n;
    }

    public int getTag(){
        return tag;
    }

    public boolean isBuilt(){
        return built;
    }

    public void build(){
        if(!built){
            if(!constantPool[descriptor_index].isBuilt())
                constantPool[descriptor_index].build();
            descriptor = constantPool[descriptor_index].toString();
            built = true;
        }
    }

    public String toString(){
        return "TODO";
    }
}
