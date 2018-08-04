package ClientServerMain;

import CarPark.ParkDetails;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ReportBuilder {

    String getSpace(int size){
        String space="";
        for(int i=0; i<size;i++)
            space+=" ";
        return space;
    }

    public void builldSTATISTICSReport(String rawData){
        String [] parse1 = rawData.split("#");
        String [] report = parse1[4].split(";");
        String headLine = "Order status | Calculation Parameter | Amount | Time Interval | Time Issued";
        String [] row1RAW = report[0].split(",");
        String row1 = row1RAW[1] + getSpace("Order status".length()-row1RAW[1].length()+3) + row1RAW[2] + getSpace("Calculation Parameter".length()-row1RAW[2].length()+3) +row1RAW[3] + getSpace("Amount".length()-row1RAW[3].length()+3) +row1RAW[4] + getSpace("Time Interval".length()-row1RAW[4].length()+3) +row1RAW[5];
        String [] row2RAW = report[1].split(",");
        String row2 = row2RAW[1] + getSpace("Order status".length()-row2RAW[1].length()+3) + row2RAW[2] + getSpace("Calculation Parameter".length()-row2RAW[2].length()+3) +row2RAW[3] + getSpace("Amount".length()-row2RAW[3].length()+3) +row2RAW[4] + getSpace("Time Interval".length()-row2RAW[4].length()+3) +row2RAW[5];
        String [] row3RAW = report[2].split(",");
        String row3 = row3RAW[1] + getSpace("Order status".length()-row3RAW[1].length()+3) + row3RAW[2] + getSpace("Calculation Parameter".length()-row3RAW[2].length()+3) +row3RAW[3] + getSpace("Amount".length()-row3RAW[3].length()+3) +row3RAW[4] + getSpace("Time Interval".length()-row3RAW[4].length()+3) +row3RAW[5];
        String [] row4RAW = report[3].split(",");
        String row4 = row4RAW[1] + getSpace("Order status".length()-row4RAW[1].length()+3) + row4RAW[2] + getSpace("Calculation Parameter".length()-row4RAW[2].length()+3) +row4RAW[3] + getSpace("Amount".length()-row4RAW[3].length()+3) +row4RAW[4] + getSpace("Time Interval".length()-row4RAW[4].length()+3) +row4RAW[5];
        try {
            List<String> lines = Arrays.asList(headLine,"",row1,row2,row3,row4);
            Path file = Paths.get(parse1[0]+"_report.txt");
            Files.write(file, lines, Charset.forName("UTF-8"));
            ProcessBuilder pb = new ProcessBuilder("Notepad.exe",parse1[0]+"_report.txt");
            pb.start();
        }catch (Exception e){}
    }

    public void builldPERIODReport(String rawData){
        String [] data = rawData.split("@");
        try {
            List<String> lines = Arrays.asList(data);
            Path file = Paths.get("period_report.txt");
            Files.write(file, lines, Charset.forName("UTF-8"));
            ProcessBuilder pb = new ProcessBuilder("Notepad.exe","period_report.txt");
            pb.start();
        }catch (Exception e){}
    }

    public void builldORDERReport(String rawData){
        String [] data = rawData.split("@");
        try {
            List<String> lines = Arrays.asList(data);
            Path file = Paths.get("order_report.txt");
            Files.write(file, lines, Charset.forName("UTF-8"));
            ProcessBuilder pb = new ProcessBuilder("Notepad.exe","order_report.txt");
            pb.start();
        }catch (Exception e){}
    }


    public void builldCOMPLAINTReport(String rawData){
        String [] data = rawData.split("@");
        try {
            List<String> lines = Arrays.asList(data);
            Path file = Paths.get("complaint_report.txt");
            Files.write(file, lines, Charset.forName("UTF-8"));
            ProcessBuilder pb = new ProcessBuilder("Notepad.exe","complaint_report.txt");
            pb.start();
        }catch (Exception e){}
    }



}
