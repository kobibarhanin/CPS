package scheduler;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import CarPark.CPSFS;
import CarPark.Struct;
import CarPark.Struct.CorruptPeriod;
import DBController.DBControllerReports;

/**
 * Main Responsibility: Exporting exceeding customer file reports
 * @author itayguy
 */
public class CPSExceedingNotifier extends Timer {
	//1800000 = 30 minutes
	private static final String CPS_SCHED_NAME = "CPSExceedingScheduler";
	private static final long DEFAULT_TIME_UNIT = 1000;
	private static int lastHourSample = 0;
	private long timePeriod;
	
	/**
	 * Main Responsibility: Initial new CPSTImer. [- period between jobs checking is 1 minutes and no daemon is used by default -]
	 * //@param isDeamon		true if the application need CPSTimer as a deamon process,false otherwise
	 */
	public CPSExceedingNotifier() {
		this(CPSExceedingNotifier.DEFAULT_TIME_UNIT);
	}
	
	/**
	 * Main Responsibility: Initial new CPSTImer
	 * @param timePeriod	time period between jobs in seconds
	 */
	public CPSExceedingNotifier(long timePeriod) {
		super(CPSExceedingNotifier.CPS_SCHED_NAME,false);
		this.timePeriod = timePeriod*1000;
	}
	
	/**
	 * Main Responsibility: Initial new scheduler that can wake up every period and do some job [- start scheduling at execution time -]
	 * 						check about exceeding customers to their reservation and notify them using email sending
	 */
	public void startSchedule() {
		this.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				System.out.println("verifying...");
				int currMinute = CPSTimer.getCurrentMinute();
				if(currMinute != CPSExceedingNotifier.lastHourSample && (currMinute == 0 || currMinute == 30)){
					CPSExceedingNotifier.lastHourSample = currMinute;
					List<ArrayList<Object>> customers = DBControllerReports.getCustomerOrdersExceeding();
					if(customers == null || customers.isEmpty()) {
						System.out.println("No customers are exceeding.");
					} else {
						for(int i = 0;i < customers.size();i++) {
							String context = CPSExceedingNotifier.contextCustomerExceeding(customers.get(i));
							File report = null;
							try {
								report = CPSExceedingNotifier.generateReport(customers.get(i).get(2).toString(),customers.get(i).get(0).toString(),context);
							} catch (Exception e) { /* need to catch nothing here */ }
							if(report != null) {
								System.out.println("Customer exceeding report has been exported to a file.");
							}
							try {
								Mailer mailer = new Mailer(report, customers.get(i).get(2).toString());
								mailer.send(); // multi-threaded implementation
							}catch(Exception ex){System.out.println(ex.getMessage()); }
						}
						System.out.println("All exceeding emails have been built.");
						DBControllerReports.testAndSetOrderStatus();
						DBControllerReports.auditExportTimeAt(Struct.report.EXCEEDING,CPSTimer.getTimestamp());
					}
				}
			}
		},0,this.timePeriod);
	}
	
	/*
	 * file report designer
	 */
	private static String contextCustomerExceeding(ArrayList<Object> customer) {
		String body = "\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n";
		body += "Id:" + customer.get(0).toString() + "\n";
		body += "Car Id:" + customer.get(1).toString() + "\n";
		body += "Email:" + customer.get(2).toString() + "\n";
		body += "Time In:" + customer.get(3).toString() + "\n";
		body += "Time Out:" + customer.get(4).toString() + "\n";
		body += "Park Id:" + customer.get(5).toString() + "\n";
		body += "Status:" + customer.get(6).toString() + "\n";
		body += "This message is to notify you that you have 30 minutes to take the reservation or to cancel it using return email.";
		body += "\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n";
		return body;
	}
	
	public static File generateReport(String userid,String identifier,String context) throws Exception {
		String body = context;
		String codeLocation = (new File(CPSReportsExporter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())).toPath().toString();
		Path reportsFolder = Paths.get(codeLocation + "\\" + CPSFS.tree.REPORTS.toString().toLowerCase());
		Path reportRole = Paths.get(reportsFolder.toString() + "\\" + CPSFS.tree.CUSTOMER_PRIVACY_EXCEEDING.toString().toLowerCase());
		Path userFolder = Paths.get(reportRole.toString() + "\\" + userid);
		Path reportYear = Paths.get(userFolder.toString() + "\\" + CPSTimer.getCurrentYear());
		Path reportMonth = Paths.get(reportYear.toString() + "\\" + CPSTimer.getCurrentMonth());
		Path reportDay = Paths.get(reportMonth.toString() + "\\" + CPSTimer.getCurrentDay());
		Path reportTime = Paths.get(reportDay.toString() + "\\" + identifier + "_" + CPSTimer.getTime()[1].replaceAll(":","-") + ".txt");
		if(Files.notExists(reportsFolder)) {
			Files.createDirectory(reportsFolder);
		}
		if(Files.notExists(reportRole)) {
			Files.createDirectory(reportRole);
		}
		if(Files.notExists(userFolder)) {
			Files.createDirectory(userFolder);
		}
		if(Files.notExists(reportYear)) {
			Files.createDirectory(reportYear);
		}
		if(Files.notExists(reportMonth)) {
			Files.createDirectory(reportMonth);
		}
		if(Files.notExists(reportDay)) {
			Files.createDirectory(reportDay);
		}
		if(Files.notExists(reportTime)) {
			Files.createFile(reportTime);
		}
		Files.write(reportTime, body.getBytes());
		return new File(reportTime.toString());
	}

	/*
	 * TODO: remove it when tests are finished
	 */
	public static void main(String[] args) {
		(new CPSExceedingNotifier()).startSchedule();
		//CPSExceedingNotifier cpsTimer = new CPSExceedingNotifier();
		//cpsTimer.startSchedule();
		//cpsTimer.cancel();
		//cpsTimer.purge();
	}
}