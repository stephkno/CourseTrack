package global;

import java.util.Iterator;
import java.io.Serializable;

public class LinkedList<T> implements Iterable<T>, Serializable {

	public static class Node<T> implements Serializable {
		
		public Node(T value) {
			data = value;
		}
		T data;
		Node<T> next;
	}
	
	private Node<T> first;
	private int numItems = 0;

	public Boolean Contains(T other) {
		for (T item : this) if(item.equals(other)) return true;
		return false;
	}
	
	public void Push(T value) {
		
		Node<T> n = new Node(value);
		
		if(first == null) {
			first = n;
			numItems++;
			return;
		}

		Node<T> curr = first;
		
		while(curr.next != null) {
			curr = curr.next;
		}

		curr.next = n;
		numItems++;
		
	}
	
	public T Get(int i) {
		
		if(first == null) {
	        throw new IndexOutOfBoundsException("Index: " + i );
		}
		
		if(i > numItems-1) {
			return null;
		}
		
		int c = 0;
		Node<T> curr = first;

		
		while(c < i && curr.next != null) {
			c++;

			curr = curr.next;
		}
		
		if(curr == null) return null;
	
		return curr.data;
		
		
	}
	
	public boolean Remove(T t) {
		
		if(first == null){
			Log.Msg("first null");
			return false;
		}		

		if(first.data.equals(t)) {
			Log.Msg("A LinkedList: " + first.data.toString() + " " + t.toString());
			first = first.next;
			numItems--;
			return true;
		}

		Node<T> curr = first;

	    while(curr != null){
			
			if(curr.data.equals(t)) {

				Log.Msg("B LinkedList: " + first.data.toString() + " " + t.toString());

				curr = curr.next;
				return true;
			}

			curr = curr.next;
	    
		} 
		Log.Msg("out");

		return false;
	}
	
	public boolean Remove(int i) {
		
		if(i < 0 || i > numItems || first == null) {
			return false;
		}
		
		if(i == 0) {
			first = first.next;
			numItems--;
			return true;
		}

		Node<T> curr = first;

	    for (int c = 0; c < i - 1; c++) {
		    curr = curr.next;
	    }
	    
	    if(curr.next != null) {
	    	curr.next = curr.next.next;
	    	numItems--;
	    }

		return true;
	
	}
	
	public int Length() {
		return numItems;
	}

	public Object[] toArray(){
		Object[] outArray = new Object[numItems];
		
		int i = 0;
	    for(Node<T> curr = first; curr != null; curr = curr.next){
			outArray[i++] = curr.data;
		} 

		return outArray;
	}
	
	public String toString() {
		 Node<T> curr = first;
		 String out = "";
		 
		 while (curr != null) {
			 out += curr.data + " ";
			 curr = curr.next;
		 }
		 
		 return out;
	}
	
	@Override
	public Iterator<T> iterator() {
		
		return new Iterator<T>() {
			Node<T> item = first;
			
			@Override
			public boolean hasNext() { return item != null; }
			
			@Override
			public T next() {
				
				if(!hasNext()) throw new java.util.NoSuchElementException();
				
				Node<T> curr = item;
				item = item.next;
				return curr.data;
				
			}
		};
	}
}
