import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

public class TestFileCreator {

    public static void main(String[] args) {
        File mypath = new File("./Testfiles");
        if (!mypath.exists())
            mypath.mkdir();
        try {
            String cmdStr = "base64 /dev/urandom | head -c 10000000 > ./Testfiles/Testfile00.txt";
            String[] cmd = new String[]{"/bin/bash", "-c", cmdStr};
            Runtime.getRuntime().exec(cmd);
            System.out.println("Success");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error");
        }
        File file = new File("./Testfiles/Testfile00.txt");
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
        try {
            String txt = new String(filecontent, "utf-8");
            int num = 1;
            int k = 0;
            while (num < 50) {
                String index = String.format("%2d", num).replace(" ","0");
                PrintWriter writer = new PrintWriter("./Testfiles/Testfile" + index + ".txt");
                StringBuilder str = new StringBuilder(txt);
                while (k < 5){
                    int randomnum = ThreadLocalRandom.current().nextInt(0,81919);
                    str.setCharAt(randomnum,'l');
                    k += 1;
                }
                writer.write(str.toString());
                writer.close();
                num += 1;
                }
            }catch (FileNotFoundException | UnsupportedEncodingException f) {
            f.printStackTrace();
        }
    }
}
