package fr.ensimag.deca.tree;


import java.util.Iterator;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;


public class ListDeclField extends TreeList<AbstractDeclField >{

	@Override
    public void decompile(IndentPrintStream s) { // TODO factoriser dans tree decompile ?
        for (AbstractDeclField c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

	/** Implémente la passe 2
	 * 
	 * @param compiler
	 * @param className
	 * 					nom de la classe courante
	 * @param superClass
	 * 					nom de la super classe
	 * @throws ContextualError
	 */
    void verifyListDeclField(DecacCompiler compiler, AbstractIdentifier className
    		) throws ContextualError {
    	/* On crée un itérateur grâce à la méthode iterator() de TreeList
    	 * Il y a sans doute un autre moyen en passant par iterChildren...
    	 * Mais il faudrait implémenter TreeFunction... non ?
    	 */
    	Iterator<AbstractDeclField> iter = this.iterator();
    	while (iter.hasNext()) {
    		AbstractDeclField  declField = iter.next();
    		declField.verifyDeclField (compiler, className.getClassDefinition());
    	}
    }
}

