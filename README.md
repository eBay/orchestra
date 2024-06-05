# Workflow Orchestrator 

Workflow Orchestrator provides a functionality to define and run workflows in intended ways. Workflow is a collection of one or more tasks and a task contains the user defined code. Execution of the tasks is configurable and can be done in sequential, parallel, repetitive and conditional ways.

# Key Concepts
Below are the basic key concepts which a developer should get aware of before integrating with the workflow library. 
## Task
Task is the basic working entity in this platform. Concrete implementation of Task class is required to perform any operation such as making a service call or doing a request/response conversion. 

## Task Report
Every task must return a report. The report essentially has a success/failure information and can also contain other details such as an error during the execution of the task. 

## Workflow
Workflow is a collection of one or more tasks. Execution of the tasks is configurable and can be done in sequential, parallel, repetitive and conditional ways. Keep in mind that all workflow types can accept both tasks as well as another workflow as its input(s). All workflows need to have the below fields set in the YAML file.

    <Unique Workflow ID:>:
      desc: <A short description>
      type: <repeat/conditional/parallel/sequential>

## Executable
Both workflows and tasks are commonly referred to as `executable` in the YAML file.

## Workflow Types

### Repeat Workflow
Repeat Workflow is used to execute a task or workflow repeatedly until the condition is met. While configuring a repeat workflow in the YAML file, the developer needs to mention the following fields.


    repeatWF:
      executable: <Task which needs to be executed>
      times: <int>


### Parallel Workflow
Parallel Workflow should be used to perform multiple tasks or workflows in parallel. Parallel Flow submits the tasks listed in the flow to the java executor service whose timeout can be configured in the YAML file. Below is the syntax for writing a parallel workflow in the YAML file. 

    parallelWF:
      timeout: <long>
      executables:
        <Task 1 which needs to be executed in parallel>:
          instanceGenerator: <optional field - If multiple instances of the above task needs to be run in parallel>
          elementType: <optional field - Constructor argument such as String, Long, etc>
        <Task 2 which needs to be executed in parallel>:
        ...
        <Task n which needs to be executed in parallel>:
        <Workflow 1 which needs to be executed in parallel>:
        <Workflow 2 which needs to be executed in parallel>:
        ...
        <Workflow n which needs to be executed in parallel>:

### Conditional Workflow
Conditional Workflow helps in branching the execution path. For instance, if request validation passes, then perform other steps otherwise construct error message and exit out. Below is the syntax for writing a conditional workflow in the YAML file. 

    conditionalWF:
      executable: <task or workflow>
      thenExecutable: <task or workflow>
      otherwiseExecutable: <task or workflow>

### Sequential Workflow
As the name suggests, Sequential workflow is used to execute tasks or workflows in the order they are listed. Below is the syntax for writing a sequential workflow in the YAML file. 

    sequentialWF:
      executable: [<task 1>, <task 2>, <workflow 1>, ... <task n>]

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
