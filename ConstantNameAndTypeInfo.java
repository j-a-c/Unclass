public class ConstantNameAndTypeInfo extends ConstantPoolEntry{

    //constant pool that entry is a member of
    ConstantPoolEntry[] constantPool;
    int tag;
    int name_index;
    String name;
    int descriptor_index;
    String descriptor;
    int[] request;
    boolean built;

    ConstantNameAndTypeInfo(ConstantPoolEntry[] cp){
        constantPool = cp;
        tag = 12;
        name_index = descriptor_index = 0;
        request = new int[2];
        request[0] = 2; request[1] = 2;
        built = false;
    }

    //request parameters
    public int[] request(){
        return request;
    }

    public void give(int n){
        if(name_index == 0)
            name_index = n;
        else descriptor_index = n;
    }

    public int getTag(){
        return tag;
    }

    public boolean isBuilt(){
        return built;
    }

    public void build(){
        if(!built){
            if(!constantPool[name_index].isBuilt())
                constantPool[name_index].build();
            name = constantPool[name_index].toString();

            if(!constantPool[descriptor_index].isBuilt())
                constantPool[descriptor_index].build();
            descriptor = constantPool[descriptor_index].toString();
        }
    }

    public String getDescriptor(){
        return descriptor;
    }

    public String toString(){
        return name;
    }
}
