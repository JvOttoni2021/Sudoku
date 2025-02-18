package br.com.dio.model;

public class Space {
    private Integer current = null;
    private final int expected;
    private final boolean fixed;

    public Space(int expected, boolean fixed) {
        this.expected = expected;
        this.fixed = fixed;
        if (fixed)
            this.current = expected;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        if (fixed)
            return;

        this.current = current;
    }

    public void clearSpace() {
        this.setCurrent(null);
    }

    public int getExpected() {
        return expected;
    }

    public boolean isFixed() {
        return fixed;
    }
}
