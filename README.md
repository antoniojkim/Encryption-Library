# Encryption-Library

## Description

A Library containing a polyalphabetic symmetric cipher algorithm that I designed.

The algorithm itself takes some passcode and utilizes a keystore to generate a key table which it uses to encrypt and decrypt data.

* There are over 10<sup>501</sup> possible keystores that can be randomly generated
   * Using the same passcode with different keystores would produce vastly different key.
   * Using different passcodes with the same keystore should produce vastly different key tables as well.
* There is a built-in hashing algorithm that I designed. It is theoretically not perfect as it produces 64-byte hashes for all inputs of any size. However, given how large the output size is, it can practically be treated to be perfect for all reasonable length inputs.
* The encryption algorithm takes in an array of bytes and returns an array of bytes. If the input is a string, the algorithm will pre-process the string into an array of bytes, encrypt it, and then post-processes the resulting array of bytes using a bytes to tetrahexigesimal converter.
* The decryption algorithm takes in an array of bytes and returns an array of bytes. If the input is a string, the algorithm will pre-process the string using the tetrahexigesimal to bytes converter, decrypt it, and uses the resulting array of bytes to reconstruct construct a string.

The library features built-in RSA capabilities of theoretical arbitrary bit lengths (default 1024). This can be used to securely share some passcode so that one could efficiently and securely share data using the symmetric cipher.

## Applications

This library has been used in some of my other projects including:

* [Encryption Rest API](https://github.com/antoniok9130/Encryption-Rest-API)
* [Password Vault](https://github.com/antoniok9130/Password-Vault)
* [Blockchain Proof of Concept](https://github.com/antoniok9130/Blockchain-PoC)
