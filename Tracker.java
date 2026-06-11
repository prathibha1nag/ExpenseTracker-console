package ExpenseTracker;

import ExpenseTracker.BusinessLayer.ExpenseService;
import ExpenseTracker.Models.Category;
import ExpenseTracker.Models.Expense;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Tracker {
    public static void main(String[] args) {
        ExpenseService expenseService = new ExpenseService();
        ArrayList<Expense> expenses = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try (Scanner sc = new Scanner(System.in)) {
            int choice = 0;
            Category[] categories = Category.values();

            try {
                expenses = new ArrayList<>(expenseService.getAllExpenses());
            } catch (SQLException e) {
                System.out.println("Could not load expenses from database: " + e.getMessage());
            }

        System.out.println("======Welcome to Monthly Expense Tracker=======");
        System.out.println("Please login or signup to continue.");
        

        while (choice != 6) {
            System.out.println("What you want to do \n 1.Add an Expense \n 2.Get Expense by Id \n 3.Update Expense \n 4.Delete Expense \n 5.Display Expenses \n 6.Exit");
            System.out.println("enter the choice");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    sc.nextLine();
                    System.out.println("Enter Date in dd/mm/yyyy format");
                    String dateInput = sc.nextLine();
                    Date date = new Date();
                    try {
                        date = sdf.parse(dateInput);
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Using today instead.");
                    }

                    System.out.println("Enter Description");
                    String desc = sc.nextLine();

                    System.out.println("Enter Amount");
                    double amt = sc.nextDouble();

                    System.out.println("Select Category:");
                    for (int i = 0; i < categories.length; i++) {
                        System.out.println((i + 1) + ". " + categories[i]);
                    }

                    System.out.println("Enter category number:");
                    int choicecat = sc.nextInt();
                    Category cat = categories[Math.max(0, Math.min(choicecat - 1, categories.length - 1))];
                    System.out.println("Selected Category : " + cat);

                    Expense createdExpense = new Expense(1, date, desc, amt, cat);
                    try {
                        createdExpense = expenseService.createExpense(createdExpense);
                        expenses.add(createdExpense);
                        System.out.println("\nExpense Added Successfully with id " + createdExpense.getId());
                    } catch (SQLException e) {
                        System.out.println("Failed to save expense: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("Enter expense id to retrieve:");
                    int retrieveId = sc.nextInt();
                    try {
                        Expense found = expenseService.getExpenseById(retrieveId);
                        if (found != null) {
                            System.out.println(found.displayAsString());
                        } else {
                            System.out.println("Expense not found for id " + retrieveId);
                        }
                    } catch (SQLException e) {
                        System.out.println("Failed to retrieve expense: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Enter expense id to update:");
                    int updateId = sc.nextInt();
                    try {
                        Expense existing = expenseService.getExpenseById(updateId);
                        if (existing == null) {
                            System.out.println("Expense not found for id " + updateId);
                            break;
                        }

                        sc.nextLine();
                        System.out.println("Enter new description (leave blank to keep current):");
                        String newDesc = sc.nextLine();
                        if (!newDesc.isBlank()) {
                            existing.setDescription(newDesc);
                        }

                        System.out.println("Enter new amount (or blank to keep current):");
                        String amountInput = sc.nextLine();
                        if (!amountInput.isBlank()) {
                            existing.setAmount(Double.parseDouble(amountInput));
                        }

                        System.out.println("Select new category number (or blank to keep current):");
                        for (int i = 0; i < categories.length; i++) {
                            System.out.println((i + 1) + ". " + categories[i]);
                        }
                        String categoryInput = sc.nextLine();
                        if (!categoryInput.isBlank()) {
                            int updatedCatIndex = Integer.parseInt(categoryInput) - 1;
                            if (updatedCatIndex >= 0 && updatedCatIndex < categories.length) {
                                existing.setCategory(categories[updatedCatIndex]);
                            }
                        }

                        boolean updated = expenseService.updateExpense(existing);
                        if (updated) {
                            for (int i = 0; i < expenses.size(); i++) {
                                if (expenses.get(i).getId() == existing.getId()) {
                                    expenses.set(i, existing);
                                    break;
                                }
                            }
                            System.out.println("Expense updated successfully.");
                        } else {
                            System.out.println("Expense update failed.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Failed to update expense: " + e.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("Enter expense id to delete:");
                    int deleteId = sc.nextInt();
                    try {
                        boolean deleted = expenseService.deleteExpense(deleteId);
                        if (deleted) {
                            expenses.removeIf(expense -> expense.getId() == deleteId);
                            System.out.println("Expense deleted successfully.");
                        } else {
                            System.out.println("Expense not found or could not be deleted.");
                        }
                    } catch (SQLException e) {
                        System.out.println("Failed to delete expense: " + e.getMessage());
                    }
                    break;
                case 5:
                    try {
                        expenses = new ArrayList<>(expenseService.getAllExpenses());
                    } catch (SQLException e) {
                        System.out.println("Failed to refresh expenses: " + e.getMessage());
                    }
                    DisplayExpenses(expenses);
                    System.out.println("Thank You for Using Expense Tracker");
                    break;
                case 6:
                    System.out.println("Thank You for Using Expense Tracker");
                    break;
                default:
                    System.out.println("Invalid Choice");
            }
        }
        sc.close();
    }
        }

    public static void DisplayExpenses(ArrayList<Expense> expenses) {
        StringBuffer sb = new StringBuffer();
        sb.append("Date | description | amount | category \n");
        for (Expense e : expenses) {
            sb.append(e.displayAsString()).append("\n");
        }
        System.out.println(sb.toString());
    }

    public static void calculateSummary(ArrayList<Expense> expenses) {

    }
}
