package linkv.io.api.tuple;

public class Pair<A, B> extends Unit<A> {
    public final B second;

    public Pair(A first, B second) {
        super(first);
        this.second = second;
    }
}
