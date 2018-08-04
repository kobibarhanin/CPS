package scheduler;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import DBController.DBControllerReports;

/**
 * Main Responsibility: System timer operations
 * @author itayguy
 */
public class CPSTimer {
	public static String[] getTime() {
		Calendar calendar = new GregorianCalendar();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		String[] cal = new String[] {new String(year + "-" + month + "-" + day),new String(hour + ":" + minute + ":" + second)};
		return cal;
	}
	
	public static int getCurrentYear() {
		Calendar calendar = new GregorianCalendar();
		return (calendar.get(Calendar.YEAR));
	}
	
	public static int getCurrentMonth() {
		Calendar calendar = new GregorianCalendar();
		return (calendar.get(Calendar.MONTH) + 1);
	}
	
	public static int getCurrentDay() {
		Calendar calendar = new GregorianCalendar();
		return (calendar.get(Calendar.DATE));
	}
	
	public static Timestamp getTimestamp() {
		return Timestamp.valueOf(CPSTimer.getTime()[0] + " " + CPSTimer.getTime()[1] + "");
	}
	
	public static void main(String[] args) {
//		DBControllerReports.UpdateCorrupted("park11",11);
		System.out.println(CPSTimer.getTime()[0] + "," + CPSTimer.getTime()[1]);
	}

	public static int fetchHour(Timestamp time) {
		String[] fullTime = time.toString().split(" ");
		int hour = (int)Double.parseDouble(fullTime[1].split(":")[2]);
		return hour;
	}

	public static int fetchMinute(Timestamp time) {
		String[] fullTime = time.toString().split(" ");
		int hour = Integer.parseInt(fullTime[1].split(":")[1]);
		return hour;
	}

	public static int getCurrentMinute() {
		return (new GregorianCalendar()).get(Calendar.MINUTE);
	}

	public static int getCurrentHour() {
		return (new GregorianCalendar()).get(Calendar.HOUR);
	}
}
