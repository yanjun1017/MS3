import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;


public class reader {

    public static void main(String[] args) {

        String csvFile = "./source/ms3Interview.csv";
        BufferedReader buffer = null;
        String line = "";
        try {
            buffer = new BufferedReader(new FileReader(csvFile));
            while ((line = buffer.readLine()) != null) {

                // System.out.println(line);
                // -1 : will deal with the empty input
                String[] datas = line.split(",", -1);
                String lastname = datas[0];
                String firstname = datas[1];
                String email = datas[2];
                String gender = datas[3];
                String data = datas[4];
                String bankcard = datas[5];
                String price = datas[6];
                String H = datas[7];
                String I = datas[8];
                String J = datas[9];

                // System.out.println(lastname + " " + firstname + "\n");

            }
        // Exception Catch
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
}