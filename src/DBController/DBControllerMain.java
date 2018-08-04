package DBController;

import CarPark.*;
import CarPark.ParkingSpot;
import com.sun.org.apache.regexp.internal.RE;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.HOUR;

public class DBControllerMain {
	
	public static Connection connectToDb() {
		try 
		{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {/* handle the error*/}
     
		Connection con=null;
		
        try 
        {
        	con = DriverManager.getConnection("jdbc:mysql://localhost/cpsDb","student","student");
    		   		
     	} catch (SQLException ex) 
     	    {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            }

    	return con;
	}

	public static void buildDb() throws SQLException {
		Connection con=connectToDb();
		Statement stmt;
		try {
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Users(name VARCHAR(40) , id int, " +
					"password int, email VARCHAR(40) PRIMARY KEY, type VARCHAR(40));");

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Cars(id int PRIMARY KEY, " +
					"user_email VARCHAR(40));");

//			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS CarParks(name VARCHAR(40) PRIMARY KEY , height int , " +
//					"width int , depth int , free_space int);");

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS CarParks(name VARCHAR(40) PRIMARY KEY , height int , " +
					"width int , depth int , free_space int , fiscal_amount_corrupted_spaces int);");

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Orders(id int PRIMARY KEY AUTO_INCREMENT , car_id int , " +
					"user_email VARCHAR(40) , time_in TIMESTAMP , time_out TIMESTAMP ,park_id int , status VARCHAR(40),type VARCHAR(40) DEFAULT 'AHEAD');");

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Complaints(id int PRIMARY KEY AUTO_INCREMENT, user_email VARCHAR(40) , " +
					"open_time TIMESTAMP, close_time TIMESTAMP , refund int , free_text VARCHAR(40) , status VARCHAR(40), park_id VARCHAR(40));");

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Fullmembership(id int PRIMARY KEY AUTO_INCREMENT ,car_id int, " +
					"user_email VARCHAR(40), start_date TIMESTAMP);");

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Regmembership(id int PRIMARY KEY AUTO_INCREMENT ,car_id int, " +
					"user_email VARCHAR(40), start_date TIMESTAMP, carpark VARCHAR(40), end_time VARCHAR(40));");

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Prices(park_id int PRIMARY KEY, in_place int , in_advance int);");

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS PriceChanges(id int PRIMARY KEY AUTO_INCREMENT ,park_id int , in_place int , in_advance int, status VARCHAR(40));");

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS CpsScheduler(report VARCHAR(40) PRIMARY KEY, timeSampling TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Statistics(tab VARCHAR(40) ,param VARCHAR(40),method VARCHAR(40),result DOUBLE,iteration VARCHAR(40), " +
					"timeSampling TIMESTAMP DEFAULT CURRENT_TIMESTAMP , total_amount int , park_id VARCHAR(40));");

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS corrupted_spaces(time TIMESTAMP DEFAULT CURRENT_TIMESTAMP() ,park_id VARCHAR(40),position int);");


			stmt.executeUpdate("INSERT IGNORE INTO Users VALUES ('CPS Manager' , ' 0 ' , '0' , 'cpsm' , '"+UserType.type.CPS_MANAGER+"')");
			DBControllerCarPark.addCarPark("Park1",3,3,3);
			DBControllerCarPark.addCarPark("Park2",3,3,3);
			DBControllerCarPark.addCarPark("Park3",3,3,3);

			DBControllerMain.addWeeklySched(stmt);
		} catch (SQLException e) {
			e.printStackTrace();}
	}

	public static void clearTable(String table) {
		Connection conn= connectToDb();
		Statement stmt;
		try
		{
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			stmt.executeUpdate("DELETE FROM " + table);

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) { /* ignored */}
			}
		} catch (SQLException e) {e.printStackTrace();}

	}

	public static void addDataToDb(){

		DBControllerUsers.addUser("Kobi","12345","123","kobibarhanin@gmail.com");
		DBControllerUsers.addUser("Itay","54321","1","i");
		DBControllerUsers.addUser("Eran","12345","1","e");
		DBControllerUsers.addUser("Mor","12345","1","m");

		DBControllerUsers.addCar("1","kobibarhanin@gmail.com");
		DBControllerUsers.addCar("2","i");
		DBControllerUsers.addCar("3","e");
		DBControllerUsers.addCar("4","m");
		DBControllerUsers.addCar("5","kobibarhanin@gmail.com");
		DBControllerUsers.addCar("6","i");
		DBControllerUsers.addCar("7","e");
		DBControllerUsers.addCar("8","m");
		DBControllerUsers.addCar("9","kobibarhanin@gmail.com");

		DBControllerUsers.addRegmembership("1","kobibarhanin@gmail.com","2","00:00:00");
		DBControllerUsers.addRegmembership("2","i","2","00:00:00");
		DBControllerUsers.addRegmembership("3","e","2","00:00:00");
		DBControllerUsers.addRegmembership("4","m","2","00:00:00");
		DBControllerUsers.addRegmembership("9","kobibarhanin@gmail.com","2","00:00:00");
		DBControllerUsers.addRegmembership("6","i","2","00:00:00");
		DBControllerUsers.addRegmembership("7","e","2","00:00:00");
		DBControllerUsers.addRegmembership("8","m","2","00:00:00");

		DBControllerUsers.addFullmembership("kobibarhanin@gmail.com","5");
		DBControllerUsers.addFullmembership("i","6");
		DBControllerUsers.addFullmembership("e","7");
		DBControllerUsers.addFullmembership("m","8");


		java.util.Date now = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());

