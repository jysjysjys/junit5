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

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.platform.commons.util.Preconditions;

class ArgumentCountValidator {

	private static final Logger logger = LoggerFactory.getLogger(ArgumentCountValidator.class);

	static final String ARGUMENT_COUNT_VALIDATION_KEY = "junit.jupiter.params.argumentCountValidation";
	private static final Namespace NAMESPACE = Namespace.create(ArgumentCountValidator.class);

	private final ParameterizedDeclarationContext<?> declarationContext;
	private final EvaluatedArgumentSet arguments;

	ArgumentCountValidator(ParameterizedDeclarationContext<?> declarationContext, EvaluatedArgumentSet arguments) {
		this.declarationContext = declarationContext;
		this.arguments = arguments;
	}

	void validate(ExtensionContext extensionContext) {
		ArgumentCountValidationMode argumentCountValidationMode = getArgumentCountValidationMode(extensionContext);
		switch (argumentCountValidationMode) {
			case DEFAULT, NONE -> {
			}
			case STRICT -> {
				int consumedCount = this.declarationContext.getResolverFacade().determineConsumedArgumentCount(
					this.arguments);
				int totalCount = this.arguments.getTotalLength();
				Preconditions.condition(consumedCount == totalCount,
					() -> "Configuration error: @%s consumes %s %s but there %s %s %s provided.%nNote: the provided arguments were %s".formatted(
						this.declarationContext.getAnnotationName(), consumedCount,
						pluralize(consumedCount, "parameter", "parameters"), pluralize(totalCount, "was", "were"),
						totalCount, pluralize(totalCount, "argument", "arguments"),
						Arrays.toString(this.arguments.getAllPayloads())));
			}
			default -> throw new ExtensionConfigurationException(
				"Unsupported argument count validation mode: " + argumentCountValidationMode);
		}
	}

	private ArgumentCountValidationMode getArgumentCountValidationMode(ExtensionContext extensionContext) {
		ArgumentCountValidationMode mode = declarationContext.getArgumentCountValidationMode();
		if (mode != ArgumentCountValidationMode.DEFAULT) {
			return mode;
		}
		else {
			return getArgumentCountValidationModeConfiguration(extensionContext);
		}
	}

	private ArgumentCountValidationMode getArgumentCountValidationModeConfiguration(ExtensionContext extensionContext) {
		String key = ARGUMENT_COUNT_VALIDATION_KEY;
		ArgumentCountValidationMode fallback = ArgumentCountValidationMode.NONE;
		ExtensionContext.Store store = getStore(extensionContext);
		return requireNonNull(store.getOrComputeIfAbsent(key, __ -> {
			Optional<String> optionalConfigValue = extensionContext.getConfigurationParameter(key);
			if (optionalConfigValue.isPresent()) {
				String configValue = optionalConfigValue.get();
				Optional<ArgumentCountValidationMode> enumValue = Arrays.stream(
					ArgumentCountValidationMode.values()).filter(
						mode -> mode.name().equalsIgnoreCase(configValue)).findFirst();
				if (enumValue.isPresent()) {
					logger.config(
						() -> "Using ArgumentCountValidationMode '%s' set via the '%s' configuration parameter.".formatted(
							enumValue.get().name(), key));
					return enumValue.get();
				}
				else {
					logger.warn(() -> String.format(
						"Invalid ArgumentCountValidationMode '%s' set via the '%s' configuration parameter. "
								+ "Falling back to the %s default value.",
						configValue, key, fallback.name()));
					return fallback;
				}
			}
			else {
				return fallback;
			}
		}, ArgumentCountValidationMode.class));
	}

	private static String pluralize(int count, String singular, String plural) {
		return count == 1 ? singular : plural;
	}

	private ExtensionContext.Store getStore(ExtensionContext context) {
		return context.getRoot().getStore(NAMESPACE);
	}
}
