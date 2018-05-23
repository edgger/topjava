package ru.javawebinar.topjava.web.validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

@Component
public class UserToValidator implements Validator {

    @Autowired
    UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserTo userTo = (UserTo) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "", "Empty email");
        try {
            User userByEmail = userService.getByEmail(userTo.getEmail());
            AuthorizedUser authorizedUser = AuthorizedUser.safeGet();
            if (userByEmail != null
                    && (authorizedUser == null
                        || (userTo.getId()!=null && !userTo.getId().equals(userByEmail.getId()))
                        || (userTo.getId()==null && AuthorizedUser.id()!=userByEmail.getId()))) {
                errors.rejectValue("email", "", "User with this email already exists");
            }
        } catch (NotFoundException ignored) {
        }
    }
}
