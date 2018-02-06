/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.params.provider;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apiguardian.api.API;

/**
 * {@code @MethodSource} is an {@link ArgumentsSource} which provides access
 * to values returned by {@linkplain #value() methods} of the class in
 * which this annotation is declared.
 *
 * <p>By default such methods must be {@code static} unless the
 * {@link org.junit.jupiter.api.TestInstance.Lifecycle#PER_CLASS PER_CLASS}
 * test instance lifecycle mode is used for the test class.
 *
 * <p>The values returned by such methods will be provided as arguments to the
 * annotated {@code @ParameterizedTest} method.
 *
 * @since 5.0
 * @see org.junit.jupiter.params.provider.ArgumentsSource
 * @see org.junit.jupiter.params.ParameterizedTest
 * @see org.junit.jupiter.api.TestInstance
 */
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(status = EXPERIMENTAL, since = "5.0")
@ArgumentsSource(MethodArgumentsProvider.class)
public @interface MethodSource {

	/**
	 * The names of the test class methods to use as sources for arguments;
	 * leave empty if the source method has the same name as the test method.
	 */
	String[] value() default "";

}
