package CarPark;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ParkingSpot {
    public String location;
    public int priority;
    public String stringSchudle;
    public ArrayList<Reservation> schedule = new ArrayList<>();

    public ParkingSpot(String location, int priority, String stringSchudle) {
        this.location = location;
        this.priority = priority;
        this.stringSchudle=stringSchudle;
        parseSchedule(stringSchudle);
    }

    public ParkingSpot() { }

    public void parseSchedule (String stringSchudles){
        if(stringSchudles.equals("0")) return;
        String [] schedule = stringSchudles.split(";");
        for(int i=0;i<schedule.length;i++){
            String [] parts = schedule[i].split(",");
            this.schedule.add(new Reservation(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), parts[2], parts[3]));
        }
    }


    public void reparseSchedule(){
        String newSchedule="";
        for (int i=0; i< schedule.size(); i++){
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            newSchedule += Integer.toString(schedule.get(i).car_id) + "," + schedule.get(i).orderId + "," + dateFormat.format(schedule.get(i).from) + "," + dateFormat.format(schedule.get(i).to) + ";";
        }
        stringSchudle=newSchedule;
    }

}
