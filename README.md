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