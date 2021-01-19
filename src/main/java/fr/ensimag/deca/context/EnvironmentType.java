package fr.ensimag.deca.context;

import java.util.HashMap;
import java.util.Map;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.ima.pseudocode.Label;

/**
 * Classe d'environnement des types
 * Je l'avais originellement déclarée interne à DecacCompiler
 * soi-disant pour éviter les problèmes d'accès par différents fils,
 * mais je doute de la validité de cet argument.
 */
public class EnvironmentType {
	
	private EnvironmentType parentEnvironment;
	private EnvironmentType envTypePredef;
	
	private Map<Symbol, TypeDefinition> map = new HashMap<Symbol, TypeDefinition>();
	
	/**
	 * Constructeur appelé pour définir env_type_predef
	 * par le constructeur du DecacCompiler (il est donc
	 * déclaré privé)
	 * 
	 * Tout ce qui concerne la classe Object est fait dans le 
	 * @param table
	 * 		Table de symboles propre au DecacCompiler, que l'on
	 * va garnir
	 */
	public EnvironmentType(SymbolTable table) {
		this.envTypePredef = this.initEnvType(table);
		this.parentEnvironment = this.envTypePredef;
		initObject(table, this.envTypePredef);
	}
	
	public EnvironmentType(SymbolTable table, EnvironmentType parent) {
		this.parentEnvironment = parent;
	}
	
	/**
	 * Initialise l'environnement de types prédéfinis sans objet
	 */
	public EnvironmentType initEnvType(SymbolTable table) {
		EnvironmentType envTypePredef = new EnvironmentType(table, null);
		Symbol INT = table.create("int");
		Symbol FLOAT = table.create("float");
		Symbol BOOLEAN = table.create("boolean");
		Symbol VOID = table.create("void");
		TypeDefinition intDef = new TypeDefinition(new IntType(INT), Location.BUILTIN);
		TypeDefinition floatDef = new TypeDefinition(new FloatType(FLOAT), Location.BUILTIN);
		TypeDefinition booleanDef = new TypeDefinition(new BooleanType(BOOLEAN), Location.BUILTIN);
		TypeDefinition voidDef = new TypeDefinition(new VoidType(VOID), Location.BUILTIN);
		envTypePredef.put(INT, intDef);
		envTypePredef.put(FLOAT, floatDef);
		envTypePredef.put(BOOLEAN, booleanDef);
		envTypePredef.put(VOID, voidDef);
		envTypePredef.parentEnvironment = null;
		return envTypePredef;
	}

	/**
	 * Ajoute la classe Object dans les types prédéfinis
	 * @param table
	 * @return
	 * @throws ContextualError
	 */
	public void initObject(SymbolTable table, EnvironmentType envType){
		Symbol OBJECT = table.create("Object"); // Attention ! avec une majuscule
		// On ajoute dans les types
		ClassType objectType = new ClassType(table.create("Object"), Location.BUILTIN, null);
		ClassDefinition objectDef = new ClassDefinition(objectType, Location.BUILTIN, null);
		envType.put(OBJECT, objectDef);
		// A présent on crée l'environment des expressions de Object qui n'a que equals
		try {
			Symbol equalsMethod = table.create("equals");
			Type boolType = envType.get(table.create("boolean")).getType();
			Signature equalsSig = new Signature();
			// On a besoin d'un type class indéfini
			Type classType = new ClassType(null, Location.BUILTIN, null); // Je sens que ça va poser des problèmes...
			equalsSig.add(classType); equalsSig.add(classType);
			MethodDefinition equalsDef = new MethodDefinition(boolType, Location.BUILTIN, equalsSig, 1);
			equalsDef.setLabel(new Label("code.Object.equals"));
			objectDef.getMembers().declare(equalsMethod, equalsDef, Location.BUILTIN);
			objectDef.incNumberOfMethods();
		} catch (DoubleDefException e) {
		}
	}
	
	public Map<Symbol, TypeDefinition> getMap() {
		return this.map;
	}
	
	public EnvironmentType getParentEnvironment() {
		return this.parentEnvironment;
	}
	/**
     * Return the type of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public TypeDefinition get(Symbol key) {
        TypeDefinition result = map.get(key); // null if no such key

        // Search in the parent dictionary after failure in current one
        if (result == null && parentEnvironment != null) {
            return parentEnvironment.get(key);
        }
        return result;
    }
    
    /**
     * Insère un nouveau couple (symbole, définition) dans la table
     * @param key
     * 		la clef (type Symbol)
     * @param def
     * 		la définition (type TypeDefinition)
     */
    public void put(Symbol key, TypeDefinition def) {
    	if (this.get(key) == null) {
    		this.getMap().put(key,  def);
    	}
    }
}