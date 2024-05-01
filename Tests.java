import java.util.Scanner;
import java.io.File;
import java.text.Normalizer;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;

@SuppressWarnings("unused")
public class Tests {

  public static float replaceCommaToDot(String valor) {
    System.out.println(valor);
    valor = valor.replace(".", "");
    System.out.println(valor);
    return Float.parseFloat(valor.replace(',', '.'));
  }

  public static String removeAcentos(String str) {
    return str
      .replaceAll("[éêè]", "e")
      .replaceAll("[óôò]", "o")
      .replaceAll("[áãâà]", "a")
      .replaceAll("[í]", "i")
      .replaceAll("[ú]", "u")
      .replaceAll("[ç]", "c");
  }

  public static boolean isUber(String historico) {
    return (
      historico.contains("UBER") ||
      historico.contains("Uber") ||
      historico.contains("uber")
    );
  }

  public static String removeAccents2(String input) {
    if (input == null) {
      return null;
    }

    // Normalize the string to decompose accented characters into their combining forms
    String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

    // Use regex to remove combining diacritical marks
    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    return pattern.matcher(normalized).replaceAll("");
  }

  

  public static String removeDoubleSpaces(String str) {
    return str.replaceAll("\\s+", " ");
  }

  

  public static void main(String[] args) {
  JFileChooser chooser = new JFileChooser();
  String userHome = System.getProperty("user.home");
  File downloadsFolder = new File(userHome, "Downloads");
  chooser.setCurrentDirectory(downloadsFolder);
  chooser.setDialogTitle("choosertitle");
  chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
  chooser.setAcceptAllFileFilterUsed(false);

  if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
    System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
    System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
  } else {
    System.out.println("No Selection ");
  }
  }
}
