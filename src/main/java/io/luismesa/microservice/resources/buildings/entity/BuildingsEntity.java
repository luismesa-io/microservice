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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.luismesa.microservice.IdentifiableVersionedEntity;
import io.luismesa.microservice.resources.floors.entity.FloorsEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Basic;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;

/**
 *
 * @author Luis Daniel Mesa Velásquez {@literal <admin@luismesa.io>}
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@AttributeOverride(name = "id", column = @Column(name = "BUILDING_ID"))
@Table(schema = "MICROSERVICE", name = "BUILDINGS", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"BUILDING_NAME"})})
@NamedQueries({
    @NamedQuery(name = "Buildings.findAll", query = "SELECT l FROM BuildingsEntity l"),
    @NamedQuery(name = "Buildings.findById", query = "SELECT l FROM BuildingsEntity l WHERE l.id = :id"),
    @NamedQuery(name = "Buildings.findByName", query = "SELECT l FROM BuildingsEntity l WHERE l.name = :name"),
    @NamedQuery(name = "Buildings.findByAddress", query = "SELECT l FROM BuildingsEntity l WHERE l.address = :address")})
@Cache(type = CacheType.SOFT_WEAK,
        size = 200,
        expiry = 60000
)
public class BuildingsEntity extends IdentifiableVersionedEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "BUILDING_NAME")
    private String name;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "BUILDING_ADDRESS")
    private String address;

    @JoinTable(
            schema = "MICROSERVICE",
            name = "BUILDING_FLOORS",
            joinColumns = @JoinColumn(name = "BUILDING_ID"),
            inverseJoinColumns = @JoinColumn(name = "FLOOR_ID"))
    @JsonSerialize(as = HashSet.class)
    private Set<FloorsEntity> floors;

    public BuildingsEntity() {
    }

    public BuildingsEntity(String id) {
        super(id);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the floors
     */
    public Set<FloorsEntity> getFloors() {
        if (floors == null) {
            this.floors = new HashSet<>();
        }
        return floors;
    }

    /**
     * @param floors the floors to set
     */
    public void setFloors(Set<FloorsEntity> floors) {
        this.floors = floors;
    }

}
