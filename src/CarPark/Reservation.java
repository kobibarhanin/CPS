package CarPark;

import java.text.ParseException;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reservation {
    int car_id;
    Date from = new Date();
    Date to = new Date();
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public int orderId;

    public Reservation(int car_id, String from, String to) {
        this.car_id = car_id;
        try {
            this.from = dateFormat.parse(from);
            this.to = dateFormat.parse(to);
        }catch (ParseException ex){}
    }

    public Reservation(int car_id, int  orderId, String from, String to) {
        this.car_id = car_id;
        this.orderId = orderId;
        try {
            this.from = dateFormat.parse(from);
            this.to = dateFormat.parse(to);
        }catch (ParseException ex){}
    }

    public int getCar_id() {
        return car_id;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    public boolean isVaild(){
        Date now = new Date();
        if(now.after(from) && now.before(to)) return true;
        else return false;
    }
}
