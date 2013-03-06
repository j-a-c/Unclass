public class ConstantMethodHandleInfo extends ConstantPoolEntry{

    //constant pool that entry is a member of
    ConstantPoolEntry[] constantPool;
    int tag;
    int reference_kind;
    int reference_index;
    String referenceIndex;
    int[] request;
    boolean built;

    ConstantMethodHandleInfo(ConstantPoolEntry[] cp){
        constantPool = cp;
        tag = 15;
        reference_kind = reference_index = 0;
        request = new int[2];
        request[0] = 1; request[1] = 2;
        built = false;
    }

    //request parameters
    public int[] request(){
        return request;
    }

    public void give(int n){
        if(reference_kind == 0)
            reference_kind = n;
        else reference_index = n;
    }

    public int getTag(){
        return tag;
    }

    public boolean isBuilt(){
        return built;
    }

    public void build(){
        if(!built){
            System.out.println("\t\t\tImplement MethodHandleInfo.build()");
            built = true;
        }
    }

    public String toString(){
        return "TODO";
    }
}
