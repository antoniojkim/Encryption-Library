import Byte_Encryption.ByteArrayOperations;
import Byte_Encryption.ByteEncryption;
import Byte_Encryption.ByteKeystore;
import String_Encryption.EncryptionEngine;
import String_Encryption.RSA;
import Tools.Print;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import static Byte_Encryption.ByteArrayOperations.bytesToHex;
import static Byte_Encryption.ByteArrayOperations.hexToBytes;

/**
 * Created by Antonio on 2017-08-05.
 */
public class EncrytionTests {

    // mvn install:install-file -Dfile="Encryption Library.jar" -DgroupId=jhk-encryption-library -DartifactId=jhk-encryption-library -Dversion=1.0.1 -Dpackaging=jar

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
//                "This is as secure a Password that I can think of",
//                "This is as secure a password that I can think of",
//                "this is as secure a password that i can come up with",
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

//        EncryptionTests();

//        System.out.println(ByteEncryption.hash("password"));
//        fileToByteArray();
//        System.out.println(((byte)113)^((byte)29));
//        System.out.println(((byte)108)^((byte)29));
//        for (int i = 0; i<100; ++i){
//            System.out.println((char)i+"    "+i);
//        }
//
//        ByteEncryption.printKeystore("./src/Byte_Encryption/keystore.txt");

//        byte[] key = ByteEncryption.generateKey(64);
//        Print.println(key);
//        String keyStr = ByteEncryption.toString(key);
//        System.out.println(keyStr);
//        Print.println(ByteEncryption.unicodeToBytes(keyStr));
//        hashBlockChainTest();
//        hashKeyTest();
//        hexTest();
//        byteKeystoreTest();
//        System.out.println(ByteEncryption.bytesToUnicode(ByteEncryption.generateKey(16)));
//        byteEncryptionTest2();
//        Print.println(ByteEncryption.shiftLeft(new byte[]{1, 2, 3, 4}, 1));
//        Print.println(ByteEncryption.shiftLeft(new byte[]{1, 2, 3, 4}, -1));
//        Print.println(ByteEncryption.shiftRight(new byte[][]{{1, 2, 3, 4}, {5, 6, 7, 8}}));
//        emptyPrivateKeyTest();
        generateKeystoreTest();
    }
    public static void generateKeystoreTest(){
         byte[][] keystore = ByteKeystore.generateKeystore(null);
         Print.println(keystore);
    }
    public static void hexEncryptionTest(){
        ByteEncryption be = new ByteEncryption("");
        String str = "example";
        String encrypted = ByteArrayOperations.bytesToHex(be.encrypt(str.getBytes()));
        System.out.println(encrypted);
        String decrypted = new String(be.decrypt(ByteArrayOperations.hexToBytes(encrypted)));
        System.out.println(decrypted);
    }
    public static void emptyPrivateKeyTest(){
        ByteEncryption be = new ByteEncryption("");
        System.out.println(be.hashString("This is a test"));
    }
    public static void hexTest(){
        ByteEncryption be = new ByteEncryption("Ì\u008Eñ°ã]¤èĊ\u007F>\u0084®)Ă\u008Dăé/\u0095\u0010\u000FĉehÀOf=t\u009C\u0014²ò©\u0019\u008F\u0087\u00170\u0086Û|²5¶a|Čû\u001C ßT¼ûÁy4flR·A");
        byte[] array = be.hash("password".getBytes());
        System.out.println(array.length);
        Print.println(array);
        String hex = bytesToHex(array);
        System.out.println(hex.length());
        System.out.println(hex);
        byte[] bytes = hexToBytes(hex);
        System.out.println(bytes.length);
        Print.println(bytes);
    }
    public static void hashTest(){
        ByteEncryption be1 = new ByteEncryption("Ì\u008Eñ°ã]¤èĊ\u007F>\u0084®)Ă\u008Dăé/\u0095\u0010\u000FĉehÀOf=t\u009C\u0014²ò©\u0019\u008F\u0087\u00170\u0086Û|²5¶a|Čû\u001C ßT¼ûÁy4flR·A");
        ByteEncryption be2 = new ByteEncryption("Ì\u008Eñ°a]¤èĊ\u007F>\u0084®)Ă\u008Dăé/\u0095\u0010\u000FĉehÀOf=t\u009C\u0014²ò©\u0019\u008F\u0087\u00170\u0086Û|²5¶a|Čû\u001C ßT¼ûÁy4flR·A");
        System.out.println(be1.hashString("password"));
        System.out.println(be1.hashString("password"));
        System.out.println(be2.hashString("password"));
        System.out.println(be2.hashString("password"));
    }
    public static void hashKeyTest(){
        ByteEncryption be1 = new ByteEncryption("Ì\u008Eñ°ã]¤èĊ\u007F>\u0084®)Ă\u008Dăé/\u0095\u0010\u000FĉehÀOf=t\u009C\u0014²ò©\u0019\u008F\u0087\u00170\u0086Û|²5¶a|Čû\u001C ßT¼ûÁy4flR·A");
        ByteEncryption be2 = new ByteEncryption("Ì\u008Eñ°a]¤èĊ\u007F>\u0084®)Ă\u008Dăé/\u0095\u0010\u000FĉehÀOf=t\u009C\u0014²ò©\u0019\u008F\u0087\u00170\u0086Û|²5¶a|Čû\u001C ßT¼ûÁy4flR·A");
    }
    public static void byteEncryptionTest1(){
        long start = System.nanoTime();
        ByteEncryption be = new ByteEncryption("Ì\u008Eñ°ã]¤èĊ\u007F>\u0084®)Ă\u008Dăé/\u0095\u0010\u000FĉehÀOf=t\u009C\u0014²ò©\u0019\u008F\u0087\u00170\u0086Û|²5¶a|Čû\u001C ßT¼ûÁy4flR·A");
        String encrypted1 = be.encryptUnicode("This is a test"), encrypted2 = be.encryptUnicode("This is a Test");
        System.out.println(encrypted1);
        System.out.println(encrypted2);
        String decrypted1 = be.decryptUnicode(encrypted1), decrypted2 = be.decryptUnicode(encrypted2);
        System.out.println(decrypted1);
        System.out.println(decrypted2);

        long end = System.nanoTime();
        System.out.println("Took "+((end-start)/1000000.0)+" Milliseconds");
    }
    public static void byteEncryptionTest2(){
        long start = System.nanoTime();
        ByteEncryption be1 = new ByteEncryption("Ì\u008Eñ°ã]¤èĊ\u007F>\u0084®)Ă\u008Dăé/\u0095\u0010\u000FĉehÀOf=t\u009C\u0014²ò©\u0019\u008F\u0087\u00170\u0086Û|²5¶a|Čû\u001C ßT¼ûÁy4flR·A");
        ByteEncryption be2 = new ByteEncryption("Ì\u008Eñ°ã]¤eĊ\u007F>\u0084®)Ă\u008Dăé/\u0095\u0010\u000FĉehÀOf=t\u009C\u0014²ò©\u0019\u008F\u0087\u00170\u0086Û|²5¶a|Čû\u001C ßT¼ûÁy4flR·A");
        String str = "Password";
        String encrypted1 = be1.encryptUnicode(str), encrypted2 = be2.encryptUnicode(str);
        System.out.println(encrypted1);
        System.out.println(encrypted2);
        String decrypted1 = be1.decryptUnicode(encrypted1), decrypted2 = be2.decryptUnicode(encrypted2);
        assert decrypted1.equals(decrypted2);
        System.out.println(decrypted1);
        System.out.println(decrypted2);
        long end = System.nanoTime();
        System.out.println("Took "+((end-start)/1000000.0)+" Milliseconds");
    }
    //NL9VAlVU3A6zOFm04Rp2/6qfieOysOo5N+jNd4R3MAnGFdQ0EwtJtAU0GT8HhZ/Y352404pggQLMFycvIZqNOQ==
    //NL9VAlVU3A6zOFm04Rp2/6qfieOysOo5N+jNd4R3MAnGFdQ0EwtJtAU0GT8HhZ/Y352404pggQLMFycvIZqNOQ==
    public static void hashBlockChainTest(){
        String str = "Password";
        long start = System.nanoTime();
        ByteEncryption be = new ByteEncryption("Ì\u008Eñ°ã]¤èĊ\u007F>\u0084®)Ă\u008Dăé/\u0095\u0010\u000FĉehÀOf=t\u009C\u0014²ò©\u0019\u008F\u0087\u00170\u0086Û|²5¶a|Čû\u001C ßT¼ûÁy4flR·A");
        //        for(int i = 1000000; i<2000000; ++i){
//            byte[] hash = be.hash(String.valueOf(i));
//        }
        int counter = 2000001;
        NONCE: for (int i = 0 ;; ++i){
            --counter;
            byte[] hash = be.hash(str+Integer.toHexString(i));
            if (hash[0] == Byte.MIN_VALUE && hash[1] == Byte.MAX_VALUE){
                System.out.println();
                System.out.println("Nonce:  "+i);
                System.out.println("Hex:  "+Integer.toHexString(i));
                Print.println(hash);
                System.out.println(ByteArrayOperations.bytesToHex(hash));
                break;
            }
            if (counter <= 0){
                counter = 2000000;
                System.out.println(Integer.toHexString(i));
                System.out.println(((System.nanoTime()-start)/1000000.0)+" Milliseconds Elapsed");
                Print.println(hash);
                System.out.println(ByteArrayOperations.bytesToHex(hash));
            }
        }
        long end = System.nanoTime();
        System.out.println("Took "+((end-start)/1000000.0)+" Milliseconds");
    }
    public static void fileToByteArray() {
        long start = System.nanoTime();
        try {
            Path path = Paths.get("./src/String_Encryption/EncryptionEngine.java");
            byte[] data = Files.readAllBytes(path);
            long sum = 0;
            for (byte b : data){
                sum += b;
            }
            System.out.println(sum);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();
        System.out.println("Took "+((end-start)/(1000000.0))+" Milliseconds");
    }

    private static class AllCharacters{
        public void print(){
            try {
                BufferedReader br = new BufferedReader (new InputStreamReader(getClass().getResourceAsStream("Resources/All Characters.txt")));
                String line = null;
                line = br.readLine();
                char[] array = (line).toCharArray();
                HashMap<Integer, Integer> map = new HashMap<>();
                for (int i = 0; i<array.length; ++i) {
                    System.out.println(array[i]+"   "+(int)array[i]);
                    if (map.containsKey((int)array[i])){
                        System.out.println(array[i]+"   "+(int)array[i]+"   found twice");
                        break;
                    }
                    else{
                        map.put((int)array[i], (int)array[i]);
                    }
                }
            } catch (IOException ignored) {}
        }
    }

    public static void EncryptionTests(){
        long start = System.currentTimeMillis();
        symmetricEncryptionTests();
        long end = System.currentTimeMillis();
        System.out.println("\nA total of "+numTests+" symmetric encryption tests have passed!\n");
        System.out.println("Took "+time(start, end)+" to complete testing\n");
        start = System.currentTimeMillis();
        numTests = 0;
        publicKeyEncryptionTests();
        end = System.currentTimeMillis();
        System.out.println("\nA total of "+numTests+" public key encryption tests have passed!\n");
        System.out.println("Took "+time(start, end)+" to complete testing\n");
    }

    public static void symmetricEncryptionTests(){
        String[] array = {"Testing", "This is a space test", "This is a newline\ntest", "Username", "Password",
                "Σ",
                "σ",
                "σΣ#~5"};
        for (String str : array){
            EncryptionTest(str);
            AdvancedEncryptionTest(str);
            SimpleEncryptionTest(str);
            engine.hash(str);
            engine.doubleHash(str);
//            MultipleEncryptionTest(str);
        }
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

    public static void publicKeyEncryptionTests(){
        String[] array = {
                "vV6w3suEs6sWhp473DONoGMHMN8ALU",
                "bQop2s3vNm8A5xQPbCwNCQSJBiRg5z",
                "tupνφϑΑβdΒ9ϕ+5πPβD(*<>^PΔy2ΩkΡO{!#5Ε3bζΔR[pΨΜχαχiΖ[1ιΝΦ"};
        for (String str : array){
            RSATest(str);
        }
    }
    public static void RSATest(String input){
        RSA decryptor = new RSA();
        RSA encryptor = new RSA(decryptor.getPublicKey());
        String encrypted = encryptor.encrypt(input);
        String decrypted = decryptor.decrypt(encrypted);
        if (input.equals(decrypted)){
            numTests++;
        }
        else {
            System.out.println("RSA Encryption Test Failed");
            System.out.println("    Input:      "+input);
            String[] publicKey = encryptor.getPublicKey();
            System.out.println("    e:          "+publicKey[0]);
            System.out.println("    N:          "+publicKey[1]);
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
