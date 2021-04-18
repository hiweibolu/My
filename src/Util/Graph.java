package Util;

import java.util.ArrayList;
import java.util.HashSet;

public class Graph {

	public int n;
	public ArrayList<ArrayList<Integer> > to = new ArrayList<>();
	public boolean[] saved;

    public Graph(int n) {
		this.n = n + 8;
		for (int i = 0; i < this.n; i++) to.add(new ArrayList<>());
		saved = new boolean[this.n];
    }

	public void add(int x, int y){
		if (x == y) return;
		to.get(x).add(y);
		to.get(y).add(x);
	}

	public int[] deg, color_arr;
	public int color_arr_num;
	public boolean[] spilled, colored;
	public int[] color = {10, 11, 12, 13, 14, 15, 16, 17, 5, 6, 7, 28, 29};
	public int[] val;
	public void work(){
		for (int i = 0; i < n; i++){
			HashSet<Integer> hs = new HashSet<Integer>(to.get(i));
			to.set(i, new ArrayList<Integer>(hs));
			/*System.out.print(i + " : ");
			System.out.println(to.get(i));*/
		}
		val = new int [n];
		spilled = new boolean [n];
		colored = new boolean [n];
		for (int i = 0; i < n; i++) val[i] = -1;

		deg = new int[n];
		ArrayList<Integer> spill_arr = new ArrayList<>();
		for (int i = 0; i < n; i++){
			if (saved[i]){
				spilled[i] = true;
			}else{
				to.get(i).forEach(x -> deg[x]++);
				spill_arr.add(i);
			}
		}
		for (int i = n - 8; i < n; i++){
			val[i] = i - (n - 8);
			colored[i] = true;
		}

		color_arr = new int [n];
		color_arr_num = 0;
		for (int i = 0; i < n; i++){
			if (!colored[i] && !spilled[i] && deg[i] < color.length){
				color_arr[color_arr_num++] = i;
				colored[i] = true;
			}
		}
		
		int head = 0;
		for (int i = 0; i < color_arr_num; i++){
			int v = color_arr[i];
			to.get(v).forEach(x -> {
				if (!colored[x] && !spilled[x]){
					if (--deg[x] < color.length){
						color_arr[color_arr_num++] = x;
						colored[x] = true;
					}
				}
			});
			if (i + 1 == color_arr_num){
				while (head < spill_arr.size()){
					int x = spill_arr.get(head++);
					if (!colored[x] && !spilled[x]){
						spilled[x] = true;
						to.get(x).forEach(y -> {
							if (!colored[y] && !spilled[y]){
								if (--deg[y] < color.length){
									color_arr[color_arr_num++] = y;
									colored[y] = true;
								}
							}
						});
						break;
					}
				}
			}
		}

		//System.out.println(color_arr_num);
		for (int i = color_arr_num - 1; i >= 0; i--){
			int v = color_arr[i];
			boolean[] used = new boolean[color.length];
			for (int j = 0; j < to.get(v).size(); j++){
				int x = to.get(v).get(j);
				if (!spilled[x] && val[x] != -1) used[val[x]] = true;
			};
			int now = 0;
			while (used[now]) now++;
			val[v] = now;
			/*System.out.print(v + "(" + val[v] + ")" + " : ");
			System.out.println(to.get(v));*/
		}
	}

	public int getColor(int x){
		if (val[x] >= 0) return color[val[x]];
		return -1;
	}
}
