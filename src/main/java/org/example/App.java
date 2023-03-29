package org.example;

import com.mysql.cj.protocol.Resultset;

import java.sql.*;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        System.out.println( "SQLDriver!" );
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost/test?serverTimezone=UTC", "root", "toor")) {
            boolean isValid = con.isValid(0);

            // "0" means disabling the timeout, when doing isValid checks
            System.out.println("Do we have a valid db connection? = " + isValid);
            Statement s = con.createStatement();

            // Any info about
            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet studentTables = dbmd.getTables(null,null,"student", null);

            // If the student table exists!
            if (studentTables.next()){

            }
            else {
                s.execute("create table student ( stud_id integer, stud_name varchar(20) )");
                s.execute("insert into student values(001,'Alex')");
                s.execute("insert into student values(002,'Robert')");
                s.execute("insert into student values(003,'Petar')");
            }

            ResultSet resultSet = s.executeQuery("select * from student");
            if (resultSet != null) { // if rs == null, then there is no record in ResultSet to show
                System.out.println("________________________________________");
                while (resultSet.next()) // By this line we will step through our data row-by-row
                {
                    System.out.println("Id of the student: " + resultSet.getString(1));
                    System.out.println("Name of student: " + resultSet.getString(2));
                    System.out.println("________________________________________");
                }
            }

            s.close(); // close the Statement to let the database know we're done with it
//            con.close(); // close the Connection to let the database know we're done with it, but including the driverManager within the try() does this automatically



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
