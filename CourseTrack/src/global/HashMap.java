package global;

import java.util.Iterator;
import java.io.Serializable;

public class HashMap<T> implements Iterable<T>, Serializable{
	
	private int numBuckets = 8;
	private int numElems = 0;
	private float maxLoad = 1.0f;
	
	// hash table item stores both a key and data for search purposes
	private class HashMapItem<T> implements Serializable{
		
		public HashMapItem(String key, T data) {
			this.key = key;
			this.data = data;
		}
		
		public String key;
		public T data;
		
	};
	
	// array of bucket/chains
	private LinkedList<HashMapItem<T>>[] buckets;

	// constructors
	public HashMap() {	
		this.numBuckets = 10;
		buckets = new LinkedList[this.numBuckets];
		for(LinkedList bucket : buckets) bucket = new LinkedList<HashMapItem<T>>();
	}
	
	public HashMap(int numBuckets) {	
		this.numBuckets = numBuckets;
		buckets = new LinkedList[this.numBuckets];
		for(LinkedList bucket : buckets) bucket = new LinkedList<HashMapItem<T>>();
	}
	
	// add a new item and key to map
	public void Put(String key, T value) throws IllegalArgumentException{
		
		// handle null key values
		if(key==null) throw new IllegalArgumentException();
		
		// get index from hashing key
		int index = hash(key);
		LinkedList<HashMapItem<T>> chain = buckets[index];
		
		if(chain == null) {
			
			// create new chain
			buckets[index] = new LinkedList();
			chain = buckets[index];
			
		}else {
			
			// overwrite old value
			HashMapItem<T> item = this.find(chain, key);
		
			if(item!= null) {			
				item.data = value;
				return;
			}
			
		}
		
		// add new value to chain
		buckets[index].Push(new HashMapItem<T>(key, value));
		numElems++;
		
		// check load factor of table
		if (getLoadFactor() > maxLoad) rehash(2);
		
	}
	
	// return value from table with key
	public T Get(String key) throws IllegalArgumentException{
	
		// disallow null keys
		if(key==null) throw new IllegalArgumentException();
		
		// get bucket index from key
		int index = hash(key);
		
		if(buckets[index] == null) return null;
		
		// get chain from buckets
		LinkedList<HashMapItem<T>> chain = buckets[index];
		HashMapItem<T> item = this.find(chain, key);
		
		if(item == null) return null;
		
		// return value if found, null if not
		return item.data;
		
	}

	// return count of elements in the table
	public int Size() {
		return numElems;
	}
	
	// does contain key
	public Boolean Contains(String key) {		
		return Get(key) != null;
	}
	
	// remove element by key
	public boolean Remove(String key) {
		
		// disallow null keys
		if(key==null) throw new IllegalArgumentException();
		
		// get bucket index from key
		int index = hash(key);
		
		// get chain from buckets
		LinkedList<HashMapItem<T>> chain = buckets[index];
		if(chain == null) return false;
		
		int i = indexOfKey(chain, key);

		if(i < 0) return false;
		
		chain.Remove(i);
		numElems--;

		return true;
		
	}

	// find element in chain with key
	private HashMapItem<T> find(LinkedList<HashMapItem<T>> chain, String key) {
		
		if(chain == null) return null;
		
		int i = 0;
		Boolean loop = true;
		HashMapItem<T> value = null;
		
		// loop until found or end
		while(loop) {
			
			HashMapItem<T> hti;
			
			try {
				hti = chain.Get(i++);
			}catch (IndexOutOfBoundsException e) {
				return null;
			}
			
			// found end of list
			if(hti == null) {
				loop = false;
			}
			// found item key?
			else if(hti.key.equals(key)) {
				loop = false;
				value = hti;
			}
			
		}
		
		return value;
	}
	
	// get index of item with key in chain
	private int indexOfKey(LinkedList<HashMapItem<T>> chain, String key) {
		if(chain == null || key == null) return -1;
		
		int i = 0;
		while(i < chain.Length()) {
			HashMapItem<T> hti = chain.Get(i);
			if(hti != null && hti.key.equals(key)) {
				return i;  // Found
			}
			i++;
		}
		return -1;  // Not found
	}
	
	// rebuild new hashmap
	private void rehash(int resize) {
		
		this.numBuckets = numBuckets*resize;
		HashMap<T> newHashMap = new HashMap<T>(this.numBuckets);
		
		for(int b = 0; b < buckets.length; b++) {
			LinkedList<HashMapItem<T>> chain = buckets[b];
			
			if(chain != null) {
				for(int j = 0; j < chain.Length(); j++) {
					HashMapItem<T> item = chain.Get(j);
					if(item != null) {
						newHashMap.Put(item.key, item.data);
					}
				}
			}
		}
		this.buckets = newHashMap.buckets;
		this.numBuckets = newHashMap.numBuckets;
	    this.numElems = newHashMap.numElems;
		
	}
	
	private float getLoadFactor() {	
	
		return (float)numElems / (float)numBuckets;
	
	}
	
	// get hash map bucket index given key
	private int hash(String input) {
	
		return Math.abs(input.hashCode()) % numBuckets;

	}
	
	public String toString() {
		
		String out = "{\n";

		for(int b = 0; b < buckets.length; b++) {
			LinkedList<HashMapItem<T>> chain = buckets[b];
			
			if(chain != null) {
				for(int j = 0; j < chain.Length(); j++) {
					HashMapItem<T> item = chain.Get(j);
					out += "\t" + item.key + ": " + item.data + "\n";
				}
			}
		}

		out += "}";
		return out;

	}

	// return array of items in hash map
	public T[] toArray() {
		
	    int totalSize = 0;
	    for (int b = 0; b < buckets.length; b++)
	        if (buckets[b] != null) 
	            totalSize += buckets[b].Length();
	  
	    T[] result = (T[]) new Object[totalSize];
	    
	    int i = 0;
	    for (int b = 0; b < buckets.length; b++)
	        if (buckets[b] != null)
	            for (int ii = 0; ii < buckets[b].Length(); ii++) result[i++] = buckets[b].Get(ii).data;
	        
	    return result;
	}
	
	// iterate over all items in hash map
	@Override
	public Iterator<T> iterator() {
	    return new Iterator<T>() {
	        private T[] array = toArray();
	        private int i = 0;
	        
	        @Override
	        public boolean hasNext() {
	            return i < array.length;
	        }
	        
	        @Override
	        public T next() {
	            return array[i++];
	        }
	    };
	}
	
}