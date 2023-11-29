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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

/**
 *
 * @author Luis Daniel Mesa Velásquez {@literal <admin@luismesa.io>}
 * @param <I>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public abstract class AbstractDatabaseFacadeTestTemplate<I extends Identifiable> {

    protected Class<I> entityClass;

    @Mock
    protected EntityManager mockEntityManager;

    @Mock
    protected CriteriaBuilder mockCriteriaBuilder;

    @Mock
    protected EntityGraph<?> mockEntityGraph;

    @Mock
    protected CriteriaQuery<I> mockCriteriaQuery;

    @Mock
    protected CriteriaQuery<Long> mockCriteriaQueryLong;

    @Mock
    protected Root<I> mockRoot;

    @Mock
    protected Expression<Long> mockExpressionLong;

    @Mock
    protected TypedQuery<I> mockTypedQuery;

    @Mock
    protected TypedQuery<Long> mockTypedQueryLong;

    public AbstractDatabaseFacadeTestTemplate(Class<I> entityClass) {
        this.entityClass = entityClass;
    }

    public Class<I> getEntityClass() {
        return entityClass;
    }

    /**
     * Test of getEntityManager method, of class AbstractDatabaseFacade.
     */
    @Test
    public void testGetEntityManager() {
        assertEquals(
                mockEntityManager, getAbstractDatabaseFacade().getEntityManager(), "Should return the injected entityManager");
    }

    /**
     * Test of create method, of class AbstractDatabaseFacade.
     */
    @Test
    @DisplayName("Test create(...) on AbstractDatabaseFacade")
    public void testCreate() {
        I entity = getEntity();

        getAbstractDatabaseFacade().create(entity);

        ArgumentCaptor<I> entityManageArgumentCaptor = ArgumentCaptor.forClass(getEntityClass());
        verify(mockEntityManager, times(1)).persist(entityManageArgumentCaptor.capture());
        I capturedValue = entityManageArgumentCaptor.getValue();
        System.out.println("capturedValue = " + capturedValue);
        System.out.println("entity = " + entity);
        assertEquals(capturedValue, entity, "Should call persist on the entity manager with the corresponding entity");
    }

    /**
     * Test of edit method, of class AbstractDatabaseFacade.
     */
    @Test
    @DisplayName("Test edit(...) on AbstractDatabaseFacade")
    public void testEdit() {
        I entity = getEntity();
        getAbstractDatabaseFacade().edit(entity);

        ArgumentCaptor<I> entityManageArgumentCaptor = ArgumentCaptor.forClass(getEntityClass());
        verify(mockEntityManager, times(1)).merge(entityManageArgumentCaptor.capture());
        I capturedValue = entityManageArgumentCaptor.getValue();
        System.out.println("capturedValue = " + capturedValue);
        System.out.println("entity = " + entity);
        assertEquals(capturedValue, entity, "Should call merge on the entity manager with the corresponding entity");
    }

    /**
     * Test of remove method, of class AbstractDatabaseFacade.
     */
    @Test
    @DisplayName("Test remove(...) on AbstractDatabaseFacade")
    public void testDelete() {
        I entity = getEntity();
        doReturn(entity).when(mockEntityManager).merge(any());
        getAbstractDatabaseFacade().remove(entity);

        ArgumentCaptor<I> entityManageArgumentCaptor = ArgumentCaptor.forClass(getEntityClass());
        verify(mockEntityManager, times(1)).merge(entityManageArgumentCaptor.capture());
        verify(mockEntityManager, times(1)).remove(entityManageArgumentCaptor.capture());
        I capturedValue = entityManageArgumentCaptor.getValue();
        System.out.println("capturedValue = " + capturedValue);
        System.out.println("entity = " + entity);
        assertEquals(
                capturedValue,
                entity,
                "Should call remove with the result of calling merge"
                + " on the entity manager with the corresponding entity");
    }

    /**
     * Test of find method, of class AbstractDatabaseFacade.
     */
    @Test
    @DisplayName("Test find(...) on AbstractDatabaseFacade")
    public void testFind() {
        I entity = getEntity();
        String id = entity.getId();
        doReturn(entity).when(mockEntityManager).find(eq(getEntityClass()), eq(entity.getId()));

        I actual = getAbstractDatabaseFacade().find(id);

        ArgumentCaptor<String> entityManageArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockEntityManager, times(1)).find(eq(getEntityClass()), entityManageArgumentCaptor.capture());
        String capturedValue = entityManageArgumentCaptor.getValue();
        System.out.println("capturedValue = " + capturedValue);
        System.out.println("entity = " + entity);
        assertEquals(
                capturedValue, id, "Should call find on the entity manager with the class of the entity and the id");
        assertEquals(entity, actual, "Should return the entity that corresponds with the given id");
    }

    /**
     * Test of selectAll method, of class AbstractDatabaseFacade.
     */
    @Test
    @DisplayName("Test findAll(...) on AbstractDatabaseFacade")
    public void testFindAll() {
        List<I> entities = IntStream.range(0, 3)
                .mapToObj($ -> UUID.randomUUID().toString())
                .map(fromGenerator())
                .collect(Collectors.toList());
        doReturn(mockCriteriaBuilder).when(mockEntityManager).getCriteriaBuilder();
        doReturn(mockCriteriaQuery).when(mockCriteriaBuilder).createQuery(eq(getEntityClass()));
        doReturn(mockRoot).when(mockCriteriaQuery).from(eq(getEntityClass()));
        //        doReturn(mockCriteriaQuery).when(mockCriteriaQuery).select(eq(mockRoot));
        doReturn(mockTypedQuery).when(mockEntityManager).createQuery(eq(mockCriteriaQuery));
        doReturn(entities).when(mockTypedQuery).getResultList();

        List<I> actual = getAbstractDatabaseFacade().findAll();

        verify(mockEntityManager, times(1)).getCriteriaBuilder();
        verify(mockCriteriaBuilder, times(1)).createQuery(eq(getEntityClass()));
        verify(mockCriteriaQuery, times(1)).from(eq(getEntityClass()));
        verify(mockCriteriaQuery).select(eq(mockRoot));
        verify(mockEntityManager, times(1)).createQuery(eq(mockCriteriaQuery));
        verify(mockTypedQuery).getResultList();
        assertIterableEquals(entities, actual, "Should return all entitites");
    }

    /**
     * Test of findRange method, of class AbstractDatabaseFacade.
     */
    @Test
    @DisplayName("Test findRange(...) on AbstractDatabaseFacade")
    public void testFindRange() {
        List<I> entities = IntStream.range(0, 3)
                .mapToObj($ -> UUID.randomUUID().toString())
                .map(fromGenerator())
                .collect(Collectors.toList());
        doReturn(mockCriteriaBuilder).when(mockEntityManager).getCriteriaBuilder();
        doReturn(mockCriteriaQuery).when(mockCriteriaBuilder).createQuery(eq(getEntityClass()));
        doReturn(mockRoot).when(mockCriteriaQuery).from(eq(getEntityClass()));
//        doReturn(mockCriteriaQuery).when(mockCriteriaQuery).select(eq(mockRoot));
        doReturn(mockTypedQuery).when(mockEntityManager).createQuery(eq(mockCriteriaQuery));
        doReturn(entities).when(mockTypedQuery).getResultList();

        List<I> actual = getAbstractDatabaseFacade().findRange(new int[]{0, 2});

        verify(mockEntityManager, times(1)).getCriteriaBuilder();
        verify(mockCriteriaBuilder, times(1)).createQuery(eq(getEntityClass()));
        verify(mockCriteriaQuery, times(1)).from(eq(getEntityClass()));
        verify(mockCriteriaQuery, times(1)).select(eq(mockRoot));
        verify(mockEntityManager, times(1)).createQuery(eq(mockCriteriaQuery));
        verify(mockTypedQuery, times(1)).setMaxResults(3);
        verify(mockTypedQuery, times(1)).setFirstResult(0);
        verify(mockTypedQuery, times(1)).getResultList();
        assertIterableEquals(entities, actual, "Should return all entitites");
    }

    /**
     * Test of findRange method, of class AbstractDatabaseFacade.
     */
    @Test
    @DisplayName("Test count(...) on AbstractDatabaseFacade")
    public void testCount() {
//        List<I> entities = IntStream.range(0, 3)
//                .mapToObj($ -> UUID.randomUUID().toString())
//                .map(fromGenerator())
//                .collect(Collectors.toList());
        doReturn(mockCriteriaBuilder).when(mockEntityManager).getCriteriaBuilder();
        doReturn(mockCriteriaQueryLong).when(mockCriteriaBuilder).createQuery(eq(Long.class));
        doReturn(mockRoot).when(mockCriteriaQueryLong).from(eq(getEntityClass()));
        doReturn(mockExpressionLong).when(mockCriteriaBuilder).count(eq(mockRoot));
//        doReturn(mockCriteriaQueryLong).when(mockCriteriaQuery).select(eq(mockExpressionLong));
        doReturn(mockTypedQueryLong).when(mockEntityManager).createQuery(eq(mockCriteriaQueryLong));
        doReturn(3L).when(mockTypedQueryLong).getSingleResult();

        int actual = getAbstractDatabaseFacade().count();

        verify(mockEntityManager, times(2)).getCriteriaBuilder();
        verify(mockCriteriaBuilder, times(1)).<Long>createQuery(eq(Long.class));
        verify(mockCriteriaQueryLong, times(1)).from(eq(getEntityClass()));
        verify(mockCriteriaBuilder, times(1)).count(eq(mockRoot));
        verify(mockCriteriaQueryLong, times(1)).select(eq(mockExpressionLong));
        verify(mockEntityManager, times(1)).createQuery(eq(mockCriteriaQueryLong));
        verify(mockTypedQueryLong, times(1)).getSingleResult();
        assertTrue(3 == actual, "Should return the number of entities");
    }

    public abstract Function<String, I> fromGenerator();

    public abstract AbstractDatabaseFacade<I> getAbstractDatabaseFacade();

    public abstract I getEntity();
}
