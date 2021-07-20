package model.error.question;

public class IllegalExtraParameterException extends QuestionException{

    public IllegalExtraParameterException(){super();}
    public IllegalExtraParameterException(String message){super(message);}
    public IllegalExtraParameterException(String message, Throwable cause){super(message, cause);}
    public IllegalExtraParameterException(Throwable cause){super(cause);}
}
