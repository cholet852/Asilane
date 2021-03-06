/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.core;

import com.asilane.core.facade.Question;
import com.asilane.core.facade.Response;

/**
 * Each Service have to implemente this interface to be used on the application
 * 
 * @author walane
 */
public interface IService {
	/**
	 * This will return an appropriate answer to the question in the good Locale
	 * 
	 * @param question
	 * 
	 * @return an appropriate answer to the question in the good Locale
	 */
	Response handleService(final Question question);

	/**
	 * This will return an appropriate answer if there is no answer given
	 * 
	 * @param question
	 * 
	 * @return an appropriate answer if there is no answer given
	 */
	Response handleRecoveryService(final Question question);
}