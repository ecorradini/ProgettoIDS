package connessioni;

import java.sql.*;

public class ExporttoCSV {
    private String filename = "d:/outfile.csv";
    private String tablename = "abc";
    private String query = "SELECT * INTO OUTFILE \'" + filename + "\' FROM " + tablename;

    public ExporttoCSV (String filename, String tablename)
    {
        this.filename = filename;
        this.tablename = tablename;
        this.query = "SELECT * INTO OUTFILE \'" + filename + "\' FIELDS TERMINATED BY \',\' LINES TERMINATED BY \'\\n\' FROM " + tablename;
    }

    public String getFilename() {
        return filename;
    }

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
