import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class InvoiceRegister {

  private Date data;
  private long dataNum;
  private String dataString;
  private String metodoPagamento;
  private String historico;
  private float valor;
  private boolean isUber = false;
  private boolean isComida = false;

  public static List<InvoiceRegister> read(String nomeArquivo)
    throws IOException {
    FileReader file = new FileReader(nomeArquivo);
    BufferedReader buffer = new BufferedReader(file);

    String line;
    int lineCount = 0;
    List<InvoiceRegister> allTransactions = new ArrayList<>();

    while ((line = buffer.readLine()) != null) {
      lineCount++;
      if (lineCount <= 5) {
        continue;
      }

      String[] fields = line.split(";");

      InvoiceRegister transaction = new InvoiceRegister();
      transaction.dataString =
        (fields.length > 0 && !fields[0].isEmpty()) ? fields[0] : "null";
      transaction.metodoPagamento =
        (fields.length > 1 && !fields[1].isEmpty())
          ? removeAccents(fields[1])
          : "null";
      transaction.historico =
        (fields.length > 2 && !fields[2].isEmpty())
          ? removeAccents(fields[2])
          : "Poupanca";
      transaction.valor =
        (fields.length > 3 && !fields[3].isEmpty())
          ? Float.parseFloat(removeDotsReplaceComma(fields[3]))
          : -1;

      transaction.data = stringToDate(transaction.dataString);
      transaction.dataNum = dateToLong(transaction.data);

      transaction.isUber = isUber(transaction.historico);
      transaction.isComida = isFood(transaction.historico);

      allTransactions.add(transaction);
    }

    buffer.close();
    file.close();
    return allTransactions;
  }

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

  public static String removeAccents(String str) {
    str = removeDoubleSpaces(str);
    return str
      .replaceAll("[éêè]", "e")
      .replaceAll("[óôò]", "o")
      .replaceAll("[áãâàÃ]", "chosenFilePath")
      .replaceAll("[í]", "i")
      .replaceAll("[ú]", "u")
      .replaceAll("[ç]", "c")
      .replaceAll("©", "")
      .replaceAll("¢", "");
  }

  public static String removeDotsReplaceComma(String valor) {
    valor = valor.replace(".", "");
    return valor.replace(',', '.');
  }

  public static String replaceDotToComma(float value) {
    return String.valueOf(value).replace('.', ',');
  }

  public static String normalize(String str) {
    if (str.contains("*")) {
      str = str.substring(str.indexOf("*") + 1);
    }
    str = str.replace("Belo Horizont Bra", "");
    if (Character.isLowerCase(str.charAt(0))) {
      str = Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
    return str;
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

  public static void writeToCsv(
    String filePath,
    List<InvoiceRegister> transactions
  ) {
    try {
      FileWriter file = new FileWriter(filePath);
      BufferedWriter buffer = new BufferedWriter(file);
      for (int i = transactions.size() - 1; i >= 0; i--) {
        InvoiceRegister transaction = transactions.get(i);
        String negativeValue = removeNegative(transaction.valor) == 0
          ? ""
          : replaceDotToComma(removeNegative(transaction.valor));
        String positiveValue = removePositive(transaction.valor) == 0
          ? ""
          : replaceDotToComma((removePositive(transaction.valor) * -1));

        if (transaction.isUber) {
          buffer.write(
            transaction.dataNum +
            ";" +
            ";" +
            normalize(transaction.historico) +
            ";" +
            positiveValue +
            ";" +
            ";" +
            ";" +
            ";" +
            ";" +
            "\n"
          );
        } else if (transaction.isComida) {
          buffer.write(
            transaction.dataNum +
            ";" +
            ";" +
            normalize(transaction.historico) +
            ";" +
            ";" +
            ";" +
            positiveValue +
            ";" +
            ";" +
            ";" +
            "\n"
          );
        } else {
          buffer.write(
            transaction.dataNum +
            ";" +
            ";" +
            normalize(transaction.historico) +
            ";" +
            ";" +
            ";" +
            ";" +
            ";" +
            negativeValue +
            ";" +
            positiveValue +
            "\n"
          );
        }
      }

      buffer.close();
      file.close();
    } catch (IOException e) {
      System.out.println("ERRO NA ESCRITA DO ARQUIVO!");
      e.printStackTrace();
    }
  }

  public void print() {
    System.out.println(toString());
  }

  public String toString() {
    String resp =
      "[" +
      dataString +
      " ## " +
      dataNum +
      " ## " +
      metodoPagamento +
      " ## " +
      historico +
      " ## ";
    resp += valor + "]";
    return resp;
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

  public static void main(String[] args) {
    // String testFileDirectory = "";
    // JFileChooser chooser = new JFileChooser();
    // String userHome = System.getProperty("user.home");
    // File downloadsFolder = new File(userHome, "Downloads");
    // chooser.setCurrentDirectory(downloadsFolder);
    // chooser.setDialogTitle("choosertitle");
    // chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    // chooser.setAcceptAllFileFilterUsed(false);

    // if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
    //   System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
    //   System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
    //   testFileDirectory = chooser.getSelectedFile().toString();
    //   testFileDirectory = testFileDirectory.replace("\\", "/");
    //   System.out.println("testFileDirectory: " + testFileDirectory);
    // } else {
    //   System.out.println("No Selection ");
    // }
    String chosenFilePath = "";
    JFileChooser chooser = new JFileChooser(
      System.getProperty("user.home") + "/Downloads"
    );
    FileNameExtensionFilter filter = new FileNameExtensionFilter(".csv", "csv");
    chooser.setFileFilter(filter);
    int returnVal = chooser.showOpenDialog(null);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      System.out.println(
        "ARQIVO ESCOLHIDO: " + chooser.getSelectedFile().getName()
      );
      chosenFilePath = chooser.getSelectedFile().getAbsolutePath();
    }
    chosenFilePath = chosenFilePath.replace("\\", "/");
    try {
      // String filePath = "";
      Scanner scanner = new Scanner(System.in);
      List<InvoiceRegister> allTransactions = new ArrayList<>();
      // String folderPath = testFileDirectory;
      // // "D:/gaming/site inovador/code/github/UNI/projects/extractDataFromCSV"
      // File folder = new File(folderPath);
      // List<String> csvFiles = findCSVFiles(folder);

      // if (csvFiles.isEmpty()) {
      //   System.out.println(
      //     "Nenhum arquivo csv encontrado na pasta especificada. Pasta: " +
      //     folderPath
      //   );
      // } else if (csvFiles.size() == 1) {
      //   filePath = csvFiles.get(0);
      //   System.out.println("Arquivo csv encontrado: " + filePath);
      // } else {
      //   System.out.println("Diversos arquivos CSV encontrados: ");
      //   for (int i = 0; i < csvFiles.size(); i++) {
      //     System.out.println((i + 1) + ". " + csvFiles.get(i));
      //   }
      //   System.out.print("Insira o número do arquivo CSV desejado: ");
      //   int choice = scanner.nextInt();
      //   if (choice >= 1 && choice <= csvFiles.size()) {
      //     System.out.println(
      //       "Você escolheu: \n" + csvFiles.get(choice - 1) + "\n"
      //     );
      //     filePath = csvFiles.get(choice - 1);
      //   } else {
      //     System.out.println("Escolha inválida.");
      //   }
      // }

      allTransactions = read(chosenFilePath);

      int countRegisters = 0;

      for (InvoiceRegister transaction : allTransactions) {
        transaction.print();
        countRegisters++;
      }

      System.out.println("\nTotal de registros: " + countRegisters);

      String outputPath = chosenFilePath;
      System.out.println(
        "Digite o nome do arquivo de saída (com ou sem extensao):"
      );
      String fileName = scanner.next();
      String csv = ".csv";
      outputPath += fileName;

      if (!fileName.endsWith(csv)) {
        outputPath += csv;
      }

      writeToCsv(outputPath, allTransactions);
      scanner.close();
    } catch (IOException e) {
      System.out.println("ERRO NA LEITURA DO ARQUIVO!");
      e.printStackTrace();
    }
  }
}
