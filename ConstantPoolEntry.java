/*
 *
 * Author: Joshua A. Campbell
 *
 * Abstract class for once of the constant pool classes at:
 * http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4
 *
 */

public abstract class ConstantPoolEntry{

    //request parameters
    //[number params,length param1, length param2,..]
    public abstract int[] request();

    //receive a parameter
    //parameters filled in order listed in specification
    public abstract void give(int n);

    //returns pool entry's tag
    public abstract int getTag();

    public abstract boolean isBuilt();

    public abstract void build();

    public abstract String toString();
}
