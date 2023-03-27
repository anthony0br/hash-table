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

    // Value used to represent a deleted node.
    private String TOMBSTONE_KEY = "";

    /* Example of bucket class that could be used to implement this hash table to store multiple data types

    In the current implementation, the key and value must be the same data type due to the used of arrays. This could be changed
    by using a List, or a custom data structure such as a bucket:

    class Bucket<T> {
        private String key;
        private T data;

        public Bucket(String key, T data) {
            this.key = key
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

    // A simple hashing function. 
    // Note that a hashing function that produces a more uniform distribution produces a more efficient hash table.
    // The hash function must also always produce the same value given a key and be very fast to compute
    private int hashFunction(String key) {
        int asciiValue = convertASCII(key);
        return asciiValue % capacity;
    }

    // Returns the index of a pair.
    // allowEmpty toggles whether to look for the next free space or find the exact key.
    // This prevents duplication of code - the method is used for finding the next free space and finding an existing index
    // This is needed in the event pairs are deleted.
    private int getIndex(String key, boolean allowEmpty) {
        int hash = hashFunction(key);

        // Optimisation: Use a different value for deleted and uninitialised, saving time checking if values don't exist
        // Values for deleted items are known as "tombstones"
        // If the table fills up with too many tombstones or needs to be resized, rehashing is used

        // Search for the next free or matching bucket, starting at the hash (linear probing)
        int index = hash;
        // Iterate the length of the array in case there are collisions. Most of the time only 1 or 2 iterations will run.
        for (int i = 0; i < this.capacity; i++) {
            String currentKey = hashArray[index][0];
            
            // If not looking for empty empty values and a null key is found, return -1 (not found).
            // This makes the searching more efficient, there cannot be any more value for the same hash after a null key.
            // This is because null represents uninitialised, whereas TOMBSTONE_KEY represents deleted.
            if (!allowEmpty && currentKey == null) {
                return -1;
            }

            // If the key is null or deleted and we are looking for empty spaces
            boolean emptyCondition = allowEmpty && (currentKey == null || hashArray[index][0].equals(TOMBSTONE_KEY));

            // If the key is not null and is equal to key we are looking for
            boolean equalsCondition = currentKey != null && currentKey.equals(key);

            // Return index if either of the above conditions are met
            if (emptyCondition || equalsCondition) {
                return index;
            }

            // Skip to the next bucket
            index += SKIP_FACTOR;

            // Loop over to the start if the end of the array is reached
            if (index >= this.capacity) {
                index = index - this.capacity;
            }
        }
        return -1;
    }

    // Known as add or put. Adds or overrides a key-value pair.
    public void add(String key, String value) {
        // Check if the table is full first
        if (size >= capacity) {
            System.out.println("FULL");
            return;
        }

        // Check if attempting to use the tombstone key
        if (key.equals(TOMBSTONE_KEY)) {
            System.out.println("Cannot enter tombstone key");
            return;
        }

        // Get the next empty index, or the index of
        int index = getIndex(key, true);
        if (index == -1) {
            return;
        }

        // Insert the key, value pair at the index
        // Buckets have been using a 2D array
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

        // Return copy of the value
        return hashArray[index][1];
    }

    public void delete(String key) {
        int index = getIndex(key, false);

        // If index not found, exit
        if (index == -1) {
            return;
        }

        // Set the key to null, marking the space as deleted
        hashArray[index][0] = TOMBSTONE_KEY;
        size = size - 1;
    }

    public void rehash() {
        // TO DO: write a rehash method that takes the new size and creates a new hash table from the current hash table
    }
}

public class App {
    public static void main(String[] args) {
        // Create a hash table of size 31 (prime) and test it.
        // Using a typical load factor, this should store
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