#! /bin/sh

# Auteur : lucie
# Version initiale : 12/01/2021

# Generation automatique des tests pour les opération binaires (< <= > >=)
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
chemin="./src/test/deca/context/invalid/created/print"


gen_exp () {
	args1=$1
	filename="$chemin/${args1}.deca"
	touch  $filename
	exec 3> $filename
	echo "// Description :" >&3
	echo "//    test $args1 avec un boolean" >&3
	echo "//" >&3
	echo "// Resultats :" >&3
	echo "//		Erreur contextuelle" >&3
	echo "//    	Ligne 10: (3.31) Les instances affichables peuvent être de type ‘int’, ‘float’, ‘string’" >&3
	echo "">&3
	echo "{" >&3
	echo "boolean a;" >&3
	echo "$args1(a);" >&3
	echo "}" >&3
	exec 3>&-
}



gen_exp "print"
gen_exp "printx"
gen_exp "println"
gen_exp "printlnx"
