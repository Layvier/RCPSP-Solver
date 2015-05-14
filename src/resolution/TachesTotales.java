package resolution;
//Liste de toutes les taches
//On retire la tache à chaque fois qu'on la place
public class TachesTotales {
	public int[] tableau;
	
	public TachesTotales(int nbTaches){
		this.tableau = new int[nbTaches];
		for(int i =0; i<nbTaches; i++){
			this.tableau[i]=i;
		}
	}
	public int[] getTableau(){
		return this.tableau;
	}
	public boolean estVide(){
		boolean B=true;
		for(int i=0; i<this.getTableau().length;i++){
			if (this.getTableau()[i] !=0){
				B=false;
			}		
		}
	return B;
	}
	
	public void retirerTaches(int tache){
		if(this.getTableau()[tache] !=0){
			this.tableau[tache]=0;
		}
		else{
			System.err.println("Tache "+tache+" a déjà été supprimé");
		}
	}
}
