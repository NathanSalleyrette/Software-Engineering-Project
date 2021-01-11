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
	//private ListDeclField listDeclField;
	//private ListDeclMethod listDeclMethod;
	
	/*
	public DeclClass(ListDeclField listDeclField, ListDeclMethod listDeclMethod) {
		this.listDeclField = listDeclField;
		this.listDeclMethod = listDeclMethod;
	}
	*/
	
	public String getNameClass() {
		return this.nameClass;
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
        throw new UnsupportedOperationException("Not yet supported");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet supported");
    }

}
