package resolution;
import instance.Instance;
import instance.Solution;

public class LST {
	
	public int finMin;
	public int[] m_solution;
	public int[] est;
	public int[] lst;
	public int[] lft;
	
	public LST(Instance m_instance, Solution m_solution) throws Exception{
		int nbTaches = m_instance.getNbTaches();
		this.est = new int[nbTaches];
		this.lst = new int[nbTaches];
		this.lft = new int[nbTaches];
		this.m_solution = new int[nbTaches];
		for(int i= 0; i< nbTaches; i++){
			this.est[i] = 0;
			
		}
		this.EST(0, 0, m_instance, nbTaches);
		System.err.println(" Fin Min : "+this.getFinMin());
		for(int i = 0; i<nbTaches; i++){
			this.lst[i]= this.getFinMin();
			this.lft[i]= this.lst[i]+m_instance.getDuree(i);
		}
		this.LatestST(nbTaches-1,this.getFinMin(), m_instance, nbTaches);
		
		
		/*Solution clone= m_solution.clone();
		int temps = 0;
		this.m_solution = new int[nbTaches];
		for(int i = 0; i < nbTaches; i++){
			this.m_solution[i]=-1;
		}
		TachesTotales tachestotales1 = new TachesTotales(nbTaches);
		clone.ajouterTache(0, temps);
		tachestotales1.retirerTaches(0);
		while(tachestotales1.estVide() == false){
			System.err.println("nbtache : "+nbTaches);
			
			boolean rien = true;
			for(int i =1; i <nbTaches; i++){
				
				if(this.m_solution[i] == -1 && this.possible(m_instance, i, temps, m_solution, tachestotales1)){
					clone.ajouterTache(i, temps);
					this.m_solution[i] = temps;
					//System.err.println("Tache "+priorite.getPremiere()+"au temps "+tempsCourant+" de durée "+this.getInstance().getDuree(priorite.getPremiere()));
					rien= false;
					tachestotales1.retirerTaches(i);
				}
			
			this.finMin = temps;
			}
			System.err.println("temps : "+temps);
			if(rien){
				temps++;
			}
			for(int k = 0; k < nbTaches; k++){
			//	System.err.print(" "+this.m_solution[k]);
			}
			System.err.println("");
		}
		*/
		
	}
	public int getFinMin(){
		return this.finMin;
	}
	public int getLST(int i){
		return this.lst[i];
	}
	public int getEST(int i){
		return this.est[i];
	}
	public int getLFT(int i){
		return this.lft[i];
	}
	public void EST(int i, int est, Instance instance, int nbTaches) throws Exception{
		if(this.est[i] < est){
			this.est[i] = est;
		}
		
		if(i == nbTaches-1 && this.finMin < est + instance.getDuree(i)){
			this.finMin = est + instance.getDuree(i);
		}
		else{
			for(int j = 1; j < nbTaches; j++){
				if(instance.estPredecesseurImmediat(i, j)){
					this.EST(j, est+instance.getDuree(i), instance, nbTaches);
				}
			}
			
			
		}
		
	}
	public void LatestST(int i, int lst, Instance instance, int nbTaches) throws Exception{
		if(this.lst[i] > lst-instance.getDuree(i)){
			this.lst[i]=lst-instance.getDuree(i);
		}
		if(i==0){
		}
		else{
			for(int j=0; j<nbTaches; j++){
				if(instance.estPredecesseurImmediat(j, i)){
					this.LatestST(j, lst-instance.getDuree(i), instance, nbTaches);
				}
			}
		}
		
		
		
		
	}
	public boolean possible(Instance m_instance, int j, int date, Solution m_solution, TachesTotales tachestotales1) throws Exception{
		int nbTaches = m_instance.getNbTaches();
		if ((j < 0) || (j > nbTaches))
			throw new Exception("Erreur: " + j + " n\'est pas un numéro de tâche compris entre 0 et " + (nbTaches - 1));
		// Vérification des contraintes de précédence
		if(this.m_solution[j] != -1){
			System.err.println(j+" : Déja supprimée");
			return false;
			
		}
		for (int i=0;i<nbTaches;i++){
			// Si i est un prédécesseur de j, il doit être ordonnancé avant
			if (m_instance.estPredecesseur(i, j)){
				if (this.m_solution[i] != -1){
					if (this.m_solution[i]+m_instance.getDuree(i)>date)
						return false;
				}
			}
			// Si i est un successeur de j, il doit être ordonnancé après
			if (m_instance.estPredecesseur(j,i)){
				if (this.m_solution[i]!= -1){
					if (this.m_solution[i]<m_instance.getDuree(j)+date)
						return false;
				}
			}
				if(m_instance.estPredecesseurImmediat(i, j) && this.m_solution[i] != -1){
					return false;
				}

		}

		return true;
		
	}

}
