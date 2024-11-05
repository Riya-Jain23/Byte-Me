package User;

import Core.Order;

import java.util.ArrayList;
import java.util.List;

public class User {
    protected String name;
    protected String userId;
    protected List<Order> orderHistory;

    public User(String name, String userId) {
        this.name = name;
        this.userId = userId;
        this.orderHistory = new ArrayList<>();
    }
    // Common methods
    public boolean login(String name, String userId) {
        // Simple login validation logic
        if (this.name.equals(name) && this.userId.equals(userId)) {
            System.out.println("Login successful for user: " + name);
            return true;
        } else {
            System.out.println("Invalid credentials for user: " + name);
            return false;
        }
    }

    public void addOrderToHistory(Order order) {
        orderHistory.add(order);
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    @Override
    public String toString() {
        return "User ID: " + userId + ", Name: " + name;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }


}
