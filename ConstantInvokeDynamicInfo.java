public class ConstantInvokeDynamicInfo extends ConstantPoolEntry{

    //constant pool that entry is a member of
    ConstantPoolEntry[] constantPool;
    int tag;
    //index to bootstrap_methods array of bootstrap method table
    int bootstrapMethodAttr_index;
    int nameAndType_index;
    String nameAndType;
    int[] request;
    boolean built;

    ConstantInvokeDynamicInfo(ConstantPoolEntry[] cp){
        constantPool = cp;
        tag = 18;
        bootstrapMethodAttr_index = nameAndType_index = 0;
        request = new int[2];
        request[0] = 2; request[1] = 2;
        built = false;
    }

    //request parameters
    public int[] request(){
        return request;
    }

    public void give(int n){
        if(bootstrapMethodAttr_index == 0)
            bootstrapMethodAttr_index = n;
        else nameAndType_index = n;
    }

    public int getTag(){
        return tag;
    }

    public boolean isBuilt(){
        return built;
    }

    public void build(){
        if(!built){
            if(!constantPool[nameAndType_index].isBuilt())
                constantPool[nameAndType_index].build();
            nameAndType = constantPool[nameAndType_index].toString();
        }
    }

    public String toString(){
        return "IMPLEMENT CONSTINVOKEDYNAMINFO";
    }
}
