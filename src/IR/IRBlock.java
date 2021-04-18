package IR;

import java.util.ArrayList;
import Util.RegIdAllocator;
import Util.Graph;

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
				case LW:
					j = 1;
				case BNEQ:
				case BEQ:
				case SW:
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
								/*temp = regIdAllocator.alloc(5);
								line = new IRLine(lineType.LOAD);
								line.args.add(temp);
								line.args.add(regId);
								new_lines.add(line);
								now_line.args.set(j, temp);
								no_need_add = true;*/
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
				case LW:
					IRRegIdentifier regId = now_line.args.get(0);
					IRLine line;
					IRRegIdentifier temp, temp2;
					switch (regId.typ){
						case 3:
							if (regId.id < 8){
								now_line.args.set(0, new IRRegIdentifier(regId.id + 10, 0, false));
							}else{
								temp = regIdAllocator.alloc(5);
								line = new IRLine(lineType.SW);
								line.args.add(temp);
								IRRegIdentifier an_temp;
								if (regIdAllocator.size(7) < regId.id - 7){
									an_temp = regIdAllocator.alloc(7);
								}else an_temp = new IRRegIdentifier(regId.id - 8, 7, false);
								line.args.add(an_temp);
								new_lines.add(line);
								now_line.args.set(0, temp);
							}
							break;
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
				case BEQ:
				case FUNC:
				case LABEL:
				case JUMP:
				case CALL:
				case RETURN:
				case SW:
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
				case LW:
					j = 1;
				case BNEQ:
				case BEQ:
				case SW:
					for (; j < now_line.args.size(); j++){
						IRRegIdentifier regId = now_line.args.get(j);
						IRLine line;
						IRRegIdentifier temp;
						switch (regId.typ){
							case 12:
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
				case LW:
					IRRegIdentifier regId = now_line.args.get(0);
					IRLine line;
					IRRegIdentifier temp;
					switch (regId.typ){
						case 12:
							temp = regIdAllocator.alloc(5);
							line = new IRLine(lineType.SW);
							line.args.add(temp);
							line.args.add(regId);
							new_lines.add(line);
							now_line.args.set(0, temp);
							break;
					}
				case BNEQ:
				case BEQ:
				case FUNC:
				case LABEL:
				case JUMP:
				case CALL:
				case RETURN:
				case SW:
			}
		}
		lines = new_lines;
	}

	public int[] free_reg = new int [32];
	public int[] used, first_used, last_used;
	public IRRegIdentifier[] used_reg, used_l_reg;
	public int paramRAM;

	public int get_free_reg(int id){
		for (int i = 0; i < 32; i++)
			if (free_reg[i] == 1){
				boolean found = false;
				for (int j = first_used[id]; j < last_used[id]; j++){
					IRLine now_line = lines.get(j);
					if (now_line.args.size() > 0 && now_line.args.get(0).typ == 3 && now_line.args.get(0).id == i - 10) found=true;
				}
				if (!found) return i;
			}
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
						int t = get_free_reg(regId.id);
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
	/*public void alloc(){
		int temp_size = regIdAllocator.size(5);
		used = new int [temp_size];
		first_used = new int [temp_size];
		last_used = new int [temp_size];
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			for (int j = 0; j < now_line.args.size(); j++){
				IRRegIdentifier regId = now_line.args.get(j);
				if (regId.typ == 5){
					used[regId.id]++;
					if (first_used[regId.id] == 0) first_used[regId.id] = i;
					last_used[regId.id] = i;
				}
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
				case BEQ:
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
	}*/
	public void allocLocal(){
		int temp_size = regIdAllocator.size(5);
		used = new int [temp_size];
		first_used = new int [temp_size];
		last_used = new int [temp_size];

		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			for (int j = 0; j < now_line.args.size(); j++){
				IRRegIdentifier regId = now_line.args.get(j);
				if (regId.typ == 5){
					used[regId.id]++;
					if (first_used[regId.id] == 0) first_used[regId.id] = 0;
					last_used[regId.id] = 0;
				}
			}
		}
		used_reg = new IRRegIdentifier [temp_size];
		used_l_reg = new IRRegIdentifier [temp_size];
		for (int i = 0; i < 32; i++) free_reg[i] = 0;
		for (int i = 30; i <= 31; i++) free_reg[i] = 1;
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
				case BEQ:
				case SW:
					easyAlloc(now_line, 0, now_line.args.size());
					easyRelease(now_line, 0, now_line.args.size());
					break;
				case CALL:
					for (int j = 30; j <= 31; j++){
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

	//----------------------------------------------------------------------------------------------------------------------------------------------
	// Optimize Start
	//----------------------------------------------------------------------------------------------------------------------------------------------

	public boolean def_line(lineType lineCode){
		switch (lineCode){
			default:
				return true;
			case FUNC: case LABEL: case JUMP: case CALL: case BNEQ: case BEQ:
				return false;
		}
	}
	public int use_line(lineType lineCode){
		switch (lineCode){
			default:
				return 1;
			case BNEQ: case BEQ:
				return 0;
			case FUNC: case LABEL: case JUMP: case CALL:
				return -1;
		}
	}
	public int getdad(int[] dad, int x){
		if (dad[x] == 0) return x;
		return dad[x] = getdad(dad, dad[x]);
	}

	public int vis_now;
	public int[] vis, jmp_target, label_target;
	public boolean[] local_vis;
	public void jump_update(){
		jmp_target = new int[lines.size()];
		int max_label = 0;
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			if (now_line.lineCode == lineType.LABEL && max_label < now_line.label) max_label = now_line.label;
		}
		label_target = new int[max_label + 1];
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			if (now_line.lineCode == lineType.LABEL){
				label_target[now_line.label] = i;
			}
		}
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			if (now_line.lineCode == lineType.JUMP || now_line.lineCode == lineType.BEQ || now_line.lineCode == lineType.BNEQ){
				jmp_target[i] = label_target[now_line.label];
			}
		}
	}
	public void expand_opt(){
		jump_update();
	}

	public ArrayList<Integer> local_list;
	public ArrayList<ArrayList<Integer>>  local_prec;
	public int[] local_dad;
	public IRRegIdentifier[] local_temp;
	public void local_pass(int i, int id, int pos){
		while (i < lines.size() && vis[i] < vis_now){
			vis[i] = vis_now;
			IRLine now_line = lines.get(i);
			int j = use_line(now_line.lineCode);
			for (;j != -1 && j < now_line.args.size(); j++){
				IRRegIdentifier regId = now_line.args.get(j);
				if (regId.typ == 1 && regId.id == id){
					now_line.args.set(j, local_temp[pos]);
				}
			}
			if (def_line(now_line.lineCode) && 
				now_line.args.get(0).typ == 1 && now_line.args.get(0).id == id){
				local_prec.get(i).add(pos);
				break;
			}

			if (now_line.lineCode == lineType.JUMP){
				i = jmp_target[i];
			}else{
				if (now_line.lineCode == lineType.BEQ || now_line.lineCode == lineType.BNEQ){
					local_pass(jmp_target[i], id, pos);
				}
				i++;
			}
		}
	}

	public void SSA(){
		//System.out.println("=======" + String.valueOf(lines.size()));

		vis = new int[lines.size()];
		local_vis = new boolean[lines.size()];
		local_prec = new ArrayList<>();
		for (int i = 0; i < lines.size(); i++) local_prec.add(new ArrayList<>());
		local_dad = new int[lines.size()];
		local_temp = new IRRegIdentifier[lines.size()];

		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			if (def_line(now_line.lineCode) && now_line.args.get(0).typ == 1){
				vis_now++;
				local_temp[i] = regIdAllocator.alloc(5);
				local_pass(i+1, now_line.args.get(0).id, i);
			}
		}

		/*for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			System.out.print(local_prec.get(i).size());
			System.out.print(" ");
			System.out.print(jmp_target[i]);
			System.out.println();
			local_prec.get(i).forEach(b -> System.out.println(local_temp[b]));
			now_line.print();
		}*/

		for (int i = 0; i < lines.size(); i++){
			for (int j = 1; j < local_prec.get(i).size(); j++){
				int x = local_prec.get(i).get(0);
				int y = local_prec.get(i).get(j);
				local_temp[x].mult = true;
				local_temp[y].mult = true;
				int u = getdad(local_dad, x);
				int v = getdad(local_dad, y);
				if (u != v){
					local_dad[v] = u;
				}
			}
		}
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			if (def_line(now_line.lineCode) && now_line.args.get(0).typ == 1){
				local_temp[i].assign(local_temp[getdad(local_dad, i)]);
				now_line.args.set(0, local_temp[i]);
			}
		}

	}

	public Graph graph;
	public int[] reach, t_end, t_begin;
	int reach_pass(int i){
		if (vis[i] == vis_now){
			return reach[i];
		}
		vis[i] = vis_now;
		reach[i] = i;
		IRLine now_line = lines.get(i);
		if (now_line.lineCode == lineType.JUMP){
			reach_pass(jmp_target[i]);
			if (reach[i] > reach[jmp_target[i]]) reach[i] = reach[jmp_target[i]];
		}else{
			if (now_line.lineCode == lineType.BEQ || now_line.lineCode == lineType.BNEQ){
				reach_pass(jmp_target[i]);
				if (reach[i] > reach[jmp_target[i]]) reach[i] = reach[jmp_target[i]];
			}
			if (i + 1 < lines.size()){
				reach_pass(i + 1);
				if (reach[i] > reach[i + 1]) reach[i] = reach[i + 1];
			}
		}
		return reach[i];
	}
	public void alloc_pass(int i, int id){
		while (i < lines.size() && vis[i] < vis_now){
			vis[i] = vis_now;
			IRLine now_line = lines.get(i);

			int j = use_line(now_line.lineCode);
			for (; j != -1 && j < now_line.args.size(); j++){
				IRRegIdentifier regId = now_line.args.get(j);
				if (regId.typ == 5)
					graph.add(id, regId.id);
				else if (regId.typ == 0 && regId.id >= 10){
					graph.add(id, regId.id - 10 + regIdAllocator.size(5));
				}
			}
			if (i >= t_end[id] && reach[i] < t_begin[id] || reach[i] >= t_end[id]){
				break;
			}
			if (def_line(now_line.lineCode)){
				IRRegIdentifier regId = now_line.args.get(0);
				if (regId.typ == 5){
					graph.add(id, regId.id);
				}
				else if (regId.typ == 0 && regId.id >= 10){
					graph.add(id, regId.id - 10 + regIdAllocator.size(5));
				}
			}

			if (now_line.lineCode == lineType.CALL){
				graph.saved[id] = true;
			}
			if (now_line.lineCode == lineType.JUMP){
				i = jmp_target[i];
			}else{
				if (now_line.lineCode == lineType.BEQ || now_line.lineCode == lineType.BNEQ){
					alloc_pass(jmp_target[i], id);
				}
				i++;
			}
		}
	}
	public void palloc_pass(int i, int id){
		while (i < lines.size()){
			IRLine now_line = lines.get(i);
			for (int j = 0; j < now_line.args.size(); j++){
				IRRegIdentifier regId = now_line.args.get(j);
				if (regId.typ == 5) graph.add(id, regId.id);
			}
			if (now_line.lineCode == lineType.CALL) break;
			i++;
		}
	}

	public void graphColor(){
		jump_update();

		vis = new int[lines.size()];
		reach = new int[lines.size()];
		t_end = new int[regIdAllocator.size(5)];
		t_begin = new int[regIdAllocator.size(5)];
		boolean[] flag = new boolean[regIdAllocator.size(5)];
		graph = new Graph(regIdAllocator.size(5));
		vis_now = 1;
		reach_pass(0);

		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			for (int j = 0; j < now_line.args.size(); j++){
				if (now_line.args.get(j).typ == 5){
					t_end[now_line.args.get(j).id] = i;
				}
			}
		}
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			if (def_line(now_line.lineCode)){
				IRRegIdentifier regId = now_line.args.get(0);
				if (regId.typ == 5){
					if (flag[regId.id] == false){
						flag[regId.id] = true;
						t_begin[regId.id] = i;
						vis_now++;
						alloc_pass(i + 1, regId.id);
					}
				}else if (regId.typ == 0){
					//System.out.println(regId.id + " " + (regId.id - 10 + regIdAllocator.size(5)));
					palloc_pass(i + 1, regId.id - 10 + regIdAllocator.size(5));
				}
			}
		}
		/*for (int i = 0; i < regIdAllocator.size(5); i++){
			System.out.println(i + " : " + t_begin[i] + " " + t_end[i] + " " + graph.saved[i]);
		}*/
		graph.work();

		IRRegIdentifier[] spill_reg = new IRRegIdentifier[regIdAllocator.size(5)];
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			for (int j = 0; j < now_line.args.size(); j++){
				IRRegIdentifier regId = now_line.args.get(j);
				if (regId.typ == 5){
					int id = regId.id;
					if (graph.getColor(id) == -1){
						if (spill_reg[id] == null) now_line.args.set(j, spill_reg[id] = regIdAllocator.alloc(12));
						else now_line.args.set(j, spill_reg[id]);
					}else{
						now_line.args.set(j, new IRRegIdentifier(graph.getColor(id), 0, false));
					}
				}
			}
		}
	}

	//----------------------------------------------------------------------------------------------------------------------------------------------
	// Optimize End
	//----------------------------------------------------------------------------------------------------------------------------------------------

	public int totalRAM, realRAM, addrStartLocal;
	public void calcRAM(){
		totalRAM = regIdAllocator.size(12) + regIdAllocator.size(7);
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

		//System.out.println(".LAB" + String.valueOf(returnLabel) + ":");
		System.out.println("\tlw\ts0," + String.valueOf(realRAM - 4) + "(sp)");
		if (containsCALL) System.out.println("\tlw\tra," + String.valueOf(realRAM - 8) + "(sp)");
		System.out.println("\taddi\tsp,sp," + String.valueOf(realRAM));
		System.out.println("\tjr\tra");
		
		System.out.println("\t.size\t" + name + ", .-" + name);
	}
}
