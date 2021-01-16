package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassType;
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


	public DeclClass(AbstractIdentifier className, AbstractIdentifier superClass) { //, ListDeclField listDeclField, ListDeclMethod listDeclMethod)
		this.className = className;
		// Potentiellement nul
		//this.superClass = superClass;
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
    	// Rajout dans l'environnement des types
    	//ClassType classType = new ClassType(className.getName(), this.getLocation(), this.getSuperClass());
    	//ClassDefinition classDef = new ClassDefinition(classType, this.getLocation(), this.getSuperClass());
    	//compiler.getEnvType().put(className.getName(), classDef);
    	/* Ajout dans l'environnement des définitions
    	 * (dans l'environnement GLOBAL, si c'est faux, à changer)
    	 */

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
        //listDeclField.prettyPrint(s, prefix, false);
    	//listDeclMethod.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        //listDeclField.iter();
        //listDeclMethod.iter();
    }

}
