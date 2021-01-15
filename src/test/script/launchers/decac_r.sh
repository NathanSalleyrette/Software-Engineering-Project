#!/bin/sh
# $1 le fichier à compiler, $2 la sortie attendue de la machine ima
# c'est un script très lourd en énérgie alors il n'est pas fais sur tout les fichiers
cd "$(dirname "$0")"/../../../.. || exit 1
source ./src/test/script/functions/colors.sh # pour les couleurs
source ./src/test/script/functions/obtient_resultat_attendu.sh
resultat_attendu="$(obtient_resultat_attendu $1)"
sortie_attendue=""

if [ "$resultat_attendu" == "PAS_DE_FICHIER" ];then
  sortie_attendue="PAS_DE_FICHIER"
else
  # https://unix.stackexchange.com/questions/193482/get-last-part-of-string-after-hyphen

  resultat_attendu="${resultat_attendu##*---SORTIE MACHINE IMA---}"
  # https://stackoverflow.com/questions/6594085/remove-first-character-of-a-string-in-bash
  sortie_attendue="${resultat_attendu:1}"
fi


nom_fichier=$(basename --suffix=.deca $1)
for ((i=4; i<=16; i++)); do
   ./src/main/bin/decac $1 -r $i
   if [ $? != 0 ];then
     echo "Le test a echoué pour le nombre de registre -r = $i: le test a crashé"
     exit 1
   fi
   sortie_ima="$(ima ${1%/*}/$nom_fichier.ass)"
   if [ "$sortie_ima" != "$sortie_attendue" ] && [ "$sortie_attendue" != "PAS_DE_FICHIER" ];then
     $rouge
     echo "Le test a echoué pour le nombre de registre -r = $i: la sortie ne correspond pas"
     $reset
     exit 1
   fi
   $vert
   echo "Le test est passé pour le nombre de registre -r = $i"
   $reset
done
exit 0
