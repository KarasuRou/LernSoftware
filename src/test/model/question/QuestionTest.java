package model.question;

import model.error.question.IllegalAnswerTypeException;
import model.error.question.IllegalExtraParameterException;
import model.error.question.IllegalQuestionMessageTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class QuestionTest {

    @Test
    @DisplayName("questionMessage")
    void setQuestionMessage() {
        Question question = new Question();

        assertThrows(IllegalQuestionMessageTypeException.class, () -> {
            question.setQuestionType(QuestionTyp.MultipleChoiceQuestion);
            question.setQuestionMessage("");
        });
        assertDoesNotThrow(() ->{
            question.setQuestionMessage(new String[]{"",""});
        });
        assertThrows(IllegalQuestionMessageTypeException.class, () ->{
            question.setQuestionType(QuestionTyp.WordsQuestion);
            question.setQuestionMessage(1);
        });
        assertDoesNotThrow(() ->{
            question.setQuestionMessage("");
        });
        assertThrows(IllegalQuestionMessageTypeException.class, () ->{
            question.setQuestionType(QuestionTyp.DirectQuestion);
            question.setQuestionMessage(1);
        });
        assertDoesNotThrow(() ->{
            question.setQuestionMessage("");
        });
    }

    @Test
    @DisplayName("answer")
    void setAnswer() {
        Question question = new Question();

        assertThrows(IllegalAnswerTypeException.class, () -> {
            question.setQuestionType(QuestionTyp.MultipleChoiceQuestion);
            question.setAnswer(1);
        });
        assertDoesNotThrow(() ->{
            question.setAnswer(new int[]{1,2});
        });
        assertThrows(IllegalAnswerTypeException.class, () ->{
            question.setQuestionType(QuestionTyp.WordsQuestion);
            question.setAnswer(1);
        });
        assertDoesNotThrow(() ->{
            question.setAnswer("");
        });
        assertThrows(IllegalAnswerTypeException.class, () ->{
            question.setQuestionType(QuestionTyp.DirectQuestion);
            question.setAnswer(1);
        });
        assertDoesNotThrow(() ->{
            question.setAnswer("");
        });
    }

    //TODO
    @Test
    @DisplayName("extraParameter")
    void setExtraParameter() {
        Question question = new Question();

        assertThrows(IllegalExtraParameterException.class, ()->{
            question.setQuestionType(QuestionTyp.MultipleChoiceQuestion);
            question.setExtraParameter(Question.DEFAULT_EXTRA_PARAMETER);
        });
        assertDoesNotThrow(()->{
            question.setQuestionType(QuestionTyp.WordsQuestion);
            question.setExtraParameter(Question.DEFAULT_EXTRA_PARAMETER);
        });
        assertThrows(IllegalExtraParameterException.class, ()->{
            question.setQuestionType(QuestionTyp.DirectQuestion);
            question.setExtraParameter(Question.DEFAULT_EXTRA_PARAMETER);
        });
    }
}