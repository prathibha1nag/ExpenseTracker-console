package ExpenseTracker.BusinessLayer;

import ExpenseTracker.DataAccessLayer.UserRepository;
import ExpenseTracker.Models.User;
import java.sql.SQLException;

public class UserService {
    private final UserRepository repository;
    private User currentUser;

    public UserService() {
        this.repository = new UserRepository();
    }

    public User signup(User user) throws SQLException {
        if (repository.getUserByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists.");
        }
        if (repository.getUserByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists.");
        }
        if (repository.getUserByPhone(user.getPhone()) != null) {
            throw new IllegalArgumentException("Phone number already exists.");
        }

        User createdUser = repository.createUser(user);
        this.currentUser = createdUser;
        return createdUser;
    }

    public User login(String username, String password) throws SQLException {
        User user = repository.getUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        this.currentUser = user;
        return user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
