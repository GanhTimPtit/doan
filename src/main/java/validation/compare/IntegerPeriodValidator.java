package validation.compare;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class IntegerPeriodValidator implements ConstraintValidator<IntegerPeriod, Object> {
    private String end;
    private String start;

    @Override
    public void initialize(IntegerPeriod constraintAnnotation) {
        this.end = constraintAnnotation.end();
        this.start = constraintAnnotation.start();
    }

    @Override
    public boolean isValid(Object inputValue, ConstraintValidatorContext context) {
        Class<?> inputClass = inputValue.getClass();
        try {
            Field greaterField = inputClass.getDeclaredField(end);
            Field lessField = inputClass.getDeclaredField(start);
            greaterField.setAccessible(true);
            lessField.setAccessible(true);

            int greaterValue = greaterField.getInt(inputValue);
            int lessValue = lessField.getInt(inputValue);

            greaterField.setAccessible(false);
            lessField.setAccessible(false);

            return greaterValue != -1 && greaterValue > lessValue;
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            return false;
        }
    }
}
