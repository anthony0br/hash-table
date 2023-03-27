// Using an interface as a hash table is one implementation of a dictionary.
// This is the same as inheritance except the interace contains no implementation.
interface Dictionary {
    // public int getSize();
    // public void rehash();
    public void add(String key, String value);
    public String getValue(String key);
    public void delete(String key);
}

// Note it is advisable to use prime number sizes
// Could use a class Bucket to implement multiple data types in the hash table
class HashTable implements Dictionary {
    private String[][] hashArray;
    private int capacity;
    private int size;
    private int SKIP_FACTOR = 1;

    /* Example of bucket class that could be used to implement this hash table to store multiple data tpyes
    class Bucket<T> {
        private T data;

        public Bucket(T data) {
            setData(data);
        }

        public void setData(T newData) {
            data = newData;
        }

        public T getData() {
            return data;
        }
    }
    */

    private static int convertASCII(String str) {
        int result = 0;
        for (int i = 0; i < str.length(); i++) {
            result = result + str.charAt(i);
        }
        return result;
    }

    // Instantiate hash table with size many buckets
    public HashTable(int capacity) {
        this.capacity = capacity;
        size = 0;
        hashArray = new String[capacity][2];
    }

    private int hashFunction(String key) {
        int asciiValue = convertASCII(key);
        return asciiValue % capacity;
    }

    // Returns index of either the first matching pair or the next free pair
    private int getIndex(String key) {
        int hash = hashFunction(key);

        // Search for the next free or matching bucket, starting at the hash (linear probing)
        int index = hash;
        // Iterate the length of the array
        for (int i = 0; i < this.capacity; i++) {
            // If free or matching, set found = false and return the index
            if (hashArray[index][0] == null || hashArray[index][0].equals(key)) {
                return index;
            }
            // Skip to the next address
            index += SKIP_FACTOR;
            // Loop over to the start if the end of the array is reached
            if (index >= this.capacity) {
                index = this.capacity - 1 - index;
            }
        }
        return -1;
    }

    public void add(String key, String value) {
        // Check if the table is full first
        if (size >= capacity) {
            return;
        }

        // Get the next empty index, or the index of
        int index = getIndex(key);
        if (index == -1) {
            return;
        }

        // Insert at that index
        hashArray[index][1] = value;
        size = size + 1;
    }

    public String getValue(String key) {
        int index = getIndex(key);

        // If index not found
        if (index == -1) {
            return "";
        }

        // Return index
        return hashArray[index][1];
    }

    public void delete(String key) {
        //size = size - 1
    }
}

public class App {
    public static void main(String[] args) {
        // Create a hash table of size 31 (prime)
        Dictionary dictionary = new HashTable(31);
        dictionary.add("key1", "value1");
        System.out.println(dictionary.getValue("key1"));
        System.out.println(dictionary.getValue("key2"));
    }
}