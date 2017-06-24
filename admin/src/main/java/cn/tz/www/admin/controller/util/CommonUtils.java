
package cn.tz.www.admin.controller.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 短网址服务.
 *
 * @author Wang.ch
 */
public class CommonUtils {
  private static final DateTimeFormatter DEFAULT_FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
  private static final DateTimeFormatter DEFAULT_SHORT_FORMATTER =
      DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");

  private CommonUtils() {}

  /**
   * 流水号转换为时间
   *
   * @param sn
   * @return
   */
  public static Date sn2Date(String sn) {
    if (sn.length() >= 17) {
      LocalDateTime dateTime = LocalDateTime.parse(sn.substring(0, 17), DEFAULT_FORMATTER);
      ZoneId zone = ZoneId.systemDefault();
      return Date.from(dateTime.atZone(zone).toInstant());
    }
    return null;
  }

  /**
   * 生成序列号
   *
   * @param id
   * @return
   */
  public static String sn(Long id) {
    return sn(String.valueOf(id));
  }

  public static String ccbSn() {
    return LocalDateTime.now().format(DEFAULT_SHORT_FORMATTER) + RandomStringUtils.randomNumeric(1);
  }

  /**
   * 生成序列号
   *
   * @param key
   * @return
   */
  public static String sn() {
    return sn((String) null);
  }

  /**
   * 生成序列号
   *
   * @param key
   * @return
   */
  public static String sn(String key) {
    if (key == null) {
      key = RandomStringUtils.randomAscii(5);
    }
    char[] chars = key.toCharArray();
    int sn = 0;
    for (char ch : chars) {
      sn += (int) ch;
    }
    String s = String.valueOf(sn);
    if (s.length() > 4) {
      s = s.substring(0, 4);
    }
    if (s.length() < 4) {
      s = String.format("%04d", sn);
    }
    return LocalDateTime.now().format(DEFAULT_FORMATTER)
        + s
        + "8"
        + RandomStringUtils.randomNumeric(4);
  }

  /**
   * 生成序列号
   *
   * @param id
   * @param localDateTime
   * @return
   */
  public static String sn(Long id, LocalDateTime localDateTime) {
    char[] chars = String.valueOf(id).toCharArray();
    int sn = 0;
    for (char ch : chars) {
      sn += (int) ch;
    }
    String s = String.valueOf(sn);
    if (s.length() > 4) {
      s = s.substring(0, 4);
    }
    if (s.length() < 4) {
      s = String.format("%04d", sn);
    }
    return localDateTime.format(DEFAULT_FORMATTER)
        + s
        + "8"
        + String.valueOf(new Random().nextInt(9999 - 1000 + 1) + 1000);
  }

  /**
   * 生成序列号
   *
   * @param id
   * @param date
   * @return
   */
  public static String sn(Long id, Date date) {
    char[] chars = String.valueOf(id).toCharArray();
    int sn = 0;
    for (char ch : chars) {
      sn += (int) ch;
    }
    String s = String.valueOf(sn);
    if (s.length() > 4) {
      s = s.substring(0, 4);
    }
    if (s.length() < 4) {
      s = String.format("%04d", sn);
    }
    ZoneId zone = ZoneId.systemDefault();
    LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), zone);
    return localDateTime.format(DEFAULT_FORMATTER)
        + s
        + "8"
        + String.valueOf(new Random().nextInt(9999 - 1000 + 1) + 1000);
  }

  public static String genWalletName(String type, Long userId) {
    return type + "_" + shortUrl(type + "_" + userId);
  }

  /**
   * 生成短网址key
   *
   * @param keyword
   * @return
   */
  public static String shortUrl(String keyword) {
    final String[] CHARS_DIC =
        new String[] {
          "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
              "s", "t", "u",
          "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C",
              "D", "E", "F",
          "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
              "Y", "Z"
        };
    byte[] encryptedTextBytes = null;
    try {
      MessageDigest md5Digest = MessageDigest.getInstance("MD5");
      md5Digest.reset();
      md5Digest.update(keyword.getBytes("UTF-8"));
      encryptedTextBytes = md5Digest.digest();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < 8; i++) {
      String hex1 = Integer.toHexString(0xff & encryptedTextBytes[i * 2]);
      String hex2 = Integer.toHexString(0xff & encryptedTextBytes[i * 2 + 1]);
      hex1 = hex1.length() == 1 ? "0" + hex1 : hex1;
      hex2 = hex2.length() == 1 ? "0" + hex2 : hex2;
      int index = (int) Long.parseLong(hex1 + hex2, 16) % CHARS_DIC.length;
      result.append(CHARS_DIC[index]);
    }
    return result.toString();
  }

  /**
   * 根据requet获取ip地址.
   *
   * @param request
   * @return
   */
  public static String getIpAddress(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }

  /**
   * unicode 转 中文
   *
   * @param unicode
   * @return
   */
  public static String uni2cn(String unicode) {
    String[] strs = unicode.split("\\\\u");
    StringBuffer returnStr = new StringBuffer();
    for (int i = 1; i < strs.length; i++) {
      returnStr.append((char) Integer.valueOf(strs[i], 16).intValue());
    }
    return returnStr.toString();
  }

  /**
   * 中文 转 unicode
   *
   * @param cn
   * @return
   */
  public static String cn2uni(String cn) {
    char[] chars = cn.toCharArray();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < chars.length; i++) {
      sb.append("\\u").append(Integer.toString(chars[i], 16));
    }
    return sb.toString();
  }
}
