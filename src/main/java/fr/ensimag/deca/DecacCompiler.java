package fr.ensimag.deca;

import fr.ensimag.deca.codegen.Error;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Line;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.Map;

/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl01
 * @date 01/01/2021
 */
public class DecacCompiler implements Runnable{
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
    
    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");
    
    private SymbolTable symbTb;
    
    private EnvironmentType envType;

    private int currentRegister; // X tq tous les registres RY, Y < X sont utilisés

    private Map<String, Label> errorMap;

    // Calcul du nombre de valeurs temporaires necessaires
    private int nbTemp;
    private int maxTemp;

    // Numerotation des labels générés : E_Fonction.nbLabel
    private int nbLabel;

    // Taille de la table des méthodes dans la pile, utilisé pour y ajouter des éléments
    private int methTableSize;

    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;
        this.symbTb = new SymbolTable();
        this.envType = new EnvironmentType(this.symbTb);
        currentRegister = 2;
        errorMap = new HashMap<String, Label>();
        nbTemp = 0;
        maxTemp = 0;
        methTableSize = 0;
    }

    public int getNbTemp() {
        return nbTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void incrNbTemp() {
        nbTemp++;
        if (maxTemp < nbTemp) maxTemp = nbTemp;
    }

    public void decrNbTemp() {
        nbTemp--;
    }

    public void reinitCounts() {
        nbTemp = 0;
        maxTemp = 0;
        maxRegister = getCurrentRegister();
    }

    //
    public int getNbLabel() {
        return nbLabel;
    }

    public void incrNbLabel() {
        nbLabel++;
    }

    //
    public int getCurrentRegister() {
        return currentRegister;
    }

    public void decrCurrentRegister() {
        currentRegister--;
    }

    public void incrCurrentRegister() {
        currentRegister++;
        if (currentRegister > maxRegister) maxRegister++;
    }

    /**
     * Maximum index of used registers
     * Used to save registers in methods
     */
    private int maxRegister = getCurrentRegister();
    public int getMaxRegister() {
        return maxRegister;
    }

    //
    public void addError(Label label) {
        String key = label.toString();
        if (!errorMap.containsKey(key)) {
            errorMap.put(key, label);
        }
    }

    //
    public int getMethTableSize() {
        return methTableSize;
    }

    public void incrMethTableSize() {
        methTableSize++;
    }

    //
    /**
     * Boolean showing if the the method body being written has a return instruction
     */
    private boolean hasReturn = false;

	public boolean hasReturn() {
		return hasReturn;
	}

	public void setReturn(boolean bool) {
		hasReturn = bool;
    }
    
    //
    public SymbolTable getSymbTb() {
    	return this.symbTb;
    }
    
    public EnvironmentType getEnvType() {
    	return this.envType;
    }
    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers
     * to use, ...).
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        if (!inMethod) program.addComment(comment);
        else programBis.addComment(comment);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     */
    public void addLabel(Label label) {
        if (!inMethod) program.addLabel(label);
        else programBis.addLabel(label);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        if (!inMethod) program.addInstruction(instruction);
        else programBis.addInstruction(instruction);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        if (!inMethod) program.addInstruction(instruction, comment);
        else programBis.addInstruction(instruction, comment);
    }

    /**
     * add the instructions treating an error
     */
    public void writeErrors() {
        for (Label label : errorMap.values()) {
            Error.writeError(program, label);
        }
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addFirst(fr.ensimag.ima.pseudocode.Line)
     */
    public void addFirst(Line l) {
        if (!inMethod) program.addFirst(l);
        else programBis.addFirst(l);
    }

    public void append(IMAProgram p) {
        program.append(p);
    }
    
    /**
     * @see 
     * fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }
    
    private final CompilerOptions compilerOptions;
    private final File source;
    /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private final IMAProgram program = new IMAProgram();
 
    /**
     * A program for the instructions of methods
     */
    private IMAProgram programBis = new IMAProgram();

    public void doneProgramBis() {
        program.append(programBis);
        inMethod = false;
        programBis = new IMAProgram();
    }

    private boolean inMethod = false;

    public void setInMethod(boolean bool) {
        inMethod = bool;
    }

    public void run() {this.compile();}
    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {
        String sourceFile = source.getAbsolutePath();
        String destFile = sourceFile.replace(".deca", ".ass");
        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        try {
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName name of the destination (assembly) file
     * @param out stream to use for standard output (output of decac -p)
     * @param err stream to use to display compilation errors
     *
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {
        AbstractProgram prog = doLexingAndParsing(sourceName, err);

        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }
        assert(prog.checkAllLocations());
        if (compilerOptions.getParse()) {
        	prog.decompile(new IndentPrintStream(System.out));
        	return false;
        }
        prog.verifyProgram(this);
        if (compilerOptions.getVerification()) {
        	return false;
        }
        assert(prog.checkAllDecorations());

        addComment("start main program");
        prog.codeGenProgram(this);
        addComment("end main program");
        LOG.debug("Generated assembly code:" + nl + program.display());
        LOG.info("Output file assembly file is: " + destName);

        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");

        program.display(new PrintStream(fstream));
        LOG.info("Compilation of " + sourceName + " successful.");
        return false;
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     * compiler.
     * @throws LocationException When a compilation error (incorrect program)
     * occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            lex = new DecaLexer(CharStreams.fromFileName(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }
}