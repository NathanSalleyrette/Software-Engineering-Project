package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

public class DeclField extends AbstractDeclField{


    @Override
    public AbstractIdentifier getName() {
        return fieldName;
    }

    final private Visibility v;
    final private AbstractIdentifier type;
    final private AbstractIdentifier fieldName;
    final private AbstractInitialization initialization;

    public DeclField(Visibility v, AbstractIdentifier type, AbstractIdentifier fieldName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(fieldName);
        this.type = type;
        this.fieldName = fieldName;
        this.v = v;
        this.initialization = initialization;

    }

    @Override
    protected void verifyDeclField(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
    	// On fait la vérification du type et on récupère le type au passage
    	Type varType = this.type.verifyType(compiler);
    	// On récupère le symbole du nom de la variable et on crée une ExpDefinition
    	Symbol nameSymb = this.fieldName.getName();
    	TypeDefinition typeDef = compiler.getEnvType().get(varType.getName());
    	type.setDefinition(typeDef);
    	VariableDefinition varDef = new VariableDefinition(varType, this.getLocation());
    	try {
    		localEnv.declare(nameSymb, varDef, getLocation());
    		fieldName.setDefinition(varDef);
    	} catch (DoubleDefException e) {
    		throw new ContextualError("(3.17) La variable " + this.fieldName.toString() + " est déjà déclarée", this.getLocation());
    	}
    	// On vérifie l'initialisation ensuite
    	this.initialization.verifyInitialization(compiler, varType, localEnv, currentClass);
    	if (varType.isVoid()) {
    		throw new ContextualError("(3.17) Le type de l'identificateur ne peut être 'void''", this.getLocation());
    	}
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
    	this.type.decompile(s);
    	s.print(" ");
    	this.fieldName.decompile(s);
    	s.print(";");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        fieldName.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        fieldName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

}
