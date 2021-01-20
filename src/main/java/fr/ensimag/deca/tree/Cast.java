package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

public class Cast extends AbstractExpr {
	
	private AbstractIdentifier type;
	private AbstractExpr expr;
	
	public Cast(AbstractIdentifier type, AbstractExpr expr) {
		this.type = type;
		this.expr = expr;
	}
	
	public AbstractIdentifier getTypeIdentifier() {
		return this.type;
	}
	
	public void setType(AbstractIdentifier type) {
		this.type = type;
	}

	public AbstractExpr getExpr() {
		return expr;
	}

	public void setExpr(AbstractExpr expr) {
		this.expr = expr;
	}

	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
			ClassDefinition currentClass) throws ContextualError {
		this.type.verifyType(compiler);
		Type typeExpr = this.expr.verifyExpr(compiler, localEnv, currentClass);
		if ((this.assignCompatible(typeExpr, type.getType())) ||
				this.assignCompatible(type.getType(), typeExpr)) {
			this.setType(type.getType());
			return this.getType();
		}
		throw new ContextualError("(3.39) Cast impossible de " + typeExpr.toString() +
				" vers " + this.type.getType().toString(), this.getLocation());
	}
	
	public void decompile(IndentPrintStream s) {
		s.print("(");
		this.type.decompile();
		s.print(")(");
		this.expr.decompile();
		s.print(")");
	}
	
	public void iterChildren(TreeFunction f) {
		type.iterChildren(f);
		expr.iterChildren(f);
	}
	
	public void prettyPrintChildren(PrintStream s, String prefix) {
		this.type.prettyPrint(s, prefix, false);
		this.expr.prettyPrint(s, prefix, true);
	}
}
