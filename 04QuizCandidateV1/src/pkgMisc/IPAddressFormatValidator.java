package pkgMisc;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPAddressFormatValidator
{

    public static boolean validate(String str) throws UnknownHostException
    {
	try
	{
	    InetAddress.getByName(str);
	} catch (UnknownHostException e)
	{
	    return false;
	}
	return true;
    }
}