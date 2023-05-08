package service;

import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.After;

import java.util.Collections;
import java.util.List;

public abstract class AbstractServiceTest {
    protected Service service;
    protected ServiceTestSetup serviceTestSetup;

    protected void setUp(List<Student> students, List<Tema> assignments, List<Nota> grades) {
        serviceTestSetup = new ServiceTestSetup();
        serviceTestSetup.setUp(students, assignments, grades);
        service = serviceTestSetup.service;
    }

    protected void setUp() {
        setUp(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    @After
    public void tearDown() {
        serviceTestSetup.tearDown();
    }
}
