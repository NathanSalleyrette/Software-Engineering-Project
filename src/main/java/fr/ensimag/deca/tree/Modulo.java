package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl01
 * @date 01/01/2021
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	AbstractExpr leftOp = this.getLeftOperand();
    	AbstractExpr rightOp = this.getRightOperand();
    	leftOp.verifyExpr(compiler, localEnv, currentClass);
    	rightOp.verifyExpr(compiler, localEnv, currentClass);
    	if ((leftOp.getType().isInt()) && (rightOp.getType().isInt())) {
    		return leftOp.getType();
    	}
    	String coupableGauche = "(3.33) ";
    	if (!leftOp.getType().isInt()) {
    		coupableGauche = "L'opérande gauche est de type " + leftOp.getType().toString() + ".";
    	}
    	String coupableDroit = "";
    	if (!rightOp.getType().isInt()) {
    		coupableDroit = "L'opérande droite est de type " + rightOp.getType().toString() + ".";
    	}
    	throw new ContextualError(coupableGauche + coupableDroit + 
    			" Or les opérandes d'un modulo doivent être de type 'int'", this.getLocation());
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }
}
