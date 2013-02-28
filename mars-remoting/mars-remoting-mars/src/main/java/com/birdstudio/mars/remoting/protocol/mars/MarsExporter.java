/*
 * Copyright 1999-2011 Birdstudio Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.birdstudio.mars.remoting.protocol.mars;

import java.util.Map;

import com.birdstudio.mars.remoting.Exporter;
import com.birdstudio.mars.remoting.Invoker;
import com.birdstudio.mars.remoting.protocol.AbstractExporter;

/**
 * DubboExporter
 * 
 * @author william.liangf
 */
public class MarsExporter<T> extends AbstractExporter<T> {

    private final String                        key;

    private final Map<String, Exporter<?>> exporterMap;

    public MarsExporter(Invoker<T> invoker, String key, Map<String, Exporter<?>> exporterMap){
        super(invoker);
        this.key = key;
        this.exporterMap = exporterMap;
    }

    @Override
    public void unexport() {
        super.unexport();
        exporterMap.remove(key);
    }

}