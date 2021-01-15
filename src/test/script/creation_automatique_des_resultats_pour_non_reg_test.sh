#!/bin/sh
# créé automatiquement les fichier .txt contenant la sortie attendue pour les tests
# Permet donc de créer l'oracle de test
# Argument 1 chemin du launcher du test, argument 2 repertoire des tests, argument 3 pas de validation de l'oracle (argument no_valid)
# Exemple  ./creation_automatique_des_resultats_pour_non_reg_test.sh launchers/test_synt ../deca/syntax/valid/created/
# TODO il faut valider les oracles manuellement
source ./src/test/script/functions/colors.sh # pour les couleurs
if [ -z "$(ls -A $2*.deca)" ];then
	$rouge
	echo "Il n'y a aucun test à compléter"
	$reset
	exit
fi
repertoire=$2
# https://stackoverflow.com/questions/793858/how-to-mkdir-only-if-a-directory-does-not-already-exist
mkdir -p $repertoire/./expected_result
for fichier_test in $2*.deca
do
	$bleu
	echo $fichier_test
	sortie=$($1 $fichier_test 2>&1)
	if [ $? != 0 ];then # si le test échoue alors on écrit pas le résultat comme oracle de test
		continue
	fi
	nom_fichier=$(basename --suffix=.deca $fichier_test)
	$reset
	# il faut mettre des "" sinon les sauts de lignes ne sont pas pris en compte
	echo "$sortie"
	$bleu

	while true; do
		if [ "$3" == "no_valid" ];then
			echo "Argument no_valid set on ne valide pas la sortie, elle est écrie directement dans le fichier"
			echo "$sortie" > $repertoire/./expected_result/$nom_fichier.txt
			break
		fi
		read -p "le résultat est il correct ? (Y/N)" yn
		case $yn in
		    [Yy]* ) echo "$sortie" > $repertoire/./expected_result/$nom_fichier.txt ;break;;
		    [Nn]* ) break ;;
		    * ) echo "(Y/N)";;
		esac
		done
 	$reset

done
