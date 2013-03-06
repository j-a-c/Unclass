public class ConstantMethodrefInfo extends ConstantPoolEntry{

    //constant pool that entry is a member of
    ConstantPoolEntry[] constantPool;
    
    int tag;
    int class_index;
    String classindex;
    int nameAndType_index;

    String nameAndType;
    String descriptor;
    String paramDescriptor;
    String returnDescriptor;
    
    int[] request;
    boolean built;

    ConstantMethodrefInfo(ConstantPoolEntry[] cp){
        constantPool = cp;
        tag = 10;
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

            ConstantNameAndTypeInfo attr = 
                (ConstantNameAndTypeInfo) constantPool[nameAndType_index];
            nameAndType = attr.toString();
            descriptor = attr.getDescriptor();
        }
        built = true;
    }

    public String toString(){
        return nameAndType; 
    }

/*
 *
 * Get methods
 *
 */

    public int getNumberOfParameters(){

        paramDescriptor = 
            descriptor.substring(1, descriptor.indexOf(')'));

        VarGenerator var = new VarGenerator();
        DescriptorParser parser = new DescriptorParser(var);
        paramDescriptor = parser.parseAddVar(paramDescriptor);

        if(paramDescriptor.equals("")){
            return 0;
        }
        //count number of commas to find number of params
        else{
            String findStr = ",";

            int lastIndex = 0;
            int count = 1;

            while(lastIndex != -1){
                lastIndex = paramDescriptor.indexOf(findStr,lastIndex);

                if( lastIndex != -1){
                    count++;
                    lastIndex += findStr.length();
                }
            }
            return count;
        }
    }//end getNumberOfParameters()

    public String getReturnDescriptor(){
        returnDescriptor = descriptor.substring(
                descriptor.indexOf(')')+1, descriptor.length());
        return returnDescriptor;
    }

    public int getTag(){
        return tag;
    }

}
