package resolution;

import java.lang.reflect.Array;
import java.util.Arrays;

import instance.Instance;
import instance.Solution;

//Priorité dynamique
//On fait un tas, on retire toujours la première et on recalcule la priorité
public class Priorite {
	public int[] tab_priorite;
	public int tempsCourant;
	public Priorite(Solution solution, int[] tachestotales, int tempsCourant) throws Exception{
		tab_priorite = null;
		
	}
	public int[] getTableau(){
		return this.tab_priorite;
	}
	
	
	public int[] actualiser(Solution solution, int[] tachestotales, int tempsCourant, Instance instance, LST lst) throws Exception{
		System.err.println("Actualisation a temps "+tempsCourant);
		this.tempsCourant=tempsCourant;
		int longueur =0;
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
		System.err.println("");
			
		//On appelle une règle de priorité sur notre tableau des taches possibles.

		//On renvoit le tableau des taches possibles et ordonnancées selon la règle de priorité
			return tachesPossibles;
		
		
	}
	public int[] prioriteGlobal( int i, int[] tachespossibles, Instance instance, LST lst, Solution solution, Paires paires) throws Exception{
		if(tachespossibles.length == 0 || tachespossibles.length == 1){
				this.tab_priorite = tachespossibles;
				return this.tab_priorite;
		}
		else{
	if( i<0 || i > 10){
		System.err.println("Problème indice méthode");
		this.tab_priorite = null;
	}
	else{
		switch(i){
		case 0 : this.tab_priorite = this.prioriteGRPW(tachespossibles, instance); break;
		case 1 : this.tab_priorite = this.prioriteMTS(tachespossibles, instance); break;
		case 2 : this.tab_priorite = this.prioriteSPT(tachespossibles, instance); break;
		case 3 : this.tab_priorite = this.prioriteLST(tachespossibles, instance, lst); break;
		case 4 : this.tab_priorite = this.prioriteMSLK(tachespossibles, instance, lst); break;
		case 5 : this.tab_priorite = this.prioriteLFT(tachespossibles, instance, lst); break;
		case 6 : this.tab_priorite = this.prioriteGRPWinv(tachespossibles, instance); break;
		case 7 : this.tab_priorite = this.prioriteSPTinv(tachespossibles, instance); break;
		case 8 : this.tab_priorite = this.prioriteWCS(tachespossibles, instance, lst, paires); break;
		case 9 : this.tab_priorite = this.prioriteIRSM(tachespossibles, instance, lst, paires); break;
		case 10 : this.tab_priorite = this.prioriteRSM(tachespossibles, instance, lst, paires); break;
		default : this.tab_priorite = null; System.err.println("PB priorité");break;
		//test
		}
	}
		return this.tab_priorite;
		}
	}
	public int[] prioriteRSM(int[] tachespossibles, Instance instance, LST lst, Paires paires) throws Exception{
		System.err.println("Règle de priorité : RSM");
		 boolean tab_en_ordre = false;
		 int swap=0;
		    int taille = tachespossibles.length;
		    int f =0;
		    while(!tab_en_ordre && f < 30)
		    {
		        tab_en_ordre = true;
		        	
		        f++;
		        for(int i=0 ; i < taille-1 ; i++)
		        {
		        	
		        		
		            if(Math.max(0,  tempsCourant+instance.getDuree(tachespossibles[i])-lst.getLST(tachespossibles[i]) ) < Math.max(0, tempsCourant+instance.getDuree(tachespossibles[i])- lst.getLST(tachespossibles[i+1])))
		            {
		                swap=tachespossibles[i];
		                tachespossibles[i]=tachespossibles[i+1];
		                tachespossibles[i+1]=swap;		       
		                tab_en_ordre = false;
		                
		               // System.err.println("swap "+(instance.getDuree(tachespossibles[i]-1))+" ("+tachespossibles[i]+
		              //  		") et  "+(instance.getDuree(tachespossibles[i+1]-1))+" ("+tachespossibles[i+1]+") ");
		           
		            }
		        
		        }
		    
		    }
		   for(int j=0; j<taille; j++){
		    	System.err.print(" "+tachespossibles[j]);
		    }
		    return tachespossibles;
	
	}
	public int[] prioriteIRSM(int[] tachespossibles, Instance instance, LST lst, Paires paires) throws Exception{
		System.err.println("Règle de priorité : IRSM");
		 boolean tab_en_ordre = false;
		 int swap=0;
		    int taille = tachespossibles.length;
		    int f =0;
		    int[] max = new int[tachespossibles.length];
		    for(int k=0; k<tachespossibles.length; k++){
		       max[k] =0;
		       for(int l=0; l<tachespossibles.length; l++){
		    	   if(l != k && max[k] < paires.getE(k, l)){
		    		   max[k] = paires.getE(k, l);
		    	   }
		       }
		     }
		    while(!tab_en_ordre && f < 30)
		    {
		        tab_en_ordre = true;
		        	
		        f++;
		        for(int i=0 ; i < taille-1 ; i++)
		        {
		        	
		        		
		            if(Math.max(0,  max[i]-lst.getLST(tachespossibles[i]) ) < Math.max(0, max[i+1]- lst.getLST(tachespossibles[i+1])))
		            {
		                swap=tachespossibles[i];
		                tachespossibles[i]=tachespossibles[i+1];
		                tachespossibles[i+1]=swap;		       
		                tab_en_ordre = false;
		                
		               // System.err.println("swap "+(instance.getDuree(tachespossibles[i]-1))+" ("+tachespossibles[i]+
		              //  		") et  "+(instance.getDuree(tachespossibles[i+1]-1))+" ("+tachespossibles[i+1]+") ");
		           
		            }
		        
		        }
		    
		    }
		   for(int j=0; j<taille; j++){
		    	System.err.print(" "+tachespossibles[j]+" : wcs vaut : "+lst.getLST(tachespossibles[j])+" - "+max[j]);
		    }
		    return tachespossibles;
	
	}
	public int[] prioriteWCS(int[] tachespossibles, Instance instance, LST lst, Paires paires) throws Exception{
		System.err.println("Règle de priorité : WCS");
		 boolean tab_en_ordre = false;
		 int swap=0;
		    int taille = tachespossibles.length;
		    int f =0;
		    int[] max = new int[tachespossibles.length];
		    for(int k=0; k<tachespossibles.length; k++){
		       max[k] =0;
		       for(int l=0; l<tachespossibles.length; l++){
		    	   if(l != k && max[k] < paires.getE(k, l)){
		    		   max[k] = paires.getE(k, l);
		    	   }
		       }
		     }
		    while(!tab_en_ordre && f < 30)
		    {
		        tab_en_ordre = true;
		        	
		        f++;
		        for(int i=0 ; i < taille-1 ; i++)
		        {
		        	
		        		
		            if((lst.getLST(tachespossibles[i]) - max[i]) > (lst.getLST(tachespossibles[i+1])- max[i+1]))
		            {
		                swap=tachespossibles[i];
		                tachespossibles[i]=tachespossibles[i+1];
		                tachespossibles[i+1]=swap;		       
		                tab_en_ordre = false;
		                
		               // System.err.println("swap "+(instance.getDuree(tachespossibles[i]-1))+" ("+tachespossibles[i]+
		              //  		") et  "+(instance.getDuree(tachespossibles[i+1]-1))+" ("+tachespossibles[i+1]+") ");
		           
		            }
		        
		        }
		    
		    }
		   for(int j=0; j<taille; j++){
		    	System.err.print(" "+tachespossibles[j]+" : wcs vaut : "+lst.getLST(tachespossibles[j])+" - "+max[j]);
		    }
		    return tachespossibles;
	
	}
	public int[] prioriteLST(int[] tachespossibles, Instance instance, LST lst) throws Exception{
		System.err.println("Règle de priorité : LST");
		 boolean tab_en_ordre = false;
		 int swap=0;
		    int taille = tachespossibles.length;
		 
		    while(!tab_en_ordre)
		    {
		        tab_en_ordre = true;
		        	
		        
		        for(int i=0 ; i < taille-1 ; i++)
		        {
		            if(lst.getLST(tachespossibles[i]) > lst.getLST(tachespossibles[i+1]))
		            {
		                swap=tachespossibles[i];
		                tachespossibles[i]=tachespossibles[i+1];
		                tachespossibles[i+1]=swap;		       
		                tab_en_ordre = false;
		                
		               // System.err.println("swap "+(instance.getDuree(tachespossibles[i]-1))+" ("+tachespossibles[i]+
		              //  		") et  "+(instance.getDuree(tachespossibles[i+1]-1))+" ("+tachespossibles[i+1]+") ");
		           
		            }
		        }
		        
		    }
		   for(int j=0; j<taille; j++){
		    	System.err.print(" "+tachespossibles[j]);
		    }
		    return tachespossibles;
	}
	public int[] prioriteMSLK(int[] tachespossibles, Instance instance, LST lst) throws Exception{
		System.err.println("Règle de priorité : MSLK");
		 boolean tab_en_ordre = false;
		 int swap=0;
		    int taille = tachespossibles.length;
		 
		    while(!tab_en_ordre)
		    {
		        tab_en_ordre = true;
		        	
		        
		        for(int i=0 ; i < taille-1 ; i++)
		        {
		            if(Math.abs(lst.getLFT(tachespossibles[i])-this.tempsCourant) > Math.abs(lst.getLFT(tachespossibles[i+1])-this.tempsCourant))
		            {
		                swap=tachespossibles[i];
		                tachespossibles[i]=tachespossibles[i+1];
		                tachespossibles[i+1]=swap;		       
		                tab_en_ordre = false;
		                
		               // System.err.println("swap "+(instance.getDuree(tachespossibles[i]-1))+" ("+tachespossibles[i]+
		              //  		") et  "+(instance.getDuree(tachespossibles[i+1]-1))+" ("+tachespossibles[i+1]+") ");
		           
		            }
		        }
		        
		    }
		   for(int j=0; j<taille; j++){
		    	System.err.print(" "+tachespossibles[j]);
		    }
		    return tachespossibles;
	}
	public int[] prioriteLFT(int[] tachespossibles, Instance instance, LST lst) throws Exception{
		System.err.println("Règle de priorité : LFT");
		 boolean tab_en_ordre = false;
		 int swap=0;
		    int taille = tachespossibles.length;
		 
		    while(!tab_en_ordre)
		    {
		        tab_en_ordre = true;
		        	
		        
		        for(int i=0 ; i < taille-1 ; i++)
		        {
		            if(lst.getLFT(tachespossibles[i]) > lst.getLFT(tachespossibles[i+1]))
		            {
		                swap=tachespossibles[i];
		                tachespossibles[i]=tachespossibles[i+1];
		                tachespossibles[i+1]=swap;		       
		                tab_en_ordre = false;
		                
		               // System.err.println("swap "+(instance.getDuree(tachespossibles[i]-1))+" ("+tachespossibles[i]+
		              //  		") et  "+(instance.getDuree(tachespossibles[i+1]-1))+" ("+tachespossibles[i+1]+") ");
		           
		            }
		        }
		        
		    }
		   for(int j=0; j<taille; j++){
		    	System.err.print(" "+tachespossibles[j]);
		    }
		    return tachespossibles;
	}
	public void afficher(int[] tab){
		if(tab.length==0){
			System.err.print("vide.");
		}
		else{
			for(int i =0;i<tab.length;i++){
				System.err.print(tab[i]+" ");
			}
		}
		System.err.println("");
	}
	public int getPremiere(){
			return this.getTableau()[0];
		
	}
	public boolean predecesseurs(int tache,Instance instance, Solution solution, int t, int[] tachesTotales) throws Exception{
		int nbTaches = instance.getNbTaches();
		if(tache==1){
				return true;
			}
		for(int i = 0; i<nbTaches; i++){
			if(instance.estPredecesseurImmediat(i, tache) && tachesTotales[i]  == 0){
				return false;
			}
		}
		return true;
	}
	public boolean estVide(){
		if(this.getTableau().length == 0){
			return true;
		}
		else{
			return false;
		}
	}
	public int[] triABulle(int[] tachespossibles, Instance instance) throws Exception{
			 boolean tab_en_ordre = false;
			 int swap=0;
			    int taille = tachespossibles.length;
			 
			    while(!tab_en_ordre)
			    {
			        tab_en_ordre = true;
			        	
			        
			        for(int i=0 ; i < taille-1 ; i++)
			        {
			            if(this.regleprioSPT(tachespossibles[i], instance) < this.regleprioSPT(tachespossibles[i+1], instance))
			            {
			                swap=tachespossibles[i];
			                tachespossibles[i]=tachespossibles[i+1];
			                tachespossibles[i+1]=swap;		       
			                tab_en_ordre = false;
			                
			                System.err.println("swap "+(instance.getDuree(tachespossibles[i]-1))+" ("+tachespossibles[i]+
			                		") et  "+(instance.getDuree(tachespossibles[i+1]-1))+" ("+tachespossibles[i+1]+") ");
			           
			            }
			        }
			        
			    }
			   for(int j=0; j<taille; j++){
			    	System.err.print(" "+tachespossibles[j]);
			    }
			    return tachespossibles;
		
		
	}
	public int regleprioSPT(int tache, Instance instance) throws Exception{
		return instance.getDuree(tache-1);
	}
	public int[] prioriteSPTinv(int[] tachespossibles, Instance instance) throws Exception{
		System.err.println("Règle de priorité : SPT");
		 boolean tab_en_ordre = false;
		 int swap=0;
		    int taille = tachespossibles.length;
		 
		    while(!tab_en_ordre)
		    {
		        tab_en_ordre = true;
		        	
		        
		        for(int i=0 ; i < taille-1 ; i++)
		        {
		            if(instance.getDuree(tachespossibles[i]-1) > instance.getDuree(tachespossibles[i+1]-1))
		            {
		                swap=tachespossibles[i];
		                tachespossibles[i]=tachespossibles[i+1];
		                tachespossibles[i+1]=swap;		       
		                tab_en_ordre = false;
		                
		                System.err.println("swap "+(instance.getDuree(tachespossibles[i]-1))+" ("+tachespossibles[i]+
		                		") et  "+(instance.getDuree(tachespossibles[i+1]-1))+" ("+tachespossibles[i+1]+") ");
		           
		            }
		        }
		        
		    }
		   for(int j=0; j<taille; j++){
		    	System.err.print(" "+tachespossibles[j]);
		    }
		    return tachespossibles;
	}
	public int[] prioriteSPT(int[] tachespossibles, Instance instance) throws Exception{
		System.err.println("Règle de priorité : SPT");
		 boolean tab_en_ordre = false;
		 int swap=0;
		    int taille = tachespossibles.length;
		 
		    while(!tab_en_ordre)
		    {
		        tab_en_ordre = true;
		        	
		        
		        for(int i=0 ; i < taille-1 ; i++)
		        {
		            if(instance.getDuree(tachespossibles[i]-1) < instance.getDuree(tachespossibles[i+1]-1))
		            {
		                swap=tachespossibles[i];
		                tachespossibles[i]=tachespossibles[i+1];
		                tachespossibles[i+1]=swap;		       
		                tab_en_ordre = false;
		                
		                System.err.println("swap "+(instance.getDuree(tachespossibles[i]-1))+" ("+tachespossibles[i]+
		                		") et  "+(instance.getDuree(tachespossibles[i+1]-1))+" ("+tachespossibles[i+1]+") ");
		           
		            }
		        }
		        
		    }
		   for(int j=0; j<taille; j++){
		    	System.err.print(" "+tachespossibles[j]);
		    }
		    return tachespossibles;
	}
	public int[] prioriteGRPW(int[] tachesPossibles, Instance instance) throws Exception{
		System.err.println("Regle de priorite : GRPW");
		int[][] sommeDurees = new int[tachesPossibles.length][2];
		for(int i=0; i<tachesPossibles.length;i++){
			sommeDurees[i][1]=instance.getDuree(tachesPossibles[i]-1);
			System.err.println("Duree de "+tachesPossibles[i]+" :"+sommeDurees[i][1]);
			sommeDurees[i][0]=tachesPossibles[i];
			for(int j = 1; j<instance.getNbTaches();j++){
				if(instance.estPredecesseurImmediat(tachesPossibles[i]-1, j-1)){
					sommeDurees[i][1]=sommeDurees[i][1]+instance.getDuree(j-1);
					System.err.println("Duree j de "+j+" : "+instance.getDuree(j-1));
				}
			}
			System.err.println("SommeDurees de tache "+sommeDurees[i][0]+" : "+sommeDurees[i][1]);
		}
		boolean tab_en_ordre = false;
		 int[] swap= new int[2];
		    int taille = tachesPossibles.length;
		    while(!tab_en_ordre)
		    {
		        tab_en_ordre = true;
		        for(int i=0 ; i < taille-1 ; i++)
		        {
		            if(sommeDurees[i][1] <sommeDurees[i+1][1])
		            {
		                swap=sommeDurees[i];
		                sommeDurees[i]=sommeDurees[i+1];
		                sommeDurees[i+1]=swap;		       
		                tab_en_ordre = false;
		            }
		        }
		        //taille--;
		    }
		    int[] retour = new int[tachesPossibles.length];
		    for(int i=0;i <tachesPossibles.length;i++){
		    	retour[i]=sommeDurees[i][0];
		    }
		return retour;
		
	}
	public int[] prioriteGRPWinv(int[] tachesPossibles, Instance instance) throws Exception{
		int[][] sommeDurees = new int[tachesPossibles.length][2];
		for(int i=0; i<tachesPossibles.length;i++){
			sommeDurees[i][1]=instance.getDuree(tachesPossibles[i]-1);
			System.err.println("Duree de "+tachesPossibles[i]+" :"+sommeDurees[i][1]);
			sommeDurees[i][0]=tachesPossibles[i];
			for(int j = 1; j<instance.getNbTaches();j++){
				if(instance.estPredecesseurImmediat(tachesPossibles[i]-1, j-1)){
					sommeDurees[i][1]=sommeDurees[i][1]+instance.getDuree(j-1);
					System.err.println("Duree j de "+j+" : "+instance.getDuree(j-1));
				}
			}
			System.err.println("SommeDurees de tache "+sommeDurees[i][0]+" : "+sommeDurees[i][1]);
		}
		boolean tab_en_ordre = false;
		 int[] swap= new int[2];
		    int taille = tachesPossibles.length;
		    while(!tab_en_ordre)
		    {
		        tab_en_ordre = true;
		        for(int i=0 ; i < taille-1 ; i++)
		        {
		            if(sommeDurees[i][1] >sommeDurees[i+1][1])
		            {
		                swap=sommeDurees[i];
		                sommeDurees[i]=sommeDurees[i+1];
		                sommeDurees[i+1]=swap;		       
		                tab_en_ordre = false;
		            }
		        }
		        //taille--;
		    }
		    int[] retour = new int[tachesPossibles.length];
		    for(int i=0;i <tachesPossibles.length;i++){
		    	retour[i]=sommeDurees[i][0];
		    }
		return retour;
		
	}
	public int [] reverse(int[] tab){
		int[] var = new int[tab.length];
		
		for(int i=0; i<tab.length; i++){
			int l= tab.length-1-i;
			var[i]=tab[l];
		}
		return var;
	}
	public int[] prioriteMTS(int[] tachesPossibles, Instance instance) throws Exception{
		int[][] nbSuccess = new int[tachesPossibles.length][2];
		for(int i=0; i<tachesPossibles.length;i++){
			nbSuccess[i][1]=0;
			nbSuccess[i][0]=tachesPossibles[i];
			for(int j = 1; j<instance.getNbTaches();j++){
				if(instance.estPredecesseur(nbSuccess[i][0]-1, j-1)){
					nbSuccess[i][1]=nbSuccess[i][1]+1;
					System.err.println(j+" successeur de "+nbSuccess[i][0]);
				}
			}
		}
		boolean tab_en_ordre = false;
		 int[] swap= new int[2];
		    int taille = tachesPossibles.length;
		    while(!tab_en_ordre)
		    {
		        tab_en_ordre = true;
		        for(int i=0 ; i < taille-1 ; i++)
		        {
		            if(nbSuccess[i][1] <nbSuccess[i+1][1])
		            {
		                swap=nbSuccess[i];
		                nbSuccess[i]=nbSuccess[i+1];
		                nbSuccess[i+1]=swap;		       
		                tab_en_ordre = false;
		            }
		        }
		        //taille--;
		    }
		    int[] retour = new int [tachesPossibles.length];
		    System.err.print("Triées :");
		    for(int i=0;i <tachesPossibles.length;i++){
		    	retour[i]=nbSuccess[i][0];
		    	
		    	System.err.print(" "+retour[i]);
		    }
		return retour;
	}
	
}
