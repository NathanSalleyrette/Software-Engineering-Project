package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;

import java.util.Iterator;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;


public class ListDeclMethod extends TreeList<AbstractDeclMethod>{

	@Override
    public void decompile(IndentPrintStream s) { // TODO factoriser dans tree decompile ?
        for (AbstractDeclMethod c : getList()) {
            c.decompile(s);
        }
    }

    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the "env_exp_r" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     */    
    void verifyListDeclMethod(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	/* On crée un itérateur grâce à la méthode iterator() de TreeList
    	 * Il y a sans doute un autre moyen en passant par iterChildren...
    	 * Mais il faudrait implémenter TreeFunction... non ?
    	 */
    	Iterator<AbstractDeclMethod> iter = this.iterator();
    	while (iter.hasNext()) {
    		AbstractDeclMethod declMethod = iter.next();
    		declMethod.verifyDeclMethod(compiler, localEnv, currentClass);
    	}
    }
}
