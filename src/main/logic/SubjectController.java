package logic;

import com.sun.istack.internal.Nullable;
import data.SubjectData;
import logic.miscellaneous.Output;
import model.Subject;
import ui.components.SubjectView;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubjectController {

    private final static SubjectController subjectController = new SubjectController();
    private final SubjectData subjectData = SubjectData.getInstance();
    private final SubjectView subjectView = SubjectView.getInstance();

    private SubjectController(){}

    /**
     * <p>This method will create a new Subject.</p>
     * @param name new Subject name
     * @param backgroundPicturePath the backgroundPicturePath (can be null if unset)
     */
    public void addSubject(String name, @Nullable String backgroundPicturePath) {//TODO Save Pictures in Application Directory
        try {
            int id;
            Subject subject = new Subject();
            subject.setName(name);
            if (backgroundPicturePath == null) {
                id = subjectData.createSubject(name);
            } else {
                id = subjectData.createSubject(name, backgroundPicturePath);
                subject.setBackgroundPicturePath(backgroundPicturePath);
            }
            subject.setID(id);
            Output.write("Add Subject: " + subject.getName().getValue() + " (ID: " + subject.getID() + ")");
            subjectView.addSubjectTab(subject);
        } catch (Exception e) {
            Output.exceptionWrite(e, "Failed to addSubject");
        }
    }

    /**
     * <p>This method will rename a specific Subject.</p>
     * @param subject the current Subject
     * @param newName the new Subject name
     */
    public void renameSubject(Subject subject, String newName) {
        if (subject.getName().getValue().equals(newName)) {
            return;
        }
        try {
            if (!subjectData.updateSubjectName(newName, subject.getID())) {
                throw new SQLException();
            }
            Output.write("Renaming Subject to: " + newName + " (ID: " + subject.getID() + ")");
            subjectView.renameSubjectTab(subject, newName);
            subject.setName(newName);
        } catch (Exception e) {
            Output.exceptionWrite(e, "Failed to renameSubject");
        }
    }

    /**
     * <p>This method will change the backgroundPath for a specific Subject.</p>
     * @param subject the current Subject
     * @param backgroundPath the new backgroundPath (that can be null if unset)
     */
    public void changeSubjectBackground(Subject subject, @Nullable String backgroundPath) {
        if ((subject.getBackgroundPicturePath() == null && backgroundPath == null)
                ||
                (subject.getBackgroundPicturePath() != null && subject.getBackgroundPicturePath().equals(backgroundPath)))
        {
            return;
        }
        try {
            String current = null;
            if (backgroundPath != null) {
                current = "'" + backgroundPath + "'";
            }
            if (!subjectData.updateSubjectPicturePath(current, subject.getID())) {
                throw new SQLException();
            }
            subject.setBackgroundPicturePath(backgroundPath);
            Output.write("Changing Subject Background from: " + subject.getName().getValue() + " (ID: " + subject.getID() + ")");
            subjectView.changeBackgroundFromSubjectTab(subject);
        } catch (Exception e) {
            Output.exceptionWrite(e, "Failed to changeSubjectBackground");
        }
    }

    /**
     * <p>This method will delete a specific Subject.</p>
     * @param subject the current Subject
     */
    public void deleteSubject(Subject subject) {
        try {
            if (!subjectData.deleteSubject(subject.getID())) {
                throw new SQLException();
            }
            Output.write("Delete Subject: " + subject.getName().getValue() + " (ID: " + subject.getID() + ")");
            subjectView.deleteSubjectTab(subject);
        } catch (Exception e) {
            Output.exceptionWrite(e, "Failed to deleteSubject");
        }
    }

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
        subjectView.init();

        firstSetUp_Subjects();
    }

    private void firstSetUp_Subjects() {
        try {
            Subject[] subjects = new Subject[subjectData.getSubjectCount()];
            ResultSet resultSet = subjectData.getAllSubjects();
            for (Subject subject : subjects) {
                resultSet.next();
                subject = new Subject();
                subject.setID(resultSet.getInt(1));
                subject.setName(resultSet.getString(2));
                subject.setBackgroundPicturePath(resultSet.getString(3));
                subjectView.addSubjectTab(subject);
            }
        } catch (SQLException e) {
            Output.exceptionWrite(e);
        }
    }
}
