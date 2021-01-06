#! /bin/sh

# Auteur : gl01
# Version initiale : 01/01/2021

# Les Tests sur la partie A sont lancés ici

cd "$(dirname "$0")"/../../.. || exit 1
PATH=./src/test/script/launchers:"$PATH"

tableau_des_tests_echoues=()
# exemple de définition d'une fonction
test_synt_invalide () {
    # $1 = premier argument.
    if test_synt "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        tput setaf 2
        echo "Echec attendu pour test_synt sur $1."
        tput sgr0
    else
        # https://stackoverflow.com/questions/5947742/how-to-change-the-output-color-of-echo-in-linux
        tput setaf 1
        echo "Succes inattendu de test_synt sur $1."
        tput sgr0
        tableau_des_tests_echoues+=($1)
    fi
}
test_synt_valide () {
    # $1 = premier argument.
    if test_synt "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        tput setaf 1
        echo "Echec inattendu pour test_synt sur $1."
        tput sgr0
        tableau_des_tests_echoues+=($1)
    else
        tput setaf 2
        echo "Succes attendu de test_synt sur $1."
        tput sgr0
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
  tput setaf 1
  echo "des tests on échoués..."
  printf '%s\n' "${tableau_des_tests_echoues[@]}"
  tput sgr0
  exit 1
  else
    tput setaf 2
    echo "tout les test sont passés."
    tput sgr0
    exit 0
fi
