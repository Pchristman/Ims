package C482PA.Controllers;

import C482PA.Model.Inventory;
import
C482PA.Model.Part;
import C482PA.Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static C482PA.Model.Inventory.*;


public class MainScreen implements Initializable {

    @FXML
    private AnchorPane scrMain;

    @FXML
    private Button btnExit;

    @FXML
    private TextField txtSearchParts;

    @FXML
    private Button btnAddParts;

    @FXML
    private Button btnModifyParts;

    @FXML
    private Button btnDeleteParts;

    @FXML
    private TableView<Part> tvParts;

    @FXML
    private TableColumn<Part, Integer> partIdCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Integer> partInvCol;

    @FXML
    private TableColumn<Part, Double> partPriceCol;

    @FXML
    private TextField txtSearchProducts;

    @FXML
    private Button btnAddProducts;

    @FXML
    private Button btnModifyProducts;

    @FXML
    private Button btnDeleteProducts;

    @FXML
    private TableView<Product> tvProducts;

    @FXML
    private TableColumn<Product, Integer> productIDCol;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TableColumn<Product, Integer> productInventoryCol;

    @FXML
    private TableColumn<Product, Double> productPriceCol;

    private static Part modifyPart;
    private static int modifyPartIndex;
    private static Product modifyProduct;
    private static int modifyProductIndex;

    public static int partToModifyIndex() {
        return modifyPartIndex;
    }

    public static int productToModifyIndex() {
        return modifyProductIndex;
    }

    /**
     * Opens the AddPart screen
     * @param event
     * @throws IOException
     */
    @FXML
    void addPartsAction(ActionEvent event) throws IOException {
            Stage mainScreen = (Stage) ((Node)event.getSource()).getScene().getWindow();
            mainScreen.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPart.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Add Part");
            stage.setScene(scene);
            stage.show();
    }

