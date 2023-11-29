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
package io.luismesa.microservice.resources.buildings.control;

import io.luismesa.microservice.*;
import io.luismesa.microservice.resources.buildings.entity.BuildingsEntity;
import jakarta.inject.Inject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Luis Daniel Mesa Velásquez {@literal <admin@luismesa.io>}
 */
public class BuildingsFacadeTest extends AbstractDatabaseFacadeTestTemplateGist<BuildingsEntity, BuildingsFacade> {

    @Inject
    BuildingsFacade facade;

    public BuildingsFacadeTest() {
        super(BuildingsEntity.class, BuildingsFacade.class);
    }

    /**
     * Test of getEntityManager method, of class AbstractDatabaseFacade.
     */
    @Test
    public void testGetEntityManager() {
        assertEquals(
                mockEntityManager, getAbstractDatabaseFacade().getEntityManager(), "Should return the injected entityManager");
    }

    @Override
    public AbstractDatabaseFacade<BuildingsEntity> getAbstractDatabaseFacade() {
        return facade;
    }

    @Override
    public BuildingsEntity enhanced(BuildingsEntity generated) {
        generated.setName("NAME");
        generated.setAddress("ADDRESS");
        generated.setVersion(0L);
        return generated;
    }

}
