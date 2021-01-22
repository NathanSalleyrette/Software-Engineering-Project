package fr.ensimag.ima.pseudocode;

import java.io.PrintStream;

public class InstructionStringLine extends AbstractLine {
	    private String instruction;

	    public InstructionStringLine(String instruction) {
	        this.instruction = instruction;
	    }

	    @Override
	    void display(PrintStream s) {
	        s.println(instruction);
	    }
	
}
