package model.question;

import model.Folder;
import model.error.question.*;

/**
 * <p>The Question class is a model for Questions.</p><br/>
 * <p>It contains a {@linkplain QuestionTyp} wich specifies a specific {@code answer} and {@code questionMessage} typ.<br/>
 * Some {@linkplain QuestionTyp}s require an {@code extraParameter}, but there is a {@linkplain Question#DEFAULT_EXTRA_PARAMETER} given.</p>
 * <p>Questions can be stored in {@linkplain Folder}s.</p>
 */
public class Question {

    /**
     * 90% Accuracy
     */
    public static double DEFAULT_EXTRA_PARAMETER = 0.9;

    private int ID;
    private QuestionTyp questionTyp = QuestionTyp.UNSET;
    private Object questionMessage;
    private Object answer;
    private Object extraParameter;

    /**
     * Construct for the Question class.<br>
     * <p>
     *     This will Create a Question, without a Answer/Question, only set's Typ.
     * </p>
     * @param questionTyp {@link QuestionTyp}
     */
    public Question(QuestionTyp questionTyp){
        if (questionTyp == QuestionTyp.WordsQuestion){
            this.extraParameter = DEFAULT_EXTRA_PARAMETER;
        }
        this.questionTyp = questionTyp;
    }
    /**
     * Construct for the Question class.<br>
     * <p>
     *     This will Create a Question, without a Answer/Question and Typ.
     * </p>
     */
    public Question(){}

    /**
     * <b>Reset's(!!)</b> the whole Question, if QuestionTyp is defined, else it will only change the QuestionTyp.
     * @param questionTyp changes the questionTyp
     */
    public void setQuestionType(QuestionTyp questionTyp) {
        if (questionTyp != QuestionTyp.UNSET){
            resetQuestion();
        }
        if (questionTyp == QuestionTyp.WordsQuestion){
            this.extraParameter = DEFAULT_EXTRA_PARAMETER;
        }
        this.questionTyp = questionTyp;
    }

    /**
     * Set's the questionMessage for the Question, accordingly to the {@link QuestionTyp}.
     * @param questionMessage {@link QuestionTyp}
     * @throws IllegalQuestionMessageTypeException if the questionMessage has the wrong typ
     */
    public void setQuestionMessage(Object questionMessage) throws IllegalQuestionMessageTypeException {
        if (questionMessage.getClass().getSimpleName().equals("String") &&
                (questionTyp == QuestionTyp.WordsQuestion || questionTyp == QuestionTyp.DirectQuestion))
        {
            this.questionMessage = questionMessage;
        }
        else if(questionMessage.getClass().getSimpleName().equals("String[]") &&
                questionTyp == QuestionTyp.MultipleChoiceQuestion)
        {
            this.questionMessage = questionMessage;
        }
        else {
            throw new IllegalQuestionMessageTypeException("Wrong \"QuestionTyp\" in combination with \"QuestionMessage-typ\".\r\n" +
                    "QuestionMessage-typ: \""+ questionMessage.getClass().getSimpleName() +"\" QuestionTyp: \"" + this.questionTyp.toString() + "\"");
        }
    }

    /**
     * Set's the Answer for the Question, accordingly to the {@link QuestionTyp}.
     * @param answer {@link QuestionTyp}
     * @throws IllegalAnswerTypeException if the answer has the wrong typ
     */
    public void setAnswer(Object answer) throws IllegalAnswerTypeException {
        if (answer.getClass().getSimpleName().equals("String") &&
                (questionTyp == QuestionTyp.WordsQuestion || questionTyp == QuestionTyp.DirectQuestion))
        {
            this.answer = answer;
        }
        else if(answer.getClass().getSimpleName().equals("boolean[]") &&
                questionTyp == QuestionTyp.MultipleChoiceQuestion)
        {
            this.answer = answer;
        }
        else {
            throw new IllegalAnswerTypeException("Wrong \"QuestionTyp\" in combination with \"Answer-typ\".\r\n" +
                    "Answer-typ: \""+ answer.getClass().getSimpleName() +"\" QuestionTyp: \"" + this.questionTyp.toString() + "\"");
        }
    }

    /**
     * Set's an extra parameter, that is used in:
     * <ul>
     *     <li>{@linkplain QuestionTyp#WordsQuestion}
     *     <br/>percentage accuracy ({@code double})
     *     <br/>default: 90%</i></li>
     *     <li>{@linkplain QuestionTyp#MultipleChoiceQuestion}
     *     <br/>question content ({@code String})</li>
     * </ul>
     * @throws IllegalExtraParameterException if the extraParameter has the wrong typ
     */
    public void setExtraParameter(Object extraParameter) throws IllegalExtraParameterException {
        if (extraParameter.getClass().getSimpleName().equals("Double") &&
                questionTyp == QuestionTyp.WordsQuestion) {
            this.extraParameter = extraParameter;
        } else if (extraParameter.getClass().getSimpleName().equals("String") &&
                questionTyp == QuestionTyp.MultipleChoiceQuestion) {
            this.extraParameter = extraParameter;
        } else {
            throw new IllegalExtraParameterException("The extra parameter: \"" + extraParameter.getClass().getSimpleName()
                    + "\" is not needed in: \"" + this.questionTyp.toString() + "\"");
        }
    }

    /**
     * @return String[] or String
     */
    public Object getQuestionMessage() {
        return this.questionMessage;
    }

    /**
     * @return int[] or String
     */
    public Object getAnswer() {
        return this.answer;
    }

    /**
     * @return double
     */
    public Object getExtraParameter() {
        return this.extraParameter;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public QuestionTyp getQuestionTyp() {
        return this.questionTyp;
    }

    private void resetQuestion(){
        this.questionTyp = null;
        this.answer = null;
        this.questionMessage = null;
        this.extraParameter = null;
    }
}
