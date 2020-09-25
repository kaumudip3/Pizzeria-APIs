/*
 * Kaumudi Patil
 * Pizzeria APIs 
 * 
 * Implemented a Producer-Consumer model:
 * Pizzeria allows : PlaceOrder, Baking and Delivery service in parallel / concurrently
 * Baking service consumes the valid PlaceOrder Queue87j .
 * Delivery service in turn consumes valid Baking Queue.
 *  
 * Pizza class :
 * PriorityBlockingQueue is thread-safe choice in this case as we need to
 * prioritize based on urgency; Implemented an explicit comparator to sort priority queue based on 
 * urgency of the order.
 * 
 * TODO : Customer notify() - >should be an observer pattern here
 * 
 * Note : To run test cases in void main : 
 * For more realistic use-cases :
 * Toggle following lines of codes :
 * IDeliveryVechicle.java - line 7 and 8
 * Oven.java - line 12 and 13
 */

import java.util.concurrent.PriorityBlockingQueue;

class PlaceOrderService implements Runnable
{
	private PriorityBlockingQueue<Pizza> orderQ;
	private PriorityBlockingQueue<Pizza> orderForBaking;
	int capacity = 25;
	int totalPizzaSize = 0;
	
	public PlaceOrderService( PriorityBlockingQueue<Pizza> q, PriorityBlockingQueue<Pizza> q2, int totalPizzaSize)
	{
		this.orderQ = q;
		this.orderForBaking = q2;
		this.totalPizzaSize = totalPizzaSize;
	}
	
	public synchronized void run()
	{
		while(true) {
		if (orderQ.isEmpty() || (orderQ.size()+orderForBaking.size()) > 26) 
		{
			try {
				//Check later
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        }
		
		// 
		// Using peek here in case the totalPizza size capacity
		// exceeds 250, don't process yet 
		Pizza pizza = orderQ.peek();
		if(pizza == null) break;
		
		int inputPizzaSize = pizza.getSize(); 
		//Pizza Size 4-16
		if( inputPizzaSize > 3 && inputPizzaSize < 17)
 		{
 			if( (totalPizzaSize+inputPizzaSize) < 251 )
 			{
 				System.out.println("Place order - "+ pizza.getSize());
 				totalPizzaSize += inputPizzaSize;
 				orderForBaking.put(pizza); 

 				// remove from previous list
 				orderQ.poll();
            
 				// notifies the consumer thread that 
 				// now it can start consuming 
 				notify(); 

 				try {  
 					Thread.sleep(1000);
 					} catch (InterruptedException e) {
 						e.printStackTrace();
 						} 
 				}// if not totalPizzaSize reached 250 end
 			
        }// if valid pizza size 
		else
	    {
			// notify failure
			System.out.println("***********Order cannot be processed, Delivery Failed!"+ pizza.getSize());
	 		orderQ.poll();
	 	}
		} // syncn
	}
}

class BakingOrderService implements Runnable
{
		
		private PriorityBlockingQueue<Pizza> orderForBaking;
		public PriorityBlockingQueue<Pizza> orderForDelivery;
		
		Oven ovenObject;
		int totalPizzaSize;
		int capacity = 25;
		public BakingOrderService(PriorityBlockingQueue<Pizza> q, Oven oven, PriorityBlockingQueue<Pizza> qout, int totalPizzaSize) {
		
			this.orderForBaking = q;
			this.ovenObject = oven;
			this.orderForDelivery =  qout;
			this.totalPizzaSize = totalPizzaSize;
		}
		public synchronized void run()
		{ 
			while(true) {
            if (orderForBaking.isEmpty()) {
				try {
					//Check later
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
            }
            
            // to retrive the first job in the list
            Pizza pizza = orderForBaking.poll(); 
        	if(pizza == null) continue;
        	
        	System.out.println("Baking Order in progress - "+String.valueOf(pizza.getSize()));
        	
            if(ovenObject.bake(pizza) == false) // operation failed
        	{
        		// TODO Return Failed status to customer
            	System.out.println("***********Failure to Bake, Delivery Failed!"+ pizza.getSize());
        		// Removed from list
        		
        	}
        	else
        	{
        		
        		try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	
        		// Send for delivery
        		orderForDelivery.put(pizza);
        	}
           
        	totalPizzaSize -= pizza.getSize();

            // Wake up producer thread 
            notify(); 

            // and sleep 
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
            
			}
        } //synch


}

// Using Runnable interface as it allows sharing same 
// resource with multiple threads
class DeliveryOrderService implements Runnable
{
		
