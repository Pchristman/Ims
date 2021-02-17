package C482PA.Controllers;

import C482PA.Model.*;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static C482PA.Controllers.MainScreen.productToModifyIndex;

public class ModifyProduct implements Initializable {

    @FXML
    private TableView<Part> tvModProductAdd;

    @FXML
    private TableColumn<Part, Integer> tvModProId;

    @FXML
    private TableColumn<Part, String> tvModProName;

    @FXML
    private TableColumn<Part, Integer> tvModProInv;

    @FXML
    private TableColumn<Part, Double> tvModProPrice;

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
    private TextField txtModSearch;

    @FXML
    private Label lblModPro;

    @FXML
    private Button btnAddPro;

    @FXML
    private Button btnDelPro;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    private Label lblModId;

    @FXML
    private TextField txtModId;

    @FXML
    private Label lblModName;

    @FXML
    private TextField txtModName;

    @FXML
    private Label lblModInv;

    @FXML
    private TextField txtModInv;

    @FXML
    private Label lblModPrice;

    @FXML
    private TextField txtModPrice;

    @FXML
    private Label lblModMax;

    @FXML
    private TextField txtModMax;

    @FXML
    private Label lblModMin;

    @FXML
    private TextField txtModMin;

    private ObservableList<Part> assocParts = FXCollections.observableArrayList();
    private int proIndex = productToModifyIndex();
    private String exceptionMessage = "";
    private int productId;

    /**
     * Adds a new associated part to the list
     * @param event
     */
    @FXML
    void handleAdd(ActionEvent event) {
        Part part = tvModProductAdd.getSelectionModel().getSelectedItem();
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
     * FUTURE ENHANCEMENT A lower processing time of finding the correct item to delete
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
                Product.deleteAssociatedPart(part);
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
     * RUNTIME ERROR Had to throw IOException to prevent from error in opening the FXML file
     * Cancels and returns to the main screen
     * @param event
     * @throws IOException
     */
    @FXML
    void handleModProCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm cancel");
        alert.setHeaderText("Confirm cancel");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Stage modProduct = (Stage) ((Node)event.getSource()).getScene().getWindow();
            modProduct.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Main Screen");
            stage.setScene(scene);
            stage.show();
        }
        else {
            alert.close();
        }

    }

    /**
     * Saves the information entered as a part
     * @param event
     */
    @FXML
    void handleModProSave(ActionEvent event) throws IOException {
        String name = txtModName.getText();
        String price = txtModPrice.getText();
        String inv = txtModInv.getText();
        String max = txtModMax.getText();
        String min = txtModMin.getText();


        try {
            exceptionMessage = Product.isProductValid(name, Double.parseDouble(price), Integer.parseInt(inv),
                                                    Integer.parseInt(min), Integer.parseInt(max), assocParts, exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning");
                alert.setHeaderText("Error Modifying Product");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            } else {
                System.out.println("Product name: " + name);
                Product modProduct = new Product(productId, name, Double.parseDouble(price), Integer.parseInt(inv),
                        Integer.parseInt(min), Integer.parseInt(max));
                modProduct.setAssociatedParts(assocParts);
                Inventory.updateProduct(productToModifyIndex(), modProduct);

                Stage modClose = (Stage) ((Node)event.getSource()).getScene().getWindow();
                modClose.close();
                Parent modSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(modSave);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Error Modifying Product");
            alert.setContentText("There was a problem with the information in the fields");
            alert.showAndWait();
        }
    }

    /**
     * searches for Part ID or Part name through the search box
     * @param event
     */
    @FXML
    void handleSearch(ActionEvent event) {
        String searchPartString = txtModSearch.getText();
        boolean found = false;
        try {
            for (Part p: Inventory.getAllParts())
                if (p.getId() == Integer.parseInt(searchPartString)) {
                    ObservableList<Part> filteredParts = FXCollections.observableArrayList();
                    filteredParts.add(p);
                    tvModProductAdd.setItems(filteredParts);
                    break;
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Part Search Warning");
                    alert.setHeaderText("There were no parts found!");
                    alert.setContentText("The search term does not match any Part ID");
                    alert.showAndWait();
                }
        }
        catch (NumberFormatException e) {
            for (Part p : Inventory.getAllParts()) {
                if (p.getName().equalsIgnoreCase(searchPartString)) {
                    found = true;
                    ObservableList<Part> filteredParts = FXCollections.observableArrayList();
                    filteredParts.add(p);
                    tvModProductAdd.setItems(filteredParts);
                }
            }
            if (found == false) {
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
        Product modProduct = Inventory.getAllProducts().get(proIndex);
        productId = Inventory.getAllProducts().get(proIndex).getId();
        txtModId.setText(("Auto Gen: " + Integer.toString(productId)));
        txtModName.setText(modProduct.getName());
        txtModPrice.setText(Double.toString(modProduct.getPrice()));
        txtModInv.setText(Integer.toString(modProduct.getStock()));
        txtModMax.setText(Integer.toString(modProduct.getMax()));
        txtModMin.setText(Integer.toString(modProduct.getMin()));

        assocParts = modProduct.getAllAssociatedParts();

        tvModProId.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        tvModProName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        tvModProInv.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        tvModProPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        tvModProductAdd.setItems(Inventory.getAllParts());

        tvDelId.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        tvDelName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        tvDelInv.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        tvDelPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        tvAddProductDelete.setItems(assocParts);


    }
}


