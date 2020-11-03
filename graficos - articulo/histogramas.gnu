reset
set terminal postscript eps color "Times Roman,20"
set encoding locale
set xlabel "Distancia"
set ylabel "Objetos"
set style fill solid 0.13
set boxwidth 1

set grid ytics
set format y "%6.0f"

set output "histogramas/31928_items.eps"
set title "Tamaño de Base de Datos: 15964 objetos"
set yrange [15:256701]
set xrange [0:60]
plot "histogramas/31928_items.tsv" using 1:2 title "" with boxes

set output "histogramas/93060_items.eps"
set title "Tamaño de Base de Datos: 46530 objetos"
set yrange [1249:78902000]
set xrange [0:60]
plot "histogramas/93060_items.tsv" using 1:2 title "" with boxes

set output "histogramas/272646_items.eps"
set title "Tamaño de Base de Datos: 136323 objetos"
set yrange [3:11353000]
set xrange [0:60]
plot "histogramas/272646_items.tsv" using 1:2 title "" with boxes


set output "histogramas/1854311_items.eps"
set title "Tamaño de Base de Datos: 213578 objetos"
set yrange [23:39946000]
set xrange [0:60]
plot "histogramas/1854311_items.tsv" using 1:2 title "" with boxes
