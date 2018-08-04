package scheduler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import CarPark.ParkDetails;
import CarPark.Struct;
import DBController.DBControllerReports;

/**
 * Main Responsibility: Exporting admin image-state
 * @author itayguy
 */
public class CPSAdminReports{
	
	/**
	 * Main Responsibility:	Exporting new image-state file report for the administrator
	 * @param	parkDetailsType	0 = positions status , 1 = statistics details
	 */
	public String getParkStateVector(ParkDetails.type parkDetailsType,int parkNum) {
		String park = "park" + parkNum;
		Map<List<Object>,Map<List<ArrayList<Object>>,List<ArrayList<Object>>>> image = DBControllerReports.makeParkStateVector(park);
		Set<List<Object>> fullSet = image.keySet();
		Object[] set = fullSet.toArray();
		List<ArrayList<ArrayList<Object>>> indicateBits = new ArrayList<ArrayList<ArrayList<Object>>>(); 
		List<ArrayList<ArrayList<Object>>> indicateStatistic = new ArrayList<ArrayList<ArrayList<Object>>>();
		List<Object> parkNames = new ArrayList<Object>();
		for(Object key : set) {
			parkNames.add(key);
			Map<List<ArrayList<Object>>,List<ArrayList<Object>>> innerMap = (Map<List<ArrayList<Object>>,List<ArrayList<Object>>>)image.get(key);
			List<ArrayList<Object>> vector = innerMap.keySet().iterator().next(); //fetch bit vector
			ArrayList<ArrayList<Object>> bits = (ArrayList<ArrayList<Object>>)vector;
			if(parkDetailsType.equals(ParkDetails.type.POSITIONS)) {
				indicateBits.add(bits);
			} else {
				Collection<List<ArrayList<Object>>> moreInnerMap = innerMap.values();
				vector = moreInnerMap.iterator().next(); //fetch statistics vector
				ArrayList<ArrayList<Object>> statistics = (ArrayList<ArrayList<Object>>)vector;
				indicateStatistic.add(statistics);
			}
		}
		if(parkDetailsType.equals(ParkDetails.type.POSITIONS)) {
			return this.parkImageCoding((ArrayList<Object>) parkNames,indicateBits);
		}
		return this.parkImageCoding((ArrayList<Object>) parkNames,indicateStatistic);
	}
	
	/*
	 * delimiters decoding:
	 * # - [park id,heigth,width,depth]
	 * , - items in the same vectoe
	 * ; - between vectors
	 * $ - between different parks
	 * this form is about one park at a time but we can scale it at once
	 */
	private String parkImageCoding(ArrayList<Object> parkNames,List<ArrayList<ArrayList<Object>>> parks) {
		String codingRet = "";
		for(int i = 0;i < parks.size();i++) {
			List<ArrayList<Object>> somePark = parks.get(i);
			ArrayList<Object> optional = ((ArrayList<Object>)parkNames.get(0));
			for(int y = 0;y < optional.size();y++) {
				codingRet +=  optional.get(y).toString() + "#";
			}
			// deep copy of length-1:
			ArrayList<Object> temp = new ArrayList<Object>();
			for(int y = 1;y < parkNames.size();y++) {
				temp.add(parkNames.get(y));
			}
			parkNames = new ArrayList<Object>(temp);
			for(int j = 0;j < somePark.size();j++) {
				Object[] parkPlaces = somePark.get(j).toArray();
				for(int k = 0;k < parkPlaces.length;k++) {
					codingRet += parkPlaces[k];
					if(k < parkPlaces.length-1) {
						codingRet += ",";
					}					
				}
				if(j < somePark.size()-1) {
					codingRet += ";";
				}
			}
			if(i < parks.size()-1) {
				codingRet += "$";
			}
		}
		return codingRet;
	}

