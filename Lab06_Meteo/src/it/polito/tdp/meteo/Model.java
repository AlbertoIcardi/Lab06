package it.polito.tdp.meteo;

import java.util.*;

import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {
	
	//Il livello rappresenta i giorni trascorsi tra 1 e 15
	//La soluzione parziale � una lista di Simple City (comprende nome e costo)
	//Soluzione parziale � anche completa quando si raggiunge l'ultimo livello
	//Una soluzione parziale � valida se il tecnico ha trascorso al massimo sei giornate in una determinata citt�
	//Una soluzione completa non � valida se il tecnico � stato meno di tre giorni consecutivi e deve essere stato in tutte le citt�
	//Ciclo sulle citt�
	//Struttura dati per soluzione parziale/completa � una lista di Simple City
	//Lista di Simple City per il migliore

	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	MeteoDAO mDAO;
	
	public Model() {
		mDAO=new MeteoDAO();
	}

	public String getUmiditaMedia(int mese) {
		String elencoMedie="";
		for(String s:mDAO.getElencoLocalita())
			elencoMedie+=s+" "+mDAO.getAvgRilevamentiLocalitaMese(mese, s)+"%\n";
		return elencoMedie;
	}

	public String trovaSequenza(int mese) {
		List<SimpleCity> best=new LinkedList<SimpleCity>();
		List<SimpleCity> parziale=new LinkedList<SimpleCity>();
		int livello=0; //Il livello in questo caso � il giorno
		ricorsione(best, livello, parziale, mese);
		return "TODO!";
	}
	
	public void ricorsione(List<SimpleCity> best, int livello, List<SimpleCity> parziale, int mese){
		//Condizione di terminazione
		if(livello==NUMERO_GIORNI_TOTALI){
			if(punteggioSoluzione(parziale)<=punteggioSoluzione(best)){
				best.clear();
				best.addAll(parziale);
				return;
			}
		}
		for(String s:mDAO.getElencoLocalita()){
			if(controllaParziale(parziale)){
				SimpleCity sc=mDAO.getElencoSimpleCity(mese, livello, s);
				if(mDAO.getElencoSimpleCity(mese, livello, s)!=null){
					parziale.add(sc);
					ricorsione(best, livello+1, parziale, mese);
					parziale.remove(sc);
				}	
			}
		}
	}

	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {
		double score = 0.0;
		score+=soluzioneCandidata.get(0).getCosto();
		for(int i=1; i<soluzioneCandidata.size(); i++){
			if(soluzioneCandidata.get(i).equals(soluzioneCandidata.get(i-1)))
				score+=soluzioneCandidata.get(i).getCosto();
			else
				score+=soluzioneCandidata.get(i).getCosto()+COST;
		}
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {
		
		return true;
	}

}
