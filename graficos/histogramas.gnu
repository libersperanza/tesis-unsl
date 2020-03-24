reset
set terminal postscript eps colortext "Times Roman,20"
set encoding locale
set xlabel "Distancia"
set ylabel "Objetos"
set style fill solid 0.13
set boxwidth 1

set grid ytics

set output "histogramas/1034_items.eps"
set title "Tamaño de Base de Datos: 1034 objetos"
set yrange [10:66000]
set xrange [0:60]
plot "histogramas/1034_items.tsv" using 1:2 title "" with boxes 

set output "histogramas/2943_items.eps"
set title "Tamaño de Base de Datos: 2943 objetos"
set yrange [6:666000]
set xrange [0:60]
plot "histogramas/2943_items.tsv" using 1:2 title "" with boxes

set output "histogramas/24708_items.eps"
set title "Tamaño de Base de Datos: 24708 objetos"
set yrange [2798:7101000]
set xrange [0:60]
plot "histogramas/24708_items.tsv" using 1:2 title "" with boxes

set output "histogramas/26186_items.eps"
set title "Tamaño de Base de Datos: 26186 objetos"
set yrange [384:15017000]
set xrange [0:60]
plot "histogramas/26186_items.tsv" using 1:2 title "" with boxes

set output "histogramas/31591_items.eps"
set title "Tamaño de Base de Datos: 31591 objetos"
set yrange [2:158000]
set xrange [0:60]
plot "histogramas/31591_items.tsv" using 1:2 title "" with boxes

set output "histogramas/31928_items.eps"
set title "Tamaño de Base de Datos: 31928 objetos"
set yrange [15:256701]
set xrange [0:60]
plot "histogramas/31928_items.tsv" using 1:2 title "" with boxes

set output "histogramas/38064_items.eps"
set title "Tamaño de Base de Datos: 38064 objetos"
set yrange [101:3460000]
set xrange [0:60]
plot "histogramas/38064_items.tsv" using 1:2 title "" with boxes

set output "histogramas/44842_items.eps"
set title "Tamaño de Base de Datos: 44842 objetos"
set yrange [40:382000]
set xrange [0:60]
plot "histogramas/44842_items.tsv" using 1:2 title "" with boxes

set output "histogramas/53806_items.eps"
set title "Tamaño de Base de Datos: 53806 objetos"
set yrange [17:546000]
set xrange [0:60]
plot "histogramas/53806_items.tsv" using 1:2 title "" with boxes

set output "histogramas/55576_items.eps"
set title "Tamaño de Base de Datos: 55576 objetos"
set yrange [1:263000]
set xrange [0:177]
plot "histogramas/55576_items.tsv" using 1:2 title "" with boxes

set output "histogramas/58852_items.eps"
set title "Tamaño de Base de Datos: 58852 objetos"
set yrange [9:590000]
set xrange [0:60]
plot "histogramas/58852_items.tsv" using 1:2 title "" with boxes

set output "histogramas/58949_items.eps"
set title "Tamaño de Base de Datos: 58949 objetos"
set yrange [5:674000]
set xrange [0:60]
plot "histogramas/58949_items.tsv" using 1:2 title "" with boxes

set output "histogramas/60840_items.eps"
set title "Tamaño de Base de Datos: 60840 objetos"
set yrange [8:844000]
set xrange [0:60]
plot "histogramas/60840_items.tsv" using 1:2 title "" with boxes

set output "histogramas/69206_items.eps"
set title "Tamaño de Base de Datos: 69206 objetos"
set yrange [8:1138000]
set xrange [0:60]
plot "histogramas/69206_items.tsv" using 1:2 title "" with boxes

set output "histogramas/72560_items.eps"
set title "Tamaño de Base de Datos: 72560 objetos"
set yrange [12:1091000]
set xrange [0:60]
plot "histogramas/72560_items.tsv" using 1:2 title "" with boxes

set output "histogramas/73914_items.eps"
set title "Tamaño de Base de Datos: 73914 objetos"
set yrange [24:963000]
set xrange [0:60]
plot "histogramas/73914_items.tsv" using 1:2 title "" with boxes

set output "histogramas/79224_items.eps"
set title "Tamaño de Base de Datos: 79224 objetos"
set yrange [40:1550000]
set xrange [0:60]
plot "histogramas/79224_items.tsv" using 1:2 title "" with boxes

set output "histogramas/93060_items.eps"
set title "Tamaño de Base de Datos: 93060 objetos"
set yrange [1249:78902000]
set xrange [0:60]
plot "histogramas/93060_items.tsv" using 1:2 title "" with boxes

set output "histogramas/114396_items.eps"
set title "Tamaño de Base de Datos: 114396 objetos"
set yrange [13:3204000]
set xrange [0:60]
plot "histogramas/114396_items.tsv" using 1:2 title "" with boxes

set output "histogramas/154527_items.eps"
set title "Tamaño de Base de Datos: 154527 objetos"
set yrange [467:6260000]
set xrange [0:60]
plot "histogramas/154527_items.tsv" using 1:2 title "" with boxes

set output "histogramas/155440_items.eps"
set title "Tamaño de Base de Datos: 155440 objetos"
set yrange [26:6145000]
set xrange [0:60]
plot "histogramas/155440_items.tsv" using 1:2 title "" with boxes

set output "histogramas/172894_items.eps"
set title "Tamaño de Base de Datos: 172894 objetos"
set yrange [59:7012000]
set xrange [0:60]
plot "histogramas/172894_items.tsv" using 1:2 title "" with boxes

set output "histogramas/174215_items.eps"
set title "Tamaño de Base de Datos: 174215 objetos"
set yrange [67:6983000]
set xrange [0:60]
plot "histogramas/174215_items.tsv" using 1:2 title "" with boxes

set output "histogramas/186027_items.eps"
set title "Tamaño de Base de Datos: 186027 objetos"
set yrange [45:8807000]
set xrange [0:60]
plot "histogramas/186027_items.tsv" using 1:2 title "" with boxes

set output "histogramas/213022_items.eps"
set title "Tamaño de Base de Datos: 213022 objetos"
set yrange [36:10113000]
set xrange [0:60]
plot "histogramas/213022_items.tsv" using 1:2 title "" with boxes

set output "histogramas/272646_items.eps"
set title "Tamaño de Base de Datos: 272646 objetos"
set yrange [3:11353000]
set xrange [0:60]
plot "histogramas/272646_items.tsv" using 1:2 title "" with boxes

set output "histogramas/335989_items.eps"
set title "Tamaño de Base de Datos: 335989 objetos"
set yrange [299:17639000]
set xrange [0:60]
plot "histogramas/335989_items.tsv" using 1:2 title "" with boxes

set output "histogramas/389374_items.eps"
set title "Tamaño de Base de Datos: 389374 objetos"
set yrange [321:36718000]
set xrange [0:60]
plot "histogramas/389374_items.tsv" using 1:2 title "" with boxes

set output "histogramas/1100749_items.eps"
set title "Tamaño de Base de Datos: 1100749 objetos"
set yrange [136:32663000]
set xrange [0:60]
plot "histogramas/1100749_items.tsv" using 1:2 title "" with boxes

set output "histogramas/1854311_items.eps"
set title "Tamaño de Base de Datos: 1854311 objetos"
set yrange [23:39946000]
set xrange [0:60]
plot "histogramas/1854311_items.tsv" using 1:2 title "" with boxes
