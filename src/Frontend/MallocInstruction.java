package Frontend;

public class MallocInstruction extends IRInstruction {
    VirtualRegister start_addr, malloc_size;
    private int malloc_size_int;
    private boolean is_class_malloc;
    MallocInstruction(op o, VirtualRegister sa, VirtualRegister ms) {
        super(o);
        assert o == op.MALLOC;
        this.start_addr = sa;
        this.malloc_size = ms;
        this.is_class_malloc = false;
    }

    MallocInstruction(op o, VirtualRegister sa, int ms) {
        super(o);
        assert o == op.MALLOC;
        this.start_addr = sa;
        this.malloc_size_int = ms;
        this.is_class_malloc = true;
    }

    @Override
    public void replace_lhs_with(VirtualRegister a, VirtualRegister b) {
        if (start_addr == a)
            start_addr = b;
        else
            assert false;
    }

    @Override
    public void codegen(RegisterAllocator regManager) {
        if (this.is_class_malloc) {
            li("a0", malloc_size_int);
            call("malloc");
            String s = regManager.askForReg(start_addr, getId(), false);
            mv(s, "a0");
            //SW("a0", start_addr.getAddrValue(), "sp");
        } else {
            String ms = regManager.askForReg(malloc_size, getId(), true);
            mv("a0", ms);
            //LW("a0", malloc_size.getAddrValue(), "sp");
            call("malloc");
            String s = regManager.askForReg(start_addr, getId(), false);
            mv(s, "a0");
            //SW("a0", start_addr.getAddrValue(), "sp");
        }
    }

    @Override
    public void optimize() {
        start_addr.write_ex(this);
        if (!this.is_class_malloc)
            malloc_size.read_ex(this);
    }

    @Override
    public String getMessage() {
        if (this.is_class_malloc)
            return (start_addr.getName() + " = malloc(" + malloc_size_int + ")");
        else
            return (start_addr.getName() + " = malloc(" + malloc_size.getName() + ")");
    }
}
