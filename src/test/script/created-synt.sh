#! /bin/sh

# Auteur : gl01
# Version initiale : 01/01/2021

# Les Tests sur la partie A sont lancés ici

cd "$(dirname "$0")"/../../.. || exit 1
PATH=./src/test/script/launchers:"$PATH"

tableau_des_tests_echoues=() # va contenur le nom de tout les tests qui ont échoués
resultat_des_tests_echoues=()
# cette fonction permet de vérifier si la sortie correspond à la sortie attendue
compare_sortie_attendue() {
  $attendue=$(cat ./src/test/deca/syntax/expected_result/$1.txt)
  if [ "$attendue" == "$2" ];then
    ::
  fi
}
# Pour les fichiers qui doivent echoués
test_synt_invalide () {
    # $1 = premier argument.
    sortie=$(test_synt "$1" 2>&1)
    if [ $? != 0 ]
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
        resultat_des_tests_echoue+=("$sortie")
    fi
}
test_synt_valide () {
    # $1 = premier argument.
    sortie=$(test_synt "$1" 2>&1)
    if [ $? != 0 ]
    then
        tput setaf 1
        echo "Echec inattendu pour test_synt sur $1."
        tput sgr0
        tableau_des_tests_echoues+=($1)
        # https://stackoverflow.com/questions/29015565/bash-adding-a-string-with-a-space-to-an-array-adds-two-elements
        resultat_des_tests_echoue+=("$sortie")
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
  # https://stackoverflow.com/questions/17403498/iterate-over-two-arrays-simultaneously-in-bash/17409966
  for i in "${!tableau_des_tests_echoues[@]}"; do
    printf "%s sortie:::\n %s\n" "${tableau_des_tests_echoues[i]}" "${resultat_des_tests_echoue[i]}"
  done
  tput sgr0
  exit 1
  else
    tput setaf 2
    echo "tout les test sont passés."
    tput sgr0
    exit 0
fi
