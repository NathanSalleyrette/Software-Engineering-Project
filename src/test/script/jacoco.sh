#!/bin/sh
# permet de générer le rapport jacoco
# Ne fonctionne que si tout les tests passent
# Donc vous pouvez commenter les tests qui ne fonctionne pas dans master-all-test.sh pour générer le rapport quand même.
# De plus, mvn verify ne fonctionnera pas (mais le rapport sera quand même généré) tant que la couverture des tests est inférieure à 75%
# Un mvn clean pourrait aussi aider dans certain cas comme toujours...
# <rule>
#     <element>BUNDLE</element>
#     <limits>
#         <limit>
#             <counter>INSTRUCTION</counter>
#             <value>COVEREDRATIO</value>
#             <minimum>0.75</minimum> <!-- A FAIRE CHANGER ???-->
#         </limit>
#     </limits>
# </rule>
if ! [ -r pom.xml ]; then
    cd "$(dirname "$0")"/../../../
fi
affichage_graphique="false"
xhost # https://superuser.com/questions/643806/detect-if-the-display-is-valid-in-linux/645068
if [ $? -eq 0 ];then
  affichage_graphique="true"
fi
mvn verify
if [ $affichage_graphique == "true" ];then
firefox $(pwd)/target/site/jacoco/index.html
else
echo "Vous n'avez pas d'interface graphique pouvoir lire le rapport"
echo "Ouvrez une interface graphique et firefox avec le fichier ./target/site/index.html"
fi
