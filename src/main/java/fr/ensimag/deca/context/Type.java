package fr.ensimag.deca.context;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
/**
 * Deca Type (internal representation of the compiler)
 *
 * @author gl01
 * @date 01/01/2021
 */

public abstract class Type {


    /**
     * True if this and otherType represent the same type (in the case of
     * classes, this means they represent the same class).
     */
    public abstract boolean sameType(Type otherType);

    private final Symbol name;

    public Type(Symbol name) {
        this.name = name;
    }

    public Symbol getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName().toString();
    }

    public boolean isClass() {
        return false;
    }

    public boolean isInt() {
        return false;
    }

    public boolean isFloat() {
        return false;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isVoid() {
        return false;
    }

    public boolean isString() {
        return false;
    }

    public boolean isNull() {
        return false;
    }

    public boolean isClassOrNull() {
        return false;
    }

    /**
     * Returns the same object, as type ClassType, if possible. Throws
     * ContextualError(errorMessage, l) otherwise.
     *
     * Can be seen as a cast, but throws an explicit contextual error when the
     * cast fails.
     */
    public ClassType asClassType(String errorMessage, Location l)
            throws ContextualError {
        throw new ContextualError(errorMessage, l);
    }
    
    /**
     * Booléen indiquant si this est un sous-type de type1, ce qui est vrai dans deux cas:
     * - si type1 = float et this = int
     * - si type1 est une classe et que this hérite à un certain degré de cette classe,
     * ou que this est de type null
     * @param envType
     * @param type1
     * @return un booléen
     * @see fr.ensimag.deca.tree.abstractExp.verifyRvalue
     */
    public boolean isSubTypeOf(Type type1, Location loc) throws ContextualError{
    	if ((type1.isFloat()) && (this.isInt())) {
    		return true;
    	} else if (type1.isClassOrNull()){
    		if (this.isNull()) {
    			return true;
    		} else if (this.isClass()) {
	    		String nl = " n'est pas une classe";
	    		ClassType type1Class = type1.asClassType(type1.toString() + nl, loc);
	    		ClassType thisClass = this.asClassType(this.toString() + nl, loc);
	        	return thisClass.isSubClassOf(type1Class);
    		}
    	}
    	return false;
    }

}
