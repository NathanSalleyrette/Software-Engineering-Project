#! /bin/sh

# Auteur : gl01
# Version initiale : 01/01/2021

# Lance tout les tests

./script_test_general.sh test_lex ./src/test/deca/lexer
./script_test_general.sh test_synt ./src/test/deca/syntax
./script_test_general.sh test_context ./src/test/deca/context
