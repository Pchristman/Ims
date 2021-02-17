package C482PA.Controllers;

import C482PA.Model.Inventory;
import C482PA.Model.Part;
import C482PA.Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddProduct implements Initializable {

    @FXML
    private AnchorPane lblAddProId;

    @FXML
    private TableView<Part> tvAddProductAdd;

    @FXML
    private TableColumn<Part, Integer> tvAddProId;

    @FXML
    private TableColumn<Part, String> tvAddProName;

    @FXML
    private TableColumn<Part, Integer> tvAddProInv;

    @FXML
    private TableColumn<Part, Double> tvAddProPrice;

    @FXML
    private TableView<Part> tvAddProductDelete;

    @FXML
    private TableColumn<Part, Integer> tvDelId;

    @FXML
    private TableColumn<Part, String> tvDelName;

    @FXML
    private TableColumn<Part, Integer> tvDelInv;

    @FXML
    private TableColumn<Part, Double> tvDelPrice;

    @FXML
    private TextField txtAddSearch;

    @FXML
    private Label lblAddPro;

    @FXML
    private Button btnAddPro;

    @FXML
    private Button btnDelPro;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private Label lblProId;

    @FXML
    private TextField txtAddProId;

    @FXML
    private Label lblAddProName;

    @FXML
    private TextField txtAddProName;

    @FXML
    private Label lblAddProInv;

    @FXML
    private TextField txtAddProInv;

    @FXML
    private Label lblAddProPrice;

    @FXML
    private TextField txtAddProPrice;

    @FXML
    private Label lblAddProMax;

    @FXML
    private TextField txtAddProMax;

    @FXML
    private Label lblAddProMin;

    @FXML
    private TextField txtAddProMin;

    private ObservableList<Part> assocParts = FXCollections.observableArrayList();
    private String exceptionMessage = "";
    private int proId;

    /**
     * RUNTIME ERROR the had a different observed list prior to the assocParts variable
     * that made nothing save correctly.
     * Adds a product to the associated part list
     * @param event
     */
    @FXML
    void handleAdd(ActionEvent event) {
        Part part = tvAddProductAdd.getSelectionModel().getSelectedItem();
        if (part == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Associated Part Error");
            alert.setHeaderText("The part wasn't added.");
            alert.setContentText("You did not select a part");
            alert.showAndWait();
        }
        else {
            assocParts.add(part);
            tvAddProductDelete.setItems(assocParts);
        }
    }

    /**
     * RUNTIME ERROR Had to throw IOException to prevent an error from opening the FXML file
     * Cancels and returns to the main screen
     * @param event
     * @throws IOException
     */
    @FXML
    void handleAddProCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm cancel");
        alert.setHeaderText("Confirm cancel");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Stage addProduct = (Stage) ((Node)event.getSource()).getScene().getWindow();
            addProduct.close();
            Parent addSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            Scene scene = new Scene(addSave);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }
        else {
            alert.close();
        }

    }

    /**
     * FUTURE ENHANCEMENT A cleaner block of code for saving a new Product
     * Saves the information as a new Product
     * @param event
     */
    @FXML
    void handleAddProSave(ActionEvent event) throws IOException {
        String name = txtAddProName.getText();
        String price = txtAddProPrice.getText();
        String inv = txtAddProInv.getText();
        String max = txtAddProMax.getText();
        String min = txtAddProMin.getText();
        double proPrice = Double.parseDouble(price);
        int proInv = Integer.parseInt(inv);
        int proMax = Integer.parseInt(max);
        int proMin = Integer.parseInt(min);


        try {
            exceptionMessage = Product.isProductValid(name, Double.parseDouble(price), Integer.parseInt(inv),Integer.parseInt(min),
                                                      Integer.parseInt(max), assocParts, exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning");
                alert.setHeaderText("Error adding Product");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();

            }
            else {
                System.out.println("Product name: " + name);
                Product newProduct = new Product(proId, name, Double.parseDouble(price), Integer.parseInt(inv), Integer.parseInt(min),
                                                Integer.parseInt(max));
                newProduct.setAssociatedParts(assocParts);
                Inventory.addProduct(newProduct);

                Stage addProduct = (Stage) ((Node)event.getSource()).getScene().getWindow();
                addProduct.close();
                Parent addSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(addSave);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Error Adding Product");
            alert.setContentText("Form contains issues with the fields");
            alert.showAndWait();
        }
    }

    /**
     * Deletes an associated part
     * @param event
     */
    @FXML
    void handleDelete(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleted Associated Part");
        alert.setHeaderText("Are you sure you want to delete the associated part?");
        alert.setContentText("Press OK to delete the part \nPress Cancel to cancel the deletion.");
        alert.showAndWait();

        if(alert.getResult() == ButtonType.OK) {
            try {
                Part part = tvAddProductDelete.getSelectionModel().getSelectedItem();
                assocParts.remove(part);
            }
            catch (NullPointerException e) {
                Alert nAlert = new Alert(Alert.AlertType.ERROR);
                nAlert.setTitle("Deletion Error");
                nAlert.setHeaderText("The part was not deleted");
                nAlert.setContentText("You have to select a part first.");
                nAlert.showAndWait();
            }
        }
        else {
            alert.close();
        }
    }

    /**
     * Searches for a part through the search box
     * @param event
     */
    @FXML
    void handleSearch(ActionEvent event) {
        String searchPartString = txtAddSearch.getText();
        boolean found = false;
        try {
            for (Part p: Inventory.getAllParts()) {
                if (p.getId() == Integer.parseInt(searchPartString)) {
                    ObservableList<Part> filteredParts = FXCollections.observableArrayList();
                    filteredParts.add(p);
                    tvAddProductAdd.setItems(filteredParts);
                    break;
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
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
                    tvAddProductAdd.setItems(filteredParts);
                }
            }
            if (!found) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Part Search Warning");
                alert.setHeaderText("There were no parts found!");
                alert.setContentText("The search term does not match any Part name");
                alert.showAndWait();
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        proId = Inventory.getProductIdCount();
        txtAddProId.setText("Auto-Gen: " + proId);

        tvAddProId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tvAddProName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tvAddProInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tvAddProPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tvAddProductAdd.setItems(Inventory.getAllParts());

        tvDelId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tvDelName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tvDelInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tvDelPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tvAddProductDelete.setItems(assocParts);



    }
}