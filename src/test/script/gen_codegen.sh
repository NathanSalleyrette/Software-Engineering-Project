#! /bin/sh

# Auteur : lucie
# Version initiale : 12/01/2021

# Generateyr automatique des tests codegen pour les opérateur binaire
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
chemin="./src/test/deca/codegen/valid/created/"


gen_exp_part () {
	args1=$1
	args2=$2
	args3=$3
	filename="$chemin/${args1}.deca"
	touch  $filename
	exec 3> $filename
	echo "// Description :" >&3
	echo "//    test du $args1 seul pour codegen" >&3
	echo "//" >&3
	echo "// Resultats :" >&3
	echo "//    	Ok" >&3
	echo "">&3
	echo "{" >&3
	echo "$args2 a = true ""$args3"" false;" >&3
	echo "}" >&3
	exec 3>&-
}

gen_exp() {
	nom=$1
	op=$2
	gen_exp_part $nom "boolean" "$op"

}


#gen_exp "plus" "+"
#gen_exp "minus" "-"
#gen_exp "mult" \*
#gen_exp "div" "/"
#gen_exp "modulo" "%"

#gen_exp "geq" ">="
#gen_exp "gt" ">"
#gen_exp "lt" "<"
#gen_exp "leq" "<="
#gen_exp "eq" "=="
#gen_exp "neq" "!="

gen_exp "and" "&&"
gen_exp "or" "||"

