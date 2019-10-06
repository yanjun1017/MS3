import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.Level;


public class Reader {

    private static final String url = "jdbc:sqlite:./sqlite/ms3Interview.db";

    public static void createDatabase(){

        try {

            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);
            if (conn != null){
                DatabaseMetaData meta = conn.getMetaData();
            }
        } catch(SQLException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void createTable(){


        String sql = "CREATE TABLE IF NOT EXISTS ms3InterviewDB (\n"
                + "  A  String,\n"
                + "  B  String,\n"
                + "  C  String,\n"
                + "  D  String,\n"
                + "  E  String,\n"
                + "  F  String,\n"
                + "  G  String,\n"
                + "  H  String,\n"
                + "  I  String,\n"
                + "  J  String\n"
                + ");";
        Connection conn = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);

            Statement stmt = conn.createStatement();
            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static Connection connect() {

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void insert(String A, String B, String C, String D, String E, String F, String G, String H, String I, String J) {

        String sql = "INSERT INTO ms3InterviewDB(A, B, C, D, E, F, G, H, I, J) VALUES(?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, A);
            pstmt.setString(2, B);
            pstmt.setString(3, C);
            pstmt.setString(4, D);
            pstmt.setString(5, E);
            pstmt.setString(6, F);
            pstmt.setString(7, G);
            pstmt.setString(8, H);
            pstmt.setString(9, I);
            pstmt.setString(10, J);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        String csvFile = "./source/ms3Interview.csv";
        BufferedReader buffer = null;
        String line = "";
        // set recoundCount to -1 in order to eliminate the header line
        int recordCount = -1;
        int sucessCount = 0;
        int failCount = 0;

        createDatabase();
        createTable();

        try(PrintWriter pw = new PrintWriter(new File("./source/ms3Interview-bad.csv"))){

            // logger to write to the log file
            Logger logger = Logger.getLogger("./source/ms3Interview.log");
            FileHandler fh = new FileHandler("./source/ms3Interview.log", true);
            logger.addHandler(fh);

            buffer = new BufferedReader(new FileReader(csvFile));

            // buffer reads each data entry line by line
            while ((line = buffer.readLine()) != null) {

                if (recordCount == -1){
                    recordCount += 1;
                }
                else {
                    recordCount += 1;
                    String[] datas = line.split(",");

                    // the data entry is consider as successful if and only if
                    // each data entry includes at least 9 data sets
                    // and datas[9] should be accessible
                    if (datas.length > 9 && datas[9] != null) {


                        String A = datas[0];
                        String B = datas[1];
                        String C = datas[2];
                        String D = datas[3];
                        String E = datas[4];
                        String F = datas[5];
                        String G = datas[6];
                        String H = datas[7];
                        String I = datas[8];
                        String J = datas[9];

//                        String sql = "INSERT INTO ms3InterviewDB(A, B, C, D, E, F, G, H, I, J) VALUES(" + A + "," + B + ",'" + C + "'," + D + "," + E + "," + F + ",'" + G + "'," + H + "," + I + "," + J + ")";
//                        try (Connection conn = connect()){
//                             Statement stmt = conn.createStatement();
//                             stmt.execute(sql);
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
                        insert(A, B, C, D, E, F, G, H, I, J);
//                        String sql = "INSERT INTO ms3InterviewDB(A, B, C, D, E, F, G, H, I, J) VALUES(?,?,?,?,?,?,?,?,?,?)";
//                        try (Connection conn = this.connect();
//                             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//                            pstmt.setString(1, name);
//                            pstmt.setDouble(2, capacity);
//                            pstmt.executeUpdate();
//                        } catch (SQLException e) {
//                            System.out.println(e.getMessage());
//                        }
                        sucessCount += 1;
                    }
                    // bad data entry should write to the bad csv file
                    else {

                        pw.println(line);
                        failCount += 1;
                    }
                }
            }
            if (logger.isLoggable(Level.INFO)){
                // input the log message
                logger.info(recordCount + " of records received\n");
                logger.info(sucessCount + " of records successful\n");
                logger.info(failCount + " of records failed\n");
            }

            // Close the printwriter after finished writing the csv file
            pw.close();

            // Exceptions
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