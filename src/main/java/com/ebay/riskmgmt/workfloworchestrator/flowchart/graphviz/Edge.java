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

package com.ebay.riskmgmt.workfloworchestrator.flowchart.graphviz;


import com.ebay.riskmgmt.workfloworchestrator.flowchart.FlowchartComponent;

import java.util.Objects;

public class Edge implements FlowchartComponent {
    private final Attributes attributes = new Attributes();
    private final String id;
    private final FlowchartComponent source;
    private final FlowchartComponent target;

    public Edge(FlowchartComponent source, FlowchartComponent target) {
        this.source = source;
        this.target = target;
        String edgeop = "->";
        this.id = String.format("\"%s\" %s \"%s\"", source.id(), edgeop, target.id());
    }

    @Override
    public String id() {
        return id;
    }

    public void addAttribute(String name, String value) {
        attributes.add(name, value);
    }

    @Override
    public String interpret() {
        return String.format("%s %s;", id, attributes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(source, edge.source) &&
                Objects.equals(target, edge.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
}
