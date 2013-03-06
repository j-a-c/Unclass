
public class Foo{

    public int i;
    private String bar;

    Foo(int a){
        i = a;
    }

    public void loopTest(double s){
        
        this.i = (int) Math.E;
        double d = 0;
        for(int i = 0; i < 10; i++)
            d += 1;
        i++;
    }

    private static int arrayTest(String[] s){
    
        int[] q = new int[5];

        char c = s[0].charAt(0);
        
        q[0] = c;

        return q[0]; 
    }//end arrayTest

    public void ifTest(){

        int a = 0;

        if(a == 1)
            System.out.println("a = 1");
        else if(a == 2)
            System.out.println("a = 2");
        else
            System.out.println("a != 1 && a != 2");
    }//end ifTest
    
    public void switchtest(int a, float b, char c){
    
        switch (a){
            case 1: 
                System.out.println("a is one.");
                break;
            case 10:
                a++;
                System.out.println("a is ten.");
            default:
                System.out.println("a is >= 10");
        }

    }//end switchtest

}
