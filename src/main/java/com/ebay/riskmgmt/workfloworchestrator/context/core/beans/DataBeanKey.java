/*
 * Copyright 2024 eBay Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ebay.riskmgmt.workfloworchestrator.context.core.beans;
/*
 * Created by pishrivastava on 2019-05-02 - 17:27
 *
 * @author pishrivastava
 * @since 1.0
 * @version 1.0
 */

import java.util.Objects;

public class DataBeanKey {

    private Class<? extends DataBean> dataBeanClass;
    private Object primaryKey;

    public DataBeanKey(Class<? extends DataBean> dataBeanClass, Object primaryKey) {
        this.dataBeanClass = dataBeanClass;
        this.primaryKey = primaryKey;
    }

    public Class<? extends DataBean> getDataBeanClass() {
        return dataBeanClass;
    }

    public Object getPrimaryKey() {
        return primaryKey;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The {@code equals} method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     * {@code x}, {@code x.equals(x)} should return
     * {@code true}.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     * {@code x} and {@code y}, {@code x.equals(y)}
     * should return {@code true} if and only if
     * {@code y.equals(x)} returns {@code true}.
     * <li>It is <i>transitive</i>: for any non-null reference values
     * {@code x}, {@code y}, and {@code z}, if
     * {@code x.equals(y)} returns {@code true} and
     * {@code y.equals(z)} returns {@code true}, then
     * {@code x.equals(z)} should return {@code true}.
     * <li>It is <i>consistent</i>: for any non-null reference values
     * {@code x} and {@code y}, multiple invocations of
     * {@code x.equals(y)} consistently return {@code true}
     * or consistently return {@code false}, provided no
     * information used in {@code equals} comparisons on the
     * objects is modified.
     * <li>For any non-null reference value {@code x},
     * {@code x.equals(null)} should return {@code false}.
     * </ul>
     * <p>
     * The {@code equals} method for class {@code Object} implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values {@code x} and
     * {@code y}, this method returns {@code true} if and only
     * if {@code x} and {@code y} refer to the same object
     * ({@code x == y} has the value {@code true}).
     * <p>
     * Note that it is generally necessary to override the {@code hashCode}
     * method whenever this method is overridden, so as to maintain the
     * general contract for the {@code hashCode} method, which states
     * that equal objects must have equal hash codes.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @see #hashCode()
     * See HashMap
     */
    @Override
    public boolean equals(Object obj) {

        if (super.equals(obj))
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        try {
            DataBeanKey beanKey = (DataBeanKey) obj;
            if (dataBeanClass == null && beanKey.dataBeanClass != null) {
                return false;
            } else {
                assert dataBeanClass != null;
                if (!dataBeanClass.equals(beanKey.dataBeanClass))
                    return false;
            }

            if (primaryKey == null && beanKey.primaryKey != null) {
                return false;
            } else {
                assert primaryKey != null;
                if (!primaryKey.equals(beanKey.primaryKey))
                    return false;
            }
        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hash tables such as those provided by
     * {HashMap}.
     * <p>
     * The general contract of {@code hashCode} is:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during
     * an execution of a Java application, the {@code hashCode} method
     * must consistently return the same integer, provided no information
     * used in {@code equals} comparisons on the object is modified.
     * This integer need not remain consistent from one execution of an
     * application to another execution of the same application.
     * <li>If two objects are equal according to the {@code equals(Object)}
     * method, then calling the {@code hashCode} method on each of
     * the two objects must produce the same integer result.
     * <li>It is <em>not</em> required that if two objects are unequal
     * according to the {@link Object#equals(Object)}
     * method, then calling the {@code hashCode} method on each of the
     * two objects must produce distinct integer results.  However, the
     * programmer should be aware that producing distinct integer results
     * for unequal objects may improve the performance of hash tables.
     * </ul>
     * <p>
     * As much as is reasonably practical, the hashCode method defined by
     * class {@code Object} does return distinct integers for distinct
     * objects. (This is typically implemented by converting the internal
     * address of the object into an integer, but this implementation
     * technique is not required by the
     * Java<font size="-2"><sup>TM</sup></font> programming language.)
     *
     * @return a hash code value for this object.
     * @see Object#equals(Object)
     * @see System#identityHashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(dataBeanClass, primaryKey);
    }

    @Override
    public String toString() {
        return "DataBeanKey{" +
                "dataBeanClass=" + dataBeanClass +
                ", primaryKey=" + (primaryKey != null ? primaryKey.toString() : "") +
                '}';
    }
}
