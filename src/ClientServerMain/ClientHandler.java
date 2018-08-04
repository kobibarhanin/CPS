package ClientServerMain;

import ocsf.ChatClient;

import java.util.concurrent.Semaphore;

public class ClientHandler {

    private Semaphore semaphore = new Semaphore(0);
    private boolean verifyFLAG = false;
    private boolean addCarFLAG = false;
    private boolean addOrderFLAG = false;
    private boolean cancelOrderFLAG = false;
    private boolean addFullMembershipFLAG = false;
    private boolean addRegMembershipFLAG = false;
    private boolean addUserFLAG = false;
    private boolean enterOrderFLAG = false;
    private boolean enterMemberFLAG = false;
    private boolean leaveFLAG = false;
    private boolean complaintFLAG = false;
    private boolean closeComplaintFLAG = false;
    private boolean approveSetPricesFLAG = false;
    private boolean rejectSetPricesFLAG = false;
    private boolean addPriceChangeRequestFLAG = false;
    private String adminReport;
    private String parkAdminReport;
    private String archiveReport;

    private String userName;
    private String userType;
    private double cost = -1;
    public String userEmail;
    public String [] carsList;
    public String [] carparks;
    public String [] fullMemberships;
    public String [] regMemberships;
    public String [] orders;
    public String [] complaints;
    private String [] priceChangeRequests;
    private String [] carparkInfo;
    private int memberType;

    ChatClient client;

    public ClientHandler(ChatClient _client) {
        client = _client;
    }

    public static String parseReply(String message){
        String [] tmpCmd = message.split("-",2);
        return tmpCmd[0];
    }

    public static String parseInput(String message){
        String [] tmpCmd = message.split("-",2);
        if (tmpCmd[1].equals("")) return message;
        else return tmpCmd[1];
    }

    public void handle(String message){
        System.out.println("From Server: " + message);

        String reply=parseReply(message);

        if(message.equals("verified")) {
            verifyFLAG = true;
            semaphore.release();
        }
        else if(message.equals("notVerified"))
            semaphore.release();

        if(reply.equals("userName")) {
            userName =parseInput(message);
            semaphore.release();
        }

        if(reply.equals("userType")) {
            userType =parseInput(message);
            semaphore.release();
        }

        if(reply.equals("carList")) {
            carsList=parseInput(message).split(",");
            semaphore.release();
        }

        if(reply.equals("order")) {
            if(parseInput(message).equals("true")){
                addOrderFLAG =true;
            }
            semaphore.release();
        }

        if(reply.equals("Full Memberships")) {
            fullMemberships=parseInput(message).split(";");
            semaphore.release();
        }

        if(reply.equals("Regular Memberships")) {
            regMemberships=parseInput(message).split(";");
            semaphore.release();
        }

        if(reply.equals("Orders")) {
            orders=parseInput(message).split(";");
            semaphore.release();
        }

        if(reply.equals("addFullMember")) {
            if(parseInput(message).equals("true")){
                addFullMembershipFLAG =true;
            }
            semaphore.release();
        }

        if(reply.equals("addRegMember")) {
            if(parseInput(message).equals("true")){
                addRegMembershipFLAG =true;
            }
            semaphore.release();
        }

        if(reply.equals("addCar")) {
            if(parseInput(message).equals("true")){
                addCarFLAG =true;
            }
            semaphore.release();
        }

        if(reply.equals("addUser")) {
            if(parseInput(message).equals("true")){
                addUserFLAG =true;
            }
            semaphore.release();
        }

        if(reply.equals("cancelOrder")) {
            if(parseInput(message).equals("true")){
                cancelOrderFLAG=true;
            }
            semaphore.release();
        }

        if(reply.equals("Cost")) {
            cost = Double.parseDouble(parseInput(message));
            semaphore.release();
        }

        if(reply.equals("enterOrder")) {
            if(parseInput(message).equals("true")){
                enterOrderFLAG=true;
            }
            semaphore.release();
        }

        if(reply.equals("enterMember")) {
            if(parseInput(message).equals("true")){
                enterMemberFLAG=true;
            }
            semaphore.release();
        }

        if(reply.equals("leave")) {
            if(parseInput(message).equals("true")){
                leaveFLAG=true;
            }
            semaphore.release();
        }

        if(reply.equals("addComplaint")) {
            if(parseInput(message).equals("true")){
                complaintFLAG=true;
            }
            semaphore.release();
        }

        if(reply.equals("getComplaints")) {
            complaints=parseInput(message).split(";");
            semaphore.release();
        }

        if(reply.equals("closeComplaint")) {
            if(parseInput(message).equals("true")){
                closeComplaintFLAG=true;
            }
            semaphore.release();
        }

        if(reply.equals("getCarparks")) {
            carparks=parseInput(message).split(";");
            semaphore.release();
        }

        if(reply.equals("isaMember")) {
            memberType =Integer.parseInt(parseInput(message));
            semaphore.release();
        }

        if(reply.equals("getPriceChangeRequests")) {
            priceChangeRequests=parseInput(message).split(";");
            semaphore.release();
        }

        if(reply.equals("approveSetPrices")) {
            if(parseInput(message).equals("true")){
                approveSetPricesFLAG=true;
            }
            semaphore.release();
        }

        if(reply.equals("rejectSetPrices")) {
            if(parseInput(message).equals("true")){
                rejectSetPricesFLAG=true;
            }
            semaphore.release();
        }

        if(reply.equals("addPriceChangeRequest")) {
            if(parseInput(message).equals("true")){
                addPriceChangeRequestFLAG=true;
            }
            semaphore.release();
        }

        if(reply.equals("getCarparkInfo")) {
            carparkInfo=parseInput(message).split(",");
            semaphore.release();
        }

        if(reply.equals("createAdminReport")) {
            this.adminReport = parseInput(message);
            semaphore.release();
        }

        if(reply.equals("createParkAdminReport")) {
            this.parkAdminReport = parseInput(message);
            semaphore.release();
        }

        if(reply.equals("retrievePastReport")) {
            this.archiveReport = parseInput(message);
            semaphore.release();
        }
    }

