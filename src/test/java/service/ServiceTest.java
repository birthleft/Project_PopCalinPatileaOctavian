package service;

import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.Before;
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

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    @org.junit.jupiter.api.Test
    void saveStudentNameNotEmpty() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Tema> temaValidator = new TemaValidator();
        Validator<Nota> notaValidator = new NotaValidator();

        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, "students_test.xml");
        TemaXMLRepository fileRepository2 = new TemaXMLRepository(temaValidator, "teme.xml");
        NotaXMLRepository fileRepository3 = new NotaXMLRepository(notaValidator, "note.xml");

        Service service = new Service(fileRepository1, fileRepository2, fileRepository3);
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        var student = service.saveStudent("42069", "Calin e smecher", 935);
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(student, 1);
        assertEquals(initialLen + 1, finalLen);

        service.deleteStudent("42069");
    }

    @Test
    void saveStudentNameEmpty() {
Validator<Student> studentValidator = new StudentValidator();
        Validator<Tema> temaValidator = new TemaValidator();
        Validator<Nota> notaValidator = new NotaValidator();

        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, "students_test.xml");
        TemaXMLRepository fileRepository2 = new TemaXMLRepository(temaValidator, "teme.xml");
        NotaXMLRepository fileRepository3 = new NotaXMLRepository(notaValidator, "note.xml");

        Service service = new Service(fileRepository1, fileRepository2, fileRepository3);
        var initialLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertThrows(ValidationException.class, () -> {
            service.saveStudent("42070", "", 935);
        });
        var finalLen = service.findAllStudents().spliterator().getExactSizeIfKnown();
        assertEquals(initialLen, finalLen);
    }
}