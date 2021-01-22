package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.Iterator;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

import fr.ensimag.ima.pseudocode.instructions.ADDSP;

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

    public ListDeclVar getListDeclVar() {
        return declVariables;
    }

    public ListInst getListInst() {
        return insts;
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

    public void codeGenBody(DecacCompiler compiler) {
        // Declaration des variables locales
        Iterator<AbstractDeclVar> iterVar = declVariables.iterator();
        int indexLocalVar = 0;
        while (iterVar.hasNext()) {
            AbstractDeclVar declVar = iterVar.next();
            declVar.getName().getVariableDefinition().setOperand(new RegisterOffset(++indexLocalVar, Register.LB)); // Attribution de la mémoire dans la pile
            AbstractExpr expr = declVar.getInitialization().getExpression();
            if (expr != null) {
                // Initialization
                AbstractExpr init = new Assign(declVar.getName(), expr);
                init.setType(expr.getType());
                init.codeGenInst(compiler);;
            }
        }
        insts.codeGenListInst(compiler);
    }
    
    public boolean isASM() {
    	return false;
    }
}
