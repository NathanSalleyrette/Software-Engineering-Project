package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class This extends AbstractExpr {

	private Boolean impl;

	public This(Boolean ecrit) {
		this.impl = ecrit;
	}
	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		// TODO Auto-generated method stub
		return null;
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

}
