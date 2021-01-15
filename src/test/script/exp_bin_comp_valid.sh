#! /bin/sh

# Auteur : lucie
# Version initiale : 12/01/2021

# Generation automatique des tests valid pour les opération binaires (< <= > >=)
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"
chemin="./src/test/deca/context/valid/created/exp_bin/op_comp"


gen_exp () {
	args1=$1
	args4=$2
	filename="$chemin/${args1}.deca"
	touch  $filename
	exec 3> $filename
	echo "// Description :" >&3
	echo "//    test $args1 " >&3
	echo "//" >&3
	echo "// Resultats :" >&3
	echo "//		Ok" >&3
	echo "" >&3
	echo "{" >&3
	echo "int a;" >&3
	echo "float b;" >&3
	echo "boolean c;" >&3
	echo "c = 1 ""$args4"" 2;" >&3
	echo "c = 0.1 ""$args4"" 2;" >&3
	echo "c = 1 ""$args4"" 0.2;" >&3
	echo "c = 0.1 ""$args4"" 0.2;" >&3
	echo "c = a ""$args4"" 1;" >&3
	echo "c = a ""$args4"" 0.1;" >&3
	echo "c = 1 ""$args4"" b;" >&3
	echo "c = 0.1 ""$args4"" b;" >&3
	echo "c = a ""$args4"" b;" >&3
	echo "c = a ""$args4"" a;" >&3
	echo "c = b ""$args4"" b;" >&3
	echo "}" >&3
	exec 3>&-
}


gen_exp "lt" "<"
gen_exp "gt" ">"
gen_exp "leq" "<="
gen_exp "geq" ">="
