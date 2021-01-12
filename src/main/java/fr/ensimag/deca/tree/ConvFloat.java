package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ContextualError;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl01
 * @date 01/01/2021
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	assert this.getOperand().getType().isInt();
    	Type floatType = compiler.getEnvType().get(compiler.getSymbTb().create("float")).getType();
    	return floatType;
    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
