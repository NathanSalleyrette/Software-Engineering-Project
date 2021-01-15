package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.RINT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.deca.codegen.Error;

/**
 *
 * @author gl01
 * @date 01/01/2021
 */
public class ReadInt extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type intType = compiler.getEnvType().get(compiler.getSymbTb().create("int")).getType();
        this.setType(intType);
        return intType;
    }

    protected void codeGenInst(DecacCompiler compiler) {
        compiler.addInstruction(new RINT());
        Error.instanceError(compiler, "io_error");
        compiler.addInstruction(new LOAD(Register.R1, Register.getR(compiler.getCurrentRegister())));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readInt()");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

}
