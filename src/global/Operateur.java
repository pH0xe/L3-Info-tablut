package global;

public enum Operateur {
    ADD('+') {
        @Override
        public int faire(int x, int y) {
            return x + y;
        }
    },
    SUB('-') {
        @Override
        public int faire(int x, int y) {
            return x - y;
        }
    },
    NOTHING(' ') {
        @Override
        public int faire(int x, int y) {
            return x;
        }
    };


    private final char op;

    Operateur(char op) {
        this.op = op;
    }
    public abstract int faire(int x, int y);
}
