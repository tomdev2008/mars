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
package com.birdstudio.mars.config.spring.annotation.provider;

import com.birdstudio.mars.config.annotation.Service;
import com.birdstudio.mars.config.spring.api.Box;
import com.birdstudio.mars.config.spring.api.DemoService;

/**
 * DemoServiceImpl
 * 
 * @author 
 */
@Service(version = "1.2")
public class AnnotationServiceImpl implements DemoService {
    
    public String sayName(String name) {
        return "annotation:" + name;
    }
    
    public Box getBox() {
        return null;
    }
    
}