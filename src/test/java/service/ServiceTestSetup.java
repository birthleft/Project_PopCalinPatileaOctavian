package service;

import domain.Nota;
import domain.Pair;
import domain.Student;
import domain.Tema;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.Validator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServiceTestSetup {
    public final static String ASSIGNMENT_FILENAME = "assignnments_test.xml";
    public final static String STUDENT_FILENAME = "students_test.xml";
    public final static String GRADE_FILENAME = "grades_test.xml";
    public Service service;

    public ServiceTestSetup() {
    }

    public void setUp(List<Student> students, List<Tema> assignments, List<Nota> grades) {
        createXmlFile(
                STUDENT_FILENAME,
                "student",
                students.stream().map(
                        student -> new Pair<>(
                                Map.ofEntries(Map.entry("ID", (Object) student.getID())),
                                Map.ofEntries(
                                        Map.entry("Nume", (Object) student.getNume()),
                                        Map.entry("Grupa", (Object) student.getGrupa())
                                )
                        )
                ).collect(Collectors.toList()));

        createXmlFile(
                ASSIGNMENT_FILENAME,
                "tema",
                assignments.stream().map(
                        assignment -> new Pair<>(
                                Map.ofEntries(Map.entry("ID", (Object) assignment.getID())),
                                Map.ofEntries(
                                        Map.entry("Descriere", (Object) assignment.getDescriere()),
                                        Map.entry("Deadline", (Object) assignment.getDeadline()),
                                        Map.entry("Startline", (Object) assignment.getStartline())
                                )
                        )
                ).collect(Collectors.toList()));

        createXmlFile(
                GRADE_FILENAME,
                "nota",
                grades.stream().map(
                        grade -> new Pair<>(
                                Map.ofEntries(
                                        Map.entry("IDStudent", (Object) grade.getID().getObject1()),
                                        Map.entry("IDTema", (Object) grade.getID().getObject2())
                                ),
                                Map.ofEntries(
                                        Map.entry("Nota", (Object) grade.getNota()),
                                        Map.entry("SaptamanaPredare", (Object) grade.getSaptamanaPredare()),
                                        Map.entry("Feedback", (Object) grade.getFeedback())
                                )
                        )
                ).collect(Collectors.toList()));

        Validator<Student> studentValidator = new StudentValidator();
        Validator<Tema> temaValidator = new TemaValidator();
        Validator<Nota> notaValidator = new NotaValidator();

        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, ServiceTestSetup.STUDENT_FILENAME);
        TemaXMLRepository fileRepository2 = new TemaXMLRepository(temaValidator, ServiceTestSetup.ASSIGNMENT_FILENAME);
        NotaXMLRepository fileRepository3 = new NotaXMLRepository(notaValidator, ServiceTestSetup.GRADE_FILENAME);

        service = new Service(fileRepository1, fileRepository2, fileRepository3);
    }

    public void setUp() {
        setUp(
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    private void createXmlFile(
            String filename,
            String name,
            List<Pair<Map<String, Object>, Map<String, Object>>> elementsList
    ) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation implementation = builder.getDOMImplementation();
            Document document = implementation.createDocument(null, "Entitati", null);

            Element root = document.getDocumentElement();

            elementsList.forEach(
                    currentElement -> {
                        Element element = document.createElement(name);
                        currentElement.getObject1().forEach(
                                (key, value) -> {
                                    element.setAttribute(key, value.toString());
                                }
                        );
                        currentElement.getObject2().forEach(
                                (key, value) -> {
                                    Element newElement = document.createElement(key);
                                    newElement.setTextContent(value.toString());
                                    element.appendChild(newElement);
                                }
                        );
                        root.appendChild(element);
                    }
            );

            document.setXmlVersion("1.0");

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new java.io.File(filename));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public void tearDown() {
        deleteFile(ASSIGNMENT_FILENAME);
        deleteFile(STUDENT_FILENAME);
        deleteFile(GRADE_FILENAME);
    }

    private void deleteFile(String filename) {
        java.io.File file = new java.io.File(filename);
        file.delete();
    }
}
