/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.platform.launcher.core;

import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;
import static org.apiguardian.api.API.Status.INTERNAL;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;
import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.DiscoveryIssue;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.engine.reporting.OutputDirectoryProvider;

/**
 * Represents the result of test discovery of the configured
 * {@linkplain TestEngine test engines}.
 *
 * @since 1.7
 */
@API(status = INTERNAL, since = "1.7", consumers = { "org.junit.platform.testkit", "org.junit.platform.suite.engine" })
public class LauncherDiscoveryResult {

	private final Map<TestEngine, EngineResultInfo> testEngineResults;
	private final ConfigurationParameters configurationParameters;
	private final OutputDirectoryProvider outputDirectoryProvider;

	LauncherDiscoveryResult(Map<TestEngine, EngineResultInfo> testEngineResults,
			ConfigurationParameters configurationParameters, OutputDirectoryProvider outputDirectoryProvider) {
		this.testEngineResults = unmodifiableMap(new LinkedHashMap<>(testEngineResults));
		this.configurationParameters = configurationParameters;
		this.outputDirectoryProvider = outputDirectoryProvider;
	}

	public TestDescriptor getEngineTestDescriptor(TestEngine testEngine) {
		return getEngineResult(testEngine).getRootDescriptor();
	}

	@API(status = INTERNAL, since = "1.13")
	public List<DiscoveryIssue> getDiscoveryIssues(TestEngine testEngine) {
		return getEngineResult(testEngine).getDiscoveryIssueNotifier().getAllIssues();
	}

	EngineResultInfo getEngineResult(TestEngine testEngine) {
		return requireNonNull(this.testEngineResults.get(testEngine));
	}

	ConfigurationParameters getConfigurationParameters() {
		return this.configurationParameters;
	}

	OutputDirectoryProvider getOutputDirectoryProvider() {
		return this.outputDirectoryProvider;
	}

	public Collection<TestEngine> getTestEngines() {
		return this.testEngineResults.keySet();
	}

	boolean containsCriticalIssuesOrContainsTests() {
		return this.testEngineResults.values().stream() //
				.anyMatch(EngineResultInfo::containsCriticalIssuesOrContainsTests);
	}

	Collection<TestDescriptor> getEngineTestDescriptors() {
		return this.testEngineResults.values().stream() //
				.map(EngineResultInfo::getRootDescriptor) //
				.toList();
	}

	public LauncherDiscoveryResult withRetainedEngines(Predicate<? super TestDescriptor> predicate) {
		Map<TestEngine, EngineResultInfo> prunedTestEngineResults = retainEngines(predicate);
		if (prunedTestEngineResults.size() < this.testEngineResults.size()) {
			return new LauncherDiscoveryResult(prunedTestEngineResults, this.configurationParameters,
				this.outputDirectoryProvider);
		}
		return this;
	}

	private Map<TestEngine, EngineResultInfo> retainEngines(Predicate<? super TestDescriptor> predicate) {
		// @formatter:off
		return this.testEngineResults.entrySet()
				.stream()
				.filter(entry -> predicate.test(entry.getValue().getRootDescriptor()))
				.collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
		// @formatter:on
	}

	static class EngineResultInfo {

		static EngineResultInfo completed(TestDescriptor rootDescriptor,
				DiscoveryIssueNotifier discoveryIssueNotifier) {
			return new EngineResultInfo(rootDescriptor, discoveryIssueNotifier, null);
		}

		static EngineResultInfo errored(TestDescriptor rootDescriptor, DiscoveryIssueNotifier discoveryIssueNotifier,
				Throwable cause) {
			return new EngineResultInfo(rootDescriptor, discoveryIssueNotifier, cause);
		}

		private final TestDescriptor rootDescriptor;

		@Nullable
		private final Throwable cause;

		private final DiscoveryIssueNotifier discoveryIssueNotifier;

		EngineResultInfo(TestDescriptor rootDescriptor, DiscoveryIssueNotifier discoveryIssueNotifier,
				@Nullable Throwable cause) {
			this.rootDescriptor = rootDescriptor;
			this.discoveryIssueNotifier = discoveryIssueNotifier;
			this.cause = cause;
		}

		TestDescriptor getRootDescriptor() {
			return this.rootDescriptor;
		}

		DiscoveryIssueNotifier getDiscoveryIssueNotifier() {
			return discoveryIssueNotifier;
		}

		Optional<Throwable> getCause() {
			return Optional.ofNullable(this.cause);
		}

		boolean containsCriticalIssuesOrContainsTests() {
			return cause != null //
					|| discoveryIssueNotifier.hasCriticalIssues() //
					|| TestDescriptor.containsTests(rootDescriptor);
		}
	}

}
