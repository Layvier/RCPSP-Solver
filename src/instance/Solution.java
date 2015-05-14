package instance;

import java.io.PrintStream;
import java.util.Arrays;

/**
 * Représente une solution du problème de gestion de projet à contraintes de ressources.
 * <br>NE PAS MODIFIER LE FONCTIONNEMENT DE L'ATTRIBUT m_solution 
 * 
 * @author Damien Prot
 *
 */

public class Solution {

	// --------------------------------------------
	// --------------- ATTRIBUTS ------------------
	// --------------------------------------------

	/**
	 * Solution stockée sous la forme d'un tableau. Le jème élément dans le
	 * tableau correspond une tâche j, et contient la date de début de cette tâche
	 * <BR>NE PAS MODIFIER LE FONCTIONNEMENT DE CET ATTRIBUT
	 */
	protected Integer[] m_solution;

	/**
	 * Valeur de l'objectif, i.e. la date de fin de tous les travaux. Attention il faut soit maintenir cette valeur correcte
	 * lorsqu'on modifie la solution soit la recalculer (méthode
	 * <code>evaluate</code>) pour que la valeur soit correcte.
	 * <br> Attention : le calcul complet avec evaluate est coûteux en temps.
	 *
	 */
	protected int m_objectif = 0;

	/** 
	 * Somme des ressources utilisées à chaque pas de temps.
	 * Pour une solution donné le recalcul de chaque ressource utilisée est réalisé dans <code>validate</code>.
	 * <br>Pour des raisons de performance il est préférable de mettre cet élément à jour incrémentalement 
	 * à l'aide de la méthode setUtilisationRessource(int i, int date, int val) que de tout recalculer.
	 * <br> Cette mise à jour incrémentale est réalisée par exemple dans <code>ajouterTache(int j, int date)</code>.  
	 */
	private int[][] utilisationRessource;


	/** Problème associé à la solution */
	protected Instance m_instance;

	/** Code d'erreur disponible après appel à la méthode <code>validate</code> */
	protected String m_error="";

	// --------------------------------------------
	// --------------- ACCESSEURS -----------------
	// --------------------------------------------



	/**
	 * @return Tableau de booléen représentant la solution
	 */
	public Integer[] getSolution() {
		return m_solution;
	}

	/**
	 * @return Valeur de l'objectif. 
	 */
	public int getObjectif() {
		return m_objectif;
	}

	/**
	 * Fixe la valeur de l'objectif à objectif
	 * 
	 * @param objectif
	 *          : valeur de l'objectif
	 */
	public void setObjectif(int objectif) {
		this.m_objectif = objectif;
	}

	/**
	 * Retourne la quantité de ressource i utilisé à la date <code>date</code>.
	 */
	public int getUtilisation(int i, int date) {
		return getUtilisationRessource()[i][date]; 
	}

	/**
	 * Initialise l'utilisation de la ressource <code>i</code> à la valeur <code>val</code> pour la date <code>date</code>.
	 */
	public void setUtilisationRessource(int i, int date, int val) {
		utilisationRessource[i][date]=val;
	}

	/**
	 * 
	 * Retourne le problème associé à la solution
	 */
	public Instance getInstance() {
		return m_instance;
	}

	/**
	 * Retourne le code d'erreur disponible après appel à la méthode
	 *         <code>validate</code>
	 */
	public String getError() {
		return m_error;
	}

	// --------------------------------------
	// ------------ CONSTRUCTEUR ------------
	// --------------------------------------

	/** Crée un objet représentant une solution du problème <code>inst</code>. 
	 * @throws Exception */
	public Solution(Instance inst) throws Exception { 
		m_instance = inst;
		int n_Taches = inst.getNbTaches();
		int m_ressources = inst.getNbRessources();
		m_solution = new Integer[n_Taches];
		Arrays.fill(m_solution, null);

		// --- Horizon de temps suffisant: la somme des durées des tâches
		int dureeMax = 0;
		for(int j=0;j<n_Taches;j++){
			dureeMax += inst.getDuree(j);
		}
		//dureeMax=100;
		setUtilisationRessource(new int[m_ressources][dureeMax]);
		for (int[] row: getUtilisationRessource())
			Arrays.fill(row, 0);
	}

	// --------------------------------------
	// -------------- METHODES --------------
	// --------------------------------------


	/** 
	 * Surcharge de la méthode <code>clone</code> de la classe <code>Object</code>. 
	 * @return Renvoie une copie de la solution.
	 */
	public Solution clone() {
		Solution solution=null;
		try {
			solution = new Solution(m_instance);
		} catch (Exception e) { 
			e.printStackTrace();
		}
		solution.m_objectif = m_objectif;
		solution.m_solution = Arrays.copyOf(m_solution, m_instance.getNbTaches());
		solution.setUtilisationRessource(Arrays.copyOf(getUtilisationRessource(),m_instance.getNbRessources()));
		solution.m_error = new String(m_error); 
		return solution;	  
	}


