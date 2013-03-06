/*
 * Author: Joshua A. Campbell
 * Represents a .class file
 * Actually reads the file
 * 
 */

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;

public class UnclassProject{

    //input stream for class file
    private DataInputStream input;
    //number of bytes available in file
    private long available;
    //variable generator
    private VarGenerator variableGenerator;
    //descriptor parser
    private DescriptorParser descriptorParser;
    //opcode decompiler
    private OpcodeDecompiler decompiler; 

    private ConstantPoolEntry[] constantPool;
    private ConstantPoolEntry[] interfaces;
    private Field[] fields;
    private Method[] methods;
    private Attribute[] attributes;
    
    private int magic_number;
    private int constant_pool_count;
    private int interaces_count;
    private int fields_count;
    private int methods_count;
    private int attributes_count;

    private int access_flags;
    private int this_class;
    private int super_class;
    private String flags;
    private String name;

    //constructor
    public UnclassProject(String filepath){
        variableGenerator = new VarGenerator();
        descriptorParser = new DescriptorParser(variableGenerator);
        decompiler = new OpcodeDecompiler(this);
        flags = "";
        //initialize input stream
        try{
            input = new DataInputStream(new FileInputStream(filepath));
            available = input.available();

        } catch(Exception e){
            System.out.println("Error in constructing Unclass Project.");
        }
    }//end UnclassProject

    //read bytes
    public void read(){
        
        readMagicNumber();   
        if(magic_number == -889275714){
            readVersion();

            readConstantPool();

            //build constant pool
            //zero-th item in constant pool is used internally by JVM
            for(int i = 1; i < constantPool.length; i++)
                constantPool[i].build();
            
            readClassInfo();
            
            readInterfaces();
            
            readFields();
            readMethods();
            readAttributes();
            //System.out.println("Bytes not read " + available);

            System.out.println("\nBuilding project...");
            build();

            System.out.println("Writing project to out.txt");
            save();
        }
        else
            System.out.println("Not a valid java .class file.");

    }//end read

    //read magic number
    public void readMagicNumber(){
        try{
            magic_number = input.readInt();
            System.out.println("Magic number: " + magic_number );
            available -= 4;   

        } catch(java.io.IOException e){
            System.out.println("Error reading magic number.");
        }
    }//end readMagicNumber

    //read min/max version
    public void readVersion(){
        try{
            System.out.println("Minor/major version: " + input.readUnsignedShort() 
                    + ", " + input.readUnsignedShort() );
            available -= 4;   

        } catch(java.io.IOException e){
            System.out.println("Error reading version number.");
        }
    }//end readVersion

    //read constant pool
    //first 2 bytes are the constant pool count
    //zero-th item in constant pool is used internally by JVM
    public void readConstantPool(){
        try{
            int constantType;
            //number of entries in constant pool
            constant_pool_count = input.readUnsignedShort();
            available -= 2;
            System.out.println("Constant pool count: " + constant_pool_count);
            constantPool = new ConstantPoolEntry[constant_pool_count];
            //read all entries
            for(int i = 1; i < constant_pool_count; i++){
                constantType = input.read();
                //determine entry type
                switch (constantType){
                    case 7: constantPool[i] = new ConstantClassInfo(constantPool); break;
                    case 9: constantPool[i] = new ConstantFieldrefInfo(constantPool); break;
                    case 10: constantPool[i] = new ConstantMethodrefInfo(constantPool); break;
                    case 11: constantPool[i] = new ConstantInterfaceMethodrefInfo(constantPool); break;
                    case 8: constantPool[i] = new ConstantStringInfo(constantPool); break;
                    case 3: constantPool[i] = new ConstantIntegerInfo(constantPool); break;
                    case 4: constantPool[i] = new ConstantFloatInfo(constantPool); break;
                    case 5: constantPool[i] = new ConstantLongInfo(constantPool); break;
                    case 6: constantPool[i] = new ConstantDoubleInfo(constantPool); break;
                    case 12: constantPool[i] = new ConstantNameAndTypeInfo(constantPool); break;
                    case 1: constantPool[i] = new ConstantUtf8Info(constantPool); break;
                    case 15: constantPool[i] = new ConstantMethodHandleInfo(constantPool); break;
                    case 16: constantPool[i] = new ConstantMethodTypeInfo(constantPool); break;
                    case 18: constantPool[i] = new ConstantInvokeDynamicInfo(constantPool); break;
                    default: System.out.println("Wrong constant type " + constantType + "."); 
                }
                available -= 1;

                //give info to entry
                int[] requests = constantPool[i].request();
                for(int j = 0; j < requests.length; j++){
                    switch (requests[j]){
                        case 1: 
                                constantPool[i].give( input.read() );
                                available -= 1; break;
                        case 2: 
                                constantPool[i].give( input.readUnsignedShort() );
                                available -= 2; break;
                        case 4: 
                                input.read(); input.read(); input.read(); input.read();
                                available -= 4; break;
                        case 17:
                                for(int k = 0; k < requests[0]; k++)
                                    constantPool[i].give( input.read() );
                                available -= requests[0]; break;
                        default: System.out.println("Wrong constant request type " + requests[j]);
                    }
                }//end giving info to entry

            }//end determining entries

        } catch(java.io.IOException e){
            System.out.println("Error reading constant pool.");
        }
    }//end readConstantPool