    public void printToServer(String output){
        client.handleMessageFromClientUI("printToServer-" + output);
    }

    public String getEmail(){
        return userEmail;
    }

    public String getUserName(String email){
        client.handleMessageFromClientUI("getUserName-" + email);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        System.out.println("userName is: " + userName);
        return userName;
    }

    public boolean addUser(String name,String id,String password,String email){
        client.handleMessageFromClientUI("addUser-" + name + "," + id + "," + password + "," + email);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        if(addUserFLAG){
            addUserFLAG =false;
            return true;
        }
        else return false;
    }

    public void buildDb (){
        client.handleMessageFromClientUI("buildDb-");
    }

    public boolean verifyUser(String email, String password){
        userEmail = email;
        client.handleMessageFromClientUI("verifyUser-" + email + "," + password);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        if(verifyFLAG) return true;
        else return false;
    }

    public boolean addCar(String carId, String userEmail){
        client.handleMessageFromClientUI("addCar-" + carId + "," + userEmail);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        if(addCarFLAG){
            addCarFLAG =false;
            return true;
        }
        else return false;
    }

    public boolean addFullMembership(String carId, String userEmail){
        client.handleMessageFromClientUI("addFullMembership-" + userEmail + "," + carId);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        if(addFullMembershipFLAG){
            addFullMembershipFLAG =false;
            return true;
        }
        else return false;
    }

    public boolean addRegMembership(String carId, String userEmail, String carpark, String end_time){
        client.handleMessageFromClientUI("addRegMembership-" + carId + "," + userEmail + "," + carpark + "," + end_time);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        if(addRegMembershipFLAG){
            addRegMembershipFLAG =false;
            return true;
        }
        else return false;
    }

    public void HelperPrintToclientConsole(String output){
        System.out.println(output);
    }

    public String[] getCars() {
        client.handleMessageFromClientUI("getCars-" + userEmail);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        return carsList;
    }

    public boolean addOrder(int car,String email,String timeIn,String timeOut,int park, String type) throws Exception {
            client.handleMessageFromClientUI("addOrder-" + car + "," + email + "," + timeIn + "," + timeOut + "," + park + "," + type);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        if(addOrderFLAG){
            addOrderFLAG =false;
            return true;
        }
        else return false;
    }

    public boolean cancelOrder(int orderId) throws Exception {
        client.handleMessageFromClientUI("cancelOrder-" + orderId);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        if(cancelOrderFLAG){
            cancelOrderFLAG =false;
            return true;
        }
        else return false;
    }

    public String [] getFullmemberships(String userEmail){
        client.handleMessageFromClientUI("getFullmemberships-" + userEmail);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        return fullMemberships;
    }

    public String [] getRegmemberships(String userEmail){
        client.handleMessageFromClientUI("getRegmemberships-" + userEmail);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        return regMemberships;
    }

