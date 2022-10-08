package com.client.eurekaclient.utilities.web3;

import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Arrays;

public class EthersUtils {
    private static final String MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

    public static String verifyMessage(String message, String signature) {
        return EthersUtils.recoverAddress(EthersUtils.hashMessage(message), signature);
    }

    public static String hashMessage(String message) {
        return Hash.sha3(
                Numeric.toHexStringNoPrefix(
                        (EthersUtils.MESSAGE_PREFIX + message.length() + message).getBytes(StandardCharsets.UTF_8)));
    }

    public static String recoverAddress(String digest, String signature) {
        Sign.SignatureData signatureData = EthersUtils.getSignatureData(signature);
        int header = 0;
        for (byte b : signatureData.getV()) {
            header = (header << 8) + (b & 0xFF);
        }
        if (header < 27 || header > 34) {
            return null;
        }
        int recId = header - 27;
        BigInteger key = Sign.recoverFromSignature(
                recId,
                new ECDSASignature(
                        new BigInteger(1, signatureData.getR()), new BigInteger(1, signatureData.getS())),
                Numeric.hexStringToByteArray(digest));
        if (key == null) {
            return null;
        }
        return ("0x" + Keys.getAddress(key)).trim();
    }

    private static Sign.SignatureData getSignatureData(String signature) {
        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }
        byte[] r = (byte[]) Arrays.copyOfRange(signatureBytes, 0, 32);
        byte[] s = (byte[]) Arrays.copyOfRange(signatureBytes, 32, 64);
        return new Sign.SignatureData(v, r, s);
    }

    public static String getUserAddressFromMessage(String signedHash, int originalMessage)
            throws SignatureException {

        String r = signedHash.substring(0, 66);
        String s = "0x" + signedHash.substring(66, 130);
        String v = "0x" + signedHash.substring(130, 132);

        String pubkey = Sign.signedPrefixedMessageToKey(String.valueOf(originalMessage).getBytes(), new Sign.SignatureData(
                        Numeric.hexStringToByteArray(v)[0], Numeric.hexStringToByteArray(r), Numeric.hexStringToByteArray(s)))
                .toString(16);

        return Keys.getAddress(pubkey);
    }
}