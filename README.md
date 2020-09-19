# JBRTest plugin

Code Inspections with quick fixes for python annotations

## Examples

1. Add type of parameter of function
```python
# before

def fun(value):
    ...

# after quickfix

def fun(value: int):
    ...
```
2. Add type to variable
```python
# before

x = 1

# after quickfix

x: int = 1
```

#Install
Create Gradle project in IDEA (Please select in frameworks Java, Kotlin/JVM, and Intellij Platform Plugin)

Download the repository

Clean the project.
```shell script
$ gradle clean
```

Run the Gradle fatJar task.

```shell script
$ gradle fatJar
```
The Jar is create under the $project/build/libs/ folder.

Open PyCharm Community

Go to Preferences - Plugin

Click on the gear

Please select "Install Plugin from disk..."

Select the generated .jar file