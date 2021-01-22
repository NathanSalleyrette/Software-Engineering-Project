package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.Iterator;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BSR;

public class MethodCall extends AbstractExpr{
    
	private AbstractExpr obj;
	private AbstractIdentifier meth;
	private ListExpr params;
    
    
    public MethodCall(AbstractExpr obj, AbstractIdentifier meth, ListExpr params) {
        Validate.notNull(obj);
        Validate.notNull(meth);
        Validate.notNull(params);
        this.obj = obj;
        this.meth = meth;
        this.params = params;
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
    	obj.decompile(s);
    	if ((obj instanceof This)) {
    		This objet =(This)obj;
    		if (!objet.getImpl()) {	
    			s.print(".");
    		}
    	} else {
    		s.print(".");
    	}
        meth.decompile(s);
        s.print("(");
        params.decompile(s);
        s.print(")");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        obj.iter(f);
        meth.iter(f);
        params.iter(f);
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        obj.prettyPrint(s, prefix, false);
        meth.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, true);
    }

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		Type objType = this.obj.verifyExpr(compiler, localEnv, currentClass);
		if (objType == null) throw new ContextualError("(3.71) Le membre gauche n'a pas de type",
				this.getLocation());
		if (!objType.isClass()) throw new ContextualError("(3.71) Le membre gauche n'est pas de type 'class'",
				this.getLocation());
		ClassDefinition objDef = (ClassDefinition) compiler.getEnvType().get(objType.getName());
		if (objDef.getMembers().get(this.meth.getName()) == null) {
			throw new ContextualError("(3.71) " + this.meth.getName().toString() + 
					" n'est pas une méthode de la classe " + objType.toString(),
					this.getLocation());
		}
		MethodDefinition methodDef = 
				objDef.getMembers().get(this.meth.getName()).asMethodDefinition(
					"(3.71) Le champ n'est pas une méthode", this.getLocation());
		Signature sig = methodDef.getSignature();
		if (this.params.getList().size() != sig.size()) throw new ContextualError(
				"(3.74) Le nombre de paramètres de correspond pas à celui de la méthode",
				this.getLocation());
		for (int i = 0; i < sig.size(); i++) {
			AbstractExpr param = this.params.getList().get(i);
			param.verifyExpr(compiler, localEnv, currentClass);
			if ((sig.paramNumber(i) != param.getType()) && 
					(!param.getType().isSubTypeOf(sig.paramNumber(i), getLocation()))) 
				throw new ContextualError(
					"(3.74) La signature ne correspond pas aux paramètres", this.getLocation());
		}
		this.meth.setDefinition(methodDef);
		this.setType(methodDef.getType());
		return this.getType();
	}

	@Override
	protected void codeGenInst(DecacCompiler compiler) {
		int stackShift = params.size() + 1;
		GPRegister reg = Register.getR(compiler.getCurrentRegister());
		compiler.addInstruction(new ADDSP(stackShift));
		// On empile le paramètre implicite
		obj.codeGenInst(compiler);
		DAddr addrImplicit = new RegisterOffset(0, Register.SP);
		compiler.addInstruction(new STORE(reg, addrImplicit));
		// On empile les paramètres explicites
		int index = 0;
		Iterator<AbstractExpr> iterParam = params.iterator();
		while (iterParam.hasNext()) {
			AbstractExpr param = iterParam.next();
			param.codeGenInst(compiler);
			compiler.addInstruction(new STORE(reg, new RegisterOffset(--index, Register.SP)));
		}
		// On récupère le paramètre implicite
		compiler.addInstruction(new LOAD(addrImplicit, reg));
		if (!compiler.getCompilerOptions().getNoCheck()) {
			compiler.addInstruction(new CMP(new NullOperand(), reg));
			Label dereference = new Label("dereferencement.null");
			compiler.addInstruction(new BEQ(dereference));
			compiler.addError(dereference);
		}
		// On récupère l'adresse de la table des méthodes
		compiler.addInstruction(new LOAD(new RegisterOffset(0, reg), reg));
		compiler.addInstruction(new BSR(new RegisterOffset(meth.getMethodDefinition().getIndex(), reg)));
		compiler.addInstruction(new SUBSP(stackShift));
		if (!meth.getMethodDefinition().getType().isBoolean()) { // Si cela renvoie un booleen sa valeur dans R0 suffit
			compiler.addInstruction(new LOAD(Register.R0, reg));
		}
	}

	@Override
	protected void boolCodeGen(DecacCompiler compiler, boolean branch, Label tag) {
		this.codeGenInst(compiler);
		compiler.addInstruction(new CMP(0, Register.R0));
        if (branch) {
            compiler.addInstruction(new BNE(tag));
        } else {
            compiler.addInstruction(new BEQ(tag));
        }
	}
}
