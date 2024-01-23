package execution.ihm;

import execution.Controleur;
import execution.metier.Joueur;
import execution.metier.cartes.Carte;
import execution.metier.cartes.CarteBleu;
import execution.metier.cartes.CarteRouge;
import execution.metier.cartes.CarteVerte;
import execution.metier.cartes.CarteViolette;
import execution.metier.cartes.CarteMonument;

import iut.algo.Clavier;
import iut.algo.Console;

import java.lang.Thread;

import java.util.ArrayList;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Plateau {
	private Controleur ctrl;
	private boolean modeDebug;

	public Plateau(Controleur ctrl, boolean modeDebug) {
		this.ctrl = ctrl;
		this.modeDebug = modeDebug;
	}

	public void nbJoueur() {
		int nb;
		do {
			Console.print("Entrer un nombre de joueur entre 2 et 4 : ");
			nb = Clavier.lire_int();
		} while (nb < 2 || nb > 4);

		String[] nom = new String[nb];
		for (int cpt = 0; cpt < nb; cpt++) {
			Console.print("Entrer le nom du Joueur n°" + (cpt + 1) + " : ");
			nom[cpt] = Clavier.lireString();
		}

		this.ctrl.nbJoueur(nb, nom);
	}

	public void tour(int numJoueur, boolean tour2) {
		Console.println(
				"C'est au tour de \033[33m" + this.ctrl.getJoueur().get(numJoueur - 1).getNom() + "\033[37m de jouer.");

		/*-----------------------------------------*/
		/* Vérification de l'activation de la Gare */
		/*-----------------------------------------*/
		int numDe1 = this.ctrl.tirage();
		int numDe2 = this.verifGareActive(numJoueur);

		if (modeDebug) // verifier si la somme des deux ne depasse pas 12 (6 individuellement)
		{
			Console.print("Voulez vous donner des valeurs aux dés ? (oui/non) ");
			if (Clavier.lireString().equals("oui")) {
				Console.print("valeur dé n°1 : ");
				numDe1 = Clavier.lire_int();

				if (this.ctrl.getJoueur().get(numJoueur - 1).getCarteMonument("Gare").getActive()) {
					Console.print("valeur dé n°2 : ");
					numDe2 = Clavier.lire_int();
				}
			}
		}

		Console.print("Vous avez fait " + numDe1);
		if (numDe2 > 0)
			Console.println(" et " + numDe2 + " aux dés.");
		else
			Console.println(" au dé.");

		/*-----------------------------------------------*/
		/* Vérification de l'activation de la Tour Radio */
		/*-----------------------------------------------*/

		if (this.ctrl.getJoueur().get(numJoueur - 1).getCarteMonument("Tour radio").getActive()) {
			if (!tour2) {
				if (numDe1 == numDe2
						&& this.ctrl.getJoueur().get(numJoueur - 1).getCarteMonument("Parc d'attractions").getActive())
					Console.println("Vous avez fait un double, vous allez rejouer un tour.");
				estimation(numJoueur, numDe1 + numDe2);
				Console.print("Voulez-vous relancer vos dés ?  (oui/non) ");
				String choix = Clavier.lireString();
				if (choix.equals("oui")) {
					this.tour(numJoueur, true);
					return;
				}
			}
		}

		/*-----------------------------------------------*/
		/* Récupération des pièces */
		/*-----------------------------------------------*/

		int nbPieceAnc = this.ctrl.getJoueur().get(numJoueur - 1).getNbPiece();
		this.ctrl.revenus(numJoueur, numDe1 + numDe2);
		int nbPieceNew = this.ctrl.getJoueur().get(numJoueur - 1).getNbPiece();

		if (nbPieceNew != nbPieceAnc)
			System.out.print("Vous avez maintenant " + nbPieceNew + " pièce");
		else
			System.out.print("Vous avez toujours " + nbPieceNew + " pièce");

		if (nbPieceNew != 0)
			System.out.println("s.");

		/*-----------------------------------------------*/
		/* Choix */
		/*-----------------------------------------------*/

		action(numJoueur);

		/*----------------------------------------------------*/
		/* Vérification de l'activation du Parc d'attractions */
		/*----------------------------------------------------*/

		if (numDe1 == numDe2
				&& this.ctrl.getJoueur().get(numJoueur - 1).getCarteMonument("Parc d'attractions").getActive()) {
			Console.println("Vous avez fait un double, vous allez rejouer un tour.");
			this.tour(numJoueur, false);
		}

		/*-----------------------------------------*/
		/* Fin du tour */
		/*-----------------------------------------*/

		Console.println("\n\t\t\033[33mVous avez fini votre tour.\033[37m\n\n");

	}

	public int menu() {
		Console.print(
				"\n\t0. information sur les cartes\n" + "\t1. acheter un batiment \n" + "\t2. activer un monument \n" + // choix
																														// avec
																														// monument
						"\t3. ne rien faire\n" + "\t4. sauvegarder partie\n" + "\t\t votre choix : ");
		return Clavier.lire_int();
	}

	public void action(int numJoueur) {
		int choix = this.menu();
		if (choix == 0)
			information(numJoueur);

		if (choix == 1) {
			int montant = this.ctrl.getJoueur().get(numJoueur - 1).getNbPiece();
			Console.println("\nVous possédez " + montant + " pièces");
			Console.print("Quelle carte voulez-vous acheter ?\n");

			boolean bAnnuler = this.acheterCarte(numJoueur);
			if (!bAnnuler) {
				action(numJoueur);
				return;
			}
		}

		if (choix == 2) {
			boolean bAnnuler = acheterCarteMonument(numJoueur);
			if (!bAnnuler) {
				action(numJoueur);
				return;
			}
		}

		if (choix == 4) {
			Console.print("Quel nom voulez-vous donnez à votre sauvegarde ? ");
			String nomDeLaSauvegarde = Console.lireString();
			this.ctrl.sauvegarder(nomDeLaSauvegarde, numJoueur);
			System.exit(0);
		}
	}

	private void information(int numJoueur) {
		Console.println(afficherPioche(true));

		ArrayList<CarteMonument> tmp = this.ctrl.getJoueur().get(numJoueur - 1).getMonument();
		for (int cpt = 0; cpt < tmp.size(); cpt++) {
			String actif = "";
			if (tmp.get(cpt).getActive())
				actif = "\033[32mactif\033[37m";
			else
				actif = "\033[31mnon actif\033[37m";

			Console.println(
					"\t     | \033[33m" + tmp.get(cpt).getNomCarte() + "\033[37m  " + actif + "\n" + "\t     | EFFET : "
							+ tmp.get(cpt).getEffet() + "\n" + "\t     |  COUT : " + tmp.get(cpt).getCout() + "\n\n");
		}
		try {
			Console.println("\033[31mFaites entrer quand vous voulez arreter de lire.\033[37m");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			br.readLine();
		} catch (IOException e) {
		}
	}

	private boolean acheterCarte(int numJoueur) {
		Carte carteTmp = new Carte(null, null, null, null, 0);
		int montant = this.ctrl.getJoueur().get(numJoueur - 1).getNbPiece();
		ArrayList<Carte> cartesPossible = new ArrayList<Carte>();

		for (Carte c : this.ctrl.getPioche()) {
			if (!c.equals(carteTmp) && c.getCout() <= montant) {
				cartesPossible.add(c);
				carteTmp = c;
			}
		}

		int cpt;
		for (cpt = 0; cpt < cartesPossible.size(); cpt++) {
			carteTmp = cartesPossible.get(cpt);
			String coul = mettreCouleur(carteTmp);
			Console.println(String.format("\t%2d. ", (cpt + 1)) + " | (" + carteTmp.getNumCarte() + ") " + coul
					+ carteTmp.getNomCarte() + "\033[37m\n" + "\t     |  TYPE : " + carteTmp.getSymbole() + "\n"
					+ "\t     | EFFET : " + carteTmp.getEffet() + "\n" + "\t     |  COUT : " + carteTmp.getCout()
					+ "\n");
		}
		Console.print(String.format("\t%2d. ", (cpt + 1)) + " Annuler\n");
		Console.print("\t\tvotre choix : ");

		int choix = Clavier.lire_int();
		if (choix > cartesPossible.size())
			return false;

		Carte carteAchetee = cartesPossible.get(choix - 1);
		for (Carte c : this.ctrl.getJoueur().get(numJoueur - 1).getMain())
			if (c.getNomCarte().equals("Stade"))
				return false;

		this.ctrl.carteAchetee(carteAchetee, numJoueur);

		Console.println("Vous venez d'acheter une carte " + carteAchetee.getNomCarte() + ".");
		Console.println(carteAchetee.getEffet());
		return true;
	}

	private boolean acheterCarteMonument(int numJoueur) {

		ArrayList<CarteMonument> cartesPossible = new ArrayList<CarteMonument>();

		for (CarteMonument c : this.ctrl.getJoueur().get(numJoueur - 1).getMonument())
			if (!c.getActive())
				cartesPossible.add(c);

		Console.println("Quelle carte voulez-vous activer ?");
		int cpt;
		for (cpt = 0; cpt < cartesPossible.size(); cpt++)
			Console.println(String.format("\t%2d. ", (cpt + 1)) + cartesPossible.get(cpt).getNomCarte());
		Console.print(String.format("\t%2d. ", (cpt + 1)) + "Annuler\n");
		Console.print("\t\tvotre choix : ");

		int choix = Clavier.lire_int();
		if (choix > cartesPossible.size())
			return false;

		CarteMonument carteAchetee = cartesPossible.get(choix - 1);
		carteAchetee.setActive(true);

		Console.println("Le monument " + carteAchetee.getNomCarte() + " a été acheté. Il est maintenant actif.");
		Console.println(carteAchetee.getEffet());

		return true;
	}

	public int verifGareActive(int numJoueur) {
		if (this.ctrl.getJoueur().get(numJoueur - 1).getCarteMonument("Gare").getActive()) {
			Console.println("Voulez-vous lancer 2 dés ? (oui/non)");
			if (Clavier.lireString().equals("oui")) {
				return this.ctrl.tirage();
			}
		}
		return 0;
	}

	// on simule un tour
	public void estimation(int numJoueur, int numDe) {
		int[] ensPieces = new int[this.ctrl.getJoueur().size()]; // On stocke les pièces de chaques joueurs

		for (int i = 0; i < ensPieces.length; i++)
			ensPieces[i] = this.ctrl.getJoueur().get(i).getNbPiece();

		int nbPieceAnc = this.ctrl.getJoueur().get(numJoueur - 1).getNbPiece();
		this.ctrl.revenus(numJoueur, numDe);
		int nbPieceNew = this.ctrl.getJoueur().get(numJoueur - 1).getNbPiece();

		if (nbPieceNew > nbPieceAnc) {
			nbPieceNew -= nbPieceAnc;
			Console.print("Vous allez gagner " + nbPieceNew + " pièce");
		} else if (nbPieceNew < nbPieceAnc) {
			Console.print("Vous allez perdre");
			if (nbPieceNew == 0)
				Console.println(" toutes vos pieces");
			else {
				nbPieceAnc -= nbPieceNew;
				Console.print(" " + nbPieceAnc + " piece");
			}
		} else if (nbPieceNew == nbPieceAnc)
			Console.print("Vous aurez toujours " + nbPieceNew + " piece");

		if (nbPieceNew != 0)
			Console.println("s.");

		// On réinitialise les pièces de chaque joueur au nombre de pièces qu'il avait
		// au début du tour
		for (int i = 0; i < ensPieces.length; i++)
			this.ctrl.getJoueur().get(i).setNbPiece(ensPieces[i]);

	}

	public void afficherPlateau() {
		Console.println(afficherPioche(false) + "\n\n");

		try {
			Thread.sleep(1 * 1000);
		} catch (Exception e) {
		}

		for (Joueur j : this.ctrl.getJoueur()) {
			try {
				Thread.sleep(1 * 1000);
			} catch (Exception e) {
			}
			Console.println(afficherJoueur(j));
		}
		try {
			Thread.sleep(1 * 1000);
		} catch (Exception e) {
		}

	}

	public String afficherPioche(boolean info) {
		String ret = "\n";
		Carte carteTmp = this.ctrl.getPioche().get(0);
		int nb = 0;

		ret += "\033[33mPioche : \033[37m\n";
		for (Carte c : this.ctrl.getPioche()) {
			if (c.equals(carteTmp))
				nb++;
			else {
				String coul = mettreCouleur(carteTmp);
				ret += "\t " + nb + "x ";
				if (info)
					ret += " | (" + carteTmp.getNumCarte() + ") " + coul + carteTmp.getNomCarte() + "\033[37m\n"
							+ "\t     |  TYPE : " + carteTmp.getSymbole() + "\n" + "\t     | EFFET : "
							+ carteTmp.getEffet() + "\n" + "\t     |  COUT : " + carteTmp.getCout() + "\n\n";
				else
					ret += coul + carteTmp.getNomCarte() + "\033[37m\n";

				carteTmp = c;
				nb = 1;
			}
		}
		String coul = mettreCouleur(carteTmp);
		ret += "\t " + nb + "x ";
		if (info)
			ret += " | (" + carteTmp.getNumCarte() + ") " + coul + carteTmp.getNomCarte() + "\033[37m\n"
					+ "\t     |  TYPE : " + carteTmp.getSymbole() + "\n" + "\t     | EFFET : " + carteTmp.getEffet()
					+ "\n" + "\t     |  COUT : " + carteTmp.getCout() + "\n\n";
		else
			ret += coul + carteTmp.getNomCarte() + "\033[37m\n";

		return ret;
	}

	public String afficherJoueur(Joueur j) {
		String ret = "\n";
		Carte carteTmp = j.getMain().get(0);
		int nb = 0;

		ret += j.getNom() + ": \n" + "  |\t" + j.getNbPiece();
		if (j.getNbPiece() != 0) {
			ret += " pièces\n";
		} else {
			ret += " pièce\n";
		}

		ret += "  |\n  |\t\033[33mCartes :\033[37m\n";
		nb = 0;
		for (Carte c : j.getMain()) {
			if (c.equals(carteTmp))
				nb++;
			else {
				String coul = mettreCouleur(carteTmp);
				ret += "  |\t " + nb + "x " + coul + carteTmp.getNomCarte() + "\033[37m\n";
				carteTmp = c;
				nb = 1;
			}
		}
		String coul = mettreCouleur(carteTmp);
		ret += "  |\t " + nb + "x " + coul + carteTmp.getNomCarte() + "\033[37m\n";

		ret += "  |\n  |\t\033[33mMonuments :\033[37m\n";
		for (CarteMonument c : j.getMonument()) {
			ret += String.format("  |\t%20s : ", c.getNomCarte());
			if (c.getActive())
				ret += "\033[32mactif\033[37m" + "\n";
			else
				ret += "\033[31mnon actif\033[37m" + "\n";
		}

		ret += "  |___________________________________\n\n\n";

		return ret;
	}

	public String mettreCouleur(Carte c) {
		if (c instanceof CarteBleu)
			return "\033[36m";
		if (c instanceof CarteRouge)
			return "\033[31m";
		if (c instanceof CarteVerte)
			return "\033[32m";
		else
			return "\033[35m";
	}
}