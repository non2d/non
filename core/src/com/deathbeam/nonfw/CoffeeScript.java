/*
 * The MIT License
 *
 * Copyright 2014 Thomas Slusny.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.deathbeam.nonfw;

import com.badlogic.gdx.files.FileHandle;
import java.io.IOException;
import javax.script.*;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 *
 * @author Thomas Slusny
 */
public final class CoffeeScript extends ScriptRuntime {
    private Scriptable scope;
    
    public static String getName() {
        return "CoffeeScript";
    }

    public static String getExtension() {
        return "coffee";
    }
    
    public CoffeeScript() {
        Context context = Context.enter();
        context.setOptimizationLevel(-1);
        try {
            scope = context.initStandardObjects();
            context.evaluateString(scope, Utils.readFile(Utils.getInternalResource("langs/coffeescript.js").read()), "coffeescript.js", 0, null);
        } catch (IOException ex) {
            Utils.error("scripting", ex.getMessage());
        } finally {
            Context.exit();
        }
        
        e = new ScriptEngineManager().getEngineByName("JavaScript");
        ScriptEngineFactory f = e.getFactory();
        
        System.out.println( "Engine name: " +f.getEngineName() );
        System.out.println( "Engine Version: " +f.getEngineVersion() );
        System.out.println( "LanguageName: CoffeeScript" );
        System.out.println( "Language Version: 1.8.0" );
    }
    
    @Override
    public void invoke(String funct) {
        try {
            e.eval(funct + "();");
        } catch (ScriptException ex) {
            Utils.log("scripting", ex.getMessage());
        }
    }
    
    @Override
    public void invoke(String funct, String args) {
        try {
            e.eval(funct + "(" + args + ");");
        } catch (ScriptException ex) {
            Utils.log("scripting", ex.getMessage());
        }
    }
    
    @Override
    public void invoke(String funct, String arg1, String arg2) {
        try {
            e.eval(funct + "(" + arg1 + "," + arg2 + ");");
        } catch (ScriptException ex) {
            Utils.log("scripting", ex.getMessage());
        }
    }

    @Override
    public Object eval(FileHandle file) {
        try {
            return e.eval(compile(Utils.readFile(file.read())));
        } catch (ScriptException ex) {
            Utils.warning("Scripting", ex.getMessage());
        } catch (IOException ex) {
            Utils.error("Resource not found", ex.getMessage());
        }
        return null;
    }
    
    private String compile (String script) {
        Context context = Context.enter();
        try {
            Scriptable compileScope = context.newObject(scope);
            compileScope.setParentScope(scope);
            compileScope.put("script", compileScope, script);
            return (String)context.evaluateString(compileScope, "CoffeeScript.compile(script);", "CoffeeScriptCompiler", 1, null);
        } finally {
            Context.exit();
        }
    }
}