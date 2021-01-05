#! /bin/sh

# Auteur : gl01
# Version initiale : 01/01/2021

# Test minimaliste de la syntaxe.
# On lance test_synt sur un fichier valide, et les tests invalides.

# dans le cas du fichier valide, on teste seulement qu'il n'y a pas eu
# d'erreur. Il faudrait tester que l'arbre donné est bien le bon. Par
# exemple, en stoquant la valeur attendue quelque part, et en
# utilisant la commande unix "diff".
#
# Il faudrait aussi lancer ces tests sur tous les fichiers deca
# automatiquement. Un exemple d'automatisation est donné avec une
# boucle for sur les tests invalides, il faut aller encore plus loin.

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

tableau_des_tests_echoues=()
#    .---------- constant part!
#    vvvv vvvv-- the code from above
RED='\033[0;31m'
NC='\033[0m' # No Color
# exemple de définition d'une fonction
test_synt_invalide () {
    # $1 = premier argument.
    if test_synt "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        printf "${GREEN}Echec attendu pour test_synt sur $1.${NC}\n"
    else
        # https://stackoverflow.com/questions/5947742/how-to-change-the-output-color-of-echo-in-linux
        printf "${RED}Succes inattendu de test_synt sur $1.{NC}\n"
        tableau_des_tests_echoues+=($1)
    fi
}
test_synt_valide () {
    # $1 = premier argument.
    if test_synt "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        printf "${RED}Echec inattendu pour test_synt sur $1${NC}\n"
        tableau_des_tests_echoues+=($1)
    else
        printf "${GREEN} Succes attendu de test_synt sur $1.${NC}\n"
    fi
}
for repertoire_de_test in "src/test/deca/syntax/invalid/provided/" "src/test/deca/syntax/invalid/created/" "src/test/deca/syntax/valid/provided/" "src/test/deca/syntax/valid/created/"
do
 echo "Test du répertoire : "$repertoire_de_test
 # https://superuser.com/questions/352289/bash-scripting-test-for-empty-directory
 if [ -z "$(ls -A $repertoire_de_test)" ]; then
    echo "Répertoire vide - pas de test"
 else
   type_test=""
   # https://stackoverflow.com/questions/229551/how-to-check-if-a-string-contains-a-substring-in-bash
   if [[ $repertoire_de_test == *"invalid"* ]]; then
   type_test="test_synt_invalide"
    else
   type_test="test_synt_valide"
    fi
   for cas_de_test in $repertoire_de_test*.deca
   do
       $type_test "$cas_de_test"
   done
 fi
done
if (( ${#tableau_des_tests_echoues[@]} )); then
  # https://stackoverflow.com/questions/15691942/print-array-elements-on-separate-lines-in-bash
  printf '%s\n' "${tableau_des_tests_echoues[@]}"
  exit 1
  else
    printf "${GREEN} tout les test sont passés${NC}\n"
    exit 0
fi
