/*
 * Copyright 2015-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.platform.launcher.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

class ManualLauncherWithPostProcessorTests {

	@Test
	void test() {
		LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
				// See https://github.com/junit-team/junit5/issues/506
				.extendWith(new Mutator("one else!"))
				//
				// https://github.com/junit-team/junit5/issues/1196
				//.configurationParameter("thing", "one else!")
				//
				//
				.selectors(DiscoverySelectors.selectClass(Something.class)).build();
		SummaryGeneratingListener summary = new SummaryGeneratingListener();
		LauncherFactory.create().execute(request, summary);
		assertEquals(0, summary.getSummary().getTestsFailedCount());
	}

	static class Mutator implements TestInstancePostProcessor {

		private final String value;

		Mutator(String value) {
			this.value = value;
		}

		@Override
		public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
			Something.class.getField("thing").set(testInstance, value);
		}
	}

	static class Something {

		public String thing = "body.";

		@Test
		void some() {
			assertEquals("Someone else!", "Some" + thing);
		}
	}

}
