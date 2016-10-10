package BrianKnottenA1;

import java.util.Arrays;

public class Set<T> implements SetInterface<T>{

    private T[] contents;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Initializes the array to a given capacity
     */
    
    public Set(int capacity){
	@SuppressWarnings("unchecked")
	T[] tempSet = (T[])new Object[capacity];
	contents = tempSet;
	size = 0;
    }

    /** 
     * Initializes array to default capacity
     */

    public Set(){
	this(DEFAULT_CAPACITY);
    }

    /**
     * Determines the current number of entries in this set.
     */

    public int getCurrentSize(){
	return size;
    }

    /**
     * Determines whether the set is empty
     */

    public boolean isEmpty(){
	return size == 0;
    }
    
    /**
     * Adds a new entry to this set, avoiding duplicates
     */
    
    public boolean add(T newEntry) throws SetFullException,
					  java.lang.IllegalArgumentException{
	if(newEntry != null){
	    if(!contains(newEntry)){
		if(size == contents.length){
		    contents = Arrays.copyOf(contents, size*2);
		}
		contents[size] = newEntry;
		size++;
		return true;
	    }else{
		return false;
	    }
	}else{
	    throw new java.lang.IllegalArgumentException("null is not valid!");
	}
    }

    /**
     * Removes a specific entry from this set, if possible.
     */

    public boolean remove(T entry) throws java.lang.IllegalArgumentException{
	if(entry != null){	    
	    if(contains(entry)){
		int index = getIndexOf(entry);
		T result = removeEntry(index);
		return entry.equals(result);
	    }else{
		return false;
	    }
	}else{
	    throw new java.lang.IllegalArgumentException("null is not valid!");
	}
    }

    /** 
     * Returns the index of a particular entry in the set
     */

    private int getIndexOf(T entry){
	int location = -1;
	boolean found = false;
	int index = 0;

	while(!found && (index < size)){
	    if(entry.equals(contents[index])){
		found = true;
		location = index;
	    }
	    index++;
	}
	return location;
    }

    /**
     * Actually performs the removal of an entry in the set given its index
     */

    private T removeEntry(int location){
	T result = null;

	if(!isEmpty() && (location >= 0)){
	    result = contents[location];
	    int lastIndex = size - 1;
	    contents[location] = contents[lastIndex];
	    contents[lastIndex] = null;
	    size--;
	}
	return result;
    }

    /** 
     * Removes an unspecified entry from this set, if possible.
     */

    public T remove(){
        T result = removeEntry(size-1);
	return result;
    }
    
    /** 
     * Removes all entries from this set.
     */

    public void clear(){
	if(!isEmpty()){
	    for(T entry : contents){
		entry = null;
	    }
	    size = 0;
	}
    }

    /**
     * Tests whether this set contains a given entry.
     */
    
    public boolean contains(T entry) throws java.lang.IllegalArgumentException{
	if(entry != null){
	    boolean found = false;
	    int index = 0;
	    while(!found && (index < size)){
		if(entry.equals(contents[index])){
		    found = true;
		}
		index++;
	    }
	    return found;
	}else{
	    throw new java.lang.IllegalArgumentException("null is not valid!");
	}
    }

    /**
     * Retrieves all entries that are in this set.
     */

    public T[] toArray(){
	@SuppressWarnings("unchecked")
	    T[] result = (T[])new Object[size];
	for(int index = 0; index < size; index++){
	    result[index] = contents[index];
	}
	return result;
    }
}
