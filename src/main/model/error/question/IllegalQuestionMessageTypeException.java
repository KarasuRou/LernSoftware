package model.error.question;

public class IllegalQuestionMessageTypeException extends QuestionException{

    public IllegalQuestionMessageTypeException(){super();}
    public IllegalQuestionMessageTypeException(String message){super(message);}
    public IllegalQuestionMessageTypeException(String message, Throwable cause){super(message, cause);}
    public IllegalQuestionMessageTypeException(Throwable cause){super(cause);}
}
