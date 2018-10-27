package validation.compare;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DoublePeriodValidator.class)
public @interface DoublePeriod {
    String message() default "{com.ptit.edu.store.validation.compare.DoublePeriodValidator.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String start();
    String end();
}
