package model.error.question;

public class IllegalAnswerTypeException extends QuestionException{

    public IllegalAnswerTypeException(){super();}
    public IllegalAnswerTypeException(String message){super(message);}
    public IllegalAnswerTypeException(String message, Throwable cause){super(message, cause);}
    public IllegalAnswerTypeException(Throwable cause){super(cause);}
}
