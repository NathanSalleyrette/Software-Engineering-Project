package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.deca.tree.AbstractIdentifier;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 *
 * @author gl01
 * @date 01/01/2021
 */
public class DeclClass extends AbstractDeclClass {

	private String nameClass;

	/* L'attribut au-dessus était déjà fourni,
	 * donc je le laisse même si j'aimerais bien le ramplacer
	 * par celui-ci après avoir regardé le poly:
	 */
	private AbstractIdentifier className;

	private AbstractIdentifier superClass;
	//private ListDeclField listDeclField;
	//private ListDeclMethod listDeclMethod;


	public DeclClass(String nameClass, AbstractIdentifier superClass) { //, ListDeclField listDeclField, ListDeclMethod listDeclMethod)
		this.nameClass = nameClass;
		// Potentiellement nul
		this.superClass = superClass;
		//this.listDeclField = listDeclField;
		//this.listDeclMethod = listDeclMethod;
	}

	public String getNameClass() {
		return this.nameClass;
	}

	public AbstractIdentifier getClassName() {
		return this.className;
	}

	public AbstractIdentifier getSuperClass() {
		return this.superClass;
	}
	
	/** Le setter de la superclass est invoqué dans 
	 * la vérification de la déclaration de classe.
	 * Il est utilisé pour définir la classe Object
	 * comme super class si aucune super class n'avait
	 * été précisée. C'est le moyen que j'ai trouvé pour 
	 * récupérer Object, définie dans le compilateur.
	 */
	public void setSuperClass(AbstractIdentifier superClass) {
		this.superClass = superClass;
	}

	/*
	public ListDeclField getListDeclFiled() {
		return this.listDeclField;
	}

	public ListDeclMethod getListDeclMethod() {
		return this.listDeclMethod;
	}
	*/


    @Override
    public void decompile(IndentPrintStream s) {
        s.println("class " + getNameClass() + " {");
        s.indent();
        //getlistDeclField().decompile(s);
        //getlistDeclMethod().decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
    	// Initialisation de className
    	this.className = new Identifier(compiler.getSymbTb().create(nameClass));
    	// Rajout dans l'environnement des types
    	if (this.getSuperClass() == null) {
    		AbstractIdentifier classObject = new Identifier(compiler.getSymbTb().create("Object"));
    		this.setSuperClass(classObject);
    		// Pour éviter une boucle if, on met à jour la localisation ici
    		this.getSuperClass().setLocation(Location.BUILTIN);
    	}
    	// L'identifier superClass n'a pas de définition, on la met à jour ici, ainsi que celle de la classe:
    	this.getSuperClass().setDefinition(compiler.getEnvType().get(this.getSuperClass().getName()));
    	ClassDefinition superClassDef = this.getSuperClass().getClassDefinition();
    	ClassType classType = new ClassType(className.getName(), this.getLocation(), superClassDef);
    	ClassDefinition classDef = new ClassDefinition(classType, this.getLocation(), superClassDef);
    	compiler.getEnvType().put(className.getName(), classDef);
    	this.getClassName().setDefinition(classDef);
    	//TODO: poursuivre
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
    	this.getClassName().prettyPrint(s, prefix, false);
    	this.getSuperClass().prettyPrint(s, prefix, false);
        //listDeclField.prettyPrint(s, prefix, false);
    	//listDeclMethod.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        //listDeclField.iter();
        //listDeclMethod.iter();
    }

}
