#! /bin/sh

# Auteur : lucie
# Version initiale : 14/01/2021

# Generation automatique des tests pour les operande manquante sur les operation binaire
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
chemin="./src/test/deca/syntax/invalid/created/operande_manquante"


gen_exp_part () {
	args1=$1
	args2=$2
	args3=$3
	args4=$4
	filename="$chemin/${args1}_${args2}_${args3}.deca"
	touch  $filename
	exec 3> $filename
	echo "// Description :" >&3
	echo "//    test $args1 pour une opérande manquante" >&3
	echo "//" >&3
	echo "// Resultats :" >&3
	echo "//		Erreur syntaxique" >&3
	echo "//    	Ligne 11: operante manquante" >&3
	echo "">&3
	echo "{" >&3
	echo "c = $args2 $args4 $args3;" >&3
	echo "}" >&3
	exec 3>&-
}

gen_exp() {
	nom=$1
	op=$2
	gen_exp_part $nom "" "a" "$op"
	gen_exp_part $nom "a" "" "$op"
}


gen_exp "eq" "=="
gen_exp "neq" "!="
gen_exp "mult" \*
gen_exp "div" "/"
gen_exp "minus" "-"
gen_exp "plus" "+"
gen_exp "and" "&&"
gen_exp "or" "||"
gen_exp "lt" "<"
gen_exp "gt" ">"
gen_exp "leq" "<="
gen_exp "geq" ">="
gen_exp "modulo" "%"





