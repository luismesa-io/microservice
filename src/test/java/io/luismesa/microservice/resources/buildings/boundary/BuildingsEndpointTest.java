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
package io.luismesa.microservice.resources.buildings.boundary;

import io.luismesa.microservice.resources.buildings.control.BuildingsFacade;
import io.luismesa.microservice.resources.buildings.entity.BuildingsEntity;
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
public class BuildingsEndpointTest {

    @Mock
    @Default
    BuildingsFacade facade;

    @InjectMocks
    BuildingsEndpoint endpoint;

    public BuildingsEndpointTest() {
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
     * Test of create method, of class BuildingsEndpoint.
     */
    @Test
    @DisplayName("Test create(...) on BuildingsEndpoint")
    public void testCreate() {
        String id = UUID.randomUUID().toString();

        BuildingsEntity entity = new BuildingsEntity();
        entity.setId(id);
        entity.setName("NAME");
        entity.setAddress("ADDRESS");
        entity.setVersion(0L);

        endpoint.create(entity);

        ArgumentCaptor<BuildingsEntity> insertCaptor = ArgumentCaptor.forClass(BuildingsEntity.class);
        //            ArgumentCaptor<String> authorizationTokenCaptor = ArgumentCaptor.forClass(String.class);
        verify(facade, times(1)).create(insertCaptor.capture());
    }

    /**
     * Test of edit method, of class BuildingsEndpoint.
     */
    @Test
    @DisplayName("Test edit(...) on BuildingsEndpoint")
    public void testEdit() {
        BuildingsEntity entity = new BuildingsEntity();
        entity.setName("NAME");
        entity.setAddress("ADDRESS");
        entity.setVersion(0L);
        String id = UUID.randomUUID().toString();

        endpoint.edit(id, entity);

        assertEquals(id, entity.getId());
        ArgumentCaptor<BuildingsEntity> updateCaptor = ArgumentCaptor.forClass(BuildingsEntity.class);
        verify(facade, times(1)).edit(updateCaptor.capture());
    }

    /**
     * Test of remove method, of class BuildingsEndpoint.
     */
    @Test
    @DisplayName("Test remove(...) on BuildingsEndpoint")
    public void testRemove() {
        String id = UUID.randomUUID().toString();
        BuildingsEntity entity = new BuildingsEntity();
        entity.setId(id);
        entity.setName("NAME");
        entity.setAddress("ADDRESS");
        entity.setVersion(0L);
        doReturn(entity).when(facade).find(id);

        endpoint.remove(id);

        ArgumentCaptor<BuildingsEntity> deleteCaptor = ArgumentCaptor.forClass(BuildingsEntity.class);
        verify(facade, times(1)).remove(deleteCaptor.capture());
    }

    /**
     * Test of find method, of class BuildingsEndpoint.
     */
    @Test
    @DisplayName("Test find(...) on BuildingsEndpoint")
    public void testFind() {
        String id = UUID.randomUUID().toString();
        BuildingsEntity entity = new BuildingsEntity();
        entity.setId(id);
        entity.setName("NAME");
        entity.setAddress("ADDRESS");
        entity.setVersion(0L);
        doReturn(entity).when(facade).find(id);

        BuildingsEntity actual = endpoint.find(id);

        assertEquals(entity, actual);

        ArgumentCaptor<String> selectCaptor = ArgumentCaptor.forClass(String.class);
        verify(facade, times(1)).find(selectCaptor.capture());
    }

    /**
     * Test of findAll method, of class BuildingsEndpoint.
     */
    @Test
    @DisplayName("Test findAll(...) on BuildingsEndpoint")
    public void testFindAll() {
        List<BuildingsEntity> entities = IntStream.range(0, 3)
                .mapToObj($ -> UUID.randomUUID().toString())
                .map(id -> {
                    BuildingsEntity entity = new BuildingsEntity();
                    entity.setId(id);
                    entity.setName("NAME");
                    entity.setAddress("ADDRESS");
                    entity.setVersion(0L);
                    return entity;
                })
                .collect(Collectors.toList());
        doReturn(entities).when(facade).findAll();

        List<BuildingsEntity> actual = endpoint.findAll();

        assertIterableEquals(entities, actual);
        verify(facade, times(1)).findAll();
    }

}
