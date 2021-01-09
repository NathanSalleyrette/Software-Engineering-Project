#! /bin/sh

# Auteur : gl01
# Version initiale : 01/01/2021

# Lance tout les tests
tests=("./src/test/script/script_test_general.sh test_lex ./src/test/deca/lexer" "./src/test/script/script_test_general.sh test_synt ./src/test/deca/syntax" "./src/test/script/script_test_general.sh test_context ./src/test/deca/context")

test_echoue=false

for test in "${tests[@]}"
do
  echo $test
  $test
  if [ $? != 0 ];then
    echo "un test a echoue"
    test_echoue=true
  fi
done

if [ "$test_echoue" = true ]; then
  exit 1
fi
exit 0
