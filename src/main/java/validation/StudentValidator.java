package validation;
import domain.Student;

public class StudentValidator implements Validator<Student> {
    public void validate(Student student) throws ValidationException {
        String errors = "";
        if (student.getID() == null || student.getID().equals("")) {
            errors += "ID should not be empty! \n";
        } else {

            if (!student.getID().matches("-?\\d+")) {
                errors += "ID should be a number! \n";
            } else {

                if (Double.parseDouble(student.getID()) <= 0) {
                    errors += "ID should be a positive number! \n";
                } else {

                    if (Double.parseDouble(student.getID()) > Integer.MAX_VALUE) {
                        errors += "ID should be less than MAX_INT! \n";
                    }
                }
            }
        }

        if (student.getNume() == null || student.getNume().equals("")) {
            errors += "Name should not be empty! \n";
        }

        if (student.getGrupa() < 0) {
            errors += "Group should be a positive number! \n";
        } else {

            if (student.getGrupa() >= 1000) {
                errors += "Group should be less than MAX_INT! \n";
            } else {
                if (student.getGrupa() < 100) {
                    errors += "Group should be greater than 100! \n";
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}

