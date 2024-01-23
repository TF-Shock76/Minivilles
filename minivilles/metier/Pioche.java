package execution.metier;

import execution.metier.cartes.Carte;
import execution.metier.cartes.CarteBleu;
import execution.metier.cartes.CarteRouge;
import execution.metier.cartes.CarteVerte;
import execution.metier.cartes.CarteViolette;
import execution.metier.cartes.CarteMonument;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;
import iut.algo.Decomposeur;

public class Pioche
{
	private ArrayList<Carte> alCartes;

	public Pioche()
	{
		this.alCartes = new ArrayList<Carte>();
		initialiserCartes();
	}

	public void initialiserCartes()
	{
		String ligne="";
		try
		{
			Scanner sc = new Scanner ( new FileInputStream( "Cartes.txt" ), "UTF-8" );  // mettre l'encoding utf-8

			while ( sc.hasNext() )
			{
				ligne = sc.nextLine() ;
				Decomposeur dec = new Decomposeur (ligne);

				String couleur = dec.getString(3);

				Carte  tmp;
				
				switch( couleur )
				{
					case "vert"   : tmp = new CarteVerte    (dec.getString(0), dec.getString(1), dec.getString(2), dec.getString(4), dec.getInt(5)); break;
					case "bleu"   : tmp = new CarteBleu     (dec.getString(0), dec.getString(1), dec.getString(2), dec.getString(4), dec.getInt(5)); break;
					case "rouge"  : tmp = new CarteRouge    (dec.getString(0), dec.getString(1), dec.getString(2), dec.getString(4), dec.getInt(5)); break;
					case "violet" : tmp = new CarteViolette (dec.getString(0), dec.getString(1), dec.getString(2), dec.getString(4), dec.getInt(5)); break;
					default       : tmp = new Carte         (dec.getString(0), dec.getString(1), dec.getString(2), dec.getString(4), dec.getInt(5)); break;
				}

				for ( int cpt=0; cpt<dec.getInt(6); cpt++ )
					alCartes.add( tmp );
			}
		} catch (Exception e) { e.printStackTrace(); }
	}

	public ArrayList<Carte> getAlCartes()
	{
		ArrayList<Carte> tmp = new ArrayList<Carte>();
		for ( Carte c : alCartes )
			tmp.add( c );

		return tmp;
	}

	public Carte getCarte( String nom )
	{
		for ( Carte c : alCartes )
			if ( c.getNomCarte().equals(nom) )
				return c;

		return null;
	}

	public void defausse( Carte c )
	{
		this.alCartes.remove( c );
	}
}