/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.service.FortyTwo;

import com.asilane.core.facade.Question;
import com.asilane.core.facade.Response;
import com.asilane.service.IService;

/**
 * @author walane
 * 
 */
public class FortyTwoService implements IService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleService(com.asilane.core.facade.Question)
	 */
	@Override
	public Response handleService(final Question question) {
		return new Response(String.valueOf(42));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asilane.service.IService#handleRecoveryService(com.asilane.core.facade.Question)
	 */
	@Override
	public Response handleRecoveryService(final Question question) {
		return null;
	}
}