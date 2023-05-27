package com.LTTBDD.ecommerce_app.common.service;

import com.LTTBDD.ecommerce_app.common.utils.StringUtils;

import java.lang.reflect.UndeclaredThrowableException;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class OTPService {
    private static final int[] DIGITS_POWER = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000 };
    private static final int MILISECOND_BUFFER = 50;
    private static final String RETURN_DIGIT_PROPERTY = "8";
    private static final String SHA_256_AlG = "HmacSHA256";
    private static final String SECRET_KEY = "LTTBDD";

    private static final String EXPIRED_MINS = "50";


    public String generate(){
        try {
            String secret = SECRET_KEY;
            String otp = null;

            TimeZone utc = TimeZone.getTimeZone("UTC");
            Calendar currentDateTime = Calendar.getInstance(utc);
            long movingFactor = currentDateTime.getTimeInMillis();
            String returnDegit = RETURN_DIGIT_PROPERTY;
            String sha256 = SHA_256_AlG;

            System.out.println("currentDatetime:" + currentDateTime);
            System.out.println("movingFactor:" + movingFactor);

            otp = generateTOTP(secret, Long.toString(movingFactor), returnDegit, sha256);
            return otp;
        }
        catch (Exception e) {
            return null;
        }
    }

    public Boolean verifyOTP(String submittedOTP){
        try {
            String secret = SECRET_KEY;
            String otp = null;
            try {

                TimeZone utc = TimeZone.getTimeZone("UTC");
                Calendar currentDateTime = Calendar.getInstance(utc);

                long movingFactor = currentDateTime.getTimeInMillis();

                String returnDegit = RETURN_DIGIT_PROPERTY;
                String sha256 = SHA_256_AlG;

                otp = generateTOTP(secret, Long.toString(movingFactor), returnDegit, sha256);

                if(otp.equals(submittedOTP)) {
                    return true;
                }

                long timeInMillis = Long.parseLong(EXPIRED_MINS) * 60 * 1000 + MILISECOND_BUFFER;

                for (int i = 1; i < timeInMillis; i++) {
                    movingFactor--;
                    otp = generateTOTP(secret, Long.toString(movingFactor), returnDegit, sha256);

                    if (otp.equals(submittedOTP))
                        return true;
                    System.out.println("otp: " + otp);
                }
                return false;

            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private String generateTOTP(String key, String time, String returnDigits, String crypto) {
        int codeDigits = Integer.decode(returnDigits).intValue();
        String result = null;

        // Using the counter
        // First 8 bytes are for the movingFactor
        // Compliant with base RFC 4226 (HOTP)
        while (time.length() < 16)
            time = "0" + time;

        // Get the HEX in a Byte[]
        byte[] msg = hexStr2Bytes(time);
        byte[] k = hexStr2Bytes(key);

        byte[] hash = hmac_sha(crypto, k, msg);

        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16)
                | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[codeDigits];

        result = Integer.toString(otp);
        while (result.length() < codeDigits) {
            result = "0" + result;
        }
        return result;
    }

    private byte[] hmac_sha(String crypto, byte[] keyBytes, byte[] text) {
        try {
            Mac hmac;
            hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }
    private byte[] hexStr2Bytes(String hex) {
        // Adding one byte to get the right conversion
        // Values starting with "0" can be converted
        try {
//			byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();
            byte[] bArray = hex.getBytes();
            // Copy all the REAL bytes, not the "first"
            byte[] ret = new byte[bArray.length - 1];
            for (int i = 0; i < ret.length; i++)
                ret[i] = bArray[i + 1];
            return ret;
        } catch (Exception e) {
            return null;
        }
    }

}
