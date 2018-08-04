package ClientServerMain;

import CarPark.CPSFS;
import CarPark.ParkDetails;
import CarPark.Struct;
import DBController.DBControllerCarPark;
import DBController.DBControllerMain;
import DBController.DBControllerUsers;
import scheduler.CPSAdminReports;
import scheduler.CPSReportsExporter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

public class ServerHandler {

    private String cmd;
    private String input;
    private String toClient;
    private String toServerConsole;
    private StackTraceElement exc[];
    private boolean quit;

    public ServerHandler(String message) {
        input = parseInput(message);
        cmd = parseCommand(message);
        toClient = "Undefined Command";
        toServerConsole = "";
        quit = false;
    }

    public static String parseCommand(String message) {
        String[] tmpCmd = message.split("-", 2);
        return tmpCmd[0];
    }

    public static String parseInput(String message) {
        String[] tmpCmd = message.split("-", 2);
        if (tmpCmd[1].equals("")) return message;
        else return tmpCmd[1];
    }

    public String respond() {
        return toClient;
    }

    public String print() {
        return toServerConsole;
    }

    public boolean quit() {
        return quit;
    }

    public String handle() {

        if (cmd.equals("printToServer")) {
            toClient = toServerConsole = input;
        }

        if (cmd.equals("buildDb")) {
            toClient = toServerConsole = "Building Data Base.";
            try {
                DBControllerMain.buildDb();
            } catch (SQLException e) {
                exc = e.getStackTrace();
            }
        }

        if (cmd.equals("quit")) {
            toServerConsole = "Client has terminated the connection. Bye bye.";
            quit = true;
        }


        if (cmd.equals("addUser")) {
            String[] parts = input.split(",");
            if (DBControllerUsers.addUser(parts[0], parts[1], parts[2], parts[3])) {
                toClient = "addUser-true";
                toServerConsole = input + " successfully added to Data Base.";
            } else {
                toClient = "addUser-false";
                toServerConsole = "Failed to add " + input +
                        ". email " + parts[3] + " already exists in Data Base.";
            }
        }

        if (cmd.equals("verifyUser")) {
            String[] parts = input.split(",");
            if (DBControllerUsers.verUser(parts[0], parts[1])) {
                toClient = "verified";
                toServerConsole = input + " Verified.";
            } else {
                toClient = "notVerified";
                toServerConsole = input + " Not-Verified.";
            }
        }

        if (cmd.equals("getUserName")) {
            String uNm = DBControllerUsers.getUserName(input);
            toClient = "userName-" + uNm;
            toServerConsole = "userName is: " + uNm;
        }

        if (cmd.equals("getUserType")) {
            String userType = DBControllerUsers.getUserType(input);
            toClient = "userType-" + userType;
            toServerConsole = "userType is: " + userType;
        }


        if (cmd.equals("addCar")) {
            String[] parts = input.split(",");
            if (DBControllerUsers.addCar(parts[0], parts[1])) {
                toClient = "addCar-true";
                toServerConsole = "car: " + input + " added successfully";
            } else {
                toClient = "addCar-false";
                toServerConsole = "addCar: " + input + "failed";
            }
        }


        if (cmd.equals("getCars")) {
            String cars = DBControllerUsers.getCars(input);
            toClient = "carList-" + cars;
            toServerConsole = "cars for user " + input + " are: " + cars;
        }

        if (cmd.equals("addOrder")) {
            String[] parameters = input.split(",");
            if (DBControllerCarPark.addOrder(parameters)) {
                toClient = "order-true";
                toServerConsole = "Order: " + input + " added successfully";

            } else {
                toClient = "order-false";
                toServerConsole = "Order failed";

            }
        }

        if (cmd.equals("cancelOrder")) {
            if (DBControllerCarPark.cancelOrder(Integer.parseInt(input))) {
                toClient = "cancelOrder-true";
                toServerConsole = "Order: " + input + " canceled successfully";
            } else {
                toClient = "cancelOrder-false";
                toServerConsole = "Order cancel failed";
            }
        }

        if (cmd.equals("addFullMembership")) {
            String[] parameters = input.split(",");
            if (DBControllerUsers.addFullmembership(parameters[0], parameters[1])) {
                toClient = "addFullMember-true";
                toServerConsole = "FullMember: " + input + " added successfully";

            } else {
                toClient = "addFullMember-false";
                toServerConsole = "addFullMember failed";
            }
        }

        if (cmd.equals("addRegMembership")) {
            String[] parameters = input.split(",");
            if (DBControllerUsers.addRegmembership(parameters[0], parameters[1], parameters[2], parameters[3])) {
                toClient = "addRegMember-true";
                toServerConsole = "RegMember: " + input + " added successfully";

            } else {
                toClient = "addRegMember-false";
                toServerConsole = "addRegMember failed";
            }
        }


        if (cmd.equals("getFullmemberships")) {
            String fullmemberships = DBControllerUsers.getFullmembership(input);
            toClient = "Full Memberships-" + fullmemberships;
            toServerConsole = "Full memberships: " + fullmemberships;
        }

        if (cmd.equals("getRegmemberships")) {
            String regmemberships = DBControllerUsers.getRegmembership(input);
            toClient = "Regular Memberships-" + regmemberships;
            toServerConsole = "Regular Memberships: " + regmemberships;
        }

        if (cmd.equals("getOrders")) {
            String orders = DBControllerCarPark.getOrders(input);
            toClient = "Orders-" + orders;
            toServerConsole = "Orders: " + orders;
        }

        if (cmd.equals("calcCost")) {
            double cost = DBControllerCarPark.calcCost(Integer.parseInt(input));
            toClient = "Cost-" + cost;
            toServerConsole = "Cost: " + cost;
        }

        if (cmd.equals("enterOrder")) {
            String[] parameters = input.split(",");
            if (DBControllerCarPark.insertCarToParkOrder(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]))) {
                toClient = "enterOrder-true";
                toServerConsole = "enterOrder: " + input + " successfull";
            } else {
                toClient = "enterOrder-false";
                toServerConsole = "enterOrder " + input + " failed";
            }
        }

        if (cmd.equals("enterMember")) {
            String[] parameters = input.split(",");
            if (DBControllerCarPark.insertCarToParkMember(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]), parameters[3])) {
                toClient = "enterMember-true";
                toServerConsole = "enterMember: " + input + " successfull";
            } else {
                toClient = "enterMember-false";
                toServerConsole = "enterMember " + input + " failed";
            }
        }

        if (cmd.equals("leave")) {
            String[] parameters = input.split(",");
            if (DBControllerCarPark.removeCarFromPark(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]))) {
                toClient = "leave-true";
                toServerConsole = "leave: " + input + " successfull";
            } else {
                toClient = "leave-false";
                toServerConsole = "leave " + input + " failed";
            }
        }

        if (cmd.equals("addComplaint")) {
            String[] parameters = input.split(",");
            if (DBControllerUsers.addComplaint(parameters[0], parameters[1],parameters[2])) {
                toClient = "addComplaint-true";
                toServerConsole = "complaint: " + input + "added successfully";
            } else {
                toClient = "addComplaint-false";
                toServerConsole = "complaint " + input + "add failed";
            }
        }

        if (cmd.equals("getComplaints")) {
            String complaints = DBControllerUsers.getComplaints();
            toClient = "getComplaints-" + complaints;
            toServerConsole = "getComplaints: " + complaints;
        }


        if (cmd.equals("closeComplaint")) {
            if (DBControllerUsers.closeComplaint(Integer.parseInt(input))) {
                toClient = "closeComplaint-true";
                toServerConsole = "closeComplaint: " + input + " successfull";
            } else {
                toClient = "closeComplaint-false";
                toServerConsole = "closeComplaint " + input + " failed";
            }
        }

        if (cmd.equals("getCarparks")) {
            String carparks = DBControllerCarPark.getCarparks();
            toClient = "getCarparks-" + carparks;
            toServerConsole = "getCarparks: " + carparks;
        }

        if (cmd.equals("isaMember")) {
            String[] parameters = input.split(",");
            int memberType = DBControllerUsers.isaMember(parameters[0], parameters[1]);
            toClient = "isaMember-" + memberType;
            toServerConsole = "isaMember is: " + memberType;
        }

        if (cmd.equals("getPriceChangeRequests")) {
            String priceChangeRequests = DBControllerCarPark.getPriceChangeRequests();
            toClient = "getPriceChangeRequests-" + priceChangeRequests;
            toServerConsole = "getPriceChangeRequests: " + priceChangeRequests;
        }


        if (cmd.equals("approveSetPrices")) {
            String[] parameters = input.split(",");
            if (DBControllerCarPark.approveSetPrices(parameters[0], parameters[1], parameters[2], parameters[3])) {
                toClient = "approveSetPrices-true";
                toServerConsole = "approveSetPrices: " + input + " successfull";
            } else {
                toClient = "approveSetPrices-false";
                toServerConsole = "approveSetPrices " + input + " failed";
            }
        }

        if (cmd.equals("rejectSetPrices")) {
            if (DBControllerCarPark.rejectSetPrices(input)) {
                toClient = "rejectSetPrices-true";
                toServerConsole = "rejectSetPrices: " + input + " successfull";
            } else {
                toClient = "rejectSetPrices-false";
                toServerConsole = "rejectSetPrices " + input + " failed";
            }
        }

        if (cmd.equals("addPriceChangeRequest")) {
            String[] parameters = input.split(",");
            if (DBControllerCarPark.addPriceChangeRequest(parameters[0], parameters[1], parameters[2])) {
                toClient = "addPriceChangeRequest-true";
                toServerConsole = "addPriceChangeRequest: " + input + " successfull";
            } else {
                toClient = "addPriceChangeRequest-false";
                toServerConsole = "addPriceChangeRequest " + input + " failed";
            }
        }

        if (cmd.equals("getCarparkInfo")) {
            String carparkInfo = DBControllerCarPark.getCarparkInfo(input);
            toClient = "getCarparkInfo-" + carparkInfo;
            toServerConsole = "getCarparkInfo: " + carparkInfo;
        }

        if (cmd.equals("createAdminReport")) {
            input = input.trim().toLowerCase();
            String[] parameters = input.split(",");
            String reportName = parameters[0];
            int parkNum = Integer.parseInt(parameters[1]);
            String report = this.createAdminReport(reportName, parkNum);
            if (report != null) {
                toClient = "createAdminReport-" + report;
                toServerConsole = "createAdminReport: report has been created.";
            } else {
                toClient = "createAdminReport-report has not created due to some problems.";
                toServerConsole = "createAdminReport: report has not created due to some problems.";
            }
        }

        if (cmd.equals("createParkAdminReport")) {
            input = input.trim().toLowerCase();
            String[] parameters = input.split(",");
            String reportName = parameters[0];
            int parkNum = Integer.parseInt(parameters[1]);
            String report = this.createParkAdminReport(reportName, parkNum);
            if (report != null) {
                toClient = "createParkAdminReport-" + report;
                toServerConsole = "createParkAdminReport: report has been created.";
            } else {
                toClient = "createParkAdminReport-report has not created due to some problems.";
                toServerConsole = "createParkAdminReport: report has not created due to some problems.";
            }
        }

        if (cmd.equals("retrievePastReport")) {
            input = input.trim().toLowerCase();
            String[] parameters = input.split(",");
            int reportYear = Integer.parseInt(parameters[0]);
            int reportMonth = Integer.parseInt(parameters[1]);
            try {
                String report = this.retrievePastReport(reportYear, reportMonth);
                if (report != null) {
                    toClient = "retrievePastReport-" + report;
                    toServerConsole = "retrievePastReport: report has been retrieved.";
                } else {
                    toClient = "retrievePastReport-report has not exist yet or not created due to some problems.";
                    toServerConsole = "retrievePastReport: report has not exist yet or not created due to some problems.";
                }
            } catch (Exception ex) {
                toClient = "retrievePastReport-report has not exist yet or not created due to some problems.";
                toServerConsole = "retrievePastReport: report has not exist yet or not created due to some problems.";
            }
        }

        return toClient;
    }

    private String retrievePastReport(int reportYear, int reportMonth) throws Exception {
        String periodName = Struct.report.PERIOD.toString().toLowerCase();
        String report = null;
        Path sourcePath = (new File(ServerHandler.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())).toPath();
        Path reportsFolder = Paths.get(sourcePath + "\\" + CPSFS.tree.REPORTS.toString().toLowerCase() + "\\" + periodName + "\\" + reportYear + "\\" + reportMonth + ".txt");
        if (Files.exists(reportsFolder)) {
            report = "";
            List<String> reportLines = Files.readAllLines(reportsFolder);
            for (String line : reportLines) {
                for (int i = 0; i < line.length(); i++) {
                    report += line.charAt(i);
                }
            }
        }
        return report;
    }

    private String createParkAdminReport(String reportName, int parkNum) {
        String orders = Struct.report.ORDER.toString().toLowerCase();
        String complaints = Struct.report.COMPLAINT.toString().toLowerCase();
        String corrupted = Struct.report.CORRUPTED.toString().toLowerCase();
        String retVal = "";
        try {
            if (reportName.equals(orders)) {
                retVal = this.getOrdersReport(parkNum); // image state
            } else if (reportName.equals(complaints)) {
                retVal = this.getComplaintsReport(parkNum); // period
            } else if (reportName.equals(corrupted)) {
                retVal = this.getCorruptedReport(parkNum); // image state
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return retVal;
    }

    /*
     * park manager
     */
    private String getOrdersReport(int parkNum) throws IOException {
        CPSReportsExporter reports = new CPSReportsExporter();
        return reports.buildOrdersReport(parkNum);
    }

    /*
     * park manager
     */
    private String getComplaintsReport(int parkNum) throws IOException {
        CPSReportsExporter reports = new CPSReportsExporter();
        return reports.buildComplaintsReport(parkNum);
    }


    /*
     * network admin
     */
    private String getCorruptedReport(int parkNum) throws IOException {
        CPSReportsExporter reports = new CPSReportsExporter();
        return reports.buildCorruptedSpacesReport(parkNum);
    }

    /*
     * network admin
     */
    private String getPeriodReport() throws IOException {
        CPSAdminReports adminReport = new CPSAdminReports();
        return adminReport.buildPeriodReport();
    }

    /*
     * network admin
     */
    private String getPerformancePair() {
        String ret = null;
        try {
            CPSAdminReports adminReport = new CPSAdminReports();
            List<Integer> pair = adminReport.getPerformacePair();
            ret = pair.get(0) + "," + pair.get(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    /*
     * creating two optional admin report from server
     */
    private String createAdminReport(String reportType, int parkNum) {
        CPSAdminReports adminReport = new CPSAdminReports();
        String pos = ParkDetails.type.POSITIONS.toString().toLowerCase();
        String stat = ParkDetails.type.STATISTICS.toString().toLowerCase();
        String period = ParkDetails.type.PERIOD.toString().toLowerCase();
        String performance = ParkDetails.type.PERFORMANCE.toString().toLowerCase();
        String retVal = "";
        try {
            adminReport.buildImageStateReport();
            if (reportType.equals(pos)) {
                retVal = adminReport.getParkStateVector(ParkDetails.type.POSITIONS, parkNum); // image state
            } else if (reportType.equals(stat)) {
                retVal = adminReport.getParkStateVector(ParkDetails.type.STATISTICS, parkNum); // period
            } else if (reportType.equals(period)) {
                retVal = this.getPeriodReport();
            } else if (reportType.equals(performance)) {
                retVal = this.getPerformancePair();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return retVal;
    }
}