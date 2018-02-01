package Encryption_Library;

import Tools.IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 2017-09-14.
 */
public class Keystore {

    private static List<Key> keys = new ArrayList<>();
    public static final String externalKeystorePath = "./Keystore/";

    public static void loadKeys(){
        if (keys.isEmpty()) {
            int keyNumber = 1;
            if (new File(externalKeystorePath).isDirectory()){
                BufferedReader br = IO.filereader(externalKeystorePath+"Key" + (keyNumber++) + ".txt");
                while (br != null) {
                    keys.add(new Key(br));
                    br = IO.filereader(externalKeystorePath+"Key" + (keyNumber++) + ".txt");
                }
                if (keys.get(0).getCharacters().length != keys.size()){
                    keys.clear();
                    loadInternal();
                }
            }
            else{
                loadInternal();
            }
        }
    }

    public static void loadInternal(){
        int keyNumber = 1;
        try {
            Keystore ks = new Keystore();
            BufferedReader br = new BufferedReader(new InputStreamReader(ks.getClass().getResourceAsStream("Keystore/Key" + (keyNumber++) + ".txt"), "UTF-8"));
            while (br != null) {
                keys.add(new Key(br));
                br = new BufferedReader(new InputStreamReader(ks.getClass().getResourceAsStream("Keystore/Key" + (keyNumber++) + ".txt"), "UTF-8"));
            }
        } catch (IOException e) {} catch (NullPointerException e) {}
    }

    public static List<Key> getKeys(){
        if (keys == null || keys.isEmpty()){
            loadKeys();
        }
        return keys;
    }
}