	/**
	 * Ajoute la tâche <code>j</code> à la date <code>date</code> (déplace la tâche si elle est déjà ordonnancée).
	 * Met à jour la valeur et les différentes ressources utilisées.
	 * @param j : indice de la tâche à ajouter.
	 * @param date : date de début de la tâche
	 * @throws Exception Retourne une erreur si l'indice ne correspond pas à une tâche.
	 */
	public void ajouterTache(int j, int date) throws Exception {
		int nbTaches = m_instance.getNbTaches();
		if ((j < 0) || (j > nbTaches))
			throw new Exception("Erreur: " + j + " n\'est pas un numéro de tâche compris entre 0 et " + (nbTaches - 1));

		// --- Si la tâche était déjà affectée à une date, on la retire
		if (m_solution[j] != null ) {
			retirerTache(j);
		}

		// --- MàJ de la solution
		m_solution[j]=date;

		// MAJ de l'objectif
		m_objectif = Math.max(m_objectif, date+m_instance.getDuree(j));

		// MAJ de l'utilisation des ressources
		for (int t=date;t<date+m_instance.getDuree(j);t++){
			for (int i = 0; i < m_instance.getNbRessources(); i++) {
				getUtilisationRessource()[i][t] += m_instance.getRessourcePourJob(i, j);
			}
		}
	}

	/**
	 * Retire la tâche <code>j</code> des tâches faites (ne fait rien si la tâche n'était pas ordonnancée).
	 * Met à jour l'utilisation des ressources et l'objectif.
	 * @param j indice de la tâche à retirer.
	 * @throws Exception Retourne une erreur si l'indice ne correspond pas à un objet.
	 */
	public void retirerTache(int j) throws Exception {
		int nbTaches = m_instance.getNbTaches();
		if ((j < 0) || (j > nbTaches))
			throw new Exception("Erreur: " + j + " n\'est pas un numéro de tâche compris entre 0 et " + (nbTaches - 1));

		if (m_solution[j]!=null){
			// --- MàJ de l'objectif
			evaluate();

			int date = m_solution[j];
			// MAJ de l'utilisation des ressources
			for (int t=date;t<date+m_instance.getDuree(j);t++){
				for (int i = 0; i < m_instance.getNbRessources(); i++) {
					getUtilisationRessource()[i][t] -= m_instance.getRessourcePourJob(i, j);
				}
			}

			// --- MàJ de la solution
			m_solution[j]=null;
		}
	} 


	/**
	 * Vérifie que l'ajout de la tâche <code>j</code> à la date <code>date</code> ne viole pas de contrainte de ressource 
	 * et de précédence (par rapport à ce qui est déjà dans la solution)
	 * @param j index de la tâche à ajouter.
	 * @param date la date de début de la tâche à ajouter.
	 * @return true si l'ajout est possible, false sinon.
	 * @throws Exception
	 */
	public boolean ajoutPossible(int j, int date) throws Exception {
		int nbTaches = m_instance.getNbTaches();
		if ((j < 0) || (j > nbTaches))
			throw new Exception("Erreur: " + j + " n\'est pas un numéro de tâche compris entre 0 et " + (nbTaches - 1));

		// --- Vérification des contraintes de ressource
		int nbContraintes = m_instance.getNbRessources();
		int dureeJob = m_instance.getDuree(j);
		for (int t=date;t<date+dureeJob;t++){
			for (int i = 0; i < nbContraintes; i++) {
				if ( getUtilisationRessource()[i][t] + m_instance.getRessourcePourJob(i,j) > m_instance.getDispoRessource(i))
					return false;
			}
		}

		// Vérification des contraintes de précédence
		for (int i=0;i<nbTaches;i++){
			// Si i est un prédécesseur de j, il doit être ordonnancé avant
			if (m_instance.estPredecesseur(i, j)){
				if (m_solution[i]!=null){
					if (m_solution[i]+m_instance.getDuree(i)>date)
						return false;
				}
			}
			// Si i est un successeur de j, il doit être ordonnancé après
			if (m_instance.estPredecesseur(j,i)){
				if (m_solution[i]!=null){
					if (m_solution[i]<m_instance.getDuree(j)+date)
						return false;
				}
			}
		}

		return true;
	}



