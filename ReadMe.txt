Bonjour, 
pour compiler et exécuter ce code vous pouvez utiliser le "Compil&Exe.bat" si vous êtes sur windows.
Sinon sur Linux ou mac, vous pouvez compiler en faisant "javac -encoding utf8 @Compile.list -d ./",
puis executer en faisant "java execution.Controleur args[0] args[1]".

args[0] peut être égal à "debug" ou alors "nom de la sauvegarde à charger" sans .txt

args[1] uniquement si args[0] = debug, peut être égal à  "nom de la sauvegarde à charger"

debug permet de pouvoir choisir le numéro des dés

la sauvegarde doit être placée dans le dossier "sauvegarde" et en .txt en respectant les règles comme le "initialisation.txt"

on peut jouer de 2 à 4 joueurs. ( Il y a une vérification du int saisi )
choisir le nom de chaque joueur

à chaque tour la pioche est affichée
puis la main de tous les joueurs est affichée
puis le programme annonce le nom du joueur qui doit jouer
ensuite le numéro que l'on a fait au(x) dé(s)
ensuite on nous annonce notre nombre de pièces

A chaque tour les joueurs ont 5 options.
-"Information"
	qui permet de lister toutes les cartes avec leur effet

- "acheter un batiment"
	qui permet de choisir d'acheter un batiment dans la liste qui s'affiche, la liste contient uniquement les batiments qui peuvent être
	achetés par le joueur en fonction de son nombre de pieces
si on décide de rien acheter cela passe le tour

- "activer un monument"
	qui permet d'activer un monument dans la liste qui est affichée, la liste contient uniquement les monuments activables par le joueur
	en fonction de son nombre de pièces
si on décide de rien activer cela passe le tour

- "ne rien faire"
	qui permet de ne faire aucune action ce tour ci et passe le tour

- "sauvegarder partie"
	qui permet de sauvegarder la partie et qui la recommence au même tour ou on l'a arreter le nom du ficher à saisir ne doit pas
	contenir .txt cela est fait automatioquement
