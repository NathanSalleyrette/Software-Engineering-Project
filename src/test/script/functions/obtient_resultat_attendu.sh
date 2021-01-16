#!/bin/sh
obtient_resultat_attendu() {
	# https://www.cyberciti.biz/faq/bash-get-basename-of-filename-or-directory-name/
	fichier_resultat_attendu=$(basename --suffix=.deca $1 2>/dev/null)
	# https://stackoverflow.com/questions/19482123/extract-part-of-a-string-using-bash-cut-split
	attendu="$(cat ${1%/*}/expected_result/$fichier_resultat_attendu.txt 2>/dev/null)"
	if [ $? != 0 ]; then # s'il n'y a pas de fichier de référence
		echo "PAS_DE_FICHIER"
		exit 1
	else
		echo "$attendu"
		exit 0
	fi
}
