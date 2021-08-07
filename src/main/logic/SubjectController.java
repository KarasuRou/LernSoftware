package logic;

import data.SubjectData;
import ui.components.SubjectView;

import java.sql.SQLException;

public class SubjectController {

    private final static SubjectController subjectController = new SubjectController();
    private final SubjectData subjectData = SubjectData.getInstance();
    private final SubjectView subjectView = SubjectView.getInstance();

    private SubjectController(){}


    /**
     * <p>This Method is the "Constructor" for the SubjectController class.</p>
     * <p>This is the only way to access the SubjectController.</p>
     * @return a {@link SubjectController} instance
     */
    public static SubjectController getInstance() {
        return subjectController;
    }

    /**
     * <p>This method will initiate the Connections between the layers.</p>
     * <p><b>NOTE:</b></p>
     * <p>Should always be called before using any Subject class!</p>
     * @throws SQLException if the SQL-initiation has a problem.
     */
    public void init() throws SQLException {
        subjectData.init();
//        subjectView.init();
    }
}
