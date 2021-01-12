package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl01
 * @date 01/01/2021
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    /** Réécrit dans modulo car c'est le seul différent (ne prend que des int)
     * 
     * Pour débogguer plus facilement, si un ou plusieurs types sont incompatibles,
     * on affichage le ou les coupables.
     */
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	AbstractExpr leftOp = this.getLeftOperand();
    	AbstractExpr rightOp = this.getRightOperand();
    	Type leftType = leftOp.verifyExpr(compiler, localEnv, currentClass);
    	Type rightType = rightOp.verifyExpr(compiler, localEnv, currentClass);
    	if ((leftType.isInt()) && (rightType.isInt())) {
    		this.setType(leftType);
    		return leftType;
    	} else if ((leftType.isInt()) && (rightType.isFloat())) {
    		ConvFloat conv = new ConvFloat(leftOp);
    		this.setLeftOperand(conv);
    		this.setType(conv.verifyExpr(compiler, localEnv, currentClass));
    		return rightType;
	    } else if ((leftType.isFloat()) && (rightType.isInt())) {
	    	ConvFloat conv = new ConvFloat(rightOp);
	    	this.setRightOperand(conv);
    		this.setType(conv.verifyExpr(compiler, localEnv, currentClass));
	    	this.setType(leftType);
			return leftType;
		} else if ((leftType.isFloat()) && (rightType.isFloat())) {
			this.setType(rightType);
			return rightType;
		}
    	String coupableGauche = "";
    	if ((!leftType.isFloat()) && (!leftType.isInt())) {
    		coupableGauche = "L'opérande gauche est de type " + leftType.toString() + ".";
    	}
    	String coupableDroit = "";
    	if ((!rightType.isFloat()) && (!rightType.isInt())) {
    		coupableDroit = " L'opérande droite est de type " + rightType.toString() + ".";
    	}
    	throw new ContextualError(coupableGauche + coupableDroit +
    			" Or les types des opérandes doivent être 'int' ou 'float' pour l'opérateur " + 
    			this.getOperatorName(), this.getLocation());
    }
}
