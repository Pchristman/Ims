package C482PA.Model;

import C482PA.Model.Part;
import javafx.collections.ObservableList;

public class Product {

    private static ObservableList<Part> associatedParts;
    int id;
    String name;
    double price;
    int stock;
    int min;
    int max;

    /**
     * Constructor for Product
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    public void setAssociatedParts(ObservableList<Part> associatedParts) {
        this.associatedParts = associatedParts;
    }

    /**
     * Sets the id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the price
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Sets the inventory level
     * @param stock
     */
    public void setStock (int stock) {
        this.stock = stock;
    }

    /**
     * Sets the minimum
     * @param min
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Sets the max
     * @param max
     */
    public void setMax(int max) { this.max = max;}

    /**
     * Returns ID
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Returns name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Returns price
     * @return
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns Inventory
     * @return
     */
    public int getStock() {
        return stock;
    }

    /**
     * Returns Min
     * @return
     */
    public int getMin() {
        return min;
    }

    /**
     * Returns Max
     * @return
     */
    public int getMax() {
        return max;
    }

    /**
     * Adds an associated part to the list
     * @param part
     */
    public static void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * Deletes the selected Associated Part
     * @param selectedAssociatedPart
     * @return
     */
    public static boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        for (int i = 0; i < associatedParts.size(); i++) {
            if (associatedParts.get(i).getId() == selectedAssociatedPart.getId()) {
                associatedParts.remove(i);
                break;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the list of all Associated Parts
     * @return
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

    //Validation
    public static String isProductValid(String name, double price, int inv, int min, int max, ObservableList<Part> parts,String errorMessage) {
        double sumOfParts = 0.00;
        if(parts != null) {
            for (int i = 0; i < parts.size(); i++) {
                sumOfParts += parts.get(i).getPrice();
            }
        }
        if (name == null) {
            errorMessage = errorMessage + "The name field is required. ";
        }
        if (inv < 1) {
            errorMessage = errorMessage + "Inventory can't be less than 1. ";
        }
        if (price <= 0) {
            errorMessage = errorMessage +  "Price must be greater than 0. ";
        }
        if (max < min) {
            errorMessage = errorMessage + "Max must be greater than or equal to min. ";
        }
        if (inv < min || inv > max) {
            errorMessage = errorMessage + "Inventory must be between min and max. ";
        }
        if (sumOfParts > price) {
            errorMessage = errorMessage + "Price must be greater than the sum of all part's cost. ";
        }
        return errorMessage;
    }



}
