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

public class DeclParam extends AbstractDeclParam{


    @Override
    public AbstractIdentifier getParamName() {
        return paramName;
    }

    final private AbstractIdentifier type;
    final private AbstractIdentifier paramName;

    public DeclParam(AbstractIdentifier type, AbstractIdentifier paramName) {
        Validate.notNull(type);
        Validate.notNull(paramName);
        this.type = type;
        this.paramName = paramName;

    }

    @Override
    protected void verifyDeclParam(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
    	// On fait la vérification du type et on récupère le type au passage
    	Type varType = this.type.verifyType(compiler);
    	// On récupère le symbole du nom de la variable et on crée une ExpDefinition
    	Symbol nameSymb = this.paramName.getName();
    	TypeDefinition typeDef = compiler.getEnvType().get(varType.getName());
    	type.setDefinition(typeDef);
    	VariableDefinition varDef = new VariableDefinition(varType, this.getLocation());
    	try {
    		localEnv.declare(nameSymb, varDef, getLocation());
    		paramName.setDefinition(varDef);
    	} catch (DoubleDefException e) {
    		throw new ContextualError("(3.17) La variable " + this.paramName.toString() + " est déjà déclarée", this.getLocation());
    	}
    	// On vérifie l'initialisation ensuite
    	if (varType.isVoid()) {
    		throw new ContextualError("(3.17) Le type de l'identificateur ne peut être 'void''", this.getLocation());
    	}
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
    	this.type.decompile(s);
    	s.print(" ");
    	this.paramName.decompile(s);
    	s.print(";");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        paramName.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        paramName.prettyPrint(s, prefix, false);
    }

}
