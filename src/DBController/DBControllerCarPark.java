package DBController;
import CarPark.*;
import CarPark.Reservation;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class DBControllerCarPark extends DBControllerMain{


    public static boolean addOrder(String[] input) {
        Connection conn = connectToDb();
        Statement stmt;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

            /*
            id given by Db
            input[0] =  car
            input[1] =  email
            input[2] =  timeIn
            input[3] =  timeOut
            input[4] =  park
            input[5] =  type
            define order status
            */

            stmt.executeUpdate("INSERT INTO Orders VALUES (NULL, '" + input[0] + "' , '" + input[1] + "' , '" + input[2] +
                    "' , '" + input[3] + "' , '" + input[4] + "' , '"+Status.order.RESERVED+"' , '" + input[5] + "')");

            /* get newly added order num */
            ResultSet rs1 = stmt.executeQuery("SELECT MAX(id) as maxId FROM Orders");
            rs1.first();
            int newOrderId= rs1.getInt("maxId");


            CarPark park = new CarPark();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Park"+input[4]+";");
            retrievePark(conn,rs,park);
            Reservation res = new Reservation(Integer.parseInt(input[0]), newOrderId, input[2], input[3]);
            park.addOrderToPark(res);
            writePark(park, Integer.parseInt(input[4]));


            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean cancelOrder(int orderId) {
        Connection conn = connectToDb();
        Statement stmt;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

            ResultSet rs1 = stmt.executeQuery("SELECT park_id as parkId FROM Orders WHERE id = '" + orderId + "'");
            rs1.first();
            int carpark= rs1.getInt("parkId");

            stmt.executeUpdate("DELETE FROM Orders WHERE id = '" + orderId + "'");

            CarPark park = new CarPark();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Park"+carpark+";");
            retrievePark(conn,rs,park);

            System.out.println("remove order: " + park.removeOrderFromPark(orderId));
            writePark(park,carpark);


            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean completeOrder(int orderId) {
        Connection conn = connectToDb();
        Statement stmt;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

            ResultSet rs1 = stmt.executeQuery("SELECT park_id as parkId FROM Orders WHERE id = '" + orderId + "'");
            rs1.first();
            int carpark= rs1.getInt("parkId");

            stmt.executeUpdate("UPDATE Orders SET status = '"+Status.order.CLOSED+"' WHERE id = '" + orderId + "'");

            CarPark park = new CarPark();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Park"+carpark+";");
            retrievePark(conn,rs,park);

            System.out.println("remove order: " + park.removeOrderFromPark(orderId));
            writePark(park,carpark);


            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getOrders (String userEmail) {
        Connection conn= connectToDb();
        Statement stmt;
        String orders="";
        try
        {


            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT id,car_id,time_in,time_out,park_id,status FROM Orders WHERE user_email = '" + userEmail + "'");

            while(rs.next())
            {
                orders+=rs.getInt(1)+","+rs.getInt(2)+","+rs.getString(3)+","+rs.getString(4)+","+rs.getInt(5)+","+rs.getString(6)+";";
            }


            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public static void addCarPark(String carParkName, int height, int width, int depth) throws SQLException {
        Connection con=connectToDb();
        Statement stmt;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS "+carParkName+"(position int PRIMARY KEY, car_id int, status VARCHAR(40), priority int, schedule VARCHAR(400));");
            int d=0;
            for(int i=1;i<=height;i++){
                for(int j=1, k=1;j<=width*depth;j++){
                    stmt.executeUpdate("INSERT IGNORE INTO "+carParkName+" VALUES ('" + Integer.toString(i)+Integer.toString(j) + "' , '0', 'EMPTY'," + (k-1+d)+" , '0')");
                    if(j%3==0)k++;
                }
                d++;
            }
            stmt.executeUpdate("INSERT IGNORE INTO CarParks VALUES ('" + carParkName + "' , '" + height + "' , '" + width + "' , '" + depth + "' , '" + height*width*depth + "' , '0' )");

            stmt.executeUpdate("INSERT IGNORE INTO prices VALUES ('" + Integer.parseInt(carParkName.replace("Park","")) + "' , '0' , '0') ");

            stmt.executeUpdate("INSERT IGNORE INTO Users VALUES ('" + carParkName + " Manager' , ' 0 ' , '0' , '" + carParkName + "m' , '"+UserType.type.BRANCH_MANAGER+"')");

        } catch (SQLException e) {
            e.printStackTrace();}
    }

    public static boolean reserveSpot(String carParName, String location, int carId){
        Connection conn = connectToDb();
        Statement stmt;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate("UPDATE "+carParName+" SET car_id = '"+carId+"' , status = 'Reserved' WHERE position = '"+location+"';");

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void writePark(CarPark park, int parkNum){
        try {
            Connection conn=connectToDb();
            Statement stmt;

            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

            for(int i=0; i<park.carpark.size(); i++){
                for(int j=0; j<park.carpark.get(i).size(); j++){

                    String sched = park.getPriorityLevel(i).get(j).stringSchudle;
                    String pos = park.getPriorityLevel(i).get(j).location;
                    stmt.executeUpdate("update park"+parkNum+" set schedule = '" + sched + "' where position = '" + pos + "';");

                }

            }
        }
        catch (SQLException ex){}
    }

    public static void retrievePark(Connection con, ResultSet rs, CarPark park){
        try {
            while (rs.next()) {

                ParkingSpot parkingSpot = new ParkingSpot(rs.getString(1), rs.getInt(4), rs.getString(5));
                switch (parkingSpot.priority) {
                    case 0:
                        park.p0.add(parkingSpot);
                        break;
                    case 1:
                        park.p1.add(parkingSpot);
                        break;
                    case 2:
                        park.p2.add(parkingSpot);
                        break;
                    case 3:
                        park.p3.add(parkingSpot);
                        break;
                    case 4:
                        park.p4.add(parkingSpot);
                        break;
                }
            }
            park.lineSize=park.p0.size();
        }
        catch (SQLException ex){}
    }

    public static boolean insertCarToParkOrder(int carId, int parkId, int orderId){
        Connection con=connectToDb();
        Statement stmt;
        try
        {
            stmt = con.createStatement();


            CarPark park = new CarPark();
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM Park"+parkId+";");
            retrievePark(con,rs1,park);
            int pos = park.getPositionOfOrder(orderId, carId);
            if(pos==0) return false;

            stmt.executeUpdate("UPDATE Orders SET status = '"+Status.order.OCCUPY+"' WHERE id = '" + orderId + "';");
            stmt.executeUpdate("UPDATE park"+parkId+" SET car_id = '"+carId+"' , status = 'OCCUPIED' WHERE position = '" +pos+ "';");
            stmt.executeUpdate("UPDATE Carparks SET free_space = free_space -1 WHERE name = 'park"+parkId+"';");

            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
        } catch (SQLException e) {e.printStackTrace();}
        return true;
    }

    public static boolean insertCarToParkMember(int carId, int parkId, int memberId, String time_out){
        Connection con=connectToDb();
        Statement stmt;
        try
        {
            stmt = con.createStatement();

            //findout userEmail from carID<<<<<<<<<<<<<
            String userEmail="";
            ResultSet rs4 = stmt.executeQuery("SELECT user_email FROM cars WHERE id = '"+carId+"';");
            if(!rs4.next()) return false;
            else userEmail=rs4.getString(1);


            //check for memberships
            if(memberId==0) {
                //check for full membership, if found continue, else return error
                ResultSet rs3 = stmt.executeQuery("SELECT * FROM fullmembership WHERE car_id = '"+carId+"';");
                if(!rs3.next()) return false;
            }
            else{
                //check for regular membership, if found continue, else return error
                ResultSet rs3 = stmt.executeQuery("SELECT * FROM regmembership WHERE id = '"+memberId+"' " +
                        "AND carpark = '"+parkId+"' AND car_id = '"+carId+"';");
                if(!rs3.next()) return false;

            }

            Date current_time = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String input = carId+","+userEmail+","+dateFormat.format(current_time)+","+time_out+","+parkId+","+"MEMBER";
            addOrder(input.split(","));

            /* get newly added order num */
            ResultSet rs1 = stmt.executeQuery("SELECT MAX(id) as maxId FROM Orders");
            rs1.first();
            int orderId= rs1.getInt("maxId");

            System.out.println("order num: " + orderId);

            CarPark park = new CarPark();
            ResultSet rs2 = stmt.executeQuery("SELECT * FROM Park"+parkId+";");
            retrievePark(con,rs2,park);
            int pos = park.getPositionOfOrder(orderId, carId);

            if(pos==0) return false;

            stmt.executeUpdate("UPDATE Orders SET status = '"+Status.order.OCCUPY+"' WHERE id = '" + orderId + "';");
            stmt.executeUpdate("UPDATE park"+parkId+" SET car_id = '"+carId+"' , status = 'Occupied' WHERE position = '" +pos+ "';");
            stmt.executeUpdate("UPDATE Carparks SET free_space = free_space -1 WHERE name = 'park"+parkId+"';");

            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
        } catch (SQLException e) {e.printStackTrace();}
        return true;
    }

    public static boolean removeCarFromPark(int carId, int parkId){
        Connection con=connectToDb();
        Statement stmt;
        try
        {
            //need to check that the id belongs to the car
            stmt = con.createStatement();

            CarPark park = new CarPark();
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM Park"+parkId+";");
            retrievePark(con,rs1,park);
            int orderId = park.getOrderId(carId);
            if(orderId==0) return false;
            writePark(park,parkId);

            stmt.executeUpdate("UPDATE park"+parkId+" SET car_id = '0' , status = 'EMPTY' WHERE car_id = '" +carId+ "';");

            completeOrder(orderId);

            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
        } catch (SQLException e) {e.printStackTrace();}
        return true;
    }

    public static boolean setPrices(int parkId, int inPlacePrice, int inAdvancePrice){
        Connection conn = connectToDb();
        Statement stmt;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

            stmt.executeUpdate("UPDATE Prices SET in_place = '" + inPlacePrice + "' , in_advance = '" + inAdvancePrice + "' WHERE park_id = '"+parkId+"';");

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

    public static String getPrices (int parkId) {
        Connection conn= connectToDb();
        Statement stmt;
        String prices="";
        try
        {

            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT in_place, in_advance FROM Prices WHERE park_id = '" + parkId + "'");

            if(rs.next()) prices+=rs.getInt(1)+","+rs.getInt(2);

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prices;
    }

    public static double calcCostOrder(int orderId){
        double cost=0;
        Connection conn = connectToDb();
        Statement stmt;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs1 = stmt.executeQuery("SELECT time_in,time_out,type,park_id FROM Orders WHERE id = '" + orderId + "'");
            rs1.next();

            Date time_in = new Date();
            Date time_out = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            time_in=dateFormat.parse(rs1.getString(1).substring(0,19));

            long timeDiff = time_out.getTime() - time_in.getTime();
            double hoursDiffFactor = 60*60*1000;
            double diffInHours = timeDiff /hoursDiffFactor;


            String type = rs1.getString(3);
            ResultSet rs2 = stmt.executeQuery("SELECT in_place,in_advance FROM Prices WHERE park_id = '" + rs1.getString(4) + "'");
            rs2.next();

            if(type.equals("AHEAD")){
                return diffInHours*(double)rs2.getInt(2);
            }
            if(type.equals("INPLACE")){
                return diffInHours*(double)rs2.getInt(1);
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return cost;
    }

    public static double calcCost(int carId){
        double cost=0;
        Connection conn = connectToDb();
        Statement stmt;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs1 = stmt.executeQuery("SELECT time_in,time_out,type,park_id FROM Orders WHERE car_id = '" + carId + "' AND type <> 'MEMBER' AND status = 'OCCUPIED'");
            rs1.next();

            Date time_in = new Date();
            Date time_out = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            time_in=dateFormat.parse(rs1.getString(1).substring(0,19));

            long timeDiff = time_out.getTime() - time_in.getTime();
            double hoursDiffFactor = 60*60*1000;
            double diffInHours = timeDiff /hoursDiffFactor;


            String type = rs1.getString(3);
            ResultSet rs2 = stmt.executeQuery("SELECT in_place,in_advance FROM Prices WHERE park_id = '" + rs1.getString(4) + "'");
            rs2.next();

            if(type.equals("AHEAD")){
                return diffInHours*(double)rs2.getInt(2);
            }
            if(type.equals("INPLACE")){
                return diffInHours*(double)rs2.getInt(1);
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return cost;
    }

    public static String getCarparks () {
        Connection conn= connectToDb();
        Statement stmt;
        String carparks="";
        try
        {


            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT name FROM carparks");

            while(rs.next())
            {
                carparks+=rs.getString(1)+";";
            }


            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carparks;
    }

    public static boolean addPriceChangeRequest(String parkId, String inPlacePrice, String aheadPrice){
        Connection conn = connectToDb();
        Statement stmt;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

            stmt.executeUpdate("INSERT INTO PriceChanges VALUES (NULL,'" + parkId + "' , '" + inPlacePrice +  "' , '"+ aheadPrice +"' , 'PENDING')");

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

    public static String getPriceChangeRequests(){
        Connection conn= connectToDb();
        Statement stmt;
        String priceChangeRequests="";
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery("SELECT id,park_id,in_place,in_advance,status FROM priceChanges");

            while(rs.next())
            {
                priceChangeRequests+=rs.getInt(1)+","+rs.getString(2)+","+rs.getString(3)+","+rs.getString(4)+","+rs.getString(5)+";";
            }


            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return priceChangeRequests;
    }

    public static boolean approveSetPrices(String requestId , String parkId, String inPlacePrice, String inAdvancePrice){
        if(setPrices(Integer.parseInt(parkId), Integer.parseInt(inPlacePrice), Integer.parseInt(inAdvancePrice))) {
            Connection conn = connectToDb();
            Statement stmt;
            try {
                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

                stmt.executeUpdate("UPDATE pricechanges SET status = 'APPROVED' WHERE id = '" + requestId + "';");

                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean rejectSetPrices(String requestId){
        Connection conn = connectToDb();
        Statement stmt;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

            stmt.executeUpdate("UPDATE pricechanges SET status = 'REJECTED' WHERE id = '" + requestId + "';");

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

    public static String getCarparkInfo(String parkId){
        Connection conn= connectToDb();
        Statement stmt;
        String carparkInfo="";
        try
        {

            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

            ResultSet rs1 = stmt.executeQuery("SELECT height,width,depth,free_space FROM carparks WHERE name = 'park" + parkId + "'");
            if(rs1.next()) carparkInfo+=(rs1.getInt(1)*rs1.getInt(2)*rs1.getInt(3)-rs1.getInt(4))+",";

            ResultSet rs2 = stmt.executeQuery("SELECT count(*) FROM orders WHERE park_id = '" + parkId + "' AND status <> 'COMPLETE' ");
            if(rs2.next()) carparkInfo+=rs2.getInt(1)+",";

            ResultSet rs3 = stmt.executeQuery("SELECT COUNT(*) FROM corrupted_spaces WHERE park_id = 'park" + parkId + "'");
            if(rs3.next()) carparkInfo+=rs3.getInt(1);

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carparkInfo;
    }

    public static boolean UpdateCorrupted(String parkId,int location,boolean isCorrupted) {
        Connection conn = connectToDb();
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            if(isCorrupted) {
                stmt.executeUpdate("UPDATE `" + parkId + "` SET `status`='" + ParkDetails.ParkPos.CORRUPT.toString() + "' WHERE `position`='" + location + "';");
                stmt.executeUpdate("UPDATE `carparks` SET `fiscal_amount_corrupted_spaces`= `fiscal_amount_corrupted_spaces`+1 WHERE `name`='" + parkId + "';");
                stmt.executeUpdate("INSERT INTO `corrupted_spaces` (`park_id`,`position`,`time`) VALUES('" + parkId + "'," + location + ",current_timestamp());");
            } else {
                ResultSet rs = stmt.executeQuery("SELECT `fiscal_amount_corrupted_spaces` FROM `carparks` WHERE `name`='" + parkId + "'");
                if(rs.next()) {
                    if(rs.getInt(1) > 0) {
                        stmt.executeUpdate("UPDATE `carparks` SET `fiscal_amount_corrupted_spaces`= `fiscal_amount_corrupted_spaces`-1 WHERE `name`='" + parkId + "';");
                    }
                }
                stmt.executeUpdate("UPDATE `" + parkId + "` SET `status`='" + ParkDetails.ParkPos.EMPTY.toString().toLowerCase() + "' WHERE `position`=" + location + ";");
                stmt.executeUpdate("DELETE FROM `corrupted_spaces` WHERE `park_id`='" + parkId + "' AND `position`=" + location + ";");
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {}
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }


    public static void main(String[] args) {
		try {
            buildDb();
//        addDataToDb();
//        addCarPark("Park18", 3, 3, 3);
            addCarPark("park4",3,4,3);

        } catch (Exception e){}

//        addPriceChangeRequest("1","9","9");
//        addPriceChangeRequest("2","8","8");
//        addPriceChangeRequest("3","97","97");

        //        System.out.println(calcCost(10));
//        setPrices(14,1,1);
//        System.out.println(Integer.parseInt("park12".replace("park","")));
//        addFullmembership("9999","k@b","2018/01/23" );
//        addFullmembership("5678","k@b","2018/01/23" );
//        System.out.println("getFullmembership(k_at_b): " + getFullmembership("k@b"));
//        addRegmembership("56787","k@b","2018/01/23","park1","10:00:00");
//        addRegmembership("12421","k@b","2018/01/23","park1","10:00:00");
//        System.out.println("getRegmembership(k_at_b): " + getRegmembership("k@b"));


//
//        String input = "5678,k@b,2018/02/02 10:35:00,2018/02/02 22:03:52,14,INPLACE";
//        String[] parameters = input.split(",");
//        System.out.println("addOrder(parameters): " + addOrder(parameters));
//
//        cancelOrder(27);
//
//        input = "8888,k@b,2018/01/17 10:00:00,2018/01/22 17:00:00,1";
//        parameters = input.split(",");
//        System.out.println("addOrder(parameters): " + addOrder(parameters));
//
//        System.out.println("getOrders(k_at_b): " + getOrders("k@b"));


//        System.out.println("insertCarToPark(8888,1,7): " + insertCarToPark(8888,1,7));
//        System.out.println("insertCarToPark(8888,1,8): " + insertCarToPark(8888,1,8));

//        clearTable("orders");


//        cancelOrder(12);
//        System.out.println("removeCarFromPark(2222,1): " + removeCarFromPark(2222,1));

//        System.out.println(getPrices(14));

//        insertCarToPark(5678,14,33);
//        removeCarFromPark(5678,14);
//        setPrices(14,5,3);

//        System.out.println("cost is: " + calcCost(3));
//        removeCarFromPark(5555,14);

//        Date now = new Date();
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(dateFormat.format(now));


//        System.out.println(insertCarToParkMember("k@b",123,14,6,"2018/02/04 17:00:00"));
//        String input = "5666,k@b,2018/02/04 10:35:00,2018/02/05 22:03:52,14,INPLACE";
//        String[] parameters = input.split(",");
//        System.out.println("addOrder(parameters): " + addOrder(parameters));

//        System.out.println(insertCarToParkMember(1,1,8,"2018/02/05 22:00:00"));
//        System.out.println(removeCarFromPark(5666,14));

        System.out.println(getCarparkInfo("1"));

    }



}