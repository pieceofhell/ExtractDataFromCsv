package cleanerVersion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

class Invoice {

  public Date data;
  public long dataNum;
  public String dataString;
  public String metodoPagamento;
  public String historico;
  public float valor;
  public boolean isUber = false;
  public boolean isComida = false;

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
}

public class InvoiceRegister {

  public static List<Invoice> read(String nomeArquivo) throws IOException {
    FileReader file = new FileReader(nomeArquivo);
    BufferedReader buffer = new BufferedReader(file);

    String line;
    int lineCount = 0;
    List<Invoice> allTransactions = new ArrayList<>();

    while ((line = buffer.readLine()) != null) {
      lineCount++;
      if (lineCount <= 5) {
        continue;
      }

      String[] fields = line.split(";");

      Invoice transaction = new Invoice();
      transaction.dataString =
        (fields.length > 0 && !fields[0].isEmpty()) ? fields[0] : "null";
      transaction.metodoPagamento =
        (fields.length > 1 && !fields[1].isEmpty())
          ? Util.removeGarbage(fields[1])
          : "null";
      transaction.historico =
        (fields.length > 2 && !fields[2].isEmpty())
          ? Util.removeGarbage(fields[2])
          : "Poupanca";
      transaction.valor =
        (fields.length > 3 && !fields[3].isEmpty())
          ? Float.parseFloat(Util.removeDotsReplaceComma(fields[3]))
          : -1;

      transaction.data = Util.stringToDate(transaction.dataString);
      transaction.dataNum = Util.dateToLong(transaction.data);

      transaction.isUber = Util.isUber(transaction.historico);
      transaction.isComida = Util.isFood(transaction.historico);

      allTransactions.add(transaction);
    }

    buffer.close();
    file.close();
    return allTransactions;
  }

  public static void writeToCsv(String filePath, List<Invoice> transactions) {
    try {
      FileWriter file = new FileWriter(filePath);
      BufferedWriter buffer = new BufferedWriter(file);
      for (int i = transactions.size() - 1; i >= 0; i--) {
        Invoice transaction = transactions.get(i);
        String negativeValue = Util.removeNegative(transaction.valor) == 0
          ? ""
          : Util.replaceDotToComma(Util.removeNegative(transaction.valor));
        String positiveValue = Util.removePositive(transaction.valor) == 0
          ? ""
          : Util.replaceDotToComma(
            (Util.removePositive(transaction.valor) * -1)
          );

        if (transaction.isUber) {
          buffer.write(
            transaction.dataNum +
            ";" +
            ";" +
            Util.removeGarbage(transaction.historico) +
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
            Util.removeGarbage(transaction.historico) +
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
            Util.removeGarbage(transaction.historico) +
            ";" +
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
        "ARQIVO ESCOLHIDO: " + chooser.getSelectedFile().getName() + "\n"
      );
      chosenFilePath = chooser.getSelectedFile().getAbsolutePath();
    }
    chosenFilePath = chosenFilePath.replace("\\", "/");
    try {
      // String filePath = "";
      Scanner scanner = new Scanner(System.in);
      List<Invoice> allTransactions = new ArrayList<>();

      // String folderPath = testFileDirectory;
      // // "D:/gaming/site inovador/code/github/UNI/projects/extractDataFromCSV"
      // File folder = new File(folderPath);
      // List<String> csvFiles = Util.findCSVFiles(folder);

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

      for (Invoice transaction : allTransactions) {
        transaction.print();
        countRegisters++;
      }

      System.out.println("\nTotal de registros: " + countRegisters);

      // output file must be in the same folder in which the source .csv file was found
      String outputPath = chosenFilePath.substring(
        0,
        chosenFilePath.lastIndexOf("/") + 1
      );
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