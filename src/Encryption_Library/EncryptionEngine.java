package Encryption_Library;

import Encryption_Library.Tools.IO;
import Encryption_Library.Tools.Search;
import Encryption_Library.Tools._Random_;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Antonio on 2017-07-29.
 */
public class EncryptionEngine {

    public static void main(String[] args){

        EncryptionEngine engine = new EncryptionEngine("This is the default password");

        for (int i = 0; i<10; i++){
            System.out.println(engine.createSalt(5));
        }

//        for (int i = 0; i<3; i++){
//            System.out.println(engine.doubleHash("this is a test"));
//        }

    }

    private Key privateKey = null;
    private char[] characters = null;
    private List<Key> keys = new ArrayList<>();

    public EncryptionEngine(){
        this("gtρVO4Οp+rKM0gWξ~1R-γ!ηDΣθBμjQU>]ϱDXΩ4Ηε<ψoCαkΕ8#IU^τϑ@Βe,Ρ)*$cΒah?&Em-.τ~BΦWGΜkφMρΕ?,&ϕ;,*ε)νhVt$G>ΥϱV)Lυ5μρpYEΖWΥWΝkθfaΞmOω");
    }
    public EncryptionEngine(String passphrase){
        loadCharacters();
        loadKeys();

        if (keys != null && characters != null && passphrase.length()>0 && passphrase.length()<=characters.length){
            char[] passphraseArray = passphrase.toCharArray();
            int sum = 0;
            for (int i = 0; i<passphraseArray.length; i++){
                sum += Search.binarySearch(characters, passphraseArray[i]);
            }
            sum = (3*sum)%keys.size();

            passphrase = hash(passphrase, keys.get(sum));

            sum = 0;
            for (int i = 0; i<passphraseArray.length; i++){
                sum += Search.binarySearch(characters, passphraseArray[i]);
            }
            sum = (3*sum)%keys.size();

            for (int i = passphrase.length(); passphrase.length()<characters.length; i++){
                if (i >= characters.length){
                    i %= characters.length;
                }
                char newcharacter = keys.get(sum).getTable()[sum][i];
                passphrase += String.valueOf(newcharacter);
                sum += Search.binarySearch(characters, newcharacter);
                if (sum >= keys.size()){
                    sum %= keys.size();
                }
            }
            passphraseArray = passphrase.toCharArray();
            List<char[]> ciphers = new ArrayList<>();
            ciphers.add(generateSequence(passphraseArray));
            for (int i = 0; i<characters.length; i++){
                ciphers.add(generateSequence(ciphers.get(i)));
            }
            privateKey = new Key(ciphers);
        }
    }

