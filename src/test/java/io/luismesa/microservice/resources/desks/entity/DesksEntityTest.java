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
package io.luismesa.microservice.resources.desks.entity;

import io.luismesa.microservice.VersionableTestGist;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

/**
 *
 * @author Luis Daniel Mesa Velásquez {@literal <admin@luismesa.io>}
 */
public class DesksEntityTest extends VersionableTestGist<DesksEntity> {

    String number = "NUMBER";
    Boolean available = Boolean.FALSE;

    public DesksEntityTest() {
        super(DesksEntity.class);
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getNumber and setNumber method, of class DesksEntity.
     */
    @Test
    @DisplayName("testNumber")
    public void testNumber() {
        DesksEntity instance = new DesksEntity();
        String actual = instance.getNumber();

        assertNull(actual);

        instance.setNumber(number);
        actual = instance.getNumber();

        assertEquals(number, actual);
    }

    /**
     * Test of getAvailable and setAvailable method, of class DesksEntity.
     */
    @Test
    @DisplayName("testAvailable")
    public void testAvailable() {
        DesksEntity instance = new DesksEntity();
        Boolean actual = instance.getAvailable();

        assertTrue(actual);

        instance.setAvailable(available);
        actual = instance.getAvailable();

        assertEquals(available, actual);
    }

}
