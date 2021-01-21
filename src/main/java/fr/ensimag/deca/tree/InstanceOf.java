package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

public class InstanceOf extends AbstractExpr{
	
	private AbstractExpr expr;
	private AbstractIdentifier type;
	
	public InstanceOf (AbstractExpr expr, AbstractIdentifier type) {
		this.expr = expr;
		this.type = type;
	}

	public AbstractExpr getExpr() {
		return expr;
	}

	public void setExpr(AbstractExpr expr) {
		this.expr = expr;
	}

	public AbstractIdentifier getTypeIdentifier() {
		return type;
	}

	public void setType(AbstractIdentifier type) {
		this.type = type;
	}
	
	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
			ClassDefinition currenClass) throws ContextualError {
		Type exprType = expr.verifyExpr(compiler, localEnv, currenClass);
		Type comparedType = type.verifyType(compiler);
		if (!exprType.isClassOrNull()) {
			throw new ContextualError("(3.40) Type gauche : " + exprType.toString() +
					", attendu : 'class' ou 'null'", this.getLocation());
		}
		if (!comparedType.isClass()) {
			throw new ContextualError("(3.40) Type droit : " + comparedType.toString() +
					", attendu : 'class'", this.getLocation());
		}
		return compiler.getEnvType().get(compiler.getSymbTb().create("boolean")).getType();
	}
	
	public void decompile(IndentPrintStream s) {
		s.print("(");
		this.type.decompile();
		s.print(" instanceof ");
		this.expr.decompile();
		s.print(") ");
	}
	
	public void iterChildren(TreeFunction f) {
		expr.iterChildren(f);
		type.iterChildren(f);
	}
	
	public void prettyPrintChildren(PrintStream s, String prefix) {
		this.expr.prettyPrint(s, prefix, false);
		this.type.prettyPrint(s, prefix, true);
	}
	
}
