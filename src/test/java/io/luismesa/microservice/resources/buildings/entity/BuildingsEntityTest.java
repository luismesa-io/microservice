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
package io.luismesa.microservice.resources.buildings.entity;

import io.luismesa.microservice.VersionableTestGist;
import io.luismesa.microservice.resources.floors.entity.FloorsEntity;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author Luis Daniel Mesa Velásquez {@literal <admin@luismesa.io>}
 */
@ExtendWith(MockitoExtension.class)
public class BuildingsEntityTest extends VersionableTestGist<BuildingsEntity> {

    String name = "NAME";
    String address = "ADDRESS";

    @Mock
    FloorsEntity firstMember;
    @Mock
    FloorsEntity secondMember;

    Set<FloorsEntity> floors;

    public BuildingsEntityTest() {
        super(BuildingsEntity.class);
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        floors = Set.of(firstMember, secondMember);
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getName and setName method, of class BuildingsEntity.
     */
    @Test
    @DisplayName("testName")
    public void testName() {
        System.out.println("setName");
        BuildingsEntity instance = new BuildingsEntity();
        String actual = instance.getName();

        assertNull(actual);

        instance.setName(name);
        actual = instance.getName();

        assertEquals(name, actual);
    }

    /**
     * Test of getAddress and setAddress method, of class BuildingsEntity.
     */
    @Test
    @DisplayName("testAddress")
    public void testAddress() {
        System.out.println("setName");
        BuildingsEntity instance = new BuildingsEntity();
        String actual = instance.getAddress();

        assertNull(actual);

        instance.setAddress(address);
        actual = instance.getAddress();

        assertEquals(address, actual);
    }

    /**
     * Test of getFloors and setFloors methods, of class BuildingsEntity.
     */
    @Test
    @DisplayName("testFloors")
    public void testFloors() {
        BuildingsEntity instance = new BuildingsEntity();
        Set<FloorsEntity> actual = instance.getFloors();

        assertNotNull(actual);
        assertTrue(actual.isEmpty());

        instance.setFloors(floors);
        actual = instance.getFloors();

        assertIterableEquals(floors, actual);

    }

}
