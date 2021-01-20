package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;

public class DeclField extends AbstractDeclField{


    @Override
    public AbstractIdentifier getName() {
        return fieldName;
    }

    final private Visibility v;
    final private AbstractIdentifier type;
    private AbstractIdentifier fieldName; //avant c'était final mais du coup je pouvais pas le modfier
    final private AbstractInitialization initialization;

    public DeclField(Visibility v, AbstractIdentifier type, AbstractIdentifier fieldName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(fieldName);
        this.type = type;
        this.fieldName = fieldName;
        this.v = v;
        this.initialization = initialization;

    }
    
    public void setFieldName(AbstractIdentifier fieldName) {
    	this.fieldName = fieldName;
    }

    @Override
    protected void verifyDeclField(DecacCompiler compiler,
            ClassDefinition currentClass)
            throws ContextualError {
    	// On regarde si une méthode déjà définie ne porte pas le même nom
    	ExpDefinition homonyme = currentClass.getMembers().get(fieldName.getName());
    	if ((homonyme != null)) {
    		FieldDefinition homofield = homonyme.asFieldDefinition("(2.3) " +
    				fieldName.getName().toString() +
    				" désigne une classe déjà définie", getLocation());
    	}
    	// On récupère le type en décorant
    	Type fieldType = this.type.verifyType(compiler);
    	// On interdit qu'il soit void
    	if (fieldType.isVoid()) throw new ContextualError("(2.5) Un attribut ne peut être de type 'void'",
    			this.getLocation());
    	// On ajoute à l'environnement local
    	FieldDefinition fieldDef = new FieldDefinition(fieldType, this.getLocation(), this.v, currentClass,
    			currentClass.getNumberOfFields());
    	try {
    		currentClass.getMembers().declare(this.fieldName.getName(), fieldDef, this.getLocation());
    	} catch (DoubleDefException e) {
    		throw new ContextualError("(2.4) L'identificateur " + this.getName().getName().toString() +
    				" est déjà défini", this.getLocation());
    	}
    	// On incrémente le nombre de champs de la classe
    	currentClass.setNumberOfFields(currentClass.getNumberOfFields() + 1);
    	// On met à jour la définition
    	this.getName().setDefinition(fieldDef);
    	// On procède aux décorations
    	this.fieldName.verifyExpr(compiler, currentClass.getMembers(), currentClass);
    	this.initialization.verifyInitialization(compiler, fieldType, currentClass.getMembers(), currentClass);
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
    	this.type.decompile(s);
    	s.print(" ");
    	this.fieldName.decompile(s);
    	this.initialization.decompile(s);
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

    @Override 
    public String prettyPrintNode() {
    	return "[visibility=" + this.v.toString() + "] " + super.prettyPrintNode();
    }
}
