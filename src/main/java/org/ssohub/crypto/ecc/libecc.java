/*
 * Copyright (c) 2021-2023, Alden Torres
 *
 * Licensed under the terms of the MIT license.
 * Copy of the license at https://opensource.org/licenses/MIT
 */

package org.ssohub.crypto.ecc;

/**
 * JNI java interface for libecc-jvm.
 *
 * @author aldenml
 */
public final class libecc {

    static {
        try {
            String path = System.getProperty("libecc.jni.path", "");
            if ("".equals(path)) {
                String libname = "ecc-jvm";
                String os = System.getProperty("os.name");
                if (os != null && os.toLowerCase(java.util.Locale.US).contains("windows"))
                    libname = "lib" + libname;
                System.loadLibrary(libname);
            } else {
                System.load(path);
            }
        } catch (LinkageError e) {
            throw new LinkageError(
                "Look for your architecture binary instructions at: https://github.com/aldenml/ecc",
                e);
        }
    }

    private libecc() {
    }

    // util


    /**
     * Fills `n` bytes at `buf` with an unpredictable sequence of bytes.
     *
     * @param buf (output) the byte array to fill, size:n
     * @param n the number of bytes to fill
     */
    public static native void ecc_randombytes(
        byte[] buf,
        int n
    );

    /**
     * Concatenates two byte arrays. Same as a || b.
     *
     * a || b: denotes the concatenation of byte strings a and b. For
     * example, "ABC" || "DEF" == "ABCDEF".
     *
     * @param out (output) result of the concatenation, size:a1_len+a2_len
     * @param a1 first byte array, size:a1_len
     * @param a1_len the length of `a1`
     * @param a2 second byte array, size:a2_len
     * @param a2_len the length of `a2`
     */
    public static native void ecc_concat2(
        byte[] out,
        byte[] a1,
        int a1_len,
        byte[] a2,
        int a2_len
    );

    /**
     * Same as calling ecc_concat2 but with three byte arrays.
     *
     * @param out (output) result of the concatenation, size:a1_len+a2_len+a3_len
     * @param a1 first byte array, size:a1_len
     * @param a1_len the length of `a1`
     * @param a2 second byte array, size:a2_len
     * @param a2_len the length of `a2`
     * @param a3 third byte array, size:a3_len
     * @param a3_len the length of `a3`
     */
    public static native void ecc_concat3(
        byte[] out,
        byte[] a1,
        int a1_len,
        byte[] a2,
        int a2_len,
        byte[] a3,
        int a3_len
    );

    /**
     * Same as calling ecc_concat2 but with four byte arrays.
     *
     * @param out (output) result of the concatenation, size:a1_len+a2_len+a3_len+a4_len
     * @param a1 first byte array, size:a1_len
     * @param a1_len the length of `a1`
     * @param a2 second byte array, size:a2_len
     * @param a2_len the length of `a2`
     * @param a3 third byte array, size:a3_len
     * @param a3_len the length of `a4`
     * @param a4 fourth byte array, size:a4_len
     * @param a4_len the length of `a4`
     */
    public static native void ecc_concat4(
        byte[] out,
        byte[] a1,
        int a1_len,
        byte[] a2,
        int a2_len,
        byte[] a3,
        int a3_len,
        byte[] a4,
        int a4_len
    );

    /**
     * For byte strings a and b, ecc_strxor(a, b) returns the bitwise XOR of
     * the two byte strings. For example, ecc_strxor("abc", "XYZ") == "9;9" (the
     * strings in this example are ASCII literals, but ecc_strxor is defined for
     * arbitrary byte strings).
     *
     * @param out (output) result of the operation, size:len
     * @param a first byte array, size:len
     * @param b second byte array, size:len
     * @param len length of both `a` and `b`
     */
    public static native void ecc_strxor(
        byte[] out,
        byte[] a,
        byte[] b,
        int len
    );

    /**
     * I2OSP converts a non-negative integer to an octet string of a
     * specified length.
     *
     * See https://datatracker.ietf.org/doc/html/rfc8017#section-4.1
     *
     * @param out (output) corresponding octet string of length xLen, size:xLen
     * @param x non-negative integer to be converted
     * @param xLen intended length of the resulting octet string
     */
    public static native void ecc_I2OSP(
        byte[] out,
        int x,
        int xLen
    );

    /**
     * Takes two pointers to unsigned numbers encoded in little-endian
     * format and returns:
     *
     * -1 if a is less than b
     * 0 if a is equals to b
     * 1 if a is greater than b
     *
     * The comparison is done in constant time
     *
     * @param a first unsigned integer argument, size:len
     * @param b second unsigned integer argument, size:len
     * @param len the length of both `a` and `b`
     * @return the result of the comparison
     */
    public static native int ecc_compare(
        byte[] a,
        byte[] b,
        int len
    );

    /**
     * Takes a byte array and test if it contains only zeros. It runs
     * in constant time.
     *
     * @param n the byte array, size:len
     * @param len the length of `n`
     * @return 0 if non-zero bits are found
     */
    public static native int ecc_is_zero(
        byte[] n,
        int len
    );

    // hash

    /**
     * The size of a SHA-256 digest.
     *
     */
    public static final int ecc_hash_sha256_HASHSIZE = 32;

    /**
     * The size of a SHA-512 digest.
     *
     */
    public static final int ecc_hash_sha512_HASHSIZE = 64;

    /**
     * Computes the SHA-256 of a given input.
     *
     * See https://en.wikipedia.org/wiki/SHA-2
     *
     * @param digest (output) the SHA-256 of the input, size:ecc_hash_sha256_HASHSIZE
     * @param input the input message, size:input_len
     * @param input_len the length of `input`
     */
    public static native void ecc_hash_sha256(
        byte[] digest,
        byte[] input,
        int input_len
    );

    /**
     * Computes the SHA-512 of a given input.
     *
     * See https://en.wikipedia.org/wiki/SHA-2
     *
     * @param digest (output) the SHA-512 of the input, size:ecc_hash_sha512_HASHSIZE
     * @param input the input message, size:input_len
     * @param input_len the length of `input`
     */
    public static native void ecc_hash_sha512(
        byte[] digest,
        byte[] input,
        int input_len
    );

    // mac

    /**
     * Size of the HMAC-SHA-256 digest.
     *
     */
    public static final int ecc_mac_hmac_sha256_HASHSIZE = 32;

    /**
     * Size of the HMAC-SHA-512 digest.
     *
     */
    public static final int ecc_mac_hmac_sha512_HASHSIZE = 64;

    /**
     * Computes the HMAC-SHA-256 of the input stream.
     *
     * See https://datatracker.ietf.org/doc/html/rfc2104
     * See https://datatracker.ietf.org/doc/html/rfc4868
     *
     * @param digest (output) the HMAC-SHA-256 of the input, size:ecc_mac_hmac_sha256_HASHSIZE
     * @param text the input message, size:text_len
     * @param text_len the length of `input`
     * @param key authentication key, size:key_len
     * @param key_len the length of `key`
     */
    public static native void ecc_mac_hmac_sha256(
        byte[] digest,
        byte[] text,
        int text_len,
        byte[] key,
        int key_len
    );

    /**
     * Computes the HMAC-SHA-512 of the input stream.
     *
     * See https://datatracker.ietf.org/doc/html/rfc2104
     * See https://datatracker.ietf.org/doc/html/rfc4868
     *
     * @param digest (output) the HMAC-SHA-512 of the input, size:ecc_mac_hmac_sha512_HASHSIZE
     * @param text the input message, size:text_len
     * @param text_len the length of `input`
     * @param key authentication key, size:key_len
     * @param key_len the length of `key`
     */
    public static native void ecc_mac_hmac_sha512(
        byte[] digest,
        byte[] text,
        int text_len,
        byte[] key,
        int key_len
    );

    // kdf

    /**
     * Key size for HKDF-SHA-256.
     *
     */
    public static final int ecc_kdf_hkdf_sha256_KEYSIZE = 32;

    /**
     * Key size for HKDF-SHA-512.
     *
     */
    public static final int ecc_kdf_hkdf_sha512_KEYSIZE = 64;

    /**
     * Salt size for Argon2id.
     *
     */
    public static final int ecc_kdf_argon2id_SALTIZE = 16;

    /**
     * Computes the HKDF-SHA-256 extract of the input using a key material.
     *
     * See https://datatracker.ietf.org/doc/html/rfc5869
     *
     * @param prk (output) a pseudorandom key, size:ecc_kdf_hkdf_sha256_KEYSIZE
     * @param salt optional salt value (a non-secret random value), size:salt_len
     * @param salt_len the length of `salt`
     * @param ikm input keying material, size:ikm_len
     * @param ikm_len the length of `ikm`
     */
    public static native void ecc_kdf_hkdf_sha256_extract(
        byte[] prk,
        byte[] salt,
        int salt_len,
        byte[] ikm,
        int ikm_len
    );

    /**
     * Computes the HKDF-SHA-256 expand of the input using a key.
     *
     * See https://datatracker.ietf.org/doc/html/rfc5869
     *
     * @param okm (output) output keying material of length `len`, size:len
     * @param prk a pseudorandom key, size:ecc_kdf_hkdf_sha256_KEYSIZE
     * @param info optional context and application specific information, size:info_len
     * @param info_len length of `info`
     * @param len length of output keying material in octets, max allowed value is 8160
     */
    public static native void ecc_kdf_hkdf_sha256_expand(
        byte[] okm,
        byte[] prk,
        byte[] info,
        int info_len,
        int len
    );

    /**
     * Computes the HKDF-SHA-512 extract of the input using a key material.
     *
     * See https://datatracker.ietf.org/doc/html/rfc5869
     *
     * @param prk (output) a pseudorandom key, size:ecc_kdf_hkdf_sha512_KEYSIZE
     * @param salt optional salt value (a non-secret random value), size:salt_len
     * @param salt_len the length of `salt`
     * @param ikm input keying material, size:ikm_len
     * @param ikm_len the length of `ikm`
     */
    public static native void ecc_kdf_hkdf_sha512_extract(
        byte[] prk,
        byte[] salt,
        int salt_len,
        byte[] ikm,
        int ikm_len
    );

    /**
     * Computes the HKDF-SHA-512 expand of the input using a key.
     *
     * See https://datatracker.ietf.org/doc/html/rfc5869
     *
     * @param okm (output) output keying material of length `len`, size:len
     * @param prk a pseudorandom key, size:ecc_kdf_hkdf_sha512_KEYSIZE
     * @param info optional context and application specific information, size:info_len
     * @param info_len length of `info`
     * @param len length of output keying material in octets, max allowed value is 16320
     */
    public static native void ecc_kdf_hkdf_sha512_expand(
        byte[] okm,
        byte[] prk,
        byte[] info,
        int info_len,
        int len
    );

    /**
     * See https://datatracker.ietf.org/doc/html/rfc7914
     *
     * @param out (output) size:len
     * @param passphrase size:passphrase_len
     * @param passphrase_len the length of `passphrase`
     * @param salt size:salt_len
     * @param salt_len the length of `salt`
     * @param cost cpu/memory cost
     * @param block_size block size
     * @param parallelization parallelization
     * @param len intended output length
     * @return 0 on success and -1 if the computation didn't complete
     */
    public static native int ecc_kdf_scrypt(
        byte[] out,
        byte[] passphrase,
        int passphrase_len,
        byte[] salt,
        int salt_len,
        int cost,
        int block_size,
        int parallelization,
        int len
    );

    /**
     * See https://datatracker.ietf.org/doc/html/rfc9106
     *
     * @param out (output) size:len
     * @param passphrase size:passphrase_len
     * @param passphrase_len the length of `passphrase`
     * @param salt size:ecc_kdf_argon2id_SALTIZE
     * @param memory_size amount of memory (in kibibytes) to use
     * @param iterations number of passes
     * @param len intended output length
     * @return 0 on success and -1 if the computation didn't complete
     */
    public static native int ecc_kdf_argon2id(
        byte[] out,
        byte[] passphrase,
        int passphrase_len,
        byte[] salt,
        int memory_size,
        int iterations,
        int len
    );

    // ed25519

    /**
     * Size of the serialized group elements.
     *
     */
    public static final int ecc_ed25519_ELEMENTSIZE = 32;

    /**
     * Size of the input to perform the Elligator 2 map operation.
     *
     */
    public static final int ecc_ed25519_UNIFORMSIZE = 32;

    /**
     * Size of the scalar used in the curve operations.
     *
     */
    public static final int ecc_ed25519_SCALARSIZE = 32;

    /**
     * Size of a non reduced scalar.
     *
     */
    public static final int ecc_ed25519_NONREDUCEDSCALARSIZE = 64;

    /**
     * Checks that p represents a point on the edwards25519 curve, in canonical
     * form, on the main subgroup, and that the point doesn't have a small order.
     *
     * @param p potential point to test, size:ecc_ed25519_ELEMENTSIZE
     * @return 1 on success, and 0 if the checks didn't pass
     */
    public static native int ecc_ed25519_is_valid_point(
        byte[] p
    );

    /**
     * Adds the point p to the point q and stores the resulting point into r.
     *
     * @param r (output) the result, size:ecc_ed25519_ELEMENTSIZE
     * @param p input point operand, size:ecc_ed25519_ELEMENTSIZE
     * @param q input point operand, size:ecc_ed25519_ELEMENTSIZE
     * @return 0 on success, or -1 if p and/or q are not valid points
     */
    public static native int ecc_ed25519_add(
        byte[] r,
        byte[] p,
        byte[] q
    );

    /**
     * Subtracts the point p to the point q and stores the resulting point into r.
     *
     * @param r (output) the result, size:ecc_ed25519_ELEMENTSIZE
     * @param p input point operand, size:ecc_ed25519_ELEMENTSIZE
     * @param q input point operand, size:ecc_ed25519_ELEMENTSIZE
     * @return 0 on success, or -1 if p and/or q are not valid points
     */
    public static native int ecc_ed25519_sub(
        byte[] r,
        byte[] p,
        byte[] q
    );

    /**
     * Main group base point (x, 4/5), generator of the prime group.
     *
     * @param g (output) size:ecc_ed25519_ELEMENTSIZE
     */
    public static native void ecc_ed25519_generator(
        byte[] g
    );

    /**
     * Maps a 32 bytes vector r to a point, and stores its compressed
     * representation into p. The point is guaranteed to be on the main
     * subgroup.
     *
     * This function directly exposes the Elligator 2 map. Uses the high
     * bit to set the sign of the X coordinate, and the resulting point is
     * multiplied by the cofactor.
     *
     * @param p (output) point in the main subgroup, size:ecc_ed25519_ELEMENTSIZE
     * @param r input vector, size:ecc_ed25519_UNIFORMSIZE
     */
    public static native void ecc_ed25519_from_uniform(
        byte[] p,
        byte[] r
    );

    /**
     * Fills p with the representation of a random group element.
     *
     * @param p (output) random group element, size:ecc_ed25519_ELEMENTSIZE
     */
    public static native void ecc_ed25519_random(
        byte[] p
    );

    /**
     * Chose a random scalar in the [0..L[ interval, L being the order of the
     * main subgroup (2^252 + 27742317777372353535851937790883648493) and fill
     * r with the bytes.
     *
     * @param r (output) scalar, size:ecc_ed25519_SCALARSIZE
     */
    public static native void ecc_ed25519_scalar_random(
        byte[] r
    );

    /**
     * Computes the multiplicative inverse of s over L, and puts it into recip.
     *
     * @param recip (output) the result, size:ecc_ed25519_SCALARSIZE
     * @param s an scalar, size:ecc_ed25519_SCALARSIZE
     * @return 0 on success, or -1 if s is zero
     */
    public static native int ecc_ed25519_scalar_invert(
        byte[] recip,
        byte[] s
    );

    /**
     * Returns neg so that s + neg = 0 (mod L).
     *
     * @param neg (output) the result, size:ecc_ed25519_SCALARSIZE
     * @param s an scalar, size:ecc_ed25519_SCALARSIZE
     */
    public static native void ecc_ed25519_scalar_negate(
        byte[] neg,
        byte[] s
    );

    /**
     * Returns comp so that s + comp = 1 (mod L).
     *
     * @param comp (output) the result, size:ecc_ed25519_SCALARSIZE
     * @param s an scalar, size:ecc_ed25519_SCALARSIZE
     */
    public static native void ecc_ed25519_scalar_complement(
        byte[] comp,
        byte[] s
    );

    /**
     * Stores x + y (mod L) into z.
     *
     * @param z (output) the result, size:ecc_ed25519_SCALARSIZE
     * @param x input scalar operand, size:ecc_ed25519_SCALARSIZE
     * @param y input scalar operand, size:ecc_ed25519_SCALARSIZE
     */
    public static native void ecc_ed25519_scalar_add(
        byte[] z,
        byte[] x,
        byte[] y
    );

    /**
     * Stores x - y (mod L) into z.
     *
     * @param z (output) the result, size:ecc_ed25519_SCALARSIZE
     * @param x input scalar operand, size:ecc_ed25519_SCALARSIZE
     * @param y input scalar operand, size:ecc_ed25519_SCALARSIZE
     */
    public static native void ecc_ed25519_scalar_sub(
        byte[] z,
        byte[] x,
        byte[] y
    );

