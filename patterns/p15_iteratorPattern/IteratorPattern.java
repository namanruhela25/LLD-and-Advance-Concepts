package patterns.p15_iteratorPattern;

interface Iterator<T> {
    boolean hasnext();
    T next();
}

interface Iterable<T> {
    Iterator<T> getIterator();
}

class LinkedList implements Iterable<Integer> {
    int data;
    LinkedList next;

    public LinkedList(int d) {
        this.data = d;
        this.next = null;
    }

    @Override
    public Iterator<Integer> getIterator() {
        return new LinkedListIterator(this);
    }    
}

class LinkedListIterator implements Iterator<Integer> {
    private LinkedList curr;

    public LinkedListIterator(LinkedList head) {
        curr = head;
    }

    @Override
    public boolean hasnext() {
        return curr.next!=null;
    }

    @Override
    public Integer next() {
        int data = curr.data;
        curr = curr.next;
        return data;
    } 


}
 
public class IteratorPattern {
    public static void main(String[] args) {
        LinkedList list1 = new LinkedList(1);
        list1.next = new LinkedList(2);
        list1.next.next = new LinkedList(3);
        list1.next.next.next = new LinkedList(4);
        list1.next.next.next.next = new LinkedList(5);
        
        Iterator<Integer> itr = list1.getIterator(); // list1 first node will return

        while (itr.hasnext()) {
            System.out.println("Node : " + itr.next());
        }
            
    }
}
