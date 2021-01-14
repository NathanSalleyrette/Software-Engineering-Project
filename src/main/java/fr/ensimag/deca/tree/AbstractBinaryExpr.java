package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.deca.codegen.EvalExpr;

/**
 * Binary expressions.
 *
 * @author gl01
 * @date 01/01/2021
 */
public abstract class AbstractBinaryExpr extends AbstractExpr {

    public AbstractExpr getLeftOperand() {
        return leftOperand;
    }

    public AbstractExpr getRightOperand() {
        return rightOperand;
    }

    protected void setLeftOperand(AbstractExpr leftOperand) {
        Validate.notNull(leftOperand);
        this.leftOperand = leftOperand;
    }

    protected void setRightOperand(AbstractExpr rightOperand) {
        Validate.notNull(rightOperand);
        this.rightOperand = rightOperand;
    }

    private AbstractExpr leftOperand;
    private AbstractExpr rightOperand;

    public AbstractBinaryExpr(AbstractExpr leftOperand,
            AbstractExpr rightOperand) {
        Validate.notNull(leftOperand, "left operand cannot be null");
        Validate.notNull(rightOperand, "right operand cannot be null");
        Validate.isTrue(leftOperand != rightOperand, "Sharing subtrees is forbidden");
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        getLeftOperand().decompile(s);
        s.print(" " + getOperatorName() + " ");
        getRightOperand().decompile(s);
        s.print(")");
    }

    abstract protected String getOperatorName();

    public String getOperator() {
        return getOperatorName();
    }

    @Override
	protected void codeGenInst(DecacCompiler compiler) {
        DVal dval = this.getRightOperand().dval(compiler);
		if (dval != Register.getR(compiler.getCurrentRegister())) {
            // Le membre de droite est atomique (Identifier ou Litteral)
			this.getLeftOperand().codeGenInst(compiler);
			EvalExpr.mnemo(compiler, this, dval, Register.getR(compiler.getCurrentRegister()));
		} else {
			// Le membre de droite n'est pas atomique
			this.getLeftOperand().codeGenInst(compiler);
			if (compiler.getCurrentRegister() < compiler.getCompilerOptions().getRMAX()) {
				compiler.incrCurrentRegister();
				this.getRightOperand().codeGenInst(compiler);
				EvalExpr.mnemo(compiler, this, Register.getR(compiler.getCurrentRegister()), Register.getR(compiler.getCurrentRegister()-1));
				compiler.decrCurrentRegister();
			} else {
                // Plus de assez registre libre
                compiler.incrNbTemp();
                compiler.addInstruction(new PUSH(Register.getR(compiler.getCurrentRegister()))); // sauvegarde
                this.getRightOperand().codeGenInst(compiler);
				compiler.addInstruction(new LOAD(Register.getR(compiler.getCurrentRegister()), Register.R0));
                compiler.addInstruction(new POP(Register.getR(compiler.getCurrentRegister()))); // restauration
                compiler.decrNbTemp();
				EvalExpr.mnemo(compiler, this, Register.R0, Register.getR(compiler.getCurrentRegister()));
			}
		}
	}

    @Override
    protected void iterChildren(TreeFunction f) {
        leftOperand.iter(f);
        rightOperand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        leftOperand.prettyPrint(s, prefix, false);
        rightOperand.prettyPrint(s, prefix, true);
    }

}
