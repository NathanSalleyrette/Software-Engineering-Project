#! /bin/sh

# Auteur : lucie
# Version initiale : 12/01/2021

# Generation automatique des tests pour les opération binaires (+ / % * -)
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
chemin="./src/test/deca/context/invalid/created/binary_exp/op_arith"


gen_exp_part () {
	args1=$1
	args2=$2
	args3=$3
	args4=$4
	filename="$chemin/${args1}_${args2}_${args3}.deca"
	touch  $filename
	exec 3> $filename
	echo "// Description :" >&3
	echo "//    test $args1 entre un $args2 et un $args3" >&3
	echo "//" >&3
	echo "// Resultats :" >&3
	echo "//		Erreur contextuelle" >&3
	echo "//    	Ligne 10: (3.33) L’opérande gauche est de type $args2, L’opérande droite est de type $args3. Or les types des opérandes doivent être ‘int’ ou ‘float’ pour l’opérateur $args4" >&3
	echo "class A{}">&3
	echo "{" >&3
	echo "A a = new A();" >&3
	echo "int c = $args2 ""$args4"" $args3;" >&3
	echo "}" >&3
	exec 3>&-
}

gen_exp() {
	nom=$1
	op=$2
	gen_exp_part $nom "1" "null" "$op"
	gen_exp_part $nom "0.5" "null" "$op"
	gen_exp_part $nom "null" "1" "$op"

}


gen_exp "mult" \*
gen_exp "div" "/"
gen_exp "minus" "-"
gen_exp "plus" "+"
