package fr.ensimag.deca.tree;

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

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 *
 * @author gl01
 * @date 01/01/2021
 */
public class DeclMethod extends AbstractDeclMethod  {
    
    @Override
    public AbstractIdentifier getName() {
        return varName;
    }

    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private ListDeclParam params;
    final private MethodBody body;


    public DeclMethod(AbstractIdentifier type, AbstractIdentifier varName, ListDeclParam params, MethodBody body) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(params);
        Validate.notNull(body);
        this.type = type;
        this.varName = varName;
        this.params = params;
        this.body = body;
    }

    @Override
    protected void verifyDeclMethod(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
    	// On fait la vérification du type et on récupère le type au passage
    	Type varType = this.type.verifyType(compiler);
    	// On récupère le symbole du nom de la variable et on crée une ExpDefinition
    	Symbol nameSymb = this.varName.getName();
    	TypeDefinition typeDef = compiler.getEnvType().get(varType.getName());
    	type.setDefinition(typeDef);
    	VariableDefinition varDef = new VariableDefinition(varType, this.getLocation());
    	try {
    		localEnv.declare(nameSymb, varDef, getLocation());
    		varName.setDefinition(varDef);
    	} catch (DoubleDefException e) {
    		throw new ContextualError("(3.17) La variable " + this.varName.toString() + " est déjà déclarée", this.getLocation());
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
    	this.varName.decompile(s);
    	s.print("(");
    	this.params.decompile(s);
    	s.print(")");
    	s.println("{");
        s.indent();
        body.decompile(s);
        s.unindent();
        s.println("}");

    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, false);
    }

}
