package io.github.cassiboy1203.staffManagerLib;

public class Tuple <T, V>{
    private final T t;
    private final V v;

    public Tuple(T t, V v) {
        this.t = t;
        this.v = v;
    }

    public T getT() {
        return t;
    }

    public V getV() {
        return v;
    }
}
