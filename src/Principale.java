import ihm.MainFrame;
import instance.Instance;
import instance.Solution;

import java.io.IOException;
import resolution.RCPSPSolver;

/**
 * La classe <code>Principale</code> est le point d'entrée dans le programme (contient la méthode static main(String[] args)).
 * <br> 
 * Elle lance la lecture du fichier de données (instance du RCPSP),
 * lance la méthode de résolution à développer depuis la classe <code>RCPSPSolver</code>
 * et vérifie la solution retournée.
 * 
 * <br> IL NE FAUT PAS MODIFIER CETTE CLASSE. Vous devez modifier la classe
 * <code>RCPSPSolver</code> (méthode solve()) et, si besoin, créer vos propres classes.
 * 
 * <br>
 * L'appel du programme est néanmoins géré depuis cette classe. Pour exécuter le programme en ligne de commande 
 * vous devez saisir la commande <code>java Principale [options] datafile</code>.
 * 
 * <br> Les options sont les suivantes:
 * <li> -help     : imprime l'aide 
 * <li> -t (int)  : temps maximum alloué à la résolution (en secondes)
 * <li> -g        : sortie graphique 
 * 
 * @author Damien Prot 2014
 * 
 */
public class Principale {

	/**
	 * Méthode principale. NE PAS MODIFIER CETTE METHODE. Les modifications au
	 * programme sont à faire dans la méthode solve de la classe
	 * <code>RCPSPSolver</code> et, si besoin, créez vos propres classes.
	 * 
	 * @param arg paramètres du programme.
	 */
	public static void main(String[] arg) {
		String filename = "instances/j1201_1.sm";
		long max_time = 60;
		boolean graphical = false;
		// --- parse command-line ---
		for (int i = 0; i < arg.length; i++) {
			if (arg[i].compareTo("-help") == 0) {
				System.err.println("Introduction aux techniques décisionnelles: cas d'application");
				System.err.println("Le Problème du Sac-à-Dos Multidimensionnel");
				System.err.println("Utilisation de l'API :");
				System.err.println("commande: java Principale [options] datafile");
				System.err.println("Options:");
				System.err.println("  -help     : imprime cette aide");
				System.err.println("  -t (int)  : temps maximum alloué à la résolution (en secondes)");
				System.err.println("  -g        : sortie graphique");


			} else if (arg[i].compareTo("-g") == 0) {
				graphical = true;
			} else if (arg[i].compareTo("-t") == 0) {
				try {
					max_time = Integer.parseInt(arg[++i]);
				} catch (Exception e) {
					System.out.println("erreur : temps invalide pour l'option -t");
					System.exit(1);
				}
			} else {
				if (filename != null) {
					System.err.println("erreur : paramètre inconnu ou trop de fichiers de données");
					System.err.println("Paramètre :" + arg[i]);
					System.exit(1);
				}
				filename = arg[i];
			}
		}
		if ((filename == null) || (filename.equals("")) ) {
			System.err.println("Erreur : vous devez saisir un nom de fichier d'instance en paramètre du programme.");
			System.exit(1);
		}

		// --- create and solve problems ---
		try {
			RCPSPSolver rcpsp = new RCPSPSolver();
			
			// create a new problem; data is read from file filename
			Instance prob = new Instance(filename);
			rcpsp.setInstance(prob);
			rcpsp.setSolution(new Solution(prob));

			// solve the problem
			long t = System.currentTimeMillis();
			rcpsp.solve(max_time);
			t = System.currentTimeMillis() - t;

			// evaluate the solution (and check whether it is feasible)
			boolean feasible = rcpsp.getSolution().validate();
			int obj = rcpsp.getSolution().getObjectif();


			// sortie du programme: fileName;routeLength;t;e
			// e est un code d'erreur :
			// e = 0 -> solution réalisable dans les temps
			// e = 1 -> solution non réalisable
			// e= 2 -> temps dépassé
			int e = 0;
			if (!feasible)
				e = 1;
			else {
				if (t > (max_time + 1) * 1000) {
					e = 2;
					System.err.println("Temps dépassé !!!");
				}
			}

			// if error, print the error
			if (e == 1)
				System.err.println("Erreur dans la solution: " + rcpsp.getSolution().getError());

			System.out.println(filename + ";" + obj + ";" + t + ";" + e);
			// Si visu, affichage des valeurs des contraintes
			if (graphical) {
				new MainFrame(rcpsp.getInstance(), rcpsp.getSolution());

			}
		} catch (IOException e) {
			System.err.println("Erreur dans la lecture du fichier: " + e.getMessage());
			System.exit(1);
		} catch (Exception e) {
			System.err.printf("error: %s", e.getMessage());
			System.err.println();
			e.printStackTrace(System.err);
			System.exit(1);
		}
		return;
	}


}
