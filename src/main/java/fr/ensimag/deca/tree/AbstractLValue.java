package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl01
 * @date 01/01/2021
 */
public abstract class AbstractLValue extends AbstractExpr {

    /**
     * Utile pour l'utilisation de dval lors d'une assignation,
     * car une sélection nécessite un registre
     * @return true if this is an instance of Selection
     */
    public boolean isSelection() {
        return false;
    }

    /**
     * Utile dans les méthodes, lors de l'assignation d'une variable
     * permet de la convertir en selection si c'est un champ
     */
    public AbstractLValue checkSelection() {
        return this;
    }
}
