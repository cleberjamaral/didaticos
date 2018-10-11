#!/bin/bash

rm graph*.pdf

a=1
for i in *.gv; do
  new=$(printf "graph_%d.pdf" "$a")
  dot -Tpdf $i -o "$new"  
  let a=a+1
done

rm graph*.gv
