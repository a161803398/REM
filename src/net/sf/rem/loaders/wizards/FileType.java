/*
 * FileType.java
 *
 * Created on February 27, 2007, 2:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.rem.loaders.wizards;

/**
 * This file is copied from module org/netbeans/web/core.
 * @author magic
 */
class FileType { 
    private String name, suffix; 

    private FileType(String name, String suffix) { 
	this.name = name; 
	this.suffix = suffix; 
    } 

    public String toString() { return name; } 

    public String getSuffix() { return suffix; }

    public static final FileType ZUL = 
	new FileType("zul", "zul"); 

    public static final FileType ZS = 
	new FileType("zs", "zs"); 

} 
