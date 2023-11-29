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

import io.luismesa.microservice.Compress;
import io.luismesa.microservice.resources.desks.entity.DesksEntity;
import io.luismesa.microservice.resources.floors.control.FloorsFacade;
import io.luismesa.microservice.resources.floors.entity.FloorsEntity;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;

/**
 *
 * @author Luis Daniel Mesa Velásquez {@literal <admin@luismesa.io>}
 */
@Stateless
@Dependent
@Path("floors")
public class FloorsEndpoint {

    @Inject
    FloorsFacade floorsFacade;

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(FloorsEntity entity) {
        floorsFacade.create(entity);
    }

    @PUT
    @CacheRemove
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@CacheKey @PathParam("id") String id, FloorsEntity entity) {
        entity.setId(id);
        floorsFacade.edit(entity);
    }

    @DELETE
    @CacheRemove
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void remove(@CacheKey @PathParam("id") String id) {
        floorsFacade.remove(find(id));
    }

    @GET
    @Compress
    @CacheResult
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public FloorsEntity find(@CacheKey @PathParam("id") String id) {
        FloorsEntity floors = floorsFacade.find(id);
        Optional.ofNullable(floors)
                .map(FloorsEntity::getDesks)
                .map(Collection::stream)
                .orElse(Stream.empty())
                .forEach(DesksEntity::getId);
        return floors;
    }

    @GET
    @Compress
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<FloorsEntity> findAll() {
        return floorsFacade.findAll();
    }

}
