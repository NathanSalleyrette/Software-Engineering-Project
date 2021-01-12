#!/bin/sh
# Pour les tests codegen, on doit à la fois executer le compilateur et la machine virtuelle ima
# $1 = fichier deca
# https://stackoverflow.com/questions/3005963/how-can-i-have-a-newline-in-a-string-in-sh
cd "$(dirname "$0")"/../../../.. || exit 1
sortie=$'---CODE_ASSEMBLEUR---\n'

./src/main/bin/decac $1
if [ $? != 0 ];then
  exit 1
fi
nom_fichier=$(basename --suffix=.deca $1)
sortie+="$(cat ${1%/*}/$nom_fichier.ass)"
sortie+=$'\n'
sortie+=$'---SORTIE MACHINE IMA---\n'
sortie_ima="$(ima ${1%/*}/$nom_fichier.ass)"
if [ $? != 0 ];then
  exit 1
fi
sortie+="$sortie_ima"
echo "$sortie"
