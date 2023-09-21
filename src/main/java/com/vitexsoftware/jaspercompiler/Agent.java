package com.vitexsoftware.jaspercompiler;
/** Represents an employee.
 * @author Vítězslav Dvořák
 * @author vitexsoftware.com
 * @version 1.0
 * @since 1.0
*/
import java.io.File;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

/**
 * This class is used to add a jar file to the classpath of the running JVM.
 * It is used by the JasperReports compiler to add the JasperReports jar file to
 * the classpath of the JVM that is running the compiler.
 * This is necessary because the JasperReports compiler uses reflection to
 * instantiate classes from the JasperReports jar file.
 */
public class Agent {

    private static Instrumentation inst = null;

    /**
     * The JRE will call method before launching your main()
     * 
     * @param a     agent arguments
     * @param inst  the instrumentation instance
     */
    public static void agentmain(final String a, final Instrumentation inst) {
        Agent.inst = inst;
    }

    /**
     * Add a jar file to the classpath of the running JVM.
     * 
     * @param f The jar file to add to the classpath
     * 
     * @return true if the jar was successfully added to the classpath
     */
    public static boolean addClassPath(File f) {
        try {
            inst.appendToSystemClassLoaderSearch(new JarFile(f));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }

}
