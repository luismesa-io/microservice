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

import static org.junit.jupiter.api.Assertions.*;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Luis Daniel Mesa Velásquez {@literal <admin@luismesa.io>}
 * @param <I>
 */
public class IdentifiableEntityTestGist<I extends IdentifiableEntity> extends IdentifiableTestGist<I> {

    public IdentifiableEntityTestGist(Class<I> clazz) {
        super(clazz);
    }

    /**
     * Test of hashCode method, of class IdentifiableEntity.
     */
    @Test
    @DisplayName("testHashCode")
    public void testHashCode() {
        try {
            String string = clazz.getCanonicalName() + "[ id=" + id + " ]";
            int expected = string.hashCode();

            int actual = clazz.getConstructor(String.class).newInstance(id).hashCode();

            assertEquals(expected, actual);
        } catch (NoSuchMethodException
                | SecurityException
                | InstantiationException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException ex) {
            Logger.getLogger(IdentifiableEntityTestGist.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex);
        }
    }

    /**
     * Test of equals method, of class IdentifiableEntity.
     */
    @Test
    @DisplayName("testEquals")
    public void testEquals() {
        try {
            Constructor<I> constructor = clazz.getConstructor(String.class);
            I instance = constructor.newInstance(id);
            I nullInstance = null;
            Serializable otherType = new Serializable() {
            };
            I otherInstance = constructor.newInstance(UUID.randomUUID().toString());
            I otherInstanceSameId = constructor.newInstance(id);

            assertTrue(instance.equals(instance));
            assertFalse(instance.equals(nullInstance));
            assertFalse(instance.equals(otherType));
            assertFalse(instance.equals(otherInstance));
            assertTrue(instance.equals(otherInstanceSameId));
        } catch (NoSuchMethodException
                | SecurityException
                | InstantiationException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException ex) {
            Logger.getLogger(IdentifiableEntityTestGist.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex);
        }
    }

    /**
     * Test of toString method, of class IdentifiableEntity.
     */
    @Test
    @DisplayName("testToString")
    public void testToString() {
        try {
            String expected = clazz.getCanonicalName() + "[ id=" + id + " ]";

            String actual = clazz.getConstructor(String.class).newInstance(id).toString();

            assertEquals(expected, actual);
        } catch (NoSuchMethodException
                | SecurityException
                | InstantiationException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException ex) {
            Logger.getLogger(IdentifiableEntityTestGist.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex);
        }
    }
}
