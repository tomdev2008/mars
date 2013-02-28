/**
 * Project: mars-rpc
 * 
 * File Created at 2012-2-24
 * $Id$
 * 
 * Copyright 1999-2100 Birdstudio.com Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Birdstudio Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Birdstudio.com.
 */
package com.birdstudio.mars.remoting.support;

import com.birdstudio.mars.remoting.Exporter;
import com.birdstudio.mars.remoting.Invoker;

/**
 * DelegateExporter
 * @author chao.liuc
 *
 */
public class DelegateExporter<T> implements Exporter<T> {
    
    private final Exporter<T> exporter;
    
    public DelegateExporter(Exporter<T> exporter) {
        if (exporter == null) {
            throw new IllegalArgumentException("exporter can not be null");
        } else {
            this.exporter = exporter;
        }
        
    }
    
    public Invoker<T> getInvoker() {
        return exporter.getInvoker();
    }
    public void unexport() {
        exporter.unexport();
    }
}
