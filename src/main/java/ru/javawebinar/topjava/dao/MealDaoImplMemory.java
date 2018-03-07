package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoImplMemory implements MealDao {
    private static final ConcurrentHashMap<Integer, Meal> meals = new ConcurrentHashMap<>();
    private static final AtomicInteger COUNTER = new AtomicInteger();
    private static volatile MealDao instance;

    private MealDaoImplMemory() {
    }

    static {
        Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        ).forEach(meal -> meals.put(COUNTER.incrementAndGet(),meal));
    }

    public static MealDao getInstance(){
        if (instance==null){
            synchronized (MealDaoImplMemory.class){
                if (instance==null) {
                    instance = new MealDaoImplMemory();
                }
            }
        }
        return instance;
    }

    @Override
    public void addMeal(Meal meal) {
        int id = COUNTER.incrementAndGet();
        meals.put(id, new Meal(meal, id));
    }

    @Override
    public void updateMeal(Meal meal) {
        int id = meal.getId();
        meals.put(id, new Meal(meal, id));
    }

    @Override
    public List<Meal> listMeals() {
        List<Meal> result = new ArrayList<>();
        MealDaoImplMemory.meals.forEach((id, meal) ->  result.add(new Meal(meal, id)));
        return result;
    }

    @Override
    public Meal getMealById(int id) {
        Meal meal = meals.get(id);
        if (meal!=null){
            return new Meal(meal, id);
        } else {
            return null;
        }
    }

    @Override
    public void removeMeal(int id) {
        meals.remove(id);
    }
}