    public void readClassInfo(){
        try{
            access_flags = input.readUnsignedShort(); //access_flags
            this_class = input.readUnsignedShort(); //this_class
            super_class = input.readUnsignedShort(); //super_class

            if((access_flags & 1) != 0)
                flags += "public ";
            if((access_flags & 16) != 0)
                flags += "final ";

            if((access_flags & 512) != 0)
                flags += "interface ";
            else if((access_flags & 16384) != 0)
                flags += "enum";
            else flags += "class ";

            name = constantPool[this_class].toString();


        } catch(java.io.IOException e){
            System.out.println("Error reading class info.");
        }
    }//end readClassInfo

    public void readInterfaces(){
        try{
            //number of interfaces 
            interaces_count = input.readUnsignedShort();
            available -= 2;
            interfaces = new ConstantPoolEntry[interaces_count];
            System.out.println("Number of intefaces: " + interaces_count);
            //read interfaces from constant pool
            for(int i = 0; i < interaces_count; i++){
                interfaces[i] = constantPool[ input.readUnsignedShort() ];
                available -= 2;
            }

        } catch(java.io.IOException e){
            System.out.println("Error reading interfaces.");
        }
    }//end readInterfaces

    public void readFields(){
        try{
            //number of fields
            int fields_count = input.readUnsignedShort();
            available -= 2;
            fields = new Field[fields_count];
            System.out.println("Number of fields: " + fields_count);
            //read fields from the bytes following
            for(int i = 0; i < fields_count; i++){
                fields[i] = new Field(this);
                //access_flags
                fields[i].setAccessFlags( input.readUnsignedShort() );
                //name_index
                fields[i].setNameIndex( input.readUnsignedShort() );
                //descriptor_index
                fields[i].setDescripIndex( input.readUnsignedShort() );
                //attributes_count
                int field_attr_count = input.readUnsignedShort();
                fields[i].setAttributeCount( field_attr_count );
                available -= 8;

                //read attributes for field
                for(int j = 0; j < field_attr_count; j++){
                    int index = input.readUnsignedShort();
                    available -= 2;
                    String attributeName = constantPool[index].toString();
                    fields[i].setAttribute( readSingleAttribute(attributeName) );
                }
            }

        } catch(java.io.IOException e){
            System.out.println("Error reading fields.");
        }
    }//end readFields

    public void readMethods(){
        try{
            //number of methods
            methods_count = input.readUnsignedShort();
            available -= 2;
            methods = new Method[methods_count]; 
            System.out.println("Number of methods: " + methods_count);
            //read methods
            for(int i = 0; i < methods_count; i++){
                methods[i] = new Method(this);
                //method access_flags
                methods[i].setAccessFlags( input.readUnsignedShort() );
                //method name_index
                int method_name_index = input.readUnsignedShort();
                methods[i].setNameIndex( method_name_index );
                methods[i].setName( constantPool[method_name_index].toString() );

                System.out.println("\tMethod name: " + constantPool[method_name_index]);
                //method descriptor_index
                int method_descrip_index = input.readUnsignedShort();
                methods[i].setDescripIndex( method_descrip_index );
                methods[i].setDescriptor( constantPool[method_descrip_index].toString() );
                System.out.println("\t\tDescriptor: " + constantPool[method_descrip_index]);
                //method attributes_count
                int method_attr_count = input.readUnsignedShort();
                methods[i].setAttributeCount( method_attr_count );
                available -= 8;
                
                //read attributes for methods
                for(int j = 0; j < method_attr_count; j++){
                    int index = input.readUnsignedShort();
                    available -= 2;
                    String attributeName = constantPool[index].toString();
                    methods[i].setAttribute( readSingleAttribute(attributeName) );
                }
            }

        } catch(java.io.IOException e){
            System.out.println("Error reading methods.");
        }
    }//end readMethods

