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

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealWithExceed> getAll() {
        log.info("getAll");
        return MealsUtil.getWithExceeded(service.getAll(AuthorizedUser.id()),AuthorizedUser.getCaloriesPerDay());
    }

    public List<MealWithExceed> getFiltered(String startDateParam, String startTimeParam, String endDateParam, String endTimeParam) {
        log.info("get filtered");

        LocalDate startDate;
        LocalTime startTime;
        LocalDate endDate;
        LocalTime endTime;

        if (startDateParam==null||startDateParam.length()==0) {
            startDate=LocalDate.MIN;
        } else {
            startDate = LocalDate.parse(startDateParam);
        }

        if (startTimeParam==null||startTimeParam.length()==0) {
            startTime=LocalTime.MIN;
        } else {
            startTime = LocalTime.parse(startTimeParam);
        }

        if (endDateParam==null||endDateParam.length()==0) {
            endDate=LocalDate.MAX;
        } else {
            endDate=LocalDate.parse(endDateParam);
        }

        if (endTimeParam==null||endTimeParam.length()==0) {
            endTime=LocalTime.MAX;
        } else {
            endTime=LocalTime.parse(endTimeParam);
        }

        return MealsUtil.getFilteredWithExceeded(
                service.getAll(AuthorizedUser.id()),AuthorizedUser.getCaloriesPerDay()
                ,startDate,startTime,endDate,endTime);

        /*return MealsUtil.getWithExceeded(
                service.getFiltered(AuthorizedUser.id(),startDate,startTime,endDate,endTime),
                AuthorizedUser.getCaloriesPerDay());*/
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id,AuthorizedUser.id());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        ValidationUtil.checkNew(meal);
        Meal savedMeal = service.save(meal, AuthorizedUser.id());
        return get(savedMeal.getId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, AuthorizedUser.id());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        ValidationUtil.assureIdConsistent(meal, id);
        service.save(meal, AuthorizedUser.id());
    }
}