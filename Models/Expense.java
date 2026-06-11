package ExpenseTracker.Models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Expense implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
    private int userId;
    private Date date;
    private String description;
    private double amount;
    private Category category;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public Expense(int id, int userId, Date date, String description, double amount, Category category) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    public Expense(int userId, Date date, String description, double amount, Category category) {
        this(0, userId, date, description, amount, category);
    }

    public Expense(Date date, String description, double amount, Category category) {
        this(0, 1, date, description, amount, category);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String displayAsString() {
        return DATE_FORMAT.format(date) + " | " + description + " | " + amount + " | " + category;
    }
}
