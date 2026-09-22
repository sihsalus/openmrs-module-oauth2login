/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.openmrs.module.oauth2login.authscheme;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.ProviderService;
import org.openmrs.api.UserService;
import org.openmrs.api.context.AuthenticationScheme;
import org.openmrs.api.context.ContextAuthenticationException;
import org.openmrs.api.context.UsernamePasswordAuthenticationScheme;
import org.openmrs.api.context.UsernamePasswordCredentials;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AuthenticationSchemeSelectionTest {
	
	private AnnotationConfigApplicationContext context;
	
	@Before
	public void setup() {
		context = new AnnotationConfigApplicationContext();
		context.getBeanFactory().registerSingleton("userService", mock(UserService.class));
		context.getBeanFactory().registerSingleton("providerService", mock(ProviderService.class));
		// Core resolves one bean by this interface. The distro's authentication
		// module contributes another scheme even when OAuth2 is enabled.
		context.getBeanFactory().registerSingleton("otherAuthenticationScheme", new UsernamePasswordAuthenticationScheme());
		context.register(OAuth2UserInfoAuthenticationScheme.class);
		context.refresh();
	}
	
	@After
	public void cleanup() {
		context.close();
	}
	
	@Test
	public void enabledOAuth_shouldBeSelectedAlongsideAnotherAuthenticationScheme() {
		assertSame(context.getBean(OAuth2UserInfoAuthenticationScheme.class), context.getBean(AuthenticationScheme.class));
	}
	
	@Test(expected = ContextAuthenticationException.class)
	public void enabledOAuth_shouldRejectPasswordCredentials() {
		context.getBean(AuthenticationScheme.class).authenticate(
		    new UsernamePasswordCredentials("admin", "synthetic-password"));
	}
}
