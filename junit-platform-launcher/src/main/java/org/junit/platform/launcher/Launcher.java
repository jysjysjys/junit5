/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.platform.launcher;

import static org.apiguardian.api.API.Status.DEPRECATED;
import static org.apiguardian.api.API.Status.MAINTAINED;
import static org.apiguardian.api.API.Status.STABLE;

import org.apiguardian.api.API;
import org.junit.platform.launcher.core.LauncherExecutionRequestBuilder;

/**
 * The {@code Launcher} API is the main entry point for client code that
 * wishes to <em>discover</em> and <em>execute</em> tests using one or more
 * {@linkplain org.junit.platform.engine.TestEngine test engines}.
 *
 * <p>Implementations of this interface are responsible for determining
 * the set of test engines to delegate to at runtime and for ensuring that
 * each test engine has an
 * {@linkplain org.junit.platform.engine.TestEngine#getId ID} that is unique
 * among the registered test engines. For example, the default implementation
 * returned by {@link org.junit.platform.launcher.core.LauncherFactory#create}
 * dynamically discovers test engines via Java's {@link java.util.ServiceLoader
 * ServiceLoader} mechanism.
 *
 * <p>Test discovery and execution require a {@link LauncherDiscoveryRequest}
 * that is passed to all registered engines. Each engine decides which tests it
 * can discover and execute according to the supplied request.
 *
 * <p>Prior to executing tests, clients of this interface should
 * {@linkplain #registerTestExecutionListeners register} one or more
 * {@link TestExecutionListener} instances in order to get feedback about the
 * progress and results of test execution. Listeners will be notified of events
 * in the order in which they were registered. The default implementation
 * returned by {@link org.junit.platform.launcher.core.LauncherFactory#create}
 * dynamically discovers test execution listeners via Java's
 * {@link java.util.ServiceLoader ServiceLoader} mechanism.
 *
 * @since 1.0
 * @see LauncherDiscoveryRequest
 * @see TestPlan
 * @see TestExecutionListener
 * @see org.junit.platform.launcher.core.LauncherFactory
 * @see org.junit.platform.engine.TestEngine
 */
@API(status = STABLE, since = "1.0")
public interface Launcher {

	/**
	 * Register one or more listeners for test discovery.
	 *
	 * @param listeners the listeners to be notified of test discovery events;
	 * never {@code null} or empty
	 */
	@API(status = STABLE, since = "1.10")
	void registerLauncherDiscoveryListeners(LauncherDiscoveryListener... listeners);

	/**
	 * Register one or more listeners for test execution.
	 *
	 * @param listeners the listeners to be notified of test execution events;
	 * never {@code null} or empty
	 */
	void registerTestExecutionListeners(TestExecutionListener... listeners);

	/**
	 * Discover tests and build a {@link TestPlan} according to the supplied
	 * {@link LauncherDiscoveryRequest} by querying all registered engines and
	 * collecting their results.
	 *
	 * @apiNote This method may be called to generate a preview of the test
	 * tree. The resulting {@link TestPlan} is unmodifiable and may be passed to
	 * {@link #execute(TestPlan, TestExecutionListener...)} for execution at
	 * most once.
	 *
	 * @param launcherDiscoveryRequest the launcher discovery request; never
	 * {@code null}
	 * @return an unmodifiable {@code TestPlan} that contains all resolved
	 * {@linkplain TestIdentifier identifiers} from all registered engines
	 */
	TestPlan discover(LauncherDiscoveryRequest launcherDiscoveryRequest);

	/**
	 * Execute a {@link TestPlan} which is built according to the supplied
	 * {@link LauncherDiscoveryRequest} by querying all registered engines and
	 * collecting their results, and notify
	 * {@linkplain #registerTestExecutionListeners registered listeners} about
	 * the progress and results of the execution.
	 *
	 * <p>Supplied test execution listeners are registered in addition to already
	 * registered listeners but only for the supplied launcher discovery request.
	 *
	 * @apiNote Calling this method will cause test discovery to be executed for
	 * all registered engines. If the same {@link LauncherDiscoveryRequest} was
	 * previously passed to {@link #discover(LauncherDiscoveryRequest)}, you
	 * should instead call {@link #execute(TestPlan, TestExecutionListener...)}
	 * and pass the already acquired {@link TestPlan} to avoid the potential
	 * performance degradation (e.g., classpath scanning) of running test
	 * discovery twice.
	 *
	 * @param launcherDiscoveryRequest the launcher discovery request; never {@code null}
	 * @param listeners additional test execution listeners; never {@code null}
	 * @deprecated Please use {@link #execute(LauncherExecutionRequest)} instead.
	 */
	@Deprecated
	@API(status = DEPRECATED, since = "6.0")
	default void execute(LauncherDiscoveryRequest launcherDiscoveryRequest, TestExecutionListener... listeners) {
		var executionRequest = LauncherExecutionRequestBuilder.request(launcherDiscoveryRequest) //
				.listeners(listeners) //
				.build();
		execute(executionRequest);
	}

	/**
	 * Execute the supplied {@link TestPlan} and notify
	 * {@linkplain #registerTestExecutionListeners registered listeners} about
	 * the progress and results of the execution.
	 *
	 * <p>Supplied test execution listeners are registered in addition to
	 * already registered listeners but only for the execution of the supplied
	 * test plan.
	 *
	 * @apiNote The supplied {@link TestPlan} must not have been executed
	 * previously.
	 *
	 * @param testPlan the test plan to execute; never {@code null}
	 * @param listeners additional test execution listeners; never {@code null}
	 * @since 1.4
	 * @deprecated Please use {@link #execute(LauncherExecutionRequest)} instead.
	 */
	@Deprecated
	@API(status = DEPRECATED, since = "6.0")
	default void execute(TestPlan testPlan, TestExecutionListener... listeners) {
		var executionRequest = LauncherExecutionRequestBuilder.request(testPlan) //
				.listeners(listeners) //
				.build();
		execute(executionRequest);
	}

	/**
	 * Execute tests according to the supplied {@link LauncherExecutionRequest}
	 * {@linkplain #registerTestExecutionListeners registered listeners} about
	 * the progress and results of the execution.
	 *
	 * <p>Test execution listeners supplied
	 * {@linkplain LauncherExecutionRequest#getAdditionalTestExecutionListeners()
	 * as part of the request} are registered in addition to already registered
	 * listeners but only for the supplied execution request.
	 *
	 * @apiNote If the execution request contains a {@link TestPlan} rather than
	 * a {@link LauncherDiscoveryRequest}, it must not have been executed
	 * previously.
	 *
	 * <p>If the execution request contains a {@link LauncherDiscoveryRequest},
	 * calling this method will cause test discovery to be executed for all
	 * registered engines. If the same {@link LauncherDiscoveryRequest} was
	 * previously passed to {@link #discover(LauncherDiscoveryRequest)}, you
	 * should instead provide the resulting {@link TestPlan} as part of the
	 * supplied execution request to avoid the potential performance degradation
	 * (e.g., classpath scanning) of running test discovery twice.
	 *
	 * @param launcherExecutionRequest the launcher execution request; never
	 * {@code null}
	 * @since 6.0
	 */
	@API(status = MAINTAINED, since = "6.0")
	void execute(LauncherExecutionRequest launcherExecutionRequest);

}