		private PriorityBlockingQueue<Pizza> orderForDelivery;
		DeliveryVehicle deliveryObject;
		int capacity = 25;
		int totalPizzaSize;
		public DeliveryOrderService(PriorityBlockingQueue<Pizza> q, DeliveryVehicle deliveryObject, int totalPizzaSize) {
		
			this.orderForDelivery = q;
			this.deliveryObject = deliveryObject;
			this.totalPizzaSize = totalPizzaSize;
		}
		public synchronized void run()
		{ 
           
			while(true) {
            if (orderForDelivery.isEmpty()) {
				try {
					//wait();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
            }
            
            // to retrive the first job in the list 
            Pizza pizza = orderForDelivery.poll(); 
            if(pizza == null) continue;
            System.out.println("Delivery in progress-"+ pizza.getSize()); 
            
        		if(deliveryObject.deliver(pizza) == false)
        		{
        			// TODO Return Failed status to customer
            		System.out.println("***********Failure to Deliver, Delivery failed!"+ pizza.getSize());
            		// Removed from list
        		}
        		else
        		{
        			try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			
        			// TODO notify customer
        		 System.out.println("*****Order Successfully delivered! - " + pizza.getSize()+"****");
        	 		
        		}
        	// Wake up producer thread 
            notify(); 

            // and sleep 
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        } //synch
		}


}

class Pizzeria
{
	// PriorityQueue is not thread safe
	// Using PriorityBlockingQueue
	
	Oven ovens[];
	int ovenIndex;
	int deliveryVehicleIndex;
	DeliveryVehicle deliveryVechicles[];
	PriorityBlockingQueue<Pizza> orders = new PriorityBlockingQueue<>();
	PriorityBlockingQueue<Pizza> ordersBake = new PriorityBlockingQueue<>();
	PriorityBlockingQueue<Pizza> ordersDelivery = new PriorityBlockingQueue<>();
	
    int capacity = 25; 
	int totalPizzaSize;

	Pizzeria()
	{
		this.ovens = new Oven[100];
		
		this.deliveryVechicles = new DeliveryVehicle[100];
		
		totalPizzaSize = 0;

		ovenIndex = 0;
		
	}
	
	Pizzeria(PriorityBlockingQueue<Pizza> orders)
	{
		this.ovens = new Oven[100];
		
		this.deliveryVechicles = new DeliveryVehicle[100];
		
		this.totalPizzaSize = 0;
		
		this.orders = orders;
		
		Thread th1 = new Thread( new PlaceOrderService(orders, ordersBake, totalPizzaSize));
		// TODO : Should process pizza in separate ovens and delivery vehicles 
		Thread th2 = new Thread( new BakingOrderService(ordersBake, new Oven(), ordersDelivery, totalPizzaSize));
		Thread th3 = new Thread( new BakingOrderService(ordersBake, new Oven(), ordersDelivery, totalPizzaSize));
		Thread th4 = new Thread( new DeliveryOrderService(ordersDelivery, new DeliveryVehicle(), totalPizzaSize));
			
		th1.start();
		th2.start();
		th3.start();
		th4.start();

	}
	
}// class pizzeria stop


public class PizzeriaOrders
{
	public static void main(String args[]) throws InterruptedException
	{
		
		// Test 1 :  
		// Order based on urgency
		
		PriorityBlockingQueue<Pizza> orders = new PriorityBlockingQueue<>();
		Customer c1 = new Customer();
		Customer c2 = new Customer();
		orders.put(new Pizza(12, false));
		orders.put(new Pizza(23, false));
		orders.put(new Pizza(10, true));
		orders.put(new Pizza(10, true));
		orders.put(new Pizza(10, true));
		orders.put(new Pizza(12, false));
		orders.put(new Pizza(13, false));
		orders.put(new Pizza(14, false));
		orders.put(new Pizza(8, false));
		orders.put(new Pizza(6, false));
		orders.put(new Pizza(15, false));
		orders.put(new Pizza(9, true));
		orders.put(new Pizza(9, true));
		Pizzeria pizzeria_test1 = new Pizzeria(orders);
		
		
		
		// Test case 2 : 
		// Order multiple pizzas
		/*PriorityBlockingQueue<Pizza> orders2 = new PriorityBlockingQueue<>();
		for(int i=0;i<30;i++) 
		{
			orders2.put(new Pizza(15, false));
			Pizzeria pizzeria_test2 = new Pizzeria(orders2);
		}	
		*/
		
		// Test case 3 : 
		// Order multiple pizzas
		/*PriorityBlockingQueue<Pizza> orders3 = new PriorityBlockingQueue<>();
		for(int i=0;i<30;i++) 
		{
			orders3.put(new Pizza(30, false));
			Pizzeria pizzeria_test3 = new Pizzeria(orders3);
		}
		*/	
				
	}		
}

