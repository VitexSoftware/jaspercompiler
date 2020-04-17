/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vitexsoftware.jaspercompiler;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 *
 * @author vitex
 */
public class Commandline {

    public static List<File> winstromJars(String searchIn) {
        File dir = new File(searchIn);
        FileFilter fileFilter = new WildcardFileFilter("*.jar");
        List<File> files = new ArrayList<File>(Arrays.asList(dir.listFiles(fileFilter)));

        Iterator<File> iterator = files.iterator();
        while (iterator.hasNext()) {
            File currentFile = iterator.next();
            if (!(currentFile.getName().toString().startsWith("winstrom-") || currentFile.getName().toString().startsWith("binding-"))) {
                iterator.remove();
            }
        }
        return files;
    }

    /**
     * Hacks the system classloader to add a classpath entry at
     * runtime.<br /><br />
     *
     * <b>Example</b><br /><br />
     * {@code ClasspathHacker.addToClasspath(new File('example.jar'));}<br />
     * {@code ClassInExampleJar.doStuff();}
     *
     * @param file The jar file to add to the classpath
     */
    public static void addToClasspath(File file) throws IOException {
        Agent.addClassPath(file);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        if (args.length > 0) {
            String sourceFileName = new String(args[0]);
            String destinationFilename = new String();

            for (File winstromJar : winstromJars("/usr/share/flexibee/lib/")) {
                addToClasspath(winstromJar);
            }

            try {

                if (args.length == 1) {
                    JasperCompileManager.compileReportToFile(sourceFileName);
                } else {

                    File f = new File(args[1]);
                    if (f.exists() && f.isDirectory()) {
                        File tmp = new File(sourceFileName);
                        destinationFilename = args[1] + "/" + tmp.getName().replace(".jrxml", ".jasper");
                    } else {
                        destinationFilename = args[1];
                    }
                    JasperCompileManager.compileReportToFile(sourceFileName, destinationFilename);
                }

            } catch (JRException e) {
                e.printStackTrace();
            }
            System.out.println("Compiling Report Design ... " + sourceFileName);

        } else {
            System.out.println("Usage: jaspcomp /path/to/report.jxml");
        }
    }

}
