import Tools.IO;
import Tools.Search;
import Tools._Random_;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Antonio on 2017-07-29.
 */
public class EncryptionEngine {

    public static void main(String[] args){

        EncryptionEngine engine = new EncryptionEngine("passwords");

//        System.out.println(engine.hash("password"));
//        String encryption = engine.getSimpleEncryption("password");
//        System.out.println(encryption+" = "+engine.getSimpleDecryption(encryption));
//        for (int i = 0; i<2; i++){
//            encryption = engine.getEncryption("password");
//            System.out.println(encryption+" = "+engine.getDecryption(encryption));
//            encryption = engine.getAdvancedEncryption("password");
//            System.out.println(encryption+" = "+engine.getAdvancedDecryption(encryption));
//        }

//        for (int i = 51; i<=144; i++){
//            engine.generateRandomKey("./src/Resources/Key"+i+".txt");
//        }
    }

    private Key privateKey = null;
    private static char[] characters = null;
    private static List<Key> keys = new ArrayList<>();

    public EncryptionEngine(){
        loadCharacters();
        loadKeys();
    }
    public EncryptionEngine(String passphrase){
        this();

        if (keys != null && characters != null && passphrase.length()>0 && passphrase.length()<=characters.length){
            int[] indices = new int[characters.length];
            char[] passphraseArray = passphrase.toCharArray();
            for (int i = 0; i<passphraseArray.length; i++){
                indices[i] = Search.binarySearch(characters, passphraseArray[i]);
            }
            int sum = 0;    for (int i : indices){  sum += i; }
            if (sum > keys.size()){
                sum %= keys.size();
            }

            passphrase = hash(passphrase, keys.get(sum));

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
            ciphers.add(generateSequence(characters, passphraseArray));
            for (int i = 0; i<characters.length; i++){
                ciphers.add(generateSequence(characters, ciphers.get(i)));
            }
            privateKey = new Key(ciphers);
        }
    }

