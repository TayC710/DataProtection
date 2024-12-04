/*
 * Copyright (c) 2023, Alden Torres
 *
 * Licensed under the terms of the MIT license.
 * Copy of the license at https://opensource.org/licenses/MIT
 */

package org.ssohub.crypto.ecc.opaque;

import org.ssohub.crypto.ecc.Data;
import org.ssohub.crypto.ecc.ristretto255.R255Scalar;

import static org.ssohub.crypto.ecc.libecc.*;

/**
 * @author aldenml
 */
public final class Opaque {

    private Opaque() {
    }

    /**
     * Returns a randomly generated private and public key pair.
     * <p>
     * This is implemented by generating a random "seed", then
     * calling internally DeriveAuthKeyPair.
     *
     * @return a new key pair.
     */
    public static GenerateAuthKeyPairResult generateAuthKeyPair() {

        byte[] privateKey = new byte[ecc_opaque_ristretto255_sha512_Nsk];
        byte[] publicKey = new byte[ecc_opaque_ristretto255_sha512_Npk];

        ecc_opaque_ristretto255_sha512_GenerateAuthKeyPair(
                privateKey,
                publicKey
        );

        return new GenerateAuthKeyPairResult(
                new OpaqueSk(new Data(privateKey)),
                new OpaquePk(new Data(publicKey))
        );
    }

    /**
     * Recover the public key related to the input `privateKey`.
     *
     * @param privateKey the private key
     * @return the corresponding public key.
     */
    public static OpaquePk recoverPublicKey(OpaqueSk privateKey) {

        byte[] publicKey = new byte[ecc_opaque_ristretto255_sha512_Npk];

        ecc_opaque_ristretto255_sha512_RecoverPublicKey(
                publicKey,
                privateKey.toBytes()
        );

        return new OpaquePk(new Data(publicKey));
    }

    public static CreateRegistrationRequestResult createRegistrationRequestWithBlind(
            Data password,
            R255Scalar blind
    ) {
        byte[] passwordBytes = password.toBytes();

        byte[] request = new byte[ecc_opaque_ristretto255_sha512_REGISTRATIONREQUESTSIZE];

        ecc_opaque_ristretto255_sha512_CreateRegistrationRequestWithBlind(
                request,
                passwordBytes,
                passwordBytes.length,
                blind.toBytes()
        );

        return new CreateRegistrationRequestResult(
                new RegistrationRequest(new Data(request)),
                new R255Scalar(blind.getData())
        );
    }

    public static CreateRegistrationRequestResult createRegistrationRequest(
            Data password
    ) {
        byte[] passwordBytes = password.toBytes();

        byte[] request = new byte[ecc_opaque_ristretto255_sha512_REGISTRATIONREQUESTSIZE];
        byte[] blind = new byte[ecc_opaque_ristretto255_sha512_Ns];

        ecc_opaque_ristretto255_sha512_CreateRegistrationRequest(
                request,
                blind,
                passwordBytes,
                passwordBytes.length
        );

        return new CreateRegistrationRequestResult(
                new RegistrationRequest(new Data(request)),
                new R255Scalar(new Data(blind))
        );
    }

    public static RegistrationResponse createRegistrationResponse(
            RegistrationRequest registrationRequest,
            OpaquePk serverPublicKey,
            Data credentialIdentifier,
            Data oprfSeed
    ) {
        if (oprfSeed.size() != ecc_opaque_ristretto255_sha512_Nh)
            throw new IllegalArgumentException("oprf seed should be of size: " + ecc_opaque_ristretto255_sha512_Nh);

        byte[] credentialIdentifierBytes = credentialIdentifier.toBytes();

        byte[] response = new byte[ecc_opaque_ristretto255_sha512_REGISTRATIONRESPONSESIZE];

        ecc_opaque_ristretto255_sha512_CreateRegistrationResponse(
                response,
                registrationRequest.toBytes(),
                serverPublicKey.toBytes(),
                credentialIdentifierBytes,
                credentialIdentifierBytes.length,
                oprfSeed.toBytes()
        );

        return new RegistrationResponse(new Data(response));
    }

