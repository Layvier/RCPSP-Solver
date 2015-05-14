package resolution;

import instance.Instance;
import instance.Solution;
import resolution.LST;

/**
 * La classe <code>RCPSPSolver</code> modélise le moteur de résolution du problème. 
 * <br>
 * <br>Cette classe contient notamment la méthode <code>solve</code> qui doit être modifiée 
 * afin de résoudre le problème.
 * <br>
 * <br> La solution trouvée par l'algorithme que vous devez développer sera modélisée par
 * un objet de la classe <code>Solution</code> stocké dans l'attribut <code>m_solution</code>.
 * <br>
 * <br> Vous pouvez librement ajouter des fonctions à RCPSPSolver ou créer de nouvelles classes 
 * qui seront appelées ou instanciées depuis la méthode <code>solve</code>.
 * 
 * Le fichier RCPSPSolver.java est à rendre à la fin du projet avec tous les autres fichiers créés. 
 * 
 * @author Damien Prot 
 * 
 */
public class RCPSPSolver {

	// --------------------------------------------
	// --------------- ATTRIBUTS ------------------
	// --------------------------------------------

	/** Stocke la solution du problème */
	private Solution m_solution;

	/** Donnnées du problème de RCPSP */
	private Instance m_instance;

	/** Temps alloué à la résolution du problème */
	private long m_time;


	// --------------------------------------------
	// --------------- ACCESSEURS -----------------
	// --------------------------------------------

	// Pour accéder aux attributs de la classe RCPSPSolver

	/** @return la solution du problème */
	public Solution getSolution() {
		return m_solution;
	}

	/** @return Donnnées du problème de RCPSP */
	public Instance getInstance() {
		return m_instance;
	}

	/** @return Temps alloué à la résolution du problème */
	public long getTime() {
		return m_time;
	}

	/**
	 * Fixe la solution du problème
	 * 
	 * @param m_solution
	 *          : la solution du problème
	 */
	public void setSolution(Solution m_solution) {
		this.m_solution = m_solution;
	}

	/**
	 * Fixe l'instance du problème
	 * 
	 * @param m_instance
	 *          : l'instance du problème
	 */
	public void setInstance(Instance m_instance) {
		this.m_instance = m_instance;
	}

	/**
	 * Fixe le temps alloué à la résolution du problème
	 * 
	 * @param time
	 *          : temps alloué à la résolution du problème
	 */
	public void setTime(long time) {
		this.m_time = time;
	}


	// --------------------------------------
	// -------------- METHODES --------------
	// --------------------------------------

