/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package helitec.contabilita;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import helitec.contabilita.model.Cantiere;
import helitec.contabilita.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;
	private String fornitoreIF;
	private String fatturaIF;
	private LocalDate dataIF;
	private Integer ivaIF;
	private Cantiere cantiereIF;
	private String lavorazioneIF;
	private String voceCapIF;
	private Double importoLavNoIvaIF;
	private Double importoTotLavIF;
	private Double importoTotIF;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="IFtxtFornitore"
    private TextField IFtxtFornitore; // Value injected by FXMLLoader

    @FXML // fx:id="IFtxtNum"
    private TextField IFtxtNum; // Value injected by FXMLLoader

    @FXML // fx:id="IFdata"
    private DatePicker IFdata; // Value injected by FXMLLoader

    @FXML // fx:id="IFboxIVA"
    private ComboBox<Integer> IFboxIVA; // Value injected by FXMLLoader

    @FXML // fx:id="IFboxCantieri"
    private ComboBox<Cantiere> IFboxCantieri; // Value injected by FXMLLoader

    @FXML // fx:id="IFtxtLavorazione"
    private TextField IFtxtLavorazione; // Value injected by FXMLLoader

    @FXML // fx:id="IFboxVoci"
    private ComboBox<String> IFboxVoci; // Value injected by FXMLLoader

    @FXML // fx:id="IFtxtImportoNoIva"
    private TextField IFtxtImportoNoIva; // Value injected by FXMLLoader

    @FXML // fx:id="IFtxtImportoTot"
    private TextField IFtxtImportoTot; // Value injected by FXMLLoader
    
    @FXML // fx:id="IFtxtImportoTotFattura"
    private TextField IFtxtImportoTotFattura; // Value injected by FXMLLoader
    
    @FXML // fx:id="IFbtnIns"
    private Button IFbtnIns; // Value injected by FXMLLoader

    @FXML // fx:id="IFtxtArea"
    private TextArea IFtxtArea; // Value injected by FXMLLoader

    @FXML // fx:id="IFbtnCanc"
    private Button IFbtnCanc; // Value injected by FXMLLoader

    @FXML // fx:id="IFbtnReset"
    private Button IFbtnReset; // Value injected by FXMLLoader

    @FXML // fx:id="IFbtnConfema"
    private Button IFbtnConfema; // Value injected by FXMLLoader

    @FXML
    void IFcancella(ActionEvent event) {

    }

    @FXML
    void IFconferma(ActionEvent event) {

    }

    @FXML
    void IFinserisci(ActionEvent event) {
    	this.fornitoreIF = this.IFtxtFornitore.getText().trim().toUpperCase();
    	this.fatturaIF = this.IFtxtNum.getText().trim().toUpperCase();
    	this.dataIF = this.IFdata.getValue();
    	this.ivaIF = this.IFboxIVA.getValue();
    	if(this.fornitoreIF==null || this.fatturaIF==null || this.dataIF==null || this.ivaIF==null) {
    		this.IFtxtArea.setText("Inserimento dati non corretto");
    		return;
    	}
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    	String fixed = "Fornitore: " + this.fornitoreIF + "\nFattura n." + this.fatturaIF + 
    			" del " + this.dataIF.format(formatter) + "	IVA =" + this.ivaIF + "%";
    	this.IFtxtArea.setText(fixed);
    	this.cantiereIF = this.IFboxCantieri.getValue();
    	this.lavorazioneIF = this.IFtxtLavorazione.getText().trim().toUpperCase();
    	this.voceCapIF = this.IFboxVoci.getValue();
    	
    	this.IFtxtFornitore.setEditable(false);
    	this.IFtxtNum.setEditable(false);
    	this.IFdata.setEditable(false);
    	this.IFboxIVA.setDisable(true);
    	
    }
    
    @FXML
    void IFsetImportoTot(ActionEvent event) {
    	
    }
  
    @FXML
    void IFreset(ActionEvent event) {
    	IFtxtFornitore.clear();
    	IFtxtArea.clear();
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert IFtxtFornitore != null : "fx:id=\"IFtxtFornitore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFtxtNum != null : "fx:id=\"IFtxtNum\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFdata != null : "fx:id=\"IFdata\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFboxIVA != null : "fx:id=\"IFboxIVA\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFboxCantieri != null : "fx:id=\"IFboxCantieri\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFtxtLavorazione != null : "fx:id=\"IFtxtLavorazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFboxVoci != null : "fx:id=\"IFboxVoci\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFtxtImportoNoIva != null : "fx:id=\"IFtxtImportoNoIva\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFtxtImportoTot != null : "fx:id=\"IFtxtImportoTot\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFtxtImportoTotFattura != null : "fx:id=\"IFtxtImportoTotFattura\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFbtnIns != null : "fx:id=\"IFbtnIns\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFtxtArea != null : "fx:id=\"IFtxtArea\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFbtnCanc != null : "fx:id=\"IFbtnCanc\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFbtnReset != null : "fx:id=\"IFbtnReset\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFbtnConfema != null : "fx:id=\"IFbtnConfema\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel (Model model) {
    	this.model = model;
    	this.IFboxIVA.getItems().clear();
    	this.IFboxIVA.getItems().addAll(0, 4, 10, 22);
    	this.IFboxCantieri.getItems().clear();
    	this.IFboxCantieri.getItems().addAll(model.getCantieri());
    	this.fornitoreIF = null;
    	this.fatturaIF = null;
    	this.dataIF = null;
    	this.ivaIF = null;
    	this.cantiereIF = null;
    	this.lavorazioneIF = null;
    	this.voceCapIF = null;
    	this.importoLavNoIvaIF = null;
    	this.importoTotLavIF = null;
    	this.importoTotIF = null;
    }
    
}
