/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package example.sharedresources;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder.request;

import example.FirstCustomEngine;
import example.SecondCustomEngine;

import org.junit.jupiter.api.Test;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.core.LauncherConfig;
import org.junit.platform.launcher.core.LauncherFactory;

class SharedResourceDemo {

	@SuppressWarnings({ "DataFlowIssue", "NullAway" })
	//tag::user_guide[]
	@Test
	void runBothCustomEnginesTest() {
		FirstCustomEngine firstCustomEngine = new FirstCustomEngine();
		SecondCustomEngine secondCustomEngine = new SecondCustomEngine();

		Launcher launcher = LauncherFactory.create(LauncherConfig.builder()
				// tag::custom_line_break[]
				.addTestEngines(firstCustomEngine, secondCustomEngine)
				// tag::custom_line_break[]
				.enableTestEngineAutoRegistration(false)
				// tag::custom_line_break[]
				.build());

		launcher.execute(request().forExecution().build());

		assertSame(firstCustomEngine.socket, secondCustomEngine.socket);
		assertTrue(firstCustomEngine.socket.isClosed(), "socket should be closed");
	}
	//end::user_guide[]
}
