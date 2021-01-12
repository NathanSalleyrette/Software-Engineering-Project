package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl01
 * @date 01/01/2021
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }
    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    public int getRMAX() {
        return RMAX;
    }

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private List<File> sourceFiles = new ArrayList<File>();
    private int RMAX = 15;

    
    public void parseArgs(String[] args) throws CLIException {
        // Gestion des arguments
        for (int i=0; i < args.length; i++) {
            if (args[i].charAt(0) == '-') {
                // Options
                if (args[i].length() != 2) throw new IllegalArgumentException("Invalid argument " + args[i]);
                switch (args[i].charAt(1)) {
                    case 'b' :
                        if (args.length > 1) throw new IllegalArgumentException("L'option -b doit être utilisée seule");
                        printBanner = true;
                        break;
                    
                    case 'p' :
                        throw new UnsupportedOperationException("not yet implemented");
                        //break;
                    
                    case 'v' :
                        throw new UnsupportedOperationException("not yet implemented");
                        //break;

                    case 'n' :
                        throw new UnsupportedOperationException("not yet implemented");
                        //break;

                    case 'r' :
                        i++; // l'option -r à un argument
                        RMAX = Integer.parseInt(args[i]);
                        break;

                    case 'd' :
                        debug++;
                        break;

                    case 'P' :
                        parallel = true;
                        break;

                    default :
                        throw new IllegalArgumentException("Invalid argument " + args[i]);
                }
            } else {
                // Fichiers deca
                sourceFiles.add(new File(args[i]));
            }
        }

        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }
    }

    protected void displayUsage() {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
