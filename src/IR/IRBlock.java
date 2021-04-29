package IR;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.HashMap;

import Util.RegIdAllocator;
import Util.Graph;

import IR.IRLine.lineType;

public class IRBlock {

    public ArrayList<IRLine> lines = new ArrayList<>();
    public RegIdAllocator regIdAllocator = null;
	public int maxParamsNumber = 0, args_num = 0;
	public String name;
	public boolean containsCALL = false, call_self = false, all_return = false;
	public int returnLabel, id;
	public int labelNumber = 0;

	public int labelAlloc(){
		return labelNumber++;
	}
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
			/*if (now_line.expanded){
				new_lines.add(now_line);
				continue;
			}*/
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
								//if (now_line.expanded) break;
								temp = regIdAllocator.alloc(5);
								line = new IRLine(lineType.LW);
								line.args.add(temp);
								line.args.add(new IRRegIdentifier(regId.id, 5, false));
								new_lines.add(line);
								now_line.args.set(j, temp);
								break;
							case 1:
							case 4:
								/*temp = regIdAllocator.alloc(5);
								line = new IRLine(lineType.LW);
								line.args.add(temp);
								line.args.add(regId);
								new_lines.add(line);
								now_line.args.set(j, temp);*/
								now_line.lineCode = lineType.LW;
								break;
							case 2:
								//if (now_line.expanded) break;
								temp = regIdAllocator.alloc(5);
								IRRegIdentifier temp1 = regIdAllocator.alloc(5);
								line = new IRLine(lineType.LOAD);
								line.args.add(temp);
								line.args.add(regId);
								new_lines.add(line);
								line = new IRLine(lineType.LW);
								line.args.add(temp1);
								line.args.add(regId);
								line.args.add(temp);
								new_lines.add(line);
								now_line.args.set(j, temp1);
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
								//System.out.println(regId.id);
								temp = regIdAllocator.alloc(5);
								line = new IRLine(lineType.SW);
								line.args.add(temp);
								IRRegIdentifier an_temp;
								while (regIdAllocator.size(7) < regId.id - 7){
									an_temp = regIdAllocator.alloc(7);
								}
								an_temp = new IRRegIdentifier(regId.id - 8, 7, false);
								line.args.add(an_temp);
								new_lines.add(line);
								now_line.args.set(0, temp);
							}
							break;
						case 6:
							//if (now_line.expanded) break;
							if (now_line.lineCode == lineType.MOVE && now_line.args.get(1).typ == 5){
								line = new IRLine(lineType.SW);
								line.args.add(now_line.args.get(1));
								line.args.add(new IRRegIdentifier(regId.id, 5, false));
								new_lines.set(new_lines.size() - 1, line);
							}else{
								temp = regIdAllocator.alloc(5);
								line = new IRLine(lineType.SW);
								line.args.add(temp);
								line.args.add(new IRRegIdentifier(regId.id, 5, false));
								new_lines.add(line);
								now_line.args.set(0, temp);
							}
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
							//if (now_line.expanded) break;
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
				if (now_line.args.get(0).id == now_line.args.get(1).id && now_line.args.get(0).typ == now_line.args.get(1).typ){
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
			case FUNC: case LABEL: case JUMP: case CALL: case BNEQ: case BEQ: case SW:
				return false;
		}
	}
	public int use_line(lineType lineCode){
		switch (lineCode){
			default:
				return 1;
			case BNEQ: case BEQ: case SW:
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
		/*int max_label = 0;
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			if (now_line.lineCode == lineType.LABEL && max_label < now_line.label) max_label = now_line.label;
		}*/
		label_target = new int[labelNumber + 1];
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			if (now_line.lineCode == lineType.LABEL){
				label_target[now_line.label] = i;
			}
		}
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			if (now_line.lineCode == lineType.JUMP || now_line.lineCode == lineType.BEQ || now_line.lineCode == lineType.BNEQ){
				if (now_line.calling) jmp_target[i] = label_target[0];
				else jmp_target[i] = label_target[now_line.label];
			}
		}
	}
	public void expand_opt(){
		ArrayList<IRLine> new_lines = new ArrayList<>();
		ArrayList<IRLine> param_lines = new ArrayList<>();
		containsCALL = false;
		IRRegIdentifier call_result = null;
		for (int i = 0; i < lines.size(); i++){
			IRLine line = lines.get(i);
			if (line.lineCode == lineType.CALL){
				if (line.block != null &&
					line.block.lines.size() <= 200 && line.block.containsCALL == false &&
					line.block != this){
					IRRegIdentifier[] t_id = new IRRegIdentifier [line.block.regIdAllocator.size(5)];
					IRRegIdentifier[] lc_id = new IRRegIdentifier [line.block.regIdAllocator.size(1)];
					Integer[] l_id = new Integer [line.block.labelNumber];
					int j = 1, param_number = 0;
					for (;param_number < param_lines.size(); param_number++){
						//System.out.println(lines.get(0).func + " : " + param_number + " / " + param_lines.size());
						//System.out.println(line.func + " "  + line.block.lines.size());
						IRLine param_line = line.block.lines.get(j);
						/*while (param_line.lineCode != lineType.MOVE){
							{
								IRLine new_line = new IRLine(param_line.lineCode); 
								if (param_line.lineCode == lineType.LABEL){
									if (l_id[param_line.label] == null){
										l_id[param_line.label] = labelAlloc();
									}
									new_line.label = l_id[param_line.label];
								}else{
									for (int k = 0; k < param_line.args.size(); k++){
										IRRegIdentifier regId = param_line.args.get(k);
										if (regId.typ == 5){
											if (t_id[regId.id] == null){
												t_id[regId.id] = regIdAllocator.alloc(5);
											}
											regId = t_id[regId.id];
										}
										new_line.args.add(regId);
									}
								}
								new_lines.add(new_line);
							}
							j++;
							param_line = line.block.lines.get(j);
						}*/
						//System.out.println(param_line.args.get(0).id);
						//param_lines.get(param_lines.size() - param_number - 1).args.get(1).print();
						IRRegIdentifier paramReg = param_lines.get(param_lines.size() - param_number - 1).args.get(1);
						if (/*paramReg.typ == 2 || paramReg.typ == 1 || paramReg.typ == 5*/true){
							IRRegIdentifier temp = regIdAllocator.alloc(1);
							IRLine new_line = new IRLine(lineType.MOVE);
							new_line.args.add(temp);
							new_line.args.add(paramReg);
							new_lines.add(new_line);
							paramReg = temp;
						}
						lc_id[param_line.args.get(0).id] = paramReg;
						j++;
					}
					param_lines.clear();
					call_result = regIdAllocator.alloc(1);
					for (;j < line.block.lines.size(); j++){
						IRLine old_line = line.block.lines.get(j);
						IRLine new_line = new IRLine(old_line.lineCode);
						if (old_line.lineCode == lineType.FUNC);
						else{
							if (old_line.lineCode == lineType.BNEQ || old_line.lineCode == lineType.BEQ ||
								old_line.lineCode == lineType.JUMP || old_line.lineCode == lineType.LABEL){
									if (l_id[old_line.label] == null){
										l_id[old_line.label] = labelAlloc();
									}
									new_line.label = l_id[old_line.label];
								}
							for (int k = 0; k < old_line.args.size(); k++){
								IRRegIdentifier regId = old_line.args.get(k);
								if (regId.typ == 5){
									if (t_id[regId.id] == null){
										t_id[regId.id] = regIdAllocator.alloc(5);
									}
									regId = t_id[regId.id];
								}else if (regId.typ == 1){
									if (lc_id[regId.id] == null){
										lc_id[regId.id] = regIdAllocator.alloc(1);
									}
									regId = lc_id[regId.id];
								}
								new_line.args.add(regId);
							}
							if (old_line.lineCode == lineType.MOVE){
								if (old_line.args.get(0).typ == 0 && old_line.args.get(0).id == 10){
									new_line.args.set(0, call_result);
								}
							}
							new_line.func = old_line.func;
							new_line.expanded = true;
							new_lines.add(new_line);
						}
					}
				}else{
					containsCALL = true;
					if (param_lines.size() > 0){
						new_lines.addAll(param_lines);
						param_lines.clear();
					}
					new_lines.add(line);
				}
			}else{
				if (line.lineCode == lineType.MOVE){
					if (line.args.get(0).typ == 3){
						param_lines.add(line);
					}
					else
					if (call_result != null && line.args.get(1).typ == 0 && line.args.get(1).id == 10){
						line.args.set(1, call_result);
						new_lines.add(line);
						call_result = null;
					}
					else{
						new_lines.add(line);
					}
				}else{
					new_lines.add(line);
				}
			}
		}
		lines = new_lines;
		jump_update();
	}

	public ArrayList<Integer> local_list;
	public ArrayList<ArrayList<ArrayList<Integer>>>  local_prec;
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
					local_prec.get(i).get(j).add(pos);
					//now_line.args.set(j, local_temp[pos]);
				}
			}
			if (def_line(now_line.lineCode) && 
				now_line.args.get(0).typ == 1 && now_line.args.get(0).id == id){
				local_prec.get(i).get(0).add(pos);
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
	public void assign_pass(int i, int id, int pos){
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
				break;
			}

			if (now_line.lineCode == lineType.JUMP){
				i = jmp_target[i];
			}else{
				if (now_line.lineCode == lineType.BEQ || now_line.lineCode == lineType.BNEQ){
					assign_pass(jmp_target[i], id, pos);
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
		for (int i = 0; i < lines.size(); i++){
			local_prec.add(new ArrayList<>());
			for (int j = 0; j < lines.get(i).args.size(); j++) local_prec.get(i).add(new ArrayList<>());
		}
		local_dad = new int[lines.size()];
		local_temp = new IRRegIdentifier[lines.size()];

		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			if (def_line(now_line.lineCode) && now_line.args.get(0).typ == 1){
				vis_now++;
				local_temp[i] = regIdAllocator.alloc(5);
				local_pass(i + 1, now_line.args.get(0).id, i);
			}
		}

		/*for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			System.out.print(local_prec.get(i) + " ");
			now_line.print();
		}*/

		for (int i = 0; i < lines.size(); i++){
			for (int k = 0; k < lines.get(i).args.size(); k++) 
				for (int j = 1; j < local_prec.get(i).get(k).size(); j++){
					int x = local_prec.get(i).get(k).get(0);
					int y = local_prec.get(i).get(k).get(j);
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
				vis_now++;
				assign_pass(i + 1, now_line.args.get(0).id, i);
				now_line.args.set(0, local_temp[i]);
			}
		}

	}

	public Graph graph;
	public int[] reach, t_end, t_begin;
	int reach_num;
	public int[] reach_dad, reach_state;
	public ArrayList<ArrayList<Integer>> reach_to;

	public void reach_pass(int id){
		int[] b = new int [lines.size()];
		int tail = 0;

		for (int i = t_begin[id] + 1; i <= t_end[id]; i++){
			IRLine now_line = lines.get(i);
			int j = use_line(now_line.lineCode);
			for (; j != -1 && j < now_line.args.size(); j++){
				IRRegIdentifier regId = now_line.args.get(j);
				if (regId.typ == 5 && id == regId.id){
					reach[i] = vis_now;
					b[tail++] = i;
				}
			}
		}

		for (int i = 0; i < tail; i++){
			int v = b[i];
			for (int j = 0; j < reach_to.get(v).size(); j++){
				int x = reach_to.get(v).get(j);
				IRLine line = lines.get(x);
				if (reach[x] < vis_now && !(def_line(line.lineCode) && line.args.get(0).typ == 5 && line.args.get(0).id == id)){
					reach[x] = vis_now;
					b[tail++] = x;
				}
			}
		}

		for (int i = 0; i < tail; i++){
			IRLine now_line = lines.get(b[i]);
			if (now_line.lineCode == lineType.CALL){
				graph.saved[id] = true;
			}
			int j = use_line(now_line.lineCode);
			for (; j != -1 && j < now_line.args.size(); j++){
				IRRegIdentifier regId = now_line.args.get(j);
				if (regId.typ == 5)
					graph.add(id, regId.id);
				else if (regId.typ == 0 && regId.id >= 10){
					graph.add(id, regId.id - 10 + regIdAllocator.size(5));
				}
			}
			if (def_line(now_line.lineCode) && b[i] + 1 < lines.size() && reach[b[i] + 1] == vis_now){
				IRRegIdentifier regId = now_line.args.get(0);
				if (regId.typ == 5){
					graph.add(id, regId.id);
				}
				else if (regId.typ == 0 && regId.id >= 10){
					graph.add(id, regId.id - 10 + regIdAllocator.size(5));
				}
			}

		}
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
			if (reach[i] < vis_now){
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
			if (now_line.lineCode == lineType.CALL || now_line.calling) break;
			i++;
		}
	}

	public void graphColor(){
		jump_update();

		vis = new int[lines.size()];
		reach = new int[lines.size()];
		reach_state = new int[lines.size()];
		reach_dad = new int[lines.size()];
		t_end = new int[regIdAllocator.size(5)];
		t_begin = new int[regIdAllocator.size(5)];
		boolean[] flag = new boolean[regIdAllocator.size(5)];
		graph = new Graph(regIdAllocator.size(5));
		vis_now = 1;
		//reach_pass(0);
		/*PriorityQueue<Integer> queue = new PriorityQueue<Integer>();
		for (int i = lines.size() - 1; i >= 0; i--){
			while (queue.size() > 0 && -queue.peek() >= i) queue.poll();
			if (queue.size() > 0) reach[i] = -queue.peek();
			IRLine line = lines.get(i);
			if (line.lineCode == lineType.JUMP || line.lineCode == lineType.BEQ ||
				line.lineCode == lineType.BNEQ) 
					if (jmp_target[i] < i)
						queue.add(-jmp_target[i]);
		}*/

		reach_to = new ArrayList<>();
		for (int i = 0; i < lines.size(); i++) reach_to.add(new ArrayList<>());
		for (int i = 0; i < lines.size(); i++){
			IRLine line = lines.get(i);
			if (line.lineCode == lineType.JUMP || line.lineCode == lineType.BEQ ||
				line.lineCode == lineType.BNEQ){
					reach_to.get(jmp_target[i]).add(i);
				}
			if (line.lineCode != lineType.JUMP && i + 1 < lines.size()) reach_to.get(i + 1).add(i);
		}


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
			if (now_line.ignored) continue;
			if (def_line(now_line.lineCode)){
				IRRegIdentifier regId = now_line.args.get(0);
				if (regId.typ == 5){
					if (flag[regId.id] == false){
						flag[regId.id] = true;
						t_begin[regId.id] = i;
						vis_now++;
						reach_pass(regId.id);
						//alloc_pass(i + 1, regId.id);
					}
				}else if (regId.typ == 0){
					//System.out.println(regId.id + " " + (regId.id - 10 + regIdAllocator.size(5)));
					palloc_pass(i + 1, regId.id - 10 + regIdAllocator.size(5));
				}
			}
		}
		/*for (int i = 0; i < regIdAllocator.size(5); i++){
			System.out.println(i + " : " + t_begin[i] + " " + t_end[i] + " " + graph.saved[i]);
		}
		for (int i = 0; i < lines.size(); i++){
			System.out.println(i + " : " + reach[i]);
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
				}else if (regId.typ == 0 && regId.id >= 10){
					regId.id = graph.c[regId.id - 10];
				}
			}
		}
	}

	public ArrayList<ArrayList<Integer>> dce_to;
	public boolean []active;
	public boolean []erased;
	public void DCE(){
		int reg_num = regIdAllocator.size(5);
		dce_to = new ArrayList<>();
		for (int i = 0; i < reg_num; i++) dce_to.add(new ArrayList<>());
		active = new boolean[reg_num];
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			if (def_line(now_line.lineCode)){
				IRRegIdentifier regId1 = now_line.args.get(0);
				int x = 0;
				if (regId1.typ == 2 || regId1.typ == 7 || regId1.typ == 0){
					x = reg_num;
				}else if (regId1.typ == 5){
					x = regId1.id;
				}else{
					continue;
				}
				for (int j = 1; j < now_line.args.size(); j++){
					IRRegIdentifier regId2 = now_line.args.get(j);
					if (regId2.typ == 5){
						/*int u = getdad(dce_dad, x);
						int v = getdad(dce_dad, regId2.id);
						if (u != v){
							dce_dad[u] = v;
						}*/
						if (reg_num == x){
							active[regId2.id] = true;
						}else{
							dce_to.get(x).add(regId2.id);
						}
					}
				}
			}
			else if (now_line.lineCode == lineType.SW){ //SW Global
				IRRegIdentifier regId1 = now_line.args.get(1);
				int x = 0;
				//if (regId1.typ == 2){
				x = reg_num;
				for (int j = 0; j < now_line.args.size(); j++){
					IRRegIdentifier regId2 = now_line.args.get(j);
					if (regId2.typ == 5){
						active[regId2.id] = true;
					}
				}			
			}
			else if (now_line.lineCode == lineType.BEQ || now_line.lineCode == lineType.BNEQ){ //Branch
				int x = reg_num;
				for (int j = 0; j < now_line.args.size(); j++){
					IRRegIdentifier regId2 = now_line.args.get(j);
					if (regId2.typ == 5){
						active[regId2.id] = true;
					}
				}
			}
		}

		int[] b = new int [reg_num];
		int tail = 0;
		for (int i = 0; i < reg_num; i++)
			if (active[i])
				b[tail++] = i;
		for (int i = 0; i < tail; i++){
			int v = b[i];
			for (int j = 0; j < dce_to.get(v).size(); j++){
				int x = dce_to.get(v).get(j);
				if (!active[x]){
					b[tail++] = x;
					active[x] = true;
				}
			};
		}

		/*jump_update();
		erased = new boolean [lines.size()];
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			if (now_line.lineCode == lineType.JUMP && jmp_target[i] < i){
				boolean flag = false;
				for (int j = jmp_target[i]; j < i; j++){
					IRLine line = lines.get(j);
					if (line.lineCode == lineType.CALL || 
						line.lineCode == lineType.SW || 
						def_line(line.lineCode) && active[line.args.get(0).id]){
							flag = true;
						}
				}
				if (!flag){
					for (int j = jmp_target[i]; j <= i; j++){
						erased[j] = true;
					}
				}
			}
		}
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			if (now_line.lineCode == lineType.BEQ || now_line.lineCode == lineType.BNEQ){ //Branch
				if (erased[i]) continue;
				int x = reg_num;
				for (int j = 0; j < now_line.args.size(); j++){
					IRRegIdentifier regId2 = now_line.args.get(j);
					if (regId2.typ == 5){
						active[regId2.id] = true;
					}
				}
			}
		}

		tail = 0;
		for (int i = 0; i < reg_num; i++)
			if (active[i])
				b[tail++] = i;
		for (int i = 0; i < tail; i++){
			int v = b[i];
			for (int j = 0; j < dce_to.get(v).size(); j++){
				int x = dce_to.get(v).get(j);
				if (!active[x]){
					b[tail++] = x;
					active[x] = true;
				}
			};
		}*/

		ArrayList<IRLine> new_lines = new ArrayList<>();

		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			/*if (erased[i]){
				System.out.println("[DEAD CODE]");
				now_line.print();
				continue;
			}*/
			if (def_line(now_line.lineCode)){
				IRRegIdentifier regId = now_line.args.get(0);
				if (regId.typ == 5 && !active[regId.id]){
					/*System.out.println("[DEAD CODE]");
					now_line.print();*/
					continue;
				}
			}
			new_lines.add(now_line);
			//now_line.print();
		}
		lines = new_lines;

		/*for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			now_line.print();
			for (int j = 0; j < now_line.args.size(); j++){
				IRRegIdentifier regId2 = now_line.args.get(j);
				if (regId2.typ == 5){
					System.out.print(active[regId2.id] + " ");
				}
			}			
			System.out.println();
		}*/
	}

	public void make_addi(){
		//print();
		ArrayList<IRLine> new_lines = new ArrayList<>();
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			if (def_line(now_line.lineCode) && now_line.args.get(0).typ != 5){
				new_lines.add(now_line);
				continue;
			}
			//now_line.print();
			if (now_line.lineCode == lineType.ADD/* && 
				now_line.args.get(0).typ == 5 && now_line.args.get(1).typ == 5 && now_line.args.get(2).typ == 5*/){
				IRLine last_line = new_lines.get(new_lines.size() - 1);
				if (last_line.lineCode == lineType.LOAD &&
					last_line.args.get(0).typ == 5 && last_line.args.get(1).typ == 8){
					IRRegIdentifier regId = last_line.args.get(0);
					IRRegIdentifier regId1 = now_line.args.get(1);
					IRRegIdentifier regId2 = now_line.args.get(2);
					if (regId.equals(regId1)){
						IRLine line = new IRLine(lineType.ADDI);
						line.args.add(now_line.args.get(0));
						line.args.add(regId2);
						line.args.add(last_line.args.get(1));
						new_lines.set(new_lines.size() - 1, line);
					}else if (regId.equals(regId2)){
						IRLine line = new IRLine(lineType.ADDI);
						line.args.add(now_line.args.get(0));
						line.args.add(regId1);
						line.args.add(last_line.args.get(1));
						new_lines.set(new_lines.size() - 1, line);
					}else{
						new_lines.add(now_line);
					}
				}else{
					new_lines.add(now_line);
				}
			}else if (now_line.lineCode == lineType.MUL){
				IRLine last_line = new_lines.get(new_lines.size() - 1);
				if (last_line.lineCode == lineType.LOAD &&
					last_line.args.get(0).typ == 5 && last_line.args.get(1).typ == 8){
					IRRegIdentifier regId = last_line.args.get(0);
					IRRegIdentifier regId1 = now_line.args.get(1);
					IRRegIdentifier regId2 = now_line.args.get(2);
					if (regId.equals(regId1)){
						IRLine line = new IRLine(lineType.MULI);
						line.args.add(now_line.args.get(0));
						line.args.add(regId2);
						line.args.add(last_line.args.get(1));
						new_lines.set(new_lines.size() - 1, line);
					}else if (regId.equals(regId2)){
						IRLine line = new IRLine(lineType.MULI);
						line.args.add(now_line.args.get(0));
						line.args.add(regId1);
						line.args.add(last_line.args.get(1));
						new_lines.set(new_lines.size() - 1, line);
					}else{
						new_lines.add(now_line);
					}
				}else{
					new_lines.add(now_line);
				}
			}else if (now_line.lineCode == lineType.LE){
				IRLine last_line = new_lines.get(new_lines.size() - 1);
				if (last_line.lineCode == lineType.LOAD &&
					last_line.args.get(0).typ == 5 && last_line.args.get(1).typ == 8){
					IRRegIdentifier regId = last_line.args.get(0);
					IRRegIdentifier regId1 = now_line.args.get(1);
					IRRegIdentifier regId2 = now_line.args.get(2);
					if (regId.equals(regId2)){
						IRLine line = new IRLine(lineType.SLTI);
						line.args.add(now_line.args.get(0));
						line.args.add(regId1);
						line.args.add(last_line.args.get(1));
						new_lines.set(new_lines.size() - 1, line);
					}else{
						new_lines.add(now_line);
					}
				}else{
					new_lines.add(now_line);
				}
			}else if (now_line.lineCode == lineType.MOVE){
				IRLine last_line = new_lines.get(new_lines.size() - 1);
				if (last_line.lineCode == lineType.LOAD &&
					last_line.args.get(0).typ == 5 && last_line.args.get(1).typ == 8){
					IRRegIdentifier regId = last_line.args.get(0);
					IRRegIdentifier regId1 = now_line.args.get(1);
					if (regId.equals(regId1)){
						IRLine line = new IRLine(lineType.LOAD);
						line.args.add(now_line.args.get(0));
						line.args.add(last_line.args.get(1));
						new_lines.set(new_lines.size() - 1, line);
					}else{
						new_lines.add(now_line);
					}
				}else{
					new_lines.add(now_line);
				}
			}else if (now_line.lineCode == lineType.AND){
				IRLine last_line = new_lines.get(new_lines.size() - 1);
				if (last_line.lineCode == lineType.LOAD &&
					last_line.args.get(0).typ == 5 && last_line.args.get(1).typ == 8){
					IRRegIdentifier regId = last_line.args.get(0);
					IRRegIdentifier regId1 = now_line.args.get(1);
					IRRegIdentifier regId2 = now_line.args.get(2);
					if (regId.equals(regId1)){
						IRLine line = new IRLine(lineType.ANDI);
						line.args.add(now_line.args.get(0));
						line.args.add(regId2);
						line.args.add(last_line.args.get(1));
						new_lines.set(new_lines.size() - 1, line);
					}else if (regId.equals(regId2)){
						IRLine line = new IRLine(lineType.ANDI);
						line.args.add(now_line.args.get(0));
						line.args.add(regId1);
						line.args.add(last_line.args.get(1));
						new_lines.set(new_lines.size() - 1, line);
					}else{
						new_lines.add(now_line);
					}
				}else{
					new_lines.add(now_line);
				}
			}else if (now_line.lineCode == lineType.EQ){
				IRLine last_line = new_lines.get(new_lines.size() - 1);
				if (last_line.lineCode == lineType.LOAD &&
					last_line.args.get(0).typ == 5 && last_line.args.get(1).typ == 8){
					IRRegIdentifier regId = last_line.args.get(0);
					IRRegIdentifier regId1 = now_line.args.get(2);
					if (regId.equals(regId1)){
						IRLine line = new IRLine(lineType.EQI);
						line.args.add(now_line.args.get(0));
						line.args.add(now_line.args.get(1));
						line.args.add(new IRRegIdentifier(-last_line.args.get(1).id, 8, false));
						new_lines.set(new_lines.size() - 1, line);
					}else{
						new_lines.add(now_line);
					}
				}else{
					new_lines.add(now_line);
				}
			}else{
				new_lines.add(now_line);
			}
		}
		lines = new_lines;
		new_lines = new ArrayList<>();
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			if (now_line.lineCode == lineType.ADDI && now_line.args.get(1).typ == 5 &&
				!now_line.args.get(1).equals(now_line.args.get(0))){
				IRLine last_line = new_lines.get(new_lines.size() - 1);
				if (last_line.lineCode == lineType.ADDI){
					IRRegIdentifier regId = last_line.args.get(0);
					IRRegIdentifier regId1 = now_line.args.get(1);
					if (regId.equals(regId1) && !regId.equals(last_line.args.get(1))){
						IRLine line = new IRLine(lineType.ADDI);
						line.args.add(now_line.args.get(0));
						line.args.add(last_line.args.get(1));
						line.args.add(new IRRegIdentifier(last_line.args.get(2).id + now_line.args.get(2).id, 8, false));
						new_lines.set(new_lines.size() - 1, line);
					}else{
						new_lines.add(now_line);
					}
				}else{
					new_lines.add(now_line);
				}
			}else{
				new_lines.add(now_line);
			}
		}
		lines = new_lines;
	}

	public void unused_jump(){
		int[] used_jmp = new int[labelNumber];
		ArrayList<IRLine> new_lines = new ArrayList<>();
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			if (now_line.lineCode == lineType.JUMP || now_line.lineCode == lineType.BNEQ ||
				now_line.lineCode == lineType.BEQ){
					used_jmp[now_line.label]++;
				}
			if (now_line.lineCode == lineType.LABEL){
				IRLine last_line = new_lines.get(new_lines.size() - 1);
				if (last_line.lineCode == lineType.JUMP){
					if (last_line.label == now_line.label && now_line.label != 0){
						used_jmp[now_line.label]--;
						new_lines.set(new_lines.size() - 1, now_line);
					}else{
						new_lines.add(now_line);
					}
				}else{
					new_lines.add(now_line);
				}
			}else{
				new_lines.add(now_line);
			}
		}
		lines = new_lines;
		//print();
		new_lines = new ArrayList<>();
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			if (now_line.lineCode == lineType.LABEL && used_jmp[now_line.label] == 0){
			}else{
				new_lines.add(now_line);
			}
		}
		lines = new_lines;
	}

	public void unused_move(){
		ArrayList<IRLine> new_lines = new ArrayList<>();
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			if (now_line.lineCode == lineType.MOVE && 
				now_line.args.get(0).typ == 5 && now_line.args.get(1).typ == 5){
				IRLine last_line = new_lines.get(new_lines.size() - 1);
				if (def_line(last_line.lineCode) && last_line.args.get(0).equals(now_line.args.get(1))){
					last_line.args.set(0, now_line.args.get(0));
				}else{
					new_lines.add(now_line);
				}
			}else{
				new_lines.add(now_line);
			}
		}
		lines = new_lines;
	}

	public void inline_self(){
		//print();
		//System.out.println("=========== " + lines.get(0).func + " ===========");
		boolean flag = false, add_flag = false;
		for (int i = 0; i < lines.size(); i++){
			IRLine now_line = lines.get(i);
			if (now_line.lineCode == lineType.CALL){
				//System.out.println(i);
				if (now_line.block != this) return;
				//System.out.println(i);
				flag = true;
				if (i + 3 < lines.size() && lines.get(i + 3).lineCode == lineType.JUMP &&
					lines.get(i + 3).label == 0){
						//System.out.println("no addup");
						continue;
					}
				if (i + 4 < lines.size() && lines.get(i + 4).lineCode == lineType.JUMP &&
					lines.get(i + 4).label == 0){
						if (lines.get(i + 2).lineCode == lineType.ADDI &&
							lines.get(i + 2).args.get(0).equals(lines.get(i + 3).args.get(1)) &&
							lines.get(i + 2).args.get(1).equals(lines.get(i + 1).args.get(0))){
								//System.out.println("addup");
								add_flag = true;
								now_line.add_up = lines.get(i + 2).args.get(2).id;
								continue;
							}
						else return;
					}
				return;
			}
		}
		if (flag && args_num <= 10){
			//print();
			int back_lab = labelAlloc();
			IRRegIdentifier[] param_reg = new IRRegIdentifier[args_num];
			IRRegIdentifier[] temp_reg = new IRRegIdentifier[args_num];
			ArrayList<IRLine> new_lines = new ArrayList<>();

			for (int i = 0; i < args_num; i++) temp_reg[i] = regIdAllocator.alloc(1);

			IRLine line = null;
			IRRegIdentifier counter = null;
			if (add_flag){
				counter = regIdAllocator.alloc(1);
			}
			for (int i = 0; i < lines.size(); i++){
				IRLine now_line = lines.get(i);
				if (now_line.lineCode == lineType.CALL){
					for (int j = args_num - 1; j >= 0; j--){
						line = new IRLine(lineType.MOVE);
						line.args.add(param_reg[j]);
						line.args.add(temp_reg[j]);
						new_lines.add(line);
					}
					if (now_line.add_up != 0){
						line = new IRLine(lineType.ADDI);
						line.args.add(counter);
						line.args.add(counter);
						line.args.add(new IRRegIdentifier(now_line.add_up, 8, false));
						new_lines.add(line);
					}
					line = new IRLine(lineType.JUMP);
					line.label = back_lab;
					//line.calling = true;
					new_lines.add(line);
					//new_lines.add(now_line);
				}else{
					if (i <= args_num){
						if (i > 0){
							param_reg[i - 1] = now_line.args.get(0);
						}
					}else{
						if (now_line.lineCode == lineType.MOVE && now_line.args.get(0).typ == 3){
							/*IRRegIdentifier temp = regIdAllocator.alloc(5);
							line = new IRLine(lineType.MOVE);
							line.args.add(temp);
							line.args.add(now_line.args.get(1));
							new_lines.add(line);*/
							now_line.args.set(0, temp_reg[now_line.args.get(0).id]);
							//now_line.args.set(0, new IRRegIdentifier(now_line.args.get(0).id + 10, 0, false));
							//now_line.args.set(1, temp);
						}
						/*if (now_line.lineCode == lineType.MOVE && now_line.args.get(1).typ == 4){
							now_line.args.set(1, new IRRegIdentifier(now_line.args.get(1).id + 18, 0, false));
						}*/
						if (add_flag && now_line.lineCode == lineType.MOVE && now_line.args.get(0).typ == 0 && now_line.args.get(0).id == 10){
							IRRegIdentifier temp = regIdAllocator.alloc(5);
							line = new IRLine(lineType.ADD);
							line.args.add(temp);
							line.args.add(now_line.args.get(1));
							line.args.add(counter);
							new_lines.add(line);
							now_line.args.set(1, temp);
						}

					}
					new_lines.add(now_line);
					if (i == args_num){
						if (add_flag){
							line = new IRLine(lineType.MOVE);
							line.args.add(counter);
							line.args.add(new IRRegIdentifier(0, 0, false));
							new_lines.add(line);
						}
						line = new IRLine(lineType.LABEL);
						line.label = back_lab;
						new_lines.add(line);
					}
					/*if (i == 0){
						for (int j = 8; j < args_num; j++){
							line = new IRLine(lineType.MOVE);
							line.args.add(new IRRegIdentifier(10 + j, 0, false));
							line.args.add(new IRRegIdentifier(j - 8, 4, false));
							new_lines.add(line);
						}
					}*/
				}
			}
			/*if (add_flag){
				line = new IRLine(lineType.ADD);
				line.args.add(new IRRegIdentifier(10, 0, false));
				line.args.add(new IRRegIdentifier(10, 0, false));
				line.args.add(counter);
				new_lines.add(line);
			}*/
			lines = new_lines;
			all_return = true;
			//print();
			containsCALL = false;
		}
	}
	public int[] c = {10, 11, 12, 13, 14, 15, 16, 17, 5, 6};

	public void LICM(){
		//print();
		jump_update();
		int[] def_times = new int[regIdAllocator.size(5)];
		//int[] global_times = new int[regIdAllocator.size(2)];
		HashMap<Integer, Integer> global_times = new HashMap<>();

		ArrayList<ArrayList<IRLine>> licm_lines = new ArrayList<>();
		boolean[] removed = new boolean[lines.size()];
		for (int i = 0; i < lines.size(); i++) licm_lines.add(new ArrayList<>());
		for (int i = lines.size() - 1; i >= 0; i--){
			IRLine line = lines.get(i);
			if (line.lineCode == lineType.JUMP && jmp_target[i] < i){
				boolean have_call = false;
				for (int j = jmp_target[i]; j < i; j++){
					IRLine now_line = lines.get(j);
					if (def_line(now_line.lineCode) && !removed[j]){
						IRRegIdentifier regId = now_line.args.get(0);
						if (regId.typ == 5){
							def_times[regId.id]++;
						}
					}else if (now_line.lineCode == lineType.SW){
						IRRegIdentifier regId = now_line.args.get(1);
						if (regId.typ == 2){
							if (!global_times.containsKey(regId.id)) global_times.put(regId.id, 1);
						}
					}else if (now_line.lineCode == lineType.CALL){
						have_call = true;
						break;
					}
				}
				if (!have_call) for (int j = jmp_target[i]; j < i; j++){
					IRLine now_line = lines.get(j);
					if (def_line(now_line.lineCode) && (now_line.lineCode != lineType.LOAD || now_line.args.get(1).typ == 2)){
						IRRegIdentifier regId = now_line.args.get(0);
						if (regId.typ == 5 && def_times[regId.id] == 1 && !removed[j]){
							boolean flag = true;
							for (int k = 1; k < now_line.args.size(); k++){
								IRRegIdentifier temp = now_line.args.get(k);
								if (temp.typ == 5 && def_times[temp.id] == 0 || temp.typ == 8 || 
									temp.typ == 2 && !global_times.containsKey(temp.id));
								else flag = false;
							}
							if (flag && licm_lines.get(jmp_target[i]).size() < 4){
								licm_lines.get(jmp_target[i]).add(now_line);
								removed[j] = true;
								def_times[regId.id]--;
								/*System.out.println("=====remove=====");
								now_line.print();
								System.out.println("=====removed=====");*/
							}
						}
					}
				}

				for (int j = jmp_target[i]; j < i; j++){
					IRLine now_line = lines.get(j);
					if (def_line(now_line.lineCode) && now_line.args.get(0).typ == 5){
						def_times[now_line.args.get(0).id] = 0;
					}
				}
				global_times.clear();
			}
		}
		ArrayList<IRLine> new_lines = new ArrayList<>();
		for (int i = 0; i < lines.size(); i++){
			new_lines.addAll(licm_lines.get(i));
			if (!removed[i]) new_lines.add(lines.get(i));
		}
		lines = new_lines;
		//print();
	}

	//----------------------------------------------------------------------------------------------------------------------------------------------
	// Optimize End
	//----------------------------------------------------------------------------------------------------------------------------------------------

	public int totalRAM, realRAM, addrStartLocal;
	public void calcRAM(){
		totalRAM = regIdAllocator.size(12) + regIdAllocator.size(7) + graph.useSaved();
		addrStartLocal = 0;
		//totalRAM++;
		addrStartLocal -= 4 * graph.useSaved(); // store s*
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
		for (int i = 0; i < graph.useSaved(); i++)
			System.out.println("\tsw\ts" + i + "," + (realRAM - 4 * (i + 1)) + "(sp)");
		//System.out.println("\tsw\ts0," + String.valueOf(realRAM - 4) + "(sp)");
		if (containsCALL) System.out.println("\tsw\tra," + String.valueOf(realRAM - 4 - 4 * graph.useSaved()) + "(sp)");
		//System.out.println("\taddi\ts0,sp," + String.valueOf(realRAM));
		
        lines.forEach(l -> l.printASM(this));

		//System.out.println(".LAB" + String.valueOf(returnLabel) + ":");
		for (int i = 0; i < graph.useSaved(); i++)
			System.out.println("\tlw\ts" + i + "," + (realRAM - 4 * (i + 1)) + "(sp)");
		//System.out.println("\tlw\ts0," + String.valueOf(realRAM - 4) + "(sp)");
		if (containsCALL) System.out.println("\tlw\tra," + String.valueOf(realRAM - 4 - 4 * graph.useSaved()) + "(sp)");
		System.out.println("\taddi\tsp,sp," + String.valueOf(realRAM));
		System.out.println("\tjr\tra");
		
		System.out.println("\t.size\t" + name + ", .-" + name);
	}
}
