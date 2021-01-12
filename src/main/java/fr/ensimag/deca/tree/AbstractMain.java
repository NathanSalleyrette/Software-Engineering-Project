package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

/**
 * Main block of a Deca program.
 *
 * @author gl01
 * @date 01/01/2021
 */
public abstract class AbstractMain extends Tree {

    protected abstract void codeGenMain(DecacCompiler compiler);

    private int nbGlobVar = 0;
    public int getnbGlobVar() {
        return nbGlobVar;
    }

    public void setnbGlobVar(int i) {
        nbGlobVar = i;
    }
    /**
     * Implements non-terminal "main" of [SyntaxeContextuelle] in pass 3 
     */
    protected abstract void verifyMain(DecacCompiler compiler) throws ContextualError;
}
