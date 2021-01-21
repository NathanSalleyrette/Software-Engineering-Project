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

	private AbstractIdentifier className;

	private AbstractIdentifier superClass;
	private ListDeclField listDeclField;
	private ListDeclMethod listDeclMethod;


	public DeclClass(AbstractIdentifier className, AbstractIdentifier superClass, ListDeclField listDeclField, ListDeclMethod listDeclMethod) { //, ListDeclField listDeclField, ListDeclMethod listDeclMethod)
		this.className = className;
		// Potentiellement nul
		this.superClass = superClass;
		this.listDeclField = listDeclField;
		this.listDeclMethod = listDeclMethod;
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


	public ListDeclField getListDeclField() {
		return this.listDeclField;
	}

	public ListDeclMethod getListDeclMethod() {
		return this.listDeclMethod;
	}



    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ") ;
        this.className.decompile(s);
        s.print(" extends ");
        this.superClass.decompile(s);
        s.println(" {");
        s.indent();
        getListDeclField().decompile(s);
        getListDeclMethod().decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
    	/* Initialisations de className et de la super classe, nécessaires car className.name
    	 * a été créé par une table de symboles externe au compilateur
    	 */
    	this.className = new Identifier(compiler.getSymbTb().create(className.getName().toString()));
    	this.setSuperClass(new Identifier(compiler.getSymbTb().create(superClass.getName().toString())));
    	// Rajout dans l'environnement des types
    	/* La superclasse par défaut est Object. Elle a déjà été définie pour des besoins de
    	 * décompilation, mais il faut la refaire ici car ce n'est pas le même symbole.
    	 */
    	if (this.getSuperClass().getName().toString() == "Object") {
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
    	// On vérifie que la super classe est bien de type class
    	if (!compiler.getEnvType().get(this.getSuperClass().getName()).getType().isClass()) {
    		throw new ContextualError("(1.3) " + this.getSuperClass().getName().toString() +
    				" n'est pas un identificateur de classe", this.getLocation());
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
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
    	this.getClassName().getClassDefinition().setNumberOfMethods(superClass.getClassDefinition().getNumberOfMethods());
        this.getListDeclMethod().verifyListDeclMethod(compiler, this.getClassName());
		this.getClassName().getClassDefinition().setNumberOfFields(superClass.getClassDefinition().getNumberOfFields());
        this.getListDeclField().verifyListDeclField(compiler, this.getClassName());
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
		this.getListDeclMethod().verifyListMethodBody(compiler, className);
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
        listDeclField.iter(f);
        listDeclMethod.iter(f);
    }

}
