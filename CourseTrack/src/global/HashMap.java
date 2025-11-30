package global;

import java.util.Iterator;
import java.io.Serializable;

public class HashMap<K,V> implements Iterable<V>, Serializable {
	
	private int numBuckets = 8;
	private int numElems = 0;
	private float maxLoad = 1.0f;
	
	// hash table item stores both a key and data for search purposes
	private class HashMapItem<K,V> implements Serializable {
		
		public HashMapItem(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		public K key;
		public V value;
		
	};
	
	// array of bucket/chains
	private LinkedList<HashMapItem<K,V>>[] buckets;

	// constructors
	public HashMap() {	
		this.numBuckets = 10;
		buckets = new LinkedList[this.numBuckets];
		for(LinkedList bucket : buckets) bucket = new LinkedList<HashMapItem<K,V>>();
	}
	
	public HashMap(int numBuckets) {	
		this.numBuckets = numBuckets;
		buckets = new LinkedList[this.numBuckets];
		for(LinkedList bucket : buckets) bucket = new LinkedList<HashMapItem<K,V>>();
	}
	
	// add a new item and key to map
	public void Put(K key, V value) throws IllegalArgumentException{
		
		// handle null key values
		if(key==null) throw new IllegalArgumentException();
		
		// get index from hashing key
		int index = hash(key);
		LinkedList<HashMapItem<K,V>> chain = buckets[index];
		
		if(chain == null) {
			
			// create new chain
			buckets[index] = new LinkedList();
			chain = buckets[index];
			
		}else {
			
			// overwrite old value
			HashMapItem<K,V> item = this.find(chain, key);
		
			if(item!= null) {			
				item.value = value;
				return;
			}
			
		}
		
		// add new value to chain
		buckets[index].Push(new HashMapItem<K,V>(key, value));
		numElems++;
		
		// check load factor of table
		if (getLoadFactor() > maxLoad) rehash(2);
		
	}
	
	// return value from table with key
	public V Get(K key) throws IllegalArgumentException{
	
		// disallow null keys
		if(key==null) throw new IllegalArgumentException();
		
		// get bucket index from key
		int index = hash(key);

		if(buckets[index] == null) return null;
		
		// get chain from buckets
		LinkedList<HashMapItem<K,V>> chain = buckets[index];
		HashMapItem<K,V> item = this.find(chain, key);
		
		if(item == null) return null;
		
		// return value if found, null if not
		return item.value;
		
	}

	// return count of elements in the table
	public int Size() {
		return numElems;
	}
	
	// does contain key
	public Boolean Contains(K key) {		
		return Get(key) != null;
	}
	
	// remove element by key
	public boolean Remove(K key) {
		
		// disallow null keys
		if(key==null) throw new IllegalArgumentException();
		
		// get bucket index from key
		int index = hash(key);
		
		// get chain from buckets
		LinkedList<HashMapItem<K,V>> chain = buckets[index];
		if(chain == null) return false;
		
		int i = indexOfKey(chain, key);

		if(i < 0) return false;
		
		chain.Remove(i);
		numElems--;

		return true;
		
	}

	// find element in chain with key
	private HashMapItem<K,V> find(LinkedList<HashMapItem<K,V>> chain, K key) {
		
		if(chain == null) return null;
		
		int i = 0;
		Boolean loop = true;
		HashMapItem<K,V> value = null;
		
		// loop until found or end
		while(loop) {
			
			HashMapItem<K,V> hti;
			
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
	private int indexOfKey(LinkedList<HashMapItem<K,V>> chain, K key) {
		if(chain == null || key == null) return -1;
		
		int i = 0;
		while(i < chain.Length()) {
			HashMapItem<K,V> hti = chain.Get(i);
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
		HashMap<K,V> newHashMap = new HashMap<K,V>(this.numBuckets);
		
		for(int b = 0; b < buckets.length; b++) {
			LinkedList<HashMapItem<K,V>> chain = buckets[b];
			
			if(chain != null) {
				for(int j = 0; j < chain.Length(); j++) {
					HashMapItem<K,V> item = chain.Get(j);
					if(item != null) {
						newHashMap.Put(item.key, item.value);
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
	private int hash(K input) {
	
		return Math.abs(input.hashCode()) % numBuckets;

	}
	
	public String toString() {
		
		String out = "{\n";

		for(int b = 0; b < buckets.length; b++) {
			LinkedList<HashMapItem<K,V>> chain = buckets[b];
			
			if(chain != null) {
				for(int j = 0; j < chain.Length(); j++) {
					HashMapItem<K,V> item = chain.Get(j);
					out += "\t" + item.key + ": " + item.value + "\n";
				}
			}
		}

		out += "}";
		return out;

	}

	// iterate over all items in hash map
	@Override
	public Iterator<V> iterator() {
		return new Iterator<V>() {
	
			private int b = 0;
			private int e = 0;

			@Override
	        public boolean hasNext() {
				while(b < buckets.length) {
					if(buckets[b] != null &&
					e < buckets[b].Length()) {
						return true;
					}
					b++;
					e = 0;
				}
				return false;
	        }
	        
	        @Override
	        public V next() {
				if(!hasNext())
					throw new java.util.NoSuchElementException();

				return buckets[b].Get(e++).value;
	        }
	    };
	}
	
}