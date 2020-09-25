import java.util.Random;

public interface IDeliveryVechicle {
	default boolean deliver(Pizza pizza)
	{
		// returns true on success, false otherwise
		// operation may fail 
		
		//return new Random().nextBoolean(); // uncomment this line and comment below for realistic situation
		return true;
	}
}
