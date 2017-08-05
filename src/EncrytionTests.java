/**
 * Created by Antonio on 2017-08-05.
 */
public class EncrytionTests {

    static EncryptionEngine engine = new EncryptionEngine();

    static int numTests = 0;

    public static void main (String[] args){

//        EncryptionTests();

    }

    public static void EncryptionTests(){
        long start = System.currentTimeMillis();
        String[] array = {"Testing", "This is a space test", "This is a newline\ntest"};
        for (String str : array){
            EncryptionTest(str);
            AdvancedEncryptionTest(str);
            SimpleEncryptionTest(str);
        }
        long end = System.currentTimeMillis();

        System.out.println("\nA total of "+numTests+" tests have passed!\n");
        System.out.println("Took "+time(start, end)+" to complete testing\n");
    }

    public static void EncryptionTest(String input){
        String encrypted = engine.getEncryption(input);
        String decrypted = engine.getDecryption(input);
        if (input.equals(decrypted)){
            numTests++;
        }
        else{
            System.out.println("Encryption Test Failed");
            System.out.println("    Input:      "+input);
            System.out.println("    Encrypted:  "+encrypted);
            System.out.println("    Decrypted:  "+decrypted);
            System.exit(1);
        }
    }
    public static void AdvancedEncryptionTest(String input){
        String encrypted = engine.getAdvancedEncryption(input);
        String decrypted = engine.getAdvancedDecryption(input);
        if (input.equals(decrypted)){
            numTests++;
        }
        else{
            System.out.println("Advanced Encryption Test Failed");
            System.out.println("    Input:      "+input);
            System.out.println("    Encrypted:  "+encrypted);
            System.out.println("    Decrypted:  "+decrypted);
            System.exit(1);
        }
    }

    public static void SimpleEncryptionTest(String input){
        String encrypted = engine.getSimpleEncryption(input);
        String decrypted = engine.getSimpleDecryption(input);
        if (input.equals(decrypted)){
            numTests++;
        }
        else{
            System.out.println("Simple Encryption Test Failed");
            System.out.println("    Input:      "+input);
            System.out.println("    Encrypted:  "+encrypted);
            System.out.println("    Decrypted:  "+decrypted);
            System.exit(1);
        }
    }


    public static String time(long start, long end){
        return time(start, end, true);
    }
    public static String time(long start, long end, boolean inSeconds){
        return (inSeconds ? ((end-start)/1000.0)+" Seconds" : (end-start)+" Milliseconds");
    }

}
