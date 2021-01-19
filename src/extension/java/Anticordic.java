package cordic1;

class CORDIC {
	double nombre;
	Liste liste;
	int L;
	double X;
	double Y;
	double epsilon;
	public static void main(String[] args) {
		int signe = -1;
		double nombre = 3.5;
		Liste aa = new Liste(0.00001525878906, null, 0.00001525878906);
		Liste ab = new Liste(0.00003051757812, aa, 0.00003051757813);
		Liste ac = new Liste(0.00006103515617 , ab, 0.00006103515625);
		Liste ad = new Liste(0.0001220703119, ac, 0.0001220703125);
		Liste ae = new Liste(0.0002441406201, ad, 0.000244140625);
		Liste af = new Liste(0.0004882812112, ae, 0.00048828125);
		Liste ag = new Liste(0.0009765621896, af, 0.0009765625);
		Liste ah = new Liste(0.001953122516, ag, 0.001953125);
		Liste ai = new Liste(0.003906230132, ah, 0.00390625);
		Liste aj = new Liste(0.00781234106, ai, 0.0078125);
		Liste ak = new Liste(0.01562372862, aj, 0.015625);
		Liste al = new Liste(0.03123983343, ak, 0.03125);
		Liste am = new Liste(0.06241881, al, 0.0625);
		Liste an = new Liste(0.1243549945, am, 0.125);
		Liste ao = new Liste(0.2449786631, an, 0.25);
		Liste ap = new Liste(0.463647609, ao, 0.5);
		Liste aq = new Liste(0.78539816339, ap, 1);
		Liste ar = new Liste(0, aq, 1);
		Liste curr = ar;
		double X = 2;
		double Y = 7;
		double epsilon = 0.0001;
		double grosse_somme = -1.570796327;
		double res = X;
		X = Y;
		Y = -res;
		while((Y >= 0 && Y >= epsilon) || (Y <= 0 && Y <= epsilon)){
			curr = curr.suivant;
			if(Y > 0)
				signe = -1;
			else
				signe = 1;
			nombre = signe * curr.contenu;
			grosse_somme = grosse_somme + nombre;
			res = X;
			X = X - (signe * curr.valeur_k * Y);
			Y = Y + (signe * curr.valeur_k * res);
		}
		System.out.println(-grosse_somme);
	}
}