package IR;

import java.util.ArrayList;
import Util.RegIdAllocator;

import IR.IRLine.lineType;

public class IRBlock {

    public ArrayList<IRLine> lines = new ArrayList<>();
    public RegIdAllocator regIdAllocator = null;
	public int maxParamsNumber = 0;
	public String name;
	public boolean containsCALL = false;
	public int returnLabel;

    public IRBlock() {
    }

    public void print(){
        lines.forEach(l -> l.print());
    }
	private int now_local;

	public void expand(){
		ArrayList<IRLine> new_lines = new ArrayList<>();
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			int j = 0;
			boolean no_need_add = false;
			switch (now_line.lineCode){
				default:
				case SW:
					j = 1;
				case BNEQ:
				case LW:
					for (; j < now_line.args.size(); j++){
						IRRegIdentifier regId = now_line.args.get(j);
						IRLine line;
						IRRegIdentifier temp;
						switch (regId.typ){
							case 6:
								temp = regIdAllocator.alloc(5);
								line = new IRLine(lineType.LW);
								line.args.add(temp);
								line.args.add(new IRRegIdentifier(regId.id, 5, false));
								new_lines.add(line);
								now_line.args.set(j, temp);
								break;
							case 1:
							case 4:
								temp = regIdAllocator.alloc(5);
								line = new IRLine(lineType.LW);
								line.args.add(temp);
								line.args.add(regId);
								new_lines.add(line);
								now_line.args.set(j, temp);
								break;
							case 2:
								temp = regIdAllocator.alloc(5);
								line = new IRLine(lineType.LOAD);
								line.args.add(temp);
								line.args.add(regId);
								new_lines.add(line);
								line = new IRLine(lineType.LW);
								line.args.add(temp);
								line.args.add(regId);
								line.args.add(temp);
								new_lines.add(line);
								now_line.args.set(j, temp);
								break;
							case 9:
								temp = regIdAllocator.alloc(5);
								line = new IRLine(lineType.LOAD);
								line.args.add(temp);
								line.args.add(regId);
								new_lines.add(line);
								/*line = new IRLine(lineType.ADDI);
								line.args.add(temp);
								line.args.add(regId);
								new_lines.add(line);*/
								now_line.args.set(j, temp);
								no_need_add = true;
						}
					}
					break;
				case CALL:
					containsCALL = true;
					break;
				case RETURN:
				case FUNC:
				case LABEL:
				case JUMP:
			}
			if (!no_need_add) new_lines.add(now_line);
			switch (now_line.lineCode){
				default:
				case SW:
					IRRegIdentifier regId = now_line.args.get(0);
					IRLine line;
					IRRegIdentifier temp, temp2;
					switch (regId.typ){
						case 3:
							if (regId.id < 6){
								break;
							}
						case 6:
							temp = regIdAllocator.alloc(5);
							line = new IRLine(lineType.SW);
							line.args.add(temp);
							line.args.add(new IRRegIdentifier(regId.id, 5, false));
							new_lines.add(line);
							now_line.args.set(0, temp);
							break;
						case 1:
						case 4:
							temp = regIdAllocator.alloc(5);
							line = new IRLine(lineType.SW);
							line.args.add(temp);
							line.args.add(regId);
							new_lines.add(line);
							now_line.args.set(0, temp);
							break;
						case 2:
							temp = regIdAllocator.alloc(5);
							temp2 = regIdAllocator.alloc(5);
							line = new IRLine(lineType.LOAD);
							line.args.add(temp2);
							line.args.add(regId);
							new_lines.add(line);
							line = new IRLine(lineType.SW);
							line.args.add(temp);
							line.args.add(regId);
							line.args.add(temp2);
							new_lines.add(line);
							now_line.args.set(0, temp);
							break;
					}
				case BNEQ:
				case FUNC:
				case LABEL:
				case JUMP:
				case CALL:
				case RETURN:
				case LW:
			}
		}
		lines = new_lines;
		now_local = regIdAllocator.size(1);
	}
	public void expandLocal(){
		ArrayList<IRLine> new_lines = new ArrayList<>();
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			int j = 0;
			switch (now_line.lineCode){
				default:
				case SW:
					j = 1;
				case BNEQ:
				case LW:
					for (; j < now_line.args.size(); j++){
						IRRegIdentifier regId = now_line.args.get(j);
						IRLine line;
						IRRegIdentifier temp;
						switch (regId.typ){
							case 1:
								if (regId.id < now_local) break;
								temp = regIdAllocator.alloc(5);
								line = new IRLine(lineType.LW);
								line.args.add(temp);
								line.args.add(regId);
								new_lines.add(line);
								now_line.args.set(j, temp);
								break;
						}
					}
					break;
				case CALL:
				case RETURN:
				case FUNC:
				case LABEL:
				case JUMP:
			}
			new_lines.add(now_line);
			switch (now_line.lineCode){
				default:
				case SW:
					IRRegIdentifier regId = now_line.args.get(0);
					IRLine line;
					IRRegIdentifier temp;
					switch (regId.typ){
						case 1:
							if (regId.id < now_local) break;
							temp = regIdAllocator.alloc(5);
							line = new IRLine(lineType.SW);
							line.args.add(temp);
							line.args.add(regId);
							new_lines.add(line);
							now_line.args.set(0, temp);
							break;
					}
				case BNEQ:
				case FUNC:
				case LABEL:
				case JUMP:
				case CALL:
				case RETURN:
				case LW:
			}
		}
		lines = new_lines;
	}

	public int[] free_reg = new int [32];
	public int[] used;
	public IRRegIdentifier[] used_reg, used_l_reg;
	public int paramRAM;

	public int get_free_reg(){
		for (int i = 0; i < 32; i++)
			if (free_reg[i] == 1) return i;
		return 0;
	}
	public void easyAlloc(IRLine now_line, int l, int r){
		for (int j = l; j < r; j++){
			IRRegIdentifier regId = now_line.args.get(j), temp;
			if (regId.typ == 5){
				if (used_reg[regId.id] == null){
					if (used_l_reg[regId.id] != null){
						temp = used_l_reg[regId.id];
					}else{
						int t = get_free_reg();
						if (t > 0){
							free_reg[t] = 0;
							temp = used_reg[regId.id] = new IRRegIdentifier(t, 0, false);
						}else{
							temp = used_l_reg[regId.id] = regIdAllocator.alloc(1);
						}
					}
				}else temp = used_reg[regId.id];
				temp.useId = regId.id;
				now_line.args.set(j, temp);
			}
		}
	}
	public void easyRelease(IRLine now_line, int l, int r){
		for (int j = l; j < r; j++){
			IRRegIdentifier regId = now_line.args.get(j), temp;
			if (regId.useId != null){
				if (used_l_reg[regId.useId] == null){
					if (--used[regId.useId] == 0){
						free_reg[used_reg[regId.useId].id] = 1;
					}
				}
			}
		}
	}
	public void alloc(){
		int temp_size = regIdAllocator.size(5);
		used = new int [temp_size];
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			for (int j = 0; j < now_line.args.size(); j++){
				IRRegIdentifier regId = now_line.args.get(j);
				if (regId.typ == 5) used[regId.id]++;
			}
		}
		used_reg = new IRRegIdentifier [temp_size];
		used_l_reg = new IRRegIdentifier [temp_size];
		for (int i = 0; i < 32; i++) free_reg[i] = 0;
		for (int i = 10; i <= 15; i++) free_reg[i] = 1;
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			
			switch (now_line.lineCode){
				default:
				case LW:
					easyAlloc(now_line, 1, now_line.args.size());
					easyRelease(now_line, 1, now_line.args.size());
					IRRegIdentifier temp = now_line.args.get(0);
					if (temp.typ == 3){
						if (temp.id < 6){
							free_reg[temp.id + 10] = 0;
							now_line.args.set(0, new IRRegIdentifier(temp.id + 10, 0, false));
						}else{
							now_line.args.set(0, regIdAllocator.alloc(7));
						}
					}else{
						easyAlloc(now_line, 0, 1);
						easyRelease(now_line, 0, 1);
					}
					break;
				case BNEQ:
				case SW:
					easyAlloc(now_line, 0, now_line.args.size());
					easyRelease(now_line, 0, now_line.args.size());
					break;
				case CALL:
					for (int j = 0; j < temp_size; j++){
						if (used[j] > 0){
							if (used_reg[j] != null){
								//System.out.println("hello");
								IRRegIdentifier new_reg = regIdAllocator.alloc(1);
								used_reg[j].id = new_reg.id;
								used_reg[j].typ = new_reg.typ;
								used_reg[j].pointer = new_reg.pointer;
								used[j] = 0;
							}
						}
					}
					for (int j = 10; j <= 15; j++){
						free_reg[j] = 1;
					}
					break;
				case FUNC:
				case LABEL:
				case JUMP:
				case RETURN:
			}
		}
	}
	public void allocLocal(){
		int temp_size = regIdAllocator.size(5);
		used = new int [temp_size];
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			for (int j = 0; j < now_line.args.size(); j++){
				IRRegIdentifier regId = now_line.args.get(j);
				if (regId.typ == 5) used[regId.id]++;
			}
		}
		used_reg = new IRRegIdentifier [temp_size];
		used_l_reg = new IRRegIdentifier [temp_size];
		for (int i = 0; i < 32; i++) free_reg[i] = 0;
		for (int i = 16; i <= 17; i++) free_reg[i] = 1;
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			switch (now_line.lineCode){
				default:
				case LW:
					easyAlloc(now_line, 1, now_line.args.size());
					easyRelease(now_line, 1, now_line.args.size());
					easyAlloc(now_line, 0, 1);
					easyRelease(now_line, 0, 1);
					break;
				case BNEQ:
				case SW:
					easyAlloc(now_line, 0, now_line.args.size());
					easyRelease(now_line, 0, now_line.args.size());
					break;
				case CALL:
					for (int j = 16; j <= 17; j++){
						free_reg[j] = 1;
					}
					break;
				case FUNC:
				case LABEL:
				case JUMP:
				case RETURN:
			}
		}
	}

	public void remove(){
		ArrayList<IRLine> new_lines = new ArrayList<>();
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			if (now_line.lineCode == lineType.MOVE){
				if (now_line.args.get(0).id == now_line.args.get(1).id){
					continue;
				}
			}
			new_lines.add(now_line);
		}
		lines = new_lines;
	}

	public int totalRAM, realRAM, addrStartLocal;
	public void calcRAM(){
		totalRAM = regIdAllocator.size(1) + regIdAllocator.size(7);
		addrStartLocal = 0;
		totalRAM++;
		addrStartLocal -= 4; // store s0
		if (containsCALL){
			totalRAM++;
			addrStartLocal -= 4; // store ra
		}
		realRAM = ((totalRAM - 1) / 4 + 1) * 16;

	}

	public int addrLocal(int id){
		return addrStartLocal + (id + 1) * -4;
	}
	public int addrParam(int id){
		return id * 4;
	}

	public void printASM(){
		System.out.println("\t.text");
		System.out.println("\t.align\t2");
		System.out.println("\t.globl\t" + name);
		System.out.println("\t.type\t" + name + ", @function");
		System.out.println(name + ":");

		System.out.println("\taddi\tsp,sp,-" + String.valueOf(realRAM));
		System.out.println("\tsw\ts0," + String.valueOf(realRAM - 4) + "(sp)");
		if (containsCALL) System.out.println("\tsw\tra," + String.valueOf(realRAM - 8) + "(sp)");
		System.out.println("\taddi\ts0,sp," + String.valueOf(realRAM));
		
        lines.forEach(l -> l.printASM(this));

		System.out.println(".LAB" + String.valueOf(returnLabel) + ":");
		System.out.println("\tlw\ts0," + String.valueOf(realRAM - 4) + "(sp)");
		if (containsCALL) System.out.println("\tlw\tra," + String.valueOf(realRAM - 8) + "(sp)");
		System.out.println("\taddi\tsp,sp," + String.valueOf(realRAM));
		System.out.println("\tjr\tra");
		
		System.out.println("\t.size\t" + name + ", .-" + name);
	}
}
