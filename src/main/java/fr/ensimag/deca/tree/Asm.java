package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.insutrctionStringLine;

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
		
		s.print("asm(");
		listeInstructions.decompile(s);
		//s.print(listeInstructions.getValue());
		s.print(");");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		this.listeInstructions.prettyPrint(s,prefix,Boolean.TRUE);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		listeInstructions.iter(f);
	}
	@Override
	protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
			Type returnType) throws ContextualError {
		this.listeInstructions.verifyExpr(compiler, localEnv, currentClass);
		
	}
	@Override
	protected void codeGenBody(DecacCompiler compiler) {
		String[] lignes = this.listeInstructions.getValue().split("\\n");//chaque ligne est une instruction
		for(int i = 0 ; i<lignes.length;++i) {
			compiler.addInstructionString(lignes[i]);
		}
	}

	@Override
	public ListDeclVar getListDeclVar() {
		return new ListDeclVar();
	}
	
	@Override
	public boolean isASM() {
		return true;
	}
}
