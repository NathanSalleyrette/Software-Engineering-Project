#! /bin/sh

# Auteur : lucie
# Version initiale : 12/01/2021

# Generation automatique des tests pour les cast entre type par defaut
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
chemin="./src/test/deca/context/invalid/created/cast"


gen_exp () {
	args1=$1
	args2=$2
	args3=$3
	filename="$chemin/$cast_${args1}_${args2}.deca"
	touch  $filename
	exec 3> $filename
	echo "// Description :" >&3
	echo "//    test cast entre un $args1 et $args2" >&3
	echo "//" >&3
	echo "// Resultats :" >&3
	echo "//		Erreur contextuelle" >&3
	echo "//    	Ligne 10: (3.39) Cast impossible de <type1> vers <type2>" >&3
	echo "">&3
	echo "class A {}">&3
	echo "{" >&3
	echo "($args1)($args3);" >&3
	echo "}" >&3
	exec 3>&-
}




gen_exp "int" "boolean" "true"
gen_exp "int" "A" "new A()"
gen_exp "int" "null" "null"
gen_exp "float" "boolean" "true"
gen_exp "float" "A" "new A()"
gen_exp "float" "null" "null"
gen_exp "boolean" "int" "1"
gen_exp "boolean" "float" "0.5"
gen_exp "boolean" "A" "new A()"
gen_exp "boolean" "null" "null"
gen_exp "void" "int" "1"
gen_exp "void" "float" "0.5"
gen_exp "void" "boolean" "true"
gen_exp "void" "A" "new A()"
gen_exp "void" "null" "null"
gen_exp "A" "boolean" "true"
gen_exp "A" "float" "0.5"
gen_exp "A" "int" "1"




