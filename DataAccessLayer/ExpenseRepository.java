package ExpenseTracker.DataAccessLayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ExpenseTracker.DBConfig;
import ExpenseTracker.Models.Category;
import ExpenseTracker.Models.Expense;

public class ExpenseRepository {
    private final DBConfig dbConfig;

    public ExpenseRepository() {
        this(DBConfig.load());
    }

    public ExpenseRepository(DBConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUser(), dbConfig.getPassword());
    }

    public Expense createExpense(Expense expense) throws SQLException {
        String sql = "INSERT INTO expense (userid, date, amount, expensecategory, description) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, expense.getUserId());
            stmt.setDate(2, new java.sql.Date(expense.getDate().getTime()));
            stmt.setDouble(3, expense.getAmount());
            stmt.setInt(4, expense.getCategory().getCode());
            stmt.setString(5, expense.getDescription());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating expense failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    expense.setId(generatedKeys.getInt(1));
                }
            }
        }

        return expense;
    }

    public Expense getExpenseById(int id) throws SQLException {
        String sql = "SELECT id, userid, date, amount, expensecategory, description FROM expense WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapExpense(rs);
                }
            }
        }

        return null;
    }

    public List<Expense> getExpensesByUser(int userId) throws SQLException {
        String sql = "SELECT id, userid, date, amount, expensecategory, description FROM expense WHERE userid = ? ORDER BY date DESC";
        List<Expense> expenses = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    expenses.add(mapExpense(rs));
                }
            }
        }

        return expenses;
    }

    public List<Expense> getAllExpenses() throws SQLException {
        String sql = "SELECT id, userid, date, amount, expensecategory, description FROM expense ORDER BY date DESC";
        List<Expense> expenses = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                expenses.add(mapExpense(rs));
            }
        }

        return expenses;
    }

    public boolean updateExpense(Expense expense) throws SQLException {
        String sql = "UPDATE expense SET userid = ?, date = ?, amount = ?, expensecategory = ?, description = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, expense.getUserId());
            stmt.setDate(2, new java.sql.Date(expense.getDate().getTime()));
            stmt.setDouble(3, expense.getAmount());
            stmt.setInt(4, expense.getCategory().getCode());
            stmt.setString(5, expense.getDescription());
            stmt.setInt(6, expense.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteExpense(int id) throws SQLException {
        String sql = "DELETE FROM expenses WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Expense mapExpense(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int userId = rs.getInt("userid");
        Date date = new Date(rs.getDate("date").getTime());
        double amount = rs.getDouble("amount");
        Category category = Category.fromCode(rs.getInt("expensecategory"));
        String description = rs.getString("description");

        return new Expense(id, userId, date, description, amount, category);
    }
}
