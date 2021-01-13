package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tree.AbstractOpBool;

/**
 *
 * @author gl01
 * @date 01/01/2021
 */
public abstract class AbstractOpExactCmp extends AbstractOpCmp {

    public AbstractOpExactCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	try {
    		return super.verifyExpr(compiler, localEnv, currentClass);
    	} catch(ContextualError e) {
    		AbstractExpr leftOp = this.getLeftOperand();
        	AbstractExpr rightOp = this.getRightOperand();
        	Type leftType = leftOp.verifyExpr(compiler, localEnv, currentClass);
        	Type rightType = rightOp.verifyExpr(compiler, localEnv, currentClass);
    		if (((leftType.isClass()) || (leftType.isNull())) &&
    				((rightType.isClass()) || (rightType.isNull()))) {
    			Type returnType = compiler.getEnvType().get(compiler.getSymbTb().create("boolean")).getType();
    			this.setType(returnType);
    			return returnType;
    		} else if ((leftType.isBoolean()) && (rightType.isBoolean())) {
    			this.setType(leftType);
        		return leftType;
        	}
    		throw new ContextualError("Opérandes de type " + leftType.toString() + ", " +
    				rightType + ", attendu : (int|float, int|float) ou (class|null, class|null), ou (bool, bool)",
    				this.getLocation());
    	}
    }

}
