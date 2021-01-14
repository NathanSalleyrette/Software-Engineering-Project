package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;

/**
 * Full if/else if/else statement.
 *
 * @author gl01
 * @date 01/01/2021
 */
public class IfThenElse extends AbstractInst {
    
    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    
   public ListInst getElseBranch() {
	   return elseBranch;
   }
   
   public void setElseBranch(ListInst elseBranch) {
	   this.elseBranch = elseBranch;
   }
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
    	this.condition.verifyCondition(compiler, localEnv, currentClass);
    	this.thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    	this.getElseBranch().verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label sinon = new Label("E_Sinon." + compiler.getNbLabel());
        Label fin = new Label("E_Fin." + compiler.getNbLabel());
        condition.boolCodeGen(compiler, false, sinon);
        thenBranch.codeGenListInst(compiler);
        if (!elseBranch.isEmpty()) {
            // Else Branch
            compiler.addInstruction(new BRA(fin));
            compiler.addLabel(sinon);
            elseBranch.codeGenListInst(compiler);
        }
        compiler.addLabel(fin);
    }

    @Override
    public void decompile(IndentPrintStream s) {
       s.print("if("); 
       condition.decompile(s); 
       s.println("){"); 
       s.indent();
       thenBranch.decompile(s);
       s.unindent(); 
       s.println("} else {");
       s.indent();
       elseBranch.decompile(s);
       s.unindent();
       s.print("}"); 
       
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
