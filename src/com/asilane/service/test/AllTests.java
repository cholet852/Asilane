package com.asilane.service.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DateServiceTest.class, FindPlaceServiceTest.class, IPServiceTest.class, MailServiceTest.class,
		WeatherForecastServiceTest.class, WikipediaServiceTest.class, YouTubeServiceTest.class })
public class AllTests {

}