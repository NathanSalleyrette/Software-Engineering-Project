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
    	this.getValeurRetour().verifyCondition(compiler, localEnv, currentClass);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        getValeurRetour().decompile(s);
        s.println(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        valeurRetour.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        valeurRetour.prettyPrint(s, prefix, false);
    }

}