		cal.add(Calendar.HOUR_OF_DAY, 2);
		Date in2hours = cal.getTime();
		cal.add(Calendar.HOUR_OF_DAY, 2);
		Date in4hours = cal.getTime();
		cal.add(Calendar.HOUR_OF_DAY, 2);
		Date in6hours = cal.getTime();
		cal.add(Calendar.HOUR_OF_DAY, 2);
		Date in8hours = cal.getTime();

		DBControllerCarPark.addOrder(("1,k,"+dateFormat.format(now)+","+dateFormat.format(in2hours)+",1,AHEAD").split(","));
		DBControllerCarPark.addOrder(("1,k,"+dateFormat.format(in4hours)+","+dateFormat.format(in6hours)+",1,AHEAD").split(","));
		DBControllerCarPark.addOrder(("2,i,"+dateFormat.format(now)+","+dateFormat.format(in2hours)+",1,INPLACE").split(","));
		DBControllerCarPark.addOrder(("3,e,"+dateFormat.format(now)+","+dateFormat.format(in2hours)+",1,AHEAD").split(","));
		DBControllerCarPark.addOrder(("3,e,"+dateFormat.format(in4hours)+","+dateFormat.format(in6hours)+",1,AHEAD").split(","));
		DBControllerCarPark.addOrder(("4,m,"+dateFormat.format(now)+","+dateFormat.format(in4hours)+",1,INPLACE").split(","));
		DBControllerCarPark.addOrder(("4,m,"+dateFormat.format(in4hours)+","+dateFormat.format(in6hours)+",1,AHEAD").split(","));
		DBControllerCarPark.addOrder(("2,i,"+dateFormat.format(in6hours)+","+dateFormat.format(in8hours)+",1,AHEAD").split(","));

//		DBControllerCarPark.insertCarToParkOrder(1,1,1);
		DBControllerCarPark.insertCarToParkOrder(2,1,3);
		DBControllerCarPark.insertCarToParkOrder(3,1,4);
		DBControllerCarPark.insertCarToParkOrder(4,1,6);

		DBControllerCarPark.insertCarToParkMember(1,2,1,dateFormat.format(in6hours));
		DBControllerCarPark.insertCarToParkMember(2,2,2,dateFormat.format(in2hours));

		DBControllerCarPark.setPrices(1,5,3);
		DBControllerCarPark.setPrices(2,7,2);
		DBControllerCarPark.setPrices(3,3,3);

		DBControllerCarPark.UpdateCorrupted("park1", 13,true);
		DBControllerCarPark.UpdateCorrupted("park1", 18,true);
		DBControllerCarPark.UpdateCorrupted("park1", 23,true);
		DBControllerCarPark.UpdateCorrupted("park2", 11,true);

		DBControllerUsers.addComplaint("kobibarhanin@gmail.com","I would like to get my monet back!","park1");
		DBControllerUsers.addComplaint("e","I would like to get my monet back!","park1");

	}

	public static void clearDb(){
		clearTable("users");
		clearTable("orders");
		clearTable("regmembership");
		clearTable("fullmembership");
		clearTable("carparks");
		clearTable("cars");
		clearTable("complaints");
		clearTable("park1");
		clearTable("park2");
		clearTable("park3");
		clearTable("prices");
		restartAutoInc("orders");
		restartAutoInc("complaints");
		restartAutoInc("regmembership");
		restartAutoInc("fullmembership");
	}

	public static void restartAutoInc(String tableName){
		Connection conn= connectToDb();
		Statement stmt;
		try
		{
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			stmt.executeUpdate("ALTER TABLE "+tableName+" AUTO_INCREMENT = 1");

			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) { /* ignored */}
			}
		} catch (SQLException e) {e.printStackTrace();}

	}

	/*
 * scheduler to calculate statistics each week, using native MySQL language
 * added by Itay Guy
 */
	private static void addWeeklySched(Statement stmt) throws SQLException {
		String query = "SET GLOBAL event_scheduler = ON;";
		stmt.executeUpdate(query);
		query = "DROP EVENT IF EXISTS weekly_event;";
		stmt.executeUpdate(query);
		query = " CREATE EVENT IF NOT EXISTS `weekly_event`\n"
				+ " ON SCHEDULE EVERY 1 WEEK STARTS TIMESTAMP(CASE DAYNAME(NOW())"
				+ " WHEN 'Sunday' THEN NOW()\n"
				+ " WHEN 'Monday' THEN NOW() + INTERVAL 6 DAY\n"
				+ " WHEN 'Tuesday' THEN NOW() + INTERVAL 5 DAY\n"
				+ " WHEN 'Wednesday' THEN NOW() + INTERVAL 4 DAY\n"
				+ " WHEN 'Thursday' THEN NOW() + INTERVAL 3 DAY\n"
				+ " WHEN 'Friday' THEN NOW() + INTERVAL 2 DAY\n"
				+ " WHEN 'Saturday' THEN NOW() + INTERVAL 1 DAY\n"
				+ " END)\n"
				+ " DO CALL do_each_week();"; // do_each_week() defined natively procedures in mySQL
		stmt.executeUpdate(query);
	}

	public static void main(String[] args) {
		try {
//			addCarPark("Park1", 3, 3, 3);

			clearDb();
			buildDb();
			addDataToDb();

		} catch (Exception e){}
//		addDataToDb();
//		prioritize();

//		String input = "1111,k@b,2018/01/12 15:00:00,2018/01/12 17:00:00,1";
//		String[] parameters = input.split(",");
//		addOrder(parameters);
	}
}

