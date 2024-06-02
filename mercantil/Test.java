import java.awt.*;

public class Test {

  public static void main(String[] args) {
    FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
    dialog.setMode(FileDialog.LOAD);
    dialog.setVisible(true);
    String file = dialog.getFile();
    dialog.dispose();
    System.out.println(file + " chosen.");
  }
}
