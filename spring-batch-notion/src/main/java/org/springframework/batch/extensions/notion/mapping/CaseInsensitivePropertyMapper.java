/*
 * Copyright 2024-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.batch.extensions.notion.mapping;

import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.Map;

/**
 * @author Stefano Cordio
 */
abstract class CaseInsensitivePropertyMapper<T> implements PropertyMapper<T> {

	@Override
	public T map(Map<String, String> properties) {
		LinkedCaseInsensitiveMap<String> caseInsensitiveProperties = new LinkedCaseInsensitiveMap<>(properties.size());
		caseInsensitiveProperties.putAll(properties);
		return mapCaseInsensitive(caseInsensitiveProperties);
	}

	abstract T mapCaseInsensitive(LinkedCaseInsensitiveMap<String> properties);

}
