package Frontend;

import java.util.ArrayList;
import java.util.List;

public class ConstantPoolTable {
    private byte[] pool;
    private int length;

    ConstantPoolTable() {
        pool = new byte[10000000];
        length = 0;
    }

    public int getAddress() {
        return length;
    }

    public void allocate(byte c) {
        pool[length++] = c;
    }

    public void allocate(String s) {
        int n = s.length();
        this.allocate((byte) n);
        this.allocate((byte) (n >> 8));
        this.allocate((byte) (n >> 16));
        this.allocate((byte) (n >> 24));
        byte[] buffer = s.getBytes();
        for (int i = 0; i < n; ++i)
            this.allocate(buffer[i]);
        this.allocate((byte) 0);
    }
}