    /**
     * Stores x * y (mod L) into z.
     *
     * @param z (output) the result, size:ecc_ed25519_SCALARSIZE
     * @param x input scalar operand, size:ecc_ed25519_SCALARSIZE
     * @param y input scalar operand, size:ecc_ed25519_SCALARSIZE
     */
    public static native void ecc_ed25519_scalar_mul(
        byte[] z,
        byte[] x,
        byte[] y
    );

    /**
     * Reduces s to s mod L and puts the bytes representing the integer
     * into r where L = (2^252 + 27742317777372353535851937790883648493) is
     * the order of the group.
     *
     * The interval `s` is sampled from should be at least 317 bits to
     * ensure almost uniformity of `r` over `L`.
     *
     * @param r (output) the reduced scalar, size:ecc_ed25519_SCALARSIZE
     * @param s the integer to reduce, size:ecc_ed25519_NONREDUCEDSCALARSIZE
     */
    public static native void ecc_ed25519_scalar_reduce(
        byte[] r,
        byte[] s
    );

    /**
     * Multiplies a point p by a valid scalar n (clamped) and puts
     * the Y coordinate of the resulting point into q.
     *
     * This function returns 0 on success, or -1 if n is 0 or if p is not
     * on the curve, not on the main subgroup, is a point of small order,
     * or is not provided in canonical form.
     *
     * Note that n is "clamped" (the 3 low bits are cleared to make it a
     * multiple of the cofactor, bit 254 is set and bit 255 is cleared to
     * respect the original design). This prevents attacks using small
     * subgroups. If you want to implement protocols that involve blinding
     * operations, use ristretto255.
     *
     * @param q (output) the result, size:ecc_ed25519_ELEMENTSIZE
     * @param n the valid input scalar, size:ecc_ed25519_SCALARSIZE
     * @param p the point on the curve, size:ecc_ed25519_ELEMENTSIZE
     * @return 0 on success, or -1 otherwise.
     */
    public static native int ecc_ed25519_scalarmult(
        byte[] q,
        byte[] n,
        byte[] p
    );

    /**
     * Multiplies the base point (x, 4/5) by a scalar n (clamped) and puts
     * the Y coordinate of the resulting point into q.
     *
     * Note that n is "clamped" (the 3 low bits are cleared to make it a
     * multiple of the cofactor, bit 254 is set and bit 255 is cleared to
     * respect the original design). This prevents attacks using small
     * subgroups. If you want to implement protocols that involve blinding
     * operations, use ristretto255.
     *
     * @param q (output) the result, size:ecc_ed25519_ELEMENTSIZE
     * @param n the valid input scalar, size:ecc_ed25519_SCALARSIZE
     * @return -1 if n is 0, and 0 otherwise.
     */
    public static native int ecc_ed25519_scalarmult_base(
        byte[] q,
        byte[] n
    );

    // ristretto255

    /**
     * Size of the serialized group elements.
     *
     */
    public static final int ecc_ristretto255_ELEMENTSIZE = 32;

    /**
     * Size of the hash input to use on the hash to map operation.
     *
     */
    public static final int ecc_ristretto255_HASHSIZE = 64;

    /**
     * Size of the scalar used in the curve operations.
     *
     */
    public static final int ecc_ristretto255_SCALARSIZE = 32;

    /**
     * Size of a non reduced scalar.
     *
     */
    public static final int ecc_ristretto255_NONREDUCEDSCALARSIZE = 64;

    /**
     * Checks that p is a valid ristretto255-encoded element. This operation
     * only checks that p is in canonical form.
     *
     * @param p potential point to test, size:ecc_ristretto255_ELEMENTSIZE
     * @return 1 on success, and 0 if the checks didn't pass.
     */
    public static native int ecc_ristretto255_is_valid_point(
        byte[] p
    );

    /**
     * Adds the element represented by p to the element q and stores
     * the resulting element into r.
     *
     * @param r (output) the result, size:ecc_ristretto255_ELEMENTSIZE
     * @param p input point operand, size:ecc_ristretto255_ELEMENTSIZE
     * @param q input point operand, size:ecc_ristretto255_ELEMENTSIZE
     * @return 0 on success, or -1 if p and/or q are not valid encoded elements
     */
    public static native int ecc_ristretto255_add(
        byte[] r,
        byte[] p,
        byte[] q
    );

    /**
     * Subtracts the element represented by p to the element q and stores
     * the resulting element into r.
     *
     * @param r (output) the result, size:ecc_ristretto255_ELEMENTSIZE
     * @param p input point operand, size:ecc_ristretto255_ELEMENTSIZE
     * @param q input point operand, size:ecc_ristretto255_ELEMENTSIZE
     * @return 0 on success, or -1 if p and/or q are not valid encoded elements
     */
    public static native int ecc_ristretto255_sub(
        byte[] r,
        byte[] p,
        byte[] q
    );

    /**
     *
     *
     * @param g (output) size:ecc_ristretto255_ELEMENTSIZE
     */
    public static native void ecc_ristretto255_generator(
        byte[] g
    );

    /**
     * Maps a 64 bytes vector r (usually the output of a hash function) to
     * a group element, and stores its representation into p.
     *
     * @param p (output) group element, size:ecc_ristretto255_ELEMENTSIZE
     * @param r bytes vector hash, size:ecc_ristretto255_HASHSIZE
     */
    public static native void ecc_ristretto255_from_hash(
        byte[] p,
        byte[] r
    );

    /**
     * Fills p with the representation of a random group element.
     *
     * @param p (output) random group element, size:ecc_ristretto255_ELEMENTSIZE
     */
    public static native void ecc_ristretto255_random(
        byte[] p
    );

    /**
     * Fills r with a bytes representation of the scalar in
     * the ]0..L[ interval where L is the order of the
     * group (2^252 + 27742317777372353535851937790883648493).
     *
     * @param r (output) random scalar, size:ecc_ristretto255_SCALARSIZE
     */
    public static native void ecc_ristretto255_scalar_random(
        byte[] r
    );

    /**
     * Computes the multiplicative inverse of s over L, and puts it into recip.
     *
     * @param recip (output) the result, size:ecc_ristretto255_SCALARSIZE
     * @param s an scalar, size:ecc_ristretto255_SCALARSIZE
     * @return 0 on success, or -1 if s is zero
     */
    public static native int ecc_ristretto255_scalar_invert(
        byte[] recip,
        byte[] s
    );

    /**
     * Returns neg so that s + neg = 0 (mod L).
     *
     * @param neg (output) the result, size:ecc_ristretto255_SCALARSIZE
     * @param s an scalar, size:ecc_ristretto255_SCALARSIZE
     */
    public static native void ecc_ristretto255_scalar_negate(
        byte[] neg,
        byte[] s
    );

    /**
     * Returns comp so that s + comp = 1 (mod L).
     *
     * @param comp (output) the result, size:ecc_ristretto255_SCALARSIZE
     * @param s an scalar, size:ecc_ristretto255_SCALARSIZE
     */
    public static native void ecc_ristretto255_scalar_complement(
        byte[] comp,
        byte[] s
    );

    /**
     * Stores x + y (mod L) into z.
     *
     * @param z (output) the result, size:ecc_ristretto255_SCALARSIZE
     * @param x input scalar operand, size:ecc_ristretto255_SCALARSIZE
     * @param y input scalar operand, size:ecc_ristretto255_SCALARSIZE
     */
    public static native void ecc_ristretto255_scalar_add(
        byte[] z,
        byte[] x,
        byte[] y
    );

    /**
     * Stores x - y (mod L) into z.
     *
     * @param z (output) the result, size:ecc_ristretto255_SCALARSIZE
     * @param x input scalar operand, size:ecc_ristretto255_SCALARSIZE
     * @param y input scalar operand, size:ecc_ristretto255_SCALARSIZE
     */
    public static native void ecc_ristretto255_scalar_sub(
        byte[] z,
        byte[] x,
        byte[] y
    );

    /**
     * Stores x * y (mod L) into z.
     *
     * @param z (output) the result, size:ecc_ristretto255_SCALARSIZE
     * @param x input scalar operand, size:ecc_ristretto255_SCALARSIZE
     * @param y input scalar operand, size:ecc_ristretto255_SCALARSIZE
     */
    public static native void ecc_ristretto255_scalar_mul(
        byte[] z,
        byte[] x,
        byte[] y
    );

    /**
     * Reduces s to s mod L and puts the bytes integer into r where
     * L = 2^252 + 27742317777372353535851937790883648493 is the order
     * of the group.
     *
     * The interval `s` is sampled from should be at least 317 bits to
     * ensure almost uniformity of `r` over `L`.
     *
     * @param r (output) the reduced scalar, size:ecc_ristretto255_SCALARSIZE
     * @param s the integer to reduce, size:ecc_ristretto255_NONREDUCEDSCALARSIZE
     */
    public static native void ecc_ristretto255_scalar_reduce(
        byte[] r,
        byte[] s
    );

    /**
     * Multiplies an element represented by p by a valid scalar n
     * and puts the resulting element into q.
     *
     * @param q (output) the result, size:ecc_ristretto255_ELEMENTSIZE
     * @param n the valid input scalar, size:ecc_ristretto255_SCALARSIZE
     * @param p the point on the curve, size:ecc_ristretto255_ELEMENTSIZE
     * @return 0 on success, or -1 if q is the identity element.
     */
    public static native int ecc_ristretto255_scalarmult(
        byte[] q,
        byte[] n,
        byte[] p
    );

    /**
     * Multiplies the generator by a valid scalar n and puts the resulting
     * element into q.
     *
     * @param q (output) the result, size:ecc_ristretto255_ELEMENTSIZE
     * @param n the valid input scalar, size:ecc_ristretto255_SCALARSIZE
     * @return -1 if n is 0, and 0 otherwise.
     */
    public static native int ecc_ristretto255_scalarmult_base(
        byte[] q,
        byte[] n
    );

    // bls12_381

    /**
     * Size of a an element in G1.
     *
     */
    public static final int ecc_bls12_381_G1SIZE = 48;

    /**
     * Size of an element in G2.
     *
     */
    public static final int ecc_bls12_381_G2SIZE = 96;

    /**
     * Size of the scalar used in the curve operations.
     *
     */
    public static final int ecc_bls12_381_SCALARSIZE = 32;

    /**
     * Size of an element in Fp.
     *
     */
    public static final int ecc_bls12_381_FPSIZE = 48;

    /**
     * Size of an element in Fp12.
     *
     */
    public static final int ecc_bls12_381_FP12SIZE = 576;

    /**
     * Computes a random element of BLS12-381 Fp.
     *
     * @param ret (output) the result, size:ecc_bls12_381_FPSIZE
     */
    public static native void ecc_bls12_381_fp_random(
        byte[] ret
    );

    /**
     * Get the identity element of BLS12-381 Fp12.
     *
     * @param ret (output) the result, size:ecc_bls12_381_FP12SIZE
     */
    public static native void ecc_bls12_381_fp12_one(
        byte[] ret
    );

    /**
     * Determine if an element is the identity in BLS12-381 Fp12.
     *
     * @param a the input, size:ecc_bls12_381_FP12SIZE
     * @return 1 if the element a is the identity in BLS12-381 Fp12.
     */
    public static native int ecc_bls12_381_fp12_is_one(
        byte[] a
    );

    /**
     * Computes the inverse of an element in BLS12-381 Fp12.
     *
     * @param ret (output) the result, size:ecc_bls12_381_FP12SIZE
     * @param a the input, size:ecc_bls12_381_FP12SIZE
     */
    public static native void ecc_bls12_381_fp12_inverse(
        byte[] ret,
        byte[] a
    );

    /**
     * Computes the square of an element in BLS12-381 Fp12.
     *
     * @param ret (output) the result, size:ecc_bls12_381_FP12SIZE
     * @param a the input, size:ecc_bls12_381_FP12SIZE
     */
    public static native void ecc_bls12_381_fp12_sqr(
        byte[] ret,
        byte[] a
    );

    /**
     * Perform a * b in Fp12.
     *
     * @param ret (output) the result, size:ecc_bls12_381_FP12SIZE
     * @param a input group element, size:ecc_bls12_381_FP12SIZE
     * @param b input group element, size:ecc_bls12_381_FP12SIZE
     */
    public static native void ecc_bls12_381_fp12_mul(
        byte[] ret,
        byte[] a,
        byte[] b
    );

    /**
     * This is a naive implementation of an iterative exponentiation by squaring.
     *
     * NOTE: This method is not side-channel attack resistant on `n`, the algorithm
     * leaks information about it, don't use this if `n` is a secret.
     *
     * @param ret (output) the result, size:ecc_bls12_381_FP12SIZE
     * @param a the base, size:ecc_bls12_381_FP12SIZE
     * @param n the exponent
     */
    public static native void ecc_bls12_381_fp12_pow(
        byte[] ret,
        byte[] a,
        int n
    );

    /**
     * Computes a random element of BLS12-381 Fp12.
     *
     * @param ret (output) the result, size:ecc_bls12_381_FP12SIZE
     */
    public static native void ecc_bls12_381_fp12_random(
        byte[] ret
    );

    /**
     *
     *
     * @param r (output) size:ecc_bls12_381_G1SIZE
     * @param p size:ecc_bls12_381_G1SIZE
     * @param q size:ecc_bls12_381_G1SIZE
     */
    public static native void ecc_bls12_381_g1_add(
        byte[] r,
        byte[] p,
        byte[] q
    );

    /**
     *
     *
     * @param neg (output) size:ecc_bls12_381_G1SIZE
     * @param p size:ecc_bls12_381_G1SIZE
     */
    public static native void ecc_bls12_381_g1_negate(
        byte[] neg,
        byte[] p
    );

    /**
     *
     *
     * @param g (output) size:ecc_bls12_381_G1SIZE
     */
    public static native void ecc_bls12_381_g1_generator(
        byte[] g
    );

    /**
     * Multiplies an element represented by p by a valid scalar n
     * and puts the resulting element into q.
     *
     * @param q (output) the result, size:ecc_bls12_381_G1SIZE
     * @param n the valid input scalar, size:ecc_bls12_381_SCALARSIZE
     * @param p the point on the curve, size:ecc_bls12_381_G1SIZE
     */
    public static native void ecc_bls12_381_g1_scalarmult(
        byte[] q,
        byte[] n,
        byte[] p
    );

    /**
     * Multiplies the generator by a valid scalar n and puts the resulting
     * element into q.
     *
     * @param q (output) the result, size:ecc_bls12_381_G1SIZE
     * @param n the valid input scalar, size:ecc_bls12_381_SCALARSIZE
     */
    public static native void ecc_bls12_381_g1_scalarmult_base(
        byte[] q,
        byte[] n
    );

    /**
     *
     *
     * @param r (output) size:ecc_bls12_381_G2SIZE
     * @param p size:ecc_bls12_381_G2SIZE
     * @param q size:ecc_bls12_381_G2SIZE
     */
    public static native void ecc_bls12_381_g2_add(
        byte[] r,
        byte[] p,
        byte[] q
    );

    /**
     *
     *
     * @param neg (output) size:ecc_bls12_381_G2SIZE
     * @param p size:ecc_bls12_381_G2SIZE
     */
    public static native void ecc_bls12_381_g2_negate(
        byte[] neg,
        byte[] p
    );

    /**
     *
     *
     * @param g (output) size:ecc_bls12_381_G2SIZE
     */
    public static native void ecc_bls12_381_g2_generator(
        byte[] g
    );

    /**
     * Multiplies the generator by a valid scalar n and puts the resulting
     * element into q.
     *
     * @param q (output) the result, size:ecc_bls12_381_G2SIZE
     * @param n the valid input scalar, size:ecc_bls12_381_SCALARSIZE
     */
    public static native void ecc_bls12_381_g2_scalarmult_base(
        byte[] q,
        byte[] n
    );

    /**
     * Fills r with a bytes representation of an scalar.
     *
     * @param r (output) random scalar, size:ecc_bls12_381_SCALARSIZE
     */
    public static native void ecc_bls12_381_scalar_random(
        byte[] r
    );

    /**
     * Evaluates a pairing of BLS12-381.
     *
     * See https://datatracker.ietf.org/doc/html/draft-irtf-cfrg-pairing-friendly-curves-09#section-2.2
     * See https://datatracker.ietf.org/doc/html/draft-irtf-cfrg-pairing-friendly-curves-09#section-2.4
     *
     * G1 is a subgroup of E(GF(p)) of order r.
     * G2 is a subgroup of E'(GF(p^2)) of order r.
     * GT is a subgroup of a multiplicative group (GF(p^12))^* of order r.
     *
     * @param ret (output) the result of the pairing evaluation in GT, size:ecc_bls12_381_FP12SIZE
     * @param p1_g1 point in G1, size:ecc_bls12_381_G1SIZE
     * @param p2_g2 point in G2, size:ecc_bls12_381_G2SIZE
     */
    public static native void ecc_bls12_381_pairing(
        byte[] ret,
        byte[] p1_g1,
        byte[] p2_g2
    );

    /**
     *
     *
     * @param ret (output) size:ecc_bls12_381_FP12SIZE
     * @param p1_g1 size:ecc_bls12_381_G1SIZE
     * @param p2_g2 size:ecc_bls12_381_G2SIZE
     */
    public static native void ecc_bls12_381_pairing_miller_loop(
        byte[] ret,
        byte[] p1_g1,
        byte[] p2_g2
    );

