#! /bin/sh

# Auteur : gl01
# Version initiale : 01/01/2021

# Lance tout les tests
for test in ./created-*
do
  echo $test
  ./$test
done
