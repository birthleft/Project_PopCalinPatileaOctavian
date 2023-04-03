package service;

import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    private Service service;
    @BeforeEach
    void setUp() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation implementation = builder.getDOMImplementation();
            org.w3c.dom.Document document = implementation.createDocument(null, "note", null);
            document.setXmlVersion("1.0");

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new java.io.File("students_test.xml"));
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

        Validator<Student> studentValidator = new StudentValidator();
        Validator<Tema> temaValidator = new TemaValidator();
        Validator<Nota> notaValidator = new NotaValidator();

        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, "students_test.xml");
        TemaXMLRepository fileRepository2 = new TemaXMLRepository(temaValidator, "teme.xml");
        NotaXMLRepository fileRepository3 = new NotaXMLRepository(notaValidator, "note.xml");

        service = new Service(fileRepository1, fileRepository2, fileRepository3);
    }

    @AfterEach
    void tearDown() {
        java.io.File file = new java.io.File("students_test.xml");
        file.delete();
    }

    @Test
    void saveStudentIdEmpty() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent("", "Calin e smecher", 935);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }

    @Test
    void saveStudentIdNotNumerical() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent("42069a", "Calin e smecher", 935);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }

    @Test
    void saveStudentNameEmpty() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent("42070", "", 935);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }

    @Test
    void saveStudentIdIsLowerBoundGroupIsLowerBound() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        var student = service.saveStudent("1", "Calin e smecher", 100);
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(student, 1);
        assertEquals(initialLen + 1, finalLen);
    }


    @Test
    void saveStudentIdIsUpperBoundGroupIsUpperBound() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        var student = service.saveStudent(Integer.toString(Integer.MAX_VALUE), "Calin e smecher", 999);
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(student, 1);
        assertEquals(initialLen + 1, finalLen);
    }
    @Test
    void saveStudentIdIsLowerBoundMinusOne() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent("0", "Calin e smecher", 935);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }

    @Test
    void saveStudentIdIsLowerBoundPlusOneGroupIsLowerBoundPlusOne() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        var student = service.saveStudent("2", "Calin e smecher", 101);
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(student, 1);
        assertEquals(initialLen + 1, finalLen);
    }

    @Test
    void saveStudentIdIsUpperBoundPlusOne() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent(Double.toString((double) Integer.MAX_VALUE + 1), "Calin e smecher", 935);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }

    @Test
    void saveStudentIdIsUpperBoundMinusOneGroupIsUpperBoundMinusOne() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        var student = service.saveStudent(Integer.toString(Integer.MAX_VALUE - 1), "Calin e smecher", 999);
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(student, 1);
        assertEquals(initialLen + 1, finalLen);
    }

    @Test
    void saveStudentGroupIsLowerBoundMinusOne() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent("42073", "Calin e smecher", 99);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }

    @Test
    void saveStudentGroupIsUpperBoundPlusOne() {
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent("42074", "Calin e smecher", 1000);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }
}