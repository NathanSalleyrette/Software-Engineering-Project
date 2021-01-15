package fr.ensimag.deca;

import java.io.File;
import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl01
 * @date 01/01/2021
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            String banner = "Lucie Fousse, Benjamin Cathelineau, Baudouin Jaubert," +
            				"Alan Manic, Nathan Salleyrette";
            System.out.println(banner);
            return;
        }
        if (options.getSourceFiles().isEmpty()) {
            String help = "La syntaxe d'utilisation de l'exécutable decac est ;\n"
                        + "decac [[-p | -v] [-n] [-r X] [-d]* [-P] [-w] <fichier deca>...] | [-b]\n\n"
                        + "La commande decac, sans argument, affiche les options disponibles. On peut appeler la commande\n"
                        + "decac avec un ou plusieurs fichiers sources Deca.\n\n"
                        + "Les options :\n"
                        + ". -b (banner) : affiche une bannière indiquant le nom de l’équipe\n"
                        + ". -p (parse) : arrête decac après l’étape de construction de\n"
                        + "\t\tl’arbre, et affiche la décompilation de ce dernier\n"
                        + ". -v (verification) : arrête decac après l’étape de vérifications\n"
                        + ". -n (no check) : supprime les tests à l’exécution \n"
                        + ". -r X (registers) : limite les registres banalisés disponibles à\n"
                        + "\t\tR0 ... R{X-1}, avec 4 <= X <= 16\n"
                        + ". -d (debug) : active les traces de debug. Répéter\n"
                        + "\t\tl’option plusieurs fois pour avoir plus de\n"
                        + "\t\ttraces.\n"
                        + ". -P (parallel) : s’il y a plusieurs fichiers sources,\n"
                        + "\t\tlance la compilation des fichiers en\n"
                        + "\t\tparallèle (pour accélérer la compilation)\n\n"
                        + "Les options -p et -v sont incompatibles.\n"
                        ;
            System.out.println(help);
            options.displayUsage();
            return;
        }
        if (options.getParallel()) {
            // A FAIRE : instancier DecacCompiler pour chaque fichier à
            // compiler, et lancer l'exécution des méthodes compile() de chaque
            // instance en parallèle. Il est conseillé d'utiliser
            // java.util.concurrent de la bibliothèque standard Java.
            throw new UnsupportedOperationException("Parallel build not yet implemented");
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
