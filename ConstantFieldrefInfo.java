public class ConstantFieldrefInfo extends ConstantPoolEntry{

    //constant pool that entry is a member of
    ConstantPoolEntry[] constantPool;
    int tag;
    int class_index;
    String classindex;
    int nameAndType_index;
    String nameAndType;
    int[] request;
    boolean built;

    ConstantFieldrefInfo(ConstantPoolEntry[] cp){
        constantPool = cp;
        tag = 9; 
        class_index = 0;
        nameAndType_index = 0;
        request = new int[2];
        request[0] = 2; request[1] = 2;
        built = false;
    }

    //request parameters
    public int[] request(){
        return request; 
    }

    public void give(int n){
        if(class_index == 0)
            class_index = n;
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
            if(!constantPool[class_index].isBuilt())
                constantPool[class_index].build();
            classindex = constantPool[class_index].toString();
            if(!constantPool[nameAndType_index].isBuilt())
                constantPool[nameAndType_index].build();
            nameAndType = constantPool[nameAndType_index].toString();
        }
        built = true;
    }

    public String toString(){
        return "this." + nameAndType;
    }
}
