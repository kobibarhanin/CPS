package Screens;

import javafx.beans.property.SimpleStringProperty;

public class OrderTuple {
    public final SimpleStringProperty OrderId = new SimpleStringProperty();
    private final SimpleStringProperty CarId = new SimpleStringProperty();
    private final SimpleStringProperty time_in = new SimpleStringProperty();
    private final SimpleStringProperty time_out = new SimpleStringProperty();
    private final SimpleStringProperty CarParkId = new SimpleStringProperty();
    private final SimpleStringProperty status = new SimpleStringProperty();

    public OrderTuple() {
        this("","","","","","");
    }

    public OrderTuple(String id, String car_id, String time_in, String time_out, String park_id, String status) {
        setId(id);
        setCarId(car_id);
        setTime_in(time_in);
        setTime_out(time_out);
        setCarParkId(park_id);
        setStatus(status);
    }

    public void setId(String id) {
        this.OrderId.set(id);
    }

    public void setCarId(String carId) {
        this.CarId.set(carId);
    }

    public void setTime_in(String time_in) {
        this.time_in.set(time_in);
    }

    public void setTime_out(String time_out) {
        this.time_out.set(time_out);
    }

    public void setCarParkId(String carParkId) {
        this.CarParkId.set(carParkId);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getId() {
        return OrderId.get();
    }

    public SimpleStringProperty idProperty() {
        return OrderId;
    }

    public String getCarId() {
        return CarId.get();
    }

    public SimpleStringProperty carIdProperty() {
        return CarId;
    }

    public String getTime_in() {
        return time_in.get();
    }

    public SimpleStringProperty time_inProperty() {
        return time_in;
    }

    public String getTime_out() {
        return time_out.get();
    }

    public SimpleStringProperty time_outProperty() {
        return time_out;
    }

    public String getCarParkId() {
        return CarParkId.get();
    }

    public SimpleStringProperty carParkIdProperty() {
        return CarParkId;
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }
}
