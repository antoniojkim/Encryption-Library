package BlockChain;

import Tools.IO;
import Tools.NumberTools;
import Tools._Random_;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Antonio on 2017-12-23.
 */
public class ByteEncryption {

    // Hash depends on key.  Hash is quick to compute, key is not.
    // This can substitute the "bitcoin 17 zero" hash

    private byte[] key;
    private byte[][] keystore;

    private byte[][] roundKeys;
    private byte[][] hashKey;

    private List<HashMap<Byte, Byte>> subByteTable;
    private List<HashMap<Byte, Byte>> reverseSubByteTable;

    public ByteEncryption(byte... key){
        this.key = key;
        this.hashKey = toBlocks(key);
        try {
            readKeystore(new BufferedReader (new InputStreamReader(getClass().getResourceAsStream("keystore.txt"), "UTF-8")));
            mixKeyStore();
            generateRoundKeys();
            generateSubByteTable();
            generateHashKey();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public ByteEncryption(String key){
        this(toByteArray(key));
    }

    private void mixKeyStore(){
        if (keystore == null)   return;
        byte s = 0;
        for (byte b : key){
            s += b;
        }
        for (int i = 0; i<keystore.length; ++i){
            byte[] mixed = new byte[keystore[i].length];
            List<Byte> unmixed = new ArrayList<>(keystore[i].length);
            for (byte b : keystore[i]){
                unmixed.add(b);
            }
            for (int j = 0; j<mixed.length; ++j){
                mixed[j] = unmixed.remove(((byte)(s + key[j % key.length]) - Byte.MIN_VALUE) % unmixed.size());
            }
            keystore[i] = mixed;
        }
    }

    private static final int numRounds = 16;
    private void generateRoundKeys(){
        if (keystore == null)   return;
        byte sum = 0;
        for (byte b : key){
            sum ^= b;
        }
        roundKeys = new byte[keystore.length][8];
        for (int i = 0; i< roundKeys.length; ++i){
            System.arraycopy(keystore[i], (sum - Byte.MIN_VALUE)%(keystore.length-8), roundKeys[i], 0, 8);
            sum = roundKeys[i][0];
        }
        for (byte[] block : hashKey) { // At this point "hashKey" is just the block of the key.
            addRoundKey(roundKeys, block);
        }
    }
    private void generateSubByteTable(){
        subByteTable = new ArrayList<>(keystore.length);
        reverseSubByteTable = new ArrayList<>(keystore.length);
        byte s1 = -1, s2 = 1;
        for (byte b : key){
            s1 ^= b;
            s2 ^= b;
        }
        if (s1 == s2)   s1 = (byte)(~((int)s1));
        HashMap<Byte, Byte> map = new HashMap<>();
        int numIdentical = 0;
        for (int i = 0; i<keystore.length; ++i){
            subByteTable.add(new HashMap<>());
            reverseSubByteTable.add(new HashMap<>());
            int a = s1 - Byte.MIN_VALUE, b = s2 - Byte.MIN_VALUE;
            for (int j = 0; j<keystore[a].length; ++j){
                subByteTable.get(i).put(keystore[a][j], keystore[b][j]);
                reverseSubByteTable.get(i).put(keystore[b][j], keystore[a][j]);
            }
            s1 = keystore[a][b];
            s2 = keystore[b][a];
            if (s1 == s2)   s1 = (byte)(~((int)s1));
            if (map.containsKey(s1) && map.get(s1) == s2){
                s1 = keystore[b][a];
                s2 = keystore[a][b];
                if (map.containsKey(s1) && map.get(s1) == s2){
                    ++numIdentical;
                }
                else{
                    map.put(s1, s2);
                }
            }
        }
        if (numIdentical > 0){
            System.err.println("Warning:   "+numIdentical+" Identical SubByte Tables Generated!");
        }
    }

    public String encryptUnicode(String data){
        return toByteCharString(encrypt(data.getBytes()));
    }
    public byte[] encrypt(byte[] data){
        byte xor = 0, sum = 0;
        for (byte b : data){
            xor ^= b;
            sum += b;
        }
        byte[] tableSequence = new byte[numRounds];
        for (int i = 0; i<tableSequence.length; ++i){
            tableSequence[i] = keystore[xor - Byte.MIN_VALUE][sum - Byte.MIN_VALUE];
            xor ^= tableSequence[i];
            sum += tableSequence[i];
        }
        byte[][] blockData = toBlocks(data, 16);
        for (byte aTableSequence : tableSequence) {
            subBytes(blockData, subByteTable.get(aTableSequence - Byte.MIN_VALUE));
            shiftLeft(blockData);
            addRoundKey(blockData, roundKeys[aTableSequence - Byte.MIN_VALUE]);
        }
        byte[] encrypted = new byte[blockData.length*8+numRounds];
        System.arraycopy(tableSequence, 0, encrypted, 0, numRounds);
        System.arraycopy(toArray(blockData), 0, encrypted, numRounds, 8*blockData.length);
        return encrypted;
    }

    public String decryptUnicode(String data){
        return new String(decrypt(toByteArray(data)));
    }
    public byte[] decrypt(byte[] encrypted){
        byte[] tableSequence = new byte[numRounds];
        System.arraycopy(encrypted, 0, tableSequence, 0, numRounds);
        byte[] data = new byte[encrypted.length-numRounds];
        System.arraycopy(encrypted, numRounds, data, 0, encrypted.length-numRounds);
        byte[][] blockData = toBlocks(data, 16);
        for (int i = numRounds-1; i>=0; --i){
            addRoundKey(blockData, roundKeys[tableSequence[i]-Byte.MIN_VALUE]);
            shiftRight(blockData);
            subBytes(blockData, reverseSubByteTable.get(tableSequence[i]-Byte.MIN_VALUE));
        }
        return toArray(blockData);
    }

    private void generateHashKey(){
        if (keystore == null)   return;
        for (int i = 0; i<3; ++i)   subBytes(hashKey);
        //Print.println(hashKey);
    }

    public String hashString(String data){
        return toByteCharString(hash(data.getBytes()));
    }
    public byte[] hash(String data){
        return hash(data.getBytes());
    }
    public byte[] hash(byte[] data){
        if (roundKeys == null)  return data;
        byte[][] hashKey = new byte[8][8];
        for (int i = 0; i<hashKey.length; ++i){
            System.arraycopy(this.hashKey[i], 0, hashKey[i], 0, 8);
        }
        for (int i = 0; i<data.length; ++i) {
            addRoundKey(hashKey, roundKeys[data[i] - Byte.MIN_VALUE]);
        }
        subBytes(hashKey);
        return toArray(hashKey);
    }

    public static byte[] pad(byte[] data){
        return pad(data, 64);
    }
    private static byte[] pad(byte[] data, int minLength){
        int floor2 = Math.max(NumberTools.floor2(data.length), minLength);
        byte[] padded = new byte[floor2 < data.length ? floor2 << 1 : floor2];
        System.arraycopy(data, 0, padded, 0, data.length);
        return padded;
    }

    public static byte[][] toBlocks(byte[] data){
        return toBlocks(data, 64);
    }
    private static byte[][] toBlocks(byte[] data, int minLength){
        data = pad(data, minLength);
        byte[][] blocks = new byte[data.length/8][8];
        for (int i = 0; i<blocks.length; ++i){
            System.arraycopy(data, 8*i, blocks[i], 0, 8);
        }
        return blocks;
    }

    public static byte[] toArray(byte[][] blocks){
        byte[] data = new byte[8*blocks.length];
        for (int i = 0; i<blocks.length; ++i){
            System.arraycopy(blocks[i], 0, data, 8*i, 8);
        }
        return data;
    }

    public static byte[] toByteArray(String data){
        char[] array = data.toCharArray();
        byte[] bytes = new byte[array.length];
        for (int i = 0; i<array.length; ++i){
            bytes[i] = (byte)(((int)array[i])-byteCharConstant);
        }
        return bytes;
    }
    public static String toString(byte[] data){
        char[] array = new char[data.length];
        for (int i = 0; i<array.length; ++i){
            array[i] = (char)((int)data[i]);
        }
        return new String(array);
    }
    public static String toByteCharString(byte[] data){
        char[] array = new char[data.length];
        for (int i = 0; i<array.length; ++i){
            array[i] = (char)(data[i]+byteCharConstant);
        }
        return new String(array);
    }

    private void subBytes(byte[] data){
        if (keystore == null)   return;
        byte sum = 0;
        for (byte b : data){
            sum += b;
        }
        for (int i = 0; i<data.length; ++i){
            data[i] = keystore[sum - Byte.MIN_VALUE][data[i] - Byte.MIN_VALUE];
            sum ^= data[i];
        }
    }
    private void subBytes(byte[][] blocks){
        if (keystore == null) return;
        for (byte[] block : blocks){
            subBytes(block);
        }
    }
    private void subBytes(byte[][] blocks, HashMap<Byte, Byte> table) {
        for (int i = 0; i < blocks.length; ++i) {
            for (int j = 0; j < blocks[i].length; ++j) {
                blocks[i][j] = table.get(blocks[i][j]);
            }
        }
    }

    public static byte[] shiftLeft(byte[] data, int amount){
        amount %= data.length;
        if (amount < 0) amount += data.length;
        byte[] shifted = new byte[data.length];
        System.arraycopy(data, amount, shifted, 0, data.length-amount);
        System.arraycopy(data, 0, shifted, data.length-amount, amount);
        return shifted;
    }
    public static void shiftLeft(byte[][] blocks){
        for (int i = 0; i<blocks.length; ++i){
            blocks[i] = shiftLeft(blocks[i], i);
        }
    }
    public static void shiftRight(byte[][] blocks){
        for (int i = 0; i<blocks.length; ++i){
            blocks[i] = shiftLeft(blocks[i], -i);
        }
    }

    public static byte[] addRoundKey(byte[] data, byte[] roundKey){
        if (roundKey.length >= data.length) {
            for (int i = 0; i<data.length; ++i){
                data[i] ^= roundKey[i];
            }
        }
        return data;
    }
    public static void addRoundKey(byte[][] blocks, byte[] roundKey){
        if (blocks.length > 0 && roundKey.length >= blocks[0].length) {
            for (int i = 0; i<blocks.length; ++i){
                for (int j = 0; j<blocks[i].length; ++j){
                    blocks[i][j] ^= roundKey[j];
                }
            }
        }
    }


    public static byte[] generateKey(int length){
        if (length > 0) {
            byte[] key = new byte[length > 64 ? NumberTools.floor2(length) : 64];
            for (int i = 0; i<key.length; ++i){
                key[i] = _Random_.randomByte();
            }
            return key;
        }
        return null;
    }

    private static int byteCharConstant = Math.abs(Byte.MIN_VALUE)+14;
    public static void generateKeystore(String path){
        PrintWriter pr = IO.printwriter(path); // "./src/BlockChain/keystore.txt"
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
    public void readKeystore(String path){
        readKeystore(IO.filereader(path));
    }
    private void readKeystore(BufferedReader br){
        try {
            if (br == null) return;
            String line;
            List<byte[]> list = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                char[] array = line.toCharArray();
                byte[] keyLine = new byte[array.length];
                for (int i = 0; i<keyLine.length; ++i){
                    keyLine[i] = (byte)(((int)array[i])-byteCharConstant);
                }
                list.add(keyLine);
            }
            keystore = new byte[list.size()][];
            for (int i = 0; i<keystore.length; ++i){
                keystore[i] = list.get(i);
            }
        } catch (IOException ignored) {}
    }

}