    public String [] getOrders(String userEmail){
        client.handleMessageFromClientUI("getOrders-" + userEmail);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        return orders;
    }

    public double calcCost(int orderId){
        client.handleMessageFromClientUI("calcCost-" + orderId);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        return cost;
    }

    public boolean enterParkOrder(int carId, int parkId, int orderId){
        client.handleMessageFromClientUI("enterOrder-" + carId + "," + parkId + ","  + orderId);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        if(enterOrderFLAG){
            enterOrderFLAG =false;
            return true;
        }
        else return false;
    }

    public boolean enterParkMember(int carId, int parkId, int memberId, String time_out){
        client.handleMessageFromClientUI("enterMember-" + carId + "," + parkId + ","  + memberId + ","  + time_out);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        if(enterMemberFLAG){
            enterMemberFLAG =false;
            return true;
        }
        else return false;
    }

    public boolean leavePark(int carId, int parkId){
        client.handleMessageFromClientUI("leave-" + carId + "," + parkId);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        if(leaveFLAG){
            leaveFLAG =false;
            return true;
        }
        else return false;
    }

    public String getUserType(String email){
        client.handleMessageFromClientUI("getUserType-" + email);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        return userType;
    }

    public boolean addComplaint(String complaint, int parkId){
        client.handleMessageFromClientUI("addComplaint-" + userEmail +"," + complaint + ", park"+parkId);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        if(complaintFLAG){
            complaintFLAG =false;
            return true;
        }
        else return false;
    }

    public String [] getComplaints(){
        client.handleMessageFromClientUI("getComplaints-");
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        return complaints;
    }

    public boolean closeComplaint(int complaintId){
        client.handleMessageFromClientUI("closeComplaint-" + complaintId);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        if(closeComplaintFLAG){
            closeComplaintFLAG =false;
            return true;
        }
        else return false;
    }

    public String [] getCarparks(){
        client.handleMessageFromClientUI("getCarparks-");
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        return carparks;
    }

    public int isaMember(int carId, int parkId){
        client.handleMessageFromClientUI("isaMember-" + carId + "," + parkId);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        return memberType;
    }

    public String [] getPriceChangeRequests(){
        client.handleMessageFromClientUI("getPriceChangeRequests-");
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        return priceChangeRequests;
    }

    public boolean approveSetPrices(int requestId, int parkId, int inPlace, int ahead){
        client.handleMessageFromClientUI("approveSetPrices-" + requestId+ "," + parkId + "," + inPlace + "," + ahead);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        if(approveSetPricesFLAG){
            approveSetPricesFLAG =false;
            return true;
        }
        else return false;
    }

    public boolean rejectSetPrices(int requestId){
        client.handleMessageFromClientUI("rejectSetPrices-" + requestId);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        if(rejectSetPricesFLAG){
            rejectSetPricesFLAG =false;
            return true;
        }
        else return false;
    }

    public boolean addPriceChangeRequest(int parkId, int inPlacePrice, int aheadPrice){
        client.handleMessageFromClientUI("addPriceChangeRequest-" + parkId + "," + inPlacePrice + "," + aheadPrice);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        if(addPriceChangeRequestFLAG){
            addPriceChangeRequestFLAG =false;
            return true;
        }
        else return false;
    }

    public String [] getCarparkInfo(int parkId){
        client.handleMessageFromClientUI("getCarparkInfo-" + parkId);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        return carparkInfo;
    }

    public String retrievePastReport(String date) { // format -> [reportType,yyyy/mm]
        String[] archiveDate = date.split("/");
        String dateFormmated = archiveDate[0] + "," + archiveDate[1];
        client.handleMessageFromClientUI("retrievePastReport-" + dateFormmated);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        return this.archiveReport;
    }

    /**
     * @param reportType	[- orders,complaints,corrupted spaces -]
     * @param parkNum		number of the park
     * @return				corresponding report
     */
    public String createParkAdminReport(String reportType,int parkNum) {
        client.handleMessageFromClientUI("createParkAdminReport-" + reportType + "," + parkNum);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        return this.parkAdminReport;
    }

    /**
     * @param reportType	[- period,performance -]
     * @return				corresponding report
     */
    public String createAdminReport(String reportType,int parkNum) {
        client.handleMessageFromClientUI("createAdminReport-" + reportType + "," + parkNum);
        try {
            semaphore.acquire();
        }
        catch (Exception e){
        }
        return this.adminReport;
    }
}
