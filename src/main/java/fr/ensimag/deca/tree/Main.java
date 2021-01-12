package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import java.util.Iterator;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.context.VoidType;

/**
 * @author gl01
 * @date 01/01/2021
 */
public class Main extends AbstractMain {
    private static final Logger LOG = Logger.getLogger(Main.class);
    
    private ListDeclVar declVariables;
    private ListInst insts;
    public Main(ListDeclVar declVariables,
            ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify Main: start");
        // A FAIRE: Appeler méthodes "verify*" de ListDeclVarSet et ListInst.
        // Vous avez le droit de changer le profil fourni pour ces méthodes
        // (mais ce n'est à priori pas nécessaire).
        // Passe 3
        // Les déclarations de variabes d'abord
        EnvironmentExp emptyEnv = new EnvironmentExp(null); // Arrêtez-moi si je fais une bêtise
        this.declVariables.verifyListDeclVariable(compiler, emptyEnv, null);
        // La liste d'instructions ensuite
        VoidType voidType = new VoidType(compiler.getSymbTb().create("void"));
        this.insts.verifyListInst(compiler, emptyEnv, null, voidType);
        LOG.debug("verify Main: end");
    }

    @Override
    protected void codeGenMain(DecacCompiler compiler) {
        // Traitement des déclarations de variables.
        setnbGlobVar(declVariables.size());
        Iterator<AbstractDeclVar> iter = declVariables.iterator();
        int indexGB = 2;
        while (iter.hasNext()) {
            AbstractDeclVar declVar = iter.next();
            // TODO : initialisation
            // LOAD dans R2 compiler.addInstruction(new );
            //compiler.addInstruction(new STORE(getR(2), QQCH.getOperand()));
            ++indexGB;
        }
        compiler.addComment("Beginning of main instructions:");
        insts.codeGenListInst(compiler);
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
