package model.error.updater;

public class ConfigurationException extends UpdaterException{

    public ConfigurationException(){super();}
    public ConfigurationException(String message){super(message);}
    public ConfigurationException(String message, Throwable cause){super(message, cause);}
    public ConfigurationException(Throwable cause){super();}
}
