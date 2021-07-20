package logic;

public class QuestionController {

    private final static QuestionController questionController = new QuestionController();

    private QuestionController(){}

    public static QuestionController getInstance() {
        return questionController;
    }
}
