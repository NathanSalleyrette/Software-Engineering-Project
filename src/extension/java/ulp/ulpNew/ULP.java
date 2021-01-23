package testUlp;


public class ULP {

    public static void main(String[] args) {
        test((float)0.0,(float)2.5);
        test((float)10000.0,(float)10002.5);
        test((float)100000.0,(float)100003.5);
    }
    public static void test(float start, float stop) {
        float a =start;
        boolean estCasse=false;
        while(a<=stop) {

            float trueUlp= Math.ulp(a);
            float myUlp= monUlp(a);
            if(myUlp==trueUlp) {
                System.out.println("Passe");
            }else {
                System.out.println("Casse on "+ a);
                estCasse=true;
            }
            a=(float) (a+0.01);
        }
        if(estCasse) {
            System.err.println("wrong");
            System.exit(-1);;
        }

    }
    public static float monUlp(float entree) {
        entree=Math.abs(entree);//fonction abs à implémenter en deca
        if (entree < Float.MIN_NORMAL) {
            return Float.MIN_VALUE;//remplacer par min_normal en deca ? 
        }
        int exponent=trouveExposant(entree);
        float ulp= (float) Math.pow(2, (exponent-23));
        return ulp;

    }
    public static int trouveExposant(float a) {
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
            while((fractionPart<1) && exponent >-127 ){
                exponent = exponent -1 ;
                fractionPart =(float) (a/Math.pow(2, exponent));

            }

        }
        int c = Float.floatToIntBits(a);
        return exponent;
    }
}

