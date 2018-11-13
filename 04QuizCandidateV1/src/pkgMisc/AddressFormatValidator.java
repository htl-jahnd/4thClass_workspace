package pkgMisc;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.InetAddressValidator;

public class AddressFormatValidator
{

    public static boolean isValidateIPAddress(String ipAddress) 
    {
	return InetAddressValidator.getInstance().isValidInet4Address(ipAddress);
    }

    public static boolean isValidEmailAddress(String email)
    {
	return EmailValidator.getInstance().isValid(email);
    }
}