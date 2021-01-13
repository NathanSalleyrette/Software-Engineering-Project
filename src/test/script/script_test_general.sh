#! /bin/sh

# Auteur : gl01
# Version initiale : 01/01/2021
cd "$(dirname "$0")"/../../.. || exit 1
source ./src/test/script/colors.sh # pour les couleurs
$bleu
echo "Debut des test sur $1"
$reset

PATH=./src/test/script/launchers:"$PATH"
type_test=$1
repertoire_test=$2
tableau_des_tests_echoues=() # va contenir le nom de tout les tests qui ont échoués
resultat_des_tests_echoues=() # va contenir la sortie des tests qui ont échoués
resultat_attendu_des_tests_echoues=() # va contenir la sortie attendue des tests qui ont échoués
repertoires=("$repertoire_test/invalid/provided/" "$repertoire_test/valid/provided/")
obtient_resultat_attendu(){
  # https://www.cyberciti.biz/faq/bash-get-basename-of-filename-or-directory-name/
  fichier_resultat_attendu=$(basename --suffix=.deca $1)
  # https://stackoverflow.com/questions/19482123/extract-part-of-a-string-using-bash-cut-split
  attendu="$(cat ${1%/*}/expected_result/$fichier_resultat_attendu.txt 2>/dev/null)"
  if [ $? != 0 ];then # s'il n'y a pas de fichier de référence
    echo "PAS_DE_FICHIER"
    exit 1
  else
    echo "$attendu"
    exit 0
  fi
}

# cette fonction permet de vérifier si la sortie correspond à la sortie attendue
compare_sortie_attendue() {

  attendu="$(obtient_resultat_attendu $1)"
  if [ $? != 0 ];then
    echo "$attendu"
    exit 1
  else
    if [ "$attendu" == "$2" ];then
      echo EGAL
    else
      echo DIFFERENT
    fi
  fi

}
# Pour les fichiers qui doivent echouer
test_invalide () {
    # $1 = premier argument.
    commande="$type_test $1"
    sortie=$($commande 2>&1)
    if [ $? != 0 ]
    then
        $vert
        echo "Echec attendu pour $type_test sur $1."
        $reset
    else
        # https://stackoverflow.com/questions/5947742/how-to-change-the-output-color-of-echo-in-linux
        $rouge
        echo "Succes inattendu de $type_test sur $1."
        echo "commande utilisée ::: $commande"
        $reset
        tableau_des_tests_echoues+=($1)
        resultat_des_tests_echoues+=("$sortie")
        resultat_attendu_des_tests_echoues+=("$(obtient_resultat_attendu $1)")
    fi
}
test_valide () {
    # $1 = premier argument.
    commande="$type_test $1"
    sortie=$($commande 2>&1)
    if [ $? != 0 ]
    then
        $rouge
        echo "Echec inattendu pour $type_test sur $1."
        echo "commande utilisée ::: $commande"
        $reset
        tableau_des_tests_echoues+=($1)
        # https://stackoverflow.com/questions/29015565/bash-adding-a-string-with-a-space-to-an-array-adds-two-elements
        resultat_des_tests_echoues+=("$sortie")
        resultat_attendu_des_tests_echoues+=("$(obtient_resultat_attendu $1)")
    else
        resultat_comparaison=$(compare_sortie_attendue "$1" "$sortie")
        if [ "$resultat_comparaison" == "DIFFERENT" ];then
          $jaune
          echo "Le  test $1 s'est déroulé sans erreur mais résultat n'est pas celui attendu"
          echo "commande utilisée ::: $commande"
          $reset
          tableau_des_tests_echoues+=($1)
          resultat_des_tests_echoues+=("$sortie")
          resultat_attendu_des_tests_echoues+=("$(obtient_resultat_attendu $1)")
        elif [ $resultat_comparaison == "PAS_DE_FICHIER" ];then
          $violet
          echo "Il n'y a pas de fichier de comparaison (pas d'oracle de test) pour le test $1. Ce test est considéré comme valide mais veuillez creer un oracle de test"
          $reset
        else
          $vert
          echo "Succes attendu de $type_test sur $1."
          $reset
        fi


    fi
}
detecte_sous_repertoire(){
  if [ -d $1 ];then
    # https://stackoverflow.com/questions/6781225/how-do-i-check-if-a-directory-has-child-directories
    if [ $(find $1 -maxdepth 1 -type d -not -name expected_result| wc -l) -eq 1 ];then # le repertoire ne contient pas de sous repertoire
      repertoires+=("$1")
      return
    fi
    for d in $1/*/; do
      if [[ "$d" != *"expected_result"* ]];then
        detecte_sous_repertoire $d
      fi
    done
  fi
}

detecte_sous_repertoire "$repertoire_test/invalid/created/"
detecte_sous_repertoire "$repertoire_test/valid/created/"
# https://stackoverflow.com/questions/13648410/how-can-i-get-unique-values-from-an-array-in-bash
repertoires=($(echo "${repertoires[@]}" | tr ' ' '\n' | sort -u | tr '\n' ' '))
for repertoire_de_test in "${repertoires[@]}"
do
 echo "Test du répertoire : "$repertoire_de_test
 # https://superuser.com/questions/352289/bash-scripting-test-for-empty-directory
 if [ -z "$(ls -A $repertoire_de_test*.deca)" ]; then
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
  $rouge
  echo "des tests on échoués... Voici la liste"
  # https://stackoverflow.com/questions/17403498/iterate-over-two-arrays-simultaneously-in-bash/17409966
  for i in "${!tableau_des_tests_echoues[@]}"; do
    $bleu
    printf "%s sortie actuelle:::\n" "${tableau_des_tests_echoues[i]}"
    $rouge
    printf "%s\n\n" "${resultat_des_tests_echoues[i]}"
    $vert
    printf "sortie attendue:::\n"
    printf "%s\n" "${resultat_attendu_des_tests_echoues[i]}"
    echo ""
    echo ""
  done
  $reset
  printf "\n\n\n\n"
  exit 1
  else
    $vert
    echo "tout les test sont passés."
    $reset
    printf "\n\n\n\n"
    exit 0
fi
