package com.teamcity.api;

import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected SoftAssertions softy;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        softy = new SoftAssertions();
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        softy.assertAll();
    }

}
