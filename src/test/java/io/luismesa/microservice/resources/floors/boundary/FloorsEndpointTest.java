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
package io.luismesa.microservice.resources.floors.boundary;

import io.luismesa.microservice.resources.floors.control.FloorsFacade;
import io.luismesa.microservice.resources.floors.entity.FloorsEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import jakarta.enterprise.inject.Default;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author Luis Daniel Mesa Velásquez {@literal <admin@luismesa.io>}
 */
@ExtendWith(MockitoExtension.class)
public class FloorsEndpointTest {

    @Mock
    @Default
    FloorsFacade facade;

    @InjectMocks
    FloorsEndpoint endpoint;

    public FloorsEndpointTest() {
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
     * Test of create method, of class FloorsEndpoint.
     */
    @Test
    @DisplayName("Test create(...) on FloorsEndpoint")
    public void testCreate() {
        String id = UUID.randomUUID().toString();

        FloorsEntity entity = new FloorsEntity();
        entity.setId(id);
        entity.setNumber("NUMBER");
        entity.setVersion(0L);

        endpoint.create(entity);

        ArgumentCaptor<FloorsEntity> insertCaptor = ArgumentCaptor.forClass(FloorsEntity.class);
        //            ArgumentCaptor<String> authorizationTokenCaptor = ArgumentCaptor.forClass(String.class);
        verify(facade, times(1)).create(insertCaptor.capture());
    }

    /**
     * Test of edit method, of class FloorsEndpoint.
     */
    @Test
    @DisplayName("Test edit(...) on FloorsEndpoint")
    public void testEdit() {
        FloorsEntity entity = new FloorsEntity();
        entity.setNumber("NUMBER");
        entity.setVersion(0L);
        String id = UUID.randomUUID().toString();

        endpoint.edit(id, entity);

        assertEquals(id, entity.getId());
        ArgumentCaptor<FloorsEntity> updateCaptor = ArgumentCaptor.forClass(FloorsEntity.class);
        verify(facade, times(1)).edit(updateCaptor.capture());
    }

    /**
     * Test of remove method, of class FloorsEndpoint.
     */
    @Test
    @DisplayName("Test remove(...) on FloorsEndpoint")
    public void testRemove() {
        String id = UUID.randomUUID().toString();
        FloorsEntity entity = new FloorsEntity();
        entity.setId(id);
        entity.setNumber("NUMBER");
        entity.setVersion(0L);
        doReturn(entity).when(facade).find(id);

        endpoint.remove(id);

        ArgumentCaptor<FloorsEntity> deleteCaptor = ArgumentCaptor.forClass(FloorsEntity.class);
        verify(facade, times(1)).remove(deleteCaptor.capture());
    }

    /**
     * Test of find method, of class FloorsEndpoint.
     */
    @Test
    @DisplayName("Test find(...) on FloorsEndpoint")
    public void testFind() {
        String id = UUID.randomUUID().toString();
        FloorsEntity entity = new FloorsEntity();
        entity.setId(id);
        entity.setNumber("NUMBER");
        entity.setVersion(0L);
        doReturn(entity).when(facade).find(id);

        FloorsEntity actual = endpoint.find(id);

        assertEquals(entity, actual);

        ArgumentCaptor<String> selectCaptor = ArgumentCaptor.forClass(String.class);
        verify(facade, times(1)).find(selectCaptor.capture());
    }

    /**
     * Test of findAll method, of class FloorsEndpoint.
     */
    @Test
    @DisplayName("Test findAll(...) on FloorsEndpoint")
    public void testFindAll() {
        List<FloorsEntity> entities = IntStream.range(0, 3)
                .mapToObj($ -> UUID.randomUUID().toString())
                .map(id -> {
                    FloorsEntity entity = new FloorsEntity();
                    entity.setId(id);
                    entity.setNumber("NUMBER");
                    entity.setVersion(0L);
                    return entity;
                })
                .collect(Collectors.toList());
        doReturn(entities).when(facade).findAll();

        List<FloorsEntity> actual = endpoint.findAll();

        assertIterableEquals(entities, actual);
        verify(facade, times(1)).findAll();
    }

}