    /**
     *
     *
     * @param ret (output) size:ecc_bls12_381_FP12SIZE
     * @param a size:ecc_bls12_381_FP12SIZE
     */
    public static native void ecc_bls12_381_pairing_final_exp(
        byte[] ret,
        byte[] a
    );

    /**
     * Perform the verification of a pairing match. Useful if the
     * inputs are raw output values from the miller loop.
     *
     * @param a the first argument to verify, size:ecc_bls12_381_FP12SIZE
     * @param b the second argument to verify, size:ecc_bls12_381_FP12SIZE
     * @return 1 if it's a pairing match, else 0
     */
    public static native int ecc_bls12_381_pairing_final_verify(
        byte[] a,
        byte[] b
    );

    // h2c

    /**
     *
     *
     */
    public static final int ecc_h2c_expand_message_xmd_sha256_MAXSIZE = 8160;

    /**
     *
     *
     */
    public static final int ecc_h2c_expand_message_xmd_sha256_DSTMAXSIZE = 255;

    /**
     *
     *
     */
    public static final int ecc_h2c_expand_message_xmd_sha512_MAXSIZE = 16320;

    /**
     *
     *
     */
    public static final int ecc_h2c_expand_message_xmd_sha512_DSTMAXSIZE = 255;

    /**
     * Produces a uniformly random byte string using SHA-256.
     *
     * @param out (output) a byte string, should be at least of size `len`, size:len
     * @param msg a byte string, size:msg_len
     * @param msg_len the length of `msg`
     * @param dst a byte string of at most 255 bytes, size:dst_len
     * @param dst_len the length of `dst`, should be
     * <
     * = ecc_h2c_expand_message_xmd_sha256_DSTMAXSIZE
     * @param len the length of the requested output in bytes, should be
     * <
     * = ecc_h2c_expand_message_xmd_sha256_MAXSIZE
     * @return 0 on success or -1 if arguments are out of range
     */
    public static native int ecc_h2c_expand_message_xmd_sha256(
        byte[] out,
        byte[] msg,
        int msg_len,
        byte[] dst,
        int dst_len,
        int len
    );

    /**
     * Produces a uniformly random byte string using SHA-512.
     *
     * @param out (output) a byte string, should be at least of size `len`, size:len
     * @param msg a byte string, size:msg_len
     * @param msg_len the length of `msg`
     * @param dst a byte string of at most 255 bytes, size:dst_len
     * @param dst_len the length of `dst`, should be
     * <
     * = ecc_h2c_expand_message_xmd_sha512_DSTMAXSIZE
     * @param len the length of the requested output in bytes, should be
     * <
     * = ecc_h2c_expand_message_xmd_sha512_MAXSIZE
     * @return 0 on success or -1 if arguments are out of range
     */
    public static native int ecc_h2c_expand_message_xmd_sha512(
        byte[] out,
        byte[] msg,
        int msg_len,
        byte[] dst,
        int dst_len,
        int len
    );

    // voprf

    /**
     * Size of a serialized group element, since this is the ristretto255
     * curve the size is 32 bytes.
     *
     */
    public static final int ecc_voprf_ristretto255_sha512_ELEMENTSIZE = 32;

    /**
     * Size of a serialized scalar, since this is the ristretto255
     * curve the size is 32 bytes.
     *
     */
    public static final int ecc_voprf_ristretto255_sha512_SCALARSIZE = 32;

    /**
     * Size of a proof. Proof is a tuple of two scalars.
     *
     */
    public static final int ecc_voprf_ristretto255_sha512_PROOFSIZE = 64;

    /**
     * Size of the protocol output in the `Finalize` operations, since
     * this is ristretto255 with SHA-512, the size is 64 bytes.
     *
     */
    public static final int ecc_voprf_ristretto255_sha512_Nh = 64;

    /**
     * A client and server interact to compute output = F(skS, input, info).
     *
     */
    public static final int ecc_voprf_ristretto255_sha512_MODE_OPRF = 0;

    /**
     * A client and server interact to compute output = F(skS, input, info) and
     * the client also receives proof that the server used skS in computing
     * the function.
     *
     */
    public static final int ecc_voprf_ristretto255_sha512_MODE_VOPRF = 1;

    /**
     * A client and server interact to compute output = F(skS, input, info).
     * Allows clients and servers to provide public input to the PRF computation.
     *
     */
    public static final int ecc_voprf_ristretto255_sha512_MODE_POPRF = 2;

    /**
     *
     *
     */
    public static final int ecc_voprf_ristretto255_sha512_MAXINFOSIZE = 2000;

    /**
     * Generates a proof using the specified scalar. Given elements A and B, two
     * non-empty lists of elements C and D of length m, and a scalar k; this
     * function produces a proof that k*A == B and k*C[i] == D[i] for each i in
     * [0, ..., m - 1]. The output is a value of type Proof, which is a tuple of two
     * scalar values.
     *
     * @param proof (output) size:ecc_voprf_ristretto255_sha512_PROOFSIZE
     * @param k size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @param A size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param B size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param C size:m*ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param D size:m*ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param m the size of the `C` and `D` arrays
     * @param mode the protocol mode VOPRF or POPRF
     * @param r size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     */
    public static native void ecc_voprf_ristretto255_sha512_GenerateProofWithScalar(
        byte[] proof,
        byte[] k,
        byte[] A,
        byte[] B,
        byte[] C,
        byte[] D,
        int m,
        int mode,
        byte[] r
    );

    /**
     * Generates a proof. Given elements A and B, two
     * non-empty lists of elements C and D of length m, and a scalar k; this
     * function produces a proof that k*A == B and k*C[i] == D[i] for each i in
     * [0, ..., m - 1]. The output is a value of type Proof, which is a tuple of two
     * scalar values.
     *
     * @param proof (output) size:ecc_voprf_ristretto255_sha512_PROOFSIZE
     * @param k size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @param A size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param B size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param C size:m*ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param D size:m*ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param m the size of the `C` and `D` arrays
     * @param mode the protocol mode VOPRF or POPRF
     */
    public static native void ecc_voprf_ristretto255_sha512_GenerateProof(
        byte[] proof,
        byte[] k,
        byte[] A,
        byte[] B,
        byte[] C,
        byte[] D,
        int m,
        int mode
    );

    /**
     * Helper function used in GenerateProof. It is an optimization of the
     * ComputeComposites function for servers since they have knowledge of the
     * private key.
     *
     * @param M (output) size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param Z (output) size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param k size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @param B size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param C size:m*ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param D size:m*ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param m the size of the `C` and `D` arrays
     * @param mode the protocol mode VOPRF or POPRF
     */
    public static native void ecc_voprf_ristretto255_sha512_ComputeCompositesFast(
        byte[] M,
        byte[] Z,
        byte[] k,
        byte[] B,
        byte[] C,
        byte[] D,
        int m,
        int mode
    );

    /**
     * This function takes elements A and B, two non-empty lists of elements C and D
     * of length m, and a Proof value output from GenerateProof. It outputs a single
     * boolean value indicating whether or not the proof is valid for the given DLEQ
     * inputs. Note this function can verify proofs on lists of inputs whenever the
     * proof was generated as a batched DLEQ proof with the same inputs.
     *
     * @param A size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param B size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param C size:m*ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param D size:m*ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param m the size of the `C` and `D` arrays
     * @param mode the protocol mode VOPRF or POPRF
     * @param proof size:ecc_voprf_ristretto255_sha512_PROOFSIZE
     * @return on success verification returns 1, else 0.
     */
    public static native int ecc_voprf_ristretto255_sha512_VerifyProof(
        byte[] A,
        byte[] B,
        byte[] C,
        byte[] D,
        int m,
        int mode,
        byte[] proof
    );

    /**
     * Helper function used in `VerifyProof`.
     *
     * @param M (output) size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param Z (output) size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param B size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param C size:m*ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param D size:m*ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param m the size of the `C` and `D` arrays
     * @param mode the protocol mode VOPRF or POPRF
     */
    public static native void ecc_voprf_ristretto255_sha512_ComputeComposites(
        byte[] M,
        byte[] Z,
        byte[] B,
        byte[] C,
        byte[] D,
        int m,
        int mode
    );

    /**
     * In the offline setup phase, the server key pair (skS, pkS) is generated using
     * this function, which produces a randomly generate private and public key pair.
     *
     * @param skS (output) size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @param pkS (output) size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     */
    public static native void ecc_voprf_ristretto255_sha512_GenerateKeyPair(
        byte[] skS,
        byte[] pkS
    );

    /**
     * Deterministically generate a key. It accepts a randomly generated seed of
     * length Ns bytes and an optional (possibly empty) public info string.
     *
     * @param skS (output) size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @param pkS (output) size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param seed size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @param info size:infoLen
     * @param infoLen the size of `info`, it should be
     * <
     * = ecc_voprf_ristretto255_sha512_MAXINFOSIZE
     * @param mode the protocol mode VOPRF or POPRF
     * @return 0 on success, or -1 if an error
     */
    public static native int ecc_voprf_ristretto255_sha512_DeriveKeyPair(
        byte[] skS,
        byte[] pkS,
        byte[] seed,
        byte[] info,
        int infoLen,
        int mode
    );

    /**
     * Same as calling `ecc_voprf_ristretto255_sha512_Blind` with an
     * specified scalar blind.
     *
     * @param blindedElement (output) blinded element, size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param input message to blind, size:inputLen
     * @param inputLen length of `input`
     * @param blind scalar to use in the blind operation, size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @param mode oprf mode
     * @return 0 on success, or -1 if an error
     */
    public static native int ecc_voprf_ristretto255_sha512_BlindWithScalar(
        byte[] blindedElement,
        byte[] input,
        int inputLen,
        byte[] blind,
        int mode
    );

    /**
     * The OPRF protocol begins with the client blinding its input. Note that this
     * function can fail for certain inputs that map to the group identity element.
     *
     * @param blind (output) scalar used in the blind operation, size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @param blindedElement (output) blinded element, size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param input message to blind, size:inputLen
     * @param inputLen length of `input`
     * @param mode oprf mode
     * @return 0 on success, or -1 if an error
     */
    public static native int ecc_voprf_ristretto255_sha512_Blind(
        byte[] blind,
        byte[] blindedElement,
        byte[] input,
        int inputLen,
        int mode
    );

    /**
     * Clients store blind locally, and send blindedElement to the server for
     * evaluation. Upon receipt, servers process blindedElement using this function.
     *
     * @param evaluatedElement (output) blinded element, size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param skS scalar used in the blind operation, size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @param blindedElement blinded element, size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     */
    public static native void ecc_voprf_ristretto255_sha512_BlindEvaluate(
        byte[] evaluatedElement,
        byte[] skS,
        byte[] blindedElement
    );

    /**
     * Servers send the output evaluatedElement to clients for processing. Recall
     * that servers may process multiple client inputs by applying the BlindEvaluate
     * function to each blindedElement received, and returning an array with the
     * corresponding evaluatedElement values. Upon receipt of evaluatedElement,
     * clients process it to complete the OPRF evaluation with this function.
     *
     * @param output (output) size:ecc_voprf_ristretto255_sha512_Nh
     * @param input the input message, size:inputLen
     * @param inputLen the length of `input`
     * @param blind size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @param evaluatedElement size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     */
    public static native void ecc_voprf_ristretto255_sha512_Finalize(
        byte[] output,
        byte[] input,
        int inputLen,
        byte[] blind,
        byte[] evaluatedElement
    );

    /**
     * An entity which knows both the secret key and the input can compute the PRF
     * result using this function.
     *
     * @param output (output) size:ecc_voprf_ristretto255_sha512_Nh
     * @param skS size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @param input the input message, size:inputLen
     * @param inputLen the length of `input`
     * @param mode oprf mode
     * @return 0 on success, or -1 if an error
     */
    public static native int ecc_voprf_ristretto255_sha512_Evaluate(
        byte[] output,
        byte[] skS,
        byte[] input,
        int inputLen,
        int mode
    );

    /**
     * Same as calling ecc_voprf_ristretto255_sha512_VerifiableBlindEvaluate but
     * using a specified scalar `r`.
     *
     * @param evaluatedElement (output) size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param proof (output) size:ecc_voprf_ristretto255_sha512_PROOFSIZE
     * @param skS size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @param pkS size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param blindedElement size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param r size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     */
    public static native void ecc_voprf_ristretto255_sha512_VerifiableBlindEvaluateWithScalar(
        byte[] evaluatedElement,
        byte[] proof,
        byte[] skS,
        byte[] pkS,
        byte[] blindedElement,
        byte[] r
    );

    /**
     * The VOPRF protocol begins with the client blinding its input. Clients store
     * the output blind locally and send blindedElement to the server for
     * evaluation. Upon receipt, servers process blindedElement to compute an
     * evaluated element and DLEQ proof using this function.
     *
     * @param evaluatedElement (output) size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param proof (output) size:ecc_voprf_ristretto255_sha512_PROOFSIZE
     * @param skS size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @param pkS size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param blindedElement size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     */
    public static native void ecc_voprf_ristretto255_sha512_VerifiableBlindEvaluate(
        byte[] evaluatedElement,
        byte[] proof,
        byte[] skS,
        byte[] pkS,
        byte[] blindedElement
    );

    /**
     * The server sends both evaluatedElement and proof back to the client. Upon
     * receipt, the client processes both values to complete the VOPRF computation
     * using this function below.
     *
     * @param output (output) size:ecc_voprf_ristretto255_sha512_Nh
     * @param input the input message, size:inputLen
     * @param inputLen the length of `input`
     * @param blind size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @param evaluatedElement size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param blindedElement size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param pkS size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param proof size:ecc_voprf_ristretto255_sha512_PROOFSIZE
     * @return 0 on success, or -1 if an error
     */
    public static native int ecc_voprf_ristretto255_sha512_VerifiableFinalize(
        byte[] output,
        byte[] input,
        int inputLen,
        byte[] blind,
        byte[] evaluatedElement,
        byte[] blindedElement,
        byte[] pkS,
        byte[] proof
    );

    /**
     * Same as calling ecc_voprf_ristretto255_sha512_PartiallyBlind with an
     * specified blind scalar.
     *
     * @param blindedElement (output) blinded element, size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param tweakedKey (output) blinded element, size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param input message to blind, size:inputLen
     * @param inputLen length of `input`
     * @param info message to blind, size:infoLen
     * @param infoLen length of `info`, it should be
     * <
     * = ecc_voprf_ristretto255_sha512_MAXINFOSIZE
     * @param pkS size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param blind size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @return 0 on success, or -1 if an error
     */
    public static native int ecc_voprf_ristretto255_sha512_PartiallyBlindWithScalar(
        byte[] blindedElement,
        byte[] tweakedKey,
        byte[] input,
        int inputLen,
        byte[] info,
        int infoLen,
        byte[] pkS,
        byte[] blind
    );

    /**
     * The POPRF protocol begins with the client blinding its input, using the
     * following modified Blind function. In this step, the client also binds a
     * public info value, which produces an additional tweakedKey to be used later
     * in the protocol. Note that this function can fail for certain private inputs
     * that map to the group identity element, as well as certain public inputs
     * that, if not detected at this point, will cause server evaluation to fail.
     *
     * @param blind (output) scalar used in the blind operation, size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @param blindedElement (output) blinded element, size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param tweakedKey (output) blinded element, size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param input message to blind, size:inputLen
     * @param inputLen length of `input`
     * @param info message to blind, size:infoLen
     * @param infoLen length of `info`, it should be
     * <
     * = ecc_voprf_ristretto255_sha512_MAXINFOSIZE
     * @param pkS size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @return 0 on success, or -1 if an error
     */
    public static native int ecc_voprf_ristretto255_sha512_PartiallyBlind(
        byte[] blind,
        byte[] blindedElement,
        byte[] tweakedKey,
        byte[] input,
        int inputLen,
        byte[] info,
        int infoLen,
        byte[] pkS
    );

    /**
     * Same as calling ecc_voprf_ristretto255_sha512_PartiallyBlindEvaluate with an
     * specified scalar r.
     *
     * @param evaluatedElement (output) size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param proof (output) size:ecc_voprf_ristretto255_sha512_PROOFSIZE
     * @param skS size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @param blindedElement size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param info message to blind, size:infoLen
     * @param infoLen length of `info`, it should be
     * <
     * = ecc_voprf_ristretto255_sha512_MAXINFOSIZE
     * @param r size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @return 0 on success, or -1 if an error
     */
    public static native int ecc_voprf_ristretto255_sha512_PartiallyBlindEvaluateWithScalar(
        byte[] evaluatedElement,
        byte[] proof,
        byte[] skS,
        byte[] blindedElement,
        byte[] info,
        int infoLen,
        byte[] r
    );

    /**
     * Clients store the outputs blind and tweakedKey locally and send
     * blindedElement to the server for evaluation. Upon receipt, servers process
     * blindedElement to compute an evaluated element and DLEQ proof using the
     * this function.
     *
     * @param evaluatedElement (output) size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param proof (output) size:ecc_voprf_ristretto255_sha512_PROOFSIZE
     * @param skS size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @param blindedElement size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param info message to blind, size:infoLen
     * @param infoLen length of `info`, it should be
     * <
     * = ecc_voprf_ristretto255_sha512_MAXINFOSIZE
     * @return 0 on success, or -1 if an error
     */
    public static native int ecc_voprf_ristretto255_sha512_PartiallyBlindEvaluate(
        byte[] evaluatedElement,
        byte[] proof,
        byte[] skS,
        byte[] blindedElement,
        byte[] info,
        int infoLen
    );

