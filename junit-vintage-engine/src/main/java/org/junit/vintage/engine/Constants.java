/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.vintage.engine;

import static org.apiguardian.api.API.Status.DEPRECATED;
import static org.apiguardian.api.API.Status.MAINTAINED;

import org.apiguardian.api.API;

/**
 * Collection of constants related to the {@link VintageTestEngine}.
 *
 * @deprecated Should only be used temporarily while migrating tests to JUnit
 * Jupiter or another testing framework with native JUnit Platform support
 */
@Deprecated(since = "6.0")
@API(status = DEPRECATED, since = "6.0")
public final class Constants {

	/**
	 * Indicates whether parallel execution is enabled for the JUnit Vintage engine.
	 *
	 * <p>Set this property to {@code true} to enable parallel execution of tests.
	 * Defaults to {@code false}.
	 *
	 * @since 5.12
	 */
	@API(status = MAINTAINED, since = "5.13.3")
	public static final String PARALLEL_EXECUTION_ENABLED = "junit.vintage.execution.parallel.enabled";

	/**
	 * Specifies the size of the thread pool to be used for parallel execution.
	 *
	 * <p>Set this property to an integer value to specify the number of threads
	 * to be used for parallel execution. Defaults to the number of available
	 * processors.
	 *
	 * @since 5.12
	 */
	@API(status = MAINTAINED, since = "5.13.3")
	public static final String PARALLEL_POOL_SIZE = "junit.vintage.execution.parallel.pool-size";

	/**
	 * Indicates whether parallel execution is enabled for test classes in the
	 * JUnit Vintage engine.
	 *
	 * <p>Set this property to {@code true} to enable parallel execution of test
	 * classes. Defaults to {@code false}.
	 *
	 * @since 5.12
	 */
	@API(status = MAINTAINED, since = "5.13.3")
	public static final String PARALLEL_CLASS_EXECUTION = "junit.vintage.execution.parallel.classes";

	/**
	 * Indicates whether parallel execution is enabled for test methods in the
	 * JUnit Vintage engine.
	 *
	 * <p>Set this property to {@code true} to enable parallel execution of test
	 * methods. Defaults to {@code false}.
	 *
	 * @since 5.12
	 */
	@API(status = MAINTAINED, since = "5.13.3")
	public static final String PARALLEL_METHOD_EXECUTION = "junit.vintage.execution.parallel.methods";

	private Constants() {
		/* no-op */
	}

}
