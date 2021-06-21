package Assignment2.TransactionService;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TransactionService  {

    private static String beforeCity;

    public ResultSet readMethod(Connection connection) throws SQLException {
        String SQL_SELECT;
        Statement statement = connection.createStatement();
        connection.setAutoCommit(false);
        SQL_SELECT = "SELECT customer_id,customer_city FROM customers WHERE zip_code = 1151";
        ResultSet result =  statement.executeQuery(SQL_SELECT);
        while(result.next())
        {
            beforeCity = result.getString("customer_city");
        }
        return result;
    }
    public String updateMethod(Connection connection, String city) throws SQLException {
        String SQL_UPDATE,SQL_SELECT;
        String AfterCity = null;
        Statement statement = connection.createStatement();
        /*connection.setAutoCommit(true);*/
        SQL_UPDATE = "UPDATE customers SET customer_city = '"+city+"' WHERE zip_code = 1151";
        statement.executeUpdate(SQL_UPDATE);
        SQL_SELECT = "SELECT customer_city FROM customers WHERE zip_code = 1151";
        ResultSet result = statement.executeQuery(SQL_SELECT);

        while(result.next())
        {
            AfterCity = result.getString("customer_city");
        }

        return AfterCity;

    }

    public String getBeforeValue()
    {
        return beforeCity;
    }

    public void printLogs(ArrayList<String> logs) throws IOException {
        File file = new File("D:/Materiel/Database Analytics/AkshitJariwala_B00866255_Assignment2/A2/A2/TransactionManagement/transactionLogs.txt");

        FileWriter fo = new FileWriter(file,true);
        PrintWriter pw = new PrintWriter(fo);
        System.out.println("Log creation started.");

        pw.println("Transaction ID     Operation    Table       Attribute       Before      After       Timestamp");
        pw.println("-------------------------------------------------------------------------------------------------------");

        for(String log:logs)
        {
            pw.println(log);
        }

        pw.println("-------------------------------------------------------------------------------------------------------");
        pw.close();
        fo.close();
    }

    public String getCurrentDateAndTime()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();

        return formatter.format(date);
    }

    public String getTransactionId(Connection connection) throws SQLException {
            String SQL = "SELECT tx.trx_id FROM information_schema.innodb_trx tx WHERE tx.trx_mysql_thread_id = connection_id()";
            Statement statement = connection.createStatement();
            ResultSet result =  statement.executeQuery(SQL);
            String id = null;
            while(result.next())
            {
                id = String.valueOf(result.getString("trx_id"));
            }
            return id;
    }
}
