package io.github.nivaldosilva.agendador_tarefas.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {
        DataLimiteValidator.class,
        DataLimiteValidatorAtualizar.class
})
@Documented
public @interface DataLimiteValida {
    String message() default "A data limite deve ser posterior à data do evento";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}