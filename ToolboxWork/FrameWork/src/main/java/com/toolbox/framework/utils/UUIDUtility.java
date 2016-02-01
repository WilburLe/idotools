package com.toolbox.framework.utils;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;


public class UUIDUtility {

    public static String uuid22f36(String uuid36) {
        UUID uuid = UUID.fromString(uuid36);
        byte[] binaryData = asBytes(uuid);
        return Base64.encodeBase64URLSafeString(binaryData);
    }

    public static String uuid22() {
        UUID uuid = UUID.randomUUID();
        byte[] binaryData = asBytes(uuid);
        return Base64.encodeBase64URLSafeString(binaryData);
    }

    public static String uuid32() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String uuid36() {
        return UUID.randomUUID().toString();
    }

    public static String uuid36f22(String uuid22) {
        byte[] binaryData = Base64.decodeBase64(uuid22);
        UUID uuid = asUuid(binaryData);
        return uuid.toString();
    }

    private static UUID asUuid(byte[] binaryData) {
        ByteBuffer bb = ByteBuffer.wrap(binaryData);
        long mostSigBits = bb.getLong();
        long leastSigBits = bb.getLong();
        return new UUID(mostSigBits, leastSigBits);
    }

    private static byte[] asBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

}
