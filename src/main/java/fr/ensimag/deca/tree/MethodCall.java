package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

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
		for (int i = 0; i < sig.size(); i++) {
			AbstractExpr param = this.params.getList().get(i);
			param.verifyExpr(compiler, localEnv, currentClass);
			if (sig.paramNumber(i) != param.getType()) throw new ContextualError(
					"(3.74) La signature ne correspond pas aux paramètres", this.getLocation());
		}
		this.setType(methodDef.getType());
		return this.getType();
	}
}
