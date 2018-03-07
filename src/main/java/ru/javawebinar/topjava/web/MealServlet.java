package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoImplMemory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static MealDao mealDao = MealDaoImplMemory.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setAttribute("dateTimeFormatter", MealsUtil.getDateTimeFormatter());

        Map<String, String[]> requestParameterMap = request.getParameterMap();
        if (requestParameterMap.containsKey("action")) {
            String action = requestParameterMap.get("action")[0];
            if (requestParameterMap.containsKey("id")) {
                int id = Integer.parseInt(requestParameterMap.get("id")[0]);
                switch (action) {
                    case "edit":
                        log.debug("forward to editMeal");
                        request.setAttribute("meal", mealDao.getMealById(id));
                        request.getRequestDispatcher("/mealEdit.jsp").forward(request, response);
                        break;
                    case "delete":
                        log.debug("delete meal");
                        mealDao.removeMeal(id);
                        response.sendRedirect("/topjava/meals");
                        break;
                }
            } else {
                if ("add".equals(action)){
                    log.debug("forward to editMeal add");
                    request.setAttribute("meal", new Meal(LocalDateTime.now(),"",0));
                    request.getRequestDispatcher("/mealEdit.jsp").forward(request, response);
                }
            }
        } else {
            log.debug("forward to meals");
            request.setAttribute("mealsWithExceed", MealsUtil.getWithExceededInOnePass(mealDao.listMeals(), 2000));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        LocalDateTime dateTime = LocalDateTime.from(MealsUtil.getDateTimeFormatter().parse(request.getParameter("dateTime")));
        Meal meal = new Meal(dateTime, request.getParameter("description"), Integer.parseInt(request.getParameter("calories")));
        int id = Integer.parseInt(request.getParameter("id"));
        if (id!=0){
            meal.setId(id);
            mealDao.updateMeal(meal);
        } else {
            mealDao.addMeal(meal);
        }
        response.sendRedirect("/topjava/meals");
    }
}
