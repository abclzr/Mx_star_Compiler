package Frontend;

import java.util.ArrayList;
import java.util.List;

public class ConstantPoolTable {
    private List<Byte> pool;
    private int length;

    ConstantPoolTable() {
        pool = new ArrayList<>();
        length = 0;
    }

    public int getAddress() {
        return length;
    }

    public void allocate(byte c) {
        pool.add(c);
        ++length;
    }

    public void allocate(String s) {
        int n = s.length();
        byte[] buffer = s.getBytes();
        for (int i = 0; i < n; ++i)
            this.allocate(buffer[i]);
        this.allocate((byte) 0);
    }
}
