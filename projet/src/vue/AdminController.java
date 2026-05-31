package vue;

import entities.Medicament;
import entities.Utilisateur;
import service.MedicamentService;
import service.UtilisateurService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML private TextField nomField, descField, prixField, quantiteField, dateField;
    @FXML private TextField rechercheField;
    @FXML private Label alerteLabel;
    @FXML private TableView<Medicament> tableView;
    @FXML private TableColumn<Medicament, Integer> idCol;
    @FXML private TableColumn<Medicament, String> nomCol, descCol;
    @FXML private TableColumn<Medicament, Date> dateCol;
    @FXML private TableColumn<Medicament, Double> prixCol;
    @FXML private TableColumn<Medicament, Integer> quantiteCol;
    @FXML private TextField empUsernameField, empPasswordField;
    @FXML private TableView<Utilisateur> employeTable;
    @FXML private TableColumn<Utilisateur, Integer> empIdCol;
    @FXML private TableColumn<Utilisateur, String> empUsernameCol, empRoleCol;

    private MedicamentService medService = new MedicamentService();
    private UtilisateurService userService = new UtilisateurService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));
        quantiteCol.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        dateCol.setCellFactory(column -> new TableCell<Medicament, Date>() {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void updateItem(Date date, boolean empty) {
        super.updateItem(date, empty);
        setText(empty || date == null ? null : sdf.format(date));
    }
});
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateExpiration"));
        empIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        empUsernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        empRoleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        chargerMedicaments();
        chargerEmployes();
        verifierStockFaible();
    }

    private void chargerMedicaments() {
        ObservableList<Medicament> liste =
            FXCollections.observableArrayList(medService.listerTous());
        tableView.setItems(liste);
    }

    private void chargerEmployes() {
        ObservableList<Utilisateur> liste =
            FXCollections.observableArrayList(userService.listerEmployes());
        employeTable.setItems(liste);
    }

    private void verifierStockFaible() {
        List<Medicament> liste = medService.listerTous();
        long count = liste.stream().filter(m -> m.getQuantite() < 10).count();
        if (count > 0) {
            alerteLabel.setText("⚠️ ALERTE: " + count + 
                " médicament(s) avec stock faible (< 10) !");
        } else {
            alerteLabel.setText("");
        }
    }

    @FXML
    public void ajouterMedicament() {
        try {
            Medicament m = new Medicament();
            m.setNom(nomField.getText());
            m.setDescription(descField.getText());
            m.setPrix(Double.parseDouble(prixField.getText()));
            m.setQuantite(Integer.parseInt(quantiteField.getText()));
            if (!dateField.getText().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                m.setDateExpiration(sdf.parse(dateField.getText()));
            }
            medService.ajouter(m);
            chargerMedicaments();
            verifierStockFaible();
            viderChamps();
        } catch (Exception e) {
            alerteLabel.setText("Erreur: " + e.getMessage());
        }
    }

    @FXML
    public void modifierMedicament() {
        try {
            Medicament m = tableView.getSelectionModel().getSelectedItem();
            if (m != null) {
                m.setNom(nomField.getText());
                m.setDescription(descField.getText());
                m.setPrix(Double.parseDouble(prixField.getText()));
                m.setQuantite(Integer.parseInt(quantiteField.getText()));
                if (!dateField.getText().isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    m.setDateExpiration(sdf.parse(dateField.getText()));
                }
                medService.modifier(m);
                chargerMedicaments();
                verifierStockFaible();
                viderChamps();
            }
        } catch (Exception e) {
            alerteLabel.setText("Erreur: " + e.getMessage());
        }
    }

    @FXML
    public void supprimerMedicament() {
        Medicament m = tableView.getSelectionModel().getSelectedItem();
        if (m != null) {
            medService.supprimer(m.getId());
            chargerMedicaments();
            verifierStockFaible();
        }
    }

    @FXML
    public void actualiser() {
        chargerMedicaments();
        chargerEmployes();
        verifierStockFaible();
    }

    @FXML
    public void rechercher() {
        String nom = rechercheField.getText();
        ObservableList<Medicament> liste =
            FXCollections.observableArrayList(medService.rechercherParNom(nom));
        tableView.setItems(liste);
    }

    @FXML
    public void voirStockFaible() {
        ObservableList<Medicament> liste =
            FXCollections.observableArrayList(medService.stockFaible(10));
        tableView.setItems(liste);
        alerteLabel.setText("Affichage stock faible (< 10)");
    }

    @FXML
    public void ajouterEmploye() {
        Utilisateur u = new Utilisateur();
        u.setUsername(empUsernameField.getText());
        u.setPassword(empPasswordField.getText());
        u.setRole("EMPLOYE");
        userService.ajouter(u);
        chargerEmployes();
        empUsernameField.clear();
        empPasswordField.clear();
    }

    @FXML
    public void supprimerEmploye() {
        Utilisateur u = employeTable.getSelectionModel().getSelectedItem();
        if (u != null) {
            userService.supprimer(u.getId());
            chargerEmployes();
        }
    }

    @FXML
    public void deconnexion() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/login.fxml"));
            Stage stage = (Stage) tableView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion Pharmacie");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viderChamps() {
        nomField.clear();
        descField.clear();
        prixField.clear();
        quantiteField.clear();
        dateField.clear();
    }
}