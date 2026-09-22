/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.oauth2login.web;

import org.openmrs.module.oauth2login.OAuth2LoginConstants;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Dispatcher managed by OpenMRS' module servlet lifecycle, including modules loaded after the web
 * container has started during an initial installation or upgrade.
 */
public class CustomDispatcherServlet extends DispatcherServlet {
	
	private static final long serialVersionUID = 1L;
	
	public CustomDispatcherServlet() {
		setContextConfigLocation("classpath*:/" + OAuth2LoginConstants.MODULE_ARTIFACT_ID + "/webApplicationContext.xml");
	}
}
