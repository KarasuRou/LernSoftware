package model.question;

/**
 * <p>Definition of the {@link QuestionTyp}, used in {@link Question}s.</p>
 * <p>Please pay attention, to the required Answer-/ and Question-typs.</p>
*/
public enum QuestionTyp {
    /**
     * <h2>MultipleChoiceQuestion Expect:</h2>
     * <p><b>Answer-typ:</b> {@code int[]} -> position(s) in the question Array</p>
     * <p><b>Question-typ:</b> {@code String[]}</p>
     * <p><b>Extra parameter:</b> String - Question content</p>
     */
    MultipleChoiceQuestion,

    /**
     * <h2>WordsQuestion Expect:</h2>
     * <p><b>Answer-typ:</b> {@code String}</p>
     * <p><b>Question-typ:</b> {@code String}</p>
     * <p><b>Extra parameter (%):</b> {@code double} -> accuracy</p>
     */
    WordsQuestion,

    /**
     * <h2>DirectQuestion Expect:</h2>
     * <p><b>Answer-typ:</b> {@code String}</p>
     * <p><b>Question-typ:</b> {@code String}</p>
     */
    DirectQuestion,

    /**
     * <h2>UNSET ENUM, EXCEPTION "PREPROGRAMMED" ^^</h2>
     */
    UNSET
}
