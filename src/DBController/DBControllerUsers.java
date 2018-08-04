package DBController;

import java.sql.DriverManager;
import java.sql.SQLException;

import CarPark.*;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBControllerUsers extends DBControllerMain {

    public static boolean addUser (String name, String id, String password, String email) {
        Connection conn = connectToDb();
        Statement stmt;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

            stmt.executeUpdate("INSERT INTO Users VALUES ('" + name + "' , '" + id +  "' , '" + password + "' , '" + email + "' , '"+UserType.type.CUSTOMER+"')");

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {}
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean verUser (String _email, String password) {
        Connection conn= connectToDb();
        Statement stmt;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT * FROM Users WHERE (email = '" + _email + "') AND (password = " + password + ")");

            if(!rs.next()) return false;
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
        } catch (SQLException e) {e.printStackTrace();}
        return true;
    }

    public static String getUserName (String _email) {
        Connection conn= connectToDb();
        Statement stmt;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT name FROM Users WHERE email = '" + _email + "'");
            if(!rs.next()) return "User Name not retrieved from db";
            String uName=rs.getString(1);

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
            return uName;
        } catch (SQLException e) {e.printStackTrace();}
        return "User Name not retrieved from db";
    }

    public static String getUserType (String _email) {
        Connection conn= connectToDb();
        Statement stmt;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT type FROM Users WHERE email = '" + _email + "'");
            if(!rs.next()) return "User Type not retrieved from db";
            String userType =rs.getString(1);

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
            return userType;
        } catch (SQLException e) {e.printStackTrace();}
        return "User Type not retrieved from db";
    }

    public static boolean removeUser (String id) {
        Connection conn= connectToDb();
        Statement stmt;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT id FROM Users WHERE id = " + id);
            if(!rs.next()) return false;

            stmt.executeUpdate("DELETE FROM Users WHERE id = " + id);

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
        } catch (SQLException e) {e.printStackTrace();}
        return true;
    }

    public static boolean addCar(String carId, String userEmail){
        Connection conn = connectToDb();
        Statement stmt;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

            stmt.executeUpdate("INSERT INTO cars VALUES ('" + carId + "' , '" + userEmail +  "')");

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {}
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getAllUsers() {
        Connection con=connectToDb();
        Statement stmt;
        String output="";
        try
        {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Users;");
            while(rs.next())
            {
                output+=rs.getString(1)+"  " +rs.getInt(2) + "\n";
            }

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
        } catch (SQLException e) {e.printStackTrace();}
        return output;
    }

    public static String getCars(String userEmail){
        Connection con=connectToDb();
        Statement stmt;
        String output="";
        try
        {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id FROM Cars WHERE user_email = '" + userEmail + "'");
            while(rs.next())
            {
                output+=rs.getInt(1)+",";
            }

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
        } catch (SQLException e) {e.printStackTrace();}
        return output;
    }

    public static boolean addFullmembership(String userEmail, String carId){
        Connection conn = connectToDb();
        Statement stmt;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

            java.util.Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            stmt.executeUpdate("INSERT INTO Fullmembership VALUES (NULL, '" + carId + "' , '" + userEmail + "' , '" + format.format(now) +  "')");

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {}
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getFullmembership (String userEmail) {
        Connection conn= connectToDb();
        Statement stmt;
        String fullMemberships="";
        try
        {


            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT id,car_id,start_date FROM Fullmembership WHERE user_email = '" + userEmail + "'");

            while(rs.next())
            {
                fullMemberships+=rs.getInt(1)+","+rs.getInt(2)+","+rs.getString(3)+";";
            }


            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fullMemberships;
    }

    public static boolean addRegmembership(String carId, String userEmail, String carpark, String end_time){
        Connection conn = connectToDb();
        Statement stmt;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

            java.util.Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            stmt.executeUpdate("INSERT INTO Regmembership VALUES (NULL, '" + carId + "' , '" + userEmail + "' , '" + format.format(now) + "' , '" + carpark + "' , '" + end_time +  "')");

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {}
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public static String getRegmembership (String userEmail) {
        Connection conn= connectToDb();
        Statement stmt;
        String regMemberships="";
        try
        {


            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT id,car_id,start_date,carpark,end_time  FROM Regmembership WHERE user_email = '" + userEmail + "'");

            while(rs.next())
            {
                regMemberships+=rs.getInt(1)+","+rs.getInt(2)+","+rs.getString(3)+","+rs.getString(4)+","+rs.getString(5)+";";
            }


            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return regMemberships;
    }

    public static boolean addComplaint(String userEmail, String complaint, String parkName){
        Connection conn = connectToDb();
        Statement stmt;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

            Date now = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            stmt.executeUpdate("INSERT INTO complaints VALUES (NULL , '" + userEmail + "' , '" + dateFormat.format(now) + "' , NULL , NULL , '" +complaint + "' ,  'RECIEVED' ,'"+parkName+"')");

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {}
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public static String getComplaints () {
        Connection conn= connectToDb();
        Statement stmt;
        String complaints="";
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT id,user_email,free_text,status FROM Complaints");

            while(rs.next())
            {
                complaints+=rs.getInt(1)+","+rs.getString(2)+","+rs.getString(3)+","+rs.getString(4)+";";
            }


            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return complaints;
    }

    public static boolean closeComplaint(int complaintId){
        Connection conn= connectToDb();
        Statement stmt;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

            stmt.executeUpdate("UPDATE  complaints SET status ='CLOSED' WHERE id = " + complaintId);

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
        } catch (SQLException e) {e.printStackTrace();}
        return true;
    }

    public static int isaMember(String carId, String parkId){
        Connection conn= connectToDb();
        Statement stmt;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT * FROM fullmembership WHERE car_id = '" + carId + "'");
            if(rs.next()) return 2;

            rs = stmt.executeQuery("SELECT * FROM regmembership WHERE car_id = '" + carId + "' AND carpark = '" + parkId + "'");
            if(rs.next()) return 1;

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static void main(String[] args) {
//		try {
//			addCarPark("Park1", 3, 3, 3);
//		} catch (Exception e){}
//		addDataToDb();
//		prioritize();

//		String input = "1111,k@b,2018/01/12 15:00:00,2018/01/12 17:00:00,1";
//		String[] parameters = input.split(",");
//		addOrder(parameters);
//        addComplaint("k@b","new complaint");
//        System.out.println(getComplaints());
//closeComplaint(1);
//        addRegmembership("5","k@b","1","18:00");

//        System.out.println(isaMember("1","3"));
//        System.out.println(isaMember("1","2"));
//        System.out.println(isaMember("5","2"));
        addComplaint("e","aaaa","park1");
    }

}