    /**
     * The server sends both evaluatedElement and proof back to the client. Upon
     * receipt, the client processes both values to complete the POPRF computation
     * using this function.
     *
     * @param output (output) size:ecc_voprf_ristretto255_sha512_Nh
     * @param input the input message, size:inputLen
     * @param inputLen the length of `input`
     * @param blind size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @param evaluatedElement size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param blindedElement size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param proof size:ecc_voprf_ristretto255_sha512_PROOFSIZE
     * @param info message to blind, size:infoLen
     * @param infoLen length of `info`, it should be
     * <
     * = ecc_voprf_ristretto255_sha512_MAXINFOSIZE
     * @param tweakedKey blinded element, size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @return 0 on success, or -1 if an error
     */
    public static native int ecc_voprf_ristretto255_sha512_PartiallyFinalize(
        byte[] output,
        byte[] input,
        int inputLen,
        byte[] blind,
        byte[] evaluatedElement,
        byte[] blindedElement,
        byte[] proof,
        byte[] info,
        int infoLen,
        byte[] tweakedKey
    );

    /**
     * An entity which knows both the secret key and the input can compute the PRF
     * result using this function.
     *
     * @param output (output) size:ecc_voprf_ristretto255_sha512_Nh
     * @param skS size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @param input the input message, size:inputLen
     * @param inputLen the length of `input`
     * @param info message to blind, size:infoLen
     * @param infoLen length of `info`, it should be
     * <
     * = ecc_voprf_ristretto255_sha512_MAXINFOSIZE
     * @return 0 on success, or -1 if an error
     */
    public static native int ecc_voprf_ristretto255_sha512_PartiallyEvaluate(
        byte[] output,
        byte[] skS,
        byte[] input,
        int inputLen,
        byte[] info,
        int infoLen
    );

    /**
     * Same as calling `ecc_voprf_ristretto255_sha512_HashToGroup` with an
     * specified DST string.
     *
     * @param out (output) element of the group, size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param input input string to map, size:inputLen
     * @param inputLen length of `input`
     * @param dst domain separation tag (DST), size:dstLen
     * @param dstLen length of `dst`
     */
    public static native void ecc_voprf_ristretto255_sha512_HashToGroupWithDST(
        byte[] out,
        byte[] input,
        int inputLen,
        byte[] dst,
        int dstLen
    );

    /**
     * Deterministically maps an array of bytes "x" to an element of "G" in
     * the ristretto255 curve.
     *
     * @param out (output) element of the group, size:ecc_voprf_ristretto255_sha512_ELEMENTSIZE
     * @param input input string to map, size:inputLen
     * @param inputLen length of `input`
     * @param mode mode to build the internal DST string (OPRF, VOPRF, POPRF)
     */
    public static native void ecc_voprf_ristretto255_sha512_HashToGroup(
        byte[] out,
        byte[] input,
        int inputLen,
        int mode
    );

    /**
     * Same as calling ecc_voprf_ristretto255_sha512_HashToScalar with a specified
     * DST.
     *
     * @param out (output) size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @param input size:inputLen
     * @param inputLen the length of `input`
     * @param dst size:dstLen
     * @param dstLen the length of `dst`
     */
    public static native void ecc_voprf_ristretto255_sha512_HashToScalarWithDST(
        byte[] out,
        byte[] input,
        int inputLen,
        byte[] dst,
        int dstLen
    );

    /**
     * Deterministically maps an array of bytes x to an element in GF(p) in
     * the ristretto255 curve.
     *
     * @param out (output) size:ecc_voprf_ristretto255_sha512_SCALARSIZE
     * @param input size:inputLen
     * @param inputLen the length of `input`
     * @param mode oprf mode
     */
    public static native void ecc_voprf_ristretto255_sha512_HashToScalar(
        byte[] out,
        byte[] input,
        int inputLen,
        int mode
    );

    // opaque

    /**
     * The size all random nonces used in this protocol.
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_Nn = 32;

    /**
     * The output size of the "MAC=HMAC-SHA-512" function in bytes.
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_Nm = 64;

    /**
     * The output size of the "Hash=SHA-512" function in bytes.
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_Nh = 64;

    /**
     * The size of pseudorandom keys.
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_Nx = 64;

    /**
     * The size of public keys used in the AKE.
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_Npk = 32;

    /**
     * The size of private keys used in the AKE.
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_Nsk = 32;

    /**
     * The size of a serialized OPRF group element.
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_Noe = 32;

    /**
     * The size of a serialized OPRF scalar.
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_Ns = 32;

    /**
     * The size of an OPRF private key.
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_Nok = 32;

    /**
     * <pre>
     * struct {
     *   uint8 nonce[Nn];
     *   uint8 auth_tag[Nm];
     * } Envelope;
     * </pre>
     *
     * nonce: A unique nonce of length Nn, used to protect this Envelope.
     * auth_tag: An authentication tag protecting the contents of the envelope, covering the envelope nonce and CleartextCredentials.
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_Ne = 96;

    /**
     * In order to avoid dynamic memory allocation, this limit is necessary.
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_PASSWORDMAXSIZE = 200;

    /**
     * In order to avoid dynamic memory allocation, this limit is necessary.
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_IDENTITYMAXSIZE = 200;

    /**
     *
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_CLEARTEXTCREDENTIALSSIZE = 434;

    /**
     * <pre>
     * struct {
     *   uint8 blinded_message[Noe];
     * } RegistrationRequest;
     * </pre>
     *
     * blinded_message: A serialized OPRF group element.
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_REGISTRATIONREQUESTSIZE = 32;

    /**
     * <pre>
     * typedef struct {
     *   uint8 evaluated_message[Noe];
     *   uint8 server_public_key[Npk];
     * } RegistrationResponse;
     * </pre>
     *
     * evaluated_message: A serialized OPRF group element.
     * server_public_key: The server's encoded public key that will be used for the online AKE stage.
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_REGISTRATIONRESPONSESIZE = 64;

    /**
     * <pre>
     * struct {
     *   uint8 client_public_key[Npk];
     *   uint8 masking_key[Nh];
     *   Envelope envelope;
     * } RegistrationRecord;
     * </pre>
     *
     * client_public_key: The client's encoded public key, corresponding to the private key client_private_key.
     * masking_key: An encryption key used by the server to preserve confidentiality of the envelope during login to defend against client enumeration attacks.
     * envelope: The client's Envelope structure.
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_REGISTRATIONRECORDSIZE = 192;

    /**
     *
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_CREDENTIALREQUESTSIZE = 32;

    /**
     *
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_CREDENTIALRESPONSESIZE = 192;

    /**
     * <pre>
     * struct {
     *   CredentialRequest credential_request;
     *   AuthRequest auth_request;
     * } KE1;
     * </pre>
     *
     * credential_request: A CredentialRequest structure.
     * auth_request: An AuthRequest structure.
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_KE1SIZE = 96;

    /**
     * <pre>
     * struct {
     *   CredentialResponse credential_response;
     *   AuthResponse auth_response;
     * } KE2;
     * </pre>
     *
     * credential_response: A CredentialResponse structure.
     * auth_response: An AuthResponse structure.
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_KE2SIZE = 320;

    /**
     * <pre>
     * struct {
     *   uint8 client_mac[Nm];
     * } KE3;
     * </pre>
     *
     * client_mac: An authentication tag computed over the handshake transcript of fixed size Nm, computed using Km2.
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_KE3SIZE = 64;

    /**
     * <pre>
     * struct {
     *   uint8 password[PASSWORDMAXSIZE];
     *   uint8 password_len;
     *   uint8 blind[Nok];
     *   ClientAkeState_t client_ake_state;
     * } ClientState;
     * </pre>
     *
     * password: The client's password.
     * blind: The random blinding inverter returned by Blind().
     * client_ake_state: a ClientAkeState structure.
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_CLIENTSTATESIZE = 361;

    /**
     *
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_SERVERSTATESIZE = 128;

    /**
     * Use Identity for the Memory Hard Function (MHF).
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_MHF_IDENTITY = 0;

    /**
     * Use Scrypt(32768,8,1) for the Memory Hard Function (MHF).
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_MHF_SCRYPT = 1;

    /**
     * Use Argon2id(t=3,p=1,m=2^16) for the Memory Hard Function (MHF). With this
     * option, the salt should always be of length ecc_opaque_ristretto255_sha512_MHF_ARGON2ID_SALTSIZE.
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_MHF_ARGON2ID = 2;

    /**
     * The length of the salt when using ecc_opaque_ristretto255_sha512_MHF_ARGON2ID.
     *
     */
    public static final int ecc_opaque_ristretto255_sha512_MHF_ARGON2ID_SALTSIZE = 16;

    /**
     * Derive a private and public key pair deterministically from a seed.
     *
     * @param private_key (output) a private key, size:ecc_opaque_ristretto255_sha512_Nsk
     * @param public_key (output) the associated public key, size:ecc_opaque_ristretto255_sha512_Npk
     * @param seed pseudo-random byte sequence used as a seed, size:ecc_opaque_ristretto255_sha512_Nn
     */
    public static native void ecc_opaque_ristretto255_sha512_DeriveKeyPair(
        byte[] private_key,
        byte[] public_key,
        byte[] seed
    );

    /**
     * Constructs a "CleartextCredentials" structure given application
     * credential information.
     *
     * @param cleartext_credentials (output) a CleartextCredentials structure, size:ecc_opaque_ristretto255_sha512_CLEARTEXTCREDENTIALSSIZE
     * @param server_public_key the encoded server public key for the AKE protocol, size:ecc_opaque_ristretto255_sha512_Npk
     * @param client_public_key the encoded client public key for the AKE protocol, size:ecc_opaque_ristretto255_sha512_Npk
     * @param server_identity the optional encoded server identity, size:server_identity_len
     * @param server_identity_len the length of `server_identity`
     * @param client_identity the optional encoded client identity, size:client_identity_len
     * @param client_identity_len the length of `client_identity`
     */
    public static native void ecc_opaque_ristretto255_sha512_CreateCleartextCredentials(
        byte[] cleartext_credentials,
        byte[] server_public_key,
        byte[] client_public_key,
        byte[] server_identity,
        int server_identity_len,
        byte[] client_identity,
        int client_identity_len
    );

    /**
     * Same as calling `ecc_opaque_ristretto255_sha512_EnvelopeStore` with an
     * specified `nonce`.
     *
     * @param envelope (output) size:ecc_opaque_ristretto255_sha512_Ne
     * @param client_public_key (output) size:ecc_opaque_ristretto255_sha512_Npk
     * @param masking_key (output) size:ecc_opaque_ristretto255_sha512_Nh
     * @param export_key (output) size:ecc_opaque_ristretto255_sha512_Nh
     * @param randomized_pwd size:64
     * @param server_public_key size:ecc_opaque_ristretto255_sha512_Npk
     * @param server_identity size:server_identity_len
     * @param server_identity_len the length of `server_identity`
     * @param client_identity size:client_identity_len
     * @param client_identity_len the length of `client_identity`
     * @param nonce size:ecc_opaque_ristretto255_sha512_Nn
     */
    public static native void ecc_opaque_ristretto255_sha512_EnvelopeStoreWithNonce(
        byte[] envelope,
        byte[] client_public_key,
        byte[] masking_key,
        byte[] export_key,
        byte[] randomized_pwd,
        byte[] server_public_key,
        byte[] server_identity,
        int server_identity_len,
        byte[] client_identity,
        int client_identity_len,
        byte[] nonce
    );

    /**
     * Creates an "Envelope" at registration.
     *
     * In order to work with stack allocated memory (i.e. fixed and not dynamic
     * allocation), it's necessary to add the restriction on length of the
     * identities to less than 200 bytes.
     *
     * @param envelope (output) size:ecc_opaque_ristretto255_sha512_Ne
     * @param client_public_key (output) size:ecc_opaque_ristretto255_sha512_Npk
     * @param masking_key (output) size:ecc_opaque_ristretto255_sha512_Nh
     * @param export_key (output) size:ecc_opaque_ristretto255_sha512_Nh
     * @param randomized_pwd size:64
     * @param server_public_key size:ecc_opaque_ristretto255_sha512_Npk
     * @param server_identity size:server_identity_len
     * @param server_identity_len the length of `server_identity`
     * @param client_identity size:client_identity_len
     * @param client_identity_len the length of `client_identity`
     */
    public static native void ecc_opaque_ristretto255_sha512_EnvelopeStore(
        byte[] envelope,
        byte[] client_public_key,
        byte[] masking_key,
        byte[] export_key,
        byte[] randomized_pwd,
        byte[] server_public_key,
        byte[] server_identity,
        int server_identity_len,
        byte[] client_identity,
        int client_identity_len
    );

    /**
     * This functions attempts to recover the credentials from the input. On
     * success returns 0, else -1.
     *
     * @param client_private_key (output) size:ecc_opaque_ristretto255_sha512_Nsk
     * @param export_key (output) size:ecc_opaque_ristretto255_sha512_Nh
     * @param randomized_pwd size:64
     * @param server_public_key size:ecc_opaque_ristretto255_sha512_Npk
     * @param envelope_raw size:ecc_opaque_ristretto255_sha512_Ne
     * @param server_identity size:server_identity_len
     * @param server_identity_len the length of `server_identity`
     * @param client_identity size:client_identity_len
     * @param client_identity_len the length of `client_identity`
     * @return on success returns 0, else -1.
     */
    public static native int ecc_opaque_ristretto255_sha512_EnvelopeRecover(
        byte[] client_private_key,
        byte[] export_key,
        byte[] randomized_pwd,
        byte[] server_public_key,
        byte[] envelope_raw,
        byte[] server_identity,
        int server_identity_len,
        byte[] client_identity,
        int client_identity_len
    );

    /**
     * Recover the public key related to the input "private_key".
     *
     * @param public_key (output) size:ecc_opaque_ristretto255_sha512_Npk
     * @param private_key size:ecc_opaque_ristretto255_sha512_Nsk
     */
    public static native void ecc_opaque_ristretto255_sha512_RecoverPublicKey(
        byte[] public_key,
        byte[] private_key
    );

    /**
     * Returns a randomly generated private and public key pair.
     *
     * This is implemented by generating a random "seed", then
     * calling internally DeriveAuthKeyPair.
     *
     * @param private_key (output) a private key, size:ecc_opaque_ristretto255_sha512_Nsk
     * @param public_key (output) the associated public key, size:ecc_opaque_ristretto255_sha512_Npk
     */
    public static native void ecc_opaque_ristretto255_sha512_GenerateAuthKeyPair(
        byte[] private_key,
        byte[] public_key
    );

    /**
     * Derive a private and public authentication key pair deterministically
     * from the input "seed".
     *
     * @param private_key (output) a private key, size:ecc_opaque_ristretto255_sha512_Nsk
     * @param public_key (output) the associated public key, size:ecc_opaque_ristretto255_sha512_Npk
     * @param seed pseudo-random byte sequence used as a seed, size:ecc_opaque_ristretto255_sha512_Nn
     */
    public static native void ecc_opaque_ristretto255_sha512_DeriveAuthKeyPair(
        byte[] private_key,
        byte[] public_key,
        byte[] seed
    );

    /**
     * Same as calling CreateRegistrationRequest with a specified blind.
     *
     * @param request (output) a RegistrationRequest structure, size:ecc_opaque_ristretto255_sha512_REGISTRATIONREQUESTSIZE
     * @param password an opaque byte string containing the client's password, size:password_len
     * @param password_len the length of `password`
     * @param blind the OPRF scalar value to use, size:ecc_opaque_ristretto255_sha512_Ns
     */
    public static native void ecc_opaque_ristretto255_sha512_CreateRegistrationRequestWithBlind(
        byte[] request,
        byte[] password,
        int password_len,
        byte[] blind
    );

    /**
     * To begin the registration flow, the client executes this function.
     *
     * @param request (output) a RegistrationRequest structure, size:ecc_opaque_ristretto255_sha512_REGISTRATIONREQUESTSIZE
     * @param blind (output) an OPRF scalar value, size:ecc_opaque_ristretto255_sha512_Ns
     * @param password an opaque byte string containing the client's password, size:password_len
     * @param password_len the length of `password`
     */
    public static native void ecc_opaque_ristretto255_sha512_CreateRegistrationRequest(
        byte[] request,
        byte[] blind,
        byte[] password,
        int password_len
    );

    /**
     * To process the client's registration request, the server executes
     * this function.
     *
     * @param response (output) a RegistrationResponse structure, size:ecc_opaque_ristretto255_sha512_REGISTRATIONRESPONSESIZE
     * @param request a RegistrationRequest structure, size:ecc_opaque_ristretto255_sha512_REGISTRATIONREQUESTSIZE
     * @param server_public_key the server's public key, size:ecc_opaque_ristretto255_sha512_Npk
     * @param credential_identifier an identifier that uniquely represents the credential, size:credential_identifier_len
     * @param credential_identifier_len the length of `credential_identifier`
     * @param oprf_seed the seed of Nh bytes used by the server to generate an oprf_key, size:ecc_opaque_ristretto255_sha512_Nh
     */
    public static native void ecc_opaque_ristretto255_sha512_CreateRegistrationResponse(
        byte[] response,
        byte[] request,
        byte[] server_public_key,
        byte[] credential_identifier,
        int credential_identifier_len,
        byte[] oprf_seed
    );

