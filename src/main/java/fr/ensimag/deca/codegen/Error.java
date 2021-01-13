package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

public class Error {
    /**
     * add the instructions treating an error
     */
    public static void writeError(IMAProgram program, Label label) {
        program.addLabel(label);
        program.addInstruction(new WSTR("Erreur : " + label));
        program.addInstruction(new WNL());
        program.addInstruction(new ERROR());
    }
}