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
package io.luismesa.microservice.resources.floors.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.luismesa.microservice.IdentifiableVersionedEntity;
import io.luismesa.microservice.resources.desks.entity.DesksEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Basic;
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
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.UuidGenerator;

/**
 *
 * @author Luis Daniel Mesa Velásquez {@literal <admin@luismesa.io>}
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@AttributeOverride(name = "id", column = @Column(name = "FLOOR_ID"))
@Table(schema = "MICROSERVICE", name = "FLOORS", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"FLOOR_NUMBER"})})
@NamedQueries({
    @NamedQuery(name = "Floors.findAll", query = "SELECT l FROM FloorsEntity l"),
    @NamedQuery(name = "Floors.findById", query = "SELECT l FROM FloorsEntity l WHERE l.id = :id"),
    @NamedQuery(name = "Floors.findByNumber", query = "SELECT l FROM FloorsEntity l WHERE l.number = :number")})
@UuidGenerator(name = "FLOORS_ID_GEN")
@Cache(type = CacheType.SOFT_WEAK,
        size = 200,
        expiry = 60000
)
public class FloorsEntity extends IdentifiableVersionedEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Column(name = "FLOOR_NUMBER", nullable = false, length = 255)
    private String number;

    @JoinTable(
            schema = "MICROSERVICE",
            name = "FLOOR_DESKS",
            joinColumns = @JoinColumn(name = "FLOOR_ID"),
            inverseJoinColumns = @JoinColumn(name = "DESK_ID"))
    @JsonSerialize(as = HashSet.class)
    private Set<DesksEntity> desks;

    public FloorsEntity() {
    }

    public FloorsEntity(String id) {
        super(id);
    }

    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return the desks
     */
    public Set<DesksEntity> getDesks() {
        if (desks == null) {
            this.desks = new HashSet<>();
        }
        return desks;
    }

    /**
     * @param desks the desks to set
     */
    public void setDesks(Set<DesksEntity> desks) {
        this.desks = desks;
    }
}
