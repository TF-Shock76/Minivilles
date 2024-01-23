package execution.metier.cartes;

public class CarteBleu extends Carte
{
	private static final String MESSAGE = "Pendant le tour de n'importe quel joueur.";

	private int    nbPiece;

	public CarteBleu( String numCarte, String nomCarte, String symbole, String effet, int cout )
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