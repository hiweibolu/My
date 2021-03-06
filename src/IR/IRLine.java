package IR;

import java.util.ArrayList;

public class IRLine {

    public enum lineType{
		FUNC, LABEL, MOVE, JUMP, CALL,
		BNEQ, BEQ, BLE, BLT, BGE, BGT,
		NEG, NOT, LOGICNOT,
		EQ, NEQ, GE, GEQ, LE, LEQ,
		ADD, SUB, MUL, DIV, MOD,
		OR, AND, XOR, SHL, SHR,
		INDEX, LOAD, LOADSTRING, RETURN,
		ADDI, ANDI, SLTI, EQI, MULI, LW, SW
	}
	public lineType lineCode;
    public ArrayList<IRRegIdentifier> args = new ArrayList<>();
	public int label = 0, add_up = 0;
	public String func = null;
	public IRBlock block = null;
	public boolean expanded = false, calling = false, ignored = false;

    public IRLine(lineType lineCode) {
        this.lineCode = lineCode;
    }
	public void print(){
		switch (lineCode){
			case FUNC: System.out.print("FUNC"); break;
			case LABEL: System.out.print("LABEL"); break;
			case MOVE: System.out.print("\tMOVE"); break;
			case JUMP: System.out.print("\tJUMP"); break;
			case CALL: System.out.print("\tCALL"); break;
			case BNEQ: System.out.print("\tBNEQ"); break;
			case BEQ: System.out.print("\tBEQ"); break;
			case BLE: System.out.print("\tBLE"); break;
			case BLT: System.out.print("\tBLT"); break;
			case BGE: System.out.print("\tBGE"); break;
			case BGT: System.out.print("\tBGT"); break;
			case NEG: System.out.print("\tNEG"); break;
			case NOT: System.out.print("\tNOT"); break;
			case LOGICNOT: System.out.print("\tLOGICNOT"); break;
			case NEQ: System.out.print("\tNEQ"); break;
			case GE: System.out.print("\tGE"); break;
			case GEQ: System.out.print("\tGEQ"); break;
			case EQ: System.out.print("\tEQ"); break;
			case LE: System.out.print("\tLE"); break;
			case LEQ: System.out.print("\tLEQ"); break;
			case ADD: System.out.print("\tADD"); break;
			case SUB: System.out.print("\tSUB"); break;
			case MUL: System.out.print("\tMUL"); break;
			case DIV: System.out.print("\tDIV"); break;
			case MOD: System.out.print("\tMOD"); break;
			case OR: System.out.print("\tOR"); break;
			case AND: System.out.print("\tAND"); break;
			case XOR: System.out.print("\tXOR"); break;
			case SHL: System.out.print("\tSHL"); break;
			case SHR: System.out.print("\tSHR"); break;
			case INDEX: System.out.print("\tINDEX"); break;
			case LOAD: System.out.print("\tLOAD"); break;
			case LOADSTRING: System.out.print("\tLOADSTRING"); break;
			case RETURN: System.out.print("\tRETURN"); break;
			case ADDI: System.out.print("\tADDI"); break;
			case SLTI: System.out.print("\tSLTI"); break;
			case ANDI: System.out.print("\tANDI"); break;
			case MULI: System.out.print("\tMULI"); break;
			case EQI: System.out.print("\tEQI"); break;
			case LW: System.out.print("\tLW"); break;
			case SW: System.out.print("\tSW"); break;
		}
		if (label > 0){
			System.out.print("(");
			System.out.print(label);
			System.out.print(")");
		}
		if (func != null){
			System.out.print(" ");
			System.out.print(func);
		}
		for (IRRegIdentifier arg : args){
			System.out.print(" ");
			arg.print();
		}
		System.out.println();
	}

	public String labelASM(IRBlock block){
		return ".b" + block.id + "l" + label;
	}

