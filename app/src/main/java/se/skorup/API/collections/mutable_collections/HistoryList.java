package se.skorup.API.collections.mutable_collections;

import java.util.Optional;

/**
 * A history list used to walk through history.
 *
 * @param <E> the type of the elements.
 * */
public class HistoryList<E> implements HistoryStructure<E>
{
    private int size;

    private ListNode<E> current;
    private ListNode<E> first;
    private ListNode<E> last;

    /**
     * Creates a new history list.
     * */
    public HistoryList()
    {
        this.size = 0;
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public boolean empty()
    {
        return first == null;
    }

    @Override
    public void add(E e)
    {
        size++;

        if (last == null)
        {
            last = new ListNode<>(e, null);
            first = last;
            reset();
            return;
        }

        var old = last;
        last = new ListNode<>(e, old);
        old.next = last;
        reset();
    }

    @Override
    public void reset()
    {
        current = last;
    }

    @Override
    public Optional<E> forward()
    {
        if (current == null || current.pre == null)
            return Optional.empty();

        current = current.pre;
        return Optional.ofNullable(current.data);
    }

    @Override
    public Optional<E> backward()
    {
        if (current == null || current.next == null)
            return Optional.empty();

        current = current.next;
        return Optional.ofNullable(current.data);
    }

    @Override
    public Optional<E> peek()
    {
        if (current == null)
            return Optional.empty();

        return Optional.ofNullable(current.data);
    }

    @Override
    public String toString()
    {
        if (size == 0)
            return "[]";

        var sb = new StringBuilder().append('[');

        var curr = first;
        while (curr != null)
        {
            sb.append(curr.data).append(", ");
            curr = curr.next;
        }

        return sb.delete(sb.length() - 2, sb.length())
                 .append(']')
                 .toString();
    }

    /**
     * The list node.
     *
     * @param <E> the type of the list node.
     * */
    private static class ListNode<E>
    {
        private final ListNode<E> pre;
        private final E data;

        private ListNode<E> next;

        /**
         * Creates a new list node with a pre and
         * some data.
         *
         * @param data the data of this list node.
         * @param pre the previous list node.
         * */
        private ListNode(E data, ListNode<E> pre)
        {
            this.data = data;
            this.pre = pre;
            this.next = null;
        }

        @Override
        public String toString()
        {
            var className = data.getClass().toString();
            className = className.substring(className.lastIndexOf('.') + 1);
            return "ListNode<%s>(data: %s, pre: %s)".formatted(className, data, pre);
        }
    }
}
