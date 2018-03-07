package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    public void addMeal(Meal meal);
    public void updateMeal(Meal meal);
    public List<Meal> listMeals();
    public Meal getMealById(int id);
    public void removeMeal(int id);
}
