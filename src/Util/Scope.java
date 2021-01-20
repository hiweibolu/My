package Util;

import Util.error.semanticError;

import java.util.HashMap;
import java.util.ArrayList;

public class Scope {
    private HashMap<String, Type> variables;
	private HashMap<String, Type> functions;
	private HashMap<String, ArrayList<Type> > params;
    private Scope parentScope;

    public Scope(Scope parentScope) {
        this.parentScope = parentScope;
		variables = new HashMap<>();
		functions = new HashMap<>();
		params = new HashMap<>();
    }

    public Scope parentScope() {
        return parentScope;
    }
	
	public void defineVariable(String name, Type t, position pos) {
        if (variables.containsKey(name))
            throw new semanticError("Variables redefine", pos);
        variables.put(name, t);
    }
    public boolean containsVariable(String name, boolean lookUpon) {
        if (variables.containsKey(name)) return true;
        else if (parentScope != null && lookUpon)
            return parentScope.containsVariable(name, true);
        else return false;
    }
    public Type getTypeVariable(String name, boolean lookUpon) {
        if (variables.containsKey(name)) return variables.get(name);
        else if (parentScope != null && lookUpon)
            return parentScope.getTypeVariable(name, true);
        return null;
    }
	
	public void defineFunction(String name, Type t, position pos) {
        if (functions.containsKey(name))
            throw new semanticError("Functions redefine", pos);
        functions.put(name, t);
    }
    public boolean containsFunction(String name, boolean lookUpon) {
        if (functions.containsKey(name)) return true;
        else if (parentScope != null && lookUpon)
            return parentScope.containsFunction(name, true);
        else return false;
    }
    public Type getTypeFunction(String name, boolean lookUpon) {
        if (functions.containsKey(name)) return functions.get(name);
        else if (parentScope != null && lookUpon)
            return parentScope.getTypeFunction(name, true);
        return null;
    }
	
	public void defineParams(String name, ArrayList<Type> t, position pos) {
		params.put(name, t);
	}
	public ArrayList<Type> getParams(String name, boolean lookUpon) {
		if (params.containsKey(name)) return params.get(name);
		else if (parentScope != null && lookUpon)
			return parentScope.getParams(name, true);
		return null;
	}

}