package service;

import domain.Tema;
import org.junit.Test;
import validation.ValidationException;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class AssignmentsServiceTest extends AbstractServiceTest {

    @Test
    public void saveAssignmentEmptyId() {
        setUp();
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
        setUp();
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
        setUp();
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
        setUp();
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
        setUp();
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
        setUp(Collections.emptyList(), List.of(new Tema(id, description, deadline, startline)), Collections.emptyList());
        long initialSize = service.findAllTeme().spliterator().getExactSizeIfKnown();

        String description2 = "desc";
        int deadline2 = 2;
        int startline2 = 1;
        int result = service.saveTema(id, description2, deadline2, startline2);
        long finalSize = service.findAllTeme().spliterator().getExactSizeIfKnown();
        assertEquals(0, result);
        assertEquals(initialSize, finalSize);
    }
}
