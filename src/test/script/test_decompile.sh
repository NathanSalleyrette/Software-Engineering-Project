#! /bin/sh


# Auteur : lucie
# Version initiale : 13/01/2021

# Generation automatique des tests pour les opération binaires (< <= > >=)
cd "$(dirname "$0")"/../../.. || exit 1

source ./src/test/script/functions/colors.sh # pour les couleurs

PATH=./src/test/script/launchers:"$PATH"
PATH=./src/main/bin:"$PATH"
succes=1


decompile () {
	chemin=$1
	touch "/tmp/f1.txt"
	f1="/tmp/f1.txt"
	touch "/tmp/f2.txt"
	f2="/tmp/fichier2.txt"
	for fichier in $chemin/*.deca 
	do
		decac -p $fichier > $f1
		decac -p $f1 > $f2
		DIFF=$(diff $f1 $f2)
		if  diff -q "$f1" "$f2" ; then
			$vert
			echo "$fichier decompilation reussie"
			$reset
		else
			$rouge
			echo "$fichier decompilation echec"
			$reset
			$succes=0
		fi
	done
	rm $f1
	rm $f2
	if [ $succes -eq 1 ]
	then
		exit 0
	else
		exit 1
	fi
	fin 
}

decompile "./src/test/deca/syntax/valid/created/"



