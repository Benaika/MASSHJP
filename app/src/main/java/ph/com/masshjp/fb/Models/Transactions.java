package ph.com.masshjp.fb.Models;

public class Transactions {
    private String source;
    private String date;
    private double amount;
    private String status;
    private String ar;
    private String counter;

    // No-argument constructor (REQUIRED by Firestore)
    public Transactions() {
    }

    // Constructor with parameters
    public Transactions(String source, String date, double amount, String status, String ar, String counter) {
        this.source = source;
        this.date = date;
        this.amount = amount;
        this.status = status;
        this.ar = ar;
        this.counter = counter;
    }

    // Getters and Setters (Required for Firestore)
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAr() {
        return ar;
    }

    public void setAr(String ar) {
        this.ar = ar;
    }
    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }
}
