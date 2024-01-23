package execution.metier;

import execution.metier.cartes.Carte;
import execution.metier.cartes.CarteBleu;
import execution.metier.cartes.CarteRouge;
import execution.metier.cartes.CarteVerte;
import execution.metier.cartes.CarteViolette;
import execution.metier.cartes.CarteMonument;

import java.util.ArrayList;
import java.util.Collections;

public class Joueur
{
	private static int nbJoueur;

	private ArrayList<CarteMonument> monument;
	private ArrayList<Carte>         main;
	private String                   nom;
	private int                      nbPiece;
	private int                      numJoueur;

	public Joueur( String nom )
	{
		this.numJoueur = ++ nbJoueur;
		this.monument  = new ArrayList<CarteMonument>();
		this.main      = new ArrayList<Carte>();
		this.nom       = nom;
		this.nbPiece   = 30;

		initialisationCartes();
	}

	public boolean equals( Joueur j )
	{
		return j.numJoueur == this.numJoueur;
	}

	public int getNumJoueur()
	{
		return this.numJoueur;
	}

	public int getNbPiece()
	{
		return this.nbPiece;
	}

	public void setNbPiece( int nbPiece )
	{
		this.nbPiece = nbPiece;
	}

	public String getNom()
	{
		return this.nom;
	}

	public ArrayList<Carte> getMain()
	{
		ArrayList<Carte> tmp = new ArrayList<Carte>();
		for ( Carte c : this.main )
			tmp.add( c );

		return tmp;
	}

	public ArrayList<CarteMonument> getMonument()
	{
		ArrayList<CarteMonument> tmp = new ArrayList<CarteMonument>();
		for ( CarteMonument c : this.monument )
			tmp.add( c );

		return tmp;	
	}

	public CarteMonument getCarteMonument( String nomCarte )
	{
		for( CarteMonument carte : this.monument )
			if( carte.getNomCarte().equals( nomCarte ) )
				return carte;

		return null;
		
	}

	public void initialisationCartes()
	{
		this.main.add( new CarteBleu ( "1"  , "Champs de blé", "blé"     , "Recevez 1 pièce de la banque.", 0 ) );
		this.main.add( new CarteVerte( "2~3", "Boulangerie"  , "commerce", "Recevez 1 pièce de la banque.", 0 ) );
	
		this.monument.add( new CarteMonument( null, "Gare"              , "tour", "Vous pouvez lancer 2 dés."                                                       ,  4, false ) );
		this.monument.add( new CarteMonument( null, "Centre commercial" , "tour", "Vos établissements de type \"café\" et \"commerce\" rapportent une pièce de plus", 10, false ) );
		this.monument.add( new CarteMonument( null, "Parc d'attractions", "tour", "Si votre jet de dés est un double, rejouez un tour après celui-ci."              , 16, false ) );
		this.monument.add( new CarteMonument( null, "Tour radio"        , "tour", "Une fois par tour, vous pouvez relancer vos dés."                                , 22, false ) );
	}

	public void ajouterCarte( Carte c, boolean initPartie )
	{
		this.main.add( c );
		if ( !initPartie )
			this.nbPiece -= c.getCout();
		Collections.sort( this.main );
	}

	public void revenus( int numDe, int numJoueur, ArrayList<Joueur> alJoueur )
	{
		if ( this.numJoueur == numJoueur )
			effetRouge( numDe, alJoueur );

		for ( Carte carte : this.main )
			for ( char cara : carte.getNumCarte().toCharArray() )
			{
				if ( numDe == (int) (cara - '0') )
				{
					if ( carte instanceof CarteVerte && numJoueur == this.numJoueur )
						effetVert( (CarteVerte) carte );
						
					if ( carte instanceof CarteBleu )
						effetBleu( (CarteBleu) carte );
					if ( carte instanceof CarteViolette && numJoueur == this.numJoueur )
						effetViolet( numDe, (CarteViolette) carte, alJoueur );
					
				}
			}
	}

