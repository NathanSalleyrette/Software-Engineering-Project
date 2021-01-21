package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.BRA;

import java.io.PrintStream;

/**
 *
 * @author gl01
 * @date 01/01/2021
 */
public class BooleanLiteral extends AbstractExpr {

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	Symbol boolSymb = compiler.getSymbTb().create(this.toString());
    	Type typeBool = compiler.getEnvType().get(boolSymb).getType();
    	this.setType(typeBool);
    	return typeBool;
    	}

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        compiler.addInstruction(new LOAD(this.dval(compiler), Register.getR(compiler.getCurrentRegister())));
    }
    
    @Override
    public DVal dval(DecacCompiler compiler) {
        // false est codé par #0, true par #1 (arbitraire, doit être différent de #0)
        if (!this.getValue()) {
            return new ImmediateInteger(0);
        } else {
            return new ImmediateInteger(1);
        }
    }

    @Override
    protected void boolCodeGen(DecacCompiler compiler, boolean branch, Label tag) {
        if (this.getValue()) {
            if (branch) compiler.addInstruction(new BRA(tag));
        } else {
            new Not(new BooleanLiteral(true)).boolCodeGen(compiler, branch, tag);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(this.getValue()));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + this.getValue() + ")";
    }

    public String toString() {
    	return "boolean";
    }

    @Override
    public boolean isAtomic() {
        return true;
    }
}
