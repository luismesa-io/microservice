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

import io.luismesa.microservice.Compress;
import io.luismesa.microservice.resources.buildings.control.BuildingsFacade;
import io.luismesa.microservice.resources.buildings.entity.BuildingsEntity;
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
@Path("buildings")
public class BuildingsEndpoint {

    @Inject
    BuildingsFacade buildingsFacade;

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(BuildingsEntity entity) {
        buildingsFacade.create(entity);
    }

    @PUT
    @CacheRemove(cacheName = "buildings")
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@CacheKey @PathParam("id") String id, BuildingsEntity entity) {
        entity.setId(id);
        buildingsFacade.edit(entity);
    }

    @DELETE
    @CacheRemove(cacheName = "buildings")
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void remove(@CacheKey @PathParam("id") String id) {
        buildingsFacade.remove(find(id));
    }

    @GET
    @Compress
    @CacheResult(cacheName = "buildings")
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public BuildingsEntity find(@CacheKey @PathParam("id") String id) {
        BuildingsEntity building = buildingsFacade.find(id);
        Optional.ofNullable(building)
                .map(BuildingsEntity::getFloors)
                .map(Collection::stream)
                .orElse(Stream.empty())
                .map(FloorsEntity::getDesks)
                .forEach(Collection::size);
        return building;
    }

    @GET
    @Compress
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<BuildingsEntity> findAll() {
        return buildingsFacade.findAll();
    }

}
