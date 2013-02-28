/*
 * Copyright 1999-2012 Birdstudio Group.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.birdstudio.mars.cluster.merger;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.birdstudio.eirene.utils.ReflectUtils;
import com.birdstudio.eirene.utils.extension.ExtensionLoader;
import com.birdstudio.mars.cluster.Merger;

/**
 * @author <a href="mailto:gang.lvg@birdstudio-inc.com">kimi</a>
 */
public class MergerFactory {

	private static final ConcurrentMap<Class<?>, Merger<?>> mergerCache = new ConcurrentHashMap<Class<?>, Merger<?>>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Merger<T> getMerger(Class<T> returnType) {
		Merger result;
		if (returnType.isArray()) {
			Class type = returnType.getComponentType();
			result = mergerCache.get(type);
			if (result == null) {
				loadMergers();
				result = mergerCache.get(type);
			}
			if (result == null && !type.isPrimitive()) {
				result = ArrayMerger.INSTANCE;
			}
		} else {
			result = mergerCache.get(returnType);
			if (result == null) {
				loadMergers();
				result = mergerCache.get(returnType);
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	static void loadMergers() {
		Set<String> names = ExtensionLoader.getExtensionLoader(Merger.class)
				.getSupportedExtensions();
		for (String name : names) {
			Merger m = ExtensionLoader.getExtensionLoader(Merger.class)
					.getExtension(name);
			mergerCache.putIfAbsent(ReflectUtils.getGenericClass(m.getClass()),
					m);
		}
	}

}
