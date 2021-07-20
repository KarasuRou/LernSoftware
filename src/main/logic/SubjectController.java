package logic;

public class SubjectController {

    private final static SubjectController subjectController = new SubjectController();

    private SubjectController(){}

    public static SubjectController getInstance() {
        return subjectController;
    }
}
