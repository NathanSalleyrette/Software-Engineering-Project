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
    		this.setType(leftOp.getType());
    		return leftOp.getType();
    	}
    	throw new ContextualError(this.typesOp() + 
    			". Attendu : 'int' pour l'opérateur %", this.getLocation());
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }
}