    /**
     * Same as calling `ecc_opaque_ristretto255_sha512_FinalizeRegistrationRequest` with an
     * specified `nonce`.
     *
     * @param record (output) a RegistrationUpload structure, size:ecc_opaque_ristretto255_sha512_REGISTRATIONRECORDSIZE
     * @param export_key (output) an additional client key, size:ecc_opaque_ristretto255_sha512_Nh
     * @param password an opaque byte string containing the client's password, size:password_len
     * @param password_len the length of `password`
     * @param blind an OPRF scalar value, size:ecc_opaque_ristretto255_sha512_Ns
     * @param response a RegistrationResponse structure, size:ecc_opaque_ristretto255_sha512_REGISTRATIONRESPONSESIZE
     * @param server_identity the optional encoded server identity, size:server_identity_len
     * @param server_identity_len the length of `server_identity`
     * @param client_identity the optional encoded client identity, size:client_identity_len
     * @param client_identity_len the length of `client_identity`
     * @param mhf the memory hard function to use
     * @param mhf_salt the salt to use in the memory hard function computation, size:mhf_salt_len
     * @param mhf_salt_len the length of `mhf_salt`
     * @param nonce size:ecc_opaque_ristretto255_sha512_Nn
     */
    public static native void ecc_opaque_ristretto255_sha512_FinalizeRegistrationRequestWithNonce(
        byte[] record,
        byte[] export_key,
        byte[] password,
        int password_len,
        byte[] blind,
        byte[] response,
        byte[] server_identity,
        int server_identity_len,
        byte[] client_identity,
        int client_identity_len,
        int mhf,
        byte[] mhf_salt,
        int mhf_salt_len,
        byte[] nonce
    );

    /**
     * To create the user record used for subsequent authentication and complete the
     * registration flow, the client executes the following function.
     *
     * @param record (output) a RegistrationUpload structure, size:ecc_opaque_ristretto255_sha512_REGISTRATIONRECORDSIZE
     * @param export_key (output) an additional client key, size:ecc_opaque_ristretto255_sha512_Nh
     * @param password an opaque byte string containing the client's password, size:password_len
     * @param password_len the length of `password`
     * @param blind an OPRF scalar value, size:ecc_opaque_ristretto255_sha512_Ns
     * @param response a RegistrationResponse structure, size:ecc_opaque_ristretto255_sha512_REGISTRATIONRESPONSESIZE
     * @param server_identity the optional encoded server identity, size:server_identity_len
     * @param server_identity_len the length of `server_identity`
     * @param client_identity the optional encoded client identity, size:client_identity_len
     * @param client_identity_len the length of `client_identity`
     * @param mhf the memory hard function to use
     * @param mhf_salt the salt to use in the memory hard function computation, size:mhf_salt_len
     * @param mhf_salt_len the length of `mhf_salt`
     */
    public static native void ecc_opaque_ristretto255_sha512_FinalizeRegistrationRequest(
        byte[] record,
        byte[] export_key,
        byte[] password,
        int password_len,
        byte[] blind,
        byte[] response,
        byte[] server_identity,
        int server_identity_len,
        byte[] client_identity,
        int client_identity_len,
        int mhf,
        byte[] mhf_salt,
        int mhf_salt_len
    );

    /**
     *
     *
     * @param request (output) a CredentialRequest structure, size:ecc_opaque_ristretto255_sha512_CREDENTIALREQUESTSIZE
     * @param password an opaque byte string containing the client's password, size:password_len
     * @param password_len the length of `password`
     * @param blind an OPRF scalar value, size:ecc_opaque_ristretto255_sha512_Ns
     */
    public static native void ecc_opaque_ristretto255_sha512_CreateCredentialRequestWithBlind(
        byte[] request,
        byte[] password,
        int password_len,
        byte[] blind
    );

    /**
     *
     *
     * @param request (output) a CredentialRequest structure, size:ecc_opaque_ristretto255_sha512_CREDENTIALREQUESTSIZE
     * @param blind (output) an OPRF scalar value, size:ecc_opaque_ristretto255_sha512_Ns
     * @param password an opaque byte string containing the client's password, size:password_len
     * @param password_len the length of `password`
     */
    public static native void ecc_opaque_ristretto255_sha512_CreateCredentialRequest(
        byte[] request,
        byte[] blind,
        byte[] password,
        int password_len
    );

    /**
     * In order to make this method not to use dynamic memory allocation, there is a
     * limit of credential_identifier_len
     * <
     * = 200.
     *
     * There are two scenarios to handle for the construction of a
     * CredentialResponse object: either the record for the client exists
     * (corresponding to a properly registered client), or it was never
     * created (corresponding to a client that has yet to register).
     *
     * In the case of a record that does not exist, the server SHOULD invoke
     * the CreateCredentialResponse function where the record argument is
     * configured so that:
     *
     * - record.masking_key is set to a random byte string of length Nh, and
     * - record.envelope is set to the byte string consisting only of
     * zeros, of length Ne
     *
     * @param response_raw (output) size:ecc_opaque_ristretto255_sha512_CREDENTIALRESPONSESIZE
     * @param request_raw size:ecc_opaque_ristretto255_sha512_CREDENTIALREQUESTSIZE
     * @param server_public_key size:ecc_opaque_ristretto255_sha512_Npk
     * @param record_raw size:ecc_opaque_ristretto255_sha512_REGISTRATIONRECORDSIZE
     * @param credential_identifier size:credential_identifier_len
     * @param credential_identifier_len the length of `credential_identifier`
     * @param oprf_seed size:ecc_opaque_ristretto255_sha512_Nh
     * @param masking_nonce size:ecc_opaque_ristretto255_sha512_Nn
     */
    public static native void ecc_opaque_ristretto255_sha512_CreateCredentialResponseWithMasking(
        byte[] response_raw,
        byte[] request_raw,
        byte[] server_public_key,
        byte[] record_raw,
        byte[] credential_identifier,
        int credential_identifier_len,
        byte[] oprf_seed,
        byte[] masking_nonce
    );

    /**
     * In order to make this method not to use dynamic memory allocation, there is a
     * limit of credential_identifier_len
     * <
     * = 200.
     *
     * There are two scenarios to handle for the construction of a
     * CredentialResponse object: either the record for the client exists
     * (corresponding to a properly registered client), or it was never
     * created (corresponding to a client that has yet to register).
     *
     * In the case of a record that does not exist, the server SHOULD invoke
     * the CreateCredentialResponse function where the record argument is
     * configured so that:
     *
     * - record.masking_key is set to a random byte string of length Nh, and
     * - record.envelope is set to the byte string consisting only of
     * zeros, of length Ne
     *
     * @param response_raw (output) size:ecc_opaque_ristretto255_sha512_CREDENTIALRESPONSESIZE
     * @param request_raw size:ecc_opaque_ristretto255_sha512_CREDENTIALREQUESTSIZE
     * @param server_public_key size:ecc_opaque_ristretto255_sha512_Npk
     * @param record_raw size:ecc_opaque_ristretto255_sha512_REGISTRATIONRECORDSIZE
     * @param credential_identifier size:credential_identifier_len
     * @param credential_identifier_len the length of `credential_identifier`
     * @param oprf_seed size:ecc_opaque_ristretto255_sha512_Nh
     */
    public static native void ecc_opaque_ristretto255_sha512_CreateCredentialResponse(
        byte[] response_raw,
        byte[] request_raw,
        byte[] server_public_key,
        byte[] record_raw,
        byte[] credential_identifier,
        int credential_identifier_len,
        byte[] oprf_seed
    );

    /**
     *
     *
     * @param client_private_key (output) size:ecc_opaque_ristretto255_sha512_Nsk
     * @param server_public_key (output) size:ecc_opaque_ristretto255_sha512_Npk
     * @param export_key (output) size:ecc_opaque_ristretto255_sha512_Nh
     * @param password size:password_len
     * @param password_len the length of `password`
     * @param blind size:ecc_opaque_ristretto255_sha512_Noe
     * @param response size:ecc_opaque_ristretto255_sha512_CREDENTIALRESPONSESIZE
     * @param server_identity size:server_identity_len
     * @param server_identity_len the length of `server_identity`
     * @param client_identity size:client_identity_len
     * @param client_identity_len the length of `client_identity`
     * @param mhf the memory hard function to use
     * @param mhf_salt the salt to use in the memory hard function computation, size:mhf_salt_len
     * @param mhf_salt_len the length of `mhf_salt`
     * @return on success returns 0, else -1.
     */
    public static native int ecc_opaque_ristretto255_sha512_RecoverCredentials(
        byte[] client_private_key,
        byte[] server_public_key,
        byte[] export_key,
        byte[] password,
        int password_len,
        byte[] blind,
        byte[] response,
        byte[] server_identity,
        int server_identity_len,
        byte[] client_identity,
        int client_identity_len,
        int mhf,
        byte[] mhf_salt,
        int mhf_salt_len
    );

    /**
     *
     *
     * @param out (output) size:length
     * @param secret size:64
     * @param label size:label_len
     * @param label_len the length of `label`
     * @param context size:context_len
     * @param context_len the length of `context`
     * @param length the length of the output
     */
    public static native void ecc_opaque_ristretto255_sha512_3DH_Expand_Label(
        byte[] out,
        byte[] secret,
        byte[] label,
        int label_len,
        byte[] context,
        int context_len,
        int length
    );

    /**
     *
     *
     * @param out (output) size:ecc_opaque_ristretto255_sha512_Nx
     * @param secret size:64
     * @param label size:label_len
     * @param label_len the length of `label`
     * @param transcript_hash size:transcript_hash_len
     * @param transcript_hash_len the length of `transcript_hash`
     */
    public static native void ecc_opaque_ristretto255_sha512_3DH_Derive_Secret(
        byte[] out,
        byte[] secret,
        byte[] label,
        int label_len,
        byte[] transcript_hash,
        int transcript_hash_len
    );

    /**
     * The OPAQUE-3DH key schedule requires a preamble.
     *
     * OPAQUE-3DH can optionally include shared "context" information in the
     * transcript, such as configuration parameters or application-specific
     * info, e.g. "appXYZ-v1.2.3".
     *
     * @param preamble (output) the protocol transcript with identities and messages, size:preamble_len
     * @param preamble_len the length of `preamble`
     * @param context optional shared context information, size:context_len
     * @param context_len the length of `context`
     * @param client_identity the optional encoded client identity, size:client_identity_len
     * @param client_identity_len the length of `client_identity`
     * @param client_public_key size:ecc_opaque_ristretto255_sha512_Npk
     * @param ke1 a KE1 message structure, size:ecc_opaque_ristretto255_sha512_KE1SIZE
     * @param server_identity the optional encoded server identity, size:server_identity_len
     * @param server_identity_len the length of `server_identity`
     * @param server_public_key size:ecc_opaque_ristretto255_sha512_Npk
     * @param ke2 a ke2 structure as defined in KE2, size:ecc_opaque_ristretto255_sha512_KE2SIZE
     * @return the protocol transcript with identities and messages
     */
    public static native int ecc_opaque_ristretto255_sha512_3DH_Preamble(
        byte[] preamble,
        int preamble_len,
        byte[] context,
        int context_len,
        byte[] client_identity,
        int client_identity_len,
        byte[] client_public_key,
        byte[] ke1,
        byte[] server_identity,
        int server_identity_len,
        byte[] server_public_key,
        byte[] ke2
    );

    /**
     * Computes the OPAQUE-3DH shared secret derived during the key
     * exchange protocol.
     *
     * @param ikm (output) size:96
     * @param sk1 size:32
     * @param pk1 size:32
     * @param sk2 size:32
     * @param pk2 size:32
     * @param sk3 size:32
     * @param pk3 size:32
     */
    public static native void ecc_opaque_ristretto255_sha512_3DH_TripleDHIKM(
        byte[] ikm,
        byte[] sk1,
        byte[] pk1,
        byte[] sk2,
        byte[] pk2,
        byte[] sk3,
        byte[] pk3
    );

    /**
     *
     *
     * @param km2 (output) size:64
     * @param km3 (output) size:64
     * @param session_key (output) size:64
     * @param ikm size:ikm_len
     * @param ikm_len the length of `ikm`
     * @param preamble size:preamble_len
     * @param preamble_len the length of `preamble`
     */
    public static native void ecc_opaque_ristretto255_sha512_3DH_DeriveKeys(
        byte[] km2,
        byte[] km3,
        byte[] session_key,
        byte[] ikm,
        int ikm_len,
        byte[] preamble,
        int preamble_len
    );

    /**
     *
     *
     * @param ke1 (output) a KE1 message structure, size:ecc_opaque_ristretto255_sha512_KE1SIZE
     * @param state (input, output) a ClientState structure, size:ecc_opaque_ristretto255_sha512_CLIENTSTATESIZE
     * @param password an opaque byte string containing the client's password, size:password_len
     * @param password_len the length of `password`
     * @param blind size:ecc_opaque_ristretto255_sha512_Ns
     * @param client_nonce size:ecc_opaque_ristretto255_sha512_Nn
     * @param client_secret size:ecc_opaque_ristretto255_sha512_Nsk
     * @param client_keyshare size:ecc_opaque_ristretto255_sha512_Npk
     */
    public static native void ecc_opaque_ristretto255_sha512_ClientInitWithSecrets(
        byte[] ke1,
        byte[] state,
        byte[] password,
        int password_len,
        byte[] blind,
        byte[] client_nonce,
        byte[] client_secret,
        byte[] client_keyshare
    );

    /**
     *
     *
     * @param ke1 (output) a KE1 message structure, size:ecc_opaque_ristretto255_sha512_KE1SIZE
     * @param state (input, output) a ClientState structure, size:ecc_opaque_ristretto255_sha512_CLIENTSTATESIZE
     * @param password an opaque byte string containing the client's password, size:password_len
     * @param password_len the length of `password`
     */
    public static native void ecc_opaque_ristretto255_sha512_ClientInit(
        byte[] ke1,
        byte[] state,
        byte[] password,
        int password_len
    );

    /**
     *
     *
     * @param ke3_raw (output) a KE3 message structure, size:ecc_opaque_ristretto255_sha512_KE3SIZE
     * @param session_key (output) the session's shared secret, size:64
     * @param export_key (output) an additional client key, size:64
     * @param state (input, output) a ClientState structure, size:ecc_opaque_ristretto255_sha512_CLIENTSTATESIZE
     * @param client_identity the optional encoded client identity, which is set
     * to client_public_key if not specified, size:client_identity_len
     * @param client_identity_len the length of `client_identity`
     * @param server_identity the optional encoded server identity, which is set
     * to server_public_key if not specified, size:server_identity_len
     * @param server_identity_len the length of `server_identity`
     * @param ke2 a KE2 message structure, size:ecc_opaque_ristretto255_sha512_KE2SIZE
     * @param mhf the memory hard function to use
     * @param mhf_salt the salt to use in the memory hard function computation, size:mhf_salt_len
     * @param mhf_salt_len the length of `mhf_salt`
     * @param context the application specific context, size:context_len
     * @param context_len the length of `context`
     * @return 0 if is able to recover credentials and authenticate with the server, else -1
     */
    public static native int ecc_opaque_ristretto255_sha512_ClientFinish(
        byte[] ke3_raw,
        byte[] session_key,
        byte[] export_key,
        byte[] state,
        byte[] client_identity,
        int client_identity_len,
        byte[] server_identity,
        int server_identity_len,
        byte[] ke2,
        int mhf,
        byte[] mhf_salt,
        int mhf_salt_len,
        byte[] context,
        int context_len
    );

    /**
     *
     *
     * @param ke1 (output) size:ecc_opaque_ristretto255_sha512_KE1SIZE
     * @param state (input, output) size:ecc_opaque_ristretto255_sha512_CLIENTSTATESIZE
     * @param credential_request size:ecc_opaque_ristretto255_sha512_CREDENTIALREQUESTSIZE
     * @param client_nonce size:ecc_opaque_ristretto255_sha512_Nn
     * @param client_secret size:ecc_opaque_ristretto255_sha512_Nsk
     * @param client_keyshare size:ecc_opaque_ristretto255_sha512_Npk
     */
    public static native void ecc_opaque_ristretto255_sha512_3DH_StartWithSecrets(
        byte[] ke1,
        byte[] state,
        byte[] credential_request,
        byte[] client_nonce,
        byte[] client_secret,
        byte[] client_keyshare
    );

    /**
     *
     *
     * @param ke1 (output) size:ecc_opaque_ristretto255_sha512_KE1SIZE
     * @param state (input, output) size:ecc_opaque_ristretto255_sha512_CLIENTSTATESIZE
     * @param credential_request size:ecc_opaque_ristretto255_sha512_CREDENTIALREQUESTSIZE
     */
    public static native void ecc_opaque_ristretto255_sha512_3DH_Start(
        byte[] ke1,
        byte[] state,
        byte[] credential_request
    );

