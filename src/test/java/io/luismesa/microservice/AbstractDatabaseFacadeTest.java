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

import jakarta.persistence.EntityManager;
import java.util.UUID;
import java.util.function.Function;

/**
 *
 * @author Luis Daniel Mesa Velásquez {@literal <admin@luismesa.io>}
 */
public class AbstractDatabaseFacadeTest extends AbstractDatabaseFacadeTestTemplate<Identifiable> {

    AbstractDatabaseFacade<Identifiable> facade = new AbstractDatabaseFacade<Identifiable>(Identifiable.class) {
        @Override
        public EntityManager getEntityManager() {
            return mockEntityManager;
        }
    };

    public AbstractDatabaseFacadeTest() {
        super(Identifiable.class);
    }

    @Override
    public Function<String, Identifiable> fromGenerator() {
        return id -> new Identifiable() {

            String entityId = id;

            @Override
            public String getId() {
                return entityId;
            }

            @Override
            public void setId(String entityId) {
                this.entityId = entityId;
            }

            @Override
            public String toString() {
                return "io.luismesa.halcyon.oracle.reference.service.mono.Identifiable[ id=" + this.getId() + " ]";
            }
        };
    }

    @Override
    public AbstractDatabaseFacade<Identifiable> getAbstractDatabaseFacade() {
        return facade;
    }

    @Override
    public Identifiable getEntity() {
        return fromGenerator().apply(UUID.randomUUID().toString());
    }

}
