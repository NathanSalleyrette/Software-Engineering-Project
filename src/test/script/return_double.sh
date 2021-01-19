#! /bin/sh

# Auteur : lucie
# Version initiale : 12/01/2021

# Generation automatique des tests pour les opération binaires (< <= > >=)
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
chemin="./src/test/deca/context/invalid/created/return"


gen_exp () {
	args1=$1
	args2=$2
	filename="$chemin/return_double_${args1}.deca"
	touch  $filename
	exec 3> $filename
	echo "// Description :" >&3
	echo "//    test double return avec $args1" >&3
	echo "//" >&3
	echo "// Resultats :" >&3
	echo "//		Erreur contextuelle" >&3
	echo "//    	Ligne 12: " >&3
	echo "">&3
	echo "class a{" >&3
	echo "$args1 met(){" >&3
	echo "$args1 a = $args2;" >&3
	echo "return a;return a;}" >&3
	echo "}" >&3
	exec 3>&-
}



gen_exp "int" "1"
gen_exp "boolean" "true"
gen_exp "float" "0.5"
gen_exp "void" ""
