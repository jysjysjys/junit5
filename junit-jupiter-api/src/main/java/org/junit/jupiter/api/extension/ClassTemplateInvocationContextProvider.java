/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.api.extension;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

import java.util.stream.Stream;

import org.apiguardian.api.API;
import org.junit.jupiter.api.ClassTemplate;

/**
 * {@code ClassTemplateInvocationContextProvider} defines the API for
 * {@link Extension Extensions} that wish to provide one or multiple contexts
 * for the invocation of a {@link ClassTemplate @ClassTemplate}.
 *
 * <p>This extension point makes it possible to execute a class template in
 * different contexts &mdash; for example, with different parameters, by
 * preparing the test class instance differently, or multiple times without
 * modifying the context.
 *
 * <p>This interface defines two main methods:
 * {@link #supportsClassTemplate} and
 * {@link #provideClassTemplateInvocationContexts}. The former is called by the
 * framework to determine whether this extension wants to act on a container
 * template that is about to be executed. If so, the latter is called and must
 * return a {@link Stream} of {@link ClassTemplateInvocationContext} instances.
 * Otherwise, this provider is ignored for the execution of the current class
 * template.
 *
 * <p>A provider that has returned {@code true} from its
 * {@link #supportsClassTemplate} method is called <em>active</em>. When
 * multiple providers are active for a class template, the
 * {@code Streams} returned by their
 * {@link #provideClassTemplateInvocationContexts} methods will be chained, and
 * the class template method will be invoked using the contexts of all active
 * providers.
 *
 * <p>An active provider may return zero invocation contexts from its
 * {@link #provideClassTemplateInvocationContexts} method if it overrides
 * {@link #mayReturnZeroClassTemplateInvocationContexts} to return
 * {@code true}.
 *
 * <h2>Constructor Requirements</h2>
 *
 * <p>Consult the documentation in {@link Extension} for details on
 * constructor requirements.
 *
 * @since 5.13
 * @see ClassTemplate
 * @see ClassTemplateInvocationContext
 */
@API(status = EXPERIMENTAL, since = "6.0")
public interface ClassTemplateInvocationContextProvider extends Extension {

	/**
	 * Determine if this provider supports providing invocation contexts for the
	 * class template represented by the supplied {@code context}.
	 *
	 * @param context the extension context for the class template
	 * about to be invoked; never {@code null}
	 * @return {@code true} if this provider can provide invocation contexts
	 * @see #provideClassTemplateInvocationContexts
	 * @see ExtensionContext
	 */
	boolean supportsClassTemplate(ExtensionContext context);

	/**
	 * Provide {@linkplain ClassTemplateInvocationContext invocation contexts}
	 * for the class template represented by the supplied {@code context}.
	 *
	 * <p>This method is only called by the framework if
	 * {@link #supportsClassTemplate} previously returned {@code true} for the
	 * same {@link ExtensionContext}; this method is allowed to return an empty
	 * {@code Stream} but not {@code null}.
	 *
	 * <p>The returned {@code Stream} will be properly closed by calling
	 * {@link Stream#close()}, making it safe to use a resource such as
	 * {@link java.nio.file.Files#lines(java.nio.file.Path) Files.lines()}.
	 *
	 * @param context the extension context for the class template about to be
	 * invoked; never {@code null}
	 * @return a {@code Stream} of {@code ClassTemplateInvocationContext}
	 * instances for the invocation of the class template; never {@code null}
	 * @throws TemplateInvocationValidationException if a validation fails when
	 * while providing or closing the {@link java.util.stream.Stream}.
	 * @see #supportsClassTemplate
	 * @see ExtensionContext
	 */
	Stream<? extends ClassTemplateInvocationContext> provideClassTemplateInvocationContexts(ExtensionContext context);

	/**
	 * Signal that this provider may provide zero
	 * {@linkplain ClassTemplateInvocationContext invocation contexts} for
	 * the class template represented by the supplied {@code context}.
	 *
	 * <p>If this method returns {@code false} (which is the default) and the
	 * provider returns an empty stream from
	 * {@link #provideClassTemplateInvocationContexts}, this will be considered
	 * an execution error. Override this method to return {@code true} to ignore
	 * the absence of invocation contexts for this provider.
	 *
	 * @param context the extension context for the class template
	 * about to be invoked; never {@code null}
	 * @return {@code true} to allow zero contexts, {@code false} to fail
	 * execution in case of zero contexts
	 */
	default boolean mayReturnZeroClassTemplateInvocationContexts(ExtensionContext context) {
		return false;
	}

}
