package execution;

import execution.ihm.Plateau;

import execution.metier.Jeu;
import execution.metier.Joueur;
import execution.metier.cartes.Carte;

import iut.algo.Console;

import java.lang.Thread;

import java.util.ArrayList;

public class Controleur
{
	private Plateau ihm;
	private Jeu     metier;

	public Controleur( boolean modeDebug, String nomSvg )
	{
		if ( modeDebug )
			Console.println( "\n\t\t\033[32mMode debug activé\033[37m\n" );

		this.metier   = new Jeu( this );
		this.ihm      = new Plateau( this, modeDebug );
		System.out.println("qsdcqdjcb");
		this.metier.initialiserPartie( nomSvg );
		action();
	}

	public Controleur( boolean modeDebug )
	{
		if ( modeDebug )
			Console.println( "\n\t\t\033[32mMode debug activé\033[37m\n" );

		this.metier = new Jeu( this );
		this.ihm    = new Plateau( this, modeDebug );
		this.ihm.nbJoueur();
		action();
	}

	public void action()
	{
		int     numJoueur = 0;

		boolean jeu = true;
		while( jeu )
		{
			this.ihm.afficherPlateau();
			this.ihm.tour( this.getJoueur().get( numJoueur ).getNumJoueur(), false );
			
			try { Thread.sleep(3*1000); }
			catch ( Exception e ) {}

			if(this.getJoueur().get(numJoueur).bVictoire()) break;
			
			if ( numJoueur == this.getJoueur().size()-1 )
				numJoueur = 0;
			else
				numJoueur ++;
			
		}

		System.out.println("Victoire de " + this.getJoueur().get( numJoueur ).getNom());
	}

	public void nbJoueur( int nbJoueur, String[] nom )
	{
		this.metier.creerJoueur( nbJoueur, nom );
	}

	public int tirage()
	{
		return this.metier.tirage();
	}

	public void sauvegarder( String nomDeLaSauvegarde , int nbJoueur )
	{
		this.metier.sauvegarde(nomDeLaSauvegarde, nbJoueur);		
	}

	public ArrayList<Carte> getPioche()
	{
		return this.metier.getPioche();
	}

	public ArrayList<Joueur> getJoueur()
	{
		return this.metier.getJoueur();
	}

	public void carteAchetee( Carte c, int numJoueur )
	{
		this.metier.carteAchetee( c, numJoueur );
	}

	public void revenus( int numJoueur, int numDe )
	{
		this.metier.revenus( numJoueur, numDe );
	}

	public static void main(String[] args) 
	{
		if ( args.length == 1 )
		{
			if ( args[0].equals("debug") )
				new Controleur( true );
			else
				new Controleur( false, args[0] );
		}
		else if ( args.length == 2 )
			new Controleur( true, args[1] );
		else
			new Controleur( false );
	}	
}
