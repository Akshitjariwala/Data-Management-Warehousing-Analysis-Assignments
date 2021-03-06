package Assignment2.Controller;

import Assignment2.TransactionService.TransactionService;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/*References*/
/* [1] "Java Multithreading", javatpoint.com [online]                           */
/*      Available : https://www.javatpoint.com/multitasking-in-multithreading   */
/* [2] "Reetrant lock in Java", geeksforgeeks.org [online]                      */
/*      Available : https://www.geeksforgeeks.org/reentrant-lock-java/          */



public class TransactionController {

    public static ArrayList<String> logs = new ArrayList<>();

    public static void main(String args[]) throws Exception {
        transaction1 t1 = new transaction1(logs);
        transaction2 t2 = new transaction2(logs);
        transaction3 t3 = new transaction3(logs);

        TransactionService service = new TransactionService();

        t1.start();
        t2.start();
        t3.start();

        t1.setPriority(Thread.MAX_PRIORITY);

        t1.join();
        t2.join();
        t3.join();

        service.printLogs(logs);
    }
}

class transaction1 extends Thread {

    ArrayList<String> logs;

    public transaction1(ArrayList<String> logs){
        this.logs = logs;
    }

    TransactionService service = new TransactionService();
    Connection connection;
    private Lock lockDatabase = new ReentrantLock();

    public void run() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://34.93.187.18:3306/Data5408","admin","Aksh@1234");
            ResultSet result = service.readMethod(connection);
            String id = service.getTransactionId(connection);
            logs.add("T1:"+id+" SELECT\tcustomers\tzip_code\t1151\t\t\t"+service.getCurrentDateAndTime());
            Thread.sleep(5000);
            String afterValue = service.updateMethod(connection,"T1 City");
            logs.add("T1:"+id+" Update\tcustomers\tcustomer_city\t"+service.getBeforeValue()+"\t    "+afterValue+"\t"+service.getCurrentDateAndTime());
            Thread.sleep(10000);
            service.getCommit(connection);
            logs.add("T1:"+id+" Changes Committed.\t\t\t\t\t\t\t"+service.getCurrentDateAndTime());
            connection.close();
        } catch (SQLException | InterruptedException throwables) {
            throwables.printStackTrace();
        }
    }
}

class transaction2 extends Thread {

    ArrayList<String> logs;

    public transaction2(ArrayList<String> logs){
        this.logs = logs;
    }

    TransactionService service = new TransactionService();
    Connection connection;
    private Lock lockDatabase = new ReentrantLock();

    public void run() {
       try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://34.93.187.18:3306/Data5408","admin","Aksh@1234");
            ResultSet result = service.readMethod(connection);
            String id = service.getTransactionId(connection);
            logs.add("T2:"+id+" SELECT\tcustomers\tzip_code\t1151\t\t\t"+service.getCurrentDateAndTime());
            Thread.sleep(5000);
            Thread.sleep(5000);
            String afterValue = service.updateMethod(connection, "T2 City");
            logs.add("T2:"+id+" Update\tcustomers\tcustomer_city\t"+service.getBeforeValue()+"\t    "+afterValue+"\t"+service.getCurrentDateAndTime());
            Thread.sleep(10000); //10000
            service.getCommit(connection);
            logs.add("T2:"+id+" Changes Committed.\t\t\t\t\t\t\t"+service.getCurrentDateAndTime());
            connection.close();
        } catch (SQLException | InterruptedException throwables) {
            throwables.printStackTrace();
        }
    }
}

class transaction3 extends Thread {

    ArrayList<String> logs;

    public transaction3(ArrayList<String> logs){
        this.logs = logs;
    }

    TransactionService service = new TransactionService();
    Connection connection;
    private Lock lockDatabase = new ReentrantLock();

    public void run() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://34.93.187.18:3306/Data5408","admin","Aksh@1234");
            Thread.sleep(5000);
            ResultSet result = service.readMethod(connection);
            String id = service.getTransactionId(connection);
            logs.add("T3:"+id+" SELECT\tcustomers\tzip_code\t1151\t\t\t"+service.getCurrentDateAndTime());
            Thread.sleep(5000);
            String afterValue = service.updateMethod(connection,"T3 City");
            logs.add("T3:"+id+" Update\tcustomers\tcustomer_city\t"+service.getBeforeValue()+"\t    "+afterValue+"\t"+service.getCurrentDateAndTime());
            Thread.sleep(5000);
            service.getCommit(connection);
            logs.add("T3:"+id+" Changes Committed.\t\t\t\t\t\t\t"+service.getCurrentDateAndTime());
            connection.close();
        }catch (SQLException | InterruptedException throwables) {
            throwables.printStackTrace();
        }
    }
}
