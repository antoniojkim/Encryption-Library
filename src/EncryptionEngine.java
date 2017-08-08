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

    public static final String keyName = "ΞYΟlρLαβzX";
    public static final String settingsName = "-$aCνjVehE";
    public static final String salt = "σΣ#~5";
    public static final String backupEnd = "zβ2yΡΛp";
    public static final String fileFormat = ".txt";
    public static final String defaultKey = "Key";
    public static final String publicKeyName = "Pε[μjlju-Ρ";
    public static final String privateKey = "Private Key";

    private static String defaultPath = "./Vault Files/";

    public EncryptionEngine(){
        open(true);
    }
    public EncryptionEngine(boolean extra){
        open(extra);
    }
    public EncryptionEngine(String path){
        loadKey(IO.filereader(path));
    }
    public EncryptionEngine(BufferedReader br){
        loadKey(br);
    }

    private boolean opened = false;

    private String[] characters;

    private String[][] cipher;

    private String publicKey = "";

    private void open(boolean useDefaultKey){
        if (!opened){
            BufferedReader br = null;
            try {
                if (!useDefaultKey){
                    try{
                        br = new BufferedReader (new FileReader(defaultPath+keyName+fileFormat));
                        if (br.readLine() == null){
                            br = new BufferedReader (new InputStreamReader(getClass().getResourceAsStream(defaultKey+fileFormat), "UTF-8"));
                        }
                        else{
                            br = new BufferedReader (new FileReader(defaultPath+keyName+fileFormat));
                        }
                    } catch (FileNotFoundException e){
                        br = new BufferedReader (new InputStreamReader(getClass().getResourceAsStream("Resources/"+defaultKey+fileFormat), "UTF-8"));
                    } catch (IOException ex) {
                        br = new BufferedReader (new InputStreamReader(getClass().getResourceAsStream("Resources/"+defaultKey+fileFormat), "UTF-8"));
                    }
                }
                else{
                    br = new BufferedReader (new InputStreamReader(getClass().getResourceAsStream("Resources/"+defaultKey+fileFormat), "UTF-8"));
                }
            } catch (UnsupportedEncodingException | NullPointerException ex) {}
            if (br == null){
                return;
            }
            loadKey(br);
//            }
        }
    }
    private void loadKey(BufferedReader br){
        try{
            List<String> lines = new ArrayList<>();
            String line = br.readLine();
            while (line != null){
                lines.add(Search.replace(line, "\uFFFD", "",    " ", ""));
                line = br.readLine();
            }
            characters = lines.get(0).split("");
            cipher = new String[characters.length][characters.length];
            for (int a = 1; a<lines.size(); a++){
                cipher[a-1] = lines.get(a).split("");
            }
            opened = true;
        }catch(IOException e){}
    }

    public String getEncryption(String str){
        return encrypt(replaceExtra(str));
    }
    public String getSimpleEncryption(String str){
        if (!opened){
            open(true);
        }
        if (opened){
            str = replaceExtra(str);
            String hash = "";
            List<String> list = new ArrayList<>(Arrays.asList(characters));
            for (int a = 0; a<str.length(); a++){
                try{
                    hash += cipher[a%list.size()][list.indexOf(str.substring(a, a+1))];
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
        return encrypt(hash(replaceExtra(str)));
    }
    public String replaceExtra(String str){
        str = str.trim();
        return Search.replace(str,
                "\\uFFFD", "",    " ", "",     "/", "λ",
                "\"", "ς",        "\n", "η",    "\r", "Γ");
    }
    private String encrypt(String str){
        if (!opened){
            open(true);
        }
        if (opened){
            if (str.equals("")){
                return "";
            }
            String key = "";
            String encrypted = "";
            List<String> list = new ArrayList<>(Arrays.asList(characters));
            int length = list.size();
            int index = _Random_.randomint(0, length-1);
            if (index%4 == 0){
                for (int a = 0; a<str.length(); a++){
                    int r = _Random_.randomint(0, length-1);
                    key += list.get(r);
                    try{
                        encrypted += cipher[r][list.indexOf(str.substring(a, a+1))];
                    }catch(ArrayIndexOutOfBoundsException e){
                        System.out.println("Could not Find:  \""+str.substring(a, a+1)+"\"");
                        System.exit(1);
                    }
                }
                return list.get(index)+key+encrypted;
            }
            else if (index%4 == 1){
                for (int a = 0; a<str.length(); a++){
                    int r = _Random_.randomint(0, length-1);
                    key += list.get(r);
                    try{
                        encrypted += cipher[r][list.indexOf(str.substring(a, a+1))];
                    }catch(ArrayIndexOutOfBoundsException e){
                        System.out.println("Could not Find:  \""+str.substring(a, a+1)+"\"");
                        System.exit(1);
                    }
                }
                return list.get(index)+encrypted+key;
            }
            else if (index%4 == 2){
                for (int a = 0; a<str.length(); a++){
                    try{
                        int r = _Random_.randomint(0, Math.min(list.size(), cipher.length)-1);
                        encrypted += list.get(r)+cipher[r][list.indexOf(str.substring(a, a+1))];
                    }catch(ArrayIndexOutOfBoundsException e){
                        System.out.println("Could not Find:  \""+str.substring(a, a+1)+"\"");
                        System.exit(1);
                    }
                }
                return list.get(index)+encrypted;
            }
            else{
                for (int a = 0; a<str.length(); a++){
                    try{
                        int r = _Random_.randomint(0, Math.min(list.size(), cipher.length)-1);
                        encrypted += cipher[r][list.indexOf(str.substring(a, a+1))]+list.get(r);
                    }catch(ArrayIndexOutOfBoundsException e){
                        System.out.println("Could not Find:  \""+str.substring(a, a+1)+"\"");
                        System.exit(1);
                    }
                }
                return list.get(index)+encrypted;
            }
        }
        return "Failed to Encrypt \""+str+"\":   No Key has been loaded";
    }

    public String getDecryption(String str){
        return returnNormal(decrypt(str));
    }
    public String getSimpleDecryption(String hash){
        if (!opened){
            open(true);
        }
        if (opened){
            String str = "";
            List<String> list = new ArrayList<>(Arrays.asList(characters));
            for (int a = 0; a<hash.length(); a++){
                int mod = a%list.size();
                String c = hash.substring(a, a+1);
                for (int b = 0; b<cipher.length; b++){
                    if (cipher[mod][b].equals(c)){
                        str += list.get(b);
                        break;
                    }
                }
            }
            return returnNormal(str);
        }
        return "Failed to Decrypt - "+hash;
    }
    public String getAdvancedDecryption(String str){
        return returnNormal(unhash(decrypt(str)));
    }
    public String returnNormal(String str){
        str = str.trim();
        return Search.replace(str,
                "", " ",     "λ", "/",
                "ς", "\"",    "η", "\n"     , "Γ", "\r");
    }
    private String decrypt (String str){
        if (!opened){
            open(true);
        }
        if (opened){
            if (str.equals("")){
                return "";
            }
            String decrypted = "";
            List<String> list = new ArrayList<>(Arrays.asList(characters));
            List<List<String>> ciphers = new ArrayList<>();
            for (int a = 0; a<cipher.length; a++){
                ciphers.add(Arrays.asList(cipher[a]));
            }
            if (str.length()%2 != 0){
                int index = list.indexOf(str.substring(0, 1));
                str = str.substring(1);
                if (index%4 == 0){
                    int half = str.length()/2;
                    String key = str.substring(0, half);
                    String encrypted = str.substring(half);
                    for (int a = 0; a<encrypted.length(); a++){
                        decrypted += list.get(ciphers.get(list.indexOf(key.substring(a, a+1))).indexOf(encrypted.substring(a, a+1)));
                    }
                }
                else if (index%4 == 1){
                    int half = str.length()/2;
                    String key = str.substring(half);
                    String encrypted = str.substring(0, half);
                    for (int a = 0; a<encrypted.length(); a++){
                        decrypted += list.get(ciphers.get(list.indexOf(key.substring(a, a+1))).indexOf(encrypted.substring(a, a+1)));
                    }
                }
                else if (index%4 == 2){
                    for (int a = 0; a+1<str.length(); a+=2){
                        try{
                            decrypted += list.get(ciphers.get(list.indexOf(str.substring(a, a+1))).indexOf(str.substring(a+1, a+2)));
                        }catch(ArrayIndexOutOfBoundsException e){
                            System.out.println(str);
                            System.out.println(str.substring(a, a+2));
                            System.exit(1);
                        }
                    }
                }
                else {
                    for (int a = 0; a+1<str.length(); a+=2){
                        decrypted += list.get(ciphers.get(list.indexOf(str.substring(a+1, a+2))).indexOf(str.substring(a, a+1)));
                    }
                }
                return decrypted;
            }
        }
        return "Failed to Decrypt";
    }

    private String hash(String str){ // Unidirectional Hash algorithm.
        if (!opened){
            open(true);
        }
        if (opened){
            String hash = "";
            int start = str.length();
            int[] indices = null;
            size = 15;
            while (indices == null){
                if (start <= size){
                    indices = new int[size];
                    break;
                }
                size += 5;
            }
            if (indices != null){
                for (int i = 0; i<str.length(); i++){
                    indices[i] = Search.indexOf(characters, str.substring(i, i+1));
                    start += indices[i];
                }
                for (int i = str.length(); i<indices.length; i++){
                    indices[i] = indices[i%str.length()];   
                    start += indices[i];
                }
                String hash = "";
                for (int i : indices){
                    start += i;
                    if (start >= characters.length){
                        start %= characters.length;
                    }
                    hash += cipher[start][i];
                }
                return hash;
            }
        }
        return "Could not Hash - "+str;
    }

    final private String[] forbidden = {"\"", "#", "*", "/", ":", "<", ">", "?", "\\", "|"};
    public String generateRandom(int size){
        if (!opened){
            open(true);
        }
        String encrypted = "";
        if (opened) {
            while (encrypted.length() < size) {
                int r = _Random_.randomint(0, characters.length - 1);
                if (Search.binarySearch(forbidden, characters[r]) == -1) {
                    encrypted += characters[r];
                }
            }
        }
        return encrypted;
    }

    public void resetDefault(){
        File file = new File("./Vault Files/"+keyName+fileFormat);
        if (file.exists()){
            EncryptionEngine encrypt = new EncryptionEngine();
            EncryptionEngine encryptdef = new EncryptionEngine(false);
            String[] files = new File("./Vault Files").list();
            for (int a = 0; a<files.length; a++){
                if (files[a].endsWith(fileFormat) && !files[a].equals(keyName+fileFormat)){
                    try{
                        BufferedReader br = IO.filereader("./Vault Files/"+files[a]);
                        String login = br.readLine();
                        String data = br.readLine();
                        br.close();
                        PrintWriter pr = IO.printwriter("./Vault Files/"+files[a]);
                        login = encrypt.getAdvancedDecryption(login);
                        if (!files[a].equals(""+settingsName+fileFormat)){
                            String[] logins = login.split(salt);
                            logins[1] = encrypt.getSimpleDecryption(logins[1]);
                            logins[1] = encryptdef.getSimpleEncryption(logins[1]);
                            login = logins[0]+salt+logins[1];
                        }
                        login = encryptdef.getAdvancedEncryption(login);
                        pr.println(login);
                        if (data != null){
                            data = encrypt.getAdvancedDecryption(data);
                            data = encryptdef.getAdvancedEncryption(data);
                            pr.println(data);
                        }
                        pr.close();
                    }catch(IOException e){}
                }
            }
            if (!file.delete()){
                PrintWriter pr = IO.printwriter(file.getPath());
                pr.close();
            }
        }
        else{
            file = new File("./Vault Files/Backup/");
            if (file.isDirectory()){
                File[] files = file.listFiles();
                for (int a = 0; a<files.length; a++){
                    if (!files[a].getName().equals(keyName+fileFormat)){
                        try{
                            BufferedReader br = IO.filereader("./Vault Files/Backup/"+files[a].getName());
                            String login = br.readLine();
                            String data = br.readLine();
                            PrintWriter pr = IO.printwriter("./Vault Files/"+files[a].getName().replaceAll(backupEnd, ""));
                            pr.println(login);
                            if (data != null){
                                pr.println(data);
                            }
                            pr.close();
                        }catch(IOException e){}
                    }
                }
            }
        }
    }
    public void reset(){
        changeKeys();
    }
    private void changeKeys(){
        EncryptionEngine EncryptionEngineOld = new EncryptionEngine(false);
        boolean backup = !new File("./Vault Files/"+keyName+fileFormat).exists();
        generatePrivateKey("./Vault Files/"+keyName+fileFormat);
        EncryptionEngine EncryptionEngineNew = new EncryptionEngine();
        File file = new File("./Vault Files");
        String[] fileList = file.list();
        file = new File("./Vault Files/Backup/");
        file.mkdirs();
        for (int a = 0; a<fileList.length; a++){
            if (fileList[a].endsWith(fileFormat) && !fileList[a].equals(""+keyName+fileFormat)){
                try{
                    BufferedReader br = IO.filereader("./Vault Files/"+fileList[a]);
                    String login = br.readLine();
                    String data = br.readLine();
                    br.close();
                    if (backup){
                        PrintWriter pr1 = IO.printwriter("./Vault Files/Backup/"+fileList[a].substring(0, fileList[a].length()-4)+backupEnd+fileFormat);
                        pr1.println(login);
                        if (!fileList[a].equals(""+settingsName+fileFormat)){
                            pr1.println(data);
                        }
                        pr1.close();
                    }
                    PrintWriter pr2 = IO.printwriter("./Vault Files/"+fileList[a]);
                    login = EncryptionEngineOld.getAdvancedDecryption(login);
                    if (!fileList[a].equals(""+settingsName+fileFormat)){
                        String[] logins = login.split(salt);
                        logins[1] = EncryptionEngineOld.getSimpleDecryption(logins[1]);
                        logins[1] = EncryptionEngineNew.getSimpleEncryption(logins[1]);
                        login = logins[0]+salt+logins[1];
                    }
                    login = EncryptionEngineNew.getAdvancedEncryption(login);
                    pr2.println(login);
                    if (!fileList[a].equals(""+settingsName+fileFormat)){
                        data = EncryptionEngineOld.getAdvancedDecryption(data);
                        data = EncryptionEngineNew.getAdvancedEncryption(data);
                        pr2.println(data);
                    }
                    pr2.close();
                }catch(IOException e){}
            }
        }
    }
//    public void generateRandomKey(String path, String... additional){
//        try {
//            String line = new BufferedReader (new InputStreamReader(getClass().getResourceAsStream("Resources/All Characters"+fileFormat), "UTF-8")).readLine().replaceAll("\u00E4", "");
//            if (!line.substring(0, 1).equals("a")){
//                line = line.substring(1);
//            }
//            String[] characters = line.split("");
//            PrintWriter pr = IO.printwriter(path);
//            List<String> list = new ArrayList<>();
//            for (int a = -1; a<characters.length; a++){
//                list.addAll(Arrays.asList(characters));
//                list.addAll(Arrays.asList(additional));
//                while(!list.isEmpty()){
//                    int r = _Random_.randomint(0, list.size()-1);
//                    pr.print(list.remove(r));
//                }
//                pr.println();
//            }
//            pr.close();
//        } catch (IOException ex) {}
//    }
    public void generatePrivateKey(String path, String... additional){
        try {
            String line = new BufferedReader (new InputStreamReader(getClass().getResourceAsStream("All Characters"+fileFormat), "UTF-8")).readLine().replaceAll("\u00E4", "");
            if (!line.substring(0, 1).equals("a")){
                line = line.substring(1);
            }
            String[] characters = line.split("");
            List<String> list = new ArrayList<>();
            list.addAll(Arrays.asList(characters));
            list.addAll(Arrays.asList(additional));
            List<String> key = new ArrayList<>();
            while (!list.isEmpty()){
                int r = _Random_.randomint(0, list.size()-1);
                key.add(list.get(r));
                list.remove(r);
            }
            list.addAll(Arrays.asList(characters));
            list.addAll(Arrays.asList(additional));
            List<String> row = new ArrayList<>();
            while (!list.isEmpty()){
                int r = _Random_.randomint(0, list.size()-1);
                row.add(list.get(r));
                list.remove(r);
            }
            List<List<String>> table = new ArrayList<>();
            for (int a = 0; a<row.size(); a++){
                List<String> temp = new ArrayList<>();
                for (int b = row.size()-a; b<row.size(); b++){
                    temp.add(row.get(b));
                }
                for (int b = 0; b<(row.size()-a); b++){
                    temp.add(row.get(b));
                }
                table.add(temp);
            }
            List<List<String>> temp = new ArrayList<>();
            temp.addAll(table);
            table.clear();
            while (!temp.isEmpty()){
                int r = _Random_.randomint(0, temp.size()-1);
                table.add(temp.get(r));
                temp.remove(r);
            }

            PrintWriter pr = IO.printwriter(path);
            for (int a = 0; a<key.size(); a++){
                pr.print(key.get(a));
            }
            pr.println();
            for (int a = 0; a<table.size(); a++){
                for (int b = 0; b<table.get(a).size(); b++){
                    pr.print(table.get(a).get(b));
                }
                pr.println();
            }
            pr.close();
        } catch (IOException ex) {}
    }
    public String getPublicKey(){
        if (this.publicKey.equals("")){
            try{
                BufferedReader br = new BufferedReader (new InputStreamReader(getClass().getResourceAsStream(privateKey+fileFormat), "UTF-8"));
                String line = br.readLine();
                while (line != null){
                    publicKey += hash(line)+"\n";
                    line = br.readLine();
                }
            }catch(IOException e){
                publicKey = "";
            }
        }
        return this.publicKey;
    }
    public void printPublicKey(String path){
        PrintWriter pr = IO.printwriter(path);
        String[] publicKey = getPublicKey().split("\n");
        for (String key : publicKey){
            pr.println(key);
        }
        pr.close();
    }
    public String createSalt(int size){
        String salt = "";
        if (!opened){
            open(true);
        }
        if (opened){
            for (int a = 0; a<size; a++){
                salt += characters[_Random_.randomint(0, characters.length-1)];
            }
        }
        return salt;
    }

}