    public char[] getCharacters(){
        if (characters == null){
            loadCharacters();
        }
        return characters;
    }
    public void loadCharacters(){
        try{
            BufferedReader br = new BufferedReader (new InputStreamReader(getClass().getResourceAsStream(resourceDirectory+"All Characters"+fileFormat), "UTF-8"));
            String line = br.readLine();
            characters = line.toCharArray();
            Arrays.sort(characters);
        }catch (IOException e){}
    }
    public List<Key> getKeys(){
        if (keys.isEmpty()){
            loadKeys();
        }
        return keys;
    }
    public void loadKeys(){
        int keyNumber = 1;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(resourceDirectory + "Key" + (keyNumber++) + fileFormat), "UTF-8"));
            while (br != null) {
                keys.add(new Key(br));
                br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(resourceDirectory + "Key" + (keyNumber++) + fileFormat), "UTF-8"));
            }
        } catch (IOException e) {} catch (NullPointerException e) {}
    }

    public char[] generateSequence(char[] characters, char[] previousSequence){
        if (characters.length == previousSequence.length){
            List<Character> list = new ArrayList<>();
            for (char c : characters){
                list.add(c);
            }
            int index = Search.binarySearch(characters, previousSequence[3]);
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

    public static final String fileFormat = ".txt";
    public static final String resourceDirectory = "Resources/";

    public static final String keyName = "ΞYΟlρLαβzX";
    public static final String settingsName = "-$aCνjVehE";
    public static final String salt = "σΣ#~5";
    public static final String backupEnd = "zβ2yΡΛp";
    public static final String defaultKey = "Key";
    public static final String publicKeyName = "Pε[μjlju-Ρ";

    private static String defaultPath = "./Vault Files/";

    public String getEncryption(String str){
        return encrypt(replaceExtra(str));
    }
    public String getSimpleEncryption(String str){
        if (privateKey != null){
            str = replaceExtra(str);
            String hash = "";
            List<Character> list = privateKey.getCharacterList();
            char[] array = str.toCharArray();
            for (int a = 0; a<array.length; a++){
                try{
                    hash += String.valueOf(privateKey.getTable()[a%list.size()][list.indexOf(array[a])]);
                }catch(ArrayIndexOutOfBoundsException e){
                    System.out.println(str);
                    System.out.println(str.substring(a, a+1));
                    System.exit(1);
                }
            }
            return hash;
        }
        return "Failed to Encrypt - "+str;
    }
    public String getAdvancedEncryption(String str){
        return encrypt(getSimpleEncryption(replaceExtra(str)));
    }
    public String replaceExtra(String str){
        str = str.trim();
        return Search.replace(str,
                "\\uFFFD", "",    " ", "",     "/", "λ",       "\"", "ς",        "\n", "η",    "\r", "Γ",
                "the", "α",       "and", "β",       "tha", "γ",       "ent", "ε",       "ion", "ϵ",       "tio", "ζ",
                "for", "θ",       "nde", "ϑ",       "has", "ι",       "nce", "κ",       "edt", "μ",       "tis", "ν",
                "oft", "ξ",       "sth", "ο",       "men", "π",       "th", "ϖ",        "er", "ρ",        "on", "ϱ",
                "an", "σ",        "re", "τ",        "he", "υ",        "in", "φ",        "ed", "ϕ",        "nd", "χ",
                "ha", "ψ",        "at", "ω",        "en", "Α",        "es", "Β",        "of", "Δ",        "or", "Ε",
                "nt", "Ζ",        "ea", "Η",        "ti", "Θ",        "to", "Ι",        "it", "Κ",        "st", "Λ",
                "io", "Μ",        "le", "Ν",        "is", "Ξ",        "ou", "Ο",        "ar", "Π",        "as", "Ρ",
                "de", "Σ",        "rt", "Τ",        "ve", "Υ",        "ss", "Φ",        "ee", "Χ",        "tt", "Ψ",
                "ff", "Ω");
    }
    private String encrypt(String str){
        if (privateKey != null){
            if (str.equals("")){
                return "";
            }
            String key = "";
            String encrypted = "";
            List<Character> list = privateKey.getCharacterList();
            int length = list.size();
            int index = _Random_.randomint(0, length-1);
            char[] array = str.toCharArray();
            if (index%4 == 0 || index%4 == 1){
                for (int a = 0; a<array.length; a++){
                    int r = _Random_.randomint(0, length-1);
                    key += String.valueOf(list.get(r));
                    try{
                        encrypted += String.valueOf(privateKey.getTable()[r][list.indexOf(array[a])]);
                    }catch(ArrayIndexOutOfBoundsException e){
                        System.out.println("Could not Find:  \""+array[a]+"\"");
                        System.exit(1);
                    }
                }
                if (index%4 == 0){
                    return String.valueOf(list.get(index))+key+encrypted;
                }
                return String.valueOf(list.get(index))+encrypted+key;
            }
            else if (index%4 == 2){
                for (int a = 0; a<array.length; a++){
                    try{
                        int r = _Random_.randomint(0, Math.min(list.size(), privateKey.getTable().length)-1);
                        encrypted += String.valueOf(list.get(r))+String.valueOf(privateKey.getTable()[r][list.indexOf(array[a])]);
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
                        int r = _Random_.randomint(0, Math.min(list.size(), privateKey.getTable().length)-1);
                        encrypted += String.valueOf(privateKey.getTable()[r][list.indexOf(array[a])])+String.valueOf(list.get(r));
                    }catch(ArrayIndexOutOfBoundsException e){
                        System.out.println("Could not Find:  \""+array[a]+"\"");
                        System.exit(1);
                    }
                }
                return String.valueOf(list.get(index))+encrypted;
            }
        }
        return "Failed to Encrypt \""+str+"\":   No Key has been loaded";
    }

    public String getDecryption(String str){
        return returnNormal(decrypt(str));
    }
    public String getSimpleDecryption(String hash){
        if (privateKey != null){
            String str = "";
            List<Character> list = privateKey.getCharacterList();
            char[] hashArray = hash.toCharArray();
            for (int a = 0; a<hashArray.length; a++){
                int mod = a%list.size();
                for (int b = 0; b<privateKey.getTable().length; b++){
                    if (privateKey.getTable()[mod][b] == hashArray[a]){
                        str += String.valueOf(list.get(b));
                        break;
                    }
                }
            }
            return returnNormal(str);
        }
        return "Failed to Decrypt - "+hash;
    }
    public String getAdvancedDecryption(String str){
        return returnNormal(getSimpleDecryption(decrypt(str)));
    }
    public String returnNormal(String str){
        str = str.trim();
        return Search.replace(str,
                "", " ",         "λ", "/",         "ς", "\"",        "η", "\n",        "Γ", "\r",
                "α", "the",       "β", "and",       "γ", "tha",       "ε", "ent",       "ϵ", "ion",       "ζ", "tio",
                "θ", "for",       "ϑ", "nde",       "ι", "has",       "κ", "nce",       "μ", "edt",       "ν", "tis",
                "ξ", "oft",       "ο", "sth",       "π", "men",       "ϖ", "th",        "ρ", "er",        "ϱ", "on",
                "σ", "an",        "τ", "re",        "υ", "he",        "φ", "in",        "ϕ", "ed",        "χ", "nd",
                "ψ", "ha",        "ω", "at",        "Α", "en",        "Β", "es",        "Δ", "of",        "Ε", "or",
                "Ζ", "nt",        "Η", "ea",        "Θ", "ti",        "Ι", "to",        "Κ", "it",        "Λ", "st",
                "Μ", "io",        "Ν", "le",        "Ξ", "is",        "Ο", "ou",        "Π", "ar",        "Ρ", "as",
                "Σ", "de",        "Τ", "rt",        "Υ", "ve",        "Φ", "ss",        "Χ", "ee",        "Ψ", "tt",
                "Ω", "ff");
    }
    private String decrypt (String str){
        if (privateKey != null){
            if (str.equals("")){
                return "";
            }
            String decrypted = "";
//            List<String> list = new ArrayList<>(Arrays.asList(characters));
//            List<List<String>> ciphers = new ArrayList<>();
//            for (int a = 0; a<cipher.length; a++){
//                ciphers.add(Arrays.asList(cipher[a]));
//            }
            if (str.length()%2 != 0){
                int index = Search.linearSearch(privateKey.getCharacters(), str.charAt(0));
                str = str.substring(1);
                if (index%4 == 0){
                    int half = str.length()/2;
                    char[] key = str.substring(0, half).toCharArray();
                    char[] encrypted = str.substring(half).toCharArray();
                    for (int a = 0; a<encrypted.length; a++){
                        decrypted += String.valueOf(privateKey.getCharacters()[Search.linearSearch(privateKey.getTable()[Search.linearSearch(privateKey.getCharacters(), key[a])], encrypted[a])]);
//                        decrypted += list.get(ciphers.get(list.indexOf(privateKey.substring(a, a+1))).indexOf(encrypted.substring(a, a+1)));
                    }
                }
                else if (index%4 == 1){
                    int half = str.length()/2;
                    char[] key = str.substring(half).toCharArray();
                    char[] encrypted = str.substring(0, half).toCharArray();
                    for (int a = 0; a<encrypted.length; a++){
                        decrypted += String.valueOf(privateKey.getCharacters()[Search.linearSearch(privateKey.getTable()[Search.linearSearch(privateKey.getCharacters(), key[a])], encrypted[a])]);
//                        decrypted += list.get(ciphers.get(list.indexOf(privateKey.substring(a, a+1))).indexOf(encrypted.substring(a, a+1)));
                    }
                }
                else if (index%4 == 2){
                    char[] array = str.toCharArray();
                    for (int a = 1; a<array.length; a+=2){
                        try{
                            decrypted += String.valueOf(privateKey.getCharacters()[Search.linearSearch(privateKey.getTable()[Search.linearSearch(privateKey.getCharacters(), array[a-1])], array[a])]);
//                            decrypted += list.get(ciphers.get(list.indexOf(str.substring(a, a+1))).indexOf(str.substring(a+1, a+2)));
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
                            decrypted += String.valueOf(privateKey.getCharacters()[Search.linearSearch(privateKey.getTable()[Search.linearSearch(privateKey.getCharacters(), array[a])], array[a-1])]);
//                            decrypted += list.get(ciphers.get(list.indexOf(str.substring(a+1, a+2))).indexOf(str.substring(a, a+1)));
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
        }
        return "Failed to Decrypt";
    }

    public String hash(String str){ // Unidirectional Hash algorithm.
        int index = 0;
        char[] array = str.toCharArray();
        for (int i = 0; i<array.length; i++){
            index += Search.binarySearch(characters, array[i]);
        }
        return hash(str, keys.get(index%keys.size()));
    }
    public String hash(String str, Key key){ // Unidirectional Hash algorithm.
        if (key.isValidKey()){
            str = replaceExtra(str);
            int start = str.length();
            int[] indices = null;
            int size = 15;
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

    public void generateRandomKey(String path, Character... additional){
        PrintWriter pr = IO.printwriter(path);
        List<Character> list = new ArrayList<>();
        for (int a = -1; a<characters.length; a++){
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
    public String createSalt(int size){
        String salt = "";
        if (privateKey != null){
            for (int a = 0; a<size; a++){
                salt += privateKey.getCharacters()[_Random_.randomint(0, characters.length-1)];
            }
        }
        return salt;
    }

}
