package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.deca.codegen.Error;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
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
            ClassDefinition currentClass) {
    	assert this.getOperand().getType().isInt();
    	Type floatType = compiler.getEnvType().get(compiler.getSymbTb().create("float")).getType();
    	this.setType(floatType);
    	return floatType;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        this.getOperand().codeGenInst(compiler);
        compiler.addInstruction(new FLOAT(this.getOperand().dval(compiler), Register.getR(compiler.getCurrentRegister())));
        Error.instanceError(compiler, "debordement_flottant");
    }

    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
