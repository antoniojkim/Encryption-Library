# Encryption-Library

A Library containing a polyalphabetic symmetric cipher algorithm that I designed.

The algorithm itself takes some passcode and utilizes a keystore to generate a key table which it uses to encrypt and decrypt data.

* There are over 10^501 possible keystores that can be randomly generated
   * Using the same passcode with different keystores would produce vastly different key.
   * Using different passcodes with the same keystore should produce vastly different key tables as well.
* There is a built-in hashing algorithm that I designed. While it has not been proven, the algorithm should be "[perfect](https://en.wikipedia.org/wiki/Perfect_hash_function)" as the hash heavily relies on each specific character and the order in which the characters appear in the string.

The library features built-in RSA capabilities of theoretical arbitrary bit lengths (default 1024). This can be used to securely share some passcode so that one could efficiently and securely share data using the symmetric cipher.
