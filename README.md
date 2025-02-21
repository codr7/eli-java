# eli-java

## Introduction
This project implements the [eli](https://github.com/codr7/eli) language in Java.

## REPL
Launching the `.jar`-file without arguments starts a REPL.

```
$ java -jar eli.jar
eli v1

 1 (say 'hello)
 2
hello
```

## Performance

```
$ java -jar eli.jar ../eli/benchmarks/run.eli
```
```
fact 1.100314039S
fib 0.880710983S
```