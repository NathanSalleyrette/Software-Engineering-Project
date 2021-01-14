package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl01
 * @date 01/01/2021
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void boolCodeGen(DecacCompiler compiler, boolean branch, Label tag) {
        new Not(new And(new Not(this.getLeftOperand()), new Not(this.getRightOperand()))).boolCodeGen(compiler, branch, tag);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }


}
