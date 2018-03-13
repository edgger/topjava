package ru.javawebinar.topjava.web.meal;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealWithExceed> getAll() {
        log.info("getAll");
        return MealsUtil.getWithExceeded(service.getAll(AuthorizedUser.id()),AuthorizedUser.getCaloriesPerDay());
    }

    public List<MealWithExceed> getFiltered(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("get filtered");

        if (startDate==null) {startDate=LocalDate.MIN;}
        if (startTime==null) {startTime=LocalTime.MIN;}
        if (endDate==null) {endDate=LocalDate.MAX;}
        if (endTime==null) {endTime=LocalTime.MAX;}

        return MealsUtil.getWithExceeded(
                service.getFiltered(AuthorizedUser.id(),startDate,startTime,endDate,endTime),
                AuthorizedUser.getCaloriesPerDay());
    }

    public MealWithExceed get(int id) {
        log.info("get {}", id);
        return MealsUtil.getWithExceeded(service.getAll(AuthorizedUser.id()), AuthorizedUser.getCaloriesPerDay()).stream()
                .filter(mealWithExceed -> mealWithExceed.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public MealWithExceed create(MealWithExceed mealWithExceed) {
        log.info("create {}", mealWithExceed);
        Meal meal = new Meal(mealWithExceed.getDateTime(), mealWithExceed.getDescription(), mealWithExceed.getCalories());
        Meal savedMeal = service.save(meal, AuthorizedUser.id());
        return get(savedMeal.getId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, AuthorizedUser.id());
    }

    public void update(MealWithExceed mealWithExceed, int id) {
        log.info("update {} with id={}", mealWithExceed, id);
        Meal meal = new Meal(mealWithExceed.getId(), mealWithExceed.getDateTime(), mealWithExceed.getDescription(), mealWithExceed.getCalories());
        ValidationUtil.assureIdConsistent(meal, id);
        service.save(meal, AuthorizedUser.id());
    }
}