    /**
     * Opens the AddProduct Screen
     * @param event
     * @throws IOException
     */
    @FXML
    void addProductsAction(ActionEvent event) throws IOException {
        Stage mainScreen = (Stage) ((Node)event.getSource()).getScene().getWindow();
        mainScreen.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProduct.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Deletes a part from the list
     * @param event
     */
    @FXML
    void deletePartsAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Part");
        alert.setHeaderText("Are you sure you want to delete this part?");
        alert.setContentText("Press OK to delete the part. \nPress Cancel to cancel.");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            try {
                Part part = tvParts.getSelectionModel().getSelectedItem();
                deletePart(part);
            }
            catch (NullPointerException e) {
                Alert nAlert = new Alert(Alert.AlertType.ERROR);
                nAlert.setTitle("Part Deletion Error");
                nAlert.setHeaderText("The part wasn't deleted");
                nAlert.setContentText("There was no part selected!");
                nAlert.showAndWait();
            }
        }
        else {
            alert.close();
        }
    }

    /**
     * Deletes a product from the list
     * @param event
     */
    @FXML
    void deleteProductsAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Product");
        alert.setHeaderText("Are you sure you want to delete this product?");
        alert.setContentText("Press OK to delete the product. \nPress Cancel to cancel.");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            try {
                Product product = tvProducts.getSelectionModel().getSelectedItem();
                deleteProduct(product);
            }
            catch (NullPointerException e) {
                Alert nAlert = new Alert(Alert.AlertType.ERROR);
                nAlert.setTitle("Part Deletion Error");
                nAlert.setHeaderText("The part wasn't deleted");
                nAlert.setContentText("There was no part selected!");
                nAlert.showAndWait();
            }
        }
        else {
            alert.close();
        }

    }

    /**
     * Exits the program
     * @param event
     */
    @FXML
    void exitButtonAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Inventory Management System");
        alert.setHeaderText("Are you sure you wish to exit?");
        alert.setContentText("Press OK to exit the program. \nPress Cancel to return to the program");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            Stage mainScreen = (Stage) ((Node)event.getSource()).getScene().getWindow();
            mainScreen.close();
        }
        else {
            alert.close();
        }
    }

    /**
     * FUTURE ENHANCEMENT A cleaner way of getting the Part's index for modification
     * Opens the modify part Screen
     * @param event
     * @throws IOException
     */
    @FXML
    void modifyPartsAction(ActionEvent event) throws IOException {
        modifyPart = tvParts.getSelectionModel().getSelectedItem();
        modifyPartIndex = getAllParts().indexOf(modifyPart);

        Stage mainScreen = (Stage) ((Node)event.getSource()).getScene().getWindow();
        mainScreen.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyPart.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Modify Part");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Opens the modify product screen
     * @param event
     * @throws IOException
     */
    @FXML
    void modifyProductsAction(ActionEvent event) throws IOException {
        modifyProduct = tvProducts.getSelectionModel().getSelectedItem();
        modifyProductIndex = getAllProducts().indexOf(modifyProduct);
        Stage mainScreen = (Stage) ((Node)event.getSource()).getScene().getWindow();
        mainScreen.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyProduct.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Modify Product");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * RUNTIME ERROR Had to catch a NumberFormat exception in case the user enters the name instead of id
     * searches for Part ID or Part name using the search bar
     * @param event
     */
    @FXML
    void searchPartsAction(ActionEvent event) {
        String searchPartString = txtSearchParts.getText();
        boolean found = false;
        try {
            for (Part p : Inventory.getAllParts()) {
                if (p.getId() == Integer.parseInt(searchPartString)) {
                    ObservableList<Part> filteredParts = FXCollections.observableArrayList();
                    filteredParts.add(p);
                    tvParts.setItems(filteredParts);
                    break;
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Part Search Warning");
                    alert.setHeaderText("There were no parts found!");
                    alert.setContentText("The search term does not match any Part ID");
                    alert.showAndWait();
                }
            }
        }
        catch (NumberFormatException e) {
            for (Part p : Inventory.getAllParts()) {
                if (p.getName().equalsIgnoreCase(searchPartString)) {
                    found = true;
                    ObservableList<Part> filteredParts = FXCollections.observableArrayList();
                    filteredParts.add(p);
                    tvParts.setItems(filteredParts);
                }
            }
            if (!found) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Part Search Warning");
                alert.setHeaderText("There were no parts found!");
                alert.setContentText("The search term does not match any Part name");
                alert.showAndWait();
            }
        }

    }

    /**
     * Searches for Product by Product ID or Product name
     * @param event
     */
    @FXML
    void searchProductsAction(ActionEvent event) {
        String searchProductString = txtSearchProducts.getText();
        boolean found = false;
        try {
            for (Product p : Inventory.getAllProducts()) {
                if (p.getId() == Integer.parseInt(searchProductString)) {
                    found = true;
                    ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
                    filteredProducts.add(p);
                    tvProducts.setItems(filteredProducts);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Part Search Warning");
                    alert.setHeaderText("There were no parts found!");
                    alert.setContentText("The search term does not match any Part ID");
                    alert.showAndWait();
                }
            }
        }
        catch (NumberFormatException e) {
            for (Product p : Inventory.getAllProducts()) {
                if (p.getName().equalsIgnoreCase(searchProductString)) {
                    found = true;
                    ObservableList<Product> filteredParts = FXCollections.observableArrayList();
                    filteredParts.add(p);
                    tvProducts.setItems(filteredParts);
                }
            }
            if (!found) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Part Search Warning");
                alert.setHeaderText("There were no parts found!");
                alert.setContentText("The search term does not match any Part name");
                alert.showAndWait();
            }
        }

    }

    /**
     * Updates Parts table view
     *
     */
    @FXML
    public void updatePartsTV() {
        tvParts.setItems(getAllParts());

    }

    /**
     * Updates Product table view
     *
     */
    @FXML
    public void updateProductsTV() {
        tvProducts.setItems(getAllProducts());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        updatePartsTV();

        productIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        updateProductsTV();
    }
}