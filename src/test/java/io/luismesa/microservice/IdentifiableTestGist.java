/*
 * The MIT License
 *
 * Copyright 2023 Luis Daniel Mesa Velásquez.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.luismesa.microservice;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

/**
 *
 * @author Luis Daniel Mesa Velásquez {@literal <admin@luismesa.io>}
 * @param <I>
 */
public abstract class IdentifiableTestGist<I extends Identifiable> {

    protected Class<I> clazz;

    protected String id = UUID.randomUUID().toString();

    public IdentifiableTestGist(Class<I> clazz) {
        this.clazz = clazz;
    }

    /**
     * Test of constructors, of classes that implement Identifiable.
     */
    @Test
    @DisplayName("testConstructors")
    public void testConstructors() {
        try {
            Constructor<I> constructor = clazz.getConstructor();
            I instance = constructor.newInstance();
            assertNotNull(instance);

            constructor = clazz.getConstructor(String.class);
            instance = constructor.newInstance(id);
            assertNotNull(instance);
        } catch (NoSuchMethodException
                | SecurityException
                | InstantiationException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException ex) {
            Logger.getLogger(IdentifiableTestGist.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex);
        }
    }

    /**
     * Test of getId and setId methods, of classes that implement Identifiable.
     */
    @Test
    @DisplayName("testId")
    public void testId() {
        try {
            Constructor<I> constructor = clazz.getConstructor();
            I instance = constructor.newInstance();
            assertNull(instance.getId());
            instance.setId(id);
            assertEquals(id, instance.getId());

            constructor = clazz.getConstructor(String.class);
            instance = constructor.newInstance(id);
            assertEquals(id, instance.getId());
        } catch (NoSuchMethodException
                | SecurityException
                | InstantiationException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException ex) {
            Logger.getLogger(IdentifiableTestGist.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex);
        }
    }
}
