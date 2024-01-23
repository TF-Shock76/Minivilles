package execution.metier.cartes;

public class CarteViolette extends Carte
{
	private static final String MESSAGE = "Pendant votre tour uniquement";

	private int nbPiece;

	public CarteViolette( String numCarte, String nomCarte, String symbole, String effet, int cout )
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
		

		
	}

	

	public int getNbPiece()
	{
		return this.nbPiece;
	}
}