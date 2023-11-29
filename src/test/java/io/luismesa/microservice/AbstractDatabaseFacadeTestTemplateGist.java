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

import io.smallrye.config.inject.ConfigExtension;
import jakarta.enterprise.inject.spi.Extension;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;

/**
 *
 * @author Luis Daniel Mesa Velásquez {@literal <admin@luismesa.io>}
 * @param <I>
 * @param <F>
 */
@EnableWeld
public abstract class AbstractDatabaseFacadeTestTemplateGist<I extends Identifiable, F extends AbstractDatabaseFacade<I>>
        extends AbstractDatabaseFacadeTestTemplate<I> {

    @SuppressWarnings("unchecked") // Cannot put @SafeVarargs in the addExtensions method of Weld
    protected Class<? extends Extension>[] extensions
            = (Class<? extends Extension>[]) new Class<?>[]{ConfigExtension.class};

    protected Class<F> facadeClass;

    @WeldSetup
    public WeldInitiator weld;

    protected I entity = fromGenerator().apply(UUID.randomUUID().toString());

    public AbstractDatabaseFacadeTestTemplateGist(Class<I> entityClass, Class<F> facadeClass) {
        super(entityClass);
        this.facadeClass = facadeClass;
        buildWeld();
    }

    private void buildWeld() {
        weld = WeldInitiator.from(
                WeldInitiator.createWeld().addExtensions(extensions).addBeanClasses(facadeClass))
                .setPersistenceContextFactory((ip) -> mockEntityManager)
                .inject(this)
                .build();
    }

    @Override
    public Function<String, I> fromGenerator() {
        return id -> {
            I instance = null;
            try {
                Constructor<I> constructor = entityClass.getConstructor(String.class);
                instance = enhanced(constructor.newInstance(id));
            } catch (NoSuchMethodException
                    | SecurityException
                    | InstantiationException
                    | IllegalAccessException
                    | IllegalArgumentException
                    | InvocationTargetException ex) {
                Logger.getLogger(AbstractDatabaseFacadeTestTemplateGist.class.getName()).log(Level.SEVERE, null, ex);
            }
            return instance;
        };
    }

    public abstract I enhanced(I generated);

    @Override
    public I getEntity() {
        return entity;
    }

}
