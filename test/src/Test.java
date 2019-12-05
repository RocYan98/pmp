import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Test {

    int method() {
        return n;
    }

    int m = method();
    int n = 1;


    public static void main(String[] args) throws Exception {
        PrintWriter pw = null;
        pw = new PrintWriter(new FileOutputStream("out.txt"));

        for (int count = 1; count <= 10; count++)

            pw.println(count);

        pw.close();

        pw = new PrintWriter(new FileOutputStream("out.txt", true));

        for (int count = 1; count <= 10; count++)

            pw.println(count);

        pw.close();
    }
}
