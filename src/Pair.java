public class Pair<T, U> {
    public T first;
    public U second;

    public Pair(T t, U u) {
        this.first= t;
        this.second = u;
    }

    public Pair(Pair<T, U> pair) {
        first = pair.first;
        second = pair.second;
    }

    @Override
    public String toString() {
        return "{" +
                 first +
                ", " + second +
                '}';
    }
}
