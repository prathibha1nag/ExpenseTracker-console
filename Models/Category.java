package ExpenseTracker.Models;

public enum Category {
    Investment,
    Groceries,
    Medicines,
    Utility,
    Travel,
    Food,
    UselessExpenses;

    public int getCode() {
        return this.ordinal();
    }

    public static Category fromCode(int code) {
        if (code < 0 || code >= values().length) {
            throw new IllegalArgumentException("Unknown category code: " + code);
        }
        return values()[code];
    }
}
