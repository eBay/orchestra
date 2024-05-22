# Workflow Orchestrator 

Workflow Orchestrator provides a functionality to define and run workflows in intended ways. Workflow is a collection of one or more tasks and a task contains the user defined code. Execution of the tasks is configurable and can be done in sequential, parallel, repetitive and conditional ways.

### Sample Workflow

![image](https://github.com/eBay/orchestra/assets/78416587/221c0391-719f-4745-b77a-87da0e697b44)


## Getting Started

```
<dependency>
  <groupId>com.ebay.riskmgmt</groupId>
  <artifactId>orchestra</artifactId>
  <version>Use Latest Release version</version>
</dependency>
```
### Define workflow structure through yaml

```
name: Sample Workflow
version: 1.0

# List down the task beans here
task:
  taskA:
  taskB:
  taskC:
  taskD:


# List down the workflows
workflow:
  SequentialWF:
    desc: main sequential workflow
    type: sequential
    sequentialWF:
      executables: [taskC, taskD]
      
  ConditionalWF:
    desc: Validate and proceed conditional workflow
    type: conditional
    conditionalWF:
      executable: taskB
      thenExecutable: taskD
      otherwiseExecutable: SequentialWF
  
  StarterWF:
    desc: main sequential workflow
    type: sequential
    sequentialWF:
      executables: [taskA, ConditionalWF]

# Main workflow where execution begins
engine: StarterWF

```

### Write tasks
```
public class TaskA implements Task {
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public TaskReport execute(WorkflowContext workflowContext) throws WorkflowException {
        System.out.println("Executing " + getName());
        return new DefaultTaskReport(TaskStatus.COMPLETED);
    }
}
```

### Define task beans

```
@Configuration
@Import(CoreAppConfig.class)
public class ServiceAppConfig {

    @Bean(name = "taskA")
    public TaskA getTaskA() {
        return new TaskA();
    }

    @Bean(name = "taskB")
    public TaskB getTaskB() {
        return new TaskB();
    }

    @Bean(name = "taskC")
    public TaskC getTaskC() {
        return new TaskC();
    }

    @Bean(name = "taskD")
    public TaskD getTaskD() {
        return new TaskD();
    }
}
```

### Run workflow

```
public void foo() {
    WorkflowManager workflowManager = applicationContext.getBean(WorkflowManager.class);
    WorkflowContext workflowContext = new WorkflowContext();
    workflowContext.setContextFilePath("path/to/workflow/dir");
    workflowContext.setContextFile("sampleWorkflow.yaml");
    try {
      workflowManager.execute(workflowContext);
    } catch (WorkflowException e) {
      throw new RuntimeException(e);
    }
  }
```
