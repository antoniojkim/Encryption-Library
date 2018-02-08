package Byte_Encryption;

import Tools.NumberTools;
import Tools.Search;

import static Byte_Encryption.ByteEncryption.byteCharConstant;

/**
 * Created by Antonio on 2018-02-07.
 */
public class ByteArrayOperations {

    static byte[] unicodeToBytes(String data){
        char[] array = data.toCharArray();
        byte[] bytes = new byte[array.length];
        for (int i = 0; i<array.length; ++i){
            bytes[i] = (byte)(((int)array[i])-byteCharConstant);
        }
        return bytes;
    }
    static String bytesToUnicode(byte[] data){
        char[] array = new char[data.length];
        for (int i = 0; i<array.length; ++i){
            array[i] = (char)(data[i]+byteCharConstant);
        }
        return new String(array);
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j]-Byte.MIN_VALUE;
            hexChars[j * 2] = hexArray[v/16];
            hexChars[j * 2 + 1] = hexArray[v%16];
        }
        return new String(hexChars);
    }
    public static byte[] hexToBytes(String hex) {
        char[] hexChars = hex.toCharArray();
        byte[] bytes = new byte[hexChars.length/2];
        for (int i = 0; i<bytes.length; ++i){
            bytes[i] = (byte)(Byte.MIN_VALUE+16* Search.linearSearch(hexArray, hexChars[2*i])+Search.linearSearch(hexArray, hexChars[2*i+1]));
        }
        return bytes;
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

    static byte[][] devectorize(byte[] data){
        return devectorize(data, 64);
    }
    static byte[][] devectorize(byte[] data, int minLength){
        data = pad(data, minLength);
        byte[][] blocks = new byte[data.length/8][8];
        for (int i = 0; i<blocks.length; ++i){
            System.arraycopy(data, 8*i, blocks[i], 0, 8);
        }
        return blocks;
    }

    static byte[] vectorize(byte[][] blocks){
        byte[] data = new byte[8*blocks.length];
        for (int i = 0; i<blocks.length; ++i){
            System.arraycopy(blocks[i], 0, data, 8*i, 8);
        }
        return data;
    }


    private static byte[] shiftLeft(byte[] data, int amount){
        amount %= data.length;
        if (amount < 0) amount += data.length;
        byte[] shifted = new byte[data.length];
        System.arraycopy(data, amount, shifted, 0, data.length-amount);
        System.arraycopy(data, 0, shifted, data.length-amount, amount);
        return shifted;
    }
    static void shiftLeft(byte[][] blocks){
        for (int i = 0; i<blocks.length; ++i){
            blocks[i] = shiftLeft(blocks[i], i);
        }
    }
    static void shiftRight(byte[][] blocks){
        for (int i = 0; i<blocks.length; ++i){
            blocks[i] = shiftLeft(blocks[i], -i);
        }
    }

    static void addRoundKey(byte[][] blocks, byte[] roundKey){
        if (blocks.length > 0 && roundKey.length >= blocks[0].length) {
            for (int i = 0; i<blocks.length; ++i){
                for (int j = 0; j<blocks[i].length; ++j){
                    blocks[i][j] ^= roundKey[j];
                }
            }
        }
    }
}
