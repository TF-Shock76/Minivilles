package execution.metier.cartes;

import java.util.ArrayList;

public class CarteMonument extends Carte
{
	private boolean active;
	
	public CarteMonument( String numCarte, String nomCarte, String symbole, String effet, int cout, boolean active )
	{
		super( numCarte, nomCarte, symbole, effet, cout );
		this.active = active;
	}

	public boolean getActive()
	{
		return this.active;
	}

	public void setActive( boolean actif )
	{
		if ( actif == true )
			this.active = actif;
	}
}