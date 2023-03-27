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

    // Returns the index of a pair
    // allowEmpty toggles whether to look for the next free space or find the exact key
    // This is needed in the event pairs are deleted
    private int getIndex(String key, boolean allowEmpty) {
        int hash = hashFunction(key);

        // Potential optimisation: Use a different value for deleted and uninitialised, saving time checking if values don't exist

        // Search for the next free or matching bucket, starting at the hash (linear probing)
        int index = hash;
        // Iterate the length of the array in case there are collisions. Most of the time only 1 or 2 iterations will run.
        for (int i = 0; i < this.capacity; i++) {
            // If the key is null and we are looking for empty spaces
            boolean emptyCondition = allowEmpty && hashArray[index][0] == null;
            // If the key is not null and is equal to key we are looking for
            boolean equalsCondition = hashArray[index][0] != null && hashArray[index][0].equals(key);
            // Return index if either of the above conditions are met
            //System.out.println(hashArray[index][0]);
            //System.out.println(equalsCondition);
            if (emptyCondition || equalsCondition) {
                return index;
            }
            // Skip to the next bucket
            index += SKIP_FACTOR;
            // Loop over to the start if the end of the array is reached
            if (index >= this.capacity) {
                index = index - this.capacity;
                //System.out.println(index);
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
        int index = getIndex(key, true);
        if (index == -1) {
            return;
        }

        // Insert the key, value pair at the index
        hashArray[index][0] = key;
        hashArray[index][1] = value;
        size = size + 1;
    }

    public String getValue(String key) {
        int index = getIndex(key, false);

        // If index not found, exit
        if (index == -1) {
            return null;
        }

        // Return the value
        return hashArray[index][1];
    }

    public void delete(String key) {
        int index = getIndex(key, false);

        // If index not found, exit
        if (index == -1) {
            return;
        }

        // Set the key to null, marking the space as deleted
        hashArray[index][0] = null;
        size = size - 1;
    }
}

public class App {
    public static void main(String[] args) {
        // Create a hash table of size 31 (prime)
        Dictionary dictionary = new HashTable(31);
        dictionary.add("key1", "value1");
        dictionary.add("key1", "epic valye");
        System.out.println(dictionary.getValue("key1"));
        System.out.println(dictionary.getValue("key2"));
        dictionary.add("key1", "epic value number 2");
        dictionary.add("key2", "this is from key 2");
        System.out.println(dictionary.getValue("key1"));
        System.out.println(dictionary.getValue("key2"));
        dictionary.delete("key1");
        System.out.println(dictionary.getValue("key1"));
        System.out.println(dictionary.getValue("key2"));
    }
}