package cn.roy.zlib.module_encrypt;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Description: AES加解密工具
 * @Author: zhouzongyi@cpic.com.cn
 * @Date: 2021/09/17
 * @Version: v1.0
 */
public class AESUtils {

    private AESUtils() {
    }

    public static String encrypt(String data, String key) {
        return encryptOrDecrypt(data, key, true);
    }

    public static String decrypt(String data, String key) {
        return encryptOrDecrypt(data, key, false);
    }

    public static String encryptOrDecrypt(String data, String key, Boolean isEncrypt) {
        if (data.isEmpty() || key.isEmpty()) {
            return null;
        }
        byte[] dataByteArray = isEncrypt ? string2Bytes(data) : hex2Bytes(data);
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(string2Bytes(key));
            keyGenerator.init(128, secureRandom);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] encodedKey = secretKey.getEncoded();
            System.out.println("编码后秘钥："+bytes2Hex(encodedKey));
            SecretKeySpec secretKeySpec = new SecretKeySpec(encodedKey, "AES");
            // TODO SecureRandom在Android平台的实现导致加密结果在java端解密失败，可通过以下代码处理两端一致问题
//          SecretKeySpec secretKeySpec = SecretKeySpec(key.encodeToByteArray(), "AES")
            // AES不指定模式和填充，默认为 AES/ECB/PKCS5Padding
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] result = cipher.doFinal(dataByteArray);
            return isEncrypt ? bytes2Hex(result) : bytes2String(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] string2Bytes(String data) {
        return data.getBytes(Charset.forName("UTF-8"));
    }

    private static String bytes2String(byte[] data) {
        return new String(data, Charset.forName("UTF-8"));
    }

    private static byte[] hex2Bytes(String data) {
        String upperCase = data.toUpperCase();
        int length = upperCase.length() / 2;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            int index = i * 2;
            bytes[i] = (byte) (Character.digit(upperCase.charAt(index), 16) << 4
                    + Character.digit(upperCase.charAt(index + 1), 16));
        }
        return bytes;
    }

    private static String bytes2Hex(byte[] data) {
        StringBuilder result = new StringBuilder();
        for (int index = 0, len = data.length; index <= len - 1; index += 1) {
            char character1 = Character.forDigit((data[index] >> 4) & 0xF, 16);
            char character2 = Character.forDigit((data[index]) & 0xF, 16);
            result.append(character1).append(character2);
        }
        return result.toString();
    }

}
