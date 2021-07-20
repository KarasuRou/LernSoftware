package model.error;

public class LernSoftwareException extends Exception{
    public LernSoftwareException(){super();}
    public LernSoftwareException(String message){super(message);}
    public LernSoftwareException(String message, Throwable cause){super(message, cause);}
    public LernSoftwareException(Throwable cause){super(cause);}
}
