package linkv.io.api.utils;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class CommUtils {
    public static String GenUniqueIDString(String appKey) {
        int nlen = 9;
        byte[] appKeyBytes = appKey.getBytes(StandardCharsets.UTF_8);
        String container = new String(subBytes(appKeyBytes, 2, appKeyBytes.length - 2)) + "-";
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

        StringBuffer sb = new StringBuffer();
        sb.append(container);

        int len = str.length();
        Random ran = new Random();
        for (int i = 0; i < nlen; i++) {
            sb.append(str.charAt(ran.nextInt(len)));
        }

        return sb.toString();
    }

    public static String GenRandomString() {
        int nLen = 16;
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

        StringBuffer sb = new StringBuffer();
        int len = str.length();
        Random ran = new Random();
        for (int i = 0; i < nLen; i++) {
            int idx = ran.nextInt(len);
            sb.append(str.charAt(idx));
            if (i == 7) {
                sb.append(System.currentTimeMillis() / 1000L);
            }
        }
        return sb.toString();
    }

    /**
     * 获取字符串的校验和，返回小写字符串
     * @param str
     * @param checkSumAlgo
     * @return
     */
    public static String genCheckSum(String str, String checkSumAlgo) {
        return genCheckSum(str, checkSumAlgo, true);
    }

    /**
     * 获取字符串的校验和
     * @param str
     * @param checkSumAlgo
     * @param isLowerCase 用以判断，返回值转换为大写，还是小写。
     * @return
     */
    public static String genCheckSum(String str, String checkSumAlgo, boolean isLowerCase) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(checkSumAlgo);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
        byte[] digestBytes = messageDigest.digest();

        String checkSum = DatatypeConverter.printHexBinary(digestBytes);
        return isLowerCase ? checkSum.toLowerCase() : checkSum.toUpperCase();
    }

    public static String GenSignature(HashMap<String, String> urlValues, String md5Secret) {
        String data = encode(urlValues) + "&key=" + md5Secret;

        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageDigest.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] digestBytes = messageDigest.digest();

        return DatatypeConverter.printHexBinary(digestBytes).toLowerCase();
    }

    private static String encode(HashMap<String, String> urlValues) {
        if (urlValues == null || urlValues.size() == 0) return "";

        StringBuffer sb = new StringBuffer();

        try {
            SortedMap<String, String> sortedMap = new TreeMap<>(urlValues);
            Set<String> keySet = sortedMap.keySet();
            for (String key : keySet) {
//                if (key.trim().equals("sign")) {
//                    continue;
//                }
                String value = sortedMap.get(key);
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(key + "=" + URLDecoder.decode(value, "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }

    public static String RandomString(int nLen) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

        StringBuffer sb = new StringBuffer();

        int len = str.length();
        Random ran = new Random();
        for (int i = 0; i < nLen; i++) {
            sb.append(str.charAt(ran.nextInt(len)));
        }
        return sb.toString();
    }

    public static String GenGUID() {
        return String.join("-", RandomString(9), RandomString(4), RandomString(4), RandomString(12));
    }
}
