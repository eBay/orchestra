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

public class Node implements FlowchartComponent {
    private final String id;
    private final Attributes attributes = new Attributes();

    public Node(String id) {
        this.id = id;
    }

    public void addAttribute(String name, String value) {
        attributes.add(name, value);
    }

    public String id() {
        return id;
    }

    @Override
    public String interpret() {
        return id + attributes + ";";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(id, node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
