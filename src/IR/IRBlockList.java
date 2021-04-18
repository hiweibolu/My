package IR;

import java.util.ArrayList;
import java.util.HashMap;

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

	public void optimize(){
		blocks.forEach(b -> b.expand_opt());
		blocks.forEach(b -> b.SSA());
		blocks.forEach(b -> b.expand());
		//print();
		blocks.forEach(b -> b.graphColor());
	}

	public void initASM(){
		blocks.forEach(b -> b.expand());
		blocks.forEach(b -> b.alloc());
		blocks.forEach(b -> b.expandLocal());
		blocks.forEach(b -> b.allocLocal());
		blocks.forEach(b -> b.remove());
		blocks.forEach(b -> b.calcRAM());
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
