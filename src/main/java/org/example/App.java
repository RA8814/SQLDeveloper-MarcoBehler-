package org.example;

import com.mysql.cj.protocol.Resultset;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
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
        try(Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost/test?serverTimezone=UTC", "root", "toor")) {
            // "0" means disabling the timeout, when doing isValid checks
            boolean isValid = con.isValid(0);
            System.out.println("Do we have a valid db connection? = " + isValid);

            // Statement object used to generate and perform statements with the statement.execute("INSERT ...") fucntion
            Statement statementGenerator = con.createStatement();

            // Any info about
            DatabaseMetaData dbmd = con.getMetaData();
            ResultSet studentTables = dbmd.getTables(null,null,"student", null);

            // If the student table exists!
            if (studentTables.next()){
                // Do noting.
            }
            else {
                statementGenerator.execute("create table student ( stud_id integer, stud_name varchar(20) )");
                statementGenerator.execute("insert into student values(001,'Alex')");
                statementGenerator.execute("insert into student values(002,'Robert')");
                statementGenerator.execute("insert into student values(003,'Petar')");
                statementGenerator.execute("insert into student values(004,'Robert')");
            }

            ResultSet resultSet = statementGenerator.executeQuery("select * from student");
            if (resultSet != null) { // if rs == null, then there is no record in ResultSet to show
                System.out.println("________________________________________");
                while (resultSet.next()) // By this line we will step through our data row-by-row
                {
                    System.out.println("Id of the student: " + resultSet.getString(1));
                    System.out.println("Name of student: " + resultSet.getString(2));
                    System.out.println("________________________________________");
                }
            }

            statementGenerator.close(); // close the Statement to let the database know we're done with it
//            con.close(); // close the Connection to let the database know we're done with it, but including the driverManager within the try() does this automatically


//-------------------------------------- More Proper Notation: ---------------------------------------------------------

            // Shorthand for "introduce local variable" is Ctrl + Alt + V
            PreparedStatement selectStatement = con.prepareStatement("SELECT * FROM STUDENT WHERE stud_name = ?");
            selectStatement.setString(1,"Robert");
            // this will return a ResultSet of all users with said name
            ResultSet selectResult = selectStatement.executeQuery();

            while (selectResult.next()){
                System.out.println("Found Student: " + selectResult.getString("stud_name")
                        + "with id: " + selectResult.getString("stud_id"));
            }

            PreparedStatement updateStatement = con.prepareStatement("UPDATE STUDENT " +
                    "SET stud_name = 'NotPetar' WHERE stud_id = ?");
            updateStatement.setString(1, "Petar");
            int updatedRows = updateStatement.executeUpdate();  //executeUpdate() returns the number of updated rows, and is used for all statements that "change" the database
            System.out.println("Rows updated: " + updatedRows);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DataSource dataSource = createDataSource();
        try (Connection hCon = dataSource.getConnection()) {
            boolean isValid = hCon.isValid(0);
            System.out.println("Do we have a valid HikariDB pool connection? = " + isValid);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static DataSource createDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl("jdbc:mysql://localhost/test?serverTimezone=UTC");
        hikariDataSource.setUsername("root");
        hikariDataSource.setPassword("toor");
        return hikariDataSource;
    }
}
