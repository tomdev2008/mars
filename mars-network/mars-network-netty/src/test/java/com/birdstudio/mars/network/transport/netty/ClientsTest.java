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
package com.birdstudio.mars.network.transport.netty;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.junit.Test;

import com.birdstudio.eirene.utils.extension.ExtensionLoader;
import com.birdstudio.mars.network.Transporter;
import com.birdstudio.mars.network.transport.netty.NettyTransporter;

/**
 * @author ding.lid
 */
public class ClientsTest {

    @Test
    public void testGetTransportEmpty() {
        try {
            ExtensionLoader.getExtensionLoader(Transporter.class).getExtension("");
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected.getMessage(), containsString("Extension name == null"));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTransportNull() {
        String name = null;
        ExtensionLoader.getExtensionLoader(Transporter.class).getExtension(name);
    }

    @Test
    public void testGetTransport3() {
        String name = "netty";
        assertEquals(NettyTransporter.class, ExtensionLoader.getExtensionLoader(Transporter.class).getExtension(name).getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetTransportWrong() {
        String name = "nety";
        assertNull(ExtensionLoader.getExtensionLoader(Transporter.class).getExtension(name).getClass());
    }
}