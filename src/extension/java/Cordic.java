package cordic1;

class CORDIC {
	double angle;
	Liste liste;
	int k;
	double X;
	double Y;
	double epsilon;
	public static void main(String[] args) {
		double angle2 = 1.047197551;
		Liste aa = new Liste(0.0000000000000001, null, 0.000000000000001);
		Liste ab = new Liste(0.000000000000001, aa, 0.000000000000001);
		Liste ac = new Liste(0.00000000000001, ab, 0.00000000000001);
		Liste ad = new Liste(0.0000000000001, ac, 0.0000000000001);
		Liste ae = new Liste(0.000000000001, ad, 0.000000000001);
		Liste af = new Liste(0.00000000001, ae, 0.00000000001);
		Liste ag = new Liste(0.0000000001, af, 0.0000000001);
		Liste ah = new Liste(0.000000001, ag, 0.000000001);
		Liste ai = new Liste(0.00000001, ah, 0.00000001);
		Liste aj = new Liste(0.0000001, ai, 0.0000001);
		Liste ak = new Liste(0.0000009999999, aj, 0.000001);
		Liste al = new Liste(0.000009999999, ak, 0.00001);
		Liste am = new Liste(0.00009999999, al, 0.0001);
		Liste an = new Liste(0.0009999996, am, 0.001);
		Liste ao = new Liste(0.0099996666, an, 0.01);
		Liste ap = new Liste(0.0996686524, ao, 0.1);
		Liste aq = new Liste(0.78539816339, ap, 1);
		Liste curr = aq;
		double X = 1;
		double Y = 0;
		double epsilon = 0.0000000000001;
		while(angle2 >= epsilon) {
			if(angle2 < curr.contenu)
				curr = curr.suivant;
			double res = X;
			X = X - (curr.valeur_k * Y);
			Y = Y + (curr.valeur_k * res);
			angle2 = angle2 - curr.contenu;
		}
		double hop = Y / X;
		System.out.println(hop);	
	}
}