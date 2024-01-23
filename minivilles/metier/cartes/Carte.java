package execution.metier.cartes;

import java.lang.Comparable;

public class Carte implements Comparable<Carte>
{
	protected String  numCarte;
	protected String  nomCarte;
	protected String  symbole;
	protected String  effet;
	protected int     cout;

	public Carte( String numCarte, String nomCarte, String symbole, String effet, int cout )
	{
		this.numCarte = numCarte;
		this.nomCarte = nomCarte;
		this.symbole  = symbole;
		this.effet    = effet;
		this.cout     = cout;
	}

	public boolean equals( Carte c )
	{
		if ( nomCarte.equals( c.nomCarte ) )
			return true;
		else
			return false;
	}

	public int compareTo( Carte c )
	{
		return this.nomCarte.compareTo( c.nomCarte );
	}

	public String getNumCarte()
	{
		return this.numCarte;
	}

	public String getNomCarte()
	{
		return this.nomCarte;
	}

	public String getSymbole()
	{
		return this.symbole;
	}

	public String getEffet()
	{
		return this.effet;
	}

	public int getCout()
	{
		return this.cout;
	}
}