# Spreadsheet Calculator


**Negative numbers are supported**


### Behavior Explanation
- Output is in Numeric format
- For empty cell, it outputs 0.00000
- For any invalid cell reference (i.e. A2.1), it prints NaN
- For cyclic cell reference (i.e. A1->A2, A2->A1), it prints NaN


### Usage

running test:
```
mvn test
```

Main class:
> Spreadsheet.java
 
 
#### Using command line to compile and execute:

go to folder  

> src/main/java

run: 
```
javac Spreadsheet.java
```

execute: 
```
cat spreadsheet.txt | java Spreadsheet
```
