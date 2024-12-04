/*
 * Copyright (c) 2021, Alden Torres
 *
 * Licensed under the terms of the MIT license.
 * Copy of the license at https://opensource.org/licenses/MIT
 */

package org.ssohub.crypto.ecc;

import static org.ssohub.crypto.ecc.libecc.*;

/**
 * This is a more friendly API for the libecc proxy re-encryption
 * primitives.
 *
 * @author aldenml
 */
public final class Pre {

    private Pre() {
    }

    /**
     * Generates a random message suitable to use in the protocol.
     * <p>
     * The output can be used in other key derivation algorithms for other
     * symmetric encryption protocols.
     *
     * @return a random plaintext message
     */
    public static byte[] pre_schema1_MessageGen() {
        byte[] m = new byte[ecc_pre_schema1_MESSAGESIZE];
        ecc_pre_schema1_MessageGen(m);
        return m;
    }

    public static final class KeyPair {

        KeyPair(byte[] pk, byte[] sk) {
            this.pk = pk;
            this.sk = sk;
        }

        public final byte[] pk;
        public final byte[] sk;
    }

    /**
     * Generate a public/private key pair.
     *
     * @return a random public/private key pair
     */
    public static KeyPair pre_schema1_KeyGen() {
        byte[] pk = new byte[ecc_pre_schema1_PUBLICKEYSIZE];
        byte[] sk = new byte[ecc_pre_schema1_PRIVATEKEYSIZE];
        ecc_pre_schema1_KeyGen(pk, sk);
        return new KeyPair(pk, sk);
    }

    public static final class SigningKeyPair {

        SigningKeyPair(byte[] spk, byte[] ssk) {
            this.spk = spk;
            this.ssk = ssk;
        }

        public final byte[] spk;
        public final byte[] ssk;
    }

    /**
     * Generate a signing public/private key pair.
     *
     * @return a random signing public/private key pair
     */
    public static SigningKeyPair pre_schema1_SigningKeyGen() {
        byte[] spk = new byte[ecc_pre_schema1_SIGNINGPUBLICKEYSIZE];
        byte[] ssk = new byte[ecc_pre_schema1_SIGNINGPRIVATEKEYSIZE];
        ecc_pre_schema1_SigningKeyGen(spk, ssk);
        return new SigningKeyPair(spk, ssk);
    }

    /**
     * Encrypt a message `m` to delegatee j, given j’s public key (pk_j) and the
     * sender i’s signing key pair (signing_i). Produces a ciphertext C_j.
     * <p>
     * This is also called encryption of level 1, since it's used to encrypt to
     * itself (i.e j == i), in order to have later the ciphertext re-encrypted
     * by the proxy with the re-encryption key (level 2).
     *
     * @param m         the plaintext message
     * @param pk_j      delegatee's public key
     * @param signing_i sender signing public/private key
     * @return a CiphertextLevel1_t structure
     */
    public static byte[] pre_schema1_Encrypt(
        byte[] m,
        byte[] pk_j,
        SigningKeyPair signing_i
    ) {
        byte[] C_j_raw = new byte[ecc_pre_schema1_CIPHERTEXTLEVEL1SIZE];
        ecc_pre_schema1_Encrypt(
            C_j_raw,
            m,
            pk_j,
            signing_i.spk,
            signing_i.ssk
        );
        return C_j_raw;
    }

    /**
     * Generate a re-encryption key from user i (the delegator) to user j (the delegatee).
     * <p>
     * Requires the delegator’s private key (sk_i), the delegatee’s public key (pk_j), and
     * the delegator’s signing key pair (signing_i).
     *
     * @param sk_i      delegator’s private key
     * @param pk_j      delegatee’s public key
     * @param signing_i delegator’s signing public/private key
     * @return a ReKey_t structure
     */
    public static byte[] pre_schema1_ReKeyGen(
        byte[] sk_i,
        byte[] pk_j,
        SigningKeyPair signing_i
    ) {
        byte[] tk_i_j_raw = new byte[ecc_pre_schema1_REKEYSIZE];
        ecc_pre_schema1_ReKeyGen(
            tk_i_j_raw,
            sk_i,
            pk_j,
            signing_i.spk,
            signing_i.ssk
        );
        return tk_i_j_raw;
    }

