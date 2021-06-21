package Assignment2.TransactionService;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*References*/
/* [1] "Java Multithreading", javatpoint.com [online]                           */
/*      Available : https://www.javatpoint.com/multitasking-in-multithreading   */
/* [2] "Reetrant lock in Java", geeksforgeeks.org [online]                      */
/*      Available : https://www.geeksforgeeks.org/reentrant-lock-java/          */

public class TransactionService  {

    private static String beforeCity;

    static Lock lockDatabase = new ReentrantLock();

    public ResultSet readMethod(Connection connection) throws SQLException {
        String SQL_SELECT;
        Statement statement = connection.createStatement();
        connection.setAutoCommit(false);
        setExclusiveLock(lockDatabase);
        ResultSet result =  null;
        try
        {
            SQL_SELECT = "SELECT customer_id,customer_city FROM customers WHERE zip_code = 1151";
            result =  statement.executeQuery(SQL_SELECT);
            while(result.next())
            {
                beforeCity = result.getString("customer_city");
            }
        } catch(Exception e)
        {
            System.out.println(e);
        }
        return result;
    }

    public String updateMethod(Connection connection, String city) throws SQLException {
        String SQL_UPDATE,SQL_SELECT;
        String AfterCity = null;
        try {
            Statement statement = connection.createStatement();
            SQL_UPDATE = "UPDATE customers SET customer_city = '" + city + "' WHERE zip_code = 1151";
            statement.executeUpdate(SQL_UPDATE);
            SQL_SELECT = "SELECT customer_city FROM customers WHERE zip_code = 1151";
            ResultSet result = statement.executeQuery(SQL_SELECT);

            while (result.next()) {
                AfterCity = result.getString("customer_city");
            }
            return AfterCity;
        }catch(Exception e)
        {
            System.out.println(e);
        }
        return AfterCity;
    }

    public void setExclusiveLock(Lock lockDatabase)
    {
        lockDatabase.lock();
    }

    public void unlockExclusiveLock(Lock lockDatabase)
    {
        lockDatabase.unlock();
    }

    public void getCommit(Connection connection) throws SQLException {
       try
       {
           connection.commit();
       }
       catch(Exception e)
       {
           System.out.println(e);
       }
       finally {
           unlockExclusiveLock(lockDatabase);
       }
    }

    public void printLogs(ArrayList<String> logs) throws IOException {
        File file = new File("D:/Materiel/Database Analytics/AkshitJariwala_B00866255_Assignment2/A2/A2/TransactionManagement/transactionLogs.txt");

        FileWriter fo = new FileWriter(file,true);
        PrintWriter pw = new PrintWriter(fo);

        pw.println("Transaction ID     Operation    Table           Attribute       Before      After       Timestamp");
        pw.println("-------------------------------------------------------------------------------------------------------");

        for(String log:logs)
        {
            pw.println(log);
        }

        pw.println("-------------------------------------------------------------------------------------------------------");
        pw.close();
        fo.close();

        System.out.println("Log creation completed.");
    }

    public String getCurrentDateAndTime()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        java.util.Date date = new java.util.Date();

        return formatter.format(date);
    }

    public String getBeforeValue()
    {
        return beforeCity;
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
