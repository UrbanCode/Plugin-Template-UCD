/*
* Licensed Materials - Property of IBM Corp.
* IBM UrbanCode Build
* IBM UrbanCode Deploy
* IBM UrbanCode Release
* IBM AnthillPro
* (c) Copyright IBM Corporation 2002, 2014. All Rights Reserved.
*
* U.S. Government Users Restricted Rights - Use, duplication or disclosure restricted by
* GSA ADP Schedule Contract with IBM Corp.
*/
/**
 *
 */
package com.urbancode.air

import groovy.lang.Closure;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author jwa
 *
 */
public class FileSet {

    //**************************************************************************
    // CLASS
    //**************************************************************************

    //**************************************************************************
    // INSTANCE
    //**************************************************************************

    def isWindows = (System.getProperty('os.name') =~ /(?i)windows/).find()
    def base
    def includes = []
    def excludes = []

    public FileSet(base) {
        if (base instanceof File) {
            this.base = base
        }
        else if (base == null) {
            this.base = new File('.').absolutePath
        }
        else {
            this.base = new File(base)
        }
    }

    public def include(antPattern) {
        forNonEmptyLines(antPattern) {
            includes << convertToPattern(it)
        }
    }

    public def exclude(antPattern) {
        forNonEmptyLines(antPattern) {
            excludes << convertToPattern(it)
        }
    }

    public def each(Closure closure) {
        base.eachFileRecurse { file ->
            def path = file.path.replace('\\', '/').substring(base.path.length())
            def matches = false
            for (p in includes) {
                if (path =~ p) {
                    matches = true
                    break;
                }
            }
            if (matches) {
                for (p in excludes) {
                    if (path =~ p) {
                        matches = false;
                        break;
                    }
                }
            }
            if (matches) {
                closure(file)
            }
        }
    }

    public def files() {
        def list = []
        each { list << it }
        return list
    }

    private forNonEmptyLines(strings, Closure closure) {
        if (strings instanceof Collection) {
            strings.each {
                def trimmed = it?.trim()
                if (trimmed?.length() > 0) {
                    closure(it)
                }
            }
        }
        else if (strings != null) {
            forNonEmptyLines(strings.readLines(), closure)
        }
    }

    private convertToPattern(antPattern) {
        // normalize file separator in pattern
        antPattern = antPattern.replace('\\', '/');

        // ensure leading / character from pattern
        def pattern = antPattern.startsWith('/') ? antPattern : '/'+antPattern

        // deal with special regex-characters that should be interpreted as literals
        '\\.+[]^${}|()'.toCharArray().each{ c ->
            pattern = pattern.replace(''+c, '\\'+c)
        }
        pattern = pattern.replace('?', '.') // ? is a single-char wildcard

        // deal with ant-style wildcards
        StringBuffer result = new StringBuffer()
        result.append("^")
        def m = (pattern =~ '\\*\\*/|\\*\\*|\\*')
        while (m) {
            def token = m.group()
            def replacement;
            if (token == '**/') {
                replacement = '.*(?<=/)'
            }
            else if (token == '**') {
                replacement = '.*'
            }
            else {
                replacement = '[^/]*'
            }
            m.appendReplacement(result, Matcher.quoteReplacement(replacement))
        }
        m.appendTail(result)
        result.append("\$")
        def flags = 0
        if (isWindows) {
            flags |= Pattern.CASE_INSENSITIVE
        }
        return Pattern.compile(result.toString(), flags)
    }
}
