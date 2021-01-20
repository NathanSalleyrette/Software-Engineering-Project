package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;


/**
 *
 * @author gl01
 * @date 01/01/2021
 */
public class Return extends AbstractInst {
    private AbstractExpr valeurRetour;

    public AbstractExpr getValeurRetour() {
        return valeurRetour;
    }


    public Return(AbstractExpr valeurRetour) {
        Validate.notNull(valeurRetour);
        this.valeurRetour = valeurRetour;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {

    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
    	Type instType = this.getValeurRetour().verifyExpr(compiler, localEnv, currentClass);
    	if (instType.isVoid()) throw new ContextualError("(3.24) On ne peut retourner un type 'void'",
    			this.getLocation());
    	// On vérifie la compatibilité du type de retour
    	if (instType != returnType) {
    		throw new ContextualError("(2.7) Type de retour effectif : " + instType.toString()+
    				", déclaré : " + returnType.toString(), this.getLocation());
    	}
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        getValeurRetour().decompile(s);
        s.print(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        valeurRetour.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        valeurRetour.prettyPrint(s, prefix, true);
    }

}

