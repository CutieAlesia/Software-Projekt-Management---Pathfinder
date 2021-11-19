# API Documentation

![](https://img.shields.io/badge/API%20version-18.11.2021-green?style=for-the-badge)

### Frontend
___
##### 1. Implement the "IFrontend" Interface from 'api.interfaces'.
```java
public class MyFrontend implements IFrontend {}
```
##### 2. Get the APIManager through the constructor by the main class.
```java
private APIManager api;

public Frontend(APIManager api) {
    this.api = api;
}
```
##### 3. Override the Interface method
```java
@Override
public void update(int[][] data) {
    System.out.println("[API - Frontend] Update arrived");
    // Usage of the update data
}
```
_This function can receive either a completed matrix or single value changes._
##### 4. Mirror the created matrix so that it will be used for data transmission
```java
api.initMatrix(this.matrix);
```
##### 5. Send the matrix to the backend to start the process
_This will send the matrix to the backend_
```java
api.sendToBackend();
```