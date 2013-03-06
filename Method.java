/*
 *
 * Author: Joshua A. Campbell
 *
 * Describes a field:
 * http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.5 
 *
 */

import java.util.ArrayList;

public class Method{

    private UnclassProject project;
    private DescriptorParser descriptorParser;
    private OpcodeDecompiler decompiler;

    private int access_flags;
    public String flags;
    private int name_index;
    private String name;
    private int descriptor_index;
    private String paramDescriptor;
    private String returnDescriptor;
    private int attributes_count;
    //holds values from the attribute table
    private Attribute[] attributes;
    //number of attributes set
    private int attributesSet;

    //list of methods parameters
    private String[] parameters;
    //the method's decompiled code
    private String code;
    //is this method static?
    boolean isStatic;

    Method(UnclassProject p){
        project = p;
        descriptorParser = project.getDescriptorParser();
        decompiler = project.getOpcodeDecompiler();
        attributesSet = 0;
        flags = "";
        code = "";
        isStatic = false;
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
        if((access_flags & 8) != 0){
            flags += "static ";
            isStatic = true;
        }
        if((access_flags & 16) != 0)
            flags += "final ";
        if((access_flags & 32) != 0)
            flags += "synchronized ";
        if((access_flags & 1024) != 0)
            flags += "abstract ";
    }//end setAccessFlags

    //sets the name index (in the constant pool)
    public void setNameIndex(int n){
        name_index = n;
    }//end setNameIndex

    public void setName(String s){
        name = s;
    }
    
    //set descriptor index
    public void setDescripIndex(int n){
        descriptor_index = n;
    }//end setDescripIndex

    //determine the parameters and return types
    //descriptor has the form (params)return
    //save method parameters to parameters
    public void setDescriptor(String descriptor){

        //return descriptor
        returnDescriptor = descriptor.substring(descriptor.indexOf(')')+1, descriptor.length());
        returnDescriptor = descriptorParser.parseNoVar(returnDescriptor);

        //parameter descriptor
        paramDescriptor = descriptor.substring(1, descriptor.indexOf(')'));
        paramDescriptor = "(" + descriptorParser.parseAddVar(paramDescriptor) + ")";
        //save parameters)
        ArrayList<String> varList = descriptorParser.getVariables();
        parameters = new String[ varList.size() ];
        for(int i = 0; i < parameters.length; i++)
            parameters[i] = varList.get(i);

    }

    //sets the attributes_count item
    public void setAttributeCount(int n){
        attributes_count = n;
        attributes = new Attribute[attributes_count];
    }//end setAttributeCount

    //sets the attributes (an attribute structure)
    public void setAttribute(Attribute a){
        attributes[attributesSet++] = a;
    }//end setAttributes

    //fix the .class representation of a descriptor
    //ex: [[[D = double d[][][]
    public String fixDescriptor(String s){
        return ""; 
    }

    public void build(){
        for(int i = 0; i < attributes.length; i++){
            if(attributes[i] instanceof AttributeCode){
                //set up decompiler and decompile JVM bytecode
                AttributeCode attrCode = (AttributeCode) attributes[i];
                decompiler.setStatic(isStatic);
                decompiler.setParameters(parameters);
                decompiler.setMaxLocals( attrCode.getMaxLocals() );
                decompiler.setCode( attrCode.getCode() );
                code = decompiler.decompile();
                //attrCode[i].build();
            }
            else attributes[i].build();
        }
    }

    public String decompile(){
        return "\t" + flags + returnDescriptor + " "
            + name + paramDescriptor + "{\n\n" + code + "\n\t}" + "\n\n";
    }
    
    //
    // Get method(s)
    //
    
    public String[] getParameters(){
        return parameters;
    }

}