    public char[] getCharacters(){
        loadCharacters();
        return characters;
    }
    public void loadCharacters(){
        if (characters == null){
            try{
                BufferedReader br = new BufferedReader (new InputStreamReader(getClass().getResourceAsStream(resourceDirectory+"All Characters"+fileFormat), "UTF-8"));
                String line = br.readLine();
                characters = line.toCharArray();
                Arrays.sort(characters);
            }catch (IOException e){}
        }
    }
    public List<Key> getKeys(){
        loadKeys();
        return keys;
    }
    public void loadKeys(){
        if (keys.isEmpty()) {
            int keyNumber = 1;
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(resourceDirectory + "Key" + (keyNumber++) + fileFormat), "UTF-8"));
                while (br != null) {
                    keys.add(new Key(br));
                    br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(resourceDirectory + "Key" + (keyNumber++) + fileFormat), "UTF-8"));
                }
            } catch (IOException e) {} catch (NullPointerException e) {}
        }
    }

    public char[] generateSequence(char[] previousSequence){
        if (characters.length == previousSequence.length){
            List<Character> list = new ArrayList<>();
            for (char c : characters){
                list.add(c);
            }
            int index = Search.binarySearch(characters, previousSequence[33]);
            char[] newSequence = new char[previousSequence.length];
            for (int i = 0; i<newSequence.length; i++){
                index += Search.binarySearch(characters, previousSequence[i]);
                if (index >= list.size()){
                    index %= list.size();
                }
                newSequence[i] = list.remove(index);
            }
            return newSequence;
        }
        return null;
    }
    public int getIndicesSum(String str){
        char[] array = str.toCharArray();
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += Search.binarySearch(getCharacters(), array[i]);
        }
        return sum;
    }

    public Key getPrivateKey(){
        return privateKey;
    }
    public String getSalt(){
        return privateKey != null ? privateKey.getSalt() : getDefaultSalt();
    }
    public static String getDefaultSalt(){
        return "σΣ#~5";
    }

    public static final String fileFormat = ".txt";
    public static final String resourceDirectory = "Resources/";

    public static final String keyName = "ΞYΟlρLαβzX";
    public static final String salt = "σΣ#~5";
    public static final String backupEnd = "zβ2yΡΛp";
    public static final String defaultKey = "Encryption_Library.Key";
    public static final String publicKeyName = "Pε[μjlju-Ρ";

    private static String defaultPath = "./Vault Files/";

    public String getSimpleEncryption(String str){
        if (privateKey == null){
            return getSimpleEncryption(str, getKeys().get(getIndicesSum(str)%getKeys().size()));
        }
        return getSimpleEncryption(str, privateKey);
    }
    public String getSimpleEncryption(String str, Key key){
        str = replaceExtra(str);
        String hash = "";
        List<Character> list = key.getCharacterList();
        char[] array = str.toCharArray();
        for (int a = 0; a<array.length; a++){
            try{
                hash += String.valueOf(key.getTable()[a%list.size()][list.indexOf(array[a])]);
            }catch(ArrayIndexOutOfBoundsException e){
                System.out.println(str);
                System.out.println(array[a]);
                System.exit(1);
            }
        }
        return hash;
    }
    public String getEncryption(String str){
//        if (privateKey == null){
//            return encrypt(replaceExtra(str), getKeys().get(getIndicesSum(str)%getKeys().size()));
//        }
        return encrypt(replaceExtra(str), privateKey);
    }
    public String getAdvancedEncryption(String str){
//        if (privateKey == null){
//            int index = getIndicesSum(str);
//            Key key1 = getKeys().get(index%getKeys().size());
//            index *= 3;
//            Key key2 = getKeys().get(index%getKeys().size());
//            return encrypt(getSimpleEncryption(replaceExtra(str), key1), key2);
//        }
        return encrypt(getSimpleEncryption(replaceExtra(str), privateKey), privateKey);
    }
    public String replaceExtra(String str){
        str = str.trim();
        return Search.replace(str,
                "\\uFFFD", "",    " ", "<_>",     "/", "<s>",       "\"", "<q>",        "\n", "<n>",    "\r", "<r>"
//                "/", "λ",       "\"", "ς",        "\n", "η",    "\r", "Γ",
//                "the", "α",       "and", "β",       "tha", "γ",       "ent", "ε",       "ion", "ϵ",       "tio", "ζ",
//                "for", "θ",       "nde", "ϑ",       "has", "ι",       "nce", "κ",       "edt", "μ",       "tis", "ν",
//                "oft", "ξ",       "sth", "ο",       "men", "π",       "th", "ϖ",        "er", "ρ",        "on", "ϱ",
//                "an", "σ",        "re", "τ",        "he", "υ",        "in", "φ",        "ed", "ϕ",        "nd", "χ",
//                "ha", "ψ",        "at", "ω",        "en", "Α",        "es", "Β",        "of", "Δ",        "or", "Ε",
//                "nt", "Ζ",        "ea", "Η",        "ti", "Θ",        "to", "Ι",        "it", "Κ",        "st", "Λ",
//                "io", "Μ",        "le", "Ν",        "is", "Ξ",        "ou", "Ο",        "ar", "Π",        "as", "Ρ",
//                "de", "Σ",        "rt", "Τ",        "ve", "Υ",        "ss", "Φ",        "ee", "Χ",        "tt", "Ψ",
//                "ff", "Ω"
        );
    }
    private String encrypt(String str, Key key){
        if (str.equals("")){
            return "";
        }
        String encrypted = "";
        List<Character> list = key.getCharacterList();
        int length = list.size();
        int index = _Random_.randomint(0, length-1);
        char[] array = str.toCharArray();
        if (index%4 == 0 || index%4 == 1){
            String indices = "";
            for (int a = 0; a<array.length; a++){
                int r = _Random_.randomint(0, length-1);
                indices += String.valueOf(list.get(r));
                try{
                    encrypted += String.valueOf(key.getTable()[r][list.indexOf(array[a])]);
                }catch(ArrayIndexOutOfBoundsException e){
                    System.out.println("Could not Find:  \""+array[a]+"\"");
                    System.exit(1);
                }
            }
            if (index%4 == 0){
                return String.valueOf(list.get(index))+indices+encrypted;
            }
            return String.valueOf(list.get(index))+encrypted+indices;
        }
        else if (index%4 == 2){
            for (int a = 0; a<array.length; a++){
                try{
                    int r = _Random_.randomint(0, Math.min(list.size(), key.getTable().length)-1);
                    encrypted += String.valueOf(list.get(r))+String.valueOf(key.getTable()[r][list.indexOf(array[a])]);
                }catch(ArrayIndexOutOfBoundsException e){
                    System.out.println("Could not Find:  \""+array[a]+"\"");
                    System.exit(1);
                }
            }
            return String.valueOf(list.get(index))+encrypted;
        }
        else{
            for (int a = 0; a<array.length; a++){
                try{
                    int r = _Random_.randomint(0, Math.min(list.size(), key.getTable().length)-1);
                    encrypted += String.valueOf(key.getTable()[r][list.indexOf(array[a])])+String.valueOf(list.get(r));
                }catch(ArrayIndexOutOfBoundsException e){
                    System.out.println("Could not Find:  \""+array[a]+"\"");
                    System.exit(1);
                }
            }
            return String.valueOf(list.get(index))+encrypted;
        }
    }

    public String getSimpleDecryption(String str){
        if (privateKey == null){
            return getSimpleDecryption(str, getKeys().get(getIndicesSum(str)%getKeys().size()));
        }
        return getSimpleDecryption(str, privateKey);
    }
    public String getSimpleDecryption(String hash, Key key){
        String str = "";
        List<Character> list = key.getCharacterList();
        char[] hashArray = hash.toCharArray();
        for (int a = 0; a<hashArray.length; a++){
            int mod = a%list.size();
            for (int b = 0; b<key.getTable().length; b++){
                if (key.getTable()[mod][b] == hashArray[a]){
                    str += String.valueOf(list.get(b));
                    break;
                }
            }
        }
        return returnNormal(str);
    }
    public String getDecryption(String str){
//        if (privateKey == null){
//            return returnNormal(decrypt(str, getKeys().get(getIndicesSum(str)%getKeys().size())));
//        }
        return returnNormal(decrypt(str, privateKey));
    }
    public String getAdvancedDecryption(String str){
//        if (privateKey == null){
//            int index = getIndicesSum(str);
//            Key key1 = getKeys().get(index%getKeys().size());
//            index *= 3;
//            Key key2 = getKeys().get(index%getKeys().size());
//            return returnNormal(getSimpleDecryption(decrypt(str, key1), key2));
//        }
        return returnNormal(getSimpleDecryption(decrypt(str, privateKey), privateKey));
    }
    public String returnNormal(String str){
        str = str.trim();
        return Search.replace(str,
                "<_>", " ",     "<s>", "/",       "<q>", "\"",        "<n>", "\n",    "<r>", "\n"
//                "λ", "/",         "ς", "\"",        "η", "\n",        "Γ", "\r",
//                "α", "the",       "β", "and",       "γ", "tha",       "ε", "ent",       "ϵ", "ion",       "ζ", "tio",
//                "θ", "for",       "ϑ", "nde",       "ι", "has",       "κ", "nce",       "μ", "edt",       "ν", "tis",
//                "ξ", "oft",       "ο", "sth",       "π", "men",       "ϖ", "th",        "ρ", "er",        "ϱ", "on",
//                "σ", "an",        "τ", "re",        "υ", "he",        "φ", "in",        "ϕ", "ed",        "χ", "nd",
//                "ψ", "ha",        "ω", "at",        "Α", "en",        "Β", "es",        "Δ", "of",        "Ε", "or",
//                "Ζ", "nt",        "Η", "ea",        "Θ", "ti",        "Ι", "to",        "Κ", "it",        "Λ", "st",
//                "Μ", "io",        "Ν", "le",        "Ξ", "is",        "Ο", "ou",        "Π", "ar",        "Ρ", "as",
//                "Σ", "de",        "Τ", "rt",        "Υ", "ve",        "Φ", "ss",        "Χ", "ee",        "Ψ", "tt",
//                "Ω", "ff"
        );
    }
    private String decrypt (String str, Key key){
        if (str.equals("")){
            return "";
        }
        String decrypted = "";
        List<Character> list = key.getCharacterList();
        if (str.length()%2 != 0){
            int index = list.indexOf(str.charAt(0));
            str = str.substring(1);
            if (index%4 == 0){
                int half = str.length()/2;
                char[] indices = str.substring(0, half).toCharArray();
                char[] encrypted = str.substring(half).toCharArray();
                for (int a = 0; a<encrypted.length; a++){
                    decrypted += String.valueOf(list.get(Search.linearSearch(key.getTable()[list.indexOf(indices[a])], encrypted[a])));
                }
            }
            else if (index%4 == 1){
                int half = str.length()/2;
                char[] indices = str.substring(half).toCharArray();
                char[] encrypted = str.substring(0, half).toCharArray();
                for (int a = 0; a<encrypted.length; a++){
                    decrypted += String.valueOf(list.get(Search.linearSearch(key.getTable()[list.indexOf(indices[a])], encrypted[a])));
                }
            }
            else if (index%4 == 2){
                char[] array = str.toCharArray();
                for (int a = 1; a<array.length; a+=2){
                    try{
                        decrypted += String.valueOf(list.get(Search.linearSearch(key.getTable()[list.indexOf(array[a-1])], array[a])));
                    }catch(ArrayIndexOutOfBoundsException e){
                        System.out.println("Failed");
                        System.out.println(str);
                        System.out.println(str.substring(a-1, a+1));
                        System.exit(1);
                    }
                }
            }
            else {
                char[] array = str.toCharArray();
                for (int a = 1; a<array.length; a+=2){
                    try{
                        decrypted += String.valueOf(list.get(Search.linearSearch(key.getTable()[list.indexOf(array[a])], array[a-1])));
                    }catch(IndexOutOfBoundsException e){
                        System.out.println("Failed");
                        System.out.println(str);
                        System.out.println(str.substring(a-1, a+1));
                        System.exit(1);
                    }
                }
            }
            return decrypted;
        }
        return "Failed to Decrypt";
    }

    public String hash(String str){ // Unidirectional Hash algorithm.
        int index = 0;
        char[] array = str.toCharArray();
        for (int i = 0; i<array.length; i++){
            index += Search.binarySearch(getCharacters(), array[i]);
        }
        return hash(str, getKeys().get(index%getKeys().size()));
    }
    public String hash(String str, Key key){ // Unidirectional Hash algorithm.
        if (key.isValidKey()){
            str = replaceExtra(str);
            int start = str.length();
            int[] indices = null;
            int size = 25;
            while (indices == null){
                if (start < size){
                    indices = new int[size];
                    break;
                }
                size += 5;
            }
            if (indices != null){
                for (int i = 0; i<str.length(); i++){
                    indices[i] = Search.linearSearch(key.getCharacters(), str.charAt(i));
                    start += indices[i];
                }
                for (int i = str.length(); i<indices.length; i++){
                    indices[i] = indices[i%str.length()];
                    start += indices[i];
                }
                String hash = "";
                for (int i : indices){
                    start += i;
                    if (start >= key.getCharacters().length){
                        start %= key.getCharacters().length;
                    }
                    hash += key.getTable()[start][i];
                }
                return hash;
            }
        }
        return "Could not Hash - "+str;
    }


    public String doubleHash(String str){ // Unidirectional Hash algorithm.
        int index = getIndicesSum(str)%getKeys().size();
        Key key1 = getKeys().get(index);
        index = (3 * index) % getKeys().size();
        Key key2 = getKeys().get(index);
        return doubleHash(str, key1, key2);
    }
    public String doubleHash(String str, Key key1, Key key2){
        return hash(hash(str, key1), key2);
    }

    final private char[] forbidden = {'\"', '#', '*', '/', ':', '<', '>', '?', '\\', '|'};
    public String generateRandom(int size){
        String encrypted = "";
        if (privateKey != null) {
            while (encrypted.length() < size) {
                int r = _Random_.randomint(0, privateKey.getCharacters().length - 1);
                if (Search.binarySearch(forbidden, privateKey.getCharacters()[r]) == -1) {
                    encrypted += privateKey.getCharacters()[r];
                }
            }
        }
        return encrypted;
    }
    public String createSalt(int size){
        String salt = "";
        if (privateKey != null){
            for (int a = 0; a<size; a++){
                salt += String.valueOf(privateKey.getCharacters()[_Random_.randomint(0, privateKey.getCharacters().length-1)]);
            }
        }
        return salt;
    }

    public void generateKeystore(String directory, Character... additional){
        if (directory == null || directory.length() == 0){
            directory = "./src/Encryption_Library/Resources/";
        }
        for (int i = 1   ; i<=144; i++){
            generateRandomKey(directory+"Key"+i+".txt");
        }
    }
    public void generateRandomKey(String path, Character... additional){
        PrintWriter pr = IO.printwriter(path);
        List<Character> list = new ArrayList<>();
        for (int a = -1; a<getCharacters().length; a++){
            for (char c : getCharacters()){
                list.add(c);
            }
            list.addAll(Arrays.asList(additional));
            while(!list.isEmpty()){
                int r = _Random_.randomint(0, list.size()-1);
                pr.print(list.remove(r));
            }
            pr.println();
        }
        pr.close();
    }

}