	public void printASM(IRBlock block){
		switch (lineCode){
			case FUNC: break;
			case LABEL:
				System.out.println(labelASM(block) + ":");
				break;
			case MOVE:
				System.out.print("\tmv\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.println(args.get(1).toASM());
				break;
			case JUMP:
				System.out.println("\tj\t" + labelASM(block));
				break;
			case CALL:
				System.out.println("\tcall\t" + func);
				break;
			case BNEQ:
				System.out.print("\tbeq\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println(labelASM(block));
				break;
			case BEQ:
				System.out.print("\tbne\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println(labelASM(block));
				break;
			case BLE:
				System.out.print("\tbge\t");
				System.out.print(args.get(1).toASM() + ",");
				System.out.print(args.get(0).toASM() + ",");
				System.out.println(labelASM(block));
				break;
			case BLT:
				System.out.print("\tblt\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println(labelASM(block));
				break;
			case BGE:
				System.out.print("\tbge\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println(labelASM(block));
				break;
			case BGT:
				System.out.print("\tblt\t");
				System.out.print(args.get(1).toASM() + ",");
				System.out.print(args.get(0).toASM() + ",");
				System.out.println(labelASM(block));
				break;
			case NEG:
				System.out.print("\tneg\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.println(args.get(1).toASM());
				break;
			case NOT:
				System.out.print("\tnot\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.println(args.get(1).toASM());
				break;
			case LOGICNOT:
				System.out.print("\tseqz\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.println(args.get(1).toASM());
				break;
			case NEQ:
				System.out.print("\tsub\t");
				System.out.print("t5" + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println(args.get(2).toASM());
				System.out.print("\tsnez\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.println("t5");
				break;
			case EQ:
				System.out.print("\tsub\t");
				System.out.print("t5" + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println(args.get(2).toASM());
				System.out.print("\tseqz\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.println("t5");
				break;
			case GE:
				System.out.print("\tsgt\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println(args.get(2).toASM());
				break;
			case LE:
				System.out.print("\tslt\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println(args.get(2).toASM());
				break;
			case GEQ:
				System.out.print("\tslt\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println(args.get(2).toASM());
				System.out.print("\txori\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(0).toASM() + ",");
				System.out.println(1);
				break;
			case LEQ:
				System.out.print("\tsgt\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println(args.get(2).toASM());
				System.out.print("\txori\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(0).toASM() + ",");
				System.out.println(1);
				break;
			case ADD:
				System.out.print("\tadd\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println(args.get(2).toASM());
				break;
			case SUB:
				System.out.print("\tsub\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println(args.get(2).toASM());
				break;
			case MUL:
				System.out.print("\tmul\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println(args.get(2).toASM());
				break;
			case DIV:
				System.out.print("\tdiv\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println(args.get(2).toASM());
				break;
			case MOD:
				System.out.print("\trem\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println(args.get(2).toASM());
				break;
			case OR:
				System.out.print("\tor\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println(args.get(2).toASM());
				break;
			case AND:
				System.out.print("\tand\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println(args.get(2).toASM());
				break;
			case XOR:
				System.out.print("\txor\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println(args.get(2).toASM());
				break;
			case SHL:
				System.out.print("\tsll\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println(args.get(2).toASM());
				break;
			case SHR:
				System.out.print("\tsra\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println(args.get(2).toASM());
				break;
			case INDEX:
				System.out.print("\tslli\t");
				System.out.print("t6,");
				System.out.print(args.get(2).toASM() + ",");
				System.out.println(2);
				System.out.print("\tadd\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println("t6");
				break;
			case LOADSTRING:
			case LOAD:
				switch (args.get(1).typ){
					case 2:
						System.out.print("\tlui\t");
						System.out.print(args.get(0).toASM() + ",");
						System.out.println("%hi(" + args.get(1).toGASM() + ")");
						break;
					case 8:
						System.out.print("\tli\t");
						System.out.print(args.get(0).toASM() + ",");
						System.out.println(args.get(1).id);
						break;
					case 9:
						System.out.print("\tlui\t");
						System.out.print(args.get(0).toASM() + ",");
						System.out.println("%hi(" + args.get(1).toSASM() + ")");
						System.out.print("\taddi\t");
						System.out.print(args.get(0).toASM() + ",");
						System.out.print(args.get(0).toASM() + ",");
						System.out.println("%lo(" + args.get(1).toSASM() + ")");
						break;
				}
				break;
			case RETURN: System.out.print("\tRETURN"); break;
			case ADDI:
				if (args.get(2).id > 2000 || args.get(2).id < -2000){
					System.out.println("\tli\tt6," + args.get(2).id);
					System.out.print("\tadd\t");
					System.out.print(args.get(0).toASM() + ",");
					System.out.print(args.get(1).toASM() + ",");
					System.out.println("t6");
				}else{
					System.out.print("\taddi\t");
					System.out.print(args.get(0).toASM() + ",");
					System.out.print(args.get(1).toASM() + ",");
					System.out.println(args.get(2).id);
				}
				break;
			case SLTI:
				if (args.get(2).id > 2000 || args.get(2).id < -2000){
					System.out.println("\tli\tt6," + args.get(2).id);
					System.out.print("\tslt\t");
					System.out.print(args.get(0).toASM() + ",");
					System.out.print(args.get(1).toASM() + ",");
					System.out.println("t6");
				}else{
					System.out.print("\tslti\t");
					System.out.print(args.get(0).toASM() + ",");
					System.out.print(args.get(1).toASM() + ",");
					System.out.println(args.get(2).id);
				}
				break;
			case ANDI:
				if (args.get(2).id > 2000 || args.get(2).id < -2000){
					System.out.println("\tli\tt6," + args.get(2).id);
					System.out.print("\tand\t");
					System.out.print(args.get(0).toASM() + ",");
					System.out.print(args.get(1).toASM() + ",");
					System.out.println("t6");
				}else{
					System.out.print("\tandi\t");
					System.out.print(args.get(0).toASM() + ",");
					System.out.print(args.get(1).toASM() + ",");
					System.out.println(args.get(2).id);
				}
				break;
			case MULI:
				System.out.println("\tli\tt6," + args.get(2).id);
				System.out.print("\tmul\t");
				System.out.print(args.get(0).toASM() + ",");
				System.out.print(args.get(1).toASM() + ",");
				System.out.println("t6");
				break;
			case EQI:
				if (args.get(2).id > 2000 || args.get(2).id < -2000){
					System.out.println("\tli\tt6," + args.get(2).id);
					System.out.print("\tadd\t");
					System.out.print("t5" + ",");
					System.out.print(args.get(1).toASM() + ",");
					System.out.println("t6");
					System.out.print("\tseqz\t");
					System.out.print(args.get(0).toASM() + ",");
					System.out.println("t5");
				}else{
					System.out.print("\taddi\t");
					System.out.print("t5" + ",");
					System.out.print(args.get(1).toASM() + ",");
					System.out.println(args.get(2).id);
					System.out.print("\tseqz\t");
					System.out.print(args.get(0).toASM() + ",");
					System.out.println("t5");
				}
				break;
			case SW:
				System.out.print("\tsw\t");
				System.out.print(args.get(0).toASM() + ",");
				switch (args.get(1).typ){
					case 0:
						System.out.print(0);
						System.out.println("(" + args.get(1).toASM() + ")");
						break;
					case 12:
						System.out.print(block.addrLocal(args.get(1).id) + block.realRAM);
						System.out.println("(sp)");
						break;
					case 2:
						System.out.print("%lo(" + args.get(1).toGASM() + ")");
						System.out.println("(" + args.get(2).toASM() + ")");
						break;
					case 4:
						System.out.print(block.addrParam(args.get(1).id) + block.realRAM);
						System.out.println("(sp)");
						break;
					case 7:
						System.out.print(block.addrParam(args.get(1).id));
						System.out.println("(sp)");
						break;
				}
				break;
			case LW:
				System.out.print("\tlw\t");
				System.out.print(args.get(0).toASM() + ",");
				switch (args.get(1).typ){
					case 0:
						System.out.print(0);
						System.out.println("(" + args.get(1).toASM() + ")");
						break;
					case 12:
						System.out.print(block.addrLocal(args.get(1).id) + block.realRAM);
						System.out.println("(sp)");
						break;
					case 2:
						System.out.print("%lo(" + args.get(1).toGASM() + ")");
						System.out.println("(" + args.get(2).toASM() + ")");
						break;
					case 4:
						System.out.print(block.addrParam(args.get(1).id) + block.realRAM);
						System.out.println("(sp)");
						break;
				}
				break;
		}
	}
}
