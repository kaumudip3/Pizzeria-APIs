import java.util.Random;

class Oven implements IOven
{
	Random random = new Random();
	
	// Bake operation
	// May fail
	@Override
	public boolean bake(Pizza pizza)
	{
		 //return random.nextBoolean(); // uncomment this line and comment below for realistic situation
		 return true;
	}

}
