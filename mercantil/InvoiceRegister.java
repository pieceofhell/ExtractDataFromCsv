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

  public static int nextId = 1; // This will be our ID counter
  public int id; // This will hold the ID for this specific Invoice
  public String numeroBanco;
  public String numeroAgencia;
  public String numeroConta;
  public Date data;
  public String numeroDocumento;
  public long dataNum;
  public String dataString;
  public String historico;
  public float valor;

  public Invoice() {
    this.id = nextId++; // Assign the next ID then increment the counter
  }

  public void print() {
    System.out.println(toString());
  }

  @Override
  public String toString() {
    String resp =
      "[" +
      id +
      " ## " +
      numeroBanco +
      " ## " +
      numeroAgencia +
      " ## " +
      numeroConta +
      " ## " +
      dataString +
      " ## " +
      numeroDocumento +
      " ## " +
      historico +
      " ## " +
      valor +
      "]";
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
      if (lineCount <= 0) {
        continue;
      }

      String[] fields = line.split(";");

      Invoice transaction = new Invoice();

      transaction.numeroBanco =
        (fields.length > 0 && !fields[0].isEmpty()) ? fields[0] : "";
      transaction.numeroAgencia =
        (fields.length > 1 && !fields[1].isEmpty()) ? fields[1] : "";
      transaction.numeroConta =
        (fields.length > 2 && !fields[2].isEmpty()) ? fields[2] : "";
      transaction.dataString =
        (fields.length > 3 && !fields[3].isEmpty()) ? fields[3] : "";
      transaction.numeroDocumento =
        (fields.length > 4 && !fields[4].isEmpty()) ? fields[4] : "";
      transaction.historico =
        (fields.length > 5 && !fields[5].isEmpty()) ? Util.removeGarbage(fields[5]) : "metodo pgto. desconhecido";
      transaction.valor =
        (fields.length > 6 && !fields[6].isEmpty()) ? Float.parseFloat(Util.removeComma(fields[6])) : -1;

      transaction.data = Util.stringToDate(transaction.dataString);
      transaction.dataNum = Util.dateToLong(transaction.data);

      Util.fix(transaction);

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
      for (int i = 0; i < transactions.size(); i++) {
        Invoice transaction = transactions.get(i);
        String negativeValue = Util.removeNegative(transaction.valor) == 0
          ? ""
          : Util.replaceDotToComma(Util.removeNegative(transaction.valor));
        String positiveValue = Util.removePositive(transaction.valor) == 0 ? "" : Util.replaceDotToComma((Util.removePositive(transaction.valor) * -1));

          buffer.write(
            transaction.id +            
            ";" +
            transaction.dataNum +
            ";" +
            ";" +
            ";" +
            ";" +
            transaction.historico +
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

      buffer.close();
      file.close();
    } catch (IOException e) {
      System.out.println("ERRO NA ESCRITA DO ARQUIVO!");
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    
    String chosenFilePath = "";
    JFileChooser chooser = new JFileChooser(
      System.getProperty("user.home") + "/Downloads"
    );
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos CSV e TXT", "csv", "txt");
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
      Scanner scanner = new Scanner(System.in);
      List<Invoice> allTransactions = new ArrayList<>();

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
      // System.out.println(
      //   "Digite o nome do arquivo de saída (com ou sem extensao):"
      // );
      // String fileName = scanner.next();
      String fileName = "arquivoSaida";
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