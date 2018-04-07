package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(Profiles.JPA)
public class UserServiceTestingJPATest extends UserServiceTesting {

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void getWithMeals() throws Exception {
        super.getWithMeals();
    }
}
