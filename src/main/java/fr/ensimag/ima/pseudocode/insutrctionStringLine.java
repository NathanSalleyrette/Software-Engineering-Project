package fr.ensimag.ima.pseudocode;

import java.io.PrintStream;

public class insutrctionStringLine extends AbstractLine {
	    private String instruction;

	    public insutrctionStringLine(String instruction) {
	        this.instruction = instruction;
	    }

	    @Override
	    void display(PrintStream s) {
	        s.println(instruction);
	    }
	
}
