Bonjour, 
pour compiler et ex�cuter ce code vous pouvez utiliser le "Compil&Exe.bat" si vous �tes sur windows.
Sinon sur Linux ou mac, vous pouvez compiler en faisant "javac -encoding utf8 @Compile.list -d ./",
puis executer en faisant "java execution.Controleur args[0] args[1]".

args[0] peut �tre �gal � "debug" ou alors "nom de la sauvegarde � charger" sans .txt

args[1] uniquement si args[0] = debug, peut �tre �gal �  "nom de la sauvegarde � charger"

debug permet de pouvoir choisir le num�ro des d�s

la sauvegarde doit �tre plac�e dans le dossier "sauvegarde" et en .txt en respectant les r�gles comme le "initialisation.txt"

on peut jouer de 2 � 4 joueurs. ( Il y a une v�rification du int saisi )
choisir le nom de chaque joueur

� chaque tour la pioche est affich�e
puis la main de tous les joueurs est affich�e
puis le programme annonce le nom du joueur qui doit jouer
ensuite le num�ro que l'on a fait au(x) d�(s)
ensuite on nous annonce notre nombre de pi�ces

A chaque tour les joueurs ont 5 options.
-"Information"
	qui permet de lister toutes les cartes avec leur effet

- "acheter un batiment"
	qui permet de choisir d'acheter un batiment dans la liste qui s'affiche, la liste contient uniquement les batiments qui peuvent �tre
	achet�s par le joueur en fonction de son nombre de pieces
si on d�cide de rien acheter cela passe le tour

- "activer un monument"
	qui permet d'activer un monument dans la liste qui est affich�e, la liste contient uniquement les monuments activables par le joueur
	en fonction de son nombre de pi�ces
si on d�cide de rien activer cela passe le tour

- "ne rien faire"
	qui permet de ne faire aucune action ce tour ci et passe le tour

- "sauvegarder partie"
	qui permet de sauvegarder la partie et qui la recommence au m�me tour ou on l'a arreter le nom du ficher � saisir ne doit pas
	contenir .txt cela est fait automatioquement
