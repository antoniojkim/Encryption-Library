package Byte_Encryption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static Byte_Encryption.ByteArrayOperations.*;

/**
 * Created by Antonio on 2017-12-23.
 */
public class ByteEncryption {

    public static int byteCharConstant = Math.abs(Byte.MIN_VALUE)+14;

    private byte[] key;
    private byte[][] keystore;

    private byte[][] roundKeys;
    private byte[][] hashKey;

    private List<HashMap<Byte, Byte>> subByteTable;
    private List<HashMap<Byte, Byte>> reverseSubByteTable;

    public ByteEncryption(byte... key){
        this(key, null);
    }
    public ByteEncryption(byte[] key, String scrambler){
        this.key = key;
        this.hashKey = devectorize(key);
        this.keystore = ByteKeystore.generateKeystore(scrambler);
        mixKeyStore();
        generateRoundKeys();
        generateSubByteTable();
        generateHashKey();
    }
    public ByteEncryption(String key){
        this(key, null);
    }
    public ByteEncryption(String key, String scrambler){
        this(unicodeToBytes(key.length() > 0 ? key : "default private key"), scrambler);
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
        return bytesToUnicode(encrypt(data.getBytes()));
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
        byte[][] blockData = devectorize(data, 16);
        for (byte aTableSequence : tableSequence) {
            subBytes(blockData, subByteTable.get(aTableSequence - Byte.MIN_VALUE));
            shiftLeft(blockData);
            addRoundKey(blockData, roundKeys[aTableSequence - Byte.MIN_VALUE]);
        }
        byte[] encrypted = new byte[blockData.length*8+numRounds];
        System.arraycopy(tableSequence, 0, encrypted, 0, numRounds);
        System.arraycopy(vectorize(blockData), 0, encrypted, numRounds, 8*blockData.length);
        return encrypted;
    }

    public String decryptUnicode(String data){
        return new String(decrypt(unicodeToBytes(data)));
    }
    public byte[] decrypt(byte[] encrypted){
        byte[] tableSequence = new byte[numRounds];
        System.arraycopy(encrypted, 0, tableSequence, 0, numRounds);
        byte[] data = new byte[encrypted.length-numRounds];
        System.arraycopy(encrypted, numRounds, data, 0, encrypted.length-numRounds);
        byte[][] blockData = devectorize(data, 16);
        for (int i = numRounds-1; i>=0; --i){
            addRoundKey(blockData, roundKeys[tableSequence[i]-Byte.MIN_VALUE]);
            shiftRight(blockData);
            subBytes(blockData, reverseSubByteTable.get(tableSequence[i]-Byte.MIN_VALUE));
        }
        return vectorize(blockData);
    }

    private void generateHashKey(){
        if (keystore == null)   return;
        for (int i = 0; i<3; ++i){
            subBytes(hashKey);
        }
    }

    public String hashString(String data){
        return bytesToHex(hash(data.getBytes()));
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
        return vectorize(hashKey);
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

}
