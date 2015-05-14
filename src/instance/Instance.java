package instance;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Contient l'ensemble des données de l'instance spécifiée pour le problème RCPSP.
 * <br>Réalise la lecture du fichier spécifié.
 * <br>NE PAS MODIFIER CETTE CLASSE 
 * @author Damien Prot
 *
 */
public class Instance {

	// --------------------------------------------
	// --------------- ATTRIBUTS ------------------
	// --------------------------------------------

	/**
	 *  Nombre de ressources
	 */
	private int m_ressource;

	/** Nombre de tâches */
	private int n_taches;


	/**
	 * Durée des tâches
	 */
	private Integer[] duree;

	/**
	 * Nombre de ressource de chaque tâche des tâches (le terme [i][j] est le nombre nécessaire de ressource i pour la tâche j)
	 */
	private Integer[][] ressource;

	/**
	 * Nombres de disponibilité de chaque ressource
	 */
	private Integer[] dispoRessource;


	/**
	 * Matrice des précédences : si precedence[i][j] = true, alors la tâche i doit être terminée avant le début de la tâche j
	 */
	private boolean[][] precedence;

	/**
	 * Matrice des précédences : si precedenceImmediate[i][j] = true, alors la tâche i doit être terminée avant le début de la tâche j
	 * (et il n'existe pas de tâche k telle que i doit finir avant k et k doit finir avant j)
	 */
	private boolean[][] precedenceImmediate;


	/** Nom du fichier correspondant à l'instance chargée */
	private String m_fileName;

	// --------------------------------------------
	// --------------- ACCESSEURS -----------------
	// --------------------------------------------

	/** 
	 * Retourne le nombre de tâches dans le problème */
	public int getNbTaches() {
		return n_taches;
	}

	/** Retourne le nombre de ressources dans le problème */
	public int getNbRessources() {
		return m_ressource;
	}

	/**
	 * Retourne le profit de la tâche <code>j</code>
	 * @param j
	 *          numéro de la tâche (doit être compris entre 0 et le nombre de tâches
	 *          du problème - 1).
	 * @throws Exception
	 *           retourne une exception si j n'est pas un indice valide.
	 */
	public Integer getDuree(int j) throws Exception {
		if ((j < 0) || (j >= n_taches))
			throw new Exception("Erreur : " + j + " n\'est pas un numéro de tâches compris entre 0 et " + (n_taches - 1));
		return duree[j];
	}

	/**
	 * Retourne la disponibilité de la ressource i
	 * @param i
	 *          numéro de la ressource (doit être compris entre 0 et le nombre de
	 *          ressource du problème - 1).
	 * @throws Exception
	 *           retourne une exception si i n'est pas un indice valide.
	 */
	public Integer getDispoRessource(int i) throws Exception {
		if ((i < 0) || (i > +m_ressource))
			throw new Exception("Erreur : " + i + " n\'est pas un numéro de ressource compris entre 0 et " + (m_ressource - 1));
		return dispoRessource[i];
	}



	/**
	 * Retourne le nombre de ressource de type i nécesaire pour effectuer la tâche j.
	 * 
	 * @param i
	 *          numéro de la ressource (doit être compris entre 0 et le nombre de
	 *          ressources du problème - 1).
	 * @param j
	 *          numéro de la tâche (doit être compris entre 0 et le nombre de tâches
	 *          du problème - 1).
	 * @return nombre de ressource de type i nécesaire pour effectuer la tâche j.
	 * @throws Exception
	 *           retourne une exception si i ou j ne sont pas des indices valides.
	 **/
	public Integer getRessourcePourJob(int i, int j) throws Exception {
		if ((j < 0) || (j >= n_taches))
			throw new Exception("Erreur : " + j + " n\'est pas un numéro de tâche compris entre 0 et " + (n_taches - 1));
		if ((i < 0) || (i >= m_ressource))
			throw new Exception("Erreur : " + i + " n\'est pas un numéro de ressource compris entre 0 et " + (m_ressource - 1));
		return ressource[i][j];
	}


	/**
	 * Retourne vrai si la tâche i doit être terminée pour pouvoir effectuer la tâche j.
	 * 
	 * @param j
	 *          numéro de la tâche (doit être compris entre 0 et le nombre de tâches
	 *          du problème - 1).
	 * @param i
	 *          numéro de la tâche (doit être compris entre 0 et le nombre de tâches
	 *          du problème - 1).
	 * @return vrai si la tâche i doit être terminée pour pouvoir effectuer la tâche j.
	 * @throws Exception
	 *           retourne une exception si i ou j ne sont pas des indices valides.
	 **/
	public boolean estPredecesseur(int i, int j) throws Exception {
		if ((j < 0) || (j >= n_taches))
			throw new Exception("Erreur : " + j + " n\'est pas un numéro de tâche compris entre 0 et " + (n_taches - 1));
		if ((i < 0) || (i >= n_taches))
			throw new Exception("Erreur : " + i + " n\'est pas un numéro de tâche compris entre 0 et " + (n_taches - 1));
		return precedence[i][j];
	}


