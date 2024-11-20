package io.github.fi0x.util.rest.validation;

import io.github.fi0x.util.dto.UserDto;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * This annotation requires a {@link UserDto} to have matching passwords.
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchValidator.class)
@Documented
public @interface PasswordMatch
{
	/**
	 * This method provides the error message, that is shown, when the validation fails.
	 *
	 * @return The message.
	 */
	String message() default "Passwords don't match";

	/**
	 * This is a required method for this validation class
	 *
	 * @return An array of classes.
	 */
	Class<?>[] groups() default {};

	/**
	 * This method is required for this class
	 *
	 * @return Returns a payload array
	 */
	Class<? extends Payload>[] payload() default {};
}
