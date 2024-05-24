package cleanerVersion;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Util {

  public static boolean isUber(String historico) {
    String lowerCaseHistorico = historico.toLowerCase();
    return lowerCaseHistorico.contains("uber");
  }

  public static boolean isFood(String historico) {
    String lowerCaseHistorico = historico.toLowerCase();
    return (
      lowerCaseHistorico.contains("pizzaria") ||
      lowerCaseHistorico.contains("trigopane") ||
      lowerCaseHistorico.contains("hamburguer") ||
      lowerCaseHistorico.contains("sorvete") ||
      lowerCaseHistorico.contains("acai") ||
      lowerCaseHistorico.contains("subway") ||
      lowerCaseHistorico.contains("batata") ||
      lowerCaseHistorico.contains("mcdonalds") ||
      lowerCaseHistorico.contains("burger") ||
      lowerCaseHistorico.contains("burguer") ||
      lowerCaseHistorico.contains("burgueria") ||
      lowerCaseHistorico.contains("attelier") ||
      lowerCaseHistorico.contains("sucos")
    );
  }

  public static String removeDoubleSpaces(String str) {
    return str.replaceAll("\\s+", " ");
  }

  public static String removeGarbage(String str) {
    str =
      str
        .replaceAll("\\s+", " ")
        .replaceAll("[éêè]", "e")
        .replaceAll("[óôò]", "o")
        .replaceAll("[áãâà]", "a")
        .replaceAll("[í]", "i")
        .replaceAll("[ú]", "u")
        .replaceAll("[ç]", "c")
        .replaceAll("[Ã]", "")
        .replaceAll("©", "é")
        .replaceAll("¢", "â");

    if (str.contains("*")) {
      str = str.substring(str.indexOf("*") + 1);
    }
    str = str.replace("Belo Horizont Bra", "");
    if (Character.isLowerCase(str.charAt(0))) {
      str = Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
    return str;
  }

  public static List<String> findCSVFiles(File folder) {
    List<String> csvFiles = new ArrayList<>();
    if (folder.isDirectory()) {
      for (File file : folder.listFiles()) {
        if (file.isFile() && file.getName().toLowerCase().endsWith(".csv")) {
          csvFiles.add(file.getAbsolutePath());
        }
      }
    }
    return csvFiles;
  }

  public static String removeDotsReplaceComma(String valor) {
    valor = valor.replace(".", "");
    return valor.replace(',', '.');
  }

  public static String replaceDotToComma(float value) {
    return String.valueOf(value).replace('.', ',');
  }

  public static long dateToLong(Date date) {
    Calendar baseDate = Calendar.getInstance();
    baseDate.set(1900, 0, -2); // 00/01/1900
    long diffInMillies = Math.abs(date.getTime() - baseDate.getTimeInMillis());
    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    return diff;
  }

  public static float removeNegative(float num) {
    if (num < 0) {
      num = 0;
    }
    return num;
  }

  public static float removePositive(float num) {
    if (num > 0) {
      num = 0;
    }
    return num;
  }

  public static Date stringToDate(String date) {
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    Date formattedDate = null;
    try {
      formattedDate = format.parse(date);
    } catch (Exception e) {
      System.out.println("ERRO NA CONVERSAO DA DATA!");
    }
    return formattedDate;
  }
}
