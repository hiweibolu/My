package IR;

public class IRRegIdentifier{
	public int id, typ;
	public Integer useId = null;
	public boolean pointer, mult = false;

	public IRRegIdentifier(int id, int typ, boolean pointer){
		this.id = id;
		this.typ = typ;
		this.pointer = pointer;
		if (pointer){
			this.typ = 6;
		}
	}

	public void assign(IRRegIdentifier other){
		id = other.id;
		typ = other.typ;
		mult = other.mult;
	}

	public void print(){
		//if (pointer) System.out.print("*");
		switch (typ){
			case 1: System.out.print("L"); break;
			case 2: System.out.print("G"); break;
			case 3: System.out.print("P"); break;
			case 4: System.out.print("F"); break;
			case 5: System.out.print("T"); break;
			case 6: System.out.print("*T"); break;
			case 7: System.out.print("LP"); break;
			case 8: System.out.print("i"); break;
			case 9: System.out.print("S"); break;
			case 10: System.out.print("Q"); break;
			case 11: System.out.print("C"); break;
			case 12: System.out.print("Spill"); break;
		}
		System.out.print(id);
	}

	public String toGASM(){
		return ".G" + String.valueOf(id);
	}

	public String toSASM(){
		return ".LS" + String.valueOf(id);
	}

	public String toASM(){
		String result = null;
		switch (id){
			case 0: result = "zero"; break;
			case 1: result = "ra"; break;
			case 2: result = "sp"; break;
			case 3: result = "gp"; break;
			case 4: result = "tp"; break;
			case 5: result = "t0"; break;
			case 6: result = "t1"; break;
			case 7: result = "t2"; break;
			case 8: result = "s0"; break;
			case 9: result = "s1"; break;
			case 10: result = "a0"; break;
			case 11: result = "a1"; break;
			case 12: result = "a2"; break;
			case 13: result = "a3"; break;
			case 14: result = "a4"; break;
			case 15: result = "a5"; break;
			case 16: result = "a6"; break;
			case 17: result = "a7"; break;
			case 18: result = "s2"; break;
			case 19: result = "s3"; break;
			case 20: result = "s4"; break;
			case 21: result = "s5"; break;
			case 22: result = "s6"; break;
			case 23: result = "s7"; break;
			case 24: result = "s8"; break;
			case 25: result = "s9"; break;
			case 26: result = "s10"; break;
			case 27: result = "s11"; break;
			case 28: result = "t3"; break;
			case 29: result = "t4"; break;
			case 30: result = "t5"; break;
			case 31: result = "t6"; break;
		}
		return result;
	}
}