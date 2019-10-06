import java.io.*;
import java.sql.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.Level;


public class Reader {

    private static final String url = "jdbc:sqlite:./sqlite/ms3Interview.db";

    public static void main(String[] args) {

        String csvFile = "./source/ms3Interview.csv";
        BufferedReader buffer = null;
        String line = "";
        // set recoundCount to -1 in order to eliminate the header line
        int recordCount = -1;
        int successCount = 0;
        int failCount = 0;

        createDatabase();
        createTable();

        try(PrintWriter pw = new PrintWriter(new File("./source/ms3Interview-bad.csv"))){

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
                    if ((datas.length == 10) && datas[9] != null){

                        insert(datas[0], datas[1], datas[2], datas[3], datas[4], datas[5], datas[6], datas[7], datas[8], datas[9]);
                        successCount += 1;
                    }
                    // bad data entries write to the bad csv file
                    else {

                        pw.println(line);
                        failCount += 1;
                    }
                }
            }

            // Write the log file
            log(recordCount, successCount, failCount);
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

    public static void createDatabase(){

        try {

            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);
            if (conn != null){
                DatabaseMetaData meta = conn.getMetaData();
            }
        } catch (SQLException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void createTable(){


        String sql = "CREATE TABLE IF NOT EXISTS ms3InterviewTB (\n"
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
        } catch (ClassNotFoundException e) {
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

        String sql = "INSERT INTO ms3InterviewTB(A, B, C, D, E, F, G, H, I, J) VALUES(?,?,?,?,?,?,?,?,?,?)";

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

    public static void log(int recordCount, int successCount, int failCount){

        // logger to write to the log file
        try{
            Logger logger = Logger.getLogger("./source/ms3Interview.log");
            FileHandler fh = new FileHandler("./source/ms3Interview.log", true);
            logger.addHandler(fh);

            if (logger.isLoggable(Level.INFO)){
                // input the log message
                logger.info(recordCount + " of records received\n");
                logger.info(successCount + " of records successful\n");
                logger.info(failCount + " of records failed\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}