package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassType;
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
	
	private String nameClass;
	
	/* L'attribut au-dessus était déjà fourni,
	 * donc je le laisse même si j'aimerais bien le ramplacer
	 * par celui-ci après avoir regardé le poly:
	 */
	private AbstractIdentifier className;
	
	private String superClass;
	//private ListDeclField listDeclField;
	//private ListDeclMethod listDeclMethod;
	
	
	public DeclClass(AbstractIdentifier className) { //, ListDeclField listDeclField, ListDeclMethod listDeclMethod)
		this.className = className;
		//this.listDeclField = listDeclField;
		//this.listDeclMethod = listDeclMethod;
	}
	
	public String getNameClass() {
		return this.nameClass;
	}
	
	public AbstractIdentifier getClassName() {
		return this.className;
	}
	
	public String getSuperClass() {
		return this.getSuperClass();
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
        throw new UnsupportedOperationException("not yet implemented");
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
    	this.getClassName().prettyPrintType(s, prefix);
        //listDeclField.prettyPrint(s, prefix, false);
    	//listDeclMethod.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        //listDeclField.iter();
        //listDeclMethod.iter();
    }

}
