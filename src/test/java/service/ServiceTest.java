package service;

import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.DOMImplementation;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import validation.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static org.junit.Assert.*;

public class ServiceTest {

    private final static String ASSIGNMENT_FILENAME = "assignnments_test.xml";
    private final static String STUDENT_FILENAME = "students_test.xml";
    private final static String GRADE_FILENAME = "grades_test.xml";

    private Service service;

    @Before
    public void setUp() {
        createXmlFile(ASSIGNMENT_FILENAME);
        createXmlFile(STUDENT_FILENAME);
        createXmlFile(GRADE_FILENAME);

        Validator<Student> studentValidator = new StudentValidator();
        Validator<Tema> temaValidator = new TemaValidator();
        Validator<Nota> notaValidator = new NotaValidator();

        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, STUDENT_FILENAME);
        TemaXMLRepository fileRepository2 = new TemaXMLRepository(temaValidator, ASSIGNMENT_FILENAME);
        NotaXMLRepository fileRepository3 = new NotaXMLRepository(notaValidator, GRADE_FILENAME);

        service = new Service(fileRepository1, fileRepository2, fileRepository3);
    }

    private void createXmlFile(String filename) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation implementation = builder.getDOMImplementation();
            org.w3c.dom.Document document = implementation.createDocument(null, "note", null);
            document.setXmlVersion("1.0");

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new java.io.File(filename));
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        deleteFile(ASSIGNMENT_FILENAME);
        deleteFile(STUDENT_FILENAME);
        deleteFile(GRADE_FILENAME);
    }

    private void deleteFile(String filename) {
        java.io.File file = new java.io.File(filename);
        file.delete();
    }

    @Test
    public void saveStudentIdEmpty() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent("", "Calin e smecher", 935);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }

    @Test
    public void saveStudentIdNotNumerical() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent("42069a", "Calin e smecher", 935);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }

    @Test
    public void saveStudentNameEmpty() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent("42070", "", 935);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }

    @Test
    public void saveStudentIdIsLowerBoundGroupIsLowerBound() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        var student = service.saveStudent("1", "Calin e smecher", 100);
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(student, 1);
        assertEquals(initialLen + 1, finalLen);
    }


    @Test
    public void saveStudentIdIsUpperBoundGroupIsUpperBound() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        var student = service.saveStudent(Integer.toString(Integer.MAX_VALUE), "Calin e smecher", 999);
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(student, 1);
        assertEquals(initialLen + 1, finalLen);
    }

    @Test
    public void saveStudentIdIsLowerBoundMinusOne() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent("0", "Calin e smecher", 935);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }

    @Test
    public void saveStudentIdIsLowerBoundPlusOneGroupIsLowerBoundPlusOne() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        var student = service.saveStudent("2", "Calin e smecher", 101);
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(student, 1);
        assertEquals(initialLen + 1, finalLen);
    }

    @Test
    public void saveStudentIdIsUpperBoundPlusOne() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent(Double.toString((double) Integer.MAX_VALUE + 1), "Calin e smecher", 935);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }

    @Test
    public void saveStudentIdIsUpperBoundMinusOneGroupIsUpperBoundMinusOne() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        var student = service.saveStudent(Integer.toString(Integer.MAX_VALUE - 1), "Calin e smecher", 999);
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(student, 1);
        assertEquals(initialLen + 1, finalLen);
    }

    @Test
    public void saveStudentGroupIsLowerBoundMinusOne() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent("42073", "Calin e smecher", 99);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }

    @Test
    public void saveStudentGroupIsUpperBoundPlusOne() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent("42074", "Calin e smecher", 1000);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }

    @Test
    public void saveAssignmentEmptyId() {
        String id = "";
        String description = "desc";
        int deadline = 1;
        int startline = 2;
        long initialSize = service.findAllTeme().spliterator().getExactSizeIfKnown();
        assertThrows(
                ValidationException.class,
                () -> service.saveTema(id, description, deadline, startline)
        );
        long finalSize = service.findAllTeme().spliterator().getExactSizeIfKnown();
        assertEquals(initialSize, finalSize);
    }

    @Test
    public void saveAssignmentEmptyDescription() {
        String id = "69";
        String description = "";
        int deadline = 1;
        int startline = 2;
        long initialSize = service.findAllTeme().spliterator().getExactSizeIfKnown();
        assertThrows(
                ValidationException.class,
                () -> service.saveTema(id, description, deadline, startline)
        );
        long finalSize = service.findAllTeme().spliterator().getExactSizeIfKnown();
        assertEquals(initialSize, finalSize);
    }

    @Test
    public void saveAssignmentInvalidDeadline() {
        String id = "69";
        String description = "desc";
        int deadline = 15;
        int startline = 1;
        long initialSize = service.findAllTeme().spliterator().getExactSizeIfKnown();
        assertThrows(
                ValidationException.class,
                () -> service.saveTema(id, description, deadline, startline)
        );
        long finalSize = service.findAllTeme().spliterator().getExactSizeIfKnown();
        assertEquals(initialSize, finalSize);
    }

    @Test
    public void saveAssignmentInvalidStartline() {
        String id = "69";
        String description = "desc";
        int deadline = 1;
        int startline = -1;
        long initialSize = service.findAllTeme().spliterator().getExactSizeIfKnown();
        assertThrows(
                ValidationException.class,
                () -> service.saveTema(id, description, deadline, startline)
        );
        long finalSize = service.findAllTeme().spliterator().getExactSizeIfKnown();
        assertEquals(initialSize, finalSize);
    }

    @Test
    public void saveAssignmentSuccessfully() {
        String id = "69";
        String description = "descr";
        int deadline = 2;
        int startline = 1;
        long initialSize = service.findAllTeme().spliterator().getExactSizeIfKnown();
        int result = service.saveTema(id, description, deadline, startline);
        long finalSize = service.findAllTeme().spliterator().getExactSizeIfKnown();
        assertEquals(1, result);
        assertEquals(initialSize + 1, finalSize);
    }

    @Test
    public void saveAssignmentDuplicate() {
        String id = "69";
        String description = "desc1";
        int deadline = 1;
        int startline = 1;
        service.saveTema(id, description, deadline, startline);
        long initialSize = service.findAllTeme().spliterator().getExactSizeIfKnown();

        String description2 = "desc";
        int deadline2 = 2;
        int startline2 = 1;
        int result = service.saveTema(id, description2, deadline2, startline2);
        long finalSize = service.findAllTeme().spliterator().getExactSizeIfKnown();
        assertEquals(0, result);
        assertEquals(initialSize, finalSize);
    }

    @Test
    public void saveGradeUnsuccessful() {
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