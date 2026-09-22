/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.openmrs.module.oauth2login.web;

import static org.junit.Assert.assertEquals;

import javax.servlet.Servlet;
import javax.servlet.ServletRegistration;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.StaticWebApplicationContext;

public class CustomDispatcherServletTest {
	
	@Test
	public void moduleDispatcher_shouldServeLoginAfterContainerInitialization() throws Exception {
		MockServletContext servletContext = new MockServletContext() {
			
			@Override
			public ServletRegistration.Dynamic addServlet(String name, Servlet servlet) {
				throw new IllegalStateException("The container has already initialized");
			}
		};
		StaticWebApplicationContext context = new StaticWebApplicationContext();
		context.setServletContext(servletContext);
		context.registerSingleton("loginController", LoginController.class);
		context.refresh();
		CustomDispatcherServlet servlet = new CustomDispatcherServlet();
		servlet.setApplicationContext(context);
		try {
			servlet.init(new MockServletConfig(servletContext, "oauth2login"));
			MockHttpServletRequest request = new MockHttpServletRequest(servletContext, "GET", "/openmrs/ms/oauth2login");
			request.setContextPath("/openmrs");
			request.setServletPath("/ms");
			request.setPathInfo("/oauth2login");
			MockHttpServletResponse response = new MockHttpServletResponse();
			
			servlet.service(request, response);
			
			assertEquals(200, response.getStatus());
			assertEquals("login-handler-reached", response.getContentAsString());
		}
		finally {
			servlet.destroy();
			context.close();
		}
	}
	
	@Controller
	public static class LoginController {
		
		@RequestMapping("/oauth2login")
		@ResponseBody
		public String login() {
			return "login-handler-reached";
		}
	}
}
