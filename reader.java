import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.Level;


public class reader {

    public static void createDatabase(String fileName){


        String url = "jdbc:sqlite:./sqlite"+fileName;
        Connection conn = null;

        try {

            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);

            if (conn != null){

                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        } catch(SQLException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        } 
    }

    public static void main(String[] args) {

        String csvFile = "./source/ms3Interview.csv";
        BufferedReader buffer = null;
        String line = "";
        int recordCount = -1;
        int sucessCount = -1;
        int failCount = 0;
        
        try(PrintWriter pw = new PrintWriter(new File("./source/ms3Interview-bad.csv"))){

            Logger logger = Logger.getLogger("./source/ms3Interview.log");
            FileHandler fh = new FileHandler("./source/ms3Interview.log", true);
            logger.addHandler(fh);

            buffer = new BufferedReader(new FileReader(csvFile));

            while ((line = buffer.readLine()) != null) {

                recordCount += 1;
                String[] datas = line.split(",");

                if (datas.length > 9 && datas[9] != null){

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
                    sucessCount += 1;
                }
                else{

                    pw.println(line);
                    failCount += 1;
                }
            }
            if (logger.isLoggable(Level.INFO)){
                
                logger.info(recordCount + " of records received\n");
                logger.info(sucessCount + " of records successful\n");
                logger.info(failCount + " of records failed\n");
            }

        
            pw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
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