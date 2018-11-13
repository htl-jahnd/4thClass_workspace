package Exceptions;

public class InvalidEmailException extends Exception
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public InvalidEmailException()
    {
	// TODO Auto-generated constructor stub
    }

    public InvalidEmailException(String message)
    {
	super(message);
	// TODO Auto-generated constructor stub
    }

    public InvalidEmailException(Throwable cause)
    {
	super(cause);
	// TODO Auto-generated constructor stub
    }

    public InvalidEmailException(String message, Throwable cause)
    {
	super(message, cause);
	// TODO Auto-generated constructor stub
    }

    public InvalidEmailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
	super(message, cause, enableSuppression, writableStackTrace);
	// TODO Auto-generated constructor stub
    }

}
