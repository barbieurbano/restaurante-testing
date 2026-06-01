package com.restaurante.restaurantetesting;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({"com.restaurante.restaurantetesting.repository"})
public class RepositoryTestsSuite {
}
