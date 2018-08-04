package scheduler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import CarPark.CPSFS;
import CarPark.Customer;
import CarPark.Struct;
import DBController.DBControllerReports;

/**
 * Main Responsibility: Exporting variety reports to the administrator
 * @author itayguy
 */
public class CPSReportsExporter {
	/**
	 * build corrupted spaces report
	 * @throws IOException 
	 */
	public String buildCorruptedSpacesReport(int parkNum) throws IOException {
		String parkName = "park" + parkNum;
		List<ArrayList<Object>> corruptedSpaces = DBControllerReports.getCorruptedSpaces(parkName);
		DBControllerReports.resetCorruptedSpacesEachFiscalQuarter(parkName); //reset the number each fiscal period
		if(corruptedSpaces.isEmpty() || (corruptedSpaces.size() > 1) && ((ArrayList<Object>)corruptedSpaces.get(1)).isEmpty()) {
			System.out.println("No corrupted spaces are exists.");
			return null;
		}
		File report = null;
		try {
			String context = this.contextCorruptedSpacesBuilder(corruptedSpaces);
			report = this.generateReport(CPSFS.tree.CORRUPTED,context);
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}
		if(report == null) {
			return null;
		}
		System.out.println("Corrupted spaces report has been exported to a file.");
		DBControllerReports.auditExportTimeAt(Struct.report.CORRUPTED,CPSTimer.getTimestamp());
		List<String> reportLines = Files.readAllLines(report.toPath());
		String ret = "";
		for(String line : reportLines) {
			for(int i = 0;i < line.length();i++) {
				ret += line.charAt(i);
			}
		}
		return ret;
	}

	/**
	 * build customer complaints report 
	 * @throws IOException 
	 */
	public String buildComplaintsReport(int parkNum) throws IOException {
		String parkName = "park" + parkNum;
		List<ArrayList<Object>> customerComplaints = DBControllerReports.getCustomerComplaints(parkName);
		if(customerComplaints.isEmpty() || (customerComplaints.size() > 1) && ((ArrayList<Object>)customerComplaints.get(1)).isEmpty()) {
			System.out.println("No customers are exists.");
			return null;
		}
		File report = null;
		try {
			String context = this.contextComplaintBuilder(customerComplaints);
			report = this.generateReport(CPSFS.tree.COMPLAINT,context);
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}
		if(report == null) {
			return null;
		}
		System.out.println("Customer complaints report has been exported to a file.");
		DBControllerReports.auditExportTimeAt(Struct.report.COMPLAINT,CPSTimer.getTimestamp());
		List<String> reportLines = Files.readAllLines(report.toPath());
		String ret = "";
		for(String line : reportLines) {
			for(int i = 0;i < line.length();i++) {
				ret += line.charAt(i);
			}
		}
		return ret;
	}

	/**
	 * build order exceeding report 
	 * @throws IOException 
	 */
	public String buildOrdersReport(int parkNum) throws IOException {
		Map<String,ArrayList<ArrayList<Object>>> customerOrders = DBControllerReports.getCustomerOrders(parkNum);
		
		if(customerOrders.isEmpty() || (customerOrders == null)) {
			System.out.println("No customers are exists.");
			return null;
		}
		File report = null;
		try {
			String contextAhead = this.contextOrderBuilder(customerOrders.get(Customer.orderState.AHEAD.toString()),Customer.orderState.AHEAD.toString());
			String contextInplace = this.contextOrderBuilder(customerOrders.get(Customer.orderState.INPLACE.toString()),Customer.orderState.INPLACE.toString());
			String context = contextAhead + "@" + contextInplace;
			report = this.generateReport(CPSFS.tree.ORDER,context);
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}
		if(report == null) {
			return null;
		}
		System.out.println("Customer orders report has been exported to a file.");
		DBControllerReports.auditExportTimeAt(Struct.report.ORDER,CPSTimer.getTimestamp());
		List<String> reportLines = Files.readAllLines(report.toPath());
		String ret = "";
		for(String line : reportLines) {
			for(int i = 0;i < line.length();i++) {
				ret += line.charAt(i);
			}
		}
		return ret;
	}
	
