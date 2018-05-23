package ru.javawebinar.topjava.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.util.List;

@Component
public class MealValidator implements Validator {

    @Autowired
    MealService mealService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Meal.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.getFieldError("dateTime") == null) {
            Meal meal = (Meal) target;
            AuthorizedUser authorizedUser = AuthorizedUser.safeGet();
            List<Meal> mealList = mealService.getBetweenDateTimes(meal.getDateTime(), meal.getDateTime(), AuthorizedUser.id());
            if (!mealList.isEmpty()&&!mealList.get(0).getId().equals(meal.getId())) {
                errors.rejectValue("dateTime", "", "Meal with this Date&Time already exists");
            }

        }
    }
}