    /**
     * Re-encrypt a ciphertext encrypted to i (C_i) into a ciphertext encrypted
     * to j (C_j), given a re-encryption key (tk_i_j) and the proxy’s signing key
     * pair (spk, ssk).
     * <p>
     * This operation is performed by the proxy and is also called encryption of
     * level 2, since it takes a ciphertext from a level 1 and re-encrypt it.
     * <p>
     * It also validate the signature on the encrypted ciphertext and re-encryption key.
     *
     * @param C_i_raw    a CiphertextLevel1_t structure
     * @param tk_i_j_raw a ReKey_t structure
     * @param spk_i      delegator’s signing public key
     * @param pk_j       delegatee’s public key
     * @param signing    proxy’s signing public/private key
     * @return a CiphertextLevel2_t structure if all the signatures are
     * valid, null if there is an error
     */
    public static byte[] pre_schema1_ReEncrypt(
        byte[] C_i_raw,
        byte[] tk_i_j_raw,
        byte[] spk_i,
        byte[] pk_j,
        SigningKeyPair signing
    ) {
        byte[] C_j_raw = new byte[ecc_pre_schema1_CIPHERTEXTLEVEL2SIZE];
        int r = ecc_pre_schema1_ReEncrypt(
            C_j_raw,
            C_i_raw,
            tk_i_j_raw,
            spk_i,
            pk_j,
            signing.spk,
            signing.ssk
        );
        return r == 0 ? C_j_raw : null;
    }

    /**
     * Decrypt a signed ciphertext (C_i) given the private key of the recipient
     * i (sk_i). Returns the original message that was encrypted, m.
     * <p>
     * This operations is usually performed by the delegator, since it encrypted
     * the message just to be stored and later be re-encrypted by the proxy.
     * <p>
     * It also validate the signature on the encrypted ciphertext.
     *
     * @param C_i_raw a CiphertextLevel1_t structure
     * @param sk_i    recipient private key
     * @param spk_i   recipient signing public key
     * @return the original plaintext message if all the signatures are
     * valid, null if there is an error
     */
    public static byte[] pre_schema1_DecryptLevel1(
        byte[] C_i_raw,
        byte[] sk_i,
        byte[] spk_i
    ) {
        byte[] m = new byte[ecc_pre_schema1_MESSAGESIZE];
        int r = ecc_pre_schema1_DecryptLevel1(
            m,
            C_i_raw,
            sk_i,
            spk_i
        );
        return r == 0 ? m : null;
    }

    /**
     * Decrypt a signed ciphertext (C_j) given the private key of the recipient
     * j (sk_j). Returns the original message that was encrypted, m.
     * <p>
     * This operations is usually performed by the delegatee, since it is the proxy
     * that re-encrypt the message and send the ciphertext to the final recipient.
     * <p>
     * It also validate the signature on the encrypted ciphertext.
     *
     * @param C_j_raw a CiphertextLevel2_t structure
     * @param sk_j    recipient private key
     * @param spk     proxy’s signing public key
     * @return the original plaintext message if all the signatures are
     * valid, null if there is an error
     */
    public static byte[] pre_schema1_DecryptLevel2(
        byte[] C_j_raw,
        byte[] sk_j,
        byte[] spk
    ) {
        byte[] m = new byte[ecc_pre_schema1_MESSAGESIZE];
        int r = ecc_pre_schema1_DecryptLevel2(
            m,
            C_j_raw,
            sk_j,
            spk
        );
        return r == 0 ? m : null;
    }
}
