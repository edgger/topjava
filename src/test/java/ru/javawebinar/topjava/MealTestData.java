package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class MealTestData {
    public static final List<Meal> USER_MEALS = new ArrayList<>();
    public static final List<Meal> ADMIN_MEALS = new ArrayList<>();

    public static final Meal ADMIN_MEAL = new Meal(100007, LocalDateTime.of(2018, Month.JUNE, 16, 10, 36, 38), "Завтрак1", 1000);
    public static final Meal USER_MEAL = new Meal(100002, LocalDateTime.of(2018, Month.MAY, 16, 10, 36, 38), "Завтрак", 1000);

}
