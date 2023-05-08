package service;

import org.junit.Test;
import validation.ValidationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class StudentsServiceTest extends AbstractServiceTest {

    @Test
    public void saveStudentIdEmpty() {
        setUp();
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent("", "Calin e smecher", 935);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }

    @Test
    public void saveStudentIdNotNumerical() {
        setUp();
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent("42069a", "Calin e smecher", 935);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }

    @Test
    public void saveStudentNameEmpty() {
        setUp();
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent("42070", "", 935);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }

    @Test
    public void saveStudentIdIsLowerBoundGroupIsLowerBound() {
        setUp();
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        var student = service.saveStudent("1", "Calin e smecher", 100);
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(student, 1);
        assertEquals(initialLen + 1, finalLen);
    }


    @Test
    public void saveStudentIdIsUpperBoundGroupIsUpperBound() {
        setUp();
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        var student = service.saveStudent(Integer.toString(Integer.MAX_VALUE), "Calin e smecher", 999);
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(student, 1);
        assertEquals(initialLen + 1, finalLen);
    }

    @Test
    public void saveStudentIdIsLowerBoundMinusOne() {
        setUp();
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent("0", "Calin e smecher", 935);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }

    @Test
    public void saveStudentIdIsLowerBoundPlusOneGroupIsLowerBoundPlusOne() {
        setUp();
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        var student = service.saveStudent("2", "Calin e smecher", 101);
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(student, 1);
        assertEquals(initialLen + 1, finalLen);
    }

    @Test
    public void saveStudentIdIsUpperBoundPlusOne() {
        setUp();
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent(Double.toString((double) Integer.MAX_VALUE + 1), "Calin e smecher", 935);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }

    @Test
    public void saveStudentIdIsUpperBoundMinusOneGroupIsUpperBoundMinusOne() {
        setUp();
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        var student = service.saveStudent(Integer.toString(Integer.MAX_VALUE - 1), "Calin e smecher", 999);
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(student, 1);
        assertEquals(initialLen + 1, finalLen);
    }

    @Test
    public void saveStudentGroupIsLowerBoundMinusOne() {
        setUp();
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent("42073", "Calin e smecher", 99);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }

    @Test
    public void saveStudentGroupIsUpperBoundPlusOne() {
        setUp();
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent("42074", "Calin e smecher", 1000);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }
}
