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
package com.birdstudio.mars.cluster.router.script;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.birdstudio.eirene.utils.Constants;
import com.birdstudio.eirene.utils.URL;
import com.birdstudio.eirene.utils.logger.Logger;
import com.birdstudio.eirene.utils.logger.LoggerFactory;
import com.birdstudio.mars.remoting.Invocation;
import com.birdstudio.mars.remoting.Invoker;
import com.birdstudio.mars.remoting.RpcContext;
import com.birdstudio.mars.remoting.RpcException;
import com.birdstudio.mars.cluster.Router;

/**
 * ScriptRouter
 * 
 * @author 
 */
public class ScriptRouter implements Router {

    private static final Logger logger = LoggerFactory.getLogger(ScriptRouter.class);
    
    private static final Map<String, ScriptEngine> engines = new ConcurrentHashMap<String, ScriptEngine>();
    
    private final ScriptEngine engine;

    private final int priority;

    private final String rule;

    private final URL url;

    public URL getUrl() {
        return url;
    }

    public ScriptRouter(URL url) {
        this.url = url;
        String type = url.getParameter(Constants.TYPE_KEY);
        this.priority = url.getParameter(Constants.PRIORITY_KEY, 0);
        String rule = url.getParameterAndDecoded(Constants.RULE_KEY);
        if (type == null || type.length() == 0){
            type = Constants.DEFAULT_SCRIPT_TYPE_KEY;
        }
        if (rule == null || rule.length() == 0){
            throw new IllegalStateException(new IllegalStateException("route rule can not be empty. rule:" + rule));
        }
        ScriptEngine engine = engines.get(type);
        if (engine == null){
            engine = new ScriptEngineManager().getEngineByName(type);
            if (engine == null) {
                throw new IllegalStateException(new IllegalStateException("Unsupported route rule type: " + type + ", rule: " + rule));
            }
            engines.put(type, engine);
        }
        this.engine = engine;
        this.rule = rule;
    }

    @SuppressWarnings("unchecked")
    public <T> List<Invoker<T>> route(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
        try {
            List<Invoker<T>> invokersCopy = new ArrayList<Invoker<T>>(invokers);
            Compilable compilable = (Compilable) engine;
            Bindings bindings = engine.createBindings();
            bindings.put("invokers", invokersCopy);
            bindings.put("invocation", invocation);
            bindings.put("context", RpcContext.getContext());
            CompiledScript function = compilable.compile(rule);
            Object obj = function.eval(bindings);
            if (obj instanceof Invoker[]) {
                invokersCopy = Arrays.asList((Invoker<T>[]) obj);
            } else if (obj instanceof Object[]) {
                invokersCopy = new ArrayList<Invoker<T>>();
                for (Object inv : (Object[]) obj) {
                    invokersCopy.add((Invoker<T>)inv);
                }
            } else {
                invokersCopy = (List<Invoker<T>>) obj;
            }
            return invokersCopy;
        } catch (ScriptException e) {
            //fail then ignore rule .invokers.
            logger.error("route error , rule has been ignored. rule: " + rule + ", method:" + invocation.getMethodName() + ", url: " + RpcContext.getContext().getUrl(), e);
            return invokers;
        }
    }

    public int compareTo(Router o) {
        if (o == null || o.getClass() != ScriptRouter.class) {
            return 1;
        }
        ScriptRouter c = (ScriptRouter) o;
        return this.priority == c.priority ? rule.compareTo(c.rule) : (this.priority > c.priority ? 1 : -1);
    }

}