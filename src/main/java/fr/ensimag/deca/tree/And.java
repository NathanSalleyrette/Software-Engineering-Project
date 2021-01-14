package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl01
 * @date 01/01/2021
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void boolCodeGen(DecacCompiler compiler, boolean branch, Label tag) {
        if (branch) {
            Label fin = new Label(tag.toString() + "_Fin." + compiler.getNbLabel());
            this.getLeftOperand().boolCodeGen(compiler, false, fin);
            this.getRightOperand().boolCodeGen(compiler, true, tag);
            compiler.addLabel(fin);
            compiler.incrNbLabel();
        } else {
            this.getLeftOperand().boolCodeGen(compiler, false, tag);
            this.getRightOperand().boolCodeGen(compiler, false, tag);
        }
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }


}
