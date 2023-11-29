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
package io.luismesa.microservice.resources.desks.entity;

import io.luismesa.microservice.IdentifiableVersionedEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Optional;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.UuidGenerator;

/**
 *
 * @author Luis Daniel Mesa Velásquez {@literal <admin@luismesa.io>}
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@AttributeOverride(name = "id", column = @Column(name = "DESK_ID"))
@Table(schema = "MICROSERVICE", name = "DESKS", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"DESK_NAME"})})
@NamedQueries({
    @NamedQuery(name = "Desks.findAll", query = "SELECT l FROM DesksEntity l"),
    @NamedQuery(name = "Desks.findById", query = "SELECT l FROM DesksEntity l WHERE l.id = :id"),
    @NamedQuery(name = "Desks.findByNumber", query = "SELECT l FROM DesksEntity l WHERE l.number = :number")})
@UuidGenerator(name = "DESKS_ID_GEN")
@Cache(type = CacheType.SOFT_WEAK,
        size = 200,
        expiry = 60000
)
public class DesksEntity extends IdentifiableVersionedEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "DESK_NUMBER")
    private String number;

    @Basic(optional = false)
    @NotNull
    @Column(name = "AVAILABLE")
    private Boolean available = Boolean.TRUE;

    public DesksEntity() {
    }

    public DesksEntity(String id) {
        super(id);
    }

    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the name to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return the available
     */
    public Boolean getAvailable() {
        return available;
    }

    /**
     * @param available the available to set
     */
    public void setAvailable(Boolean available) {
        this.available = Optional.ofNullable(available).orElse(Boolean.TRUE);
    }

}