	/*
	 * file report designer
	 */
	private String contextCorruptedSpacesBuilder(List<ArrayList<Object>> customers) {
		String body = "\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n";
		body += "Corrupted placed:";
		body += "\n##########################\n";
		for(int i = 0;i < customers.size();i++) {
			body += "Park name:" + customers.get(i).get(0).toString() + "\n";
			body += "Corrupted places amount in fiscal quarter:" + customers.get(i).get(1).toString() + "\n";
			if(i != customers.size()-1) {
				body += "\n----------------------------------------------------\n";
			}
		}
		body += "\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n";
		return body;
	}
	
	/*
	 * file report designer
	 */
	private String contextComplaintBuilder(List<ArrayList<Object>> customers) {
		String body = "@$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$@";
		body += "Complaints Report:";
		body += "@##########################@";
		body += "@----------------------------------------------------@";
		for(int i = 0;i < customers.size();i++) {
			body += "Customer I.D.:" + customers.get(i).get(0).toString() + "@";
			body += "Email address:" + customers.get(i).get(1).toString() + "@";
			body += "Open at:" + customers.get(i).get(2).toString() + "@";
			body += "Close at:" + customers.get(i).get(3).toString() + "@";
			body += "Complaint refunding in $:" + customers.get(i).get(4).toString() + "@";
			body += "Customer freetext message:" + customers.get(i).get(5).toString() + "@";
			body += "Complaint status:" + customers.get(i).get(6).toString();
			if(i != customers.size()-1) {
				body += "@----------------------------------------------------@";
			}
		}
		body += "@$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$@";
		return body;
	}
	
	/*
	 * file report designer
	 */
	private String contextOrderBuilder(List<ArrayList<Object>> customers,String customerType) {
		String body = "Orders Report:";
		body += "@##########################@";
		body += "@$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$@";
		body += customerType;
		body += ":@----------------------------------------------------@";
		for(int i = 0;i < customers.size();i++) {
			body += "Customer I.D.:" + customers.get(i).get(0).toString() + "@";
			body += "Car I.D.:" + customers.get(i).get(1).toString() + "@";
			body += "Email address:" + customers.get(i).get(2).toString() + "@";
			body += "Time in:" + customers.get(i).get(3).toString() + "@";
			body += "Time out:" + customers.get(i).get(4).toString() + "@";
			body += "Park I.D.:" + customers.get(i).get(5).toString() + "@";
			body += "Order status:" + customers.get(i).get(6).toString();
			if(i != customers.size()-1) {
				body += "@----------------------------------------------------@";
			}
		}
		body += "@$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$@";
		return body;
	}
	
	/**
	 * Main Responsibility:	generat new report file into the folder tree structure to use as archive
	 * @param reportLabel	label to determine the folder partition name
	 * @param context		context to write into the file
	 * @return				new File the written already to the file-system
	 * @throws Exception
	 */
	public File generateReport(CPSFS.tree entryName,String context) throws Exception {
		String body = context;
		String codeLocation = (new File(CPSReportsExporter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())).toPath().toString();
		Path reportsFolder = Paths.get(codeLocation + "\\" + CPSFS.tree.REPORTS.toString().toLowerCase());
		Path reportRole = Paths.get(reportsFolder.toString() + "\\" + entryName.toString().toLowerCase());
		Path reportYear = Paths.get(reportRole.toString() + "\\" + CPSTimer.getCurrentYear());
		Path reportMonth = Paths.get(reportYear.toString() + "\\" + CPSTimer.getCurrentMonth() + ".txt");
		if(Files.notExists(reportsFolder)) {
			Files.createDirectory(reportsFolder);
		}
		if(Files.notExists(reportRole)) {
			Files.createDirectory(reportRole);
		}
		if(Files.notExists(reportYear)) {
			Files.createDirectory(reportYear);
		}
		if(Files.notExists(reportMonth)) {
			Files.createFile(reportMonth);
		}
		Files.write(reportMonth, body.getBytes());
		return new File(reportMonth.toString());
	}
	
	/*
	 * TODO: remove it when tests are finished
	 */
	public static void main(String[] args) throws Exception {
		CPSReportsExporter orderReport = new CPSReportsExporter();
		String ret1 = orderReport.buildOrdersReport(11);
		String ret2 = orderReport.buildComplaintsReport(11);
		String ret3 = orderReport.buildCorruptedSpacesReport(11);
	}
}