    //read attributes for the class file
    public void readAttributes(){
        try{
            //attributes_count
            attributes_count = input.readUnsignedShort();
            available -= 2;
            attributes = new Attribute[attributes_count];
            System.out.println("Number of attributes: " + attributes_count);

            //read all attributes for this class
            for(int i = 0; i < attributes_count; i++){
                int index = input.readUnsignedShort();
                available -= 2;
                String attributeName = constantPool[index].toString();
                attributes[i] = readSingleAttribute(attributeName);
            }

        } catch(java.io.IOException e){
            System.out.println("Error reading attributes.");
        }
    }//end readAttributes

    
    //read a single attribute
    //Cases:
    //  Code:contains the JVM instructions and auxiliary information for a single method
    //  default: unknown, unimplement classes
    public Attribute readSingleAttribute(String attrName){
        try{

            //the Code Attribute
            if(attrName.equals("Code")){
                AttributeCode attr = new AttributeCode();
                System.out.println("\t\t\tAttribute: Code");
                //attribute_length
                attr.setLength( 0x00FFFFFFFF & input.readInt() );
                available -= 4;
                //max_stack
                attr.setMaxStack( input.readUnsignedShort() );
                available -= 2;
                //max_locals
                attr.setMaxLocals( input.readUnsignedShort() );
                available -= 2;
                //code_length
                int codeLength = 0x00FFFFFFFF & input.readInt();
                attr.setCodeLength(codeLength);
                available -= 4;
                //add code to AttributeCode
                for(int i = 0; i < codeLength; i++){
                    int code = 0x0FF & input.read();
                    attr.addCode( code );
                    available -= 1;
                }
                //exception_table_length
                int exception_table_length = input.readUnsignedShort();
                available -= 2;
                attr.setExceptionTableLength(exception_table_length);
                //TODO
                //read exception table
                for(int i = 0; i < exception_table_length; i++){
                    input.readUnsignedShort();
                    input.readUnsignedShort();
                    input.readUnsignedShort();
                    input.readUnsignedShort();
                }

                //number of attributes
                int single_attributes_count = input.readUnsignedShort();
                available -= 2;
                attr.setAttributeCount(single_attributes_count);
                //read attributes for AttributeCode 
                for(int i = 0; i < single_attributes_count; i++){
                    int index = input.readUnsignedShort();
                    available -= 2;
                    String attributeName = constantPool[index].toString();
                    attr.setAttribute( readSingleAttribute(attributeName) );
                }
                return attr;
            }//end Code Attribute

            //IMPLEMENT UNKNOWN ATTRIBUTES
            else{
                System.out.println("\t\t\tUnknown attribute " + attrName);
                int unknown_attribute_length = 0x00FFFFFFFF & input.readInt();
                available -= 2;
                for(int i = 0; i < unknown_attribute_length; i++){
                    input.read();
                    available--;
                }
                return new Attribute();
            }//end case for unknown attribute type

        } catch(java.io.IOException e){
            System.out.println("Error reading an attribute.");
        }
        return null;
    }//end readSingleAttribute
    
    public void build(){
        //build class's field variables
        for(int i = 0; i < fields.length; i++)
            fields[i].build();

        //give field variables to decompiler
        decompiler.setFields();

        //build class's methods
        for(int i = 0; i < methods.length; i++)
            methods[i].build();

        //build class's attributes
        for(int i = 0; i < attributes.length; i++)
            attributes[i].build();
    }//end build

    public void save(){
        try{
            FileWriter fstream = new FileWriter("out.txt");
            BufferedWriter out = new BufferedWriter(fstream);
            
            out.write(flags + name + "{\n\n");

            for(int i = 0; i < fields.length; i++)
                out.write( fields[i].decompile() );
            out.write("\n");
            for(int i = 0; i < methods.length; i++)
                out.write( methods[i].decompile() );
            for(int i = 0; i < attributes.length; i++)
                out.write( attributes[i].decompile() );

            out.write("}");

            out.close();
        } catch(Exception e){
            System.out.println("Error saving file.");
        }
    }//end save]
    

    //
    // Get Methods
    //

    public DescriptorParser getDescriptorParser(){
        return descriptorParser;
    }

    public OpcodeDecompiler getOpcodeDecompiler(){
        return decompiler;
    }

    public ConstantPoolEntry[] getConstantPool(){
        return constantPool;
    }

    public VarGenerator getVariableGenerator(){
        return variableGenerator;
    }
    
    public String[] getFieldVariables(){
        String[] fieldvars = new String[fields.length];

        for(int i = 0; i < fields.length; i++)
            fieldvars[i] = fields[i].getName();

        return fieldvars;
    }

}//end UnclassProject
