package com.teamcity.api;

import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected SoftAssertions softy;

    @BeforeMethod
    public void beforeMethod() {
        softy = new SoftAssertions();
    }

    @AfterMethod
    public void afterMethod() {
        softy.assertAll();
    }

}
