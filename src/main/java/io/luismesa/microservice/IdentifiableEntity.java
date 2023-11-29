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

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 *
 * @author Luis Daniel Mesa Velásquez {@literal <admin@luismesa.io>}
 */
@MappedSuperclass
public class IdentifiableEntity implements Identifiable, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 36)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, length = 36)
    private String id;

    public IdentifiableEntity() {
    }

    public IdentifiableEntity(String id) {
        this.id = Optional.ofNullable(id)
                .map(String::toLowerCase)
                .orElse(null);
    }

    @Override
    public String getId() {
        return Optional.ofNullable(this.id)
                .map(String::toLowerCase)
                .orElse(null);
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += this.toString().hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        return object != null
                && (this == object
                || (this.getClass().isAssignableFrom(object.getClass())
                && Objects.equals(
                        this.getId(),
                        this.getClass().cast(object).getId())));
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ id=" + this.getId() + " ]";
    }
}
