#! /bin/sh

# Auteur : lucie
# Version initiale : 20/01/2021

# Generation automatique des tests pour les opération binaires (+ / % * -)
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
chemin="./src/test/deca/context/invalid/created/assign"


gen_exp () {
	args1=$1
	args2=$2
	args3=$3
	filename="$chemin/assign_${args1}_${args2}.deca"
	touch  $filename
	exec 3> $filename
	echo "// Description :" >&3
	echo "//    test  assign entre un $args1 et un $args2" >&3
	echo "//" >&3
	echo "// Resultats :" >&3
	echo "//		Erreur contextuelle" >&3
	echo "//    	Ligne 11: (3.28) Les types $args1 et $args2 sont incompatibles pour l’affectation
" >&3
	echo "">&3
	echo "{" >&3
	echo "$args1 a = $args3;" >&3
	echo "$args2 b;" >&3
	echo "b = a;" >&3
	echo "}" >&3
	exec 3>&-
}


gen_exp "int" "boolean" "1"
gen_exp "float" "boolean" "0.5"
gen_exp "float" "int" "0.5"
gen_exp "boolean" "int" "true"
gen_exp "boolean" "float" "true"



