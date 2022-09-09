package se.skorup.API.collections.mutable_collections;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * A thread safe FIFO queue that will block the thread from
 * accessing the queue if it is being used by another thread.
 *
 * @param <E> The type of the elements in the BlockingQueue.
 * */
public class BlockingQueue<E>
{
    private final Queue<E> q;

    private final Semaphore mutex;
    private final Semaphore sem;

    /**
     * Creates a new BlockingQueue.
     * */
    public BlockingQueue()
    {
        this.q = new LinkedList<>();
        this.mutex = new Semaphore(1);
        this.sem = new Semaphore(0);
    }

    /**
     * This will add an item to the queue. It will
     * wait until the queue is free to use it.
     *
     * @param item the item that is supposed to be inserted
     *             into the queue.
     * */
    public void enqueue(E item) throws InterruptedException
    {
        mutex.acquire();
        q.offer(item);
        mutex.release();
        sem.release();
    }

    /**
     * This will dequeue an item from the queue. The item
     * that will be dequeued is the item that has been inside
     * the queue the longest. It will not dequeue an item if
     * it is the queue is empty or the queue is being accessed by
     * another thread.
     *
     * @return the dequeued item.
     * */
    public E dequeue() throws InterruptedException
    {
        sem.acquire();
        mutex.acquire();
        var elem = q.poll();
        mutex.release();
        return elem;
    }

    /**
     * Adds all elements to the queue in the order of the collection
     * provided. This will only run when the queue is not being used
     * by another thread. It will wait for the queue to be free.
     *
     * @param elems the elements to be added.
     * */
    public void addAll(Collection<E> elems) throws InterruptedException
    {
        mutex.acquire();
        q.addAll(elems);
        mutex.release();
        sem.release(elems.size());
    }

    /**
     * Checks if the queue is empty at this point in time. Might not hold
     * if many threads access the queue at the same time. Then it could get
     * false positives. It will wait for the queue to be free until it queries
     * and checks if the queue is empty.<br><br>
     *
     * <b>WARNING!</b> <i>If used in a multi threaded context
     * then the result of the operation might not hold. It can be the case that
     * it is empty whilst checking but before the result is useful it can be falsified
     * by another thread adding another element.</i>
     *
     * @return {@code true} iff it is empty at the time of querying, else
     *         {@code false}. <b>WARNING!</b> <i>If used in a multi threaded context
     *         then the result of the operation might not hold. It can be the case that
     *         it is empty whilst checking but before the result is useful it can be falsified
     *         by another thread adding another element.</i>
     * */
    public boolean isEmpty() throws InterruptedException
    {
        mutex.acquire();
        var res = q.isEmpty();
        mutex.release();

        return res;
    }
}
