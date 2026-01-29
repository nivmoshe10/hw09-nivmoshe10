/** A linked list of character data objects.
 * (Actually, a list of Node objects, each holding a reference to a CharData object.
 * The list holds the characters that appear in the text and their counts.
 */
public class List {

    // Points to the first node in this list
    public Node first;

    // The number of elements in this list
    public int size;

    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }

    /** Returns the number of elements in this list. */
    public int getSize() {
        return size;
    }

    /** Returns the first element in the list */
    public CharData getFirst() {
        if (first == null) {
            return null;
        }
        return first.cp;
    }

    /** Givens a character, returns the index of the object having this character.
     * If the character is not found in the list, returns -1. */
    public int indexOf(char chr) {
        Node current = first;
        int index = 0;
        while (current != null) {
            if (current.cp.chr == chr) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    /** Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        CharData newCharData = new CharData(chr);
        Node newNode = new Node(newCharData);
        newNode.next = first;
        first = newNode;
        size++;
    }

    /** If the given character exists in one of the CharData objects in this list,
     * increments its counter. Otherwise, adds a new CharData object with the
     * given character to the beginning of this list. */
    public void update(char chr) {
        Node current = first;
        while (current != null) {
            if (current.cp.chr == chr) {
                current.cp.count++;
                return;
            }
            current = current.next;
        }
        // If not found, add to beginning as per instructions
        addFirst(chr);
    }

    /** If the given character exists in one of the CharData objects in this list,
     * removes this CharData object from the list and returns true. Otherwise, returns false. */
    public boolean remove(char chr) {
        Node prev = null;
        Node current = first;
        while (current != null) {
            if (current.cp.chr == chr) {
                if (prev == null) {
                    first = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    /** Returns the CharData object at the specified index in this list. 
     * If the index is negative or is greater than the size of this list, 
     * throws an IndexOutOfBoundsException. */
    public CharData get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.cp;
    }

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public CharData[] toArray() {
        CharData[] arr = new CharData[size];
        Node current = first;
        int i = 0;
        while (current != null) {
            arr[i++]  = current.cp;
            current = current.next;
        }
        return arr;
    }

    /** Returns an iterator over the elements in this list, starting at the given index. */
    public ListIterator listIterator(int index) {
        if (index == 0) return new ListIterator(first);
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return new ListIterator(current);
    }
    
    /** Returns a textual representation of this list. */
    public String toString() {
        if (size == 0) return "()";
        StringBuilder str = new StringBuilder("(");
        Node current = first;
        while (current != null) {
            str.append(current.cp.toString());
            if (current.next != null) {
                str.append(" ");
            }
            current = current.next;
        }
        str.append(")");
        return str.toString();
    }
}