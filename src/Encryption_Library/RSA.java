package Encryption_Library;

import Encryption_Library.Tools.IO;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Antonio on 2017-09-12.
 */
public class RSA {

    private BigInteger p;
    private BigInteger q;
    private BigInteger N;
    private BigInteger phi;
    private BigInteger e;
    private BigInteger d;
    public static final int bitlength1024 = 1024, bitlength2048 = 2048;
    private Random r;

    public RSA(){
        this(bitlength1024);
    }
    public RSA(int bitlength){
        if (bitlength < bitlength1024)    bitlength = bitlength1024;
        if (bitlength > bitlength2048)    bitlength = bitlength2048;
        r = new Random();
        p = BigInteger.probablePrime(bitlength, r);
        q = BigInteger.probablePrime(bitlength, r);
        N = p.multiply(q);
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = BigInteger.probablePrime(bitlength / 2, r);
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0)
        {
            e.add(BigInteger.ONE);
        }
        d = e.modInverse(phi);
    }

    public RSA(String[] publicKey)
    {
        if (publicKey.length == 2){
            this.e = new BigInteger(publicKey[0]);
            this.N = new BigInteger(publicKey[1]);
        }
        else{
            System.out.println("Invalid Public Key");
            System.exit(1);
        }
    }
    public RSA(String e, String N)
    {
        this.e = new BigInteger(e);
        this.N = new BigInteger(N);
    }
    public RSA(BigInteger e, BigInteger N)
    {
        this.e = e;
        this.N = N;
    }

    public BigInteger getN() {
        return N;
    }

    public BigInteger getE() {
        return e;
    }

    public BigInteger getD() {
        return d;
    }

    public String[] getPublicKey(){
        return new String[]{
                e.toString(),
                N.toString()
        };
    }

    // Encrypt message
    public String encrypt(String message){
        return encrypt(message.getBytes());
    }
    public String encrypt(byte[] message){
        return IO.byteArrayToString((new BigInteger(message)).modPow(e, N).toByteArray());
    }
    public byte[] encryptBytes(String message){
        return encryptBytes(message.getBytes());
    }
    public byte[] encryptBytes(byte[] message){
        return (new BigInteger(message)).modPow(e, N).toByteArray();
    }

    // Decrypt message
    public String decrypt(String message){
        return decrypt(IO.byteArraySringToByteArray(message));
    }
    public String decrypt(byte[] message){
        if (d == null){
            return "Invalid RSA Object";
        }
        return new String((new BigInteger(message)).modPow(d, N).toByteArray());
    }
    public byte[] decryptBytes(String message){
        return decryptBytes(IO.byteArraySringToByteArray(message));
    }
    public byte[] decryptBytes(byte[] message){
        if (d == null){
            return "Invalid RSA Object".getBytes();
        }
        return (new BigInteger(message)).modPow(d, N).toByteArray();
    }
}
