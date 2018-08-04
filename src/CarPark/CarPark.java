package CarPark;

import java.util.ArrayList;

public class CarPark {

    public ArrayList<ParkingSpot> p0 = new ArrayList<>();
    public ArrayList<ParkingSpot> p1 = new ArrayList<>();
    public ArrayList<ParkingSpot> p2 = new ArrayList<>();
    public ArrayList<ParkingSpot> p3 = new ArrayList<>();
    public ArrayList<ParkingSpot> p4 = new ArrayList<>();
    public int lineSize;
    public int priorities = 4;
    public ArrayList<ArrayList<ParkingSpot>> carpark = new ArrayList<ArrayList<ParkingSpot>>();

    public ArrayList<ParkingSpot> getPriorityLevel(int level){
        switch (level) {
            case 0:
                return p0;
            case 1:
                return p1;
            case 2:
                return p2;
            case 3:
                return p3;
            case 4:
                return p4;
        }
        return null;
    }

    public CarPark(ArrayList<ParkingSpot> p0, ArrayList<ParkingSpot> p1, ArrayList<ParkingSpot> p2, ArrayList<ParkingSpot> p3, ArrayList<ParkingSpot> p4) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
    }

    public CarPark(){
        carpark.add(p0);
        carpark.add(p1);
        carpark.add(p2);
        carpark.add(p3);
        carpark.add(p4);

    }

    public boolean addOrderToPark(Reservation reservation){
        String loc = "";
        Reservation tmp;
        tmp=tryReserve(p4, reservation);
        if(tmp!=null) tmp=tryReserve(p3, tmp);
        if(tmp!=null) tmp=tryReserve(p2, tmp);
        if(tmp!=null) tmp=tryReserve(p1, tmp);
        if(tmp!=null) tmp=tryReserve(p0, tmp);
        if(tmp!=null) return false;
        return true;
    }

    public Reservation tryReserve(ArrayList<ParkingSpot> priorityLevel, Reservation reservation){
        boolean noResched = false;
        boolean noOverlap = true;
        boolean diff;
        ParkingSpot freeSpot = new ParkingSpot();

        for (ParkingSpot spot : priorityLevel) {
            for (Reservation res : spot.schedule){
                if((reservation.to.before(res.from))||(reservation.from.after(res.to))) diff = true;
                else diff = false;
                noOverlap=noOverlap&&diff;
            }
            if(noOverlap)freeSpot = spot;
            noResched=noResched||noOverlap;
        }
        if(noResched){
            freeSpot.schedule.add(reservation);
            freeSpot.reparseSchedule();
            return null;
        }
        else{
            for (ParkingSpot spot : priorityLevel) {
                for (Reservation res : spot.schedule){
                    if((reservation.from.before(res.from))&&(reservation.to.after(res.to))){
                        Reservation tmp = new Reservation(res.car_id,res.from.toString(),res.to.toString());//CHECK OLD CONSTRUCTOR!!
                        priorityLevel.remove(res);
                        spot.schedule.add(reservation);
                        spot.reparseSchedule();
                        return tmp;
                    }
                }
            }
            return reservation;
        }
    }

    public int getPositionOfOrder(int orderId, int carId){

        for(int i=0; i<this.carpark.size(); i++){
            for(int j=0; j<carpark.get(i).size(); j++){
                for(int k=0; k<carpark.get(i).get(j).schedule.size(); k++){
                    if(carpark.get(i).get(j).schedule.get(k).orderId==orderId  && carpark.get(i).get(j).schedule.get(k).isVaild() && carpark.get(i).get(j).schedule.get(k).car_id==carId){
                        return Integer.parseInt(carpark.get(i).get(j).location);
                    }
                }
            }
        }
        return 0;
    }

    public boolean removeOrderFromPark(int orderId){

        for(int i=0; i<this.carpark.size(); i++){
            for(int j=0; j<carpark.get(i).size(); j++){
                for(int k=0; k<carpark.get(i).get(j).schedule.size(); k++){
                    if(carpark.get(i).get(j).schedule.get(k).orderId==orderId){
                        carpark.get(i).get(j).schedule.remove(k);
                        carpark.get(i).get(j).reparseSchedule();
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public int getOrderId(int carId){

        for(int i=0; i<this.carpark.size(); i++){
            for(int j=0; j<carpark.get(i).size(); j++){
                for(int k=0; k<carpark.get(i).get(j).schedule.size(); k++){
                    if(/*carpark.get(i).get(j).schedule.get(k).isVaild() && */carpark.get(i).get(j).schedule.get(k).car_id==carId){
                        return carpark.get(i).get(j).schedule.get(k).orderId;
                    }
                }
            }
        }
        return 0;
    }
}
