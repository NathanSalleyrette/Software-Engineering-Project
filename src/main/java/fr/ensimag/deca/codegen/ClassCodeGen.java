package fr.ensimag.deca.codegen;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.tree.AbstractDeclClass;
import fr.ensimag.deca.tree.DeclClass;
import fr.ensimag.deca.tree.AbstractDeclMethod;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.deca.tree.DeclMethod;
import fr.ensimag.deca.tree.ListDeclClass;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.instructions.*;

public class ClassCodeGen {
    public static void buildTable(DecacCompiler compiler, ListDeclClass classes) {
        compiler.addComment("Construction de la table des méthodes de Object");
        compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
        DAddr address = new RegisterOffset(1, Register.GB);
        compiler.addInstruction(new STORE(Register.R0, address));
        AbstractIdentifier classObject = new Identifier(compiler.getSymbTb().create("Object"));
        classObject.setDefinition(compiler.getEnvType().get(classObject.getName()));
        classObject.getClassDefinition().setAddress(address);
        compiler.incrMethTableSize();
        // Methode Object.equals
        compiler.addInstruction(new LOAD(new LabelOperand(new Label("code.Object.equals")), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(2, Register.GB)));
        compiler.incrMethTableSize();
        Iterator<AbstractDeclClass> iter = classes.iterator();
        while (iter.hasNext()) {
            try {
                DeclClass c = (DeclClass) iter.next();
                addToTable(compiler, c);
            } catch (ClassCastException e) {
                throw new UnsupportedOperationException("AbstractDeclClass should be a DeclClass");
            }
        }
    }

    public static void addToTable(DecacCompiler compiler, DeclClass class1) {
        compiler.addComment("Construction de la table des méthodes de " + class1.getClassName().getName());
        compiler.addInstruction(new LEA(class1.getSuperClass().getClassDefinition().getAddress(), Register.R0));
        compiler.incrMethTableSize();
        DAddr address = new RegisterOffset(compiler.getMethTableSize(), Register.GB);
        compiler.addInstruction(new STORE(Register.R0, address));
        class1.getClassName().getClassDefinition().setAddress(address);
        // Ajout des méthodes
        Map<Integer, Label> map = new HashMap<Integer, Label>();
        addMethods(compiler, class1.getClassName().getClassDefinition(), compiler.getMethTableSize(), map);
        for (Map.Entry<Integer, Label> entry : map.entrySet()) {
        //for (int i = 1; i <= class1.getClassName().getClassDefinition().getNumberOfMethods(); i++) {
            compiler.addInstruction(new LOAD(new LabelOperand(entry.getValue()), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(entry.getKey(), Register.GB)));
            compiler.incrMethTableSize();
        }
    }

    /**
     * Ajoute les méthodes de la classe à la table des méthodes stockée en address(GB)
     * Cette table peut appartenir à une des sous-classe de className.
     * @param compiler
     * @param classDef
     * @param address
     */
    public static void addMethods(DecacCompiler compiler, ClassDefinition classDef, int address, Map<Integer, Label> map) {
        ClassDefinition superClass = classDef.getSuperClass();
        if (superClass != null) {
            // Ajour des méthodes de la super classe
            addMethods(compiler, superClass, address, map);
        }
        Collection<ExpDefinition> members = classDef.getMembers().getValues();
        for (ExpDefinition def : members) {
            if (def.isMethod()) {
                try {
                MethodDefinition method = def.asMethodDefinition(null, null);
                map.put(address + method.getIndex(), method.getLabel());
                } catch (ContextualError e) {}
            }
        }
    }
}