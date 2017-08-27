import Tools.Search;

/**
 * Created by Antonio on 2017-08-05.
 */
public class EncrytionTests {

    static EncryptionEngine engine = new EncryptionEngine("password");

    static int numTests = 0;

    public static void main (String[] args){
//            engine.generateRandomKey("./"+engine.defaultKey+engine.fileFormat);

//        String[] common = ("the, and, tha, ent, ion, tio, for, nde, has, nce, edt, tis, oft, sth, men, th, er, on, an, re, he, in, ed, nd, ha, at, en, es, of, or, nt, ea, ti, to, it, st, io, le, is, ou, ar, as, de, rt, ve, ss, ee, tt, ff").split(", ");
//        String[] greek = ("αβγεϵζθϑικμνξοπϖρϱστυφϕχψωΑΒΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ").split("");
//
//        int length = Math.min(common.length, greek.length);
//
//        for (int i = 0; i<length; i++){
//            if (i%6 == 0){
//                System.out.println();
//            }
//            System.out.print("        \""+greek[i]+"\", \""+common[i]+"\",");
//        }


//        String[] array = {"Password", "Password", "password",
////                "This is as secure a Password that I can think of",
////                "This is as secure a password that I can think of",
////                "this is as secure a password that i can come up with",
//                "this is as secure a password that i can come up wit"};
//        for (String str : array){
//            String hashed = engine.hash(str);
//            String extra = engine.replaceExtra(str);
//            System.out.println(str.length()+"     "+str);
//            System.out.println(extra.length()+"     "+extra);
//            System.out.println(hashed.length()+"    "+hashed);
//            System.out.println();
//        }
//        System.out.println();

        EncryptionTests();


        System.out.println(Search.count);

    }

    public static void EncryptionTests(){
        long start = System.currentTimeMillis();
        String[] array = {"Testing", "This is a space test", "This is a newline\ntest", "Username", "Password"};
        for (String str : array){
            EncryptionTest(str);
            AdvancedEncryptionTest(str);
            SimpleEncryptionTest(str);
//            MultipleEncryptionTest(str);
        }
        long end = System.currentTimeMillis();

        System.out.println("\nA total of "+numTests+" tests have passed!\n");
        System.out.println("Took "+time(start, end)+" to complete testing\n");
    }

    public static void EncryptionTest(String input){
        String encrypted = engine.getEncryption(input);
        String decrypted = engine.getDecryption(encrypted);
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
//    public static void MultipleEncryptionTest(String input){
//        String encrypted1 = engine.getSimpleEncryption(input);
//        String encrypted2 = engine.getEncryption(encrypted1);
//        String encrypted3 = engine.getAdvancedEncryption(encrypted2);
//        String encrypted4 = engine.getEncryption(encrypted3);
//        String decrypted4 = engine.getDecryption(encrypted4);
//        String decrypted3 = engine.getAdvancedDecryption(decrypted4);
//        String decrypted2 = engine.getDecryption(decrypted3);
//        String decrypted1 = engine.getSimpleDecryption(decrypted2);
//        if (input.equals(decrypted1)){
//            numTests++;
//        }
//        else{
//            System.out.println("Multiple Encryption Test Failed");
//            System.out.println("    Input:      "+input);
//            System.out.println("    Encrypted:  "+encrypted4);
//            System.out.println("    Decrypted:  "+decrypted1);
//            System.exit(1);
//        }
//    }
    public static void AdvancedEncryptionTest(String input){
        String encrypted = engine.getAdvancedEncryption(input);
        String decrypted = engine.getAdvancedDecryption(encrypted);
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
        String decrypted = engine.getSimpleDecryption(encrypted);
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
