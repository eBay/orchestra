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
import com.ebay.riskmgmt.workfloworchestrator.flowchart.FlowchartVisitor;
import com.ebay.riskmgmt.workfloworchestrator.task.Task;
import com.ebay.riskmgmt.workfloworchestrator.workflow.BranchFlow;
import com.ebay.riskmgmt.workfloworchestrator.workflow.ConditionalFlow;
import com.ebay.riskmgmt.workfloworchestrator.workflow.ParallelFlow;
import com.ebay.riskmgmt.workfloworchestrator.workflow.RepeatFlow;
import com.ebay.riskmgmt.workfloworchestrator.workflow.SequentialFlow;
import com.ebay.riskmgmt.workfloworchestrator.workflow.Workflow;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GraphvizFlowchartVisitor implements FlowchartVisitor {
    private final Digraph rootGraph;
    private final AtomicInteger counter = new AtomicInteger(0);

    public GraphvizFlowchartVisitor(Digraph rootGraph) {
        this.rootGraph = rootGraph;
    }

    @Override
    public Node visitTask(Task task) {
        Node node = new Node("task" + counter.getAndIncrement());
        node.addAttribute("label", task.getName());
        node.addAttribute("shape", "oval");
//        node.addAttribute("color", "red");
        return node;
    }

    @Override
    public FlowchartComponent visitConditionalFlow(ConditionalFlow workflow) {
        Task toExecute = workflow.getToExecute();
        Task successTask = workflow.getNextOnPredicateSuccess();
        Task failureTask = workflow.getNextOnPredicateFailure();
        Subgraph subgraph = new Subgraph(buildSubgraphId(workflow));
        subgraph.addAttribute("label", "conditional:" + workflow.getName());

        FlowchartComponent predicateNode = createNode(toExecute);
        predicateNode.addAttribute("shape", "oval");
        subgraph.setTarget(predicateNode);

        FlowchartComponent successNode = createNode(successTask);
        FlowchartComponent failureNode = createNode(failureTask);

        Edge successEdge = new Edge(predicateNode, successNode);
        successEdge.addAttribute("label", "Pass");
        subgraph.add(successEdge);
        Edge failureEdge = new Edge(predicateNode, failureNode);
        failureEdge.addAttribute("label", "Fail");
        subgraph.add(failureEdge);

        subgraph.add(predicateNode);
        subgraph.add(successNode);
        subgraph.add(failureNode);
        return subgraph;
    }

    @Override
    public FlowchartComponent visitSequentialFlow(SequentialFlow workflow) {
        Subgraph subgraph = new Subgraph(buildSubgraphId(workflow));
        List<FlowchartComponent> nodes = workflow.getWorks().stream().map(this::createNode).collect(Collectors.toList());
        subgraph.addAttribute("label", "Sequential:" + workflow.getName());
        subgraph.addAttribute("rank", "same");
        for (int i = 0; i < nodes.size() - 1; i++) {
            Edge edge = new Edge(nodes.get(i), nodes.get(i + 1));
            subgraph.add(nodes.get(i));
            subgraph.add(edge);
        }
        subgraph.setTarget(nodes.get(0));
        subgraph.add(nodes.get(nodes.size() - 1)); // add last node
        return subgraph;
    }

    @Override
    public FlowchartComponent visitParallelFlow(ParallelFlow workflow) {
        Subgraph subgraph = new Subgraph(buildSubgraphId(workflow));
        List<FlowchartComponent> nodes = workflow.getWorks().stream().map(this::createNode).collect(Collectors.toList());
        subgraph.addAttribute("label", "Parallel:" + workflow.getName());
        subgraph.addAttribute("rank", "same");
        for (int i = 0; i < nodes.size() - 1; i++) {
            Edge edge = new Edge(nodes.get(i), nodes.get(i + 1));
            edge.addAttribute("style", "invis");
            subgraph.add(edge);
            subgraph.add(nodes.get(i));
        }
        subgraph.setTarget(nodes.get(0));
        subgraph.add(nodes.get(nodes.size() - 1));
        return subgraph;
    }

    @Override
    public FlowchartComponent visitBranchFlow(BranchFlow workflow) {
        Subgraph subgraph = new Subgraph(buildSubgraphId(workflow));
        subgraph.addAttribute("label", "branch: " + workflow.getName());

        Task toExecute = workflow.getToExecute();
        FlowchartComponent executeNode = createNode(toExecute);
        subgraph.add(executeNode);
        subgraph.setTarget(executeNode);

        Map<String, Task> taskCasesMap = workflow.getTaskCasesMap();
        for (Map.Entry<String, Task> entry : taskCasesMap.entrySet()) {
            FlowchartComponent caseNode = createNode(entry.getValue());
            Edge edge = new Edge(executeNode, caseNode);
            edge.addAttribute("label", entry.getKey());
            subgraph.add(caseNode);
            subgraph.add(edge);
        }
        return subgraph;
    }

    @Override
    public FlowchartComponent visitRepeatFlow(RepeatFlow workflow) {
        Task task = workflow.getWork();

        Subgraph subgraph = new Subgraph(buildSubgraphId(workflow));
        subgraph.addAttribute("label", "repeat:" + workflow.getName());
        subgraph.addAttribute("rank", "same");

        FlowchartComponent node = createNode(task);
        subgraph.setTarget(node);
        subgraph.add(node);
        return subgraph;
    }

    private FlowchartComponent createNode(Task task) {
        Node source = visitTask(task);
        if (task instanceof Workflow) {
            source.addAttribute("shape", "folder");
            String subgraphId = "cluster_" + buildSubgraphId(task);
            Subgraph component = (Subgraph) rootGraph.getComponent(subgraphId);
            if (component == null) {
                component = (Subgraph) task.accept(this);
                rootGraph.add(component);
            }
            Edge edge = new Edge(source, component.getTarget());
            edge.addAttribute("lhead", component.id());
            rootGraph.add(edge);
        }
        return source;
    }

    private String buildSubgraphId(Task task) {
        return task.getClass().getSimpleName() + "_" + task.getName().replaceAll("[ -]", "_");
    }
}
