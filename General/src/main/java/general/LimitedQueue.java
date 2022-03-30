package general;

import java.util.LinkedList;

/**
 * Вспомогательный класс, реализует коллекцию LinkedList
 * С помощью этого класса реализована команда history
 * Имеет переопределенный метод add, который обеспечивает ограниченность в размере коллекции, при добавлении убирается крайний элемент
 *
 * @param <E>
 */
public class LimitedQueue<E> extends LinkedList<E> {
    private final int limit;

    public LimitedQueue(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(E o) {
        super.add(o);
        while (size() > limit) {
            super.remove();
        }
        return true;
    }
}