	/**
	 * Recalcule et retourne la valeur de l'objectif.
	 * Met à jour l'attribut <code>m_objectif</code>.
	 * 
	 * @throws Exception
	 */ 
	public int evaluate() throws Exception {
		m_objectif = 0;
		int nbTaches = m_instance.getNbTaches();
		for (int j = 0; j < nbTaches; j++) {
			if (m_solution[j]!=null) {
				m_objectif = Math.max(m_objectif,m_instance.getDuree(j)+m_solution[j]);
			}
		}
		return m_objectif;
	}


	/**
	 * Vérifie que la soution est une solution réalisable du RCPSP. Les opérations /
	 * tests réalisés sont les suivants : <li>recalcule l'objectif
	 * (appel à la méthode <code>evaluate</code>) et l'utilisation des ressources. <li>Vérifie que chaque
	 * contrainte est vérifiée. Lorsqu'une erreur est rencontrée, les messages
	 * d'erreurs sont accessibles en apppelant la méthode <code>getError()</code>.
	 * 
	 * @return Renvoie le booleen <code>true</code> si la solution est valide,
	 *         <code>false</code> sinon.
	 * @throws Exception
	 */
	 /**
     * Vérifie que la soution est une solution réalisable du RCPSP. Les opérations /
     * tests réalisés sont les suivants : <li>recalcule l'objectif
     * (appel à la méthode <code>evaluate</code>) et l'utilisation des ressources. <li>Vérifie que chaque
     * contrainte est vérifiée. Lorsqu'une erreur est rencontrée, les messages
     * d'erreurs sont accessibles en apppelant la méthode <code>getError()</code>.
     *
     * @return Renvoie le booleen <code>true</code> si la solution est valide,
     *         <code>false</code> sinon.
     * @throws Exception
     */
    public boolean validate() throws Exception {
        boolean result = true;
        m_error = "";
        evaluate();


        int nbTaches = m_instance.getNbTaches();
        int nbRessources = m_instance.getNbRessources();
        //--- MAJ de l'utilisation des ressources
        for (int[] row: getUtilisationRessource())
            Arrays.fill(row, 0);
        for (int j=0;j<nbTaches;j++){
            if (m_solution[j]==null){
                m_error+= "Erreur: la tâche " + j + " n'est pas ordonnancée\n";
                result = false;
            }else{
                for (int t=m_solution[j];t<m_solution[j]+m_instance.getDuree(j);t++){
                    for (int i = 0; i < nbRessources; i++) {
                        getUtilisationRessource()[i][t] += m_instance.getRessourcePourJob(i, j);
                    }
                }
            }
        }

        int dureeMax = getUtilisationRessource()[0].length;
        for (int t=0;t<dureeMax;t++){
            for (int i = 0; i < nbRessources ; i++) {
                if (getUtilisationRessource()[i][t] > m_instance.getDispoRessource(i)) {
                    m_error += "Erreur: la ressource " + i + " à l'instant " + t + " est surconsommée.\n";
                    result = false;
                }
            }
        }


        // --- Contrainte de précédence
        for (int j=0;j<nbTaches;j++){
            if (m_solution[j]!=null){

                // Vérification des contraintes de précédence
                for (int i=j+1;i<nbTaches;i++){
                    // Si i est un prédécesseur de j, il doit être ordonnancé avant
                    if (m_instance.estPredecesseur(i, j)){
                        if (m_solution[i]!=null){
                            if (m_solution[i]+m_instance.getDuree(i)>m_solution[j]){
                                m_error += "Erreur: la tâche " + i + " doit être finie avant le début de la tâche "+ j + "\n";
                                result = false;
                            }
                        }
                    }
                    // Si i est un successeur de j, il doit être ordonnancé après
                    if (m_instance.estPredecesseur(j,i)){
                        if (m_solution[i]!=null){
                            if (m_solution[i]<m_instance.getDuree(j)+m_solution[j]){
                                m_error += "Erreur: la tâche " + j + " doit être finie avant le début de la tâche "+ i + "\n";
                                result = false;
                            }
                        }
                    }
                }
            }
        }


        if (result)
            m_error += "La solution est réalisable.";
        else
            m_error += "La solution n'est pas réalisable.";
        return result;
    }

	/**
	 * Imprime la solution sur la sortie <code>out</code>.
	 * 
	 * @param out
	 *          : sortie
	 */
	public void print(PrintStream out) {
		out.println("Résolution du RCPSP\nValeur : " + m_objectif + "\nDates choisies : ");
		for (int j = 0; j < m_instance.getNbTaches(); j++) {
			out.print("Tâche " + j + ": " + m_solution[j]+"\n");
		}
		out.println("");
	}


	public int[][] getUtilisationRessource() {
		return utilisationRessource;
	}

	public void setUtilisationRessource(int[][] utilisationRessource) {
		this.utilisationRessource = utilisationRessource;
	}

}
