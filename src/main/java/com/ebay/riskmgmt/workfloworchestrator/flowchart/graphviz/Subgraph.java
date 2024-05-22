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

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Subgraph implements FlowchartComponent {
    private final String id;
    private final Map<String, FlowchartComponent> components = new LinkedHashMap<>();
    private final Attributes attributes = new Attributes();
    private final Set<Edge> edges = new LinkedHashSet<>();
    private final String ctor;
    private FlowchartComponent target;

    protected Subgraph(String ctor, String id) {
        this.ctor = ctor;
        this.id = id;
    }

    public Subgraph(String id) {
        this("subgraph", "cluster_" + id);
    }

    public String id() {
        return id;
    }

    @Override
    public void addAttribute(String name, String value) {
        attributes.add(name, value);
    }

    public  FlowchartComponent add(FlowchartComponent component) {
        if (component instanceof Edge) {
            addEdge((Edge) component);
            return component;
        }

        if (components.containsKey(component.id())) {
            return components.get(component.id());
        }
        components.put(component.id(), component);
        return component;
    }

    public FlowchartComponent getComponent(String id) {
        return components.get(id);
    }

    private void addEdge(Edge edge) {
        edges.add(edge);
    }

    @Override
    public String interpret() {
        StringBuilder builder = new StringBuilder();
        builder.append(ctor).append(" ").append(id).append(" {\n");
        if (attributes.isNotEmpty()) {
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                builder.append(entry.getKey()).append(" = \"").append(entry.getValue()).append("\";\n");
            }
        }
        for (FlowchartComponent component : components.values()) {
            builder.append(component.interpret()).append("\n");
        }

        for (Edge edge : edges) {
            builder.append(edge.interpret()).append("\n");
        }

        builder.append("}");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subgraph subgraph = (Subgraph) o;
        return Objects.equals(id, subgraph.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public FlowchartComponent getTarget() {
        return target;
    }

    public void setTarget(FlowchartComponent target) {
        this.target = target;
    }
}
