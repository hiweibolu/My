package Util;

import java.util.ArrayList;
import java.util.HashSet;

public class Graph {

	public int n;
	public ArrayList<ArrayList<Integer> > to = new ArrayList<>();
	public boolean[] saved;
	public int[] depth, times;

	public boolean cmp(int a, int b){
		return depth[a] < depth[b] || depth[a] == depth[b] && times[a] > times[b];
	}

    public Graph(int n) {
		this.n = n + c.length;
		for (int i = 0; i < this.n; i++) to.add(new ArrayList<>());
		saved = new boolean[this.n];
		depth = new int[this.n];
		times = new int[this.n];
    }

	public void add(int x, int y){
		if (x == y) return;
		to.get(x).add(y);
		to.get(y).add(x);
	}

	public int[] deg, color_arr;
	public int color_arr_num;
	public boolean[] spilled, colored;
	public int max_color = 0, normal_color = 13;
	public int[] color = {10, 11, 12, 13, 14, 15, 16, 17, 5, 6, 7, 28, 29, 8, 9, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27};
	public int[] c = {10, 11, 12, 13, 14, 15, 16, 17, 5, 6};
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
		ArrayList<Integer> saved_arr = new ArrayList<>();
		for (int i = 0; i < n; i++){
			if (saved[i]){
				//spilled[i] = true;
				saved_arr.add(i);
				to.get(i).forEach(x -> {
					deg[x]++;
				});
			}else{
				to.get(i).forEach(x -> {
					if (!saved[x]) deg[x]++;
				});
			}
		}
		for (int i = n - c.length; i < n; i++){
			val[i] = i - (n - c.length);
			colored[i] = true;
		}

		//System.out.println(saved_arr);

		color_arr = new int [n];
		color_arr_num = 0;

		//--------------------------------------------------------------------------
		// Color saved begin
		//--------------------------------------------------------------------------

		for (int j = 0; j < saved_arr.size(); j++){
			int i = saved_arr.get(j);
			if (deg[i] < color.length - normal_color){
				color_arr[color_arr_num++] = i;
				colored[i] = true;
			}else{
				spill_arr.add(i);
			}
		}
 
 		for (int i = 0, head = 0; ; i++){
			while (i == color_arr_num && head < spill_arr.size()){
				int x = spill_arr.get(head++);
				if (!colored[x] && !spilled[x]){
					spilled[x] = true;
					to.get(x).forEach(y -> {
						if (!colored[y] && !spilled[y]){
							if (--deg[y] < color.length - normal_color && saved[y]){
								color_arr[color_arr_num++] = y;
								colored[y] = true;
							}
						}
					});
				}
			}
			if (i >= color_arr_num) break;
			int v = color_arr[i];
			to.get(v).forEach(x -> {
				if (saved[x] && !colored[x] && !spilled[x]){
					if (--deg[x] < color.length - normal_color){
						color_arr[color_arr_num++] = x;
						colored[x] = true;
					}
				}
			});
		}

		for (int i = color_arr_num - 1; i >= 0; i--){
			int v = color_arr[i];
			boolean[] used = new boolean[color.length];
			for (int j = 0; j < to.get(v).size(); j++){
				int x = to.get(v).get(j);
				if (!spilled[x] && val[x] != -1) used[val[x]] = true;
			};
			int now = normal_color;
			while (used[now]) now++;
			val[v] = now;
			if (now > max_color) max_color = now;
		}

		//--------------------------------------------------------------------------
		// Color saved end
		//--------------------------------------------------------------------------
		//for (int i = 0; i < n; i++) System.out.println(i + ":" + deg[i]);

		color_arr_num = 0;
		spill_arr.clear();
		for (int i = 0; i < n; i++){
			if (!colored[i] && !spilled[i] && deg[i] < color.length){
				color_arr[color_arr_num++] = i;
				colored[i] = true;
			}
			spill_arr.add(i);
		}
		spill_arr.sort((Integer a, Integer b) -> {
			if (deg[a] < deg[b]) return 1;
			if (deg[b] < deg[a]) return -1;
			return 0;
		});
		int rotate_num = 20;
		if (spill_arr.size() > rotate_num){
			ArrayList<Integer> new_spill_arr = new ArrayList<>();
			for (int i = rotate_num; i < spill_arr.size(); i++) new_spill_arr.add(spill_arr.get(i));
			for (int i = 0; i < rotate_num; i++) new_spill_arr.add(spill_arr.get(i));
			spill_arr = new_spill_arr;
		}
		
 		for (int i = 0, head = 0; ; i++){
			while (i == color_arr_num && head < spill_arr.size()){
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
				}
			}
			if (i >= color_arr_num) break;
			int v = color_arr[i];
			to.get(v).forEach(x -> {
				if (!colored[x] && !spilled[x]){
					if (--deg[x] < color.length){
						color_arr[color_arr_num++] = x;
						colored[x] = true;
					}
				}
			});
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
			if (now > max_color) max_color = now;
			/*System.out.print(v + "(" + val[v] + ")" + " : ");
			System.out.println(to.get(v));*/
		}
	}

	public int getColor(int x){
		if (val[x] >= 0) return color[val[x]];
		return -1;
	}

	public int useSaved(){
		if (max_color >= normal_color) return max_color - normal_color + 1;
		return 0;
	}
}
