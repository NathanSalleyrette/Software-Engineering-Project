#! /bin/sh

# Auteur : gl01
# Version initiale : 01/01/2021

# Les Tests sur la partie A sont lancés ici

cd "$(dirname "$0")"/../../.. || exit 1
PATH=./src/test/script/launchers:"$PATH"
type_test=$1
repertoire_test=$2
tableau_des_tests_echoues=() # va contenir le nom de tout les tests qui ont échoués
resultat_des_tests_echoues=() # va contenir la sortie des tests qui ont échoués
# cette fonction permet de vérifier si la sortie correspond à la sortie attendue
compare_sortie_attendue() { 
# https://www.cyberciti.biz/faq/bash-get-basename-of-filename-or-directory-name/
  fichier_resultat_attendu=$(basename --suffix=.deca $1)
  # https://stackoverflow.com/questions/19482123/extract-part-of-a-string-using-bash-cut-split
  attendue=$(cat ${1%/*}/expected_result/$fichier_resultat_attendu.txt)
  if [ "$attendue" == "$2" ];then
    echo EGAL
  else
    echo DIFFERENT
  fi

}
# Pour les fichiers qui doivent echouer
test_invalide () {
    # $1 = premier argument.
    sortie=$($type_test "$1" 2>&1)
    if [ $? != 0 ]
    then
        tput setaf 2
        echo "Echec attendu pour $type_test sur $1."
        tput sgr0
    else
        # https://stackoverflow.com/questions/5947742/how-to-change-the-output-color-of-echo-in-linux
        tput setaf 1
        echo "Succes inattendu de $type_test sur $1."
        tput sgr0
        tableau_des_tests_echoues+=($1)
        resultat_des_tests_echoue+=("$sortie")
    fi
}
test_valide () {
    # $1 = premier argument.
    sortie=$($type_test "$1" 2>&1)
    if [ $? != 0 ]
    then
        tput setaf 1
        echo "Echec inattendu pour $type_test sur $1."
        tput sgr0
        tableau_des_tests_echoues+=($1)
        # https://stackoverflow.com/questions/29015565/bash-adding-a-string-with-a-space-to-an-array-adds-two-elements
        resultat_des_tests_echoue+=("$sortie")
    else
        resultat_comparaison=$(compare_sortie_attendue $1 "$sortie")
        if [ $resultat_comparaison == "DIFFERENT" ];then
          tput setaf 1
          echo "Le  test $1 s'est déroulé sans eurreur mais résultat n'est pas celui attendu"
          tput sgr0
          tableau_des_tests_echoues+=($1)
          resultat_des_tests_echoue+=("$sortie")
        else
          tput setaf 2
          echo "Succes attendu de $type_test sur $1."
          tput sgr0
        fi


    fi
}
for repertoire_de_test in "$repertoire_test/invalid/provided/" "$repertoire_test/invalid/created/" "$repertoire_test/valid/provided/" "$repertoire_test/valid/created/"
do
 echo "Test du répertoire : "$repertoire_de_test
 # https://superuser.com/questions/352289/bash-scripting-test-for-empty-directory
 if [ -z "$(ls -A $repertoire_de_test)" ]; then
    echo "Répertoire vide - pas de test"
 else
   mode_test=""
   # https://stackoverflow.com/questions/229551/how-to-check-if-a-string-contains-a-substring-in-bash
   if [[ $repertoire_de_test == *"invalid"* ]]; then
   mode_test="test_invalide"
    else
   mode_test="test_valide"
    fi
   for cas_de_test in $repertoire_de_test*.deca
   do
       $mode_test "$cas_de_test"
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