    /**
     *
     *
     * @param ke3_raw (output) size:ecc_opaque_ristretto255_sha512_KE3SIZE
     * @param session_key (output) size:64
     * @param state_raw (input, output) size:ecc_opaque_ristretto255_sha512_CLIENTSTATESIZE
     * @param client_identity size:client_identity_len
     * @param client_identity_len the length of `client_identity`
     * @param client_private_key size:ecc_opaque_ristretto255_sha512_Nsk
     * @param server_identity size:server_identity_len
     * @param server_identity_len the lenght of `server_identity`
     * @param server_public_key size:ecc_opaque_ristretto255_sha512_Npk
     * @param ke2_raw size:ecc_opaque_ristretto255_sha512_KE2SIZE
     * @param context the application specific context, size:context_len
     * @param context_len the length of `context`
     * @return 0 if success, else -1
     */
    public static native int ecc_opaque_ristretto255_sha512_3DH_ClientFinalize(
        byte[] ke3_raw,
        byte[] session_key,
        byte[] state_raw,
        byte[] client_identity,
        int client_identity_len,
        byte[] client_private_key,
        byte[] server_identity,
        int server_identity_len,
        byte[] server_public_key,
        byte[] ke2_raw,
        byte[] context,
        int context_len
    );

    /**
     *
     *
     * @param ke2_raw (output) a KE2 structure, size:ecc_opaque_ristretto255_sha512_KE2SIZE
     * @param state_raw (input, output) a ServerState structure, size:ecc_opaque_ristretto255_sha512_SERVERSTATESIZE
     * @param server_identity the optional encoded server identity, which is set to
     * server_public_key if null, size:server_identity_len
     * @param server_identity_len the length of `server_identity`
     * @param server_private_key the server's private key, size:ecc_opaque_ristretto255_sha512_Nsk
     * @param server_public_key the server's public key, size:ecc_opaque_ristretto255_sha512_Npk
     * @param record_raw the client's RegistrationUpload structure, size:ecc_opaque_ristretto255_sha512_REGISTRATIONRECORDSIZE
     * @param credential_identifier an identifier that uniquely represents the credential
     * being registered, size:credential_identifier_len
     * @param credential_identifier_len the length of `credential_identifier`
     * @param oprf_seed the server-side seed of Nh bytes used to generate an oprf_key, size:ecc_opaque_ristretto255_sha512_Nh
     * @param ke1_raw a KE1 message structure, size:ecc_opaque_ristretto255_sha512_KE1SIZE
     * @param client_identity the optional encoded server identity, which is set to
     * client_public_key if null, size:client_identity_len
     * @param client_identity_len the length of `client_identity`
     * @param context the application specific context, size:context_len
     * @param context_len the length of `context`
     * @param masking_nonce size:ecc_opaque_ristretto255_sha512_Nn
     * @param server_nonce size:ecc_opaque_ristretto255_sha512_Nn
     * @param server_secret size:ecc_opaque_ristretto255_sha512_Nsk
     * @param server_keyshare size:ecc_opaque_ristretto255_sha512_Npk
     */
    public static native void ecc_opaque_ristretto255_sha512_ServerInitWithSecrets(
        byte[] ke2_raw,
        byte[] state_raw,
        byte[] server_identity,
        int server_identity_len,
        byte[] server_private_key,
        byte[] server_public_key,
        byte[] record_raw,
        byte[] credential_identifier,
        int credential_identifier_len,
        byte[] oprf_seed,
        byte[] ke1_raw,
        byte[] client_identity,
        int client_identity_len,
        byte[] context,
        int context_len,
        byte[] masking_nonce,
        byte[] server_nonce,
        byte[] server_secret,
        byte[] server_keyshare
    );

    /**
     *
     *
     * @param ke2_raw (output) a KE2 structure, size:ecc_opaque_ristretto255_sha512_KE2SIZE
     * @param state_raw (input, output) a ServerState structure, size:ecc_opaque_ristretto255_sha512_SERVERSTATESIZE
     * @param server_identity the optional encoded server identity, which is set to
     * server_public_key if null, size:server_identity_len
     * @param server_identity_len the length of `server_identity`
     * @param server_private_key the server's private key, size:ecc_opaque_ristretto255_sha512_Nsk
     * @param server_public_key the server's public key, size:ecc_opaque_ristretto255_sha512_Npk
     * @param record_raw the client's RegistrationUpload structure, size:ecc_opaque_ristretto255_sha512_REGISTRATIONRECORDSIZE
     * @param credential_identifier an identifier that uniquely represents the credential
     * being registered, size:credential_identifier_len
     * @param credential_identifier_len the length of `credential_identifier`
     * @param oprf_seed the server-side seed of Nh bytes used to generate an oprf_key, size:ecc_opaque_ristretto255_sha512_Nh
     * @param ke1_raw a KE1 message structure, size:ecc_opaque_ristretto255_sha512_KE1SIZE
     * @param client_identity the optional encoded server identity, which is set to
     * client_public_key if null, size:client_identity_len
     * @param client_identity_len the length of `client_identity`
     * @param context the application specific context, size:context_len
     * @param context_len the length of `context`
     */
    public static native void ecc_opaque_ristretto255_sha512_ServerInit(
        byte[] ke2_raw,
        byte[] state_raw,
        byte[] server_identity,
        int server_identity_len,
        byte[] server_private_key,
        byte[] server_public_key,
        byte[] record_raw,
        byte[] credential_identifier,
        int credential_identifier_len,
        byte[] oprf_seed,
        byte[] ke1_raw,
        byte[] client_identity,
        int client_identity_len,
        byte[] context,
        int context_len
    );

    /**
     *
     *
     * @param session_key (output) the shared session secret if and only if KE3 is valid, size:64
     * @param state (input, output) a ServerState structure, size:ecc_opaque_ristretto255_sha512_SERVERSTATESIZE
     * @param ke3 a KE3 structure, size:ecc_opaque_ristretto255_sha512_KE3SIZE
     * @return 0 if the user was authenticated, else -1
     */
    public static native int ecc_opaque_ristretto255_sha512_ServerFinish(
        byte[] session_key,
        byte[] state,
        byte[] ke3
    );

    /**
     *
     *
     * @param ke2_raw (output) size:ecc_opaque_ristretto255_sha512_KE2SIZE
     * @param state_raw (input, output) size:ecc_opaque_ristretto255_sha512_SERVERSTATESIZE
     * @param server_identity size:server_identity_len
     * @param server_identity_len the length of `server_identity`
     * @param server_private_key size:ecc_opaque_ristretto255_sha512_Nsk
     * @param server_public_key size:ecc_opaque_ristretto255_sha512_Npk
     * @param client_identity size:client_identity_len
     * @param client_identity_len the length of `client_identity`
     * @param client_public_key size:ecc_opaque_ristretto255_sha512_Npk
     * @param ke1_raw size:ecc_opaque_ristretto255_sha512_KE1SIZE
     * @param credential_response_raw size:ecc_opaque_ristretto255_sha512_CREDENTIALRESPONSESIZE
     * @param context size:context_len
     * @param context_len the length of `context`
     * @param server_nonce size:ecc_opaque_ristretto255_sha512_Nn
     * @param server_secret size:ecc_opaque_ristretto255_sha512_Nsk
     * @param server_keyshare size:ecc_opaque_ristretto255_sha512_Npk
     */
    public static native void ecc_opaque_ristretto255_sha512_3DH_ResponseWithSecrets(
        byte[] ke2_raw,
        byte[] state_raw,
        byte[] server_identity,
        int server_identity_len,
        byte[] server_private_key,
        byte[] server_public_key,
        byte[] client_identity,
        int client_identity_len,
        byte[] client_public_key,
        byte[] ke1_raw,
        byte[] credential_response_raw,
        byte[] context,
        int context_len,
        byte[] server_nonce,
        byte[] server_secret,
        byte[] server_keyshare
    );

    /**
     *
     *
     * @param ke2_raw (output) size:ecc_opaque_ristretto255_sha512_KE2SIZE
     * @param state_raw (input, output) size:ecc_opaque_ristretto255_sha512_SERVERSTATESIZE
     * @param server_identity size:server_identity_len
     * @param server_identity_len the length of `server_identity`
     * @param server_private_key size:ecc_opaque_ristretto255_sha512_Nsk
     * @param server_public_key size:ecc_opaque_ristretto255_sha512_Npk
     * @param client_identity size:client_identity_len
     * @param client_identity_len the length of `client_identity`
     * @param client_public_key size:ecc_opaque_ristretto255_sha512_Npk
     * @param ke1_raw size:ecc_opaque_ristretto255_sha512_KE1SIZE
     * @param credential_response_raw size:ecc_opaque_ristretto255_sha512_CREDENTIALRESPONSESIZE
     * @param context size:context_len
     * @param context_len the length of `context`
     */
    public static native void ecc_opaque_ristretto255_sha512_3DH_Response(
        byte[] ke2_raw,
        byte[] state_raw,
        byte[] server_identity,
        int server_identity_len,
        byte[] server_private_key,
        byte[] server_public_key,
        byte[] client_identity,
        int client_identity_len,
        byte[] client_public_key,
        byte[] ke1_raw,
        byte[] credential_response_raw,
        byte[] context,
        int context_len
    );

    // sign

    /**
     * Signature size.
     *
     */
    public static final int ecc_sign_ed25519_SIGNATURESIZE = 64;

    /**
     * Seed size.
     *
     */
    public static final int ecc_sign_ed25519_SEEDSIZE = 32;

    /**
     * Public key size.
     *
     */
    public static final int ecc_sign_ed25519_PUBLICKEYSIZE = 32;

    /**
     * Secret key size.
     *
     */
    public static final int ecc_sign_ed25519_SECRETKEYSIZE = 64;

    /**
     * Size of the signing private key (size of a scalar in BLS12-381).
     *
     */
    public static final int ecc_sign_eth_bls_PRIVATEKEYSIZE = 32;

    /**
     * Size of the signing public key (size of a compressed G1 element in BLS12-381).
     *
     */
    public static final int ecc_sign_eth_bls_PUBLICKEYSIZE = 48;

    /**
     * Signature size (size of a compressed G2 element in BLS12-381).
     *
     */
    public static final int ecc_sign_eth_bls_SIGNATURESIZE = 96;

    /**
     * Signs the `message` whose length is `message_len` in bytes, using the
     * secret key `sk`, and puts the signature into `signature`.
     *
     * @param signature (output) the signature, size:ecc_sign_ed25519_SIGNATURESIZE
     * @param message input message, size:message_len
     * @param message_len the length of `message`
     * @param sk the secret key, size:ecc_sign_ed25519_SECRETKEYSIZE
     */
    public static native void ecc_sign_ed25519_Sign(
        byte[] signature,
        byte[] message,
        int message_len,
        byte[] sk
    );

    /**
     * Verifies that `signature` is a valid signature for the `message` whose length
     * is `message_len` in bytes, using the signer's public key `pk`.
     *
     * @param signature the signature, size:ecc_sign_ed25519_SIGNATURESIZE
     * @param message input message, size:message_len
     * @param message_len the length of `message`
     * @param pk the public key, size:ecc_sign_ed25519_PUBLICKEYSIZE
     * @return -1 if the signature fails verification, or 0 on success
     */
    public static native int ecc_sign_ed25519_Verify(
        byte[] signature,
        byte[] message,
        int message_len,
        byte[] pk
    );

    /**
     * Generates a random key pair of public and private keys.
     *
     * @param pk (output) public key, size:ecc_sign_ed25519_PUBLICKEYSIZE
     * @param sk (output) private key, size:ecc_sign_ed25519_SECRETKEYSIZE
     */
    public static native void ecc_sign_ed25519_KeyPair(
        byte[] pk,
        byte[] sk
    );

    /**
     * Generates a random key pair of public and private keys derived
     * from a `seed`.
     *
     * @param pk (output) public key, size:ecc_sign_ed25519_PUBLICKEYSIZE
     * @param sk (output) private key, size:ecc_sign_ed25519_SECRETKEYSIZE
     * @param seed seed to generate the keys, size:ecc_sign_ed25519_SEEDSIZE
     */
    public static native void ecc_sign_ed25519_SeedKeyPair(
        byte[] pk,
        byte[] sk,
        byte[] seed
    );

    /**
     * Extracts the seed from the secret key `sk` and copies it into `seed`.
     *
     * @param seed (output) the seed used to generate the secret key, size:ecc_sign_ed25519_SEEDSIZE
     * @param sk the secret key, size:ecc_sign_ed25519_SECRETKEYSIZE
     */
    public static native void ecc_sign_ed25519_SkToSeed(
        byte[] seed,
        byte[] sk
    );

    /**
     * Extracts the public key from the secret key `sk` and copies it into `pk`.
     *
     * @param pk (output) the public key, size:ecc_sign_ed25519_PUBLICKEYSIZE
     * @param sk the secret key, size:ecc_sign_ed25519_SECRETKEYSIZE
     */
    public static native void ecc_sign_ed25519_SkToPk(
        byte[] pk,
        byte[] sk
    );

    /**
     * Generates a secret key `sk` deterministically from a secret
     * octet string `ikm`. The secret key is guaranteed to be nonzero.
     *
     * For security, `ikm` MUST be infeasible to guess, e.g., generated
     * by a trusted source of randomness and be at least 32 bytes long.
     *
     * @param sk (output) a secret key, size:ecc_sign_eth_bls_PRIVATEKEYSIZE
     * @param ikm a secret octet string, size:ikm_len
     * @param ikm_len the length of `ikm`
     */
    public static native void ecc_sign_eth_bls_KeyGen(
        byte[] sk,
        byte[] ikm,
        int ikm_len
    );

    /**
     * Takes a secret key `sk` and outputs the corresponding public key `pk`.
     *
     * @param pk (output) a public key, size:ecc_sign_eth_bls_PUBLICKEYSIZE
     * @param sk the secret key, size:ecc_sign_eth_bls_PRIVATEKEYSIZE
     */
    public static native void ecc_sign_eth_bls_SkToPk(
        byte[] pk,
        byte[] sk
    );

    /**
     * Ensures that a public key is valid.  In particular, it ensures
     * that a public key represents a valid, non-identity point that
     * is in the correct subgroup.
     *
     * @param pk a public key in the format output by SkToPk, size:ecc_sign_eth_bls_PUBLICKEYSIZE
     * @return 0 for valid or -1 for invalid
     */
    public static native int ecc_sign_eth_bls_KeyValidate(
        byte[] pk
    );

    /**
     * Computes a signature from sk, a secret key, and a message message
     * and put the result in sig.
     *
     * @param signature (output) the signature, size:ecc_sign_eth_bls_SIGNATURESIZE
     * @param sk the secret key, size:ecc_sign_eth_bls_PRIVATEKEYSIZE
     * @param message input message, size:message_len
     * @param message_len the length of `message`
     */
    public static native void ecc_sign_eth_bls_Sign(
        byte[] signature,
        byte[] sk,
        byte[] message,
        int message_len
    );

    /**
     * Checks that a signature is valid for the message under the public key pk.
     *
     * @param pk the public key, size:ecc_sign_eth_bls_PUBLICKEYSIZE
     * @param message input message, size:message_len
     * @param message_len the length of `message`
     * @param signature the signature, size:ecc_sign_eth_bls_SIGNATURESIZE
     * @return 0 if valid, -1 if invalid
     */
    public static native int ecc_sign_eth_bls_Verify(
        byte[] pk,
        byte[] message,
        int message_len,
        byte[] signature
    );

    /**
     * Aggregates multiple signatures into one.
     *
     * @param signature (output) the aggregated signature that combines all inputs, size:ecc_sign_eth_bls_SIGNATURESIZE
     * @param signatures array of individual signatures, size:n*ecc_sign_eth_bls_SIGNATURESIZE
     * @param n amount of signatures in the array `signatures`
     * @return 0 if valid, -1 if invalid
     */
    public static native int ecc_sign_eth_bls_Aggregate(
        byte[] signature,
        byte[] signatures,
        int n
    );

    /**
     *
     *
     * @param pks size:n*ecc_sign_eth_bls_PUBLICKEYSIZE
     * @param n the number of public keys in `pks`
     * @param message size:message_len
     * @param message_len the length of `message`
     * @param signature size:ecc_sign_eth_bls_SIGNATURESIZE
     * @return 0 if valid, -1 if invalid
     */
    public static native int ecc_sign_eth_bls_FastAggregateVerify(
        byte[] pks,
        int n,
        byte[] message,
        int message_len,
        byte[] signature
    );

    /**
     * Checks an aggregated signature over several (PK, message) pairs. The
     * messages are concatenated and in PASCAL-encoded form [size, chars].
     *
     * In order to keep the API simple, the maximum length of a message is 255.
     *
     * @param n number of pairs
     * @param pks size:n*ecc_sign_eth_bls_PUBLICKEYSIZE
     * @param messages size:messages_len
     * @param messages_len total length of the buffer `messages`
     * @param signature size:ecc_sign_eth_bls_SIGNATURESIZE
     * @return 0 if valid, -1 if invalid
     */
    public static native int ecc_sign_eth_bls_AggregateVerify(
        int n,
        byte[] pks,
        byte[] messages,
        int messages_len,
        byte[] signature
    );

    // frost

    /**
     * Size of a scalar, since this is using the ristretto255
     * curve the size is 32 bytes.
     *
     */
    public static final int ecc_frost_ristretto255_sha512_SCALARSIZE = 32;

