package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class Asm extends AbstractMethodBody{
	private StringLiteral listeInstructions;
	 public Asm(StringLiteral listeInstructions) {
		this.setListeinstructions(listeInstructions);
	}
	public void setListeinstructions(StringLiteral listeInstructions) {
		this.listeInstructions = listeInstructions;
	}


	@Override
	public void decompile(IndentPrintStream s) {
		s.print("asm");
		s.print("(");
		listeInstructions.decompile(s);
		s.print(")");
		s.print(";");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// Il n'y a pas d'enfant
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		listeInstructions.iter(f);
	}
	@Override
	protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
			Type returnType) throws ContextualError {
		// TODO Auto-generated method stub
		
	}

	




}