    public static FinalizeRegistrationRequestResult finalizeRegistrationRequestWithNonce(
            Data password,
            R255Scalar blind,
            RegistrationResponse registrationResponse,
            Data serverIdentity,
            Data clientIdentity,
            MHF mhf,
            Data mhfSalt,
            OpaqueSeed nonce
    ) {
        byte[] passwordBytes = password.toBytes();
        byte[] serverIdentityBytes = serverIdentity != null ? serverIdentity.toBytes() : new byte[0];
        byte[] clientIdentityBytes = clientIdentity != null ? clientIdentity.toBytes() : new byte[0];
        byte[] mhfSaltBytes = mhfSalt != null ? mhfSalt.toBytes() : new byte[0];

        byte[] record = new byte[ecc_opaque_ristretto255_sha512_REGISTRATIONRECORDSIZE];
        byte[] exportKey = new byte[ecc_opaque_ristretto255_sha512_Nh];

        ecc_opaque_ristretto255_sha512_FinalizeRegistrationRequestWithNonce(
                record,
                exportKey,
                passwordBytes,
                passwordBytes.length,
                blind.toBytes(),
                registrationResponse.toBytes(),
                serverIdentityBytes,
                serverIdentityBytes.length,
                clientIdentityBytes,
                clientIdentityBytes.length,
                mhf.intValue,
                mhfSaltBytes, mhfSaltBytes.length,
                nonce.toBytes()
        );

        return new FinalizeRegistrationRequestResult(
                new RegistrationRecord(new Data(record)),
                new Data(exportKey)
        );
    }

    public static FinalizeRegistrationRequestResult finalizeRegistrationRequest(
            Data password,
            R255Scalar blind,
            RegistrationResponse registrationResponse,
            Data serverIdentity,
            Data clientIdentity,
            MHF mhf,
            Data mhfSalt
    ) {
        byte[] passwordBytes = password.toBytes();
        byte[] serverIdentityBytes = serverIdentity != null ? serverIdentity.toBytes() : new byte[0];
        byte[] clientIdentityBytes = clientIdentity != null ? clientIdentity.toBytes() : new byte[0];
        byte[] mhfSaltBytes = mhfSalt != null ? mhfSalt.toBytes() : new byte[0];

        byte[] record = new byte[ecc_opaque_ristretto255_sha512_REGISTRATIONRECORDSIZE];
        byte[] exportKey = new byte[ecc_opaque_ristretto255_sha512_Nh];

        ecc_opaque_ristretto255_sha512_FinalizeRegistrationRequest(
                record,
                exportKey,
                passwordBytes,
                passwordBytes.length,
                blind.toBytes(),
                registrationResponse.toBytes(),
                serverIdentityBytes,
                serverIdentityBytes.length,
                clientIdentityBytes,
                clientIdentityBytes.length,
                mhf.intValue,
                mhfSaltBytes, mhfSaltBytes.length
        );

        return new FinalizeRegistrationRequestResult(
                new RegistrationRecord(new Data(record)),
                new Data(exportKey)
        );
    }

    public static KE1 clientInitWithSecrets(
            ClientState state,
            Data password,
            R255Scalar blind,
            OpaqueSeed clientNonce,
            OpaqueSk clientSecret,
            OpaquePk clientKeyshare
    ) {
        byte[] passwordBytes = password.toBytes();

        byte[] ke1 = new byte[ecc_opaque_ristretto255_sha512_KE1SIZE];

        ecc_opaque_ristretto255_sha512_ClientInitWithSecrets(
                ke1,
                state.data(),
                passwordBytes,
                passwordBytes.length,
                blind.toBytes(),
                clientNonce.toBytes(),
                clientSecret.toBytes(),
                clientKeyshare.toBytes()
        );

        return new KE1(new Data(ke1));
    }

    public static KE1 clientInit(
            ClientState state,
            Data password
    ) {
        byte[] passwordBytes = password.toBytes();

        byte[] ke1 = new byte[ecc_opaque_ristretto255_sha512_KE1SIZE];

        ecc_opaque_ristretto255_sha512_ClientInit(
                ke1,
                state.data(),
                passwordBytes,
                passwordBytes.length
        );

        return new KE1(new Data(ke1));
    }

