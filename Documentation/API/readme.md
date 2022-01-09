# API Documentation

![](https://img.shields.io/badge/API%20version-18.11.2021-green?style=for-the-badge)

### Setup
___
##### 1. Import the necessities
```java
import API.APIManager;
import API.Interfaces.IFrontend;
import API.Interfaces.IBackend;
import API.Models.Node;
```
##### 2. Implement the Interface.

```java
public class MyFrontend implements IFrontend { }

public class MyBackEnd implements IBackend { }
```
##### 2. Get the APIManager through the constructor by the main class.
```java
private APIManager api;

public MyClass(APIManager api) {
    this.api = api;
}
```
### Frontend
___
##### 3. Override the Interface method
```java
@Override
public void update(Node node) {
    System.out.println("[API - Frontend] Node update received");
    this.matrix[node.getX()][node.getY()] = node;
    System.out.println(node);
}
```
##### 4. Transmit the Matrix created by the Frontend to the Backend
```java
api.initMatrix(this.matrix);
```
### Backend
___
##### 3. Override the Interface method
```java
@Override
    public void receive(Node[][] matrix) {
        System.out.format("[API - Backend] New Matrix received");
        this.matrix = matrix;
    }
```
##### 4. Send node changes to the Frontend
```java
public void sendToFrontEnd(Node node) {
    api.sendToFrontend(node);
}
```