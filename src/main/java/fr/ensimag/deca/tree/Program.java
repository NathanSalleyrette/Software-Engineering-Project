package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ClassCodeGen;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl01
 * @date 01/01/2021
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");      
        // Passe 1
        // Liste de déclaration de classes
        this.getClasses().verifyListClass(compiler);
        // Passe 2
        this.getClasses().verifyListClassMembers(compiler);
        this.getClasses().verifyListClassBody(compiler);
        // Passe 3
        // Main
        this.getMain().verifyMain(compiler);
        LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        compiler.addComment("Construction des tables des méthodes");
        // Passe 1
        ClassCodeGen.buildTable(compiler, classes);
        // Passe 2
        compiler.addComment("Main program");
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
        // Entête : TSTO + ADDSP
        int stackSize = main.getnbGlobVar() + compiler.getMethTableSize();
        compiler.addFirst(new Line(new ADDSP(stackSize)));
        if (!compiler.getCompilerOptions().getNoCheck()) {
            Label pilePleine = new Label("pile_pleine");
            compiler.addError(pilePleine);
            compiler.addFirst(new Line(new BOV(pilePleine)));
            compiler.addFirst(new Line(new TSTO(stackSize + compiler.getMaxTemp())));
        }
        compiler.reinitCounts();
        ClassCodeGen.buildMethodsAndFields(compiler, classes);
        // Messages d'erreurs
        compiler.addComment("Erreurs");
        compiler.writeErrors();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
