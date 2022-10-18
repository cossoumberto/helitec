/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package helitec.contabilita;

import java.math.BigDecimal;

import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import helitec.contabilita.model.Cantiere;
import helitec.contabilita.model.Fattura;
import helitec.contabilita.model.Importo;
import helitec.contabilita.model.Lavorazione;
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
	private Fattura f;
	private List<Lavorazione> ll;

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
    
    @FXML // fx:id="IFtxtNoteImporto"
    private TextField IFtxtNoteImporto; // Value injected by FXMLLoader

    @FXML // fx:id="IFtxtArea"
    private TextArea IFtxtArea; // Value injected by FXMLLoader
    
    @FXML // fx:id="IFtxtNoteFattura"
    private TextField IFtxtNoteFattura; // Value injected by FXMLLoader

    @FXML // fx:id="IFbtnCanc"
    private Button IFbtnCanc; // Value injected by FXMLLoader

    @FXML // fx:id="IFbtnReset"
    private Button IFbtnReset; // Value injected by FXMLLoader

    @FXML // fx:id="IFbtnConfema"
    private Button IFbtnConfema; // Value injected by FXMLLoader
    
    @FXML // fx:id="IFbtnCancArea"
    private Button IFbtnCancArea; // Value injected by FXMLLoader

    @FXML
    void IFcancella(ActionEvent event) {
    	if(f!=null && f.getImporti().size()>0 && this.IFtxtArea.getText().equals(f.toStringConImporti())) {
    		f.cancLastImporto();
    		this.IFtxtArea.setText(f.toStringConImporti());
    		this.IFtxtArea.appendText("");
    		this.IFtxtImportoTotFattura.setText(f.getImportoTot().toString());
       	} else if(f!=null)
       		this.IFtxtArea.setText(f.toStringConImporti());
    }
    
    @FXML
    void IFcancellaArea(ActionEvent event) {
    	if(f==null) {
	    	this.IFtxtArea.clear();
	    	this.IFtxtNoteFattura.clear();
	    	this.IFtxtImportoTotFattura.clear();
	    	this.IFbtnCancArea.setDisable(true);
    	}
    }

    @FXML
    void IFconferma(ActionEvent event) {
    	Fattura fnew = null;
    	if(f!=null && f.getImporti().size()>0 && this.IFtxtArea.getText().equals(f.toStringConImporti())) {
    		if(this.IFtxtNoteFattura.getText().trim().length()>0)
    			f.setNote(this.IFtxtNoteFattura.getText().trim().toUpperCase());
    		fnew = new Fattura(f);
    		model.elaboraFattura(fnew, ll);
    		this.IFtxtArea.setText(f.toStringConImporti());
    		this.IFtxtArea.appendText("\n\nFattura caricata correttamente");
    		this.f = null;
    		this.IFtxtFornitore.setEditable(true);
    		this.IFtxtNum.setEditable(true);
    		this.IFdata.setEditable(true);
    		this.IFboxIVA.setDisable(false);
    		this.IFbtnCancArea.setDisable(false);
    	} else if(f!=null && f.getImporti().size()==0)
    		this.IFtxtArea.setText(f.toStringConImporti() + "\n\nInserire importi");
    	else
    		return;
    }

    @FXML
    void IFinserisci(ActionEvent event) {
    	//Gestione inserimento dati comuni della fattura
    	if(f==null || (f!=null && f.getImporti().size()==0)) {
    		f = new Fattura();
    		if(this.IFtxtFornitore.getText().trim().length()!=0)
    				this.f.setFornitore(this.IFtxtFornitore.getText().trim().toUpperCase());
    		if(this.IFtxtNum.getText().trim().length()!=0)
    			this.f.setNumero(this.IFtxtNum.getText().trim().toUpperCase());
    		this.f.setData(this.IFdata.getValue());
    		if(model.verificaIdFattura(f)==true) {
    			f =  null;
    			this.IFtxtArea.setText("Inseriti dati fattura già esistente");
    			return;
    		}
    		this.f.setIva(this.IFboxIVA.getValue());
    		if(this.f.getFornitore()==null || this.f.getNumero()==null || this.f.getData()==null || this.f.getIva()==null) {
    			f = null;
        		this.IFtxtArea.setText("Inserimento dati non corretto");
        		return;
        	}
    	}
    	this.IFtxtArea.setText(f.toStringConImporti());
    	
    	//Gestione inserimento importi numerici
    	Double importoLavNoIva = null;
    	try {
			importoLavNoIva = Double.parseDouble(this.IFtxtImportoNoIva.getText());
			if(importoLavNoIva<0)
				throw new NumberFormatException();
		} catch (NumberFormatException e) {
			this.IFtxtArea.appendText("\n\nInserimento importo non valido");
			return;
			//e.printStackTrace();
		}
    	Double importoLav = null;
    	try {
			importoLav = Double.parseDouble(this.IFtxtImportoTot.getText());
			if(importoLav<0)
				throw new NumberFormatException();
			BigDecimal x = new BigDecimal(importoLav).setScale(2, RoundingMode.HALF_EVEN);
			importoLav = x.doubleValue();
		} catch (NumberFormatException e) {
			this.IFtxtArea.appendText("\n\nInserimento importo non valido");
			return;
			//e.printStackTrace();
		}	
    	
    	//Gestione inserimento lavorazione
    	Lavorazione l = null;
    	if(this.IFboxCantieri.getValue()!=null || this.IFtxtLavorazione.getText().trim().length()!=0) {
    		l = new Lavorazione();
    		if(this.IFboxCantieri.getValue()!=null) 
    			l.setCantiere(this.IFboxCantieri.getValue());
    		if(this.IFtxtLavorazione.getText().trim().length()!=0)
    			l.setDescrizione(this.IFtxtLavorazione.getText().trim().toUpperCase());
    		if(this.IFboxVoci.getValue()!=null)
    			l.setVoceCapitolato(this.IFboxVoci.getValue());
    		if(!ll.contains(l))
    			ll.add(l);
    		else 
    			for(Lavorazione lav : ll)
    				if(lav.equals(l))
    					l = lav;
    	}
    	
    	//Creazione importo per fattura
    	Importo i = null;
    	if(this.IFtxtNoteImporto.getText().trim().length()!=0)
    		i = new Importo(this.f.getImporti().size()+1, f, l, importoLavNoIva, importoLav,
    			this.IFtxtNoteImporto.getText().trim().toUpperCase());
    	else 
    		i = new Importo(this.f.getImporti().size()+1, f, l, importoLavNoIva, importoLav, null);
    	this.f.addImporto(i);
    	
    	//Creazione e visualizzazione risultati + disattivazione input dati comuni fattura 
    	this.IFtxtArea.setText(f.toStringConImporti());
    	this.IFtxtArea.appendText("");
    	this.IFtxtImportoTotFattura.setText(this.f.getImportoTot().toString());
    	this.IFtxtFornitore.setEditable(false);
    	this.IFtxtNum.setEditable(false);
    	this.IFdata.setEditable(false);
    	this.IFboxIVA.setDisable(true);
    }
   
   @FXML
    void IFsetImportoTot(ActionEvent event) {
	   	try {
	   		Double d1 = Double.parseDouble(this.IFtxtImportoNoIva.getText());
	   		if(d1>=0 && this.IFboxIVA.getValue()!=null) {
	   			Double d2 = d1 * (1+this.IFboxIVA.getValue().doubleValue()/100);
	   			BigDecimal x = new BigDecimal(d2).setScale(2, RoundingMode.HALF_EVEN);
	   			this.IFtxtImportoTot.setText(x.toString());
	   		} 
	   	} catch (NumberFormatException e) {
			return;
			//e.printStackTrace();
		}	
    }
  
    @FXML
    void IFreset(ActionEvent event) {
    	this.f = null;
    	this.ll = new ArrayList<>();
    	this.IFtxtFornitore.clear();
    	this.IFtxtFornitore.setEditable(true);
    	this.IFtxtNum.clear();
    	this.IFtxtNum.setEditable(true);
    	this.IFdata.setValue(null);
    	this.IFdata.setEditable(true);
    	this.IFboxIVA.setValue(null);
    	this.IFboxIVA.setDisable(false);
    	this.IFboxCantieri.setValue(null);
    	this.IFtxtLavorazione.clear();
    	this.IFboxVoci.setValue(null);
    	this.IFtxtImportoNoIva.clear();
    	this.IFtxtImportoTot.clear();
    	this.IFtxtNoteImporto.clear();
    	this.IFtxtArea.clear();
    	this.IFtxtNoteFattura.clear();
    	this.IFtxtImportoTotFattura.clear();
    	this.IFbtnCancArea.setDisable(true);
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
        assert IFtxtNoteImporto != null : "fx:id=\"IFtxtNoteImporto\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFtxtArea != null : "fx:id=\"IFtxtArea\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFtxtNoteFattura != null : "fx:id=\"IFtxtNoteFattura\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFbtnCanc != null : "fx:id=\"IFbtnCanc\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFbtnReset != null : "fx:id=\"IFbtnReset\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFbtnConfema != null : "fx:id=\"IFbtnConfema\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFbtnCancArea != null : "fx:id=\"IFbtnCancArea\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel (Model model) {
    	this.model = model;
    	this.IFboxIVA.getItems().clear();
    	this.IFboxIVA.getItems().addAll(0, 4, 10, 22);
    	this.IFboxCantieri.getItems().clear();
    	this.IFboxCantieri.getItems().add(null);
    	this.IFboxCantieri.getItems().addAll(model.getCantieri());
    	this.f = null;
    	this.ll = new ArrayList<>();
    }
    
}
