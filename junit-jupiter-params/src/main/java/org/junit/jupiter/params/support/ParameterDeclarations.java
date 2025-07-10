/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.params.support;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Optional;

import org.apiguardian.api.API;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;

/**
 * {@code ParameterDeclarations} encapsulates the combined <em>declarations</em>
 * of all <em>indexed</em> {@code @ParameterizedClass} or
 * {@code @ParameterizedTest} parameters.
 *
 * <p>For a {@code @ParameterizedTest}, the parameter declarations are derived
 * from the method signature. For a {@code @ParameterizedClass}, they may be
 * derived from the constructor or
 * {@link java.lang.reflect.Parameter @Parameter}-annotated fields.
 *
 * <p>Aggregators, that is parameters of type
 * {@link ArgumentsAccessor ArgumentsAccessor} or parameters annotated with
 * {@link org.junit.jupiter.params.aggregator.AggregateWith @AggregateWith}, are
 * <em>not</em> indexed and thus not included in the list of parameter
 * declarations.
 *
 * @since 5.13
 * @see ParameterDeclaration
 * @see org.junit.jupiter.params.ParameterizedClass
 * @see org.junit.jupiter.params.ParameterizedTest
 */
@API(status = EXPERIMENTAL, since = "6.0")
public interface ParameterDeclarations {

	/**
	 * {@return all <em>indexed</em> parameter declarations; never {@code null},
	 * sorted by index}
	 */
	List<ParameterDeclaration> getAll();

	/**
	 * {@return the first <em>indexed</em> parameter declaration, if available;
	 * never {@code null}}
	 */
	Optional<ParameterDeclaration> getFirst();

	/**
	 * {@return the <em>indexed</em> parameter declaration for the supplied
	 * index, if available; never {@code null}}
	 */
	Optional<ParameterDeclaration> get(int parameterIndex);

	/**
	 * {@return the source element of all parameter declarations}
	 *
	 * <p>For {@code @ParameterizedTest}, this always corresponds to the
	 * parameterized test method. For {@code @ParameterizedClass}, this
	 * corresponds to the parameterized test class constructor, if constructor
	 * injection is used; or the test class itself, if field injection is used.
	 */
	AnnotatedElement getSourceElement();

	/**
	 * {@return a human-readable description of the source element}
	 *
	 * <p>This may, for example, be used in error messages.
	 *
	 * @see #getSourceElement()
	 */
	String getSourceElementDescription();

}
