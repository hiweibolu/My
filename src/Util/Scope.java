package Util;

import Util.error.semanticError;
import Util.error.internalError;
import Util.RegIdAllocator;

import java.util.HashMap;
import java.util.ArrayList;

import IR.IRRegIdentifier;

public class Scope {

    public RegIdAllocator regIdAllocator;

    public HashMap<String, Type> variables;
    private HashMap<String, Integer> variable_ids;
    private HashMap<String, IRRegIdentifier> variablesRegId;
	private HashMap<String, Type> functions;
	private HashMap<String, String> function_names;
	private HashMap<String, Integer> function_inclasses = new HashMap<>();
	private HashMap<String, ArrayList<Type> > params;
    private Scope parentScope;

	public int id_num;
	public String class_name = null;

    public Scope(Scope parentScope) {
        this.parentScope = parentScope;
		variables = new HashMap<>();
		variable_ids = new HashMap<>();
		id_num = 0;
        variablesRegId = new HashMap<>();
		functions = new HashMap<>();
		function_names = new HashMap<>();
		params = new HashMap<>();
        if (parentScope != null) regIdAllocator = parentScope.regIdAllocator;
        else regIdAllocator = null;
    }

    public Scope parentScope() {
        return parentScope;
    }
	
	public IRRegIdentifier defineVariable(String name, Type t, position pos, int typ) {
        if (variables.containsKey(name))
            throw new semanticError("Variables redefine", pos);
        variables.put(name, t);
		variable_ids.put(name, id_num++);
		IRRegIdentifier result = regIdAllocator.alloc(typ);
        variablesRegId.put(name, result);
		return result;
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
    public int getIdVariable(String name, boolean lookUpon) {
        if (variable_ids.containsKey(name)) return variable_ids.get(name);
        else if (parentScope != null && lookUpon)
            return parentScope.getIdVariable(name, true);
        return 0;
    }
    public IRRegIdentifier getRegIdVariable(String name, boolean lookUpon) {
        if (variablesRegId.containsKey(name)) return variablesRegId.get(name);
        else if (parentScope != null && lookUpon)
            return parentScope.getRegIdVariable(name, true);
        throw new internalError("Variable regId not found.", new position(0, 0));
    }
	
	public void defineFunction(String name, Type t, position pos) {
        if (functions.containsKey(name))
            throw new semanticError("Functions redefine", pos);
        functions.put(name, t);
		if (class_name != null){
			if (class_name.equals("!array")) function_names.put(name, "my_array_size");
			else function_names.put(name, "my_c_" + class_name + "_" + name);
			function_inclasses.put(name, 1);
		}else{
			function_names.put(name, name);
			function_inclasses.put(name, 0);
		}
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
    public Integer getInClassFunction(String name, boolean lookUpon) {
        if (function_inclasses.containsKey(name)) return function_inclasses.get(name);
        else if (parentScope != null && lookUpon)
            return parentScope.getInClassFunction(name, true);
        return null;
    }
    
    public String getNameFunction(String name, boolean lookUpon) {
        if (function_names.containsKey(name)) return function_names.get(name);
        else if (parentScope != null && lookUpon)
            return parentScope.getNameFunction(name, true);
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