	public void effetVert( CarteVerte carte )
	{
		if ( carte.getCondition() == null )
		{
			this.nbPiece += carte.getNbPiece();
			if ( this.getCarteMonument("Centre commercial").getActive() )
				this.nbPiece += 1;

		}
		else
		{
			String symbole = carte.getCondition();
			int    cpt     = 0;
			for ( Carte c : this.main )
				if ( c.getSymbole().equals( symbole ) )
					cpt++;

			this.nbPiece += carte.getNbPiece() * cpt;
			if ( this.getCarteMonument("Centre commercial").getActive() )
				this.nbPiece += 1;
		}
	}

	public void effetBleu( CarteBleu carte )
	{
		this.nbPiece += carte.getNbPiece();
	}

	public void effetRouge( int tirage, ArrayList<Joueur> alJoueur )
	{
		int cpt;
		int cptMonument = 0;
		int nbPieceDecrementer = 0;

		if ( numJoueur+1 > alJoueur.size() )
			cpt = 1;
		else
			cpt = numJoueur + 1;

		while ( numJoueur != alJoueur.get(cpt-1).numJoueur )
		{
			for ( Carte carte : alJoueur.get(cpt-1).main )
				for ( char cara : carte.getNumCarte().toCharArray() )
				{
					if ( tirage == (int) (cara - '0') )
					{
						if ( carte instanceof CarteRouge )
						{
							if ( alJoueur.get(cpt-1).getCarteMonument("Centre commercial").getActive() )
							{
								cptMonument ++;
								System.out.println( "autre." + alJoueur.get(cpt-1).nom + " --> " + "CarteMonument centre commercial active" );
							}

							CarteRouge carteTmp = (CarteRouge) carte;
							nbPieceDecrementer = carteTmp.getNbPiece() + cptMonument;

							// !bEssai
							if ( this.nbPiece - nbPieceDecrementer < 0 )
							{
								alJoueur.get(cpt-1).nbPiece += this.nbPiece;
								this.nbPiece = 0;
							}
							else
							{
								alJoueur.get(cpt-1).nbPiece += nbPieceDecrementer;
								this.nbPiece -= nbPieceDecrementer;
							}
						}
					}
				}
			if ( cpt == alJoueur.size() )
				cpt = 1;
			else
				cpt++;
		}
	}

	public void effetViolet( int tirage, CarteViolette carteV, ArrayList<Joueur> alJoueur )
	{

		int cpt;
		int cptMonument = 0;
		int nbPieceDecrementer = 0;

		if ( numJoueur+1 > alJoueur.size() )
			cpt = 1;
		else
			cpt = numJoueur + 1;

		if( carteV.getNomCarte().equals("Stade"))
		{
			while ( numJoueur != alJoueur.get(cpt-1).numJoueur )
			{
				for ( Carte carte : this.main )
					for ( char cara : carte.getNumCarte().toCharArray() )
					{
						if ( tirage == 6 )
						{
							if ( carte instanceof CarteViolette )
							{
								CarteViolette carteTmp = (CarteViolette) carte;
								nbPieceDecrementer = carteTmp.getNbPiece();
								
								if ( alJoueur.get(cpt-1).getNbPiece() >= 2 )
								{
									this.nbPiece += 2;
									alJoueur.get(cpt-1).nbPiece -= 2;
								}
								else
								{
									this.nbPiece += alJoueur.get(cpt-1).nbPiece;
									alJoueur.get(cpt-1).nbPiece -= alJoueur.get(cpt-1).nbPiece;
								}
								
							}
						}
					}
				if ( cpt == alJoueur.size() )
					cpt = 1;
				else
					cpt++;
			}
		}	
	}

	public boolean bVictoire()
	{
		for ( CarteMonument cMonument : this.monument )
			if ( !cMonument.getActive() )
				return false;
		return true;
	}

}
