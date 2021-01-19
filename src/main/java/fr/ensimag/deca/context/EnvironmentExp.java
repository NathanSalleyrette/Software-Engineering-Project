package fr.ensimag.deca.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 * Dictionary associating identifier's ExpDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 * 
 * The dictionary at the head of this list thus corresponds to the "current" 
 * block (eg class).
 * 
 * Searching a definition (through method get) is done in the "current" 
 * dictionary and in the parentEnvironment if it fails. 
 * 
 * Insertion (through method declare) is always done in the "current" dictionary.
 * 
 * @author gl01
 * @date 01/01/2021
 */
public class EnvironmentExp {
    // A FAIRE : implémenter la structure de donnée représentant un
    // environnement (association nom -> définition, avec possibilité
    // d'empilement).

    EnvironmentExp parentEnvironment;
    private Map<Symbol, ExpDefinition> map = new HashMap<Symbol, ExpDefinition>();
    
    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
        
        private String message;
        private Location location;
        
        public DoubleDefException(String message, Location location) {
        	this.message = message;
        	this.location = location;
        }
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public ExpDefinition get(Symbol key) {
        ExpDefinition result = map.get(key); // null if no such key

        // Search in the parent dictionary after failure in current one
        if (result == null && parentEnvironment != null) {
            return parentEnvironment.get(key);
        }
        return result;
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     * 
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary 
     * - or, hides the previous declaration otherwise.
     * 
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws DoubleDefException
     *             if the symbol is already defined at the "current" dictionary
     *
     */
    public void declare(Symbol name, ExpDefinition def, Location location) throws DoubleDefException {
        if (map.containsKey(name)) {
        	throw new DoubleDefException(name.getName() + "déjà défini", location);
        }
        // By adding the definition in the current dictionary we hide the previous ones
        // Because the getter stops at the first occurrence from the current dictionary downward.
        else map.put(name, def);
    }

    public Collection<ExpDefinition> getValues() {
        return map.values();
    }
}
