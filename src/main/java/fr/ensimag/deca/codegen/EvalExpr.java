package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractBinaryExpr;
import fr.ensimag.deca.tree.AbstractDeclVar;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.AbstractInitialization;
import fr.ensimag.deca.tree.AbstractOpArith;
import fr.ensimag.deca.tree.Assign;
import fr.ensimag.deca.tree.BooleanLiteral;
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
import fr.ensimag.deca.tools.DecacInternalError;

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
                if (op.getType().isInt()) {
                    compiler.addInstruction(new QUO(dval, reg));
                    if (!compiler.getCompilerOptions().getNoCheck()) {
                        Error.instanceError(compiler, "division_par_zero");
                    }
                } else {
                    compiler.addInstruction(new DIV(dval, reg));
                }
                break;
            case "%" :
                // Reste entier
                compiler.addInstruction(new REM(dval, reg));
                if (!compiler.getCompilerOptions().getNoCheck()) {
                    Error.instanceError(compiler, "division_par_zero");
                }
                break;
            case "=" :
                // Assign, le contexte devrait empecher l'exception
                try {
                    compiler.addInstruction(new STORE(reg, (DAddr) dval));
                } catch (ClassCastException e) {
                    throw new DecacInternalError(
                            "DVal "
                                    + dval.toString()
                                    + " is not a DAddr, you can't assign it");
                }
                break;
            case "==" :
                compiler.addInstruction(new CMP(dval, reg));
                break;
            case "!=" :
                compiler.addInstruction(new CMP(dval, reg));
                break;
            case ">" :
                compiler.addInstruction(new CMP(dval, reg));
                break;
            case "<" :
                compiler.addInstruction(new CMP(dval, reg));
                break;
            case ">=" :
                compiler.addInstruction(new CMP(dval, reg));
                break;
            case "<=" :
                compiler.addInstruction(new CMP(dval, reg));
                break;
            default :
                throw new UnsupportedOperationException("unsupported operation");
        }
        if (op.getType().isFloat() && !compiler.getCompilerOptions().getNoCheck()) {
            // Gestion des erreurs liés au calcul flottant
            Error.instanceError(compiler, "debordement_flottant");
        }
    }
}