package ExpenseTracker.Models;

import java.util.HashMap;

public class MonthlyExpenseSummary {
    public int Month;
    public int Year;
    public double MonthlyTotal;
    public HashMap<String, Double> expByCat = new HashMap<>();
}
