package fr.ensimag.ima.pseudocode;

/**
 * Immediate operand representing an integer.
 * 
 * @author Ensimag
 * @date 01/01/2021
 */
public class ImmediateInteger extends DVal {
    private int value;

    public ImmediateInteger(int value) {
        super();
        this.value = value;
    }

    @Override
    public int powerOfTwo() {
        int val = value;
        int res = 0;
        while (val != 1) {
            if (val % 2 == 0) {
                res++;
                val >>= 1;
            } else {
                return -1;
            }
        }
        return res;
    }

    @Override
    public String toString() {
        return "#" + value;
    }
}
