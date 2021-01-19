import java.util.Iterator;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.DeclClass;
import fr.ensimag.deca.tree.DeclMethod;
import fr.ensimag.deca.tree.ListDeclClass;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.instructions.*;

public class ClassCodeGen {
    public static void buildTable(DecacCompiler compiler, ListDeclClass classes) {
        compiler.addComment("Construction de la table des méthodes de Object");
        compiler.addInstruction(new LOAD(NullOperand, Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(1, Register.GB)));
        // TODO : set adress
        compiler.incrMethTableSize();
        compiler.addInstruction(new LOAD(new LabelOperand(new Label("code.object.equals")), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(2, Register.GB)));
        compiler.incrMethTableSize();
        Iterator<DeclClass> iter = classes.iterator();
        while (iter.hasNext()) {
            DeclClass c = iter.next();
            addToTable(compiler, c);
        }
    }

    public static void addToTable(DecacCompiler compiler, DeclClass class1) {
        compiler.addComment("Construction de la table des méthodes de " + class1.getClassName());
        compiler.addInstruction(new LEA(class1.getSuperClass())); // TODO : get address
        compiler.incrMethTableSize();
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getMethTableSize(), Register.GB)));
        // TODO : set adress
        Iterator<DeclMethod> iter = class1.getListDeclMethod().iterator(); // TODO : méthodes des parents ?
        while (iter.hasNext()) {
            DeclMethod method = iter.next();
            compiler.addInstruction(new LOAD(new LabelOperand(new Label("code." + class1.getClassName() + "." + method.getName())), Register.R0));
            compiler.incrMethTableSize();
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getMethTableSize(), Register.GB)));
        }
    }
}