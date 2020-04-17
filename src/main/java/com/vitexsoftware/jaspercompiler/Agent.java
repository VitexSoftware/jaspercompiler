/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vitexsoftware.jaspercompiler;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

public class Agent {

    private static Instrumentation inst = null;

    // The JRE will call method before launching your main()
    public static void agentmain(final String a, final Instrumentation inst) {
        Agent.inst = inst;
    }

    public static boolean addClassPath(File f) {
        try {
            inst.appendToSystemClassLoaderSearch(new JarFile(f));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }

}
