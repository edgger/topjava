package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {

    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private Map<Integer, List<Integer>> mapIdUserIdMeal = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    public InMemoryMealRepositoryImpl() {
        MealsUtil.MEALS.forEach(meal -> this.save(meal,1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {} with userId={}", meal, userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            ArrayList<Integer> idList = new ArrayList<>();
            idList.add(meal.getId());
            mapIdUserIdMeal.merge(userId, idList,(mealIds, newMealId) -> {
                mealIds.addAll(newMealId);
                return mealIds;
            });
            return meal;
        } else {
            if (mapIdUserIdMeal.get(userId).contains(meal.getId())){
                // treat case: update, but absent in storage
                return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
            } else {
                return null;
            }
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {} with userId={}", id, userId);
        if (mapIdUserIdMeal.get(userId).contains(id)){
            repository.remove(id);
            mapIdUserIdMeal.get(userId).remove(new Integer(id));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {} with userId={}", id, userId);
        if (mapIdUserIdMeal.get(userId).contains(id)){
            return repository.get(id);
        } else {
            return null;
        }
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll with userId={}", userId);
        List<Integer> currentUserMealIds = mapIdUserIdMeal.get(userId);
        return currentUserMealIds.stream()
                .map(mealId -> repository.get(mealId))
                .sorted(Comparator.comparing(Meal::getDateTime))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getFiltered(int userId, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("getFiltered with userId={}", userId);
        List<Integer> currentUserMealIds = mapIdUserIdMeal.get(userId);
        return currentUserMealIds.stream()
                .map(mealId -> repository.get(mealId))
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(),startDate,endDate))
                .filter(meal -> DateTimeUtil.isBetween(meal.getTime(),startTime,endTime))
                .sorted(Comparator.comparing(Meal::getDateTime))
                .collect(Collectors.toList());
    }
}

