package BonzeeGame;// reference: https://stackoverflow.com/questions/6271731/whats-the-best-way-to-return-a-pair-of-values-in-java

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
