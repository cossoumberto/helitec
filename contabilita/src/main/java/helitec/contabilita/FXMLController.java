/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package helitec.contabilita;

import java.math.BigDecimal;



import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import helitec.contabilita.model.Cantiere;
import helitec.contabilita.model.Fattura;
import helitec.contabilita.model.Importo;
import helitec.contabilita.model.Lavorazione;
import helitec.contabilita.model.Model;
import helitec.contabilita.model.Pagamento;
import helitec.contabilita.model.PagamentoFattura;
import helitec.contabilita.model.PagamentoFattura.Intero;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class FXMLController {
	
	// TAB INSERISCI FATTURA
	//
	//
	//
	//

	private Fattura f;
	private List<Lavorazione> ll;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="IFtxtRicercaFornitore"
    private TextField IFtxtFornitore; // Value injected by FXMLLoader

    @FXML // fx:id="IFboxFornitori"
    private ComboBox<String> IFboxFornitori; // Value injected by FXMLLoader

    @FXML // fx:id="IFtxtNum"
    private TextField IFtxtNum; // Value injected by FXMLLoader

    @FXML // fx:id="IFdata"
    private DatePicker IFdata; // Value injected by FXMLLoader

    @FXML // fx:id="IFboxIVA"
    private ComboBox<Integer> IFboxIVA; // Value injected by FXMLLoader

    @FXML // fx:id="IFtxtRicercaCantiere"
    private TextField IFtxtRicercaCantiere; // Value injected by FXMLLoader

    @FXML // fx:id="IFboxCantieri"
    private ComboBox<Cantiere> IFboxCantieri; // Value injected by FXMLLoader

    @FXML // fx:id="IFtxtRicercaLavorazione"
    private TextField IFtxtLavorazione; // Value injected by FXMLLoader

    @FXML // fx:id="IFboxLavorazioni"
    private ComboBox<String> IFboxLavorazioni; // Value injected by FXMLLoader

    @FXML // fx:id="IFtxtRicercaVoce"
    private TextField IFtxtRicercaVoce; // Value injected by FXMLLoader

    @FXML // fx:id="IFboxVoci"
    private ComboBox<String> IFboxVoci; // Value injected by FXMLLoader

    @FXML // fx:id="IFtxtImportoNoIva"
    private TextField IFtxtImportoNoIva; // Value injected by FXMLLoader

    @FXML // fx:id="IFtxtImportoTot"
    private TextField IFtxtImportoTot; // Value injected by FXMLLoader

    @FXML // fx:id="IFbtnIns"
    private Button IFbtnIns; // Value injected by FXMLLoader

    @FXML // fx:id="IFtxtNoteImporto"
    private TextField IFtxtNoteImporto; // Value injected by FXMLLoader

    @FXML // fx:id="IFtxtArea"
    private TextArea IFtxtArea; // Value injected by FXMLLoader

    @FXML // fx:id="IFtxtNoteFattura"
    private TextField IFtxtNoteFattura; // Value injected by FXMLLoader

    @FXML // fx:id="IFtxtImportoTotFattura"
    private TextField IFtxtImportoTotFattura; // Value injected by FXMLLoader

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
    		int n = 0;
    		for(Importo i : f.getImporti())
    			if(f.getImporti().get(f.getImporti().size()-1).getLavorazione().equals(i.getLavorazione()))
    				n++;
    		if(n<=1)
    			ll.remove(ll.get(ll.indexOf(f.getImporti().get(f.getImporti().size()-1).getLavorazione())));
    		f.cancLastImporto();
    		this.IFtxtArea.setText(f.toStringConImporti());
    		this.IFtxtArea.appendText("");
    		this.IFtxtImportoTotFattura.setText(f.getImportoTot().toString());
       	} else if(f!=null)
       		this.IFtxtArea.setText(f.toStringConImporti());
    }
    
    @FXML
    void IFcancellaArea(ActionEvent event) {
    	if(f==null && ll.size()==0) {
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
    		if(this.IFtxtNoteFattura.getText().trim().length()>0 && this.IFtxtNoteFattura.getText().trim().length()<=400)
    			f.setNote(this.IFtxtNoteFattura.getText().trim().toUpperCase());
    		else if(this.IFtxtNoteFattura.getText().trim().length()>400) {
    			this.IFtxtArea.appendText("\n\nInserire max 400 caratteri in Note Fattura");
    			return;
    		}
    		fnew = new Fattura(f);
    		Integer i = model.elaboraFattura(fnew, ll);
    		this.IFtxtArea.setText(f.toStringConImporti());
    		this.IFtxtArea.appendText("\n\nFattura caricata correttamente");
    		if(i==null)
    			this.IFtxtArea.appendText("\nPagamento da caricare");
    		else if(i==0)
    			this.IFtxtArea.appendText("\nErrore: incongruenza importo fattura - dati pagamento esistente");
    		else if(i==1)
    			this.IFtxtArea.appendText(" e attribuita a pagamento esistente");
    		else if(i==2)
    			this.IFtxtArea.appendText("\nImpossibile attrubuire pagamento alla fattura senza importo pagamento relativo");
    		this.f = null;
    		this.ll = new ArrayList<>();
    		this.IFtxtFornitore.setEditable(true);
    		this.IFboxFornitori.setDisable(false);
    		this.IFtxtNum.setEditable(true);
    		this.IFdata.setDisable(false);
    		this.IFboxIVA.setDisable(false);
    		this.IFbtnCancArea.setDisable(false);
    		//Aggiornamento dati input altre sezioni
    		this.FAresetDatiInput();
    		this.FAtxtArea.clear();
    		this.FAboxFatture.getItems().clear();
    		this.FAboxFatture.getItems().addAll(model.getFatture());
    	} else if(f!=null && f.getImporti().size()==0)
    		this.IFtxtArea.setText(f.toStringConImporti() + "\n\nInserire importi");
    	else
    		return;
    }
    
    @FXML
    void IFinserisci(ActionEvent event) {
    	//Gestione inserimento dati comuni fattura
    	if(f==null || (f!=null && f.getImporti().size()==0)) {
    		f = new Fattura();
    		if(this.IFboxFornitori.getValue()!=null)
    			this.f.setFornitore(this.IFboxFornitori.getValue());
    		else if(this.IFtxtFornitore.getText().trim().length()>0 && this.IFtxtFornitore.getText().trim().length()<=50) {
    			if(this.IFboxFornitori.getItems().contains(this.IFtxtFornitore.getText().trim().toUpperCase())) {
    				this.IFtxtArea.setText("Selezionare fornitore esistente");
    				return;
    			} else 
    				this.f.setFornitore(this.IFtxtFornitore.getText().trim().toUpperCase());
    		}
    		if(this.IFtxtNum.getText().trim().length()>0 && this.IFtxtNum.getText().trim().length()<=50)
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
    	if(this.IFboxLavorazioni.getValue()!=null || this.IFtxtLavorazione.getText().trim().length()>0 && this.IFtxtLavorazione.getText().trim().length()<=100) {
    		l = new Lavorazione();
    		if(this.IFboxCantieri.getValue()!=null) 
    			l.setCantiere(this.IFboxCantieri.getValue());
    		if(this.IFboxLavorazioni.getValue()!=null)
    			l.setDescrizione(this.IFboxLavorazioni.getValue());
    		else {
    			if(this.IFtxtLavorazione.getText().trim().length()>0 && this.IFtxtLavorazione.getText().trim().length()<=100 &&
    					!this.IFboxLavorazioni.getItems().contains(this.IFtxtLavorazione.getText().trim().toUpperCase()))
    			l.setDescrizione(this.IFtxtLavorazione.getText().trim().toUpperCase());
    			else if(this.IFtxtLavorazione.getText().trim().length()==0) {
    	    		this.IFtxtArea.appendText("\n\nInserire lavorazione");
    				return;
    			} else if (this.IFtxtLavorazione.getText().trim().length()>100) {
    				this.IFtxtArea.appendText("\n\nInserire descrizine lavorazione di max 100 caratteri");
    				return;
    			} else if(this.IFboxLavorazioni.getItems().contains(this.IFtxtLavorazione.getText().trim().toUpperCase())){
    				this.IFtxtArea.appendText("\n\nSelezionare lavorazione tra quelle esistenti");
    				return;
    			}
    		}
    		if(this.IFboxVoci.getValue()!=null)
    			l.setVoceCapitolato(this.IFboxVoci.getValue());
    		if(!ll.contains(l))
    			ll.add(l);
    		l = ll.get(ll.indexOf(l));
    	}
    	
    	//Creazione importo per fattura
    	Importo i = null;
    	if(this.IFtxtNoteImporto.getText().trim().length()>0 && this.IFtxtNoteImporto.getText().trim().length()<=400)
    		i = new Importo(this.f.getImporti().size()+1, f, l, importoLavNoIva, importoLav,
    			this.IFtxtNoteImporto.getText().trim().toUpperCase());
    	else if(this.IFtxtNoteImporto.getText().trim().length()>400) {
    		this.IFtxtArea.appendText("\n\nInserire max 400 caratteri in Note");
			return;
    	} else
    		i = new Importo(this.f.getImporti().size()+1, f, l, importoLavNoIva, importoLav, null);
    	this.f.addImporto(i);
    	
    	//Creazione e visualizzazione risultati + disattivazione input dati comuni fattura 
    	this.IFtxtArea.setText(f.toStringConImporti());
    	this.IFtxtArea.appendText("");
    	this.IFtxtImportoTotFattura.setText(this.f.getImportoTot().toString());
    	this.IFtxtFornitore.setEditable(false);
    	this.IFboxFornitori.setDisable(true);
    	this.IFtxtNum.setEditable(false);
    	this.IFdata.setDisable(true);
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
    	this.IFboxFornitori.getItems().clear();
    	this.IFboxFornitori.getItems().addAll(model.getFornitori());
    	this.IFboxFornitori.setDisable(false);
    	this.IFtxtFornitore.setEditable(true);
    	this.IFtxtNum.clear();
    	this.IFtxtNum.setEditable(true);
    	this.IFdata.setValue(null);
    	this.IFdata.setDisable(false);
    	this.IFboxIVA.setValue(null);
    	this.IFboxIVA.setDisable(false);
    	this.IFtxtRicercaCantiere.clear();
    	this.IFboxCantieri.setValue(null);
    	this.IFboxCantieri.getItems().clear();
    	this.IFboxCantieri.getItems().add(null);
    	this.IFboxCantieri.getItems().addAll(model.getCantieri());
    	this.IFtxtLavorazione.clear();
    	this.IFboxLavorazioni.getItems().clear();
    	this.IFboxLavorazioni.getItems().addAll(model.getDescrizioniLavorazioni());
    	this.IFtxtRicercaVoce.clear();
    	this.IFboxVoci.getItems().clear();
    	this.IFboxVoci.getItems().add(null);
    	this.IFboxVoci.getItems().addAll(model.getVociCapitolato());
    	this.IFtxtImportoNoIva.clear();
    	this.IFtxtImportoTot.clear();
    	this.IFtxtNoteImporto.clear();
    	this.IFtxtArea.clear();
    	this.IFtxtNoteFattura.clear();
    	this.IFtxtImportoTotFattura.clear();
    	this.IFbtnCancArea.setDisable(true);
    }
    
    @FXML
    void IFricercaCantiere(KeyEvent event) {
    	if(this.IFtxtRicercaCantiere.getText().trim().length()>0) {
	    	String ins = this.IFtxtRicercaCantiere.getText().trim().toUpperCase();
	    	List<Cantiere> list = new ArrayList<>();
	    	for(Cantiere c : model.getCantieri())
	    		if(c.toString().contains(ins))
	    			list.add(c);
	    	this.IFboxCantieri.getItems().clear();
	    	this.IFboxCantieri.getItems().add(null);
	    	this.IFboxCantieri.getItems().addAll(list);
    	} else {
    		this.IFboxCantieri.getItems().clear();
	    	this.IFboxCantieri.getItems().add(null);
	    	this.IFboxCantieri.getItems().addAll(model.getCantieri());
    	}
    }
    
    @FXML
    void IFricercaFornitore(KeyEvent event) {
    	if(this.IFtxtFornitore.getText().trim().length()>0) {
	    	String ins = this.IFtxtFornitore.getText().trim().toUpperCase();
	    	List<String> list = new ArrayList<>();
	    	for(String s : model.getFornitori())
	    		if(s.contains(ins))
	    			list.add(s);
	    	this.IFboxFornitori.getItems().clear();
	    	this.IFboxFornitori.getItems().add(null);
	    	this.IFboxFornitori.getItems().addAll(list);
    	} else {
    		this.IFboxFornitori.getItems().clear();
	    	this.IFboxFornitori.getItems().add(null);
	    	this.IFboxFornitori.getItems().addAll(model.getFornitori());
    	}
    }

    @FXML
    void IFricercaLavorazione(KeyEvent event) {
    	if(this.IFtxtLavorazione.getText().trim().length()>0) {
	    	String ins = this.IFtxtLavorazione.getText().trim().toUpperCase();
	    	List<String> list = new ArrayList<>();
	    	for(String s : model.getDescrizioniLavorazioni())
	    		if(s.contains(ins))
	    			list.add(s);
	    	this.IFboxLavorazioni.getItems().clear();
	    	this.IFboxLavorazioni.getItems().add(null);
	    	this.IFboxLavorazioni.getItems().addAll(list);
    	} else {
    		this.IFboxLavorazioni.getItems().clear();
	    	this.IFboxLavorazioni.getItems().add(null);
	    	this.IFboxLavorazioni.getItems().addAll(model.getDescrizioniLavorazioni());
    	}
    }
    
    @FXML
    void IFricercaVoci(KeyEvent event) {
    	if(this.IFtxtRicercaVoce.getText().trim().length()>0) {
	    	String ins = this.IFtxtRicercaVoce.getText().trim().toUpperCase();
	    	List<String> list = new ArrayList<>();
	    	for(String s : model.getVociCapitolato())
	    		if(s.contains(ins))
	    			list.add(s);
	    	this.IFboxVoci.getItems().clear();
	    	this.IFboxVoci.getItems().add(null);
	    	this.IFboxVoci.getItems().addAll(list);
    	} else {
    		this.IFboxVoci.getItems().clear();
	    	this.IFboxVoci.getItems().add(null);
	    	this.IFboxVoci.getItems().addAll(model.getVociCapitolato());
    	}
    }
    
    
    //TAB INSERISCI PAGAMENTO 
    //
    //
    //
    
    private Pagamento p;
    
    @FXML // fx:id="IPtxtFornitore"
    private TextField IPtxtFornitore; // Value injected by FXMLLoader

    @FXML // fx:id="IPdata"
    private DatePicker IPdata; // Value injected by FXMLLoader

    @FXML // fx:id="IPtxtImporto"
    private TextField IPtxtImporto; // Value injected by FXMLLoader

    @FXML // fx:id="IPboxFatture"
    private ComboBox<Fattura> IPboxFatture; // Value injected by FXMLLoader

    @FXML // fx:id="IPtxtImportoRel"
    private TextField IPtxtImportoRel; // Value injected by FXMLLoader

    @FXML // fx:id="IPtxtNumFattura"
    private TextField IPtxtNumFattura; // Value injected by FXMLLoader

    @FXML // fx:id="IPboxIntero"
    private ComboBox<String> IPboxIntero; // Value injected by FXMLLoader

    @FXML // fx:id="IPbtnInserisci"
    private Button IPbtnInserisci; // Value injected by FXMLLoader
    
    @FXML // fx:id="IPtxtNotePagFatt"
    private TextField IPtxtNotePagFatt; // Value injected by FXMLLoade

    @FXML // fx:id="IPtxtArea"
    private TextArea IPtxtArea; // Value injected by FXMLLoader

    @FXML // fx:id="IPbtnCanc"
    private Button IPbtnCanc; // Value injected by FXMLLoader

    @FXML // fx:id="IPbtnReset"
    private Button IPbtnReset; // Value injected by FXMLLoader

    @FXML // fx:id="IPbtnConferma"
    private Button IPbtnConferma; // Value injected by FXMLLoader

    @FXML
    void IPcancella(ActionEvent event) {
    	if(p!=null && p.getFatture().size()>0 && this.IPtxtArea.getText().equals(p.toStringConFatture())){
    		p.cancLastFattura();
    		this.IPtxtArea.setText(p.toStringConFatture());
    	} else if(p!=null)
    		this.IPtxtArea.setText(p.toStringConFatture());
    }

    @FXML
    void IPconferma(ActionEvent event) {
    	if(this.p!=null && p.getFatture().size()>0 && this.IPtxtArea.getText().equals(p.toStringConFatture())) {
    		List<String> stamp = model.elaboraPagamento(p);
    		this.IPtxtArea.appendText("\n");
    		for(String s : stamp) {
    			this.IPtxtArea.appendText("\n" + (stamp.indexOf(s)+1) + ") " + s);
    		}
    		p = null;
    		this.IPtxtFornitore.setEditable(true);
    		this.IPdata.setDisable(false);
    		this.IPtxtImporto.setEditable(true);
    	}
    }

    @FXML
    void IPinserisci(ActionEvent event) { //gestire vincoli date
    	//Gestione inserimento dati comuni pagamento
    	if(this.p==null) {
    		p = new Pagamento();
    		if(this.IPtxtFornitore.getText().trim().length()>0)
    			p.setFornitore(this.IPtxtFornitore.getText().trim().toUpperCase());
    		p.setData(this.IPdata.getValue());
    		if(p.getFornitore()==null || p.getData()==null) {
    			p = null;
    			this.IPtxtArea.setText("Inserimento dati non corretto");
    			return;
    		}
    		if(model.getPagamenti().contains(p)) {
    			p = null;
    			this.IPtxtArea.setText("Pagamento già inserito");
    			return;
    		}
        	Double importo = null;
        	try {
    			importo = Double.parseDouble(this.IPtxtImporto.getText());
    			if(importo<0)
    				throw new NumberFormatException();
       		} catch (NumberFormatException e) {
    			p = null;
    			this.IPtxtArea.setText("Inserimento importo non valido");
    			return;
    			//e.printStackTrace();
    		}
        	p.setImporto(importo);
    	}
    	
    	//Gestione inserimento dati importo relativo
    	this.IPtxtArea.setText(p.toStringConFatture());
    	if(this.IPboxFatture.getValue()==null && this.IPtxtNumFattura.getText().length()==0){
			this.IPtxtArea.appendText("\n\nInserire fatture");
			return;
    	}
    	Fattura f = null;
    	if(this.IPboxFatture.getValue()==null) {
    		f = new Fattura();
    		f.setFornitore(p.getFornitore());
    		f.setNumero(this.IPtxtNumFattura.getText().trim());
    		for(Fattura f2 : this.IPboxFatture.getItems())
    			if(f2!=null && f2.getNumero().equals(f.getNumero())){
    	    		this.IPtxtArea.appendText("\n\nSelezionare fattura n." + f.getNumero() + " da elenco fattura inserite");
    	    		return;
    	    	}
    	} else 
    		f = this.IPboxFatture.getValue();
    	for(PagamentoFattura pf : p.getFatture())
	    	if(pf.getFattura().equals(f)) {
	    		this.IPtxtArea.appendText("\n\nFattura già inserita");
	    		return;
	    	}
    	Double importoRel = null;
    	if(this.IPtxtImportoRel.getText().length()>0) {
	    	try {
				importoRel = Double.parseDouble(this.IPtxtImportoRel.getText());
				if(importoRel<0 || importoRel + p.getSommaImportiRelativi() > p.getImporto() || 
						(f.getImportoTot()!=null && importoRel > f.getImportoTot()) )
					throw new NumberFormatException();
	   		} catch (NumberFormatException e) {
				this.IPtxtArea.appendText("\n\nInserimento importo relativo non valido");
				return;
				//e.printStackTrace();
			}
    	}
    	Intero intero = Intero.INTERO;
    	if(this.IPboxIntero.getValue()==null)
    		intero = null;
    	else if(this.IPboxIntero.getValue().equals("Acconto"))
    		intero = Intero.ACCONTO;
    	else if(this.IPboxIntero.getValue().equals("Saldo"))
    		intero = Intero.SALDO;
    	if(f.getData()!=null && f.getData().isAfter(p.getData()) && !intero.equals(Intero.ACCONTO)) {
    		this.IPtxtArea.appendText("\n\nImpossibile inserire pagamento (che non sia acconto) con data successiva a emissione fattura");
    		return;
    	}
    	String note = null;
    	if(this.IPtxtNotePagFatt.getText().length()>0 && this.IPtxtNotePagFatt.getText().length()<=400)
    		note = this.IPtxtNotePagFatt.getText();
    	else if(this.IPtxtNotePagFatt.getText().length()>400) {
    		this.IPtxtArea.appendText("\n\nInserire max 400 caratteri in Note Pagamento Fattura");
			return;
    	}
    	//Creazione e visualizzazione risultati + disattivazione input dati comuni
    	PagamentoFattura pf = new PagamentoFattura(f, p, importoRel, intero, note);
    	p.aggiungiPagFattura(pf);
    	this.IPtxtArea.setText(p.toStringConFatture());
    	this.IPtxtFornitore.setEditable(false);
    	this.IPdata.setDisable(true);;
    	this.IPtxtImporto.setEditable(false);
    }

    @FXML
    void IPreset(ActionEvent event) {
    	this.p = null;
    	this.IPtxtFornitore.clear();
    	this.IPtxtFornitore.setEditable(true);
    	this.IPdata.setValue(null);
    	this.IPdata.setDisable(false);
    	this.IPtxtImporto.setEditable(true);
    	this.IPtxtImporto.clear();
    	this.IPtxtNumFattura.clear();
    	this.IPboxFatture.setValue(null);
    	this.IPboxFatture.getItems().clear();
    	this.IPtxtImportoRel.clear();
    	this.IPtxtNotePagFatt.clear();
    	this.IPtxtArea.clear();
   	}

    @FXML
    void IPricercaFattureForn(KeyEvent event) {
    	this.IPboxFatture.getItems().clear();
    	if(this.IPtxtFornitore.getText().trim().length()>0 && !model.getFornitori().contains(this.IPtxtFornitore.getText().trim().toUpperCase()))
    			this.IPtxtArea.setText("Fornitore inserito non esistente");
    		
    	else {
    		this.IPtxtArea.clear();
    		this.IPboxFatture.getItems().add(null);
    		this.IPboxFatture.getItems().addAll(model.getFattureFornitoreDaPag(this.IPtxtFornitore.getText().trim().toUpperCase()));
    	}
    }
    
    @FXML
    void IPsetImportoRel(ActionEvent event) {
    	if(this.IPboxFatture.getValue()!=null)
    		this.IPtxtImportoRel.setText(this.IPboxFatture.getValue().getImportoTot().toString());
    }
    
    //TAB FATTURE
    
    private List<String> forn;
    private List<String> lastFornList;
    private List<Cantiere> cant;
    private List<Cantiere> lastCantList;
    private List<String> lav;
    private List<String> lastLavList;
    private List<String> voci;
    private List<String> lastVociList;
    private LocalDate max;
    private LocalDate min;
    private Map<Integer, InputType> inputList;
    private String nd;
    private Fattura f2;
    
    public enum InputType { Fornitore, Cantiere, Lavorazione, VoceCap}
    
    @FXML // fx:id="FAboxForn"
    private ComboBox<String> FAboxForn; // Value injected by FXMLLoader

    @FXML // fx:id="FAdataDa"
    private DatePicker FAdataDa; // Value injected by FXMLLoader

    @FXML // fx:id="FAdataA"
    private DatePicker FAdataA; // Value injected by FXMLLoader

    @FXML // fx:id="FAbtnInserisci"
    private Button FAbtnInserisci; // Value injected by FXMLLoader

    @FXML // fx:id="FAboxCant"
    private ComboBox<Cantiere> FAboxCant; // Value injected by FXMLLoader

    @FXML // fx:id="FAboxLav"
    private ComboBox<String> FAboxLav; // Value injected by FXMLLoader

    @FXML // fx:id="FAboxVoce"
    private ComboBox<String> FAboxVoce; // Value injected by FXMLLoader

    @FXML // fx:id="FAbtnRimuovi"
    private Button FAbtnRimuovi; // Value injected by FXMLLoader

    @FXML // fx:id="FAtxtRicercaLav"
    private TextField FAtxtRicercaLav; // Value injected by FXMLLoader

    @FXML // fx:id="FAtxtRicercaVoce"
    private TextField FAtxtRicercaVoce; // Value injected by FXMLLoader

    @FXML // fx:id="FAtxtRicercaCant"
    private TextField FAtxtRicercaCant; // Value injected by FXMLLoader

    @FXML // fx:id="FAtxtRicercaForn"
    private TextField FAtxtRicercaForn; // Value injected by FXMLLoader
    
    @FXML // fx:id="FAbtnResetFiltri"
    private Button FAbtnResetFiltri; // Value injected by FXMLLoader
    
    @FXML // fx:id="FAbtnResetDate"
    private Button FAbtnResetDate; // Value injected by FXMLLoader

    @FXML // fx:id="FAtxtArea"
    private TextArea FAtxtArea; // Value injected by FXMLLoader

    @FXML // fx:id="FAtxtImportoFattura"
    private TextField FAtxtImportoFattura; // Value injected by FXMLLoader

    @FXML // fx:id="FAboxFatture"
    private ComboBox<Fattura> FAboxFatture; // Value injected by FXMLLoader
    
    @FXML // fx:id="FAbtnAnnulla"
    private Button FAbtnAnnulla; // Value injected by FXMLLoader

    @FXML // fx:id="FAtxtNoteFattura"
    private TextField FAtxtNoteFattura; // Value injected by FXMLLoader

    @FXML // fx:id="FAboxImporti"
    private ComboBox<Importo> FAboxImporti; // Value injected by FXMLLoader

    @FXML // fx:id="FAboxCant2"
    private ComboBox<Cantiere> FAboxCant2; // Value injected by FXMLLoader

    @FXML // fx:id="FAtxtLavorazione"
    private TextField FAtxtLavorazione; // Value injected by FXMLLoader
    
    @FXML // fx:id="FAboxLavorazioni2"
    private ComboBox<String> FAboxLavorazioni2; // Value injected by FXMLLoader

    @FXML // fx:id="FAtxtRicercaVoce2"
    private TextField FAtxtRicercaVoce2; // Value injected by FXMLLoader

    @FXML // fx:id="FAboxVoce2"
    private ComboBox<String> FAboxVoce2; // Value injected by FXMLLoader

    @FXML // fx:id="FAbtnCanImporto"
    private Button FAbtnCanImporto; // Value injected by FXMLLoader

    @FXML // fx:id="FAtxtImportNoIva"
    private TextField FAtxtImportNoIva; // Value injected by FXMLLoader

    @FXML // fx:id="FAtxtImportoTot"
    private TextField FAtxtImportoTot; // Value injected by FXMLLoader

    @FXML // fx:id="FAbtnInserisci2"
    private Button FAbtnInserisci2; // Value injected by FXMLLoader

    @FXML // fx:id="FAtxtRicercaCant2"
    private TextField FAtxtRicercaCant2; // Value injected by FXMLLoader

    @FXML // fx:id="FAtxtNoteImporto"
    private TextField FAtxtNoteImporto; // Value injected by FXMLLoader

    @FXML // fx:id="FAbtnConferma"
    private Button FAbtnConferma; // Value injected by FXMLLoader

    @FXML // fx:id="FAbtnReset"
    private Button FAbtnReset; // Value injected by FXMLLoader
    
    @FXML
    void FAannulla(ActionEvent event) {
    	if(this.FAboxFatture.getValue()!=null && this.FAtxtArea.getText().equals(this.FAboxFatture.getValue().toStringConImporti())) {
    		this.stampaOutputRicerca(this.FAboxFatture.getItems());
    		this.FAboxFatture.setValue(null);
    		this.FAtxtImportoFattura.clear();
    		this.FAtxtNoteFattura.clear();
    	}
    }
    
    @FXML
    void FAricercaForn(KeyEvent event) {
    	if(this.FAtxtRicercaForn.getText().trim().length()>0) {
	    	String ins = this.FAtxtRicercaForn.getText().trim().toUpperCase();
	    	List<String> list = new ArrayList<>();
	    	for(String s : this.lastFornList)
	    		if(s!=null && s.contains(ins))
	    			list.add(s);
	    	this.FAboxForn.getItems().clear();
	    	this.FAboxForn.getItems().add(null);
	    	this.FAboxForn.getItems().addAll(list);
    	} else {
    		this.FAboxForn.getItems().clear();
    		this.FAboxForn.getItems().addAll(this.lastFornList);
    	}
    }
   
    @FXML
    void FAricercaCant(KeyEvent event) {
    	if(this.FAtxtRicercaCant.getText().trim().length()>0) {
	    	String ins = this.FAtxtRicercaCant.getText().trim().toUpperCase();
	    	List<Cantiere> list = new ArrayList<>();
	    	for(Cantiere c : this.lastCantList)
	    		if(c!=null && c.toString().contains(ins))
	    			list.add(c);
	    	this.FAboxCant.getItems().clear();
	    	this.FAboxCant.getItems().add(null);
	    	this.FAboxCant.getItems().addAll(list);
    	} else {
    		this.FAboxCant.getItems().clear();
	    	this.FAboxCant.getItems().addAll(this.lastCantList);
    	}
    }

    @FXML
    void FAricercaLav(KeyEvent event) {
    	if(this.FAtxtRicercaLav.getText().trim().length()>0) {
	    	String ins = this.FAtxtRicercaLav.getText().trim().toUpperCase();
	    	List<String> list = new ArrayList<>();
	    	for(String s : this.lastLavList)
	    		if(s!=null && s.contains(ins))
	    			list.add(s);
	    	this.FAboxLav.getItems().clear();
	    	this.FAboxLav.getItems().add(null);
	    	this.FAboxLav.getItems().addAll(list);
    	} else {
    		this.FAboxLav.getItems().clear();
    		this.FAboxLav.getItems().addAll(this.lastLavList);
    	}
    }

    @FXML
    void FAricercaVoce(KeyEvent event) {
    	if(this.FAtxtRicercaVoce.getText().trim().length()>0) {
		    String ins = this.FAtxtRicercaVoce.getText().trim().toUpperCase();
		   	List<String> list = new ArrayList<>();
		   	for(String s : this.lastVociList)
		   		if(s!=null && s.contains(ins))
		   			list.add(s);
	    	this.FAboxVoce.getItems().clear();
	    	this.FAboxVoce.getItems().add(null);
	    	this.FAboxVoce.getItems().addAll(list);
	    } else {
			this.FAboxVoce.getItems().clear();
			this.FAboxVoce.getItems().addAll(this.lastVociList);
	    }
    }

    @FXML
    void FAsetFattura(ActionEvent event) {
    	if(this.FAboxFatture.getValue()!=null) {
    		this.FAresetDatiImporto();
    		f2 = new Fattura(this.FAboxFatture.getValue());
    		this.f2.rimuoviImporti();
    		for(Importo i : this.FAboxFatture.getValue().getImporti())
    			f2.addImporto(new Importo(i));
    		this.FAtxtArea.setText(f2.toStringConImporti());
    		this.FAtxtImportoFattura.setText(f2.getImportoTot().toString());
    		this.FAtxtNoteFattura.setDisable(false);
    		this.FAtxtNoteFattura.setText(f2.getNote());
    		this.FAboxImporti.setDisable(false);
    		this.FAboxImporti.getItems().clear();
    		this.FAboxImporti.getItems().add(new Importo(null, null, null, null, null, null));
    		this.FAboxImporti.getItems().addAll(f2.getImporti());
    	} else {
    		this.FAtxtNoteFattura.setDisable(true);
    		this.FAboxImporti.setDisable(true);
    	}
    }

    @FXML
    void FAcancImporto(ActionEvent event) {
    	if(this.FAboxImporti.getValue()!=null && f2!=null && f2.getImporti().size()>0 
    			&& this.FAtxtArea.getText().equals(this.f2.toStringConImporti())) {
    		f2.cancImporto(this.FAboxImporti.getValue());
    		this.FAtxtArea.setText(f2.toStringConImporti());
    		this.FAboxImporti.getItems().clear();
    		this.FAboxImporti.getItems().addAll(f2.getImporti());
    		this.FAtxtImportoFattura.setText(f2.getImportoTot().toString());
    		if(f2.getImporti().size()>0) {
            	this.FAbtnConferma.setDisable(false);
        	}
    	} else if(this.FAboxImporti.getValue()!=null && f2!=null) {
    		this.FAtxtArea.setText(f2.toStringConImporti());
    	}
    }

    @FXML
    void FAconferma(ActionEvent event) {
    	if(f2!=null && f2.getImporti().size()>0 && this.FAtxtArea.getText().equals(f2.toStringConImporti())) {
    		if(this.FAtxtNoteFattura.getText()!=null 
    				&& this.FAtxtNoteFattura.getText().trim().length()>0 && this.FAtxtNoteFattura.getText().trim().length()<=400)
    			f2.setNote(this.FAtxtNoteFattura.getText().trim().toUpperCase());
    		else if(this.FAtxtNoteFattura.getText()!=null && this.FAtxtNoteFattura.getText().trim().length()>400) {
    			this.FAtxtArea.appendText("\n\nInserire max 400 caratteri in Note Fattura");
    			return;
    		}
    		if(this.FAboxFatture.getValue().equalsTotale(f2))
				this.FAtxtArea.appendText("\n\nNessuna modifica effettuata");
			else {
	    		model.aggiornaFattura(this.FAboxFatture.getValue(), this.f2);
	    		this.FAboxImporti.getItems().clear();
	    		this.FAboxImporti.getItems().addAll(f2.getImporti());
	    		this.FAtxtImportoFattura.setText(f2.getImportoTot().toString());
	    		this.FAresetDatiInput();
	    		this.FAresetDatiImporto();  
	    		this.FAtxtArea.appendText("\n\nFattura aggiornata correttamente");
			}
    	}
    }

    @FXML
    void FAinserisci(ActionEvent event) {
    	//Memorizzazione input
    	if(this.FAdataDa.getValue()!=null || this.FAdataA.getValue()!=null) {
    		if(this.FAdataDa.getValue()!=null)
    			min = this.FAdataDa.getValue();
    		if(this.FAdataA.getValue()!=null)
    			max = this.FAdataA.getValue();
    		if(min!=null && max!=null && min.isAfter(max)) {
    			this.FAtxtArea.setText("Errore inserimento date");
    			this.FAresetDate();
    			return;
    		}
    	}
    	if(this.FAboxForn.getValue()!=null && !this.forn.contains(this.FAboxForn.getValue())) {
    		forn.add(this.FAboxForn.getValue());
    		this.inputList.put(inputList.size(), InputType.Fornitore);
    	}
    	if(this.FAboxCant.getValue()!=null) {
    		Cantiere c = null;
			if(this.FAboxCant.getValue().getNumero()!=null)
				c = this.FAboxCant.getValue();
			if(!this.cant.contains(c)) {
				cant.add(c);
				this.inputList.put(inputList.size(), InputType.Cantiere);
			}
    	}
    	if(this.FAboxLav.getValue()!=null) {
			String s = null;
			if(!this.FAboxLav.getValue().equals(nd))
				s = this.FAboxLav.getValue();
			if(!lav.contains(s)) {
				lav.add(s);
				this.inputList.put(inputList.size(), InputType.Lavorazione);	
			}
    	}
    	if(this.FAboxVoce.getValue()!=null) {
    		String s = null;
			if(!this.FAboxVoce.getValue().equals(nd))
				s = this.FAboxVoce.getValue();
			if(!voci.contains(s)) {
				voci.add(s);
				this.inputList.put(inputList.size(), InputType.VoceCap);
			}
    	}
	    //Recupero le fatture richieste
    	List<Fattura> list = model.getFattureRichieste(this.forn, this.cant, this.lav, this.voci, 
    			min, max, inputList);
    	//Verifico esistenza fatture
    	if(list.size()==0) {
    		this.FAtxtArea.setText("Fatture non trovate");
    		this.FAresetDate();
    	} else {
	    	//Aggiorna box input
	    	this.rimuoviInputExtraAggiornaBox(list);
	    	//Stampa
	    	this.stampaOutputRicerca(list);
    	}
    }
    
    @FXML
    void FAinserisciImporto(ActionEvent event) {
    		//Gestione inserimento importi numerici
        	Double importoLavNoIva = null;
        	try {
    			importoLavNoIva = Double.parseDouble(this.FAtxtImportNoIva.getText());
    			if(importoLavNoIva<0)
    				throw new NumberFormatException();
    		} catch (NumberFormatException e) {
    			this.FAtxtArea.appendText("\n\nInserimento importo non valido");
    			return;
    			//e.printStackTrace();
    		}
        	Double importoLav = null;
        	try {
    			importoLav = Double.parseDouble(this.FAtxtImportoTot.getText());
    			if(importoLav<0)
    				throw new NumberFormatException();
    			BigDecimal x = new BigDecimal(importoLav).setScale(2, RoundingMode.HALF_EVEN);
    			importoLav = x.doubleValue();
    		} catch (NumberFormatException e) {
    			this.FAtxtArea.appendText("\n\nInserimento importo non valido");
    			return;
    			//e.printStackTrace();
    		}	
    		//Gestione lavorazione
    		Lavorazione l = null;
    		l = new Lavorazione();
    		if(this.FAboxLavorazioni2.getValue()!=null)
    			l.setDescrizione(this.FAboxLavorazioni2.getValue());
        	else {
        			if(this.FAtxtLavorazione.getText().trim().length()>0 && this.FAtxtLavorazione.getText().trim().length()<=100 &&
        					!this.FAboxLavorazioni2.getItems().contains(this.FAtxtLavorazione.getText().trim().toUpperCase())) {
        			l.setDescrizione(this.FAtxtLavorazione.getText().trim().toUpperCase());
        			} else if(this.FAtxtLavorazione.getText().trim().length()==0) {
        	    		this.FAtxtArea.appendText("\n\nInserire lavorazione");
        				return;
        			} else if (this.FAtxtLavorazione.getText().trim().length()>100) {
        				this.FAtxtArea.appendText("\n\nInserire descrizine lavorazione di max 100 caratteri");
        				return;
        			} else if(this.FAboxLavorazioni2.getItems().contains(this.FAtxtLavorazione.getText().trim().toUpperCase())){
        				this.FAtxtArea.appendText("\n\nSelezionare lavorazione tra quelle esistenti");
        				return;
        			}
    		}
    		if(this.FAboxCant2.getValue()!=null) 
    			l.setCantiere(this.FAboxCant2.getValue());
    		if(this.FAboxVoce2.getValue()!=null)
    			l.setVoceCapitolato(this.FAboxVoce2.getValue());
    		String note = null;
    		if(this.FAtxtNoteImporto.getText()!=null && this.FAtxtNoteImporto.getText().trim().length()>0)
    			note = this.FAtxtNoteImporto.getText().trim().toUpperCase();
    		if(this.FAboxImporti.getValue()!=null && this.FAboxImporti.getValue().getNumero()!=null) {
        		this.f2.getImporti().get(this.f2.getImporti().indexOf(this.FAboxImporti.getValue())).setLavorazione(l);
        		this.f2.getImporti().get(this.f2.getImporti().indexOf(this.FAboxImporti.getValue())).setImporto(importoLavNoIva);
        		this.f2.getImporti().get(this.f2.getImporti().indexOf(this.FAboxImporti.getValue())).setImportoIva(importoLav);
        		this.f2.getImporti().get(this.f2.getImporti().indexOf(this.FAboxImporti.getValue())).setNote(note);
        		this.f2.calcolaImportoTot();
        		if(this.FAboxFatture.getValue().getImporti().get(this.f2.getImporti().indexOf(this.FAboxImporti.getValue())).
        				equalsTotale
        					(this.f2.getImporti().get(this.f2.getImporti().indexOf(this.FAboxImporti.getValue())))) {
        			this.FAtxtArea.setText(this.f2.toStringConImporti());
        			this.FAtxtArea.appendText("\n\nNessuma modifica effettuata sull'importo selezionato");
        			return;
        		}
        	} else {
        		Importo i = new Importo (f2.getImporti().size()+1, f2, l, importoLavNoIva, importoLav, note);
        		f2.addImporto(i);
        	}
    		this.FAtxtArea.setText(this.f2.toStringConImporti());
    		this.FAtxtImportoFattura.setText(this.f2.getImportoTot().toString());
    		this.FAboxImporti.getItems().clear();
    		this.FAboxImporti.getItems().add(new Importo(null, null, null, null, null, null));
    		this.FAboxImporti.getItems().addAll(f2.getImporti());
        	if(f2.getImporti().size()>0) {
            	this.FAbtnConferma.setDisable(false);
        	}
    }

    @FXML
    void FAreset(ActionEvent event) {
    	this.FAresetDatiInput();
    	this.FAtxtImportoFattura.clear();
    	this.FAtxtNoteFattura.clear();
    	this.FAresetDatiImporto();
    	this.FAtxtArea.clear();

    }
    
    @FXML
    void FAresetFiltri(ActionEvent event) {
    	this.FAresetDatiInput();
    	this.stampaOutputRicerca(model.getFatture());
    }

    @FXML
    void FAricercaCant2(KeyEvent event) {
    	if(this.FAtxtRicercaCant2.getText().trim().length()>0) {
		    String ins = this.FAtxtRicercaCant2.getText().trim().toUpperCase();
		   	List<Cantiere> list = new ArrayList<>();
		   	for(Cantiere c : model.getCantieri())
		   		if(c!=null && c.toString().contains(ins))
		   			list.add(c);
	    	this.FAboxCant2.getItems().clear();
	    	this.FAboxCant2.getItems().add(null);
	    	this.FAboxCant2.getItems().addAll(list);
	    } else {
			this.FAboxCant2.getItems().clear();
			this.FAboxCant2.getItems().add(null);
			this.FAboxCant2.getItems().addAll(model.getCantieri());
	    }
    }
    
    @FXML
    void FAricercaLav2(KeyEvent event) {
    	if(this.FAtxtLavorazione.getText().trim().length()>0) {
		    String ins = this.FAtxtLavorazione.getText().trim().toUpperCase();
		   	List<String> list = new ArrayList<>();
		   	for(String s : model.getDescrizioniLavorazioni())
		   		if(s!=null && s.contains(ins))
		   			list.add(s);
	    	this.FAboxLavorazioni2.getItems().clear();
	    	this.FAboxLavorazioni2.getItems().add(null);
	    	this.FAboxLavorazioni2.getItems().addAll(list);
	    } else {
			this.FAboxLavorazioni2.getItems().clear();
			this.FAboxLavorazioni2.getItems().add(null);
			this.FAboxLavorazioni2.getItems().addAll(model.getDescrizioniLavorazioni());
	    }
    }

    @FXML
    void FAricercaVoce2(KeyEvent event) {
    	if(this.FAtxtRicercaVoce2.getText().trim().length()>0) {
		    String ins = this.FAtxtRicercaVoce2.getText().trim().toUpperCase();
		   	List<String> list = new ArrayList<>();
		   	for(String s : model.getVociCapitolato())
		   		if(s!=null && s.contains(ins))
		   			list.add(s);
	    	this.FAboxVoce2.getItems().clear();
	    	this.FAboxVoce2.getItems().add(null);
	    	this.FAboxVoce2.getItems().addAll(list);
	    } else {
			this.FAboxVoce2.getItems().clear();
			this.FAboxVoce2.getItems().add(null);
			this.FAboxVoce2.getItems().addAll(model.getVociCapitolato());
	    }
    }

    @FXML
    void FArimuovi(ActionEvent event) {	
    	if(this.FAtxtArea.getText().equals("Fatture non trovate") || (this.FAtxtArea.getText().equals("Errore inserimento date"))) {
    		this.stampaOutputRicerca(this.FAboxFatture.getItems());
    		return;
    	}
		List<Fattura> fatture = null;
		//Rimozione fornitore immesso da eliminare
		if(this.FAboxForn.getValue()!=null && this.forn.contains(this.FAboxForn.getValue())) {
			Integer index = this.forn.indexOf(this.FAboxForn.getValue());
			this.forn.remove(this.FAboxForn.getValue());
			Integer count = -1;
			for(Integer i : inputList.keySet()) {
				if(inputList.get(i).equals(InputType.Fornitore))
					count++;
				if(count==index) {
					inputList.remove(i);
					break;
				}
			}
			inputList = this.ricalcolaInputIndex(inputList);
			fatture = model.getFattureRichieste(this.forn, this.cant, this.lav, this.voci, 
					this.FAdataDa.getValue(), this.FAdataA.getValue(), inputList);
		}
		//Rimozione cantiere immesso da eliminare
		else if(this.FAboxCant.getValue()!=null) {
			Cantiere c = null;
			if(this.FAboxCant.getValue().getNumero()!=null)
				c = this.FAboxCant.getValue();
			if(this.cant.contains(c)) {
				Integer index = this.cant.indexOf(c);
				this.cant.remove(c);
				Integer count = -1;
				for(Integer i : inputList.keySet()) {
					if(inputList.get(i).equals(InputType.Cantiere))
						count++;
					if(count==index) {
						inputList.remove(i);
						break;
					}
				}
				inputList = this.ricalcolaInputIndex(inputList);
				fatture = model.getFattureRichieste(this.forn, this.cant, this.lav, this.voci, 
						this.FAdataDa.getValue(), this.FAdataA.getValue(), inputList);
			}
		} 
		//Rimozione lavorazione immessa da eliminare
		else if(this.FAboxLav.getValue()!=null) {
			String s = null;
			if(!this.FAboxLav.getValue().equals(nd))
				s = this.FAboxLav.getValue();
			if(lav.contains(s)) {
				Integer index = this.lav.indexOf(s);
				this.lav.remove(s);
				Integer count = -1;
				for(Integer i : inputList.keySet()) {
					if(inputList.get(i).equals(InputType.Lavorazione))
						count++;
					if(count==index) {
						inputList.remove(i);
						break;
					}
				}
				inputList = this.ricalcolaInputIndex(inputList);
				fatture = model.getFattureRichieste(this.forn, this.cant, this.lav, this.voci, 
						this.FAdataDa.getValue(), this.FAdataA.getValue(), inputList);
			}
		}
		//Rimozione voce immessa da eliminare
		else if(this.FAboxVoce.getValue()!=null) {
			String s = null;
			if(!this.FAboxVoce.getValue().equals(nd))
				s = this.FAboxVoce.getValue();
			if(voci.contains(s)) {
				Integer index = this.voci.indexOf(s);
				this.voci.remove(s);
				Integer count = -1;
				for(Integer i : inputList.keySet()) {
					if(inputList.get(i).equals(InputType.VoceCap))
						count++;
					if(count==index) {
						inputList.remove(i);
						break;
					}
				}
				inputList = this.ricalcolaInputIndex(inputList);
				fatture = model.getFattureRichieste(this.forn, this.cant, this.lav, this.voci, 
						this.FAdataDa.getValue(), this.FAdataA.getValue(), inputList);
			}
		}
		if(fatture!=null) {
			//Aggiornamento box input
			this.rimuoviInputExtraAggiornaBox(fatture);
			//Stampa
			this.stampaOutputRicerca(fatture);
		}
    }

	@FXML
    void FAsetImporto(ActionEvent event) {
		if(this.FAboxImporti.getValue()!=null && this.FAboxImporti.getValue().getNumero()!=null) {
			//attivazione input
			this.FAbtnCanImporto.setDisable(false);
			this.FAtxtRicercaCant2.setDisable(false);
			this.FAboxCant2.setDisable(false);
			this.FAboxCant2.setValue(this.FAboxImporti.getValue().getLavorazione().getCantiere());
			this.FAtxtLavorazione.setDisable(false);
			this.FAboxLavorazioni2.setDisable(false);
			this.FAboxLavorazioni2.setValue(this.FAboxImporti.getValue().getLavorazione().getDescrizione());
			this.FAtxtRicercaVoce2.setDisable(false);
			this.FAboxVoce2.setDisable(false);
			this.FAboxVoce2.setValue(this.FAboxImporti.getValue().getLavorazione().getVoceCapitolato());
			this.FAtxtImportNoIva.setDisable(false);
			this.FAtxtImportNoIva.setText(this.FAboxImporti.getValue().getImporto().toString());
			this.FAtxtImportoTot.setDisable(false);
			this.FAtxtImportoTot.setText(this.FAboxImporti.getValue().getImportoIva().toString());
			this.FAbtnInserisci2.setDisable(false);
			this.FAtxtNoteImporto.setDisable(false);
			this.FAtxtNoteImporto.setText(this.FAboxImporti.getValue().getNote());
    	} else if(this.FAboxImporti.getValue()!=null && this.FAboxImporti.getValue().getNumero()==null) {
    		this.FAbtnCanImporto.setDisable(false);
			this.FAtxtRicercaCant2.setDisable(false);
			this.FAboxCant2.setDisable(false);
			this.FAboxCant2.setValue(null);
			this.FAtxtLavorazione.setDisable(false);
			this.FAboxLavorazioni2.setDisable(false);
			this.FAboxLavorazioni2.setValue(null);
			this.FAtxtRicercaVoce2.setDisable(false);
			this.FAboxVoce2.setDisable(false);
			this.FAboxVoce2.setValue(null);
			this.FAtxtImportNoIva.setDisable(false);
			this.FAtxtImportNoIva.setText(null);
			this.FAtxtImportoTot.setText(null);
			this.FAtxtImportoTot.setDisable(false);
			this.FAbtnInserisci2.setDisable(false);
			this.FAtxtNoteImporto.setDisable(false);
			this.FAtxtNoteImporto.setText(null);
    	}
    }
   
    @FXML
    void FAsetFornitore(ActionEvent event) {
    	if(this.FAboxForn.getValue()!=null) {
    		this.FAtxtRicercaCant.setDisable(true);
    		this.FAtxtRicercaLav.setDisable(true);
    		this.FAtxtRicercaVoce.setDisable(true);
	    	this.FAboxCant.setDisable(true);
	    	this.FAboxLav.setDisable(true);	
	    	this.FAboxVoce.setDisable(true);
	    	this.FAdataDa.setDisable(true);
	    	this.FAdataA.setDisable(true);
    	} else {
    		this.FAtxtRicercaCant.setDisable(false);
    		this.FAtxtRicercaLav.setDisable(false);
    		this.FAtxtRicercaVoce.setDisable(false);
    		this.FAboxCant.setDisable(false);
	    	this.FAboxLav.setDisable(false);
	    	this.FAboxVoce.setDisable(false);
	    	this.FAdataDa.setDisable(false);
	    	this.FAdataA.setDisable(false);
    	}
    }

    @FXML
    void FAsetCantiere(ActionEvent event) {
    	if(this.FAboxCant.getValue()!=null) {
    		this.FAtxtRicercaForn.setDisable(true);
    		this.FAtxtRicercaLav.setDisable(true);
    		this.FAtxtRicercaVoce.setDisable(true);
	    	this.FAboxForn.setDisable(true);
	    	this.FAboxLav.setDisable(true);
	    	this.FAboxVoce.setDisable(true);
	    	this.FAdataDa.setDisable(true);
	    	this.FAdataA.setDisable(true);
    	} else {
    		this.FAtxtRicercaForn.setDisable(false);
    		this.FAtxtRicercaLav.setDisable(false);
    		this.FAtxtRicercaVoce.setDisable(false);
    		this.FAboxForn.setDisable(false);
	    	this.FAboxLav.setDisable(false);
	    	this.FAboxVoce.setDisable(false);
	    	this.FAdataDa.setDisable(false);
	    	this.FAdataA.setDisable(false);
    	}
    }

    @FXML
    void FAsetLavorazione(ActionEvent event) {
    	if(this.FAboxLav.getValue()!=null) {
    		this.FAtxtRicercaForn.setDisable(true);
    		this.FAtxtRicercaCant.setDisable(true);
    		this.FAtxtRicercaVoce.setDisable(true);
	    	this.FAboxForn.setDisable(true);
	    	this.FAboxCant.setDisable(true);
	    	this.FAboxVoce.setDisable(true);
	    	this.FAdataDa.setDisable(true);
	    	this.FAdataA.setDisable(true);
    	} else {
    		this.FAtxtRicercaForn.setDisable(false);
    		this.FAtxtRicercaCant.setDisable(false);
    		this.FAtxtRicercaVoce.setDisable(false);
    		this.FAboxForn.setDisable(false);
	    	this.FAboxCant.setDisable(false);
	    	this.FAboxVoce.setDisable(false);
	    	this.FAdataDa.setDisable(false);
	    	this.FAdataA.setDisable(false);
    	}
    }
    
    @FXML
    void FAsetVoce(ActionEvent event) {
    	if(this.FAboxVoce.getValue()!=null) {
    		this.FAtxtRicercaForn.setDisable(true);
    		this.FAtxtRicercaCant.setDisable(true);
    		this.FAtxtRicercaLav.setDisable(true);
	    	this.FAboxForn.setDisable(true);
	    	this.FAboxCant.setDisable(true);
	    	this.FAboxLav.setDisable(true);
	    	this.FAdataDa.setDisable(true);
	    	this.FAdataA.setDisable(true);
    	} else {
    		this.FAtxtRicercaForn.setDisable(false);
    		this.FAtxtRicercaCant.setDisable(false);
    		this.FAtxtRicercaLav.setDisable(false);
    		this.FAboxForn.setDisable(false);
	    	this.FAboxCant.setDisable(false);
	    	this.FAboxLav.setDisable(false);
	    	this.FAdataDa.setDisable(false);
	    	this.FAdataA.setDisable(false);
    	}
    }
    
    @FXML
    void FAsetDataA(ActionEvent event) {
    	if(this.FAdataA.getValue()!=null) {
    		this.FAtxtRicercaForn.setDisable(true);
    		this.FAtxtRicercaCant.setDisable(true);
    		this.FAtxtRicercaLav.setDisable(true);
    		this.FAtxtRicercaVoce.setDisable(true);
	    	this.FAboxForn.setDisable(true);
	    	this.FAboxCant.setDisable(true);
	    	this.FAboxLav.setDisable(true);
	    	this.FAboxVoce.setDisable(true);
    	} else if(this.FAdataDa.getValue()==null){
    		this.FAtxtRicercaForn.setDisable(false);
    		this.FAtxtRicercaCant.setDisable(false);
    		this.FAtxtRicercaLav.setDisable(false);
    		this.FAtxtRicercaVoce.setDisable(false);
    		this.FAboxForn.setDisable(false);
	    	this.FAboxCant.setDisable(false);
	    	this.FAboxLav.setDisable(false);
	    	this.FAboxVoce.setDisable(false);
    	}
    }

    @FXML
    void FAsetDataDa(ActionEvent event) {
    	if(this.FAdataDa.getValue()!=null) {
    		this.FAtxtRicercaForn.setDisable(true);
    		this.FAtxtRicercaCant.setDisable(true);
    		this.FAtxtRicercaLav.setDisable(true);
    		this.FAtxtRicercaVoce.setDisable(true);
	    	this.FAboxForn.setDisable(true);
	    	this.FAboxCant.setDisable(true);
	    	this.FAboxLav.setDisable(true);
	    	this.FAboxVoce.setDisable(true);
    	} else if(this.FAdataA.getValue()==null){
    		this.FAtxtRicercaForn.setDisable(false);
    		this.FAtxtRicercaCant.setDisable(false);
    		this.FAtxtRicercaLav.setDisable(false);
    		this.FAtxtRicercaVoce.setDisable(false);
    		this.FAboxForn.setDisable(false);
	    	this.FAboxCant.setDisable(false);
	    	this.FAboxLav.setDisable(false);
	    	this.FAboxVoce.setDisable(false);
    	}
    }
    
    @FXML
    void FAresetDate(ActionEvent event) {
    	this.FAresetDate();
    }
    
    @FXML
    void FAsetImportoTot(ActionEvent event) {
    	try {
	   		Double d1 = Double.parseDouble(this.FAtxtImportNoIva.getText());
	   		if(d1>=0 && f2!=null) {
	   			Double d2 = d1 * (1+this.f2.getIva().doubleValue()/100);
	   			BigDecimal x = new BigDecimal(d2).setScale(2, RoundingMode.HALF_EVEN);
	   			this.FAtxtImportoTot.setText(x.toString());
	   		} 
	   	} catch (NumberFormatException e) {
			return;
			//e.printStackTrace();
		}	
    }
    
    private void FAresetDate() {
    	this.min = null;
    	this.max = null;
    	this.FAdataDa.setDisable(false);
    	this.FAdataA.setDisable(false);
    	this.FAdataDa.setValue(null);
    	this.FAdataA.setValue(null);
    	this.FAdataDa.setPromptText("Da");
    	this.FAdataA.setPromptText("A");
    }
    
    private void FAresetDatiInput() {
    	this.FAtxtRicercaForn.clear();
    	this.FAboxForn.getItems().clear();
    	this.FAboxForn.getItems().add(null);
    	this.FAboxForn.getItems().addAll(model.getFornitori());
    	this.lastFornList = new ArrayList<>(this.FAboxForn.getItems());
    	this.FAtxtRicercaCant.clear();
    	this.FAboxCant.getItems().clear();
    	this.FAboxCant.getItems().add(null);
    	List<Cantiere> boxCant = model.getCantieriAttivi();
    	if(boxCant.contains(null)) {
    		this.FAboxCant.getItems().add(new Cantiere(null, null, null, null, null, null));
    		boxCant.remove(null);
    	}	
    	this.FAboxCant.getItems().addAll(boxCant);
    	this.lastCantList = new ArrayList<>(this.FAboxCant.getItems());
    	this.FAtxtRicercaLav.clear();
    	this.FAboxLav.getItems().clear();
    	this.FAboxLav.getItems().add(null);
    	List<String> boxLav = model.getDescrizioniLavorazioni();
    	if(boxLav.contains(null)) {
    		this.FAboxLav.getItems().add(nd);
    		boxLav.remove(null);
    	}	
    	this.FAboxLav.getItems().addAll(boxLav);
    	this.lastLavList = new ArrayList<>(this.FAboxLav.getItems());
    	this.FAtxtRicercaVoce.clear();
    	this.FAboxVoce.getItems().clear();
    	this.FAboxVoce.getItems().add(null);
    	List<String> boxVoci = model.getVociCapitolatoAttive();
    	if(boxVoci.contains(null)) {
    		this.FAboxVoce.getItems().add(nd);
    		boxVoci.remove(null);
    	}	
    	this.FAboxVoce.getItems().addAll(boxVoci);
    	this.lastVociList = new ArrayList<>(this.FAboxVoce.getItems());
    	this.FAdataDa.setDisable(false);
    	this.FAdataA.setDisable(false);
    	this.forn = new ArrayList<>();
    	this.cant = new ArrayList<>();
    	this.lav = new ArrayList<>();
    	this.voci = new ArrayList<>();
    	this.inputList = new TreeMap<>();
    	this.FAresetDate();
    	this.FAboxFatture.getItems().clear();
    	this.FAboxFatture.getItems().addAll(model.getFatture());
    	if(this.f2!=null)
    		this.FAboxFatture.setValue(f2);
    }
    
    private void FAresetDatiImporto() {
    	this.FAbtnCanImporto.setDisable(true);
    	this.FAtxtRicercaCant2.setDisable(true);
    	this.FAtxtRicercaCant2.clear();
    	this.FAboxCant2.setDisable(true);
    	this.FAboxCant2.getItems().clear();
    	this.FAboxCant2.getItems().add(null);
    	this.FAboxCant2.getItems().addAll(model.getCantieri());
    	this.FAtxtLavorazione.setDisable(true);
    	this.FAtxtLavorazione.clear();
    	this.FAboxLavorazioni2.setDisable(true);
    	this.FAboxLavorazioni2.getItems().clear();
    	this.FAboxLavorazioni2.getItems().add(null);
    	this.FAboxLavorazioni2.getItems().addAll(model.getDescrizioniLavorazioni());
    	this.FAtxtRicercaVoce2.setDisable(true);
    	this.FAtxtRicercaVoce2.clear();
    	this.FAboxVoce2.setDisable(true);
    	this.FAboxVoce2.getItems().clear();
    	this.FAboxVoce2.getItems().add(null);
    	this.FAboxVoce2.getItems().addAll(model.getVociCapitolato());
    	this.FAtxtImportNoIva.setDisable(true);
    	this.FAtxtImportoTot.setDisable(true);
    	this.FAtxtImportNoIva.clear();
    	this.FAtxtImportoTot.clear();
    	this.FAbtnInserisci2.setDisable(true);
    	this.FAtxtNoteImporto.clear();
    	this.FAtxtNoteImporto.setDisable(true);
    	this.FAbtnConferma.setDisable(true);
    	this.f2 = null;
    	
    }
    
    private void rimuoviInputExtraAggiornaBox (List<Fattura> list) {
    	//Rimuovo da liste parametri input i parametri non influenti
    	//	//rimuovo fornitori di troppo
    	List<String> tempF = new ArrayList<>(forn);
    	Collections.sort(tempF);
    	if(!tempF.equals(model.getFornitoriFatture(list))) {
	    	List<String> newForn = new ArrayList<>();
	    	Map<Integer, String> m = new TreeMap<>();
	    	for(String s : forn)
	    		if(model.getFornitoriFatture(list).contains(s))
	    			newForn.add(s);
	    		else
	    			m.put(forn.indexOf(s), s);
	    	Integer countF = -1;
	    	List<Integer> inputR = new ArrayList<>();
	    	for(Integer i : inputList.keySet()) 
	    		if(inputList.get(i).equals(InputType.Fornitore)) {
	    			countF++;
	    				if(m.keySet().contains(countF))
	    					inputR.add(i);
	    		}
	    	forn = newForn;
	    	for(Integer i : inputR)
	    		inputList.remove(i);
	    	inputList = this.ricalcolaInputIndex(inputList);
    	}
    	//	//rimuovo cantieri di troppo
    	List<Cantiere> tempC = new ArrayList<>(cant);
    	if(cant.contains(null))
    		tempC.remove(null);
    	Collections.sort(tempC);
    	if(cant.contains(null))
    		tempC.add(0, null);
    	if(!tempC.equals(model.getCantieriFatture(list, lav, voci))) {
	    	List<Cantiere> newCant = new ArrayList<>();
	    	Map<Integer, Cantiere> m = new TreeMap<>();
	    	for(Cantiere c : cant)
	    		if(model.getCantieriFatture(list, lav, voci).contains(c))
	    			newCant.add(c);
	    		else
	    			m.put(cant.indexOf(c), c);
	    	Integer countF = -1;
	    	List<Integer> inputR = new ArrayList<>();
	    	for(Integer i : inputList.keySet()) 
	    		if(inputList.get(i).equals(InputType.Cantiere)) {
	    			countF++;
	    				if(m.keySet().contains(countF))
	    					inputR.add(i);
	    		}
	    	cant = newCant;
	    	for(Integer i : inputR)
	    		inputList.remove(i);
	    	inputList = this.ricalcolaInputIndex(inputList);
    	}
    	//	//rimuovo lav di troppo
    	List<String> tempL = new ArrayList<>(lav);
    	if(lav.contains(null))
    		tempL.remove(null);
    	Collections.sort(tempL);
    	if(lav.contains(null))
    		tempL.add(0, null);
    	if(!tempL.equals(model.getDescrLavorazioniFatture(list, cant, voci))) {
    		List<String> newLav = new ArrayList<>();
	    	Map<Integer, String> m = new TreeMap<>();
	    	for(String s : lav)
	    		if(model.getDescrLavorazioniFatture(list, cant, voci).contains(s))
	    			newLav.add(s);
	    		else
	    			m.put(lav.indexOf(s), s);
	    	Integer countF = -1;
	    	List<Integer> inputR = new ArrayList<>();
	    	for(Integer i : inputList.keySet()) 
	    		if(inputList.get(i).equals(InputType.Lavorazione)) {
	    			countF++;
	    				if(m.keySet().contains(countF))
	    					inputR.add(i);
	    		}
	    	lav = newLav;
	    	for(Integer i : inputR)
	    		inputList.remove(i);
	    	inputList = this.ricalcolaInputIndex(inputList);
    	}
    	//	//rimuovo voci di troppo
    	List<String> tempV = new ArrayList<>(voci);
    	if(voci.contains(null))
    		tempV.remove(null);
    	Collections.sort(tempV);
    	if(voci.contains(null))
    		tempV.add(0, null);
    	if(!tempV.equals(model.getVociFatture(list, cant, lav))) {
    		List<String> newVoci = new ArrayList<>();
	    	Map<Integer, String> m = new TreeMap<>();
	    	for(String s : voci)
	    		if(model.getVociFatture(list, cant, lav).contains(s))
	    			newVoci.add(s);
	    		else
	    			m.put(voci.indexOf(s), s);
	    	Integer countF = -1;
	    	List<Integer> inputR = new ArrayList<>();
	    	for(Integer i : inputList.keySet()) 
	    		if(inputList.get(i).equals(InputType.VoceCap)) {
	    			countF++;
	    				if(m.keySet().contains(countF))
	    					inputR.add(i);
	    		}
	    	voci = newVoci;
	    	for(Integer i : inputR)
	    		inputList.remove(i);
	    	inputList = this.ricalcolaInputIndex(inputList);
    	}
    	//Aggiorno le varie box con le possibili scelte
    	//	//Box fatture
    	this.FAboxFatture.getItems().clear();
    	this.FAboxFatture.getItems().addAll(list);
    	//	//Box fornitori
    	this.FAboxForn.getItems().clear();
    	this.FAboxForn.getItems().add(null);
    	if(this.inputList.size()==0 || !this.inputList.get(this.inputList.size()-1).equals(InputType.Fornitore)) 
    		this.FAboxForn.getItems().addAll(model.getFornitoriFatture(list));
    	else if(this.inputList.get(this.inputList.size()-1).equals(InputType.Fornitore)) {
			List<String> tempForn = new ArrayList<>(forn);
			Map<Integer, InputType> tempInput = new TreeMap<>(inputList);
    		Integer remove = 0;
    		for(int i=this.inputList.size()-1; i>=0 && this.inputList.get(i).equals(InputType.Fornitore); i--) {
    				remove++;
    				tempInput.remove(i);
    		}
    		for(int i=tempForn.size()-1; i>=0 && remove>0; i--) {
    			tempForn.remove(i);
    			remove--;
    		}
    		tempInput = this.ricalcolaInputIndex(tempInput);
    		List<Fattura> tempFatt = model.getFattureRichieste(tempForn, this.cant, this.lav, this.voci, 
    				this.FAdataDa.getValue(), this.FAdataA.getValue(), tempInput);
    		this.FAboxForn.getItems().addAll(model.getFornitoriFatture(tempFatt));
    	}
    	if(!this.lastFornList.equals(this.FAboxForn.getItems()))
    		this.lastFornList = new ArrayList<>(this.FAboxForn.getItems());
    	this.FAtxtRicercaForn.clear();
    	this.FAtxtRicercaForn.setDisable(false);
    	this.FAboxForn.setDisable(false);
    	//	//Box cantieri
    	this.FAboxCant.getItems().clear();
    	this.FAboxCant.getItems().add(null);
    	if(this.inputList.size()==0 || !this.inputList.get(this.inputList.size()-1).equals(InputType.Cantiere)) {
        	List<Cantiere> boxCant = model.getCantieriFatture(list, lav, voci);
        	if(boxCant.contains(null)) {
        		this.FAboxCant.getItems().add(new Cantiere(null, null, null, null, null, null));
        		boxCant.remove(null);
        	}	
        	this.FAboxCant.getItems().addAll(boxCant);
    	} else if(this.inputList.get(this.inputList.size()-1).equals(InputType.Cantiere)) {
    		List<Cantiere> tempCant = new ArrayList<>(cant);
    		Map<Integer, InputType> tempInput = new TreeMap<>(inputList);
    		Integer remove = 0;
    		for(int i=this.inputList.size()-1; i>=0 && this.inputList.get(i).equals(InputType.Cantiere); i--) {
    				remove++;
    				tempInput.remove(i);
    		}
    		for(int i=tempCant.size()-1; i>=0 && remove>0; i--) {
    			tempCant.remove(i);
    			remove--;
    		}
    		tempInput = this.ricalcolaInputIndex(tempInput);
    		List<Fattura> tempFatt = model.getFattureRichieste(this.forn, tempCant, this.lav, this.voci, 
    				this.FAdataDa.getValue(), this.FAdataA.getValue(), tempInput);
    		List<Cantiere> boxCant = model.getCantieriFatture(tempFatt, lav, voci);
        	if(boxCant.contains(null)) {
        		this.FAboxCant.getItems().add(new Cantiere(null, null, null, null, null, null));
        		boxCant.remove(null);
        	}	
        	this.FAboxCant.getItems().addAll(boxCant);
    	}
    	if(!this.lastCantList.equals(this.FAboxCant.getItems()))
    		this.lastCantList = new ArrayList<>(this.FAboxCant.getItems());
    	this.FAtxtRicercaCant.clear();
    	this.FAtxtRicercaCant.setDisable(false);
    	this.FAboxCant.setDisable(false);
    	//	//Box lavorazioni
    	this.FAboxLav.getItems().clear();
    	this.FAboxLav.getItems().add(null);
    	if(this.inputList.size()==0 || !this.inputList.get(this.inputList.size()-1).equals(InputType.Lavorazione)) {
    		List<String> boxLav = model.getDescrLavorazioniFatture(list, cant, voci);
        	if(boxLav.contains(null)) {
        		this.FAboxLav.getItems().add(nd);
        		boxLav.remove(null);
        	}	
			this.FAboxLav.getItems().addAll(boxLav);
    	}
    	else if(this.inputList.get(this.inputList.size()-1).equals(InputType.Lavorazione)) {
			List<String> tempLav = new ArrayList<>(lav);
    		Map<Integer, InputType> tempInput = new TreeMap<>(inputList);
    		Integer remove = 0;
    		for(int i=this.inputList.size()-1; i>=0 && this.inputList.get(i).equals(InputType.Lavorazione); i--) {
				remove++;
				tempInput.remove(i);
    		}
    		for(int i=tempLav.size()-1; i>=0 && remove>0; i--) {
    			tempLav.remove(i);
    			remove--;
    		}
    		tempInput = this.ricalcolaInputIndex(tempInput);
    		List<Fattura> tempFatt = model.getFattureRichieste(this.forn, this.cant, tempLav, this.voci, 
    				this.FAdataDa.getValue(), this.FAdataA.getValue(), tempInput);
    		List<String> boxLav = model.getDescrLavorazioniFatture(tempFatt, cant, voci);
        	if(boxLav.contains(null)) {
        		this.FAboxLav.getItems().add(nd);
        		boxLav.remove(null);
        	}	
			this.FAboxLav.getItems().addAll(boxLav);
    	}
    	if(!this.lastLavList.equals(this.FAboxLav.getItems()))
    		this.lastLavList = new ArrayList<>(this.FAboxLav.getItems());
    	this.FAtxtRicercaLav.clear();
    	this.FAtxtRicercaLav.setDisable(false);
    	this.FAboxLav.setDisable(false);
    	//	//Box voce
    	this.FAboxVoce.getItems().clear();
    	this.FAboxVoce.getItems().add(null);
    	if(this.inputList.size()==0 || !this.inputList.get(this.inputList.size()-1).equals(InputType.VoceCap)) {
    		List<String> boxVoci = model.getVociFatture(list, cant, lav);
	    	if(boxVoci.contains(null)) {
	    		this.FAboxVoce.getItems().add(nd);
	    		boxVoci.remove(null);
	    	}	
			this.FAboxVoce.getItems().addAll(boxVoci);
    	} else if(this.inputList.get(this.inputList.size()-1).equals(InputType.VoceCap)) {
    		List<String> tempVoci = new ArrayList<>(voci);
    		Map<Integer, InputType> tempInput = new TreeMap<>(inputList);
    		Integer remove = 0;
    		for(int i=this.inputList.size()-1; i>=0 && this.inputList.get(i).equals(InputType.VoceCap); i--) {
				remove++;
				tempInput.remove(i);
    		}
    		for(int i=tempVoci.size()-1; i>=0 && remove>0; i--) {
    			tempVoci.remove(i);
    			remove--;
    		}
    		tempInput = this.ricalcolaInputIndex(tempInput);
    		List<Fattura> tempFatt = model.getFattureRichieste(this.forn, this.cant, this.lav, tempVoci, 
    				this.FAdataDa.getValue(), this.FAdataA.getValue(), tempInput);
    		List<String> boxVoci = model.getVociFatture(tempFatt, cant, lav);
	    	if(boxVoci.contains(null)) {
	    		this.FAboxVoce.getItems().add(nd);
	    		boxVoci.remove(null);
	    	}	
			this.FAboxVoce.getItems().addAll(boxVoci);
    	}
    	if(!this.lastVociList.equals(this.FAboxLav.getItems()))
    		this.lastVociList = new ArrayList<>(this.FAboxVoce.getItems());
    	this.FAtxtRicercaVoce.clear();
    	this.FAtxtRicercaVoce.setDisable(false);
    	this.FAboxVoce.setDisable(false);
    	//	//Date
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    	if(min!=null)
    		this.FAdataDa.setPromptText(min.format(formatter));
    	if(max!=null)
    		this.FAdataA.setPromptText(max.format(formatter));
    	this.FAdataDa.setDisable(false);
    	this.FAdataA.setDisable(false);
    	this.FAdataA.setValue(null);
    	this.FAdataDa.setValue(null);
    }
    
    private void stampaOutputRicerca(List<Fattura> fatture) {
		this.FAtxtArea.setText("Elenco fatture richieste");
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		if(min!=null)
			this.FAtxtArea.appendText(" dal " + model.getDataMinFatture(fatture).format(formatter));
		if(max!=null)
			this.FAtxtArea.appendText(" fino al " + model.getDataMaxFatture(fatture).format(formatter));
		this.FAtxtArea.appendText("		" + fatture.size() + " fatture");
		List<Cantiere> cStamp = new ArrayList<>(cant);
		if(cant.contains(null)) {
			int i = cant.indexOf(null);
			cStamp.remove(null);
			cStamp.add(i, new Cantiere(null, null, null, null, null, null));
		}
		List<String> lStamp = new ArrayList<>(lav);
		if(lav.contains(null)) {
			int i = lav.indexOf(null);
			lStamp.remove(null);
			lStamp.add(i, nd);
		}
		List<String> vStamp = new ArrayList<>(voci);
		if(voci.contains(null)) {
			int i = voci.indexOf(null);
			vStamp.remove(null);
			vStamp.add(i, nd);
		}
		this.FAtxtArea.appendText("\nFornitori: ");
		if(forn.size()==0)
			this.FAtxtArea.appendText("/");
		else 
			for(String s : forn) {
				if(forn.indexOf(s)!=0)
					this.FAtxtArea.appendText(", ");
				this.FAtxtArea.appendText(forn.indexOf(s)+1 + ". " + s);
			}
		this.FAtxtArea.appendText("\nCantieri: ");
		if(cant.size()==0)
			this.FAtxtArea.appendText("/");
		else
			for(Cantiere c : cStamp) {
				if(cStamp.indexOf(c)!=0)
					this.FAtxtArea.appendText(", ");
				this.FAtxtArea.appendText(cStamp.indexOf(c)+1 + ". " + c.toString());
			} 
		this.FAtxtArea.appendText("\nLavorazioni: ");
		if(lav.size()==0)
			this.FAtxtArea.appendText("/");
		else 
			for(String s : lStamp) {
				if(lStamp.indexOf(s)!=0)
					this.FAtxtArea.appendText(", ");
				this.FAtxtArea.appendText(lStamp.indexOf(s)+1 + ". " + s);
			}
		this.FAtxtArea.appendText("\nVoci capitolato: ");
		if(voci.size()==0)
			this.FAtxtArea.appendText("/");
		else 
			for(String s : vStamp) {
				if(vStamp.indexOf(s)!=0)
					this.FAtxtArea.appendText(", ");
				this.FAtxtArea.appendText(vStamp.indexOf(s)+1 + ". " + s);
			}
		this.FAtxtArea.appendText("\n");
		for(Fattura f : fatture)
			if(f!=null)
				this.FAtxtArea.appendText("\n " + f.toString());
    }
    
    private Map<Integer,InputType> ricalcolaInputIndex(Map<Integer, InputType> map) {
    	int j = 0;
    	Map<Integer,InputType> newInput = new TreeMap<>();
		for(Integer i : map.keySet()) {
			if(i.equals(j))
				newInput.put(i, map.get(i));
			else {
				newInput.put(j, map.get(i));
			}
			j++;
		}
		return newInput;
	}
    
    //INIZIALIZZAZIONE
    //
    //
    //
    
    private Model model;
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
    	//TAB INSERISCI FATTURA
    	 assert IFtxtFornitore != null : "fx:id=\"IFtxtRicercaFornitore\" was not injected: check your FXML file 'Scene.fxml'.";
         assert IFboxFornitori != null : "fx:id=\"IFboxFornitori\" was not injected: check your FXML file 'Scene.fxml'.";
         assert IFtxtNum != null : "fx:id=\"IFtxtNum\" was not injected: check your FXML file 'Scene.fxml'.";
         assert IFdata != null : "fx:id=\"IFdata\" was not injected: check your FXML file 'Scene.fxml'.";
         assert IFboxIVA != null : "fx:id=\"IFboxIVA\" was not injected: check your FXML file 'Scene.fxml'.";
         assert IFtxtRicercaCantiere != null : "fx:id=\"IFtxtRicercaCantiere\" was not injected: check your FXML file 'Scene.fxml'.";
         assert IFboxCantieri != null : "fx:id=\"IFboxCantieri\" was not injected: check your FXML file 'Scene.fxml'.";
         assert IFtxtLavorazione != null : "fx:id=\"IFtxtRicercaLavorazione\" was not injected: check your FXML file 'Scene.fxml'.";
         assert IFboxLavorazioni != null : "fx:id=\"IFboxLavorazioni\" was not injected: check your FXML file 'Scene.fxml'.";
         assert IFtxtRicercaVoce != null : "fx:id=\"IFtxtRicercaVoce\" was not injected: check your FXML file 'Scene.fxml'.";
         assert IFboxVoci != null : "fx:id=\"IFboxVoci\" was not injected: check your FXML file 'Scene.fxml'.";
         assert IFtxtImportoNoIva != null : "fx:id=\"IFtxtImportoNoIva\" was not injected: check your FXML file 'Scene.fxml'.";
         assert IFtxtImportoTot != null : "fx:id=\"IFtxtImportoTot\" was not injected: check your FXML file 'Scene.fxml'.";
         assert IFbtnIns != null : "fx:id=\"IFbtnIns\" was not injected: check your FXML file 'Scene.fxml'.";
         assert IFtxtNoteImporto != null : "fx:id=\"IFtxtNoteImporto\" was not injected: check your FXML file 'Scene.fxml'.";
         assert IFtxtArea != null : "fx:id=\"IFtxtArea\" was not injected: check your FXML file 'Scene.fxml'.";
         assert IFtxtNoteFattura != null : "fx:id=\"IFtxtNoteFattura\" was not injected: check your FXML file 'Scene.fxml'.";
         assert IFtxtImportoTotFattura != null : "fx:id=\"IFtxtImportoTotFattura\" was not injected: check your FXML file 'Scene.fxml'.";
         assert IFbtnCanc != null : "fx:id=\"IFbtnCanc\" was not injected: check your FXML file 'Scene.fxml'.";
         assert IFbtnReset != null : "fx:id=\"IFbtnReset\" was not injected: check your FXML file 'Scene.fxml'.";
         assert IFbtnConfema != null : "fx:id=\"IFbtnConfema\" was not injected: check your FXML file 'Scene.fxml'.";
         assert IFbtnCancArea != null : "fx:id=\"IFbtnCancArea\" was not injected: check your FXML file 'Scene.fxml'.";
        //TAB INSERISCI PAGAMENTO
        assert IPtxtFornitore != null : "fx:id=\"IPtxtFornitore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IPdata != null : "fx:id=\"IPdata\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IPtxtImporto != null : "fx:id=\"IPtxtImporto\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IPboxFatture != null : "fx:id=\"IPboxFatture\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IPtxtImportoRel != null : "fx:id=\"IPtxtImportoRel\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IPtxtNumFattura != null : "fx:id=\"IPtxtNumFattura\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IPboxIntero != null : "fx:id=\"IPboxIntero\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IPbtnInserisci != null : "fx:id=\"IPbtnInserisci\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IPtxtNotePagFatt != null : "fx:id=\"IPtxtNotePagFatt\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IPtxtArea != null : "fx:id=\"IPtxtArea\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IPbtnCanc != null : "fx:id=\"IPbtnCanc\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IPbtnReset != null : "fx:id=\"IPbtnReset\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IPbtnConferma != null : "fx:id=\"IPbtnConferma\" was not injected: check your FXML file 'Scene.fxml'.";
        //TAB FATTURE
        assert FAboxForn != null : "fx:id=\"FAboxForn\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAdataDa != null : "fx:id=\"FAdataDa\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAdataA != null : "fx:id=\"FAdataA\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAbtnInserisci != null : "fx:id=\"FAbtnInserisci\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAboxCant != null : "fx:id=\"FAboxCant\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAboxLav != null : "fx:id=\"FAboxLav\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAboxVoce != null : "fx:id=\"FAboxVoce\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAbtnRimuovi != null : "fx:id=\"FAbtnRimuovi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAtxtRicercaLav != null : "fx:id=\"FAtxtRicercaLav\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAtxtRicercaVoce != null : "fx:id=\"FAtxtRicercaVoce\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAtxtRicercaCant != null : "fx:id=\"FAtxtRicercaCant\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAtxtRicercaForn != null : "fx:id=\"FAtxtRicercaForn\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAbtnResetFiltri != null : "fx:id=\"FAbtnResetFiltri\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAbtnResetDate != null : "fx:id=\"FAbtnResetDate\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAtxtArea != null : "fx:id=\"FAtxtArea\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAtxtImportoFattura != null : "fx:id=\"FAtxtImportoFattura\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAboxFatture != null : "fx:id=\"FAboxFatture\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAbtnAnnulla != null : "fx:id=\"FAbtnAnnulla\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAtxtNoteFattura != null : "fx:id=\"FAtxtNoteFattura\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAboxImporti != null : "fx:id=\"FAboxImporti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAboxCant2 != null : "fx:id=\"FAboxCant2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAtxtLavorazione != null : "fx:id=\"FAtxtLavorazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAboxLavorazioni2 != null : "fx:id=\"FAboxLavorazioni2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAtxtRicercaVoce2 != null : "fx:id=\"FAtxtRicercaVoce2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAboxVoce2 != null : "fx:id=\"FAboxVoce2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAbtnCanImporto != null : "fx:id=\"FAbtnCanImporto\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAtxtImportNoIva != null : "fx:id=\"FAtxtImportNoIva\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAtxtImportoTot != null : "fx:id=\"FAtxtImportoTot\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAbtnInserisci2 != null : "fx:id=\"FAbtnInserisci2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAtxtRicercaCant2 != null : "fx:id=\"FAtxtRicercaCant2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAtxtNoteImporto != null : "fx:id=\"FAtxtNoteImporto\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAbtnConferma != null : "fx:id=\"FAbtnConferma\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAbtnReset != null : "fx:id=\"FAbtnReset\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel (Model model) {
    	this.model = model;
    	//TAB INSERISCI FATTURA
    	this.IFboxFornitori.getItems().clear();
    	this.IFboxFornitori.getItems().addAll(model.getFornitori());
    	this.IFboxIVA.getItems().clear();
    	this.IFboxIVA.getItems().addAll(0, 4, 5, 10, 22);
    	this.IFboxCantieri.getItems().clear();
    	this.IFboxCantieri.getItems().add(null);
    	this.IFboxCantieri.getItems().addAll(model.getCantieri());
    	this.IFboxLavorazioni.getItems().clear();
    	this.IFboxLavorazioni.getItems().addAll(model.getDescrizioniLavorazioni());
    	this.IFboxVoci.getItems().clear();
    	this.IFboxVoci.getItems().add(null);
    	this.IFboxVoci.getItems().addAll(model.getVociCapitolato());
    	this.f = null;
    	this.ll = new ArrayList<>();
    	//TAB INSERISCI PAGAMENTO
    	this.IPboxIntero.getItems().clear();
    	this.IPboxIntero.getItems().add(null);
    	this.IPboxIntero.getItems().addAll("Interamente pagata", "Acconto", "Saldo");
    	this.IPboxIntero.setValue("Interamente pagata");
    	this.p = null;
    	//TAB FATTURE
    	this.nd = "-- NON DETERMINABILE --";
    	this.FAboxForn.getItems().clear();
    	this.FAboxForn.getItems().add(null);
    	this.FAboxForn.getItems().addAll(model.getFornitori());
    	this.lastFornList = new ArrayList<>(this.FAboxForn.getItems());
    	this.FAboxCant.getItems().clear();
    	this.FAboxCant.getItems().add(null);
    	List<Cantiere> boxCant = model.getCantieriAttivi();
    	if(boxCant.contains(null)) {
    		this.FAboxCant.getItems().add(new Cantiere(null, null, null, null, null, null));
    		boxCant.remove(null);
    	}	
    	this.FAboxCant.getItems().addAll(boxCant);
    	this.lastCantList = new ArrayList<>(this.FAboxCant.getItems());
    	this.FAboxLav.getItems().clear();
    	this.FAboxLav.getItems().add(null);
    	List<String> boxLav = model.getDescrizioniLavorazioni();
    	if(boxLav.contains(null)) {
    		this.FAboxLav.getItems().add(nd);
    		boxLav.remove(null);
    	}	
    	this.FAboxLav.getItems().addAll(boxLav);
    	this.lastLavList = new ArrayList<>(this.FAboxLav.getItems());
    	this.FAboxVoce.getItems().clear();
    	this.FAboxVoce.getItems().add(null);
    	List<String> boxVoci = model.getVociCapitolatoAttive();
    	if(boxVoci.contains(null)) {
    		this.FAboxVoce.getItems().add(nd);
    		boxVoci.remove(null);
    	}	
    	this.FAboxVoce.getItems().addAll(boxVoci);
    	this.lastVociList = this.FAboxVoce.getItems();
    	this.FAboxFatture.getItems().clear();
    	this.FAboxFatture.getItems().addAll(model.getFatture());
    	this.FAtxtNoteFattura.setDisable(true);
    	this.FAboxImporti.setDisable(true);
    	this.FAbtnCanImporto.setDisable(true);
    	this.FAtxtRicercaCant2.setDisable(true);
    	this.FAboxCant2.setDisable(true);
    	this.FAboxCant2.getItems().clear();
    	this.FAboxCant2.getItems().add(null);	
    	this.FAboxCant2.getItems().addAll(model.getCantieri());
    	this.FAtxtLavorazione.setDisable(true);
    	this.FAboxLavorazioni2.setDisable(true);
    	this.FAboxLavorazioni2.getItems().clear();
    	this.FAboxLavorazioni2.getItems().add(null);
    	this.FAboxLavorazioni2.getItems().addAll(model.getDescrizioniLavorazioni());
    	this.FAtxtRicercaVoce2.setDisable(true);
    	this.FAboxVoce2.setDisable(true);
    	this.FAboxVoce2.getItems().clear();
    	this.FAboxVoce2.getItems().add(null);	
    	this.FAboxVoce2.getItems().addAll(model.getVociCapitolato());
    	this.FAtxtImportNoIva.setDisable(true);
    	this.FAtxtImportoTot.setDisable(true);
    	this.FAbtnInserisci2.setDisable(true);
    	this.FAtxtNoteImporto.setDisable(true);
    	this.FAbtnConferma.setDisable(true);
    	this.forn = new ArrayList<>();
    	this.cant = new ArrayList<>();
    	this.lav = new ArrayList<>();
    	this.voci = new ArrayList<>();
    	this.min = null;
    	this.max = null;
    	this.inputList = new TreeMap<>();
    	this.f2 = null;
    }
    
}
