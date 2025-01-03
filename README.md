## Intro
`jx` is a custom Lisp interpreter implemented and designed to be easily embedded in Java.

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

## Methods
Methods may be defined using `^`.

```
(^foo [x] Int 
  x)
  
(foo)
```

### Recursion
`recall` may be used to jump to the start of the current method.

```
(^fib [n a b] Int
  (if (> n 1) 
    (recall (dec n) b (+ a b)) 
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