    /**
     * Size of an element, since this is using the ristretto255
     * curve the size is 32 bytes.
     *
     */
    public static final int ecc_frost_ristretto255_sha512_ELEMENTSIZE = 32;

    /**
     * Size of a scalar point for polynomial evaluation (x, y).
     *
     */
    public static final int ecc_frost_ristretto255_sha512_POINTSIZE = 64;

    /**
     *
     *
     */
    public static final int ecc_frost_ristretto255_sha512_COMMITMENTSIZE = 96;

    /**
     *
     *
     */
    public static final int ecc_frost_ristretto255_sha512_BINDINGFACTORSIZE = 64;

    /**
     * Size of a private key, since this is using the ristretto255
     * curve the size is 32 bytes, the size of an scalar.
     *
     */
    public static final int ecc_frost_ristretto255_sha512_SECRETKEYSIZE = 32;

    /**
     * Size of a public key, since this is using the ristretto255
     * curve the size is 32 bytes, the size of a group element.
     *
     */
    public static final int ecc_frost_ristretto255_sha512_PUBLICKEYSIZE = 32;

    /**
     * Size of a schnorr signature, a pair of a scalar and an element.
     *
     */
    public static final int ecc_frost_ristretto255_sha512_SIGNATURESIZE = 64;

    /**
     * Size of a nonce tuple.
     *
     */
    public static final int ecc_frost_ristretto255_sha512_NONCEPAIRSIZE = 64;

    /**
     * Size of a nonce commitment tuple.
     *
     */
    public static final int ecc_frost_ristretto255_sha512_NONCECOMMITMENTPAIRSIZE = 64;

    /**
     *
     *
     * @param nonce (output) size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param secret size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param random_bytes size:32
     */
    public static native void ecc_frost_ristretto255_sha512_nonce_generate_with_randomness(
        byte[] nonce,
        byte[] secret,
        byte[] random_bytes
    );

    /**
     *
     *
     * @param nonce (output) size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param secret size:ecc_frost_ristretto255_sha512_SCALARSIZE
     */
    public static native void ecc_frost_ristretto255_sha512_nonce_generate(
        byte[] nonce,
        byte[] secret
    );

    /**
     * Lagrange coefficients are used in FROST to evaluate a polynomial f at f(0),
     * given a set of t other points, where f is represented as a set of coefficients.
     *
     * @param L_i (output) the i-th Lagrange coefficient, size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param x_i an x-coordinate contained in L, a scalar, size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param L the set of x-coordinates, each a scalar, size:L_len*ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param L_len the number of x-coordinates in `L`
     */
    public static native void ecc_frost_ristretto255_sha512_derive_interpolating_value(
        byte[] L_i,
        byte[] x_i,
        byte[] L,
        int L_len
    );

    /**
     * This is an optimization that works like `ecc_frost_ristretto255_sha512_derive_interpolating_value`
     * but with a set of points (x, y).
     *
     * @param L_i (output) the i-th Lagrange coefficient, size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param x_i an x-coordinate contained in L, a scalar, size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param L the set of (x, y)-points, size:L_len*ecc_frost_ristretto255_sha512_POINTSIZE
     * @param L_len the number of (x, y)-points in `L`
     */
    public static native void ecc_frost_ristretto255_sha512_derive_interpolating_value_with_points(
        byte[] L_i,
        byte[] x_i,
        byte[] L,
        int L_len
    );

    /**
     * Encodes a list of participant commitments into a bytestring for use in the
     * FROST protocol.
     *
     * @param out (output) size:commitment_list_len*ecc_frost_ristretto255_sha512_COMMITMENTSIZE
     * @param commitment_list a list of commitments issued by each signer, MUST be sorted in ascending order by signer index, size:commitment_list_len*ecc_frost_ristretto255_sha512_COMMITMENTSIZE
     * @param commitment_list_len the number of elements in `commitment_list`
     */
    public static native void ecc_frost_ristretto255_sha512_encode_group_commitment_list(
        byte[] out,
        byte[] commitment_list,
        int commitment_list_len
    );

    /**
     * Extracts participant identifiers from a commitment list.
     *
     * @param identifiers (output) size:commitment_list_len*ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param commitment_list a list of commitments issued by each signer, MUST be sorted in ascending order by signer index, size:commitment_list_len*ecc_frost_ristretto255_sha512_COMMITMENTSIZE
     * @param commitment_list_len the number of elements in `commitment_list`
     */
    public static native void ecc_frost_ristretto255_sha512_participants_from_commitment_list(
        byte[] identifiers,
        byte[] commitment_list,
        int commitment_list_len
    );

    /**
     * Extracts a binding factor from a list of binding factors.
     *
     * @param binding_factor (output) size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param binding_factor_list a list of binding factors for each participant, MUST be sorted in ascending order by signer index, size:binding_factor_list_len*ecc_frost_ristretto255_sha512_BINDINGFACTORSIZE
     * @param binding_factor_list_len the number of elements in `binding_factor_list`
     * @param identifier participant identifier, size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @return 0 on success, or -1 if the designated participant is not known
     */
    public static native int ecc_frost_ristretto255_sha512_binding_factor_for_participant(
        byte[] binding_factor,
        byte[] binding_factor_list,
        int binding_factor_list_len,
        byte[] identifier
    );

    /**
     * Compute binding factors based on the participant commitment list and message
     * to be signed.
     *
     * @param binding_factor_list (output) list of binding factors (identifier, Scalar) tuples representing the binding factors, size:commitment_list_len*ecc_frost_ristretto255_sha512_BINDINGFACTORSIZE
     * @param commitment_list a list of commitments issued by each signer, MUST be sorted in ascending order by signer index, size:commitment_list_len*ecc_frost_ristretto255_sha512_COMMITMENTSIZE
     * @param commitment_list_len the number of elements in `commitment_list`
     * @param msg the message to be signed, size:msg_len
     * @param msg_len the length of `msg`
     */
    public static native void ecc_frost_ristretto255_sha512_compute_binding_factors(
        byte[] binding_factor_list,
        byte[] commitment_list,
        int commitment_list_len,
        byte[] msg,
        int msg_len
    );

    /**
     * Create the group commitment from a commitment list.
     *
     * @param group_comm (output) size:ecc_frost_ristretto255_sha512_ELEMENTSIZE
     * @param commitment_list a list of commitments issued by each signer, MUST be sorted in ascending order by signer index, size:commitment_list_len*ecc_frost_ristretto255_sha512_COMMITMENTSIZE
     * @param commitment_list_len the number of elements in `commitment_list`
     * @param binding_factor_list size:ecc_frost_ristretto255_sha512_BINDINGFACTORSIZE
     * @param binding_factor_list_len the number of elements in `binding_factor_list`
     */
    public static native void ecc_frost_ristretto255_sha512_compute_group_commitment(
        byte[] group_comm,
        byte[] commitment_list,
        int commitment_list_len,
        byte[] binding_factor_list,
        int binding_factor_list_len
    );

    /**
     * Create the per-message challenge.
     *
     * @param challenge (output) a challenge Scalar value, size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param group_commitment an Element representing the group commitment, size:ecc_frost_ristretto255_sha512_ELEMENTSIZE
     * @param group_public_key public key corresponding to the signer secret key share, size:ecc_frost_ristretto255_sha512_PUBLICKEYSIZE
     * @param msg the message to be signed (sent by the Coordinator), size:msg_len
     * @param msg_len the length of `msg`
     */
    public static native void ecc_frost_ristretto255_sha512_compute_challenge(
        byte[] challenge,
        byte[] group_commitment,
        byte[] group_public_key,
        byte[] msg,
        int msg_len
    );

    /**
     *
     *
     * @param nonce (output) a nonce pair, size:ecc_frost_ristretto255_sha512_NONCEPAIRSIZE
     * @param comm (output) a nonce commitment pair, size:ecc_frost_ristretto255_sha512_NONCECOMMITMENTPAIRSIZE
     * @param sk_i the secret key share, size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param hiding_nonce_randomness size:32
     * @param binding_nonce_randomness size:32
     */
    public static native void ecc_frost_ristretto255_sha512_commit_with_randomness(
        byte[] nonce,
        byte[] comm,
        byte[] sk_i,
        byte[] hiding_nonce_randomness,
        byte[] binding_nonce_randomness
    );

    /**
     *
     *
     * @param nonce (output) a nonce pair, size:ecc_frost_ristretto255_sha512_NONCEPAIRSIZE
     * @param comm (output) a nonce commitment pair, size:ecc_frost_ristretto255_sha512_NONCECOMMITMENTPAIRSIZE
     * @param sk_i the secret key share, size:ecc_frost_ristretto255_sha512_SCALARSIZE
     */
    public static native void ecc_frost_ristretto255_sha512_commit(
        byte[] nonce,
        byte[] comm,
        byte[] sk_i
    );

    /**
     * To produce a signature share.
     *
     * @param sig_share (output) signature share, size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param identifier identifier of the signer. Note identifier will never equal 0, size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param sk_i signer secret key share, size:ecc_frost_ristretto255_sha512_SECRETKEYSIZE
     * @param group_public_key public key corresponding to the signer secret key share, size:ecc_frost_ristretto255_sha512_PUBLICKEYSIZE
     * @param nonce_i pair of scalar values generated in round one, size:ecc_frost_ristretto255_sha512_NONCEPAIRSIZE
     * @param msg the message to be signed (sent by the Coordinator), size:msg_len
     * @param msg_len the length of `msg`
     * @param commitment_list a list of commitments issued by each signer, MUST be sorted in ascending order by signer index, size:commitment_list_len*ecc_frost_ristretto255_sha512_COMMITMENTSIZE
     * @param commitment_list_len the number of elements in `commitment_list`
     */
    public static native void ecc_frost_ristretto255_sha512_sign(
        byte[] sig_share,
        byte[] identifier,
        byte[] sk_i,
        byte[] group_public_key,
        byte[] nonce_i,
        byte[] msg,
        int msg_len,
        byte[] commitment_list,
        int commitment_list_len
    );

    /**
     * Performs the aggregate operation to obtain the resulting signature.
     *
     * @param signature (output) a Schnorr signature consisting of an Element and Scalar value, size:ecc_frost_ristretto255_sha512_SIGNATURESIZE
     * @param commitment_list the group commitment returned by compute_group_commitment, size:commitment_list_len*ecc_frost_ristretto255_sha512_PUBLICKEYSIZE
     * @param commitment_list_len the group commitment returned by compute_group_commitment, size:ecc_frost_ristretto255_sha512_PUBLICKEYSIZE
     * @param msg the message to be signed (sent by the Coordinator), size:msg_len
     * @param msg_len the length of `msg`
     * @param sig_shares a set of signature shares z_i for each signer, size:sig_shares_len*ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param sig_shares_len the number of elements in `sig_shares`, must satisfy THRESHOLD_LIMIT
     * <
     * = sig_shares_len
     * <
     * = MAX_SIGNERS
     */
    public static native void ecc_frost_ristretto255_sha512_aggregate(
        byte[] signature,
        byte[] commitment_list,
        int commitment_list_len,
        byte[] msg,
        int msg_len,
        byte[] sig_shares,
        int sig_shares_len
    );

    /**
     * Check that the signature share is valid.
     *
     * @param identifier identifier of the signer. Note identifier will never equal 0, size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param public_key_share_i the public key for the ith signer, size:ecc_frost_ristretto255_sha512_PUBLICKEYSIZE
     * @param comm_i pair of Element values (hiding_nonce_commitment, binding_nonce_commitment) generated in round one from the ith signer, size:ecc_frost_ristretto255_sha512_NONCECOMMITMENTPAIRSIZE
     * @param sig_share_i a Scalar value indicating the signature share as produced in round two from the ith signer, size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param commitment_list a list of commitments issued by each signer, MUST be sorted in ascending order by signer index, size:commitment_list_len*ecc_frost_ristretto255_sha512_COMMITMENTSIZE
     * @param commitment_list_len the number of elements in `commitment_list`
     * @param group_public_key the public key for the group, size:ecc_frost_ristretto255_sha512_PUBLICKEYSIZE
     * @param msg the message to be signed (sent by the Coordinator), size:msg_len
     * @param msg_len the length of `msg`
     * @return 1 if the signature share is valid, and 0 otherwise.
     */
    public static native int ecc_frost_ristretto255_sha512_verify_signature_share(
        byte[] identifier,
        byte[] public_key_share_i,
        byte[] comm_i,
        byte[] sig_share_i,
        byte[] commitment_list,
        int commitment_list_len,
        byte[] group_public_key,
        byte[] msg,
        int msg_len
    );

    /**
     * Map arbitrary inputs to non-zero Scalar elements of the prime-order group scalar field.
     *
     * @param h1 (output) size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param m size:m_len
     * @param m_len the length of `m`
     */
    public static native void ecc_frost_ristretto255_sha512_H1(
        byte[] h1,
        byte[] m,
        int m_len
    );

    /**
     * Map arbitrary inputs to non-zero Scalar elements of the prime-order group scalar field.
     *
     * This is a variant of H2 that folds internally all inputs in the same
     * hash calculation.
     *
     * @param h1 (output) size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param m1 size:m1_len
     * @param m1_len the length of `m1`
     * @param m2 size:m2_len
     * @param m2_len the length of `m2`
     */
    public static native void ecc_frost_ristretto255_sha512_H1_2(
        byte[] h1,
        byte[] m1,
        int m1_len,
        byte[] m2,
        int m2_len
    );

    /**
     * Map arbitrary inputs to non-zero Scalar elements of the prime-order group scalar field.
     *
     * @param h2 (output) size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param m size:m_len
     * @param m_len the length of `m`
     */
    public static native void ecc_frost_ristretto255_sha512_H2(
        byte[] h2,
        byte[] m,
        int m_len
    );

    /**
     * Map arbitrary inputs to non-zero Scalar elements of the prime-order group scalar field.
     *
     * This is a variant of H2 that folds internally all inputs in the same
     * hash calculation.
     *
     * @param h2 (output) size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param m1 size:m1_len
     * @param m1_len the length of `m1`
     * @param m2 size:m2_len
     * @param m2_len the length of `m2`
     * @param m3 size:m3_len
     * @param m3_len the length of `m3`
     */
    public static native void ecc_frost_ristretto255_sha512_H2_3(
        byte[] h2,
        byte[] m1,
        int m1_len,
        byte[] m2,
        int m2_len,
        byte[] m3,
        int m3_len
    );

    /**
     * This is an alias for the ciphersuite hash function with
     * domain separation applied.
     *
     * @param h3 (output) size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param m size:m_len
     * @param m_len the length of `m`
     */
    public static native void ecc_frost_ristretto255_sha512_H3(
        byte[] h3,
        byte[] m,
        int m_len
    );

    /**
     * This is an alias for the ciphersuite hash function with
     * domain separation applied.
     *
     * This is a variant of H3 that folds internally all inputs in the same
     * hash calculation.
     *
     * @param h3 (output) size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param m1 size:m1_len
     * @param m1_len the length of `m1`
     * @param m2 size:m2_len
     * @param m2_len the length of `m2`
     */
    public static native void ecc_frost_ristretto255_sha512_H3_2(
        byte[] h3,
        byte[] m1,
        int m1_len,
        byte[] m2,
        int m2_len
    );

    /**
     * Implemented by computing H(contextString || "msg" || m).
     *
     * @param h4 (output) size:64
     * @param m size:m_len
     * @param m_len the length of `m`
     */
    public static native void ecc_frost_ristretto255_sha512_H4(
        byte[] h4,
        byte[] m,
        int m_len
    );

    /**
     * Implemented by computing H(contextString || "com" || m).
     *
     * @param h5 (output) size:64
     * @param m size:m_len
     * @param m_len the length of `m`
     */
    public static native void ecc_frost_ristretto255_sha512_H5(
        byte[] h5,
        byte[] m,
        int m_len
    );

    /**
     * Generate a single-party setting Schnorr signature.
     *
     * @param signature (output) signature, size:ecc_frost_ristretto255_sha512_SIGNATURESIZE
     * @param msg message to be signed, size:msg_len
     * @param msg_len the length of `msg`
     * @param SK private key, a scalar, size:ecc_frost_ristretto255_sha512_SECRETKEYSIZE
     */
    public static native void ecc_frost_ristretto255_sha512_prime_order_sign(
        byte[] signature,
        byte[] msg,
        int msg_len,
        byte[] SK
    );

    /**
     * Verify a Schnorr signature.
     *
     * @param msg signed message, size:msg_len
     * @param msg_len the length of `msg`
     * @param signature signature, size:ecc_frost_ristretto255_sha512_SIGNATURESIZE
     * @param PK public key, a group element, size:ecc_frost_ristretto255_sha512_PUBLICKEYSIZE
     * @return 1 if signature is valid, and 0 otherwise
     */
    public static native int ecc_frost_ristretto255_sha512_prime_order_verify(
        byte[] msg,
        int msg_len,
        byte[] signature,
        byte[] PK
    );

