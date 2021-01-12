#! /bin/sh

# Auteur : gl01
# Version initiale : 01/01/2021

# Lance tout les tests
# Commentez / retirez du tableau "tests" les tests qui ne passent pas
tests=("./src/test/script/script_test_general.sh test_lex ./src/test/deca/lexer" "./src/test/script/script_test_general.sh test_synt ./src/test/deca/syntax" "./src/test/script/script_test_general.sh test_context ./src/test/deca/context" "./src/test/script/script_test_general.sh src/main/bin/decac ./src/test/deca/codegen/")
# tests=("./src/test/script/script_test_general.sh test_lex ./src/test/deca/lexer")
test_echoue=false

for test in "${tests[@]}"
do
  echo $test
  $test
  if [ $? != 0 ];then
    echo "un ou plusieurs test ont echoues"
    test_echoue=true
  fi
done

if [ "$test_echoue" = true ]; then
  exit 1
fi
exit 0
