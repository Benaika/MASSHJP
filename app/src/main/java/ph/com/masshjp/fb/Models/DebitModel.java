package ph.com.masshjp.fb.Models;

import java.sql.Time;
import java.sql.Timestamp;

public class DebitModel {
    private String source, date, ar, counter;
    private double amount;
    private Timestamp timestamp;

    public DebitModel() {}

    public DebitModel(Timestamp timestamp, String source, String date, double amount, String ar, String counter) {
        this.timestamp = timestamp;
        this.source = source;
        this.date = date;
        this.amount = amount;
        this.ar = ar;
        this.counter = counter;
    }
    public Timestamp getTimestamp() { return timestamp; }
    public String getSource() { return source; }
    public String getDate() { return date; }
    public double getAmount() { return amount; }
    public String getAr() { return ar; }
    public String getCounter() { return counter; }
}

