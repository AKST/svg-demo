# svg-demo

![image](http://i.imgur.com/9Ovv6CP.gif)

## Setup

First [install leiningen](http://leiningen.org), then

```
git clone git@github.com:AKST/svg-demo.git
cd svg-demo
lein deps
lein figwheel
```

A clojure repl will appear, run `(in-ns 'svg-thing.core)` to go into the appropiate namespace, then you can change the light and temperature like so.

```
> (swap! app-state assoc-in [:light :explict] 0)
> (swap! app-state assoc-in [:light :explict] 100)
> (swap! app-state assoc-in [:temp :explict] 20)
> (swap! app-state assoc-in [:temp :explict] 60)
```
