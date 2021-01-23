#! /bin/sh

# Auteur : lucie
# Version initiale : 12/01/2021

# Generation automatique des tests pour les opération binaires (&& et ||)
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
chemin="./src/test/deca/context/invalid/created/instanceof"


gen_exp () {
	args1=$1
	args2=$2
	args3=$3
	args4=$4
	filename="$chemin/instanceof_${args1}_${args2}.deca"
	touch  $filename
	exec 3> $filename
	echo "// Description :" >&3
	echo "//    test instanceof entre un $args1 et un $args2" >&3
	echo "//" >&3
	echo "// Resultats :" >&3
	echo "//		Erreur contextuelle" >&3
	echo "//    	Ligne 10 : (3.40) Type gauche : <>, attendu : 'class' ou 'null',  Type droit : <>, attendu : 'class'" >&3
	echo "class A{}">&3
	echo "{" >&3
	echo "A a;" >&3
	echo "boolean c = $args1 instanceof $args2;" >&3
	echo "}" >&3
	exec 3>&-
}



gen_exp "a" "int"
gen_exp "a" "boolean"
gen_exp "a" "void"
gen_exp "a" "float"
gen_exp "a" "null"
gen_exp "a" "0"
gen_exp "1" "A"
gen_exp "1.0" "A"
gen_exp "true" "A"

