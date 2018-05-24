package connessioni;

import java.sql.*;

public class ExporttoCSV {
    String filename = "d:/outfile.csv";
    String tablename = "abc";
    String query = "SELECT * INTO OUTFILE \"" + filename + "\" FROM " + tablename;

    public void scritturaFileCsv() {
        try {
            Connection con = Database.getConn();
            Statement stm = con.createStatement();
            stm.executeUpdate(query);

            stm.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
