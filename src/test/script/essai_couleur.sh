#!/bin/bash
# ex30a.sh : Version "colorisée" de ex30.sh.
#            Base de données d'adresses.


clear                                   # Efface l'écran.

echo -n "          "
echo -e '\E[37;44m'"\033[1mListe de contacts\033[0m"
                                        # Blanc sur fond bleu
echo; echo
echo -e "\033[1mChoisissez une des personnes suivantes :\033[0m"
                                        # Bold
tput sgr0
echo "(Entrez seulement les premières lettres du nom)"
echo
echo -en '\E[47;34m'"\033[1mE\033[0m"   # Bleu
tput sgr0                               #  Réinitialise les couleurs à la
                                        #+ "normale."
echo "vans, Roland"                     # "[E]vans, Roland"
echo -en '\E[47;35m'"\033[1mJ\033[0m"   # Magenta
tput sgr0
echo "ones, Mildred"
echo -en '\E[47;32m'"\033[1mS\033[0m"   # Vert
tput sgr0
echo "mith, Julie"
echo -en '\E[47;31m'"\033[1mZ\033[0m"   # Rouge
tput sgr0
echo "ane, Morris"
echo

read personne

case "$personne" in
# Notez que la variable est entre guillemets.

  "E" | "e" )
  # Accepte une entrée en majuscule ou minuscule.
  echo
  echo "Roland Evans"
  echo "4321 Floppy Dr."
  echo "Hardscrabble, CO 80753"
  echo "(303) 734-9874"
  echo "(303) 734-9892 fax"
  echo "revans@zzy.net"
  echo "Business partner & old friend"
  ;;

  "J" | "j" )
  echo
  echo "Mildred Jones"
  echo "249 E. 7th St., Apt. 19"
  echo "New York, NY 10009"
  echo "(212) 533-2814"
  echo "(212) 533-9972 fax"
  echo "milliej@loisaida.com"
  echo "Girlfriend"
  echo "Birthday: Feb. 11"
  ;;

# Ajoutez de l'info pour Smith & Zane plus tard.

          * )
   # Option par défaut.   
   # Une entrée vide (en appuyant uniquement sur RETURN) vient ici aussi.
   echo
   echo "Pas encore dans la base de données."
  ;;

esac

tput sgr0                               #  Réinitialisation des couleurs à la
                                        #+ "normale".

echo

exit 0