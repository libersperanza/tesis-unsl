reset
set terminal postscript eps colortext "Times Roman,24"
set encoding locale
set style data histogram
set style line 2 lc rgb 'black' lt 1 lw 1
set style histogram cluster gap 1
set style fill pattern border -1
set boxwidth 0.9
set grid ytics
set xtics format ""
set xlabel "Pivotes"
set ylabel "Evaluaciones de distancia"


set title "Tamaño de Base de Datos: 15964 objetos"
set yrange [10520:14829]
set output "random_vs_incremental/g1_15964.eps"
plot "random_vs_incremental/G1.tsv" using 12:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 13 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 46530 objetos"
set yrange [28655:40759]
set output "random_vs_incremental/g2_46530.eps"
plot "random_vs_incremental/G2.tsv" using 24:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 25 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 136323 objetos"
set yrange [91455:108968]
set output "random_vs_incremental/g3_136323.eps"
plot "random_vs_incremental/G3.tsv" using 16:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 17 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 213578 objetos"
set yrange [125521:154903]
set output "random_vs_incremental/g4_213578.eps"
plot "random_vs_incremental/G4.tsv" using 8:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 9 title "random" fill pattern 2 lc 9
