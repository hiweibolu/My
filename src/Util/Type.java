package Util;

import java.util.HashMap;

public class Type {
	public String name;
	public int dimension;
	public boolean isFunc;
	
	public Type(String name){
		this.name = name;
		this.dimension = 0;
		this.isFunc = false;
	}
	public Type(String name, int dimension){
		this.name = name;
		this.dimension = dimension;
		this.isFunc = false;
	}
	public Type(Type other){
		this.name = other.name;
		this.dimension = other.dimension;
		this.isFunc = false;
	}
	
	public Type getFuncType(){
		Type t = new Type(this);
		t.isFunc = true;
		return t;
	}
	public boolean equals(String name){
		return this.name.equals(name) && this.dimension == 0;
	}
	public boolean equals(String name, int dimension){
		return this.name.equals(name) && this.dimension == dimension;
	}
	public boolean equals(Type other){
		return this.name.equals(other.name) && this.dimension == other.dimension;
	}

	public boolean isInt(){ return equals("int"); }
	public boolean isBool(){ return equals("bool"); }
	public boolean isVoid(){ return equals("void"); }
	public boolean isString(){ return equals("string"); }
	public boolean isNull(){ return equals("null"); }
	public boolean isClass(){ return !isInt() && !isBool() && !isVoid() && !isString() && !isNull() && this.dimension == 0; }
	
}
