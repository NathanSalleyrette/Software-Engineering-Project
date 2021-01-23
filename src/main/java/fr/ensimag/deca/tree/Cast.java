package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.POP;

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
		if (((this.assignCompatible(typeExpr, type.getType())) ||
				this.assignCompatible(type.getType(), typeExpr)) &&
				(!type.getType().isVoid())) {
			this.setType(type.getType());
			return this.getType();
		}
		throw new ContextualError("(3.39) Cast impossible de " + typeExpr.toString() +
				" vers " + this.type.getType().toString(), this.getLocation());
	}
	
	public void decompile(IndentPrintStream s) {
		s.print("(");
		this.type.decompile(s);
		s.print(")(");
		this.expr.decompile(s);
		s.print(")");
	}
	@Override
	protected void codeGenInst(DecacCompiler compiler) {

		compiler.addComment("cast  " + this.expr.getType() + " vers " + this.getType() + " ligne " + this.getLocation().getLine());
		this.expr.codeGenInst(compiler);
		GPRegister reg = Register.getR(compiler.getCurrentRegister());
		if (this.type.getType().isInt() && this.expr.getType().isFloat()) { // (int) <float>
			compiler.addInstruction(new INT(reg, reg));

		} else if (this.type.getType().isFloat() && this.expr.getType().isInt()) { // (float) <int>
			compiler.addInstruction(new FLOAT(reg, reg));

		} else if (!this.type.getType().sameType(this.expr.getType())) {
			Label fin = new Label("Cast_Fin." + compiler.getNbLabel());
			compiler.incrNbLabel();
			compiler.addInstruction(new CMP(new NullOperand(), reg)); // Cast d'un object de valeur null
			compiler.addInstruction(new BEQ(fin));
			AbstractExpr condition = new InstanceOf(expr, type);
			condition.setLocation(this.getLocation());
			compiler.incrNbTemp();
			compiler.addInstruction(new PUSH(reg)); // Sauvegarde de la valeur de l'expression
			condition.boolCodeGen(compiler, true, fin);
			// Le cast est impossible
			Label impossibleCast = new Label("cast_impossible");
			compiler.addInstruction(new BRA(impossibleCast));
			compiler.addError(impossibleCast);
			// Le cast est possible;		
			compiler.addLabel(fin);
			compiler.addInstruction(new POP(reg)); // Resauration de la valeur de l'expression
			compiler.decrNbTemp();
		}
		// Si on cast vers le même type, on ne fait rien de plus
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
