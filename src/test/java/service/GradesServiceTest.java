package service;

import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GradesServiceTest extends AbstractServiceTest {
    private static final double DEDUCTED_PER_WEEK = 2.5;
    @Mock
    private StudentXMLRepository studentXMLRepository;

    @Mock
    private NotaXMLRepository notaXMLRepository;

    @Mock
    private TemaXMLRepository temaXMLRepository;

    private void mockStudents() {
        studentXMLRepository = Mockito.mock(StudentXMLRepository.class);
        List<Student> students = new LinkedList<>();

        Mockito.when(
                studentXMLRepository.save(Mockito.any(Student.class))
        ).then((i) -> {
            students.add(i.getArgument(0));
            return null;
        }).thenReturn(null);

        Mockito.when(
                studentXMLRepository.findOne(Mockito.anyString())
        ).thenAnswer(new Answer<Student>() {
            @Override
            public Student answer(InvocationOnMock invocation) throws Throwable {
                if (students.isEmpty()) {
                    return null;
                } else {
                    return students.get(0);
                }
            }
        });

        Mockito.when(
                studentXMLRepository.findAll()
        ).thenReturn(students);
    }

    private void mockAssignments() {
        temaXMLRepository = Mockito.mock(TemaXMLRepository.class);
        List<Tema> assignments = new LinkedList<>();

        Mockito.when(
                temaXMLRepository.save(Mockito.any(domain.Tema.class))
        ).then(
                (i) -> {
                    assignments.add(i.getArgument(0));
                    return null;
                }
        ).thenReturn(null);

        Mockito.when(
                temaXMLRepository.findOne(Mockito.anyString())
        ).thenAnswer(new Answer<Tema>() {
            @Override
            public Tema answer(InvocationOnMock invocation) throws Throwable {
                if (assignments.isEmpty()) {
                    return null;
                } else {
                    return assignments.get(0);
                }
            }
        });

        Mockito.when(
                temaXMLRepository.findAll()
        ).thenReturn(assignments);
    }

    private void mockGrades() {
        notaXMLRepository = Mockito.mock(NotaXMLRepository.class);
        List<Nota> note = new LinkedList<>();

        Mockito.when(
                notaXMLRepository.save(Mockito.any(Nota.class))
        ).then(
                (i) -> {
                    note.add(i.getArgument(0));
                    return null;
                }
        ).thenReturn(null);

        Mockito.when(
                notaXMLRepository.findAll()
        ).thenReturn(note);
    }

    @After
    public void tearDown() {
        if (serviceTestSetup != null)
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

    @Test
    public void testAddStudentMockRepository() {
        mockStudents();
        Service service = new Service(studentXMLRepository, temaXMLRepository, notaXMLRepository);


        String idStudent = "1";
        String name = "nume";
        int group = 999;

        assertEquals(service.findAllStudents().spliterator().getExactSizeIfKnown(), 0);
        int result = service.saveStudent(idStudent, name, group);
        assertEquals(result, 1);
        assertEquals(service.findAllStudents().spliterator().getExactSizeIfKnown(), 1);

        Student student = service.findAllStudents().iterator().next();
        assertEquals(student.getID(), idStudent);
        assertEquals(student.getNume(), name);
        assertEquals(student.getGrupa(), group);
    }

    @Test
    public void testAddStudentAddAssignmentMockRepository() {
        mockStudents();
        mockAssignments();

        Service service = new Service(studentXMLRepository, temaXMLRepository, notaXMLRepository);

        String idStudent = "1";
        String name = "nume";
        int group = 999;
        assertEquals(service.findAllStudents().spliterator().getExactSizeIfKnown(), 0);
        int result = service.saveStudent(idStudent, name, group);
        assertEquals(result, 1);
        assertEquals(service.findAllStudents().spliterator().getExactSizeIfKnown(), 1);

        String idTema = "1";
        String description = "desc";
        int deadline = 2;
        int startline = 1;
        assertEquals(service.findAllTeme().spliterator().getExactSizeIfKnown(), 0);
        result = service.saveTema(idTema, description, deadline, startline);
        assertEquals(result, 1);
        assertEquals(service.findAllTeme().spliterator().getExactSizeIfKnown(), 1);

        Tema assignment = service.findAllTeme().iterator().next();
        assertEquals(assignment.getID(), idTema);
        assertEquals(assignment.getDescriere(), description);
        assertEquals(assignment.getDeadline(), deadline);
        assertEquals(assignment.getStartline(), startline);
    }

    @Test
    public void testAddStudentAddAssignmentAddGradeMockRepository() {
        mockStudents();
        mockAssignments();
        mockGrades();

        Service service = new Service(studentXMLRepository, temaXMLRepository, notaXMLRepository);
        String idStudent = "1";
        String name = "nume";
        int group = 999;
        assertEquals(service.findAllStudents().spliterator().getExactSizeIfKnown(), 0);
        int result = service.saveStudent(idStudent, name, group);
        assertEquals(result, 1);
        assertEquals(service.findAllStudents().spliterator().getExactSizeIfKnown(), 1);

        String idTema = "1";
        String description = "desc";
        int deadline = 2;
        int startline = 1;
        assertEquals(service.findAllTeme().spliterator().getExactSizeIfKnown(), 0);
        result = service.saveTema(idTema, description, deadline, startline);
        assertEquals(result, 1);
        assertEquals(service.findAllTeme().spliterator().getExactSizeIfKnown(), 1);

        String feedback = "feedback";
        int grade = 10;
        int weekGiven = 3;
        assertEquals(service.findAllNote().spliterator().getExactSizeIfKnown(), 0);
        result = service.saveNota(idStudent, idTema, grade, weekGiven, feedback);
        assertEquals(1, result);
        assertEquals(service.findAllNote().spliterator().getExactSizeIfKnown(), 1);

        Nota nota = service.findAllNote().iterator().next();
        assertEquals(nota.getID().getObject1(), idStudent);
        assertEquals(nota.getID().getObject2(), idTema);
        assertEquals(nota.getNota(), grade - DEDUCTED_PER_WEEK, 0.0001);
        assertEquals(nota.getFeedback(), feedback);
    }
}
