package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractBinaryExpr;
import fr.ensimag.deca.tree.AbstractDeclVar;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.AbstractInitialization;
import fr.ensimag.deca.tree.AbstractOpArith;
import fr.ensimag.deca.tree.Assign;
import fr.ensimag.deca.tree.DeclVar;
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.deca.tree.IntLiteral;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * Class for the evaluation of any expression
 */
public class EvalExpr {

    /**
     * Write assembly instructions to put the value of the register in the stack
     */
    public static void assignVariable(DecacCompiler compiler, DAddr op) {
        compiler.addInstruction(new STORE(Register.getR(compiler.getCurrentRegister()), op));
    }

    public static void mnemo(DecacCompiler compiler, AbstractBinaryExpr op, DVal dval, GPRegister reg) {
        switch (op.getOperator()) {
            case "+" :
                compiler.addInstruction(new ADD(dval, reg));
                break;
            case "-" :
                compiler.addInstruction(new SUB(dval, reg));
                break;
            case "*" :
                compiler.addInstruction(new MUL(dval, reg));
                break;
            case "/" :
                break;
            case "%" :
                break;
            default :
                throw new UnsupportedOperationException("not yet implemented");
        }
        if (op.getType().isFloat()) {
            // Gestion des erreurs liés au calcul flottant
            Label overflow = new Label("overflow_error");
            compiler.addInstruction(new BOV(overflow));
            compiler.addError(overflow);
        }
    }
}