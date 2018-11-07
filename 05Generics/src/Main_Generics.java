import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

public class Main_Generics
{

	public static void main(String[] args)
	{
		ArrayList<BigDecimal> coll = new ArrayList<BigDecimal>();
		coll.add(new BigDecimal(12));
		sum_meth(coll);
	}

	private static double sum_meth(ArrayList<? extends Number> coll)   //extends --> upper bound; super --> lower bound
	{
		return 0d;
	}

}
