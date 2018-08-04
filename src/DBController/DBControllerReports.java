package DBController;

import CarPark.ParkDetails;
import CarPark.Status;
import CarPark.Struct;
import CarPark.Struct.CorruptPeriod;
import scheduler.CPSTimer;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DBControllerReports extends DBControllerMain{
    public static List<ArrayList<Object>> getCustomerOrdersExceeding() {
        Connection con = DBControllerMain.connectToDb();
        Statement stmt = null;
        List<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet res = stmt.executeQuery("SELECT * FROM `orders` WHERE `status`='" + Status.order.DELAY + "'");
            while(res.next()) {
            	ArrayList<Object> customers = new ArrayList<Object>();
                customers.add(res.getString(1)); //id
                customers.add(res.getInt(2)); // car id
                customers.add(res.getString(3)); // user email
                customers.add(res.getTimestamp(4)); // time_in
                customers.add(res.getTimestamp(5)); // time out
                customers.add(res.getString(6)); // park id
                customers.add(res.getString(7)); // status
                result.add(customers);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public static void auditExportTimeAt(Struct.report reportName,Timestamp timestamp) {
        Connection con = DBControllerMain.connectToDb();
        Statement stmt = null;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            int res = stmt.executeUpdate("UPDATE cpsscheduler SET `timesampling`='" + timestamp + "' WHERE `report`='" + reportName + "';");
            if(res == 0) {
            	stmt.executeUpdate("INSERT INTO `cpsscheduler` (`report`,`timeSampling`) VALUES('" + reportName + "','" + timestamp + "');");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<ArrayList<Object>> getCorruptedSpaces(String parkName){
        Connection con=connectToDb();
        Statement stmt = null;
        List<ArrayList<Object>> output = new ArrayList<ArrayList<Object>>();
        try
        {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM CarParks WHERE `name`='" + parkName + "';");
            while(rs.next())
            {
                ArrayList<Object> park= new ArrayList<Object>();
                park.add(rs.getString(1)); //park name
                park.add(rs.getInt(11)); //fiscal corrupted spaces
                output.add(park);
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

    public static ArrayList<ArrayList<Object>> getCustomerComplaints(String parkName){
        Connection con=connectToDb();
        Statement stmt;
        ArrayList<ArrayList<Object>> output = new ArrayList<ArrayList<Object>>();
        try
        {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM `complaints` WHERE `park_id`='" + parkName + "';");
            while(rs.next())
            {
                ArrayList<Object> comp= new ArrayList<Object>(7);
                comp.add(rs.getInt(1));
                comp.add(rs.getString(2));
                comp.add(rs.getTimestamp(3));
                comp.add(rs.getTimestamp(4));
                comp.add(rs.getInt(5));
                comp.add(rs.getString(6));
                comp.add(rs.getString(7));
                output.add(comp);
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

    /**
	 * Main Responsibility: fetching all customer orders
     * @return Map that contains all the customers divided in their customer type [- ahead order/inplace order -]
     */
    public static Map<String,ArrayList<ArrayList<Object>>> getCustomerOrders(int parkNum){
        Connection con=connectToDb();
        Statement stmt;
        Map<String,ArrayList<ArrayList<Object>>> output = new HashMap<String,ArrayList<ArrayList<Object>>>();
        try
        {
            stmt = con.createStatement();
            String query = "SELECT * FROM `orders` WHERE `park_id`=" + parkNum + ";";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next())
            {
                ArrayList<Object> ord= new ArrayList<Object>(7);
                ord.add(rs.getInt(1));
                ord.add(rs.getInt(2));
                ord.add(rs.getString(3));
                ord.add(rs.getTimestamp(4));
                ord.add(rs.getTimestamp(5));
                ord.add(rs.getInt(6));
                ord.add(rs.getString(7));
                ord.add(rs.getString(8));
                String customerType = rs.getString(8);
            	ArrayList<ArrayList<Object>> temp = output.get(customerType);
            	if(temp != null) {
            		temp.add(ord);
            	} else {
            		temp = new ArrayList<ArrayList<Object>>();
            		temp.add(ord);
            	}
            	output.put(customerType,temp);
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

    public static void resetCorruptedSpacesEachFiscalQuarter(String parkName) {
        Connection con=connectToDb();
        Statement stmt = null;
        try
        {
            stmt = con.createStatement();
            stmt.executeUpdate("UPDATE `carparks` SET `fiscal_amount_corrupted_spaces`=" + 0 + " WHERE `name`='" + parkName + "';");
            if (stmt != null) {
                try {
                	stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
        } catch (SQLException e) {e.printStackTrace();}
    }

    public static Timestamp getLastSamplingTime(Struct.report reportName) {
        Connection con = DBControllerMain.connectToDb();
        Statement stmt;
        Timestamp maxTime = null;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet orderSet = stmt.executeQuery("SELECT `timeSampling` FROM `cpsscheduler` WHERE `report`='" + reportName + "';");
            orderSet.next();
            maxTime = orderSet.getTimestamp(1);
        }catch(Exception ex) {
            System.out.println("Could not retrieve the timestamp from DB.");
            return null;
        }
        return maxTime;
    }
    

    
    public static Map<List<Object>,Map<List<ArrayList<Object>>,List<ArrayList<Object>>>> makeParkStateVector(String parkName) {
		Connection conn = connectToDb();
		Statement stmt = null;
		Map<List<Object>,Map<List<ArrayList<Object>>,List<ArrayList<Object>>>> image = new HashMap<List<Object>,Map<List<ArrayList<Object>>,List<ArrayList<Object>>>>();
		try
		{
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			Timestamp now = CPSTimer.getTimestamp();
			stmt.executeQuery("CALL do_each_month();");
			ResultSet result = stmt.executeQuery("SELECT * FROM `statistics` WHERE `tab`='ORDER' AND (`param`='OCCUPY' OR `param`='CANCEL') AND (`method`='MEDIAN' OR `method`='STD') AND `iteration`='MONTH' AND `timeSampling`>='" + now + "' AND `park_id`='" + parkName + "'");
			List<ArrayList<Object>> parkNameBlackList = new ArrayList<ArrayList<Object>>();
			List<ArrayList<Object>> existParkList = new ArrayList<ArrayList<Object>>();
			ArrayList<Object> parkList = new ArrayList<Object>();
			while(result.next()) {
				String parkId = result.getString(8);//park_id
				ArrayList<Object> parkVec = null;
				for(ArrayList<Object> parks : parkNameBlackList) {
					if(parks.contains(parkId)) {
						parkVec = parks;
					}
				}
				ArrayList<Object> existPark = null;
				for(ArrayList<Object> exist : existParkList) {
					if(exist.contains(parkId)) {
						existPark = exist;
					}
				}			
				if(parkVec == null) {
					ArrayList<Object> temp = new ArrayList<Object>();
					temp.add(result.getString(1));//tab
					temp.add(result.getString(2));//param
					temp.add(result.getString(3));//method
					temp.add(result.getDouble(4));//result
					temp.add(result.getString(5));//iteration
					temp.add(result.getTimestamp(6));//timeSampling
					temp.add(result.getInt(7));//total_amount
					temp.add(parkId);
					if(image.get(existPark) == null) {
						stmt = conn.createStatement();
						ResultSet parkDetails = stmt.executeQuery("SELECT * FROM `carparks` WHERE `name`='" + parkId + "'");
						parkDetails.next();
						parkList.add(parkId);
						parkList.add(parkDetails.getInt(2));
						parkList.add(parkDetails.getInt(3));
						parkList.add(parkDetails.getInt(4));
						Map<List<ArrayList<Object>>,List<ArrayList<Object>>> map = new HashMap<List<ArrayList<Object>>,List<ArrayList<Object>>>();
						List<ArrayList<Object>> features = new ArrayList<ArrayList<Object>>();
						features.add(temp);
						List<ArrayList<Object>> indicators = DBControllerReports.getPlacesStateIndicate(parkId);
						if(indicators != null && !indicators.isEmpty()) {
							map.put(indicators, features);
							ArrayList<Object> deepCopyParkList = new ArrayList<Object>(parkList);
							image.put(deepCopyParkList,map);
							if(!existParkList.contains(parkList)) {
								existParkList.add(deepCopyParkList);
							}
							parkList.clear();
							System.out.println(existParkList);
						} else {
							if(!parkNameBlackList.contains(parkList)) {
								parkNameBlackList.add(new ArrayList<Object>(parkList));
							}
							parkList.clear();
							System.out.println("Park is empty of not exist.");
							System.out.println(parkNameBlackList);
						}
					} else {
						Map<List<ArrayList<Object>>,List<ArrayList<Object>>> parkMap = ((Map<List<ArrayList<Object>>,List<ArrayList<Object>>>)image.get(existPark));
						Set<List<ArrayList<Object>>> bits = parkMap.keySet();
						List<ArrayList<Object>> bitList = bits.iterator().next();
						List<ArrayList<Object>> currentFeatures = ((List<ArrayList<Object>>)parkMap.get(bitList));
						currentFeatures.add(temp);
						parkMap.put(bitList,currentFeatures);
						image.put(new ArrayList<Object>(existPark),parkMap);
					}
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return image;
    }
    
    private static List<ArrayList<Object>> getPlacesStateIndicate(String parkId){
		Connection conn = connectToDb();
		Statement stmt = null;
		List<ArrayList<Object>> parksVector = new ArrayList<ArrayList<Object>>();
		try
		{
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ResultSet result = stmt.executeQuery("SELECT * FROM `" + parkId + "`;");
			while(result.next()) {
				ArrayList<Object> temp = new ArrayList<Object>();
				temp.add(result.getInt(1));//position
				temp.add(result.getInt(2));//car_id
				temp.add(result.getString(3));//status
				temp.add(result.getInt(4));//priority
				parksVector.add(temp);
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return parksVector;
    }
    
    public static ArrayList<ArrayList<Object>> getImageParksState(){
        Connection con=connectToDb();
        Statement stmt;
        ArrayList<ArrayList<Object>> output = new ArrayList<ArrayList<Object>>();
        try
        {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM CarParks;");
            while(rs.next())
            {
                ArrayList<Object> comp= new ArrayList<Object>(3);
                comp.add(rs.getString(1)); // park name
                comp.add(rs.getInt(5)); // free space
                comp.add(rs.getInt(7)); // resided space
                output.add(comp);
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

	public static boolean testAndSetOrderStatus() {
        Connection con=connectToDb();
        Statement stmt = null;
        boolean isIncomplete = false;
        try
        {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM `orders`;");
            String condition = "";
            while(rs.next())
            {
            	int id = rs.getInt(1);
            	Timestamp dbTimestamp = rs.getTimestamp(4);
            	String dbStatus = rs.getString(7);
                if(dbTimestamp.before(CPSTimer.getTimestamp()) && dbStatus.equals(Status.order.OCCUPY.toString())) {
                	if(!condition.equals("")) {
                		condition += " OR `id`=" + id;
                	} else {
                		condition += "`id`=" + id;
                	}
                }
            }
            if(condition.length() > 0) {
            	isIncomplete = true;
            	String query = "UPDATE Orders SET `status`='" + Status.order.DELAY + "' WHERE " + condition + ";";
            	stmt.executeUpdate(query);
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
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        return isIncomplete;
	}
	
	public static List<Integer> getPerformacePair(){
        Connection con=connectToDb();
        Statement stmt = null;
        List<Integer> pair = new ArrayList<Integer>();
        pair.add(0);
        pair.add(0);
        try
        {
            stmt = con.createStatement();
            ResultSet rsMem = stmt.executeQuery("CALL more_than_two_cars();");
            stmt = con.createStatement();
            ResultSet rsMonthCount = stmt.executeQuery("CALL current_members();");
            if(rsMem.next()) {
            	pair.set(0,rsMem.getInt(2));
            }
            if(rsMonthCount.next()) {
            	pair.set(1,rsMonthCount.getInt(1));
            }            
            if (rsMem != null) {
                try {
                	rsMem.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (rsMonthCount != null) {
                try {
                	rsMonthCount.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        return pair;
	}

	
	public static Map<String,List<ArrayList<Object>>> getPeriodReport() {
        Connection con=connectToDb();
        Statement stmt = null;
		Map<String,List<ArrayList<Object>>> map = new HashMap<String,List<ArrayList<Object>>>();
        try
        {
            stmt = con.createStatement();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Timestamp monthAgo = new Timestamp(cal.getTimeInMillis());
            stmt.executeQuery("CALL do_each_month();");
			ResultSet parks = stmt.executeQuery("SELECT * FROM `carparks`;");
			while(parks.next()) {
				String parkName = parks.getString(1);
				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM `statistics` WHERE `tab`='CORRUPTED_SPACES' AND `param`='CORRUPTED_SPACES' AND (`method`='MEDIAN' OR `method`='STD') AND `iteration`='MONTH' AND `timeSampling`>'" + monthAgo + "' AND `park_id`='" + parkName + "';");
				List<ArrayList<Object>> stats = new ArrayList<ArrayList<Object>>();
				ArrayList<Object> arr = new ArrayList<Object>();
				while(rs.next()) {
					arr.add(rs.getString(1));
					arr.add(rs.getString(2));
					arr.add(rs.getString(3));
					arr.add(rs.getDouble(4));
					arr.add(rs.getString(5));
					arr.add(rs.getTimestamp(6));
					arr.add(rs.getInt(7));
					arr.add(rs.getString(8));
					stats.add(arr);
	            }
				map.put(parkName, stats);
			}
			if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        	return null;
        }
        return map;		
	}
}
