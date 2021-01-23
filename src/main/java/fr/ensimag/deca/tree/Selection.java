package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.tools.IndentPrintStream;

import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.DVal;

public class Selection extends AbstractLValue{
	
	private AbstractExpr obj;
	private AbstractIdentifier field;
	
	public Selection(AbstractExpr obj, AbstractIdentifier field) {
		Validate.notNull(obj);
		Validate.notNull(field);
		this.obj = obj;
		this.field = field;
	}

	
	public AbstractExpr getObj() {
		return obj;
	}


	public void setObj(AbstractExpr obj) {
		this.obj = obj;
	}


	public AbstractIdentifier getField() {
		return field;
	}


	public void setField(AbstractIdentifier field) {
		this.field = field;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		Type leftType = this.getObj().verifyExpr(compiler, localEnv, currentClass);
		if (leftType == null) throw new ContextualError("(3.65) Le membre gauche n'a pas de type", this.getLocation());
		ClassType objType = leftType.asClassType(
				"(3.65) Le membre gauche n'est pas de type 'class'", this.getLocation());
		ClassDefinition objDef = (ClassDefinition) compiler.getEnvType().get(objType.getName());
		if (objDef.getMembers().get(this.getField().getName()) == null) {
			throw new ContextualError("(3.65) " + this.getField().getName().toString() + 
					" n'est pas un attribut de la classe " + objType.toString(),
					this.getLocation());
		}
		FieldDefinition fieldDef = 
				objDef.getMembers().get(this.getField().getName()).asFieldDefinition(
					"(3.65) le champ n'est pas un attribut de classe", this.getLocation());
		/* Si l'attribut est protégé, il faut qu'on soit dans la classe qui le contient
		 * ou une de ses filles
		 */
		/*
		if ((fieldDef.getVisibility() == Visibility.PROTECTED)
				&& ((currentClass == null) || ((obj.isShallow(localEnv))||
				((currentClass != fieldDef.getContainingClass())
				&& (!currentClass.getType().isSubClassOf(fieldDef.getContainingClass().getType())))))) {
			throw new ContextualError("(3.65) L'attribut est protégé", this.getLocation());
		}
		*/
		if ((fieldDef.getVisibility() == Visibility.PROTECTED) && ((currentClass == null) ||
				(((currentClass.getType().isSubClassOf(fieldDef.getContainingClass().getType()) ||
						currentClass == fieldDef.getContainingClass())
						&& ((objType.isSubClassOf(currentClass.getType()) ||
								(objDef == currentClass))))))) {
			throw new ContextualError("(3.65) L'attribut est protégé", this.getLocation());
		}
 		Type fieldType = this.getField().verifyExpr(compiler, objDef.getMembers(), objDef);
		this.setType(fieldType);
		return fieldType;
	}

	@Override
	public void decompile(IndentPrintStream s) {
        obj.decompile(s);
        s.print(".");
        field.decompile(s);	
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
        obj.prettyPrint(s, prefix, false);
        field.prettyPrint(s, prefix, true);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
        obj.iter(f);
        field.iter(f);
	}

	@Override
    protected void codeGenInst(DecacCompiler compiler) {
		compiler.addInstruction(new LOAD(this.dval(compiler), Register.getR(compiler.getCurrentRegister())));
	}
	
	@Override
	protected void boolCodeGen(DecacCompiler compiler, boolean branch, Label tag) {
		compiler.addInstruction(new LOAD(this.dval(compiler), Register.R0));
        compiler.addInstruction(new CMP(0, Register.R0));
        if (branch) {
            compiler.addInstruction(new BNE(tag));
        } else {
            compiler.addInstruction(new BEQ(tag));
        }
	}

    @Override
    public DVal dval(DecacCompiler compiler) {
		obj.codeGenInst(compiler);
		// L'adresse dans le tas de obj est stockée dans le registre courant
		if (!compiler.getCompilerOptions().getNoCheck()) {
			compiler.addInstruction(new CMP(new NullOperand(), Register.getR(compiler.getCurrentRegister())));
			Label label = new Label("dereferencement.null");
			compiler.addInstruction(new BEQ(label));
			compiler.addError(label);
		}
        return new RegisterOffset(field.getFieldDefinition().getIndex(), Register.getR(compiler.getCurrentRegister()));
	}
	
	@Override
	public boolean isSelection() {
		return true;
	}
}
