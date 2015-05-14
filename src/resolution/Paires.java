package resolution;

import instance.Instance;
import instance.Solution;

public class Paires {
	public int[][] APN;
	public int[][] GFPN;
	public int[][] TFPN;
	public int[][] CSPN;
	public int[][][] earliestTime;
	public int tempsCourant;

	public Paires(Instance instance, int nbTaches) throws Exception{
	/*	this.APN = new int[factorielle(nbTaches)][2];
		this.GFPN = new int[nbTaches][2];
		this.TFPN = new int[nbTaches][2];
		this.CSPN = new int[nbTaches][2];*/

		//this.earliestTime= new int[nbTaches][nbTaches][3];
		//[0] : vaut 0 si GFPN, 1 si TFPN, 2 si CSPN
		//[1] : vaut earliestTime PI(i,j)
		//[2] : vaut E(i,j)
		
		
/*		this.GFPN = new int[tachesPossibles.length][2];
		this.TFPN = new int[tachesPossibles.length][2];
		this.CSPN = new int[tachesPossibles.length][2];*/
		/*
		for(int i=0; i<nbTaches;i++){
			for(int j=0; j<nbTaches;j++){
				//this.earliestTime[i][j][1]=1000;
				if(i == j){
					this.earliestTime[i][j][0]=0;
					this.earliestTime[i][j][1]=1000;
				}
				else{
					boolean pasPossible = false;
					for(int l=0; l< instance.getNbRessources(); l++){
						if(instance.getRessourcePourJob(l, i)+instance.getRessourcePourJob(l, j) > instance.getDispoRessource(l)){
							pasPossible = true;
						}
					}
					if(pasPossible){
						this.earliestTime[i][j][0]=0;
						this.earliestTime[i][j][1]=1000;
					}
				}
			}
		}*/
		
		/*this.APN = new int[nbTaches*(nbTaches-1)/2][3];
				int nb=0;
		for(int i = 0; i < nbTaches-1; i++){
			for(int j=i+1; j < nbTaches; j++){
					this.APN[nb][0] = i;
					this.APN[nb][1] = j;
					//System.err.println("nb :"+nb);
					nb++;
			}	
		}
		
		int longueur5=0;
		boolean pasPossible = false;
		for(int i=0; i<nbTaches-1; i++){
			for(int j=i+1; j < nbTaches; j++){
				for(int l=0; l< instance.getNbRessources(); l++){
					if(instance.getRessourcePourJob(l, i)+instance.getRessourcePourJob(l, j) > instance.getDispoRessource(l)){
					pasPossible = true;
				}
				
				}
				if(pasPossible){
					longueur5++;
					
				}
			}
		}
		pasPossible = false;
		int k =0;
		this.GFPN = new int[longueur5][3];
		for(int i=0; i<nbTaches-1; i++){
			for(int j=i+1; j < nbTaches; j++){
				for(int l=0; l< instance.getNbRessources(); l++){
					if(instance.getRessourcePourJob(l, i)+instance.getRessourcePourJob(l, j) > instance.getDispoRessource(l)){
					pasPossible = true;
					
					this.GFPN[k][0]=i;
					this.GFPN[k][1]=j;
					this.GFPN[k][2]=1000;
					k++;
					}
				}
			}
		}
		*/
		

		
		
	}
	public int factorielle(int nbTaches) {
		if(nbTaches==1) return 1;
		else return factorielle(nbTaches-1)*nbTaches;
	}
	public void actualiser(Instance instance, int[] tachestotales, int tempsCourant, Solution solution, int[] tachesPossibles) throws Exception{
		this.tempsCourant=tempsCourant;
		/*int longueur =0;
		System.err.println("Longueur de tachestotales : "+tachestotales.length);
		System.err.print(" Taches totales restantes : ");
		for(int i=0; i<tachestotales.length; i++){
			if(tachestotales[i] !=0){
				longueur = longueur +1;
			}

			System.err.print(" "+tachestotales[i]);
		}
		int[] tachesDispo = new int[longueur];
		int k =0;
		for(int i=0;i<tachestotales.length; i++){
			if(tachestotales[i] !=0){
				tachesDispo[k]=tachestotales[i];
				k++;
			}

		}
		//On a le tableau tachesDispos de toutes les taches non deja ordonnancées

		System.err.println("");
		System.err.println("longueur (Longueur de TachesDispo) : "+longueur);
		System.err.print("Taches dispo : ");
		//for(int i=0; i<tachesDispo.length; i++){



		//on a un tableau avec toutes les taches qui n'ont pas encore été ordonnancées


		//On cherche la longueur du tableau contenant toutes les taches qu'on peut ajouter à ce moment 
		int longueur2 =0;
		for(int i = 0;i<longueur;i++){
			System.err.print(" "+tachesDispo[i]);//Juste pour afficher les taches dispo
			boolean possible = true;
			if(solution.ajoutPossible(tachesDispo[i], tempsCourant)){
				for(int j=0; j<tachestotales.length;j++){
					if(instance.estPredecesseurImmediat(j, tachesDispo[i]) && tachestotales[j] != 0){
						possible = false;
					}
				}
			}
			else{
				possible = false;
			}

			if(possible == true)	longueur2 = longueur2+1;
		}	
		//On a la longueur du tableau, on le crée et on place les taches qu'on peut ajouter(possible)
		System.err.println("");
		System.err.println("longueur 2 (Longueur de tachesPossibles) : "+longueur2);
		System.err.print("Taches Possibles :");	
		int[] tachesPossibles = new int[longueur2];
		int l = 0;
		for(int i = 0;i<longueur;i++){
			boolean possible = true;
			if(solution.ajoutPossible(tachesDispo[i], tempsCourant)){
				for(int j=0; j<tachestotales.length;j++){
					if(instance.estPredecesseurImmediat(j, tachesDispo[i]) && tachestotales[j] != 0){
						possible = false;
					}

				}
			}
			else{
				possible = false;
			}
			if(possible == true){
				tachesPossibles[l] = tachesDispo[i];

				System.err.print(" "+tachesPossibles[l]);
				l++;
			}

		}
		*/
		//Jusqu'ici tout va bien
		int[] tachesPossibles2 = tachesPossibles;
		//tachesPossibles = tachesDispo;
		this.earliestTime= new int[tachesPossibles.length][tachesPossibles.length][3];
		Solution clone = solution.clone();
		for(int i=0; i<tachesPossibles.length; i++){
			for(int j=0; j<tachesPossibles.length; j++){
				if(i == j){
					this.earliestTime[i][j][0]=0;
					
				}
				this.earliestTime[i][j][1]=1000;
				boolean pasPossible = false;
				for(int l=0; l< instance.getNbRessources(); l++){
					if(instance.getRessourcePourJob(l, tachesPossibles[i])+instance.getRessourcePourJob(l, tachesPossibles[j]) > instance.getDispoRessource(l)){							
						pasPossible = true;
					}
				}
				if(pasPossible){
					this.earliestTime[i][j][0]=0;
					this.earliestTime[i][j][1]=1000;
				}
				//on a déjà les GFPN
				clone.ajouterTache(tachesPossibles[i], tempsCourant);
				if(solution.ajoutPossible(tachesPossibles[j], tempsCourant)== false && this.earliestTime[i][j][0] != 0){
					this.earliestTime[i][j][0]=1;
						
				}
				clone.retirerTache(tachesPossibles[i]);
				if(this.earliestTime[i][j][0] ==1){
					int[] eTRessource = new int[instance.getNbRessources()];
						for(int l=0; l<instance.getNbRessources(); l++){
							int t = tempsCourant;
							while(instance.getRessourcePourJob(l,tachesPossibles[i])+
									instance.getRessourcePourJob(l, tachesPossibles[j]) > instance.getDispoRessource(l)-solution.getUtilisation(l, t) && t < tempsCourant + 15){
									t++;
							}
							if(t >= tempsCourant+15)		System.err.println("Problème calcul earliestTime");
							eTRessource[l] =t;
						}
						int max =0;
						for(int k=0; k<instance.getNbRessources(); k++){
							if(eTRessource[k] > max){
								max = eTRessource[k];
						}
						this.earliestTime[i][j][1]= max;
						
				}
				}
				//On a les TFPN et leurs earliest Time
				if(this.earliestTime[i][j][0] !=1 && this.earliestTime[i][j][0] !=0){
					clone.ajouterTache(tachesPossibles[i], tempsCourant);
					if(solution.ajoutPossible(tachesPossibles[j], tempsCourant)){
						this.earliestTime[i][j][0]=1;
						this.earliestTime[i][j][1]=tempsCourant;		
					}
					clone.retirerTache(tachesPossibles[i]);
				}
			}
		}
		// ON a les PI(i,j)
		System.err.println("");
		for(int i=0; i<tachesPossibles.length; i++){
			for(int j=0; j<tachesPossibles.length; j++){
				if(i !=j){
					this.earliestTime[i][j][2]=Math.min(tempsCourant+instance.getDuree(tachesPossibles[i]), this.earliestTime[i][j][1]);
				}
				else{
					this.earliestTime[i][j][2]=1000;
				}
				System.err.print(" "+this.earliestTime[i][j][2]);
			}
			System.err.println("");
		}
		//On a les E(i,j)
		
		
		
		
		
		
		
		
		
		
	/*	int longueur3=0;
		for(int i=0; i<tachesPossibles.length-1; i++){
			solution.ajouterTache(tachesPossibles[i], tempsCourant);
			for(int j=i+1; j < tachesPossibles.length; j++){
				if(solution.ajoutPossible(tachesPossibles[j], tempsCourant)== false){
					longueur3++;
				}
			}
			solution.retirerTache(tachesPossibles[i]);
		}
		this.TFPN = new int[longueur3][3];
		int m=0;
		System.err.println("TFPN");
		for(int i=0; i<tachesPossibles.length-1; i++){
			solution.ajouterTache(tachesPossibles[i], tempsCourant);
			for(int j=i+1; j < tachesPossibles.length; j++){
				if(solution.ajoutPossible(tachesPossibles[j], tempsCourant)== false){
					this.TFPN[m][0] = tachesPossibles[i];
					this.TFPN[m][1] = tachesPossibles[j];
					System.err.println(" "+this.TFPN[m][0]);
					System.err.print(" "+this.TFPN[m][1]);
					m++;
				}
			}
			solution.retirerTache(tachesPossibles[i]);
		}
		
		
		int longueur4=0;
		for(int i=0; i<tachesPossibles.length-1; i++){
			solution.ajouterTache(tachesPossibles[i], tempsCourant);
			for(int j=i+1; j < tachesPossibles.length; j++){
				if(solution.ajoutPossible(tachesPossibles[j], tempsCourant)){
					longueur4++;
					
				}
			}
			solution.retirerTache(tachesPossibles[i]);
		}
		this.CSPN = new int[longueur4][3];
		m=0;
		for(int i=0; i<tachesPossibles.length-1; i++){
			solution.ajouterTache(tachesPossibles[i], tempsCourant);
			for(int j=i+1; j < tachesPossibles.length; j++){
				if( i != j && solution.ajoutPossible(tachesPossibles[j], tempsCourant)){
					this.CSPN[m][0] = tachesPossibles[i];
					this.CSPN[m][1] = tachesPossibles[j];
					this.CSPN[m][2] = tempsCourant;
					System.err.println("");
					solution.retirerTache(tachesPossibles[i]);
					System.err.println("CSPN "+this.CSPN[m][0]+" et "+this.CSPN[m][1]+" compatibles");
					System.err.println("Ressources 1 : "+solution.getUtilisation(0, tempsCourant)+
							"Ressources 2 : "+solution.getUtilisation(1, tempsCourant)+
							"Ressources 3 : "+solution.getUtilisation(2, tempsCourant)+
							"Ressources 4 : "+solution.getUtilisation(3, tempsCourant));
					solution.ajouterTache(tachesPossibles[i], tempsCourant);
					m++;
				}
			}
			solution.retirerTache(tachesPossibles[i]);
		}
		int t = tempsCourant;
		int[] earliestTime = new int[longueur3];
		int[] eTRessource = new int[instance.getNbRessources()];
		for(int i=0; i<longueur3;i++){
			for(int j=0; j<instance.getNbRessources(); j++){
				while(instance.getRessourcePourJob(j,this.TFPN[i][1])+
						instance.getRessourcePourJob(j, this.TFPN[i][0]) > instance.getDispoRessource(j)-solution.getUtilisation(j, t) && t < tempsCourant + 15){
						t++;
				}
				if(t >= tempsCourant+15)		System.err.println("Problème calcul earliestTime");
				eTRessource[j] =t;
			}
			
			this.TFPN[i][2] = Math.min(Math.max(eTRessource[0],eTRessource[1]), Math.max(eTRessource[2], eTRessource[3]));
		}*/

	}
	public int getE(int i, int j){
		return this.earliestTime[i][j][2];
	}

}
