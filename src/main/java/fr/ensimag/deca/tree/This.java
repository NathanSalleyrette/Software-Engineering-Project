package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class This extends AbstractExpr {

	private Boolean impl;

	public This(Boolean ecrit) {
		this.impl = ecrit;
	}
	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		if (currentClass == null) throw new ContextualError("(3.43) 'this' ne peut être utilisé dans le main",
				this.getLocation());
		this.setType(currentClass.getType());
		return this.getType();
	}

	@Override
	public void decompile(IndentPrintStream s) {
		if (!impl) {
			s.print("this");
		}
		
	}
	
	public boolean getImpl() {
		return impl;
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DVal dval(DecacCompiler compiler) {
		return new RegisterOffset(-2, Register.LB);
	}

	@Override
	protected void codeGenInst(DecacCompiler compiler) {
		compiler.addInstruction(new LOAD(this.dval(compiler), Register.getR(compiler.getCurrentRegister())));
	}

	@Override
	public boolean isAtomic() {
		return true;
	}

}
