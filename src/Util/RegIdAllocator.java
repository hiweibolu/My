package Util;

import IR.IRRegIdentifier;

public class RegIdAllocator {

    private int[] nowRegId = new int[20];

    public IRRegIdentifier alloc(int typ){
        return new IRRegIdentifier(nowRegId[typ]++, typ, false);
    }

    public int size(int typ){
        return nowRegId[typ];
    }

    public RegIdAllocator() {
    }
}
