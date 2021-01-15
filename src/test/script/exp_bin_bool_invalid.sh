#! /bin/sh

# Auteur : lucie
# Version initiale : 12/01/2021

# Generation automatique des tests pour les opération binaires (&& et ||)
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
chemin="./src/test/deca/context/invalid/created/binary_exp/op_bool"


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
	echo "//    	Ligne 11: (3.33) Type gauche $args2. Type droit $args3 Type attendu: ‘boolean’" >&3
	echo "">&3
	echo "{" >&3
	echo "$args2 a;" >&3
	echo "$args3 b;" >&3
	echo "boolean c = a ""$args4"" b;" >&3
	echo "}" >&3
	exec 3>&-
}

gen_exp() {
	nom=$1
	op=$2
	gen_exp_part $nom "int" "int" "$op"
	gen_exp_part $nom "float" "float" "$op"
	gen_exp_part $nom "boolean" "float" "$op"
	gen_exp_part $nom "boolean" "int" "$op"
	gen_exp_part $nom "float" "boolean" "$op"
	gen_exp_part $nom "int" "boolean" "$op"
}


gen_exp "and" "&&"
gen_exp "or" "||"

