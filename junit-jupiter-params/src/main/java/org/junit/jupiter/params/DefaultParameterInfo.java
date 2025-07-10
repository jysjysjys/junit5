/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.params;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.support.ParameterDeclarations;
import org.junit.jupiter.params.support.ParameterInfo;

/**
 * @since 5.13
 */
record DefaultParameterInfo(ParameterDeclarations declarations, ArgumentsAccessor arguments) implements ParameterInfo {

	@Override
	public ParameterDeclarations getDeclarations() {
		return this.declarations;
	}

	@Override
	public ArgumentsAccessor getArguments() {
		return this.arguments;
	}

	void store(ExtensionContext context) {
		context.getStore(NAMESPACE).put(KEY, this);
	}
}
