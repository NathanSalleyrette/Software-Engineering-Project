package testUlp;

public class ULP {

	public static void main(String[] args) {
		test((float)0.0,(float)2.5);
		test((float)10000.0,(float)10002.5);


	}
	public static void test(float start, float stop) {
		float a =start;
		boolean estCasse=false;
		while(a<=stop) {
			a=(float) (a+0.01);
			float trueUlp= Math.ulp(a);
			float myUlp= monUlp(a);
			if(myUlp==trueUlp) {
				System.out.println("Passe");
			}else {
				System.out.println("Casse on "+ a);
				estCasse=true;
			}
		}
		if(estCasse) {
			System.exit(-1);;
		}
		
	}
	public static float monUlp(float entree) {
		entree=Math.abs(entree);//fonction abs à implémenter en deca
		IEEE754 number=ieee745Compute(entree);
		float entreeSwitch=convertBackAndSwitch(number.exponent, number.fractionPart);
		return Math.abs(entree-entreeSwitch);
	}
	public static IEEE754 ieee745Compute(float a) {
		IEEE754 formatedNumber=new IEEE754();
		if(a<0.0) {
			formatedNumber.sign=true;
		}else {
			formatedNumber.sign=false;
		}
		float fractionPart;
		int exponent;
		if(a >= 1) {
			exponent =0;
			fractionPart =(float) (a/Math.pow(2, exponent));//function puissance à implémenter en deca (peut être avec des shift sachant que ce sont des puissance de 2)
			while(fractionPart>=2){
				exponent = exponent +1 ;
				fractionPart =(float) (a/Math.pow(2, exponent));	
			}
		}else {
			exponent =-1;
			fractionPart =(float) (a/Math.pow(2, exponent));
			while(fractionPart<1){
				exponent = exponent -1 ;
				fractionPart =(float) (a/Math.pow(2, exponent));
			}

		}
		fractionPart=fractionPart-1;
		formatedNumber.exponent=exponent;

		int i =1;
		BooleanLinkedList courrant=formatedNumber.fractionPart;
		while(i<=IEEE754.taillePartieFractionnaire) {
			float oldfractionPart=fractionPart;
			fractionPart=(float) (fractionPart - Math.pow(2, -i));

			if( fractionPart < 0) {
				fractionPart=oldfractionPart;
				courrant.valeur=false;
			}else {
				courrant.valeur=true;
			}
			courrant=courrant.suivant;
			++i;
		}
		return formatedNumber;
	}
	public static float convertBackAndSwitch(int exponent,BooleanLinkedList fractionPart) {
		BooleanLinkedList dernierElement=dernierElement(fractionPart);
		if(dernierElement.valeur) {//Switch le dernier bit
			dernierElement.valeur=false;
		}else {
			dernierElement.valeur=true;
		}
		float value=(float) 0.0;
		BooleanLinkedList courrant=fractionPart;
		int i = 0;
		while(courrant != null) {
			if(courrant.valeur) {
				value+=Math.pow(2, -(i+1));
			}
			courrant=courrant.suivant;
			++i;
		}
		value+=1;
		value=(float) (value*Math.pow(2, exponent));
		return value;
	}
	public static BooleanLinkedList dernierElement (BooleanLinkedList premierElement) {
		BooleanLinkedList courrant=premierElement;
		BooleanLinkedList precedent=premierElement;
		while(courrant != null) {
			precedent=courrant;
			courrant=courrant.suivant;
		}
		return precedent;
	}
}
