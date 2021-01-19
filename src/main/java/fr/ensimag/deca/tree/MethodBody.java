package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.Iterator;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.VoidType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

public class MethodBody extends AbstractMethodBody{
    
    private ListDeclVar declVariables;
    private ListInst insts;
    public MethodBody(ListDeclVar declVariables,
            ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType) throws ContextualError {
    	this.declVariables.verifyListDeclVariable(compiler, localEnv, currentClass);
    	this.insts.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
        declVariables.decompile(s);
        insts.decompile(s);
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
