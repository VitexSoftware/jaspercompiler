/**
 * Jasper Compiler with FlexiBee support
 *
 * @author Vítězslav Dvořák <info@vitexsoftware.cz>
 * @copyright 2020-2025 VitexSoftware
 */
package com.vitexsoftware.jaspercompiler;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author vitex
 */
public class Commandline {

     /**
     * Linux Lib Path
     */
    public static final String LINUX_LIBDIR = "/usr/share/flexibee/lib/";

    /**
     * Windows Lib Path
     */
    public static final String WINDOWS_LIBDIR = "C:\\Program Files (x86)\\WinStrom\\lib";
    
    /**
     * Find all winstrom jars in given directory
     * 
     * @param searchIn directory to search in
     * 
     * @return List files found
     */
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
     * Extracts the name attribute from the jasperReport root element
     * 
     * @param jrxmlFile the JRXML file to parse
     * 
     * @return the name attribute value, or null if not found or error occurs
     */
    public static String extractJasperReportName(File jrxmlFile) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(jrxmlFile);
            
            Element rootElement = document.getDocumentElement();
            if ("jasperReport".equals(rootElement.getNodeName())) {
                return rootElement.getAttribute("name");
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.err.println("Warning: Could not parse JRXML file to extract name attribute: " + e.getMessage());
        }
        return null;
    }

    /**
     * Hacks the system classloader to add a classpath entry at
     * runtime.<br /><br />
     *
     * <b>Example</b><br /><br />
     * {@code ClasspathHacker.addToClasspath(new File('example.jar'));}<br />
     * {@code ClassInExampleJar.doStuff();}
     * 
     * @throws java.io.IOException file not found
     *
     * @param file The jar file to add to the classpath
     */
    public static void addToClasspath(File file) throws IOException {
        Agent.addClassPath(file);
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException                     file not found
     * @throws java.lang.ClassNotFoundException        class not found
     * @throws java.lang.Exception                     exception   
     * @throws net.sf.jasperreports.engine.JRException jasper exception
     * 
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, Exception {

        if (args.length > 0) {
            String sourceFileName = new String(args[0]);
            String destinationFilename = new String();
            File linuxLibDir  = new File(LINUX_LIBDIR);
            String libDir = new String();
            
            if(linuxLibDir.exists()){
                libDir = linuxLibDir.toString();
            } else {    
                File windowsLibDir  = new File(WINDOWS_LIBDIR);
                if(windowsLibDir.exists()){
                    libDir = windowsLibDir.toString();
                } else {
                    throw new Exception("No Winstrom Lib dir found");
                }
            }            
            
            for (File winstromJar : winstromJars(libDir)) {
                addToClasspath(winstromJar);
            }

            try {

                // Determine base destination based on args
                if (args.length == 1) {
                    destinationFilename = sourceFileName.replace(".jrxml", ".jasper");
                } else {
                    File f = new File(args[1]);
                    if (f.exists() && f.isDirectory()) {
                        File tmp = new File(sourceFileName);
                        destinationFilename = args[1] + "/" + tmp.getName().replace(".jrxml", ".jasper");
                    } else {
                        destinationFilename = args[1];
                    }
                }

                // New check: ensure filename matches jasperReport name
                File srcFile = new File(sourceFileName);
                String baseName = srcFile.getName().replaceFirst("\\.jrxml$", "");
                String reportName = extractJasperReportName(srcFile);
                if (reportName != null && !reportName.isEmpty() && !baseName.equals(reportName)) {
                    System.out.println("WARNING: Input filename base ('" + baseName + "') differs from jasperReport name ('" + reportName + "').");
                    // Adjust destination filename to use reportName
                    if (destinationFilename.endsWith(".jasper")) {
                        destinationFilename = destinationFilename.replaceFirst("[^/\\\\]+\\.jasper$", reportName + ".jasper");
                    } else {
                        // If destination is a directory path without .jasper, append corrected name
                        if (new File(destinationFilename).isDirectory()) {
                            destinationFilename = new File(destinationFilename, reportName + ".jasper").getPath();
                        }
                    }
                    System.out.println("Destination adjusted to: " + destinationFilename);
                }

                // Perform compilation
                if (args.length == 1) {
                    JasperCompileManager.compileReportToFile(sourceFileName);
                } else {
                    JasperCompileManager.compileReportToFile(sourceFileName, destinationFilename);
                }

                System.out.println("Compiling Report Design: " + destinationFilename);
            } catch (JRException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Commandline AbraFlexi custom reports compiler v0.5.0");
            System.out.println("Usage: jaspercompiler /path/to/report.jxml [destination/path/[filename.jasper]]");
        }
    }
}
