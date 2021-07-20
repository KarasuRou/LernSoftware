package model.error.updater;

import model.error.LernSoftwareException;

public class UpdaterException extends LernSoftwareException {

    public UpdaterException(){super();}
    public UpdaterException(String message){super(message);}
    public UpdaterException(String message, Throwable cause){super(message, cause);}
    public UpdaterException(Throwable cause){super();}
}
