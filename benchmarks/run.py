from timeit import Timer

def benchmark(reps, setup, test):
    Timer(test, setup).timeit(reps)
    return Timer(test, setup).timeit(reps)


print('fact', benchmark(10000, '''
def fact(acc, n):
  return acc if n == 1 else fact(n * acc, n - 1)
''',
'fact(1, 900)'))