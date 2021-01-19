package testUlp;

public class IEEE754 {
	final static int taillePartieFractionnaire=23;
	boolean sign;
	int exponent; 
	BooleanLinkedList fractionPart;
	public IEEE754() {
		int i =0;
		fractionPart = new BooleanLinkedList();
		BooleanLinkedList courrant=fractionPart;
		BooleanLinkedList suivant;
		while (i<(taillePartieFractionnaire -1)) {
			suivant=new BooleanLinkedList();
			courrant.suivant=suivant;
			courrant.valeur=false;
			courrant=suivant;
			++i;
		}
		courrant.suivant=null;
	}
}
