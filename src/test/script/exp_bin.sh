#! /bin/sh

# Auteur : lucie
# Version initiale : 12/01/2021

# Generation automatique des tests pour les opération binaires (+ / % * -)
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
chemin="./src/test/deca/context/invalid/created/binary_exp/op_arith"


gen_exp () {
	#args1=$1
	#args2=$2
	#args3=$3
	filename="$chemin/mult_bool_bool.deca"
	touch  $filename
	exec 3< $filename
	echo "// Description :" >&3
	exec 3>&-
}

gen_exp



