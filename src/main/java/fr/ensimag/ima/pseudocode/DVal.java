package fr.ensimag.ima.pseudocode;

/**
 * Operand that contains a value.
 * 
 * @author Ensimag
 * @date 01/01/2021
 */
public abstract class DVal extends Operand {
    /**
     * 
     * @return the power of 2 corresponding to the value for integers, -1 if it is not a power of 2
     */
    public int powerOfTwo() {
        return -1;
    }
}
