# lojo

Clojure turtle graphics.

## Usage

```[lojo "0.0.1"]```

```
(start)
(forward 100)
(left 90)
(forward 100)
(left 90)
(forward 100)
(left 90)
(forward 100)
(left 90)

(home)
(clear)

(dotimes [_ 6] (forward 50) (right 60))

(home)
(clear)

(dotimes [_ 20] (left 18) (dotimes [_ 6] (forward 100) (left 60)))
```

![nice](./nice.png)
