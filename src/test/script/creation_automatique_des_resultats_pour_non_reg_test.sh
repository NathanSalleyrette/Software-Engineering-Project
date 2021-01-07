#!/bin/sh
# créé automatiquement les fichier .txt contenant la sortie attendue pour les tests
# Permet donc de créer l'oracle de test 
pwd
echo $1 $2

if [ -z "$(ls -A $2)" ];then
	echo "Il n'y a aucun test à compléter"
	exit 
fi
repertoire=$2
# https://stackoverflow.com/questions/793858/how-to-mkdir-only-if-a-directory-does-not-already-exist
mkdir -p $repertoire/./expected_result 
for fichier_test in $2*.deca
do
	tput setaf 6
	
	echo $fichier_test
	sortie=$($1 $fichier_test 2>&1)
	
	nom_fichier=$(basename --suffix=.deca $fichier_test)
	tput sgr0
	echo $sortie
	tput setaf 6
	
	while true; do
		read -p "le résultat est il correct ? (Y/N)" yn
		case $yn in
		    [Yy]* ) echo $sortie > $repertoire/./expected_result/$nom_fichier.txt ;break;;
		    [Nn]* ) break ;;
		    * ) echo "(Y/N)";;
		esac
		done
 	tput sgr0
 
done