package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;

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
		this.setType(compiler.getEnvType().get(compiler.getSymbTb().create("boolean")).getType());
		return this.getType();
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
	
	@Override
	protected void boolCodeGen(DecacCompiler compiler, boolean branch, Label tag) {
		compiler.addComment("instanceof ligne " + this.getLocation().getLine());
		// On charge l'adresse dans le tas de l'objet dans le registre courant
		GPRegister reg = Register.getR(compiler.getCurrentRegister());
		expr.codeGenInst(compiler);
		// On stocke l'adresse de la table des méthodes de la classe dans R0
		compiler.addInstruction(new LEA(type.getClassDefinition().getAddress(), Register.R0));

		Label debut = new Label("InstanceOf_Debut." + compiler.getNbLabel());
		Label cond = new Label("InstanceOf_Cond." + compiler.getNbLabel());
		Label vrai = new Label("InstanceOf_True." + compiler.getNbLabel());
		Label fin = new Label("InstanceOf_Fin." + compiler.getNbLabel());
		compiler.incrNbLabel();
		compiler.addInstruction(new BRA(cond));
        compiler.addLabel(debut);
		compiler.addInstruction(new LOAD(new RegisterOffset(0, reg), reg));
		// On compare pour voir si c'est la même classe
		compiler.addInstruction(new CMP(Register.R0, reg));
		compiler.addInstruction(new BEQ(vrai));
		compiler.addLabel(cond);
		// Condition : La valeur dans reg n'est pas null
		compiler.addInstruction(new CMP(new NullOperand(), reg));
		compiler.addInstruction(new BNE(debut));
		// On arrive ici si le instanceof est faux
		new Not(new BooleanLiteral(true)).boolCodeGen(compiler, branch, tag);
		compiler.addInstruction(new BRA(fin));
		//
		compiler.addLabel(vrai);
		// On arrive ici si le instanceof est vrai
		if (branch) compiler.addInstruction(new BRA(tag));
		//
		compiler.addLabel(fin);
	}
}
