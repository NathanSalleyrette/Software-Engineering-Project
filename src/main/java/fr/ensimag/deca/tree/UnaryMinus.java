package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.deca.codegen.Error;
import fr.ensimag.ima.pseudocode.instructions.OPP;

/**
 * @author gl01
 * @date 01/01/2021
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        if ((this.getOperand().getType().isFloat()) || (this.getOperand().getType().isInt())) {
        	return this.getOperand().getType();
        }
        throw new ContextualError("Type opérande : " + this.getOperand().getType().toString() +
        		", attendu : 'int' ou 'float' pour l'opérateur" + this.getOperatorName(), this.getLocation());
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        compiler.addInstruction(new OPP(this.getOperand().dval(), Register.getR(compiler.getCurrentRegister())));
        if (this.getType().isFloat()) {
            Error.instanceError(compiler, "debordement_flottant");
        }
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }

}
