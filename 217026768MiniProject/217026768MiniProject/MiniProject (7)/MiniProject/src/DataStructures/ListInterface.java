package DataStructures;

//interface for the arraylist class
public interface ListInterface<F> {
    int size();
	
	boolean isEmpty();
	
	F get(int i) throws IndexOutOfBoundsException;
	
	F set(int i, F e) throws IndexOutOfBoundsException;
	
	void add(int i, F e)throws IndexOutOfBoundsException;
	
	F remove(int i) throws IndexOutOfBoundsException;

}
