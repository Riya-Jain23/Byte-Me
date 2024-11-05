package User;
import Core.FoodItem;
import Core.Order;
import Core.OrderComparator;
import java.util.*;

public class Admin extends User {
    public TreeMap<String, FoodItem> menu; // Sorted menu by food item name
    private PriorityQueue<Order> pendingOrders; // Priority queue so, VIP orders processed first
    private List<Order> completedOrders; // To store already processed orders

    // Constructor
    public Admin(String name, String userId) {
        super(name,userId);
        this.menu = new TreeMap<>();
        this.pendingOrders = new PriorityQueue<>(new OrderComparator()); // Custom comparator for VIP priority
        this.completedOrders = new ArrayList<>();
    }

    public PriorityQueue<Order> getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(PriorityQueue<Order> pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public List<Order> getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(List<Order> completedOrders) {
        this.completedOrders = completedOrders;
    }

    public void addMenuItem(FoodItem item) {
        menu.put(item.getName(), item);
    }

    public void updateMenuItem(String itemName, double newPrice, boolean isAvailable) {
        FoodItem item = menu.get(itemName);
        if (item != null) {
            item.updateDetails(newPrice, isAvailable);
        } else {
            System.out.println("Item not found in menu.");
        }
    }

    public void removeMenuItem(String itemName) {
        FoodItem item = menu.remove(itemName);
        if (item != null) {
            List<Order> ordersToUpdate = new ArrayList<>();
            for (Order order : pendingOrders) {
                if (order.getItems().containsKey(item)) {
                    order.updateStatus("Denied");
                    ordersToUpdate.add(order);
                }
            }
            pendingOrders.removeAll(ordersToUpdate);
        } else {
            System.out.println("Item not found in menu.");
        }
    }

    //Order Management

    // Add a new order to the pending orders queue
    public void addOrder(Order order) {
        pendingOrders.add(order);
    }

    public void viewPendingOrders() {
        if (pendingOrders.isEmpty()) {
            System.out.println("No pending orders.");
            return;
        }
        for (Order order : pendingOrders) {
            System.out.println(order);
        }
    }

    public void updateOrderStatus(Order order, String newStatus) {
        if (pendingOrders.remove(order)) {
            order.updateStatus(newStatus);
            if ("Delivered".equals(newStatus) || "Denied".equals(newStatus)) {
                completedOrders.add(order);
            } else {
                pendingOrders.add(order); // Re-add to queue if status allows
            }
        } else {
            System.out.println("Order not found in pending orders.");
        }
    }

    public void processRefund(Order order) {
        if (order.getStatus().equals("Cancelled")) {
            System.out.println("Refund processed for Order");
        } else {
            System.out.println("Order is not canceled by Customer yet.");
        }
    }

    //Report Generation
    public String generateDailySalesReport() {
        double totalSales = 0;
        int totalOrders = completedOrders.size();
        Map<String, Integer> itemPopularity = new HashMap<>();

        for (Order order : completedOrders) {
            totalSales += order.calculateTotal();
            for (FoodItem item : order.getItems().keySet()) {
                itemPopularity.put(item.getName(), itemPopularity.getOrDefault(item.getName(), 0) + 1);
            }
        }

        // Find the most popular item
        String mostPopularItem = itemPopularity.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No items sold");

        return String.format("Daily Sales Report:\nTotal Sales: $%.2f\nTotal Orders: %d\nMost Popular Item: %s",
                totalSales, totalOrders, mostPopularItem);
    }

    //Helper Methods

    // Display the current menu items
    public void viewMenu() {
        if (menu.isEmpty()) {
            System.out.println("Menu is currently empty.");
        } else {
            for (FoodItem item : menu.values()) {
                System.out.println(item);
            }
        }
    }

    // Handle special requests
    public void handleSpecialRequest(Order targetOrder, String request) {
        if (pendingOrders.remove(targetOrder)) {
            targetOrder.setSpecialRequest(request);
            pendingOrders.add(targetOrder);
            System.out.println("Special request added to order.");
        } else {
            System.out.println("Order not found in pending orders.");
        }
    }

    public FoodItem findMenuItem(String itemName) {
        System.out.println(menu.get(itemName));
        return menu.get(itemName);
    }
    public void filterMenuItemByCategory(String category) {
        System.out.println("Food menu Items in " + category + " are:");
        for (FoodItem foodItem : menu.values()) {
            if (foodItem.category.equalsIgnoreCase(category)) {
                System.out.println(foodItem);
            }
        }
    }
    public void SortByPrice(String sortOption){
            if(sortOption.equalsIgnoreCase("ascending")){
                List<FoodItem> itemsList = new ArrayList<>(menu.values());
                Collections.sort(itemsList,Comparator.comparingDouble(FoodItem::getPrice));
                System.out.println("Food Items sorted in ascending order: ");
                for(FoodItem foodItem : itemsList){
                    System.out.println(foodItem);
                }
            }
            else{
                List<FoodItem> itemsList = new ArrayList<>(menu.values());
                Collections.sort(itemsList,Comparator.comparingDouble(FoodItem::getPrice).reversed());
                System.out.println("Food Items sorted in descending order: ");
                for(FoodItem foodItem : itemsList){
                    System.out.println(foodItem);
                }
            }
    }
    // Logs the admin out
    public void logout() {
        System.out.println("Admin logged out.");
    }
}
