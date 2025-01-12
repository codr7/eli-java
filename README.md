## Intro
`jx` is a custom Lisp interpreter implemented and designed to be easily embedded in Java.

## REPL
Launching the `.jar`-file without arguments starts a REPL.

```
$ java -jar jx.jar
jx v1

 1 (say 'hello)
 2
hello
```

## Performance
The core eval loop currently seems to run faster than Python3,
especially once the byte code has been stripped of redundancies.

### python
```
$ python3 benchmarks/run.py
```
```
fact 1.689405483
fib 0.634149815
```

### jx
```
$ java -jar jx.jar benchmarks/run.jx
```
```
fact 0.777442805S
fib 0.000486441S
```

As to exactly what's going on with the Fibonacci benchmark, your guess is as good as mine.
The optimized code produces the correct result, but runs quite a bit faster than I expected.

## Bindings
Values may be bound to identifiers at compile time using `var`.

```
(var foo 42)
foo
```
`42`

`let` may be used to create runtime bindings.

```
(let [foo 'bar]
  foo)
```
`'bar`

When `let` is used to override an outer binding, the value is
only overridden in the current call stack and restored on exit.

```
(var foo 'foo)
foo
```
`'foo`

```
(^baz [] foo)
  
(let [foo 'bar] (baz))
```
`'bar`

```
foo
```
`'foo`

## Loops
`for` may be used to repeat a block of code for each item in a sequence.

```
(let [foo 0]
  (for [i [1 2 3]
    (inc foo i))
  (foo))
```
`6`

## Methods
Methods may be defined using `^`.

```
(^foo [x] Int 
  x)
  
(foo 42)
```
`42`

### Recursion
`recall` may be used to jump to the start of the current method.

```
(^fib [n a b] Int
  (if (> n 1) 
    (recall (- n 1) b (+ a b)) 
    (if (is n 0) a b)))

(fib 10 0 1)
```
`55`

### Return
`return` exits the current method after evaluating its arguments.

```
(^foo [] Int 1 (return 2 3) 4)
(foo)
```
`3`

## IO
`say` prints its arguments followed by newline to standard output.
```
(say "35+7=" (+ 35 7))
```
```
35+7=42
```

## Quoting
Any expression may be quoted using `'`.

```
'[foo bar baz]
```
`['foo 'bar 'baz]`

## Testing
`check` may be used to validate that a block of code produces the expected value.
```
(check 1 2)
```
```
Error in REPL@1:1: Check failed; expected 1, actual: 2
```

When called with one argument, the expected value is assumed to be `T`.
```
(check (= 42 42))
```