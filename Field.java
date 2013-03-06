/*
 *
 * Author: Joshua A. Campbell
 *
 * Describes a field:
 * http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.5 
 *
 */

public class Field{

    UnclassProject project;
    ConstantPoolEntry[] constantPool;
    DescriptorParser descriptorParser;

    private int access_flags;
    private String flags;
    private int name_index;
    private String name;
    private int descriptor_index;
    private String descriptor;
    private int attributes_count;
    //holds values from the attribute table
    private Attribute[] attributes;
    //number of attributes set
    private int attributesSet;

    Field(UnclassProject p){
        project = p;
        constantPool = project.getConstantPool();
        descriptorParser = project.getDescriptorParser();
        attributesSet = 0;
        flags = "";
    }
    
    //sets the access flag
    public void setAccessFlags(int n){
        access_flags = n;

        if((access_flags & 1) != 0)
            flags += "public ";
        if((access_flags & 2) != 0)
            flags += "private ";
        if((access_flags & 4) != 0)
            flags += "protected ";
        if((access_flags & 8) != 0)
            flags += "static ";
        if((access_flags & 16) != 0)
            flags += "final ";
        if((access_flags & 64) != 0)
            flags += "volatile ";
         if((access_flags & 128) != 0)
            flags += "transient ";       
        if((access_flags & 16384) != 0)
            flags += "enum ";
    }//end setAccessFlags

    //sets the name index (in the constant pool)
    public void setNameIndex(int n){
        name_index = n;
        name = constantPool[name_index].toString();
    }//end setNameIndex
    
    //set descriptor index
    public void setDescripIndex(int n){
        descriptor_index = n;
        descriptor = constantPool[descriptor_index].toString(); //descriptor from constant pool
        descriptor = descriptorParser.parseNoVar(descriptor);
    }//end setDescripIndex

    //sets the attributes_count item
    public void setAttributeCount(int n){
        attributes_count = n;
        attributes = new Attribute[attributes_count];
    }//end setAttributeCount

    //sets the attributes (an attribute structure)
    public void setAttribute(Attribute a){
        attributes[attributesSet++] = a;
    }//end setAttributes

    public void build(){
    
    }

    public String decompile(){
        return "\t" + flags + descriptor + " " + name + ";\n"; 
    }
    

    //
    // Get method(s)
    //

    public String getName(){
        return "this." + name;
    }
}
