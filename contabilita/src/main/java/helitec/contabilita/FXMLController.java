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
    
    @FXML // fx:id="IFtxtRicercaCantiere"
    private TextField IFtxtRicercaCantiere; // Value injected by FXMLLoader

    @FXML // fx:id="IFboxCantieri"
    private ComboBox<Cantiere> IFboxCantieri; // Value injected by FXMLLoader

    @FXML // fx:id="IFtxtLavorazione"
    private TextField IFtxtLavorazione; // Value injected by FXMLLoader
    
    @FXML // fx:id="IFtxtRicercaVoce"
    private TextField IFtxtRicercaVoce; // Value injected by FXMLLoader

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
    		/*else if(i==-1)
    			this.IFtxtArea.appendText("\nErrore: incongruenza date fattura-pagamento");*/ //NECESSARIO?
    		this.f = null;
    		this.ll = new ArrayList<>();
    		this.IFtxtFornitore.setEditable(true);
    		this.IFtxtNum.setEditable(true);
    		this.IFdata.setDisable(false);
    		this.IFboxIVA.setDisable(false);
    		this.IFbtnCancArea.setDisable(false);
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
    	this.IFtxtFornitore.setEditable(true);
    	this.IFtxtNum.clear();
    	this.IFtxtNum.setEditable(true);
    	this.IFdata.setValue(null);
    	this.IFdata.setEditable(true);
    	this.IFboxIVA.setValue(null);
    	this.IFboxIVA.setDisable(false);
    	this.IFtxtRicercaCantiere.clear();
    	this.IFboxCantieri.setValue(null);
    	this.IFtxtLavorazione.clear();
    	this.IFtxtRicercaVoce.clear();
    	this.IFboxVoci.setValue(null);
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
    void IFricercaForn(KeyEvent event) {
    	if(this.IFtxtFornitore.getText().trim().length()>0 && !model.getFornitori().contains(this.IFtxtFornitore.getText().trim().toUpperCase()))
			this.IFtxtArea.setText("Fornitore inserito non esistente");
    	else 
    		this.IFtxtArea.clear();
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
    private List<Cantiere> cant;
    private List<String> lav;
    private List<String> voci;
    
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

    @FXML // fx:id="FAtxtArea"
    private TextArea FAtxtArea; // Value injected by FXMLLoader

    @FXML // fx:id="FAtxtImportoFattura"
    private TextField FAtxtImportoFattura; // Value injected by FXMLLoader

    @FXML // fx:id="FAboxFatture"
    private ComboBox<Fattura> FAboxFatture; // Value injected by FXMLLoader

    @FXML // fx:id="FAtxtNoteFattura"
    private TextField FAtxtNoteFattura; // Value injected by FXMLLoader

    @FXML // fx:id="FAbtnCancNoteFattura"
    private Button FAbtnCancNoteFattura; // Value injected by FXMLLoader

    @FXML // fx:id="FAbtnModNoteFattura"
    private Button FAbtnModNoteFattura; // Value injected by FXMLLoader

    @FXML // fx:id="FAboxImporti"
    private ComboBox<Importo> FAboxImporti; // Value injected by FXMLLoader

    @FXML // fx:id="FAboxCant2"
    private ComboBox<Cantiere> FAboxCant2; // Value injected by FXMLLoader

    @FXML // fx:id="FAtxtLavorazione"
    private TextField FAtxtLavorazione; // Value injected by FXMLLoader

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

    @FXML // fx:id="FAbtnCancInserimento"
    private Button FAbtnCancInserimento; // Value injected by FXMLLoader

    @FXML // fx:id="FAbtnConferma"
    private Button FAbtnConferma; // Value injected by FXMLLoader

    @FXML // fx:id="FAbtnReset"
    private Button FAbtnReset; // Value injected by FXMLLoader

    @FXML
    void FAricercaCant(KeyEvent event) {
    	if(this.FAtxtRicercaCant.getText().trim().length()>0) {
	    	String ins = this.FAtxtRicercaCant.getText().trim().toUpperCase();
	    	List<Cantiere> list = new ArrayList<>();
	    	for(Cantiere c : model.getCantieri())
	    		if(c.toString().contains(ins))
	    			list.add(c);
	    	this.FAboxCant.getItems().clear();
	    	this.FAboxCant.getItems().add(null);
	    	this.FAboxCant.getItems().addAll(list);
    	} else {
    		this.FAboxCant.getItems().clear();
	    	this.FAboxCant.getItems().add(null);
	    	this.FAboxCant.getItems().addAll(model.getCantieri());
    	}
    }

    @FXML
    void FAricercaForn(KeyEvent event) {
    	if(this.FAtxtRicercaForn.getText().trim().length()>0) {
	    	String ins = this.FAtxtRicercaForn.getText().trim().toUpperCase();
	    	List<String> list = new ArrayList<>();
	    	for(String s : model.getFornitori())
	    		if(s.contains(ins))
	    			list.add(s);
	    	this.FAboxForn.getItems().clear();
	    	this.FAboxForn.getItems().add(null);
	    	this.FAboxForn.getItems().addAll(list);
    	} else {
    		this.FAboxForn.getItems().clear();
	    	this.FAboxForn.getItems().add(null);
	    	this.FAboxForn.getItems().addAll(model.getFornitori());
    	}
    }

    @FXML
    void FAricercaLav(KeyEvent event) {
    	if(this.FAtxtRicercaLav.getText().trim().length()>0) {
	    	String ins = this.FAtxtRicercaLav.getText().trim().toUpperCase();
	    	List<String> list = new ArrayList<>();
	    	for(String s : model.getDescrizioniLavorazioni())
	    		if(s.contains(ins))
	    			list.add(s);
	    	this.FAboxLav.getItems().clear();
	    	this.FAboxLav.getItems().add(null);
	    	this.FAboxLav.getItems().addAll(list);
    	} else {
    		this.FAboxForn.getItems().clear();
	    	this.FAboxForn.getItems().add(null);
	    	this.FAboxForn.getItems().addAll(model.getDescrizioniLavorazioni());
    	}
    }

    @FXML
    void FAricercaVoce(KeyEvent event) {
    	if(this.FAtxtRicercaVoce.getText().trim().length()>0) {
	    	String ins = this.FAtxtRicercaVoce.getText().trim().toUpperCase();
	    	List<String> list = new ArrayList<>();
	    	for(String s : model.getVociCapitolato())
	    		if(s.contains(ins))
	    			list.add(s);
	    	this.FAboxVoce.getItems().clear();
	    	this.FAboxVoce.getItems().add(null);
	    	this.FAboxVoce.getItems().addAll(list);
    	} else {
    		this.FAboxVoce.getItems().clear();
	    	this.FAboxVoce.getItems().add(null);
	    	this.FAboxVoce.getItems().addAll(model.getVociCapitolato());
    	}
    }

    @FXML
    void FAsetFattura(ActionEvent event) {
    	if(this.FAboxFatture.getValue()!=null) {
    		this.FAtxtArea.setText(this.FAboxFatture.getValue().toStringConImporti());
    		this.FAtxtImportoFattura.setText(this.FAboxFatture.getValue().getImportoTot().toString());
    		this.FAtxtNoteFattura.setText(this.FAboxFatture.getValue().getNote());
    	}
    }
    

    @FXML
    void FAcancImporto(ActionEvent event) {

    }

    @FXML
    void FAcancInserimento(ActionEvent event) {

    }

    @FXML
    void FAcancNoteFattura(ActionEvent event) {

    }

    @FXML
    void FAconferma(ActionEvent event) {

    }

    @FXML
    void FAinserisci(ActionEvent event) {
    	if(this.FAboxForn.getValue()!=null && !this.forn.contains(this.FAboxForn.getValue()))
    		forn.add(this.FAboxForn.getValue());
    	if(this.FAboxCant.getValue()!=null && !this.cant.contains(this.FAboxCant.getValue()))
			cant.add(this.FAboxCant.getValue());
    	if(this.FAboxLav.getValue()!=null && !this.lav.contains(this.FAboxLav.getValue()))
			lav.add(this.FAboxLav.getValue());
    	if(this.FAboxVoce.getValue()!=null && !this.voci.contains(this.FAboxVoce.getValue()))
    		voci.add(this.FAboxVoce.getValue());
    	List<Fattura> list = model.getFattureRichieste(this.forn, this.cant, this.lav, this.voci, 
    			this.FAdataDa.getValue(), this.FAdataA.getValue());
    	
    	/*
    	//CONTROLLO FORNITORE
    	if(this.FAboxForn.getValue()!=null && !this.FAtxtArea.getText().contains(this.FAboxForn.getValue())) {
    		//CONTROLLO SE E' IL PRIMO FILTRO INSERITO
	    	if(this.FAboxFatture.getItems().size()<model.getFatture().size()+1)
	    		list.addAll(this.FAboxFatture.getItems());
	    	else
	    		list.add(null);
	    	//AGGIUNGO FATTURE INTERESSATE
    		for(Fattura f : model.getFatture())
    			if(f.getFornitore().equals(this.FAboxForn.getValue()))
    				list.add(f);
    		//MEMORIZZO FORNITORI INTERESSATI
    		for(Fattura f : list)
    			if(f!=null && !forn.contains(f.getFornitore()))
    				forn.add(f.getFornitore());
    		//AGGIORNO BOX FATTURE
    		this.FAboxFatture.getItems().clear();
    		this.FAboxFatture.getItems().addAll(list);*/
    		
    		
    		
    		/* STAMPA
    		this.FAtxtArea.setText("Elenco fatture di ");
    		for(String s : forn) {
    			if(forn.indexOf(s)!=0)
    				this.FAtxtArea.appendText(", ");
    			this.FAtxtArea.appendText(s);
    		}
    		for(Fattura f : list)
    			if(f!=null)
    				this.FAtxtArea.appendText("\n " + f.toString());*/
    }

    @FXML
    void FAinserisciImporto(ActionEvent event) {

    }

    @FXML
    void FAmodNoteFattura(ActionEvent event) {

    }

    @FXML
    void FAreset(ActionEvent event) {

    }

    @FXML
    void FAricercaCant2(KeyEvent event) {

    }

    @FXML
    void FAricercaVoce2(KeyEvent event) {

    }

    @FXML
    void FArimuovi(ActionEvent event) {

    }

    @FXML
    void FAsetImporto(ActionEvent event) {

    }
    
    //INIZIALIZZAZIONE
    //
    //
    //
    
    private Model model;
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
    	//TAB INSERISCI FATTURA
        assert IFtxtFornitore != null : "fx:id=\"IFtxtFornitore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFtxtNum != null : "fx:id=\"IFtxtNum\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFdata != null : "fx:id=\"IFdata\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFboxIVA != null : "fx:id=\"IFboxIVA\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFtxtRicercaCantiere != null : "fx:id=\"IFtxtRicercaCantiere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFboxCantieri != null : "fx:id=\"IFboxCantieri\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFtxtLavorazione != null : "fx:id=\"IFtxtLavorazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert IFtxtRicercaVoce != null : "fx:id=\"IFtxtRicercaVoce\" was not injected: check your FXML file 'Scene.fxml'.";
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
        assert FAtxtArea != null : "fx:id=\"FAtxtArea\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAtxtImportoFattura != null : "fx:id=\"FAtxtImportoFattura\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAboxFatture != null : "fx:id=\"FAboxFatture\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAtxtNoteFattura != null : "fx:id=\"FAtxtNoteFattura\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAbtnCancNoteFattura != null : "fx:id=\"FAbtnCancNoteFattura\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAbtnModNoteFattura != null : "fx:id=\"FAbtnModNoteFattura\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAboxImporti != null : "fx:id=\"FAboxImporti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAboxCant2 != null : "fx:id=\"FAboxCant2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAtxtLavorazione != null : "fx:id=\"FAtxtLavorazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAtxtRicercaVoce2 != null : "fx:id=\"FAtxtRicercaVoce2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAboxVoce2 != null : "fx:id=\"FAboxVoce2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAbtnCanImporto != null : "fx:id=\"FAbtnCanImporto\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAtxtImportNoIva != null : "fx:id=\"FAtxtImportNoIva\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAtxtImportoTot != null : "fx:id=\"FAtxtImportoTot\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAbtnInserisci2 != null : "fx:id=\"FAbtnInserisci2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAtxtRicercaCant2 != null : "fx:id=\"FAtxtRicercaCant2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAtxtNoteImporto != null : "fx:id=\"FAtxtNoteImporto\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAbtnCancInserimento != null : "fx:id=\"FAbtnCancInserimento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAbtnConferma != null : "fx:id=\"FAbtnConferma\" was not injected: check your FXML file 'Scene.fxml'.";
        assert FAbtnReset != null : "fx:id=\"FAbtnReset\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel (Model model) {
    	this.model = model;
    	//TAB INSERISCI FATTURA
    	this.IFboxIVA.getItems().clear();
    	this.IFboxIVA.getItems().addAll(0, 4, 10, 22);
    	this.IFboxCantieri.getItems().clear();
    	this.IFboxCantieri.getItems().add(null);
    	this.IFboxCantieri.getItems().addAll(model.getCantieri());
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
    	this.FAboxForn.getItems().clear();
    	this.FAboxForn.getItems().add(null);
    	this.FAboxForn.getItems().addAll(model.getFornitori());
    	this.FAboxCant.getItems().clear();
    	this.FAboxCant.getItems().add(null);
    	this.FAboxCant.getItems().addAll(model.getCantieri());
    	this.FAboxLav.getItems().clear();
    	this.FAboxLav.getItems().add(null);
    	this.FAboxLav.getItems().addAll(model.getDescrizioniLavorazioni());
    	this.FAboxVoce.getItems().clear();
    	this.FAboxVoce.getItems().add(null);
    	this.FAboxVoce.getItems().addAll(model.getVociCapitolato());
    	this.FAboxFatture.getItems().clear();
    	this.FAboxFatture.getItems().add(null);
    	this.FAboxFatture.getItems().addAll(model.getFatture());
    	this.forn = new ArrayList<>();
    	this.cant = new ArrayList<>();
    	this.lav = new ArrayList<>();
    	this.voci = new ArrayList<>();
    	
    }
    
}
