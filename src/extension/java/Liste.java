package cordic1;

public class Liste {
	double contenu;
	Liste suivant;
	double valeur_k;
	
	Liste(double x, Liste a, double k) {
		contenu = x;
		suivant = a;
		valeur_k = k;
	}
}