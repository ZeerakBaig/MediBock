package DataStructures;

public class ArrayList<F> implements ListInterface<F> {
	
	public static final int CAPACITY = 16;
	private F[] data;
	private int size = 0;
	
	//default constructor
	public ArrayList()
	{
		this(CAPACITY);
	}
	public ArrayList(int cap)
	{
		data = (F[]) new Object[cap];
	}

	@Override
	public int size() {
		
		return size;
	}

	@Override
	public boolean isEmpty() {
		
		return size==0;
	}

	@Override
	public F get(int i) throws IndexOutOfBoundsException {
		
		checkIndex(i,size);
		
		return data[i];
		
	}

	@Override
	public F set(int i, F e) throws IndexOutOfBoundsException {
		
		checkIndex(i,size);
		F tempdata = data[i];
		data[i] = e;
		
		return tempdata;
	}

	@Override
	public void add(int i, F e) throws IndexOutOfBoundsException {
		
		checkIndex(i,size+1);
		if(size==data.length)
		{
			throw new IllegalStateException("Array is full");
		}
		 for(int x = size-1 ; x >= i; x--)
		 {
			 data[x+1] = data[x];
		 }
		 data[i] = e;
		 size++;
		 
		
	}

	@Override
	public F remove(int i) throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		checkIndex(i,size);
		 F tempdata = data[i];
		  for(int x = i; x < size-1;x++)
		  {
			  data[x] = data[x+1];
		  }
		  data[size-1] = null;
		  size--;
		return tempdata;
	}
	
	
	protected void checkIndex(int x , int y) throws IndexOutOfBoundsException{
		if(x<0||x>=y)
		{
			throw new IndexOutOfBoundsException("Illegal index: "+ x);
		}
	}
	
	public void displayContents()
	{
		for(int x = 0 ; x < data.length ; ++x)
		{
			System.out.println("The data is " + data[x]);
		}
	}

}
