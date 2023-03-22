package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