    public static KE2 serverInitWithSecrets(
            ServerState state,
            Data serverIdentity,
            OpaqueSk serverPrivateKey,
            OpaquePk serverPublicKey,
            RegistrationRecord record,
            Data credentialIdentifier,
            Data oprfSeed,
            KE1 ke1,
            Data clientIdentity,
            Data context,
            OpaqueSeed maskingNonce,
            OpaqueSeed serverNonce,
            OpaqueSk serverSecret,
            OpaquePk serverKeyshare
    ) {
        if (oprfSeed.size() != ecc_opaque_ristretto255_sha512_Nh)
            throw new IllegalArgumentException("oprf seed should be of size: " + ecc_opaque_ristretto255_sha512_Nh);

        byte[] serverIdentityBytes = serverIdentity != null ? serverIdentity.toBytes() : new byte[0];
        byte[] clientIdentityBytes = clientIdentity != null ? clientIdentity.toBytes() : new byte[0];
        byte[] credentialIdentifierBytes = credentialIdentifier.toBytes();
        byte[] contextBytes = context.toBytes();

        byte[] ke2 = new byte[ecc_opaque_ristretto255_sha512_KE2SIZE];

        ecc_opaque_ristretto255_sha512_ServerInitWithSecrets(
                ke2,
                state.data(),
                serverIdentityBytes,
                serverIdentityBytes.length,
                serverPrivateKey.toBytes(),
                serverPublicKey.toBytes(),
                record.toBytes(),
                credentialIdentifierBytes,
                credentialIdentifierBytes.length,
                oprfSeed.toBytes(),
                ke1.toBytes(),
                clientIdentityBytes,
                clientIdentityBytes.length,
                contextBytes,
                contextBytes.length,
                maskingNonce.toBytes(),
                serverNonce.toBytes(),
                serverSecret.toBytes(),
                serverKeyshare.toBytes()
        );

        return new KE2(new Data(ke2));
    }

    public static KE2 serverInit(
            ServerState state,
            Data serverIdentity,
            OpaqueSk serverPrivateKey,
            OpaquePk serverPublicKey,
            RegistrationRecord record,
            Data credentialIdentifier,
            Data oprfSeed,
            KE1 ke1,
            Data clientIdentity,
            Data context
    ) {
        if (oprfSeed.size() != ecc_opaque_ristretto255_sha512_Nh)
            throw new IllegalArgumentException("oprf seed should be of size: " + ecc_opaque_ristretto255_sha512_Nh);

        byte[] serverIdentityBytes = serverIdentity != null ? serverIdentity.toBytes() : new byte[0];
        byte[] clientIdentityBytes = clientIdentity != null ? clientIdentity.toBytes() : new byte[0];
        byte[] credentialIdentifierBytes = credentialIdentifier.toBytes();
        byte[] contextBytes = context.toBytes();

        byte[] ke2 = new byte[ecc_opaque_ristretto255_sha512_KE2SIZE];

        ecc_opaque_ristretto255_sha512_ServerInit(
                ke2,
                state.data(),
                serverIdentityBytes,
                serverIdentityBytes.length,
                serverPrivateKey.toBytes(),
                serverPublicKey.toBytes(),
                record.toBytes(),
                credentialIdentifierBytes,
                credentialIdentifierBytes.length,
                oprfSeed.toBytes(),
                ke1.toBytes(),
                clientIdentityBytes,
                clientIdentityBytes.length,
                contextBytes,
                contextBytes.length
        );

        return new KE2(new Data(ke2));
    }

    public static ClientFinishResult clientFinish(
            ClientState state,
            Data clientIdentity,
            Data serverIdentity,
            KE2 ke2,
            MHF mhf,
            Data mhfSalt,
            Data context
    ) {
        byte[] serverIdentityBytes = serverIdentity != null ? serverIdentity.toBytes() : new byte[0];
        byte[] clientIdentityBytes = clientIdentity != null ? clientIdentity.toBytes() : new byte[0];
        byte[] mhfSaltBytes = mhfSalt != null ? mhfSalt.toBytes() : new byte[0];
        byte[] contextBytes = context.toBytes();

        byte[] ke3 = new byte[ecc_opaque_ristretto255_sha512_KE3SIZE];
        byte[] sessionKey = new byte[64];
        byte[] exportKey = new byte[64];

        int result = ecc_opaque_ristretto255_sha512_ClientFinish(
                ke3,
                sessionKey,
                exportKey,
                state.data(),
                clientIdentityBytes,
                clientIdentityBytes.length,
                serverIdentityBytes,
                serverIdentityBytes.length,
                ke2.toBytes(),
                mhf.intValue,
                mhfSaltBytes, mhfSaltBytes.length,
                contextBytes,
                contextBytes.length
        );

        return new ClientFinishResult(
                new KE3(new Data(ke3)),
                new Data(sessionKey),
                new Data(exportKey),
                result
        );
    }

    public static ServerFinishResult serverFinish(
            ServerState state,
            KE3 ke3
    ) {
        byte[] sessionKey = new byte[64];

        int result = ecc_opaque_ristretto255_sha512_ServerFinish(
                sessionKey,
                state.data(),
                ke3.toBytes()
        );

        return new ServerFinishResult(
                new Data(sessionKey),
                result
        );
    }

    public enum MHF {
        IDENTITY(0),
        SCRYPT(1),
        ARGON2ID(2);

        final int intValue;

        MHF(int intValue) {
            this.intValue = intValue;
        }
    }
}