	/**
	 * TODO Méthode à modifier pour la résolution du problème.
	 * 
	 * Attention à ne pas écrire de message sur la sortie standard (pas de
	 * <code>System.out.print/println</code>). Vous pouvez utilisez la sortie
	 * d'erreur (<code>System.err.print/println</code>).
	 * 
	 * Plus précisément il faut renseigner la solution <code>m_solution</code>
	 * pour créer la meilleure solution possible. Se référer à la documentation de
	 * la classe <code>Solution</code> pour la méthode et le codage de la
	 * solution. 
	 * 
	 * <br> Attention, il faut vérifier que le temps écoulé est inférieur à
	 * la limite (30 secondes par défaut) spécifiée après l'option -t en 
	 * paramètre du programme.  
	 * 
	 * @param time
	 *          temps accordé (en secondes) à la résolution du problème.
	 * @throws Exception
	 *           Retourne des exceptions notamment en cas d'erreur dans la lecture
	 *           du fichier d'entrée.
	 */
	public void solve(long time) throws Exception {
		m_time = time;
		// gestion du temps maximum
		long t = System.currentTimeMillis(); // heure de début (en ms)
		long tempspasse = 0;

		
		// --- Exemple de méthode (très naïve) de résolution : vous DEVEZ modifier ce code
		int nbMethodes = 11;
		int[] tempsMethode = new int[nbMethodes];
		Solution[] solutions = new Solution[nbMethodes];
		for(int l=0; l<nbMethodes; l++){
			solutions[l] = new Solution(m_instance);
		}
		int nbTaches = m_instance.getNbTaches();
		LST lst = new LST(m_instance, m_solution);
		int LST = lst.getFinMin();
		
		for(int k=0; k<nbMethodes; k++){
		int tempsCourant = 0;
		TachesTotales tachestotales = new TachesTotales(nbTaches);
		Priorite priorite= new Priorite(solutions[k], tachestotales.getTableau(), tempsCourant);
		solutions[k].ajouterTache(0, tempsCourant);
		Paires paires = new Paires(m_instance, nbTaches);
		while(tachestotales.estVide() == false){
			System.err.println("nbtache : "+nbTaches);
			int[] tachesPossibles = priorite.actualiser(solutions[k], tachestotales.getTableau(), tempsCourant, this.getInstance(), lst);
			paires.actualiser(m_instance, tachestotales.getTableau(), tempsCourant, solutions[k], tachesPossibles);
			priorite.prioriteGlobal(k, tachesPossibles, m_instance, lst, solutions[k], paires);
			
			if(priorite.estVide()){
				tempsCourant=tempsCourant+1;
			}
			else{
				solutions[k].ajouterTache(priorite.getPremiere(), tempsCourant);
				System.err.println("Tache "+priorite.getPremiere()+"au temps "+tempsCourant+" de durée "+this.getInstance().getDuree(priorite.getPremiere()));
				tachestotales.retirerTaches(priorite.getPremiere());
				System.err.println("");
				System.err.println("Tache retirée : "+priorite.getPremiere());
			}
			System.err.println("temps : "+tempsCourant);
			
		}
		tempsMethode[k] = solutions[k].getObjectif();
		/*
		System.err.println("FinMin: "+LST);
		System.out.println("est : ");
		for(int i= 0; i< nbTaches; i++){
			System.out.print(" "+lst.est[i]);	
		}
		System.out.println("");
		System.out.println("lst : ");
		for(int i= 0; i< nbTaches; i++){
			System.out.print(" "+lst.lst[i]);	
		}
		
		System.err.println("");
		System.err.println("GFPN : ");*/
		/*for(int i=0; i< paires.APN.length; i++){
			if(i<=99)		System.err.print(" ");
			System.err.print(" "+i);
		}
		
		for(int i=0; i< paires.GFPN.length; i++){
			if(paires.GFPN[i][0]<=9 && paires.GFPN[i][1] >=10)		System.err.print(" ");
			System.err.print(" "+paires.GFPN[i][0]);
		}
		System.err.println("");
		for(int i=0; i< paires.GFPN.length; i++){
			System.err.print(" "+paires.GFPN[i][1]);
		}*/
		
		//System.err.println("Longueurs : "+paires.APN.length+" = "+
	//	paires.CSPN.length+" + "+paires.GFPN.length+" + "+paires.TFPN.length);
		}
		int tempsMin = tempsMethode[0];
		int min = 0;
		for(int j=0; j<nbMethodes; j++){
			System.err.println("Temps de la méthode"+j+" : "+tempsMethode[j]);
			if(tempsMin > tempsMethode[j]){
				tempsMin = tempsMethode[j];
				min = j;
			}
		}
		m_solution=solutions[min];
		
		
		/*
		
		
		
		
		// --- Boucle sur les tâches
		// --- On fixe la tâche (j+1) à la date de fin de la tâche j.
		// --- Cela fonctionne uniquement si les tâches sont bien triées par rapport à leur précédence,
		// --- i.e. si i précède j, alors i<j
		for (int j=0;j<nbTaches;j++){
			m_solution.ajouterTache(j, tempsCourant);
			tempsCourant+= m_instance.getDuree(j);
		}
		*/
		tempspasse = System.currentTimeMillis() - t;
		System.err.println("Temps pour l'heuristique : " + tempspasse);
		// --- Affichage console de la solution
		m_solution.print(System.err);
		System.err.flush();
		System.err.println("Results found with "+nbMethodes+" heuristics with different priority rules for the RCPSP NP-Problem :");
		for(int j=0; j<nbMethodes; j++){
			System.err.println("Shortest duration found by method "+j+" : "+tempsMethode[j]);
		}
		System.err.println("Best heuristic method for this data : "+min);
		
		
		
		
	}

}
