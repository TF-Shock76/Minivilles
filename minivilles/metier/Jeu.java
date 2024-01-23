package execution.metier;

import execution.Controleur;

import execution.metier.cartes.Carte;
import execution.metier.cartes.CarteBleu;
import execution.metier.cartes.CarteRouge;
import execution.metier.cartes.CarteVerte;
import execution.metier.cartes.CarteViolette;
import execution.metier.cartes.CarteMonument;

import iut.algo.Decomposeur;

import java.util.Scanner;
import java.util.ArrayList;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileInputStream;



public class Jeu
{
	private Controleur        ctrl;
	private int               tirage2;
	private Pioche            pioche;
	private ArrayList<Joueur> alJoueur;


	public Jeu( Controleur ctrl )
	{
		this.ctrl      = ctrl;
		this.pioche    = new Pioche();
		this.alJoueur  = new ArrayList<Joueur>();
	}

	public void creerJoueur( int nbJoueur, String[] nom )
	{
		for ( int cpt=0; cpt<nbJoueur; cpt++ )
			alJoueur.add( new Joueur( nom[cpt] ) );
	}

	public int tirage()
	{
		return (int) ((Math.random()*6)+1);
	}

	public ArrayList<Carte> getPioche()
	{
		return this.pioche.getAlCartes();
	}

	public ArrayList<Joueur> getJoueur()
	{
		ArrayList<Joueur> tmp = new ArrayList<Joueur>();
		for ( Joueur j : alJoueur )
			tmp.add( j );

		return tmp;
	}

	public void carteAchetee( Carte c, int numJoueur )
	{
		this.pioche.defausse( c );
		this.alJoueur.get( numJoueur-1 ).ajouterCarte( c, false );
	}

	public void revenus( int numJoueur, int numDe )
	{
		for ( Joueur j : alJoueur )
			j.revenus( numDe, numJoueur, this.alJoueur );
	}

	public void initialiserPartie( String sauvegarde)
	{
		Scanner sc;

		try
		{
			Joueur joueur = null;
			sc = new Scanner( new FileInputStream( "./sauvegarde/" + sauvegarde + ".txt" ), "UTF-8" );
			while ( sc.hasNextLine() )
			{
				String ligne  = sc.nextLine();

				if ( ligne.length() > 0 && ligne.charAt(0) == 'J' )
				{
					Decomposeur dec = new Decomposeur( ligne );
					joueur = new Joueur( dec.getString(1) );
					alJoueur.add( joueur );
				}

				if ( ligne.length() > 0 && ligne.charAt(0) == 'P' && joueur != null )
				{
					Decomposeur dec = new Decomposeur( ligne );
						joueur.setNbPiece( dec.getInt(1) );
				}

				if ( ligne.length() > 0 && ligne.charAt(0) == 'C' && joueur != null )
				{
					String carte=" ";
					while ( carte.length()>0 && sc.hasNextLine())
					{
						carte = sc.nextLine();
						if ( carte.length() > 0 )
						{
							Decomposeur dec = new Decomposeur( carte );
							for ( int cpt=0; cpt < dec.getInt(0); cpt++ )
							{
								Carte carteTmp = pioche.getCarte( dec.getString(1) );
								if ( carteTmp != null )
								{
									joueur.ajouterCarte( carteTmp, true );
									pioche.defausse( carteTmp );
								}
							}
						}
					}
				}

				if ( ligne.length() > 0 && ligne.charAt(0) == 'M' && joueur != null )
				{
					String monument = " ";
					while ( monument.length() > 0 && sc.hasNextLine() )
					{
						monument = sc.nextLine();
						if ( monument.length() > 0 )
						{
							Decomposeur dec = new Decomposeur( monument );
							CarteMonument carteMonument = joueur.getCarteMonument( dec.getString(1) );
							System.out.println( carteMonument.getNomCarte() );
							if ( carteMonument != null  )
							{
								if ( dec.getChar(0) == 't' )
									carteMonument.setActive( true );
								else
									carteMonument.setActive( false );
							}
						}
					}
				}
			}

		} catch (IOException e) { System.out.println("zedfghjkloiiuygf");}
	}

	public void sauvegarde( String nomDeLaSauvegarde , int numJoueur )
	{
		numJoueur--;
		ArrayList<Joueur> joueurTrie = new ArrayList<Joueur>();
		for(int i = 0 ; i < this.alJoueur.size() ; i++ )
			joueurTrie.add( this.alJoueur.get( ( numJoueur + i ) % this.alJoueur.size() ) );

		PrintWriter fw = null;
		try {
			// ouverture du fichier en mode écriture
			fw = new PrintWriter( "./sauvegarde/" + nomDeLaSauvegarde + ".txt" , "UTF-8");
			
			// écriture des lignes de texte
			String save = "";

			for ( int i = 1 ; i <= joueurTrie.size() ; i++ )
			{
				Joueur joueurActuel = joueurTrie.get(i-1);

				save += "Joueur " + i + " :\t" + joueurActuel.getNom() + "\n\n" +
						 "Piece :\t" + joueurActuel.getNbPiece() +
						 "\n\nCartes :\n";

				int nb = 1;
				Carte carteTmp = joueurActuel.getMain().get(0);
				for( Carte c : joueurActuel.getMain() )
				{
					if ( c.equals(carteTmp) )
						nb++;
					else
					{
						if( c.getNomCarte().equals("Boulangerie") || c.getNomCarte().equals("Champs de blé") )
							nb--;
						
						save += nb + "\t" + carteTmp.getNomCarte() + "\n";
						carteTmp = c;
						nb = 1;
					}
				}
				save += nb + "\t" + carteTmp.getNomCarte() + "\n";
				save += "\nMonument :\n";

				for( CarteMonument m : joueurActuel.getMonument() )
				{
					if( m.getActive() )
						save += "t";
					else
						save += "f";

					save += "\t" + m.getNomCarte() + "\n";
				}
				save += "\n";
			}

			// ecriture de la sauvegarde dans le fichier et fermeture du fichier
			fw.write(save);
			fw.close();
		}
		catch (IOException e){}
	}
}