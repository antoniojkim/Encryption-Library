package Byte_Encryption;

import Tools.IO;
import Tools.NumberTools;
import Tools.Search;
import Tools._Random_;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static Byte_Encryption.ByteEncryption.byteCharConstant;

/**
 * Created by Antonio on 2018-02-07.
 */
public class ByteKeystore {

    public static byte[] generateKey(int length){
        if (
                length > 0) {
            byte[] key = new byte[length > 64 ? NumberTools.floor2(length) : 64];
            for (int i = 0; i<key.length; ++i){
                key[i] = _Random_.randomByte();
            }
            return key;
        }
        return null;
    }

    public static void printKeystore(String path){
        PrintWriter pr = IO.printwriter(path); // "./src/Byte_Encryption/keystore.txt"
        if (pr == null) return;
        for (int j = Byte.MIN_VALUE; j<=Byte.MAX_VALUE; ++j){
            List<Integer> list = new ArrayList<>();
            for (int b = Byte.MIN_VALUE; b<=Byte.MAX_VALUE; ++b){
                list.add(b+byteCharConstant);
            }
            while (!list.isEmpty()){
                pr.print((char)((int) _Random_.pop(list)));
            }
            pr.println();
        }
        pr.close();
    }
    public static byte[][] generateKeystore(String scrambler){
        if (scrambler == null || scrambler.length() == 0) scrambler = "Lorem ipsum dolor sit amet";
        char[] scrambleArray = scrambler.toCharArray();
        byte[][] keystore = new byte[256][256];
        for (int i = 0; i<256; ++i){
            List<Byte> list = new ArrayList<>(256);
            for (int j = Byte.MIN_VALUE; j<=Byte.MAX_VALUE; ++j){
                list.add((byte)j);
            }
            if (i > 0){
                for (int j = 0; j<256; ++j){
                    keystore[i][j] = list.remove(Search.linearSearch(keystore[i-1], (byte)j)%list.size());
                }
            }
            else{
                for (int j = 0; j<256; ++j){
                    keystore[i][j] = list.remove(((int)scrambleArray[(i+j)%scrambleArray.length]-Byte.MIN_VALUE)%list.size());
                }
            }
        }
        return keystore;
    }
    public static byte[][] generateRandomKeystore(){
        byte[][] keystore = new byte[256][256];
        for (int i = 0; i<256; ++i){
            List<Byte> list = new ArrayList<>(256);
            for (int j = Byte.MIN_VALUE; j<=Byte.MAX_VALUE; ++j){
                list.add((byte)j);
            }
            for (int j = 0; j<256; ++j){
                keystore[i][j] = _Random_.pop(list);
            }
        }
        return keystore;
    }
    public void read(String path){
        read(IO.filereader(path));
    }
    public static byte[][] read(BufferedReader br){
        if (br != null) {
            try {
                String line;
                List<byte[]> list = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    char[] array = line.toCharArray();
                    byte[] keyLine = new byte[array.length];
                    for (int i = 0; i < keyLine.length; ++i) {
                        keyLine[i] = (byte) (((int) array[i]) - byteCharConstant);
                    }
                    list.add(keyLine);
                }
                byte[][] keystore = new byte[list.size()][];
                for (int i = 0; i < keystore.length; ++i) {
                    keystore[i] = list.get(i);
                }
                return keystore;
            } catch (IOException ignored) {}
        }
        return null;
    }
}