	/**
	 * Period report for network admin
	 * @throws IOException 
	 */
	public String buildPeriodReport() throws IOException {
		Map<String,List<ArrayList<Object>>> map = DBControllerReports.getPeriodReport();
		if(map == null || map.isEmpty()) {
			System.out.println("Period data does not exist yet.");
			return null;
		}
		Set<String> set = map.keySet();
		String context = "----------------Period Report:----------------@";
		for(String key : set) {
			try {
				context += this.contextPeriodReportBuilder(key,map.get(key)) + "@";
			}catch(Exception ex) {
				ex.getStackTrace();
				return null;
			}
		}
		context += "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$@";
		File report = null;
		try {
			report = this.generateReport(Struct.report.PERIOD.toString().toLowerCase(),context);
			if(report != null) {
				System.out.println("Period report has been exported to a file.");
			}
		}catch(Exception ex) {ex.printStackTrace();}
		DBControllerReports.auditExportTimeAt(Struct.report.PERIOD,CPSTimer.getTimestamp());
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
	 * fetching the report data from DB, the its correctness and build the report
	 * optional function to use as global park knowledge
	 * @throws IOException 
	 */
	public String buildImageStateReport() throws IOException {
		List<ArrayList<Object>> imageParkState = DBControllerReports.getImageParksState();
		if(imageParkState.isEmpty() || (imageParkState.size() > 1) && ((ArrayList<Object>)imageParkState.get(1)).isEmpty()) {
			System.out.println("Image state is not exist yet.");
			return null;
		}
		File report = null;
		try {
			String context = this.contextImageStateBuilder(imageParkState);
			report = this.generateReport(Struct.report.IMAGE_STATE.toString().toLowerCase(),context);
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}
		if(report != null) {
			System.out.println("Image state report has been exported to a file.");
		}
		DBControllerReports.auditExportTimeAt(Struct.report.IMAGE_STATE,CPSTimer.getTimestamp());
		List<String> reportLines = Files.readAllLines(report.toPath());
		String ret = "";
		for(String line : reportLines) {
			for(int i = 0;i < line.length();i++) {
				ret += line.charAt(i);
			}
		}
		return ret;
	}
	
	public List<Integer> getPerformacePair() {
		return DBControllerReports.getPerformacePair();
	}
	
	/*
	 * file report designer
	 */
	private String contextPeriodReportBuilder(String key,List<ArrayList<Object>> stats) {
		String body = key + ":";	
		body += "@############@";
		for(int i = 0;i < stats.size();i++) {
			body += "From: " + stats.get(i).get(0).toString() + "@";
			body += "Parameter: " + stats.get(i).get(1).toString() + "@";
			body += "Method: " + stats.get(i).get(2).toString() + "@";
			body += "Result: " + stats.get(i).get(3).toString() + "@";
			body += "Each: " + stats.get(i).get(4).toString() + "@";
			body += "TimeSampling: " + stats.get(i).get(5).toString() + "@";
			if(i != stats.size()-1) {
				body += "@-------------------------------------------------------@";
			}
		}
		return body;
	}
	
	/*
	 * file report designer
	 */
	private String contextImageStateBuilder(List<ArrayList<Object>> imageParkState) {
		String body = "Status Image:";
		body += "\n##########################\n";
		body += "\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n";
		for(int i = 0;i < imageParkState.size();i++) {
			body += "Park number:" + imageParkState.get(i).get(0).toString() + "\n";
			body += "Free space amount:" + imageParkState.get(i).get(1).toString() + "\n";
			body += "Resided space amount:" + imageParkState.get(i).get(2).toString() + "\n";
			if(i != imageParkState.size()-1) {
				body += "\n-------------------------------------------------------\n";
			}
		}
		body += "\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n";
		return body;
	}
	
	/**
	 * Main Responsibility:	generat new report file into the folder tree structure to use as archive
	 * @param context		context to write into the file
	 * @return				new File the written already to the file-system
	 * @throws Exception
	 */
	public File generateReport(String reportTypeName,String context) throws Exception {
		String body = context;
		String codeLocation = (new File(CPSReportsExporter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())).toPath().toString();
		Path reportsFolder = Paths.get(codeLocation + "\\reports");
		Path reportRole = Paths.get(reportsFolder.toString() + "\\" + reportTypeName);
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
	public static void main(String[] args) throws IOException {
//		CPSAdminReports cpsTimer = new CPSAdminReports();
//		//cpsTimer.buildPeriodReport();
//		//String ret1 = cpsTimer.getParkStateVector(ParkDetails.type.POSITIONS,11);
//		//System.out.println(ret1);
//		String ret2 = cpsTimer.getParkStateVector(ParkDetails.type.STATISTICS,11);
//		System.out.println(ret2);
		System.out.println();
	}
}
