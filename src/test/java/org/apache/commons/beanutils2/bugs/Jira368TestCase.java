/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.beanutils2.bugs;

import static org.junit.jupiter.api.Assertions.assertThrows;

import junit.framework.TestCase;

import org.apache.commons.beanutils2.BeanUtils;

/**
 * @see <a href="https://issues.apache.org/jira/browse/BEANUTILS-368">https://issues.apache.org/jira/browse/BEANUTILS-368</a>
 */
public class Jira368TestCase extends TestCase {

    /**
     * Create a test case with the specified name.
     *
     * @param name The name of the test
     */
    public Jira368TestCase(final String name) {
        super(name);
    }

    /**
     * Sets up.
     *
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Tear Down.
     *
     * @throws Exception
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test {@link BeanUtils} setProperty() with Null value
     */
    public void testBeanUtilsSetProperty_NullBean() throws Exception {
        assertThrows(NullPointerException.class, () -> BeanUtils.setProperty(null, "foo", "bar"));
    }
}
