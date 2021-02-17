package C482PA.Model;
import javafx.collections.*;

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static int partIdCount = 0;
    private static int productIdCount = 0;

    /**
     * Adds the new Part to the arrayList
     *
     * @param newPart New Part to add
     */
    public static void addPart(Part newPart) {
        if (newPart != null) {
            allParts.add(newPart);
        }
    }

    /**
     * Adds a new product to the list
     *
     * @param newProduct
     */
    public static void addProduct(Product newProduct) {
        if (newProduct != null) {
            allProducts.add(newProduct);
        }
    }

    /**
     * Looks up the part via Part ID
     *
     * @param partId
     * @return
     */
    public static Part lookupPart(int partId) {
        if (!allParts.isEmpty()) {
            for (int i = 0; i < allParts.size(); i++) {
                if (allParts.get(i).getId() == partId) {
                    return allParts.get(i);
                }
            }
        }
        return null;
    }

    /**
     * Looks up product via product ID
     *
     * @param productId
     * @return
     */
    public static Product lookupProduct(int productId) {
        if (!allProducts.isEmpty()) {
            for (int i = 0; i < allProducts.size(); i++) {
                if (allParts.get(i).getId() == productId) {
                    return allProducts.get(i);
                }
            }
        }
        return null;
    }


    /**
     * FUTURE ENHANCEMENT Find a way that I don't have to cast the return
     * Looks up Part via name
     *
     * @param partName
     * @return
     */
    public static ObservableList<Part> lookupPart(String partName) {

        if (!allParts.isEmpty()) {
            for (int i = 0; i < allParts.size(); i++) {
                if (allParts.get(i).getName().equals(partName)) {
                    return (ObservableList<Part>) allParts.get(i);
                }
            }
        }
        return null;
    }

    /**
     * Looks up Product via name
     *
     * @param productName
     * @return
     */
    public static ObservableList<Product> lookupProduct(String productName) {

        if (!allProducts.isEmpty()) {
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getName().equals(productName)) {
                    return (ObservableList<Product>) allProducts.get(i);
                }
            }
        }
        return null;
    }

    /**
     * Updates the part through index and object data
     *
     * @param index
     * @param selectedPart
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Updates the product through index and object data
     *
     * @param index
     * @param newProduct
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /**
     * Deletes Part selected
     *
     * @param selectedPart
     * @return
     */
    public static boolean deletePart(Part selectedPart) {
        boolean isFound = false;
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getId() == selectedPart.getId()) {
                allParts.remove(i);
                isFound = true;
            }
        }
        return isFound;
    }

    /**
     * Deletes product selected
     *
     * @param selectedProduct
     * @return
     */
    public static boolean deleteProduct(Product selectedProduct) {
        boolean isFound = false;
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == selectedProduct.getId()) {
                allProducts.remove(i);
                isFound = true;
            }
        }
        return isFound;
    }

    /**
     * Returns the list of all parts
     *
     * @return
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Returns the list of all products
     *
     * @return
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    public static int getPartIdCount() {
        partIdCount++;
        return partIdCount;
    }

    public static int getProductIdCount() {
        productIdCount++;
        return productIdCount;
    }

}

