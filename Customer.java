
public class Customer implements ICustomer {
	
	Pizza Order(int pizzaSize, boolean isUrgent)
	{
		return new Pizza(pizzaSize, isUrgent);
	}

	@Override
	public void notify(Pizza pizza, boolean delivered) {
		// TODO Auto-generated method stub

	}

}
