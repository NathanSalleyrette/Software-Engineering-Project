package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;


/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 *
 * @author gl01
 * @date 01/01/2021
 */
public class DeclClass extends AbstractDeclClass {

	//private String nameClass;

	/* L'attribut au-dessus était déjà fourni,
	 * donc je le laisse même si j'aimerais bien le remplacer
	 * par celui-ci après avoir regardé le poly:
	 */
	private AbstractIdentifier className;

	private AbstractIdentifier superClass;
	private ListDeclField listDeclField;
	private ListDeclMethod listDeclMethod;


	public DeclClass(AbstractIdentifier nameClass, AbstractIdentifier superClass, ListDeclField listDeclField, ListDeclMethod listDeclMethod) { //, ListDeclField listDeclField, ListDeclMethod listDeclMethod)
		this.className = nameClass;
		// Potentiellement nul
		this.superClass = superClass;
		this.listDeclField = listDeclField;
		this.listDeclMethod = listDeclMethod;
	}

	/*
	public String getNameClass() {
		return this.nameClass;
	}
	*/

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
        s.println("class " + className.toString() + " {");
        s.indent();
        //getlistDeclField().decompile(s);
        //getlistDeclMethod().decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
    	// Initialisation de className
    	this.className = new Identifier(compiler.getSymbTb().create(className.toString()));
    	// Rajout dans l'environnement des types
    	if (this.getSuperClass() == null) {
    		AbstractIdentifier classObject = new Identifier(compiler.getSymbTb().create("Object"));
    		this.setSuperClass(classObject);
    		// Pour éviter une boucle if, on met à jour la localisation ici
    		this.getSuperClass().setLocation(Location.BUILTIN);
    	}
    	// On empêche de déclarer deux fois la classe
    	if (compiler.getEnvType().get(className.getName()) != null) {
    		throw new ContextualError("(1.3) La classe est déjà définie", this.getLocation());
    	}
    	// On empêche d'avoir une super classe non déclarée
    	if (compiler.getEnvType().get(superClass.getName()) == null) {
    		throw new ContextualError("(1.3) Identificateur " + superClass.getName().toString()
    				+ " non déclaré", this.getLocation());
    	}
    	// L'identificateur superClass n'a pas de définition, on la met à jour ici, ainsi que celle de la classe:
    	this.getSuperClass().setDefinition(compiler.getEnvType().get(this.getSuperClass().getName()));
    	ClassDefinition superClassDef = this.getSuperClass().getClassDefinition();
    	// L'identificateur de super classe doit désigner une classe
    	if (superClassDef == null) {
    		throw new ContextualError("(1.3) Identificateur de classe attendu", this.getLocation());
    	}
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
    	if (this.getSuperClass() != null) {
	    	this.getSuperClass().prettyPrint(s, prefix, false);
    	}
	    this.listDeclField.prettyPrint(s, prefix, false);
	    this.listDeclMethod.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        //listDeclField.iter();
        //listDeclMethod.iter();
    }

}
