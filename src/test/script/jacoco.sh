#!/bin/sh
# permet de générer le rapport jacoco
# Ne fonctionne que si tout les tests passent
if ! [ -r pom.xml ]; then
	cd "$(dirname "$0")"/../../../
fi
affichage_graphique="false"
xhost # https://superuser.com/questions/643806/detect-if-the-display-is-valid-in-linux/645068
if [ $? -eq 0 ]; then
	affichage_graphique="true"
fi
mvn verify
if [ $affichage_graphique == "true" ]; then
	firefox $(pwd)/target/site/jacoco/index.html
else
	echo "Vous n'avez pas d'interface graphique pouvoir lire le rapport"
	echo "Ouvrez une interface graphique et firefox avec le fichier ./target/site/index.html"
fi
