┌──────────────────────────────────────────────────────────────┐
│   Time (approx) | Event & Internal Executor State            │
└──────────────────────────────────────────────────────────────┘
t = 0 ms
    → Executor created: ThreadPoolExecutor(RUNNING, workerCount=0)
    → state = RUNNING (accepting tasks)

t = 10 ms
    → submit() called
      - workerCount incremented → 1
      - one worker thread starts running your lambda
      - thread enters Thread.sleep(5000)

    state: RUNNING
    threads: [Worker-1 (sleeping)]
    queue: []

──────────────────────────────────────────────────────────────
t = 100 ms
    → shutdownNow() called

    Internally:
    1. lock mainLock
    2. advanceRunState(STOP)
       → ctl upper bits changed to STOP (111)
    3. interrupt all workers
       → Worker-1 gets Thread.interrupt()
    4. drainQueue() (empty)

    state: STOP
    threads: [Worker-1 (interrupted)]
    queue: []

──────────────────────────────────────────────────────────────
t = 101 ms
    → Worker thread wakes up from sleep due to InterruptedException.
      - Your catch block rethrows RuntimeException
      - Worker.run() exits
      - workerCount decremented → 0

    ThreadPoolExecutor notices no workers left → enters tidy state.

    state: TIDYING
    → calls terminated() hook (empty by default)
    → state changes to TERMINATED

    state: TERMINATED
    threads: []
    queue: []

──────────────────────────────────────────────────────────────
t = 200 ms
    → awaitTermination(1 sec) called
      - sees ctl == TERMINATED
      - returns immediately: true

    Your output:
      isTerminated() → true
