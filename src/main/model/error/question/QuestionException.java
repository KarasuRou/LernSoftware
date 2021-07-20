package model.error.question;

import model.error.LernSoftwareException;

public class QuestionException extends LernSoftwareException {

    public QuestionException(){super();}
    public QuestionException(String message){super(message);}
    public QuestionException(String message, Throwable cause){super(message, cause);}
    public QuestionException(Throwable cause){super(cause);}
}
