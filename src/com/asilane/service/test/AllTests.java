/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.service.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DateServiceTest.class, FindPlaceServiceTest.class, IPServiceTest.class, MailServiceTest.class,
		WeatherForecastServiceTest.class, WikipediaServiceTest.class, YouTubeServiceTest.class })
public class AllTests {

}