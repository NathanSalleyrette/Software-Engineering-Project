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
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	AbstractExpr leftOp = this.getLeftOperand();
    	AbstractExpr rightOp = this.getRightOperand();
    	Type leftType = leftOp.verifyExpr(compiler, localEnv, currentClass);
    	Type rightType = rightOp.verifyExpr(compiler, localEnv, currentClass);
        if (((leftType.isInt()) || (leftType.isFloat())) && ((rightType.isInt()) 
        		|| rightType.isFloat())) {
        			// Au moins je suis sûr de récupérer le type Boolean comme ça
        			Type returnType = compiler.getEnvType().get(compiler.getSymbTb().create("boolean")).getType();
        			this.setType(returnType);
        			return returnType;
        }  	
        throw new ContextualError("(3.33) Opérandes de type " + leftType.toString() + ", " +
        			rightType.toString() + ", attendu: 'int' ou 'float'", this.getLocation());
    }
}

