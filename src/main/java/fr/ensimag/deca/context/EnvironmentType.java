package fr.ensimag.deca.context;

import java.util.HashMap;
import java.util.Map;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;

/**
 * Classe d'environnement des types
 * Je l'avais originellement déclarée interne à DecacCompiler
 * soi-disant pour éviter les problèmes d'accès par différents fils,
 * mais je doute de la validité de cet argument.
 */
public class EnvironmentType {
	
	private EnvironmentType parentEnvironment;
	private EnvironmentType envTypePredef;
	private EnvironmentExp envExpObject;
	
	private Map<Symbol, TypeDefinition> map;
	
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
		this.envExpObject = initEnvExpObject(table, this.envTypePredef);
	}
	
	public EnvironmentType(SymbolTable table, EnvironmentType parent) {
		this.parentEnvironment = parent;
		this.map = new HashMap<Symbol, TypeDefinition>();
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
		this.put(INT, intDef);
		this.put(FLOAT, floatDef);
		this.put(BOOLEAN, booleanDef);
		this.put(VOID, voidDef);
		this.parentEnvironment = null;
		return envTypePredef;
	}

	/**
	 * Ajoute la classe Object dans les types prédéfinis
	 * @param table
	 * @return
	 * @throws ContextualError
	 */
	public EnvironmentExp initEnvExpObject(SymbolTable table, EnvironmentType envType){
		EnvironmentExp envObject = new EnvironmentExp(null);
		Symbol equalsMethod = table.create("equals");
		Symbol OBJECT = table.create("Object"); // Attention ! avec une majuscule
		try {
			Type boolType = envType.get(table.create("boolean")).getType();
			Signature equalsSig = new Signature();
			// On a besoin d'un type class indéfini
			Type classType = new ClassType(null, Location.BUILTIN, null); // Je sens que ça va poser des problèmes...
			equalsSig.add(classType); equalsSig.add(classType);
			MethodDefinition equalsDef = new MethodDefinition(boolType, Location.BUILTIN, equalsSig, 0);
			envObject.declare(equalsMethod, equalsDef, Location.BUILTIN);
		} catch (Exception e) {
		}
		// A présent on peut ajouter dans les types
		ClassType objectType = new ClassType(table.create("object"), Location.BUILTIN, null);
		ClassDefinition objectDef = new ClassDefinition(objectType, Location.BUILTIN, null);
		envType.put(OBJECT, objectDef);
		return envObject;
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