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
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        this.getOperand().verifyCondition(compiler, localEnv, currentClass);
        if (this.getOperand().getType().isBoolean()) {
        	return this.getOperand().getType();
        }
        throw new ContextualError("Type opérande : " + this.getOperand().getType().toString() +
        		", attendu : 'boolean' pour l'opérateur " + this.getOperatorName(), this.getLocation());
    }


    @Override
    protected String getOperatorName() {
        return "!";
    }
}
