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
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	AbstractExpr leftOp = this.getLeftOperand();
    	AbstractExpr rightOp = this.getRightOperand();
    	leftOp.verifyExpr(compiler, localEnv, currentClass);
    	rightOp.verifyExpr(compiler, localEnv, currentClass);
    	Type leftType = leftOp.getType();
    	Type rightType = rightOp.getType();
    	if ((leftType.isBoolean()) && (rightType.isBoolean())) {
    		return leftType;
    	}
    	String coupableGauche = "";
    	if (!leftType.isBoolean()) {
    		coupableGauche = "Type opérateur gauche " + leftType.toString();
    	}
    	String coupableDroit= "";
    	if (!rightType.isBoolean()) {
    		coupableGauche = " Type opérateur droit " + rightType.toString();
    	}
    	throw new ContextualError(coupableGauche + coupableDroit +
    			" Type attendu: 'boolean'", this.getLocation());
    }

}
