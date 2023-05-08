package service;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GradesServiceTest extends AbstractServiceTest {
    @After
    public void tearDown() {
        serviceTestSetup.tearDown();
    }

    @Test
    public void saveGradeUnsuccessful() {
        setUp();
        String idStudent = "1";
        String idTema = "1";
        String feedback = "feedback";
        int grade = 10;
        long initialSize = service.findAllNote().spliterator().getExactSizeIfKnown();
        int result = service.saveNota(idStudent, idTema, grade, 1, feedback);
        long finalSize = service.findAllNote().spliterator().getExactSizeIfKnown();
        assertEquals(-1, result);
        assertEquals(initialSize, finalSize);
    }

    @Test
    public void saveGradeSuccessfulBigBang() {
        setUp();
        String idStudent = "1";
        String name = "nume";
        int group = 999;
        service.saveStudent(idStudent, name, group);

        String idTema = "1";
        String description = "desc";
        int deadline = 2;
        int startline = 1;
        service.saveTema(idTema, description, deadline, startline);

        String feedback = "feedback";
        int grade = 10;
        long initialSize = service.findAllNote().spliterator().getExactSizeIfKnown();
        int result = service.saveNota(idStudent, idTema, grade, 1, feedback);
        long finalSize = service.findAllNote().spliterator().getExactSizeIfKnown();
        assertEquals(1, result);
        assertEquals(initialSize + 1, finalSize);
    }
}
