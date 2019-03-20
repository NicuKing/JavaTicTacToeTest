package sample;

import com.sun.rowset.CachedRowSetImpl;

import javax.swing.plaf.nimbus.State;
import java.sql.*;

public class Controller {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static  Connection connect = null;
    private static final String connString = "jdbc:mysql://localhost/test";

    public Controller() throws SQLException, ClassNotFoundException {

    }

    public static void dbConnect() throws SQLException, ClassNotFoundException {
        try {
            Class.forName(JDBC_DRIVER);
        }
        catch (ClassNotFoundException e){
            System.out.println("SQL Driver is missing");
            e.printStackTrace();
            throw e;
        }

        System.out.println("SQL Driver has been registered");

        try{
            connect = DriverManager.getConnection(connString, "root", "");
            System.out.println("Connection Successful!");
        } catch (SQLException e) {
            System.out.println("Connection failed! Check output console: "+e);
            throw e;
        }
    }

    public static void dbDisconnect() throws SQLException {
        try{
            if(connect != null && !connect.isClosed()){
                connect.close();
            }
        } catch (Exception e){
            throw e;
        }
    }

    //Method for excecuting Operations on DB
    public static void dbExcecuteQuery(String sqlStmt) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        try {
            dbConnect();
            stmt = connect.createStatement();
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e){
            System.out.println("Problem at dbExcecuteQuery operation"+e);
            throw e;
        } finally {
            if(stmt!=null){
                stmt.close();
            }
            dbDisconnect();
        }
    }

    public static ResultSet dbExcecute(String sqlQuery) throws ClassNotFoundException, SQLException{
        Statement stmt = null;
        ResultSet rs = null;
        CachedRowSetImpl crs = null;

        try{
            dbConnect();
            stmt = connect.createStatement();
            rs = stmt.executeQuery(sqlQuery);
            crs = new CachedRowSetImpl();
            crs.populate(rs);
        } catch (SQLException e){
            System.out.println("Error in dbExceute operation: "+e);
            throw e;
        }
        finally{
            if(rs != null){
                rs.close();
            }
            if(stmt != null){
                stmt.close();
            }
            dbDisconnect();
        }
        return crs;
    }
}
