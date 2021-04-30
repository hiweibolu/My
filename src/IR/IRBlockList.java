package IR;

import java.util.ArrayList;
import java.util.HashMap;
import IR.IRLine.lineType;
import java.util.HashSet;

public class IRBlockList {

	public ArrayList<IRBlock> blocks = new ArrayList<>();
	public ArrayList<String> strings = new ArrayList<>();
    public ArrayList<Integer> globals = new ArrayList<>();
    public HashMap<String, Integer> class_sizes = new HashMap<>();
	public boolean haveNoReturn = false;

    public int addString(String s){
        int size = strings.size();
        strings.add(s);
        return size;
    }

    public IRBlockList() {
    }

    public void print(){
        for (int i = 0; i < strings.size(); i++){
            System.out.print("STRING(");
            System.out.print(i);
            System.out.print(") ");
            System.out.println(strings.get(i));
        }
        for (int i = 0; i < globals.size(); i++){
            System.out.print("GLOBAL(");
            System.out.print(i);
            System.out.print(") ");
            System.out.println(globals.get(i));
        }
        blocks.forEach(b -> b.print());
    }

    public HashMap<String, Integer> block_map = new HashMap<>();
	public ArrayList<ArrayList<Integer>> calls = new ArrayList<>();
	public ArrayList<ArrayList<Integer>> calleds = new ArrayList<>();
	boolean [] inline_flag;
	int [] topo_arr;
	public void toposort(){
		for (int i = 0; i < blocks.size(); i++){
			IRBlock block = blocks.get(i);
			block_map.put(block.lines.get(0).func, i);
			calls.add(new ArrayList<>());
			calleds.add(new ArrayList<>());
		}
		ArrayList<IRBlock> new_blocks = new ArrayList<>();
		boolean [] flag = new boolean[blocks.size()];
		inline_flag = flag;
		int [] b = new int[blocks.size()];
		int [] deg = new int[blocks.size()];
		topo_arr = b;
		int tail = 0;
		for (int i = 0; i < blocks.size(); i++){
			IRBlock block = blocks.get(i);
			for (int j = 0; j < block.lines.size(); j++){
				IRLine line = block.lines.get(j);
				if (line.lineCode == lineType.CALL){
					block.containsCALL = true;
					if (block_map.containsKey(line.func)){
						int x = block_map.get(line.func);
						line.block = blocks.get(x);
						if (i != x){
							calls.get(i).add(x);
							calleds.get(x).add(i);
						}
					}
				}
			}
			calls.set(i, new ArrayList<Integer>(new HashSet<Integer>(calls.get(i))));
			deg[i] = calls.get(i).size();
			if (deg[i] == 0){
				b[tail++] = i;
				flag[i] = true;
				new_blocks.add(blocks.get(i));
			}
		}
		for (int i = 0; i < tail; i++){
			int v = b[i];
			for (int j = 0; j < calleds.get(i).size(); j++){
				int x = calleds.get(i).get(j);
				if (--deg[x] == 0){
					b[tail++] = x;
					flag[x] = true;
					new_blocks.add(blocks.get(x));
				}
			}
		}
		for (int i = 0; i < blocks.size(); i++){
			if (!flag[i]){
				b[tail++] = i;
				new_blocks.add(blocks.get(i));
			}
		}
		blocks = new_blocks;
	}
	public void optimize(){
		toposort();
		blocks.forEach(b -> {
			b.expand_opt();
			b.unused_jump();
			b.unused_move();
			b.make_addi();
			b.inline_self();
		});
		blocks.forEach(b -> b.jump_update());
		blocks.forEach(b -> b.SSA());
		blocks.forEach(b -> b.expand());
		blocks.forEach(b -> b.DCE());
		blocks.forEach(b -> b.LICM());
		blocks.forEach(b -> b.graphColor());
		blocks.forEach(b -> b.remove());
		blocks.forEach(b -> b.expandLocal());
		blocks.forEach(b -> b.allocLocal());
		blocks.forEach(b -> b.calcRAM());
		blocks.forEach(b -> b.combine());
	}

	public void printASM(){
		if (strings.size() > 0 || globals.size() > 0){
			System.out.println("\t.text");
			for (int i = 0; i < strings.size(); i++){
				if (i == 0) System.out.println("\t.section\t.rodata");
				System.out.println("\t.align\t2");
				System.out.print(".LS");
				System.out.print(i);
				System.out.println(":");
				System.out.print("\t.string\t");
				System.out.println("\"" + strings.get(i) + "\"");
			}
			for (int i = 0; i < globals.size(); i++){
				String s = ".G" + String.valueOf(i);
				System.out.println("\t.globl\t" + s);
				if (i == 0) System.out.println("\t.section\t.sbss,\"aw\",@nobits");
				System.out.println("\t.align\t2");
				System.out.println("\t.type\t" + s + ", @object");
				System.out.println("\t.size\t" + s + ", 4");
				System.out.println(s + ":");
				System.out.println("\t.zero\t4");
			}
		}
		blocks.forEach(b -> b.printASM());
	}
}
