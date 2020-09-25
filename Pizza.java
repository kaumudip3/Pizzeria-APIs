
class Pizza implements IPizza, Comparable<Pizza> {

	int size; // data member pizza sizes
	boolean isUrgent;
	
	Pizza(int size, boolean isUrgent)
	{
		this.size = size;
		this.isUrgent = isUrgent;
	}
	
	@Override
	public int getSize()
    {
		return this.size;
	}
	@Override	
	public boolean isUrgent()
	{
		return this.isUrgent;
	}
	
	@Override
    public int compareTo(Pizza pizza1) {
        // Overriding Comparator  
        // to sort urgent orders on top
            if(!this.isUrgent  && pizza1.isUrgent ) {
            	return -1;
            }
            if( !this.isUrgent  && pizza1.isUrgent ) { 
            	return 1;
            }
            return 0;
    
   }
	
}
