package linkv.io.api.tuple;

public class Triplet<A, B, C> extends Pair<A, B> {
    public final C three;

    public Triplet(A first, B second, C three) {
        super(first, second);
        this.three = three;
    }
}