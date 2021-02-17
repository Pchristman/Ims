package C482PA.Controllers;

import C482PA.Model.InHouse;
import C482PA.Model.Inventory;
import C482PA.Model.Outsourced;
import C482PA.Model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddPart implements Initializable {

    @FXML
    private Label lblAddPartsScreen;

    @FXML
    private RadioButton rbInHouse;

    @FXML
    private ToggleGroup tgSource;

    @FXML
    private RadioButton rbOutsource;

    @FXML
    private Label lblAddPartDuo;

    @FXML
    private TextField txtAddPartID;

    @FXML
    private TextField txtAddPartName;

    @FXML
    private TextField txtAddPartInv;

    @FXML
    private TextField txtAddPartPrice;

    @FXML
    private TextField txtAddPartMax;

    @FXML
    private TextField txtAddPartDuo;

    @FXML
    private TextField txtAddPartMin;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    private boolean isInHouse;
    private int partId;
    private String exceptionMessage = new String();

    /**
     * Changes the label depending on radio button selected
     * @param event
     */
    @FXML
    void AddInHouseRadio(ActionEvent event) {
        isInHouse = true;
        lblAddPartDuo.setText("Machine ID");
        txtAddPartDuo.setPromptText("Machine ID");
        rbOutsource.setSelected(false);

    }

    /**
     * Changes the label depending on radio button selected
     * @param event
     */
    @FXML
    void addOutsourcedRadio(ActionEvent event) {
        isInHouse = false;
        lblAddPartDuo.setText("Company name");
        txtAddPartDuo.setPromptText("Company name");
        rbInHouse.setSelected(false);
    }

    /**
     * RUNTIME ERROR Had to throw an IOException to prevent an error from selecting the FXML file
     * Cancels and returns to the main screen
     * @param event
     * @throws IOException
     */
    @FXML
    void handleAddPartCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm cancel");
        alert.setHeaderText("Confirm cancel");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            Stage addPart = (Stage) ((Node)event.getSource()).getScene().getWindow();
            addPart.close();
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
     * FUTURE ENHANCEMENT A more seamless way of creating the objects
     * Saves the new information as a a new part
     * RUNTIME ERROR The Parts were initializing as null values due to an error in the Part's constructor
     * @param event
     */
    @FXML
    void handleAddPartSave(ActionEvent event) throws IOException  {
        String partName = txtAddPartName.getText();
        String partInv = txtAddPartInv.getText();
        String partPrice = txtAddPartPrice.getText();
        String partMin = txtAddPartMin.getText();
        String partMax = txtAddPartMax.getText();
        String partDuo = txtAddPartDuo.getText();


        try {
            exceptionMessage = Part.isPartValid(partName, Double.parseDouble(partPrice), Integer.parseInt(partInv),
                                            Integer.parseInt(partMin), Integer.parseInt(partMax), exceptionMessage );
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning");
                alert.setHeaderText("Error adding part");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            }
            else {
                if (rbInHouse.isSelected()) {
                    System.out.println("Part name: " + partName);
                    InHouse inPart = new InHouse(partId,  partName, Double.parseDouble(partPrice), Integer.parseInt(partInv),
                            Integer.parseInt(partMin), Integer.parseInt(partMax), Integer.parseInt(partDuo));
                    Inventory.addPart(inPart);

                }
                else {
                    System.out.println("Part name: " + partName);
                    Outsourced outPart = new Outsourced(partId,  partName, Double.parseDouble(partPrice), Integer.parseInt(partInv),
                            Integer.parseInt(partMin), Integer.parseInt(partMax), partDuo);
                    Inventory.addPart(outPart);
                }
                Stage addPart = (Stage) ((Node)event.getSource()).getScene().getWindow();
                addPart.close();
                Parent addSave = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(addSave);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Error Adding Part");
            alert.setContentText("There were problems with the fields.");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partId = Inventory.getPartIdCount();
        txtAddPartID.setText("Auto-Gen: " + partId);

    }
}