    /**
     *
     *
     * @param participant_private_keys (output) MAX_PARTICIPANTS shares of the secret key s, each a tuple consisting of the participant identifier (a NonZeroScalar) and the key share (a Scalar), size:n*ecc_frost_ristretto255_sha512_POINTSIZE
     * @param group_public_key (output) public key corresponding to the group signing key, an Element, size:ecc_frost_ristretto255_sha512_ELEMENTSIZE
     * @param vss_commitment (output) a vector commitment of Elements in G, to each of the coefficients in the polynomial defined by secret_key_shares and whose first element is G.ScalarBaseMult(s), size:t*ecc_frost_ristretto255_sha512_ELEMENTSIZE
     * @param polynomial_coefficients (output) size:t*ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param secret_key a group secret, a Scalar, that MUST be derived from at least Ns bytes of entropy, size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param n the number of shares to generate
     * @param t the threshold of the secret sharing scheme
     * @param coefficients size:(t-1)*ecc_frost_ristretto255_sha512_SCALARSIZE
     */
    public static native void ecc_frost_ristretto255_sha512_trusted_dealer_keygen_with_coefficients(
        byte[] participant_private_keys,
        byte[] group_public_key,
        byte[] vss_commitment,
        byte[] polynomial_coefficients,
        byte[] secret_key,
        int n,
        int t,
        byte[] coefficients
    );

    /**
     * Split a secret into shares.
     *
     * @param secret_key_shares (output) A list of n secret shares, each of which is an element of F, size:n*ecc_frost_ristretto255_sha512_POINTSIZE
     * @param polynomial_coefficients (output) a vector of t coefficients which uniquely determine a polynomial f, size:t*ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param s secret value to be shared, a scalar, size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param coefficients an array of size t - 1 with randomly generated scalars, not including the 0th coefficient of the polynomial, size:(t-1)*ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param n the number of shares to generate, an integer less than 2^16
     * @param t the threshold of the secret sharing scheme, an integer greater than 0
     * @return 0 if no errors, else -1
     */
    public static native int ecc_frost_ristretto255_sha512_secret_share_shard(
        byte[] secret_key_shares,
        byte[] polynomial_coefficients,
        byte[] s,
        byte[] coefficients,
        int n,
        int t
    );

    /**
     * Combines a shares list of length MIN_PARTICIPANTS to recover the secret.
     *
     * @param s (output) the resulting secret s that was previously split into shares, size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param shares a list of at minimum MIN_PARTICIPANTS secret shares, each a tuple (i, f(i)) where i and f(i) are Scalars, size:shares_len*ecc_frost_ristretto255_sha512_POINTSIZE
     * @param shares_len the number of shares in `shares`
     * @return 0 if no errors, else -1
     */
    public static native int ecc_frost_ristretto255_sha512_secret_share_combine(
        byte[] s,
        byte[] shares,
        int shares_len
    );

    /**
     * Evaluate a polynomial f at a particular input x, i.e., y = f(x)
     * using Horner's method.
     *
     * @param value (output) scalar result of the polynomial evaluated at input x, size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param x input at which to evaluate the polynomial, a scalar, size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param coeffs the polynomial coefficients, a list of scalars, size:coeffs_len*ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param coeffs_len the number of coefficients in `coeffs`
     */
    public static native void ecc_frost_ristretto255_sha512_polynomial_evaluate(
        byte[] value,
        byte[] x,
        byte[] coeffs,
        int coeffs_len
    );

    /**
     * Recover the constant term of an interpolating polynomial defined by a set
     * of points.
     *
     * @param f_zero (output) the constant term of f, i.e., f(0), a scalar, size:ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param points a set of t points with distinct x coordinates on a polynomial f, each a tuple of two Scalar values representing the x and y coordinates, size:points_len*ecc_frost_ristretto255_sha512_POINTSIZE
     * @param points_len the number of elements in `points`
     */
    public static native void ecc_frost_ristretto255_sha512_polynomial_interpolate_constant(
        byte[] f_zero,
        byte[] points,
        int points_len
    );

    /**
     * Compute the commitment using a polynomial f of degree at most MIN_PARTICIPANTS-1.
     *
     * @param vss_commitment (output) a vector commitment to each of the coefficients in coeffs, where each item of the vector commitment is an Element, size:coeffs_len*ecc_frost_ristretto255_sha512_ELEMENTSIZE
     * @param coeffs a vector of the MIN_PARTICIPANTS coefficients which uniquely determine a polynomial f, size:coeffs_len*ecc_frost_ristretto255_sha512_SCALARSIZE
     * @param coeffs_len the length of `coeffs`
     */
    public static native void ecc_frost_ristretto255_sha512_vss_commit(
        byte[] vss_commitment,
        byte[] coeffs,
        int coeffs_len
    );

    /**
     * For verification of a participant's share.
     *
     * @param share_i a tuple of the form (i, sk_i), size:ecc_frost_ristretto255_sha512_POINTSIZE
     * @param vss_commitment a vector commitment to each of the coefficients in coeffs, where each item of the vector commitment is an Element, size:t*ecc_frost_ristretto255_sha512_ELEMENTSIZE
     * @param t the threshold of the secret sharing scheme
     * @return 1 if sk_i is valid, and 0 otherwise.
     */
    public static native int ecc_frost_ristretto255_sha512_vss_verify(
        byte[] share_i,
        byte[] vss_commitment,
        int t
    );

    /**
     * Derive group info.
     *
     * @param PK (output) the public key representing the group, an Element, size:ecc_frost_ristretto255_sha512_ELEMENTSIZE
     * @param participant_public_keys (output) a list of MAX_PARTICIPANTS public keys PK_i for i=1,...,MAX_PARTICIPANTS, where each PK_i is the public key, an Element, for participant i., size:n*ecc_frost_ristretto255_sha512_ELEMENTSIZE
     * @param n the number of shares to generate
     * @param t the threshold of the secret sharing scheme
     * @param vss_commitment a VSS commitment to a secret polynomial f, a vector commitment to each of the coefficients in coeffs, where each element of the vector commitment is an Element, size:t*ecc_frost_ristretto255_sha512_ELEMENTSIZE
     */
    public static native void ecc_frost_ristretto255_sha512_derive_group_info(
        byte[] PK,
        byte[] participant_public_keys,
        int n,
        int t,
        byte[] vss_commitment
    );

    // pre

    /**
     * Size of the PRE-SCHEMA1 plaintext and ciphertext messages (size of a Fp12 element in BLS12-381).
     *
     */
    public static final int ecc_pre_schema1_MESSAGESIZE = 576;

    /**
     * Size of the PRE-SCHEMA1 seed used in all operations.
     *
     */
    public static final int ecc_pre_schema1_SEEDSIZE = 32;

    /**
     * Size of the PRE-SCHEMA1 public key (size of a G1 element in BLS12-381).
     *
     */
    public static final int ecc_pre_schema1_PUBLICKEYSIZE = 48;

    /**
     * Size of the PRE-SCHEMA1 private key (size of a scalar in BLS12-381).
     *
     */
    public static final int ecc_pre_schema1_PRIVATEKEYSIZE = 32;

    /**
     * Size of the PRE-SCHEMA1 signing public key (ed25519 signing public key size).
     *
     */
    public static final int ecc_pre_schema1_SIGNINGPUBLICKEYSIZE = 32;

    /**
     * Size of the PRE-SCHEMA1 signing private key (ed25519 signing secret key size).
     *
     */
    public static final int ecc_pre_schema1_SIGNINGPRIVATEKEYSIZE = 64;

    /**
     * Size of the PRE-SCHEMA1 signature (ed25519 signature size).
     *
     */
    public static final int ecc_pre_schema1_SIGNATURESIZE = 64;

    /**
     * Size of the whole ciphertext structure, that is the result of the simple Encrypt operation.
     *
     */
    public static final int ecc_pre_schema1_CIPHERTEXTLEVEL1SIZE = 752;

    /**
     * Size of the whole ciphertext structure, that is the result of the one-hop ReEncrypt operation.
     *
     */
    public static final int ecc_pre_schema1_CIPHERTEXTLEVEL2SIZE = 2096;

    /**
     * Size of the whole re-encryption key structure.
     *
     */
    public static final int ecc_pre_schema1_REKEYSIZE = 816;

    /**
     * Generates a random message suitable to use in the protocol.
     *
     * The output can be used in other key derivation algorithms for other
     * symmetric encryption protocols.
     *
     * @param m (output) a random plaintext message, size:ecc_pre_schema1_MESSAGESIZE
     */
    public static native void ecc_pre_schema1_MessageGen(
        byte[] m
    );

    /**
     * Derive a public/private key pair deterministically
     * from the input "seed".
     *
     * @param pk (output) public key, size:ecc_pre_schema1_PUBLICKEYSIZE
     * @param sk (output) private key, size:ecc_pre_schema1_PRIVATEKEYSIZE
     * @param seed input seed to generate the key pair, size:ecc_pre_schema1_SEEDSIZE
     */
    public static native void ecc_pre_schema1_DeriveKey(
        byte[] pk,
        byte[] sk,
        byte[] seed
    );

    /**
     * Generate a public/private key pair.
     *
     * @param pk (output) public key, size:ecc_pre_schema1_PUBLICKEYSIZE
     * @param sk (output) private key, size:ecc_pre_schema1_PRIVATEKEYSIZE
     */
    public static native void ecc_pre_schema1_KeyGen(
        byte[] pk,
        byte[] sk
    );

    /**
     * Derive a signing public/private key pair deterministically
     * from the input "seed".
     *
     * @param spk (output) signing public key, size:ecc_pre_schema1_SIGNINGPUBLICKEYSIZE
     * @param ssk (output) signing private key, size:ecc_pre_schema1_SIGNINGPRIVATEKEYSIZE
     * @param seed input seed to generate the key pair, size:ecc_pre_schema1_SEEDSIZE
     */
    public static native void ecc_pre_schema1_DeriveSigningKey(
        byte[] spk,
        byte[] ssk,
        byte[] seed
    );

    /**
     * Generate a signing public/private key pair.
     *
     * @param spk (output) signing public key, size:ecc_pre_schema1_SIGNINGPUBLICKEYSIZE
     * @param ssk (output) signing private key, size:ecc_pre_schema1_SIGNINGPRIVATEKEYSIZE
     */
    public static native void ecc_pre_schema1_SigningKeyGen(
        byte[] spk,
        byte[] ssk
    );

    /**
     * Encrypt a message `m` to delegatee j, given j’s public key (pk_j) and the
     * sender i’s signing key pair (spk_i, ssk_i). Produces a ciphertext C_j.
     *
     * This is also called encryption of level 1, since it's used to encrypt to
     * itself (i.e j == i), in order to have later the ciphertext re-encrypted
     * by the proxy with the re-encryption key (level 2).
     *
     * @param C_j_raw (output) a CiphertextLevel1_t structure, size:ecc_pre_schema1_CIPHERTEXTLEVEL1SIZE
     * @param m the plaintext message, size:ecc_pre_schema1_MESSAGESIZE
     * @param pk_j delegatee's public key, size:ecc_pre_schema1_PUBLICKEYSIZE
     * @param spk_i sender signing public key, size:ecc_pre_schema1_SIGNINGPUBLICKEYSIZE
     * @param ssk_i sender signing private key, size:ecc_pre_schema1_SIGNINGPRIVATEKEYSIZE
     * @param seed seed used to generate the internal ephemeral key, size:ecc_pre_schema1_SEEDSIZE
     */
    public static native void ecc_pre_schema1_EncryptWithSeed(
        byte[] C_j_raw,
        byte[] m,
        byte[] pk_j,
        byte[] spk_i,
        byte[] ssk_i,
        byte[] seed
    );

    /**
     * Encrypt a message `m` to delegatee j, given j’s public key (pk_j) and the
     * sender i’s signing key pair (spk_i, ssk_i). Produces a ciphertext C_j.
     *
     * This is also called encryption of level 1, since it's used to encrypt to
     * itself (i.e j == i), in order to have later the ciphertext re-encrypted
     * by the proxy with the re-encryption key (level 2).
     *
     * @param C_j_raw (output) a CiphertextLevel1_t structure, size:ecc_pre_schema1_CIPHERTEXTLEVEL1SIZE
     * @param m the plaintext message, size:ecc_pre_schema1_MESSAGESIZE
     * @param pk_j delegatee's public key, size:ecc_pre_schema1_PUBLICKEYSIZE
     * @param spk_i sender signing public key, size:ecc_pre_schema1_SIGNINGPUBLICKEYSIZE
     * @param ssk_i sender signing private key, size:ecc_pre_schema1_SIGNINGPRIVATEKEYSIZE
     */
    public static native void ecc_pre_schema1_Encrypt(
        byte[] C_j_raw,
        byte[] m,
        byte[] pk_j,
        byte[] spk_i,
        byte[] ssk_i
    );

    /**
     * Generate a re-encryption key from user i (the delegator) to user j (the delegatee).
     *
     * Requires the delegator’s private key (sk_i), the delegatee’s public key (pk_j), and
     * the delegator’s signing key pair (spk_i, ssk_i).
     *
     * @param tk_i_j_raw (output) a ReKey_t structure, size:ecc_pre_schema1_REKEYSIZE
     * @param sk_i delegator’s private key, size:ecc_pre_schema1_PRIVATEKEYSIZE
     * @param pk_j delegatee’s public key, size:ecc_pre_schema1_PUBLICKEYSIZE
     * @param spk_i delegator’s signing public key, size:ecc_pre_schema1_SIGNINGPUBLICKEYSIZE
     * @param ssk_i delegator’s signing private key, size:ecc_pre_schema1_SIGNINGPRIVATEKEYSIZE
     */
    public static native void ecc_pre_schema1_ReKeyGen(
        byte[] tk_i_j_raw,
        byte[] sk_i,
        byte[] pk_j,
        byte[] spk_i,
        byte[] ssk_i
    );

    /**
     * Re-encrypt a ciphertext encrypted to i (C_i) into a ciphertext encrypted
     * to j (C_j), given a re-encryption key (tk_i_j) and the proxy’s signing key
     * pair (spk, ssk).
     *
     * This operation is performed by the proxy and is also called encryption of
     * level 2, since it takes a ciphertext from a level 1 and re-encrypt it.
     *
     * It also validate the signature on the encrypted ciphertext and re-encryption key.
     *
     * @param C_j_raw (output) a CiphertextLevel2_t structure, size:ecc_pre_schema1_CIPHERTEXTLEVEL2SIZE
     * @param C_i_raw a CiphertextLevel1_t structure, size:ecc_pre_schema1_CIPHERTEXTLEVEL1SIZE
     * @param tk_i_j_raw a ReKey_t structure, size:ecc_pre_schema1_REKEYSIZE
     * @param spk_i delegator’s signing public key, size:ecc_pre_schema1_SIGNINGPUBLICKEYSIZE
     * @param pk_j delegatee’s public key, size:ecc_pre_schema1_PUBLICKEYSIZE
     * @param spk proxy’s signing public key, size:ecc_pre_schema1_SIGNINGPUBLICKEYSIZE
     * @param ssk proxy’s signing private key, size:ecc_pre_schema1_SIGNINGPRIVATEKEYSIZE
     * @return 0 if all the signatures are valid, -1 if there is an error
     */
    public static native int ecc_pre_schema1_ReEncrypt(
        byte[] C_j_raw,
        byte[] C_i_raw,
        byte[] tk_i_j_raw,
        byte[] spk_i,
        byte[] pk_j,
        byte[] spk,
        byte[] ssk
    );

    /**
     * Decrypt a signed ciphertext (C_i) given the private key of the recipient
     * i (sk_i). Returns the original message that was encrypted, m.
     *
     * This operations is usually performed by the delegator, since it encrypted
     * the message just to be stored and later be re-encrypted by the proxy.
     *
     * It also validate the signature on the encrypted ciphertext.
     *
     * @param m (output) the original plaintext message, size:ecc_pre_schema1_MESSAGESIZE
     * @param C_i_raw a CiphertextLevel1_t structure, size:ecc_pre_schema1_CIPHERTEXTLEVEL1SIZE
     * @param sk_i recipient private key, size:ecc_pre_schema1_PRIVATEKEYSIZE
     * @param spk_i recipient signing public key, size:ecc_pre_schema1_SIGNINGPUBLICKEYSIZE
     * @return 0 if all the signatures are valid, -1 if there is an error
     */
    public static native int ecc_pre_schema1_DecryptLevel1(
        byte[] m,
        byte[] C_i_raw,
        byte[] sk_i,
        byte[] spk_i
    );

    /**
     * Decrypt a signed ciphertext (C_j) given the private key of the recipient
     * j (sk_j). Returns the original message that was encrypted, m.
     *
     * This operations is usually performed by the delegatee, since it is the proxy
     * that re-encrypt the message and send the ciphertext to the final recipient.
     *
     * It also validate the signature on the encrypted ciphertext.
     *
     * @param m (output) the original plaintext message, size:ecc_pre_schema1_MESSAGESIZE
     * @param C_j_raw a CiphertextLevel2_t structure, size:ecc_pre_schema1_CIPHERTEXTLEVEL2SIZE
     * @param sk_j recipient private key, size:ecc_pre_schema1_PRIVATEKEYSIZE
     * @param spk proxy’s signing public key, size:ecc_pre_schema1_SIGNINGPUBLICKEYSIZE
     * @return 0 if all the signatures are valid, -1 if there is an error
     */
    public static native int ecc_pre_schema1_DecryptLevel2(
        byte[] m,
        byte[] C_j_raw,
        byte[] sk_j,
        byte[] spk
    );

}
