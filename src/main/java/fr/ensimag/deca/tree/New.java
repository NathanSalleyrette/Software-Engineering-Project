package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

public class New extends AbstractExpr {

	private String newClassName;
	
	private AbstractIdentifier newClass;
		
	public New(String newClassName) {
		this.newClassName = newClassName;
	}
	
	public String getNewClassName() {
		return this.newClassName;
	}
	
	public AbstractIdentifier getNewClass() {
		return this.newClass;
	}
	
	public void setNewClass(String name, DecacCompiler compiler) {
		this.newClass = new Identifier(compiler.getSymbTb().create(name));
	}
	
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp envExp,
			ClassDefinition currentCLass) throws ContextualError{
		this.setNewClass(this.newClassName, compiler);
		this.setType(getNewClass().verifyType(compiler));
		this.getNewClass().setDefinition(compiler.getEnvType().get(this.getNewClass().getName()));
		return this.getType();
	}
	
	public void prettyPrintChildren(PrintStream s, String prefix) {
		this.getNewClass().prettyPrint(s, prefix, true);
	}
	
	public void iterChildren(TreeFunction f) {
		newClass.iter(f);
	}
	
	public void decompile(IndentPrintStream s) {
		s.print(" new ");
		newClass.decompile(s);
		s.print("()");
	}
}
