package pkgData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Bar
{

    private static Queue<Pizza> pizzas;
    private static Bar INSTANCE;
    public static final int MAX_PIZZAS_ON_BAR = 4;
    
    public synchronized void addPizza(Pizza p) {
	pizzas.add(p);
    }
    
    public synchronized Queue<Pizza> getPizzas(){
	return pizzas;
    }
    
    public static Bar newInstance()
    {
	if(INSTANCE == null) {
	    INSTANCE = new Bar();
	    
	}
	return INSTANCE;
    }
    private Bar()
    {
	pizzas = new LinkedList<Pizza>();
    }

}