	/**
	 * Retourne vrai si la tâche i est un prédecesseur immédiat la tâche j.
	 * 
	 * @param j
	 *          numéro de la tâche (doit être compris entre 0 et le nombre de tâches
	 *          du problème - 1).
	 * @param i
	 *          numéro de la tâche (doit être compris entre 0 et le nombre de tâches
	 *          du problème - 1).
	 * @return vrai si la tâche i doit être terminée pour pouvoir effectuer la tâche j.
	 * @throws Exception
	 *           retourne une exception si i ou j ne sont pas des indices valides.
	 **/
	public boolean estPredecesseurImmediat(int i, int j) throws Exception {
		if ((j < 0) || (j >= n_taches))
			throw new Exception("Erreur : " + j + " n\'est pas un numéro de tâche compris entre 0 et " + (n_taches - 1));
		if ((i < 0) || (i >= n_taches))
			throw new Exception("Erreur : " + i + " n\'est pas un numéro de tâche compris entre 0 et " + (n_taches - 1));
		return precedenceImmediate[i][j];
	}


	/**
	 * Renvoie la matrice des ressources nécessaires
	 */
	public Integer[][] getRessources() {
		return ressource;
	}

	/**
	 * Renvoie le nom du fichier chargé.
	 */
	public String getFileName() {
		return m_fileName;
	}

	// --------------------------------------
	// ------------ CONSTRUCTEUR ------------
	// --------------------------------------

	/**
	 * Constructeur : crée un objet du type Instance et charge l'instance du
	 * problème correspondant au fichier fileName.
	 * 
	 * @param fileName
	 *          fichier d'instance.
	 * @throws IOException
	 *           Exception retournée en cas d'erreur de lecture dans le fichier.
	 */
	public Instance(String fileName) throws IOException {
		m_fileName = fileName;
		read();
	}

	// --------------------------------------
	// -------------- METHODES --------------
	// --------------------------------------

	/** Réalise la lecture du fichier d'instance */
	private void read() throws IOException {

		File mfile = new File(m_fileName);
		if (!mfile.exists()) {
			throw new IOException("Le fichier saisi : " + m_fileName + ", n'existe pas.");
		}
		Scanner sc = new Scanner(mfile);
		String line = sc.nextLine();

		//--- Lecture du nombre de tâches
		while (!line.contains("jobs"))
			line = sc.nextLine();
		Scanner lineSc = new Scanner(line);
		String s;
		do{
			s = lineSc.next();
		}while(!s.contains(":"));
		n_taches = lineSc.nextInt();

		precedence = new boolean[n_taches][n_taches];
		precedenceImmediate = new boolean[n_taches][n_taches];
		duree = new Integer[n_taches];

		// --- Lecture du nombre de ressources
		while (!line.contains("renewable"))
			line = sc.nextLine();
		lineSc = new Scanner(line);
		do{
			s = lineSc.next();
		}while(!s.contains(":"));
		m_ressource = lineSc.nextInt();

		dispoRessource = new Integer[m_ressource];
		ressource = new Integer[m_ressource][n_taches];

		// --- Lecture des précédences
		while (!line.contains("jobnr"))
			line = sc.nextLine();

		for (int j=0;j<n_taches;j++){
			line = sc.nextLine();
			lineSc = new Scanner(line);

			// ---Initialisation
			for (int i=0;i<n_taches;i++){
				precedence[j][i]=false;
				precedenceImmediate[j][i]=false;
			}
			lineSc.nextInt();
			lineSc.nextInt();
			int nbSucc = lineSc.nextInt();
			for (int i=0;i<nbSucc;i++){
				int succ = lineSc.nextInt()-1; // --- La numérotation commence à 1 dans le fichier de donnée
				precedence[j][succ]=true;
				precedenceImmediate[j][succ]=true;
			}
		}


		// --- Fermeture transitive des précédences, pour une manipulation plus facile 
		// --- Pour chaque tâche j 
		for (int j=0;j<n_taches;j++){
			ArrayList<Integer> lSucc = new ArrayList<Integer>();
			// --- On ajoute ses successeurs immédiats dans une liste
			for (int i=j+1;i<n_taches;i++){
				if (precedence[j][i]==true)
					lSucc.add(i);
			}
			// --- On ajoute si besoin les successeurs de chaque élément de la liste
			while (lSucc.size()!=0){
				int current = lSucc.get(0);
				lSucc.remove(0);
				precedence[j][current]=true;
				for (int i=current+1;i<n_taches;i++){
					if (precedence[current][i]==true && !lSucc.contains(i))
						lSucc.add(i); 
				}
			}		
		}

		// --- Lecture des ressources
		while (!line.contains("jobnr"))
			line = sc.nextLine();

		sc.nextLine();
		for (int j=0;j<n_taches;j++){
			line = sc.nextLine();
			lineSc = new Scanner(line);
			lineSc.nextInt();
			lineSc.nextInt();
			duree[j]=lineSc.nextInt();
			for (int i=0;i<m_ressource;i++)
			{
				ressource[i][j] = lineSc.nextInt();
			}
		}
		
		
		// --- Dispo des ressources
		while (!line.contains("RESOURCEAVAILABILITIES"))
			line = sc.nextLine();
		
		line = sc.nextLine();
		line = sc.nextLine();
		lineSc = new Scanner(line);
		for (int i=0;i<m_ressource;i++)
		{
			dispoRessource[i] = lineSc.nextInt();
		}
	}


}
