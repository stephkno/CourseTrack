package clientGUI.UIInformations;


public class UIArrayList<T> {
    @SuppressWarnings("unchecked")
    T[] list = (T[]) new Object[2];
    private int index = 0;
    public UIArrayList() {

    }
    public UIArrayList(T[] data) {
        list = data;
        index = data.length;
    }

    public int getLength() {return index;}
    public T get(int ind) {return list[ind];}
    public void append(T item) {
        resizeList();
        list[index] = item;
        index++;
    }
    public void insert(T item, int ind) {
        ind = ind % index;
        resizeList();
        T[] listCopy = list.clone();
        for(int i = ind; i < index; i++) {
            list[i+1] = listCopy[i];
        }
        list[ind] = item;
        index++;
    }
    public void pop() {
        if(index == 0) {return;}
        index--;
        list[index] = null;
    }
    public void pop(int ind) {
        ind = ind % index;
        //this is cool it handles negatives and numbers beyond the index
        for(int i = ind+1; i < index; i++) {
            list[i-1] = list[i];
        }
        index--;
        list[index] = null;
    }
    public void remove(T item) {
        int setIndex = 0;
        
        for(int i = 0; i < index; i++) {
            T citem = list[i];
            if(item.equals(citem)) {
                setIndex++;
                index--;
            }
            list[i] = list[setIndex];
            
            setIndex++;
        }
        int toRemove = setIndex-index;
        for(int i = 0; i < toRemove; i++) {
            list[index+toRemove] = null;
        }
    }

    public void clear() {
        index = 0;
        list = NewList();
    }

    private void resizeList() {
        if(index == list.length) {
            T[] newList = NewList(list.length*2);
            for(int i = 0; i < list.length; i++) {
                newList[i] = list[i];
            }
            list = newList;
        }
    }


    public void foreach(forEachAction<T> func) {
        for(int i = 0; i < index; i++) {
            T item = list[i];
            func.action(item);
        }
    }

    public static interface forEachAction<E> {
        void action(E val);
    }

    public T[] toArray(T[] l) {
        for(int i = 0; i < index; i++) { l[i] = get(i); }
        return l;
    }

    public boolean contains(T item) {

        for(int i = 0; i < index; i++) {
            if(list[i].equals(item)) {
                return true;
            }
        }
        return false;
    }

    private T[] NewList() {
        @SuppressWarnings("unchecked")
        T[] newlist = (T[]) new Object[2];
        return newlist;
    }
    private T[] NewList(int len) {
        @SuppressWarnings("unchecked")
        T[] newlist = (T[]) new Object[len];
        return newlist;
    }
}
