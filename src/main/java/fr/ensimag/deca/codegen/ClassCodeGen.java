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
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tree.Assign;
import fr.ensimag.deca.tree.AbstractDeclClass;
import fr.ensimag.deca.tree.AbstractDeclField;
import fr.ensimag.deca.tree.DeclClass;
import fr.ensimag.deca.tree.DeclField;
import fr.ensimag.deca.tree.AbstractDeclMethod;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.deca.tree.DeclMethod;
import fr.ensimag.deca.tree.ListDeclClass;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Line;
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
            // Ajout des méthodes de la super classe
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

    /**
     * Ecrit le corps des méthodes et l'initialisation des champs des classes
     * @param compiler
     */
    public static void buildMethodsAndFields(DecacCompiler compiler, ListDeclClass classes) {
        compiler.addComment("Classe Object");
        compiler.addComment("---------- Code de la méthode equals dans la classe Object (built in)");
        compiler.addLabel(new Label("code.Object.equals"));
        // Corps de equals
        equalsCodeGen(compiler);
        Iterator<AbstractDeclClass> iter = classes.iterator();
        while (iter.hasNext()) {
            try {
                DeclClass c = (DeclClass) iter.next();
                addMethodsBody(compiler, c);
            } catch (ClassCastException e) {
                throw new UnsupportedOperationException("AbstractDeclClass should be a DeclClass");
            }
        }
    }

    /**
     * Ecrit le corps des méthodes et l'initialisation des champs d'une classe
     * @param compiler
     * @param class1
     */
    public static void addMethodsBody(DecacCompiler compiler, DeclClass class1) {
        String className = class1.getClassName().getName().getName();
        compiler.addComment("Classe " + className);
        ClassDefinition classDef = class1.getClassName().getClassDefinition();

        // Initialisation des champs
        if (classDef.getNumberOfFields() > 0) {
            compiler.reinitCounts();
            compiler.addComment("---------- Initialisation des champs de " + className);
            compiler.addLabel(new Label("init." + className));
            compiler.setInMethod(true);
            Iterator<AbstractDeclField> iterFields = class1.getListDeclField().iterator();
            DAddr thisAddress = new RegisterOffset(-2, Register.LB);
            // Initialisation des nouveaux champs à zéro
            boolean isFirst = true;
            Type precType = null; // Il faut conserver : si precType = null alors isFirst = true
            compiler.addInstruction(new LOAD(thisAddress, Register.R1));
            while (iterFields.hasNext()) {
                try {
                    DeclField f = (DeclField) iterFields.next();
                    Type fieldType = f.getName().getType();
                    if (isFirst || !fieldType.sameType(precType)) { // Inutile de recharger la valeur dans R0 si elle y est déjà
                        compiler.addInstruction(new LOAD(zeroValue(f.getName().getType()), Register.R0));
                        precType = fieldType;
                        isFirst = false;
                    }
                    DAddr fieldAddress = new RegisterOffset(f.getName().getFieldDefinition().getIndex(), Register.R1);
                    compiler.addInstruction(new STORE(Register.R0, fieldAddress));
                } catch (ClassCastException e) {
                    throw new UnsupportedOperationException("AbstractDeclField should be a DeclField");
                }
            }
            int nbTSTO = 0;
            // Initialisation des champs hérités
            if (classDef.getSuperClass().getNumberOfFields() > 0) {
                nbTSTO += 2;
                compiler.addInstruction(new PUSH(Register.R1));
                compiler.addInstruction(new BSR(new LabelOperand(new Label("init." + class1.getSuperClass().getName()))));
                compiler.addInstruction(new SUBSP(1));
            }
            // Initialisation explicite des nouveaux champs
            isFirst = true; // Passe a false si il y a une initialisation explicite
            iterFields = class1.getListDeclField().iterator();
            while (iterFields.hasNext()) {
                try {
                    DeclField f = (DeclField) iterFields.next();
                    AbstractExpr expr = f.getInitialization().getExpression();
                    if (expr != null) {
                        if (isFirst) isFirst = false;
                        // Initialization
                        if (!expr.getType().isBoolean()) {
                            expr.codeGenExpr(compiler);
                        } else {
                            EvalExpr.boolInRegister(compiler, expr);
                        }
                        compiler.addInstruction(new LOAD(thisAddress, Register.R1));
                        DAddr fieldAddress = new RegisterOffset(f.getName().getFieldDefinition().getIndex(), Register.R1);
                        compiler.addInstruction(new STORE(Register.getR(compiler.getCurrentRegister()), fieldAddress));
                    }
                } catch (ClassCastException e) {
                    throw new UnsupportedOperationException("AbstractDeclField should be a DeclField");
                }
            }
            // POPS
            if (!isFirst) {
                for (int i = compiler.getMaxRegister(); i >= compiler.getCurrentRegister(); i--) {
                    compiler.addInstruction(new POP(Register.getR(i)));;
                }
            }
            compiler.addInstruction(new RTS());
            // PUSHS
            if (!isFirst){
                for (int i = compiler.getMaxRegister(); i >= compiler.getCurrentRegister(); i--) {
                    compiler.addFirst(new Line(new PUSH(Register.getR(i))));;
                    nbTSTO++;
                }
            }
            nbTSTO += compiler.getMaxTemp();
            if ((nbTSTO != 0) && !compiler.getCompilerOptions().getNoCheck()) {
                Label pilePleine = new Label("pile_pleine");
                compiler.addError(pilePleine);
                compiler.addFirst(new Line(new BOV(pilePleine)));
                compiler.addFirst(new Line(new TSTO(nbTSTO)));
            }
            compiler.doneProgramBis();
        }

        // Corps des méthodes
        Iterator<AbstractDeclMethod> iterMethods = class1.getListDeclMethod().iterator();
        while (iterMethods.hasNext()) {
            try {
                DeclMethod method = (DeclMethod) iterMethods.next();
                compiler.reinitCounts();
                int nbTSTO = 0;
                int nbVariables = method.getBody().getListDeclVar().size();
                compiler.addComment("---------- Code de la methode " + method.getName().getName() + " dans la classe " + className + " ligne " + method.getLocation().getLine());
                compiler.addLabel(method.getName().getMethodDefinition().getLabel());
                compiler.setInMethod(true);

                method.codeGenBody(compiler);
                if (!method.getBody().isASM()) {
                    // Erreur si il n'y a pas de return alors qu'on attend un type non void en sortie
                    if (!method.getType().isVoid()) {
                        compiler.addInstruction(new WSTR("Erreur : sortie de la méthode sans return"));
                        compiler.addInstruction(new WNL());
                        compiler.addInstruction(new ERROR());
                    }

                    compiler.addLabel(new Label(method.getName().getMethodDefinition().getLabel().toString().replaceFirst("code", "fin")));
                    // POPS
                    for (int i = compiler.getMaxRegister(); i >= compiler.getCurrentRegister(); i--) {
                        compiler.addInstruction(new POP(Register.getR(i)));;
                    }
                    compiler.addInstruction(new RTS());
                    // PUSHS
                    for (int i = compiler.getMaxRegister(); i >= compiler.getCurrentRegister(); i--) {
                        compiler.addFirst(new Line(new PUSH(Register.getR(i))));;
                        nbTSTO++;
                    }
                    compiler.addFirst(new Line("Sauvegarde des registres"));
                    if (nbVariables > 0) {
                        compiler.addFirst(new Line(new ADDSP(nbVariables)));
                    }
                    if ((nbTSTO != 0) && !compiler.getCompilerOptions().getNoCheck()) {
                        Label pilePleine = new Label("pile_pleine");
                        compiler.addError(pilePleine);
                        compiler.addFirst(new Line(new BOV(pilePleine)));
                        compiler.addFirst(new Line(new TSTO(nbTSTO)));
                    }
                }
                compiler.doneProgramBis();
            } catch (ClassCastException e) {
                throw new UnsupportedOperationException("AbstractDeclMethod should be a DeclMethod");
            }
        }
    }

    /**
     * @param type
     * @return the default value of the type for fields
     */
    public static DVal zeroValue(Type type) {
        if (type.isInt() || type.isBoolean()) {
            return new ImmediateInteger(0);
        } else if (type.isFloat()) {
            return new ImmediateFloat(0);
        } else {
            return new NullOperand();
        }
    }

    /**
     * Génère le code du corps de la méthode Object.equals
     * @param compiler
     */
    public static void equalsCodeGen(DecacCompiler compiler) {
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
        compiler.addInstruction(new CMP(new RegisterOffset(-3, Register.LB), Register.R1));
        compiler.addInstruction(new BEQ(new Label("Object.equals.true")));
        compiler.addInstruction(new LOAD(0, Register.R0));
        compiler.addInstruction(new BRA(new Label("fin.Object.equals")));
        compiler.addLabel(new Label("Object.equals.true"));
        compiler.addInstruction(new LOAD(1, Register.R0));
        compiler.addLabel(new Label("fin.Object.equals"));
        compiler.addInstruction(new RTS());
    }
}