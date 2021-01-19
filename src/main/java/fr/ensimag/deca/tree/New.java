package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

public class New extends AbstractExpr {
	
	private AbstractIdentifier newClass;
		
	public New(AbstractIdentifier newClass) {
		this.newClass = newClass;
	}
	
	public AbstractIdentifier getNewClass() {
		return this.newClass;
	}
	
	public void setNewClass(AbstractIdentifier newClass) {
		this.newClass = newClass;
	}
	
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp envExp,
			ClassDefinition currentCLass) throws ContextualError{
		this.setType(getNewClass().verifyType(compiler));
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
		s.print("();");
	}
}
