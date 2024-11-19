package io.github.fi0x.util.rest.validation;

import io.github.fi0x.util.dto.UserDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * This validator is used for the {@link PasswordMatch} annotation
 */
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object>
{
	/**
	 * Initialization method of this validator.
	 *
	 * @param constraintAnnotation The annotation that is associated with this validator
	 */
	@Override
	public void initialize(PasswordMatch constraintAnnotation)
	{
	}

	/**
	 * Determines, if a UserDto has a valid password and matching password.
	 *
	 * @param o                          The {@code UserDto} that should be validated.
	 * @param constraintValidatorContext The context.
	 * @return Whether the passwords in the UserDto match or not.
	 */
	@Override
	public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext)
	{
		UserDto userDto = (UserDto) o;
		return userDto.getPassword().equals(userDto.getMatchingPassword());
	}
}
