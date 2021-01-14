#! /bin/sh

# Auteur : lucie
# Version initiale : 12/01/2021

# Generation automatique des tests if invalid
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
chemin="./src/test/deca/context/invalid/created/if_while"


gen_exp_part () {
	args1=$1
	args2=$2
	filename="$chemin/${args2}_${args1}.deca"
	touch  $filename
	exec 3> $filename
	echo "// Description :" >&3
	echo "//    test du $args2 avec condition $args1 " >&3
	echo "//" >&3
	echo "// Resultats :" >&3
	echo "//		Erreur contextuelle" >&3
	echo "//    	Ligne 10: (3.29) Type de l’expression : $args1, attendu : ‘boolean’ pour une condition" >&3
	echo "">&3
	echo "{" >&3
	echo "$args1 a;" >&3
	echo "$args2 (a) {}" >&3
	echo "}" >&3
	exec 3>&-
}

gen_exp () {
op=$1
gen_exp_part "float"  $op
gen_exp_part "int" $op
}

gen_exp "if"
gen_exp "while"

