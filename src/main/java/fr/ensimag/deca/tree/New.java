package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.deca.codegen.Error;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;

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
		if (!this.getType().isClass()) throw new ContextualError("(3.42) " + 
				this.getType().toString() + " n'est pas un type de classe", this.getLocation());
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

	@Override
	protected void codeGenInst(DecacCompiler compiler) {
		compiler.addComment("new ligne " + this.getLocation().getLine());
		GPRegister reg = Register.getR(compiler.getCurrentRegister());
		compiler.addInstruction(new NEW(1 + newClass.getClassDefinition().getNumberOfFields(), reg));
		Error.instanceError(compiler, "tas_plein");
		// Adresse de la table des méthodes
		compiler.addInstruction(new LEA(newClass.getClassDefinition().getAddress(), Register.R0));
		compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, reg)));
		if (newClass.getClassDefinition().getNumberOfFields() > 0) {
			// Sauvegarde du registre
			compiler.addInstruction(new PUSH(reg));
			// Initialisation des champs
			compiler.addInstruction(new BSR(new LabelOperand(new Label("init." + newClass.getName()))));
			// Restauration du registre
			compiler.addInstruction(new POP(reg));
		}
	}
}
