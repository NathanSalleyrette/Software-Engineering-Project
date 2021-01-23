package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.NullType;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.deca.tools.IndentPrintStream;

public class Null extends AbstractExpr{

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		/* Comme pour les string, ce n'est pas un type prédéfini.
		 * On regarde donc s'il existe déjà à chaque fois qu'on
		 * crée une instance null, et on le crée si ce n'es pas
		 * le cas.
		 */
		Symbol nullSymb = compiler.getSymbTb().create("null");
		Type nullType = new NullType(nullSymb);
		TypeDefinition nullDef = compiler.getEnvType().get(nullSymb);
		if (nullDef == null) {
			compiler.getEnvType().put(nullSymb, new TypeDefinition(nullType, this.getLocation()));
		}
		this.setType(nullType);
		return nullType;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print("null");
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		//leaf
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		//leaf
		
	}

	@Override
    protected void codeGenInst(DecacCompiler compiler) {
        compiler.addInstruction(new LOAD(this.dval(compiler), Register.getR(compiler.getCurrentRegister())));
    }

    @Override
    public DVal dval(DecacCompiler compiler) {
        return new NullOperand();
    }
}
