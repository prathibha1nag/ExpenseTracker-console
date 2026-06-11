package ExpenseTracker.BusinessLayer;

import java.sql.SQLException;
import java.util.List;

import ExpenseTracker.DataAccessLayer.ExpenseRepository;
import ExpenseTracker.Models.Expense;

public class ExpenseService {
    private final ExpenseRepository repository;

    public ExpenseService() {
        this.repository = new ExpenseRepository();
    }

    public Expense createExpense(Expense expense) throws SQLException {
        return repository.createExpense(expense);
    }

    public Expense getExpenseById(int id) throws SQLException {
        return repository.getExpenseById(id);
    }

    public List<Expense> getExpensesByUser(int userId) throws SQLException {
        return repository.getExpensesByUser(userId);
    }

    public List<Expense> getAllExpenses() throws SQLException {
        return repository.getAllExpenses();
    }

    public boolean updateExpense(Expense expense) throws SQLException {
        return repository.updateExpense(expense);
    }

    public boolean deleteExpense(int id) throws SQLException {
        return repository.deleteExpense(id);
    }
}
