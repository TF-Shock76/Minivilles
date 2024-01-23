package execution.metier.cartes;

public class CarteVerte extends Carte
{
	private static final String MESSAGE = "Pendant votre tour uniquement.";
	
	private String condition;
	private int    nbPiece;
	
	public CarteVerte( String numCarte, String nomCarte, String symbole, String effet, int cout )
	{
		super( numCarte, nomCarte, symbole, effet, cout );

		initialiserEffet();
	}

	public void initialiserEffet()
	{
		if ( effet.contains( "Recevez") )
			this.nbPiece = (int) ( effet.charAt(8) - '0' );
		else
			this.nbPiece = 0;
		
		if ( effet.contains( "\"" ) )
			this.condition = effet.substring( effet.indexOf( "\"" )+1, effet.lastIndexOf( "\"" ) );
		else
			this.condition = null;
	}

	/*public int effetCentreCommercial(int numDe)
	{
		switch(numDe)
		{
			case 2: return 1;
			case 3: return 1;
			case 4: return 1;
		}
		return 0;
	}*/

	public String getCondition()
	{
		return this.condition;
	}

	public int getNbPiece()
	{
		return this.nbPiece;
	}
}