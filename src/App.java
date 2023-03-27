interface Dictionary {
    // public int getSize();
    // public void rehash();
    public void add();
    public string getValue();
    public void delete();
}

class HashTable implements Dictionary {
    private String[] hashArray;
    private int size;

    public static int convertASCII(String str) {
        int result = 0;
        for (int i = 0; i < str.length(); i++) {
            result = result + str.charAt(i);
        }
        return result;
    }

    // Instantiate hash table with size many buckets
    public HashTable(int size) {
        this.size = size;
        hashArray = new String[size];
    }

    private int hashFunction(String data) {
        int asciiValue = convertASCII(data);
        return asciiValue % size;
    }

    private int get

    // Implemented using linear probing
    public void add(String key, String value) {
        int index = hashFunction(key);
        
    }

    public string getValue(String key) {
        
    }

    public void delete(String key) {
        
    }
}


public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
    }
}

// Create a hash table of size 31 (prime)