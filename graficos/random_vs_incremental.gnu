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


set title "Tamaño de Base de Datos: 1034 objetos"
set yrange [389:980]
set output "random_vs_incremental/g1_1034.eps"
plot "random_vs_incremental/G1.tsv" using 2:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 3 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 2943 objetos"
set yrange [1406:2468]
set output "random_vs_incremental/g1_2943.eps"
plot "random_vs_incremental/G1.tsv" using 4:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 5 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 12354 objetos"
set yrange [6186:8977]
set output "random_vs_incremental/g1_12354.eps"
plot "random_vs_incremental/G1.tsv" using 6:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 7 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 13093 objetos"
set yrange [8889:12044]
set output "random_vs_incremental/g1_13093.eps"
plot "random_vs_incremental/G1.tsv" using 8:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 9 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 15796 objetos"
set yrange [10534:14203]
set output "random_vs_incremental/g1_15796.eps"
plot "random_vs_incremental/G1.tsv" using 10:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 11 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 15964 objetos"
set yrange [10520:14829]
set output "random_vs_incremental/g1_15964.eps"
plot "random_vs_incremental/G1.tsv" using 12:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 13 title "random" fill pattern 2 lc 9


set title "Tamaño de Base de Datos: 19032 objetos"
set yrange [8977:14207]
set output "random_vs_incremental/g2_19032.eps"
plot "random_vs_incremental/G2.tsv" using 2:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 3 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 22421 objetos"
set yrange [10667:14837]
set output "random_vs_incremental/g2_22421.eps"
plot "random_vs_incremental/G2.tsv" using 4:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 5 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 26903 objetos"
set yrange [14508:20140]
set output "random_vs_incremental/g2_26903.eps"
plot "random_vs_incremental/G2.tsv" using 6:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 7 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 27788 objetos"
set yrange [10984:17128]
set output "random_vs_incremental/g2_27788.eps"
plot "random_vs_incremental/G2.tsv" using 8:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 9 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 29426 objetos"
set yrange [13365:22271]
set output "random_vs_incremental/g2_29426.eps"
plot "random_vs_incremental/G2.tsv" using 10:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 11 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 29475 objetos"
set yrange [16770:23797]
set output "random_vs_incremental/g2_29475.eps"
plot "random_vs_incremental/G2.tsv" using 12:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 13 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 30420 objetos"
set yrange [17770:26695]
set output "random_vs_incremental/g2_30420.eps"
plot "random_vs_incremental/G2.tsv" using 14:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 15 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 34603 objetos"
set yrange [18003:28008]
set output "random_vs_incremental/g2_34603.eps"
plot "random_vs_incremental/G2.tsv" using 16:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 17 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 36280 objetos"
set yrange [20853:27834]
set output "random_vs_incremental/g2_36280.eps"
plot "random_vs_incremental/G2.tsv" using 18:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 19 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 36957 objetos"
set yrange [20738:28596]
set output "random_vs_incremental/g2_36957.eps"
plot "random_vs_incremental/G2.tsv" using 20:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 21 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 39612 objetos"
set yrange [18577:28974]
set output "random_vs_incremental/g2_39612.eps"
plot "random_vs_incremental/G2.tsv" using 22:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 23 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 46530 objetos"
set yrange [28655:40759]
set output "random_vs_incremental/g2_46530.eps"
plot "random_vs_incremental/G2.tsv" using 24:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 25 title "random" fill pattern 2 lc 9


set title "Tamaño de Base de Datos: 57198 objetos"
set yrange [33929:42513]
set output "random_vs_incremental/g3_57198.eps"
plot "random_vs_incremental/G3.tsv" using 2:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 3 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 77264 objetos"
set yrange [33567:52639]
set output "random_vs_incremental/g3_77264.eps"
plot "random_vs_incremental/G3.tsv" using 4:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 5 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 77720 objetos"
set yrange [38953:56403]
set output "random_vs_incremental/g3_77720.eps"
plot "random_vs_incremental/G3.tsv" using 6:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 7 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 86447 objetos"
set yrange [51212:65028]
set output "random_vs_incremental/g3_86447.eps"
plot "random_vs_incremental/G3.tsv" using 8:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 9 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 87108 objetos"
set yrange [49306:69702]
set output "random_vs_incremental/g3_87108.eps"
plot "random_vs_incremental/G3.tsv" using 10:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 11 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 93014 objetos"
set yrange [46598:71178]
set output "random_vs_incremental/g3_93014.eps"
plot "random_vs_incremental/G3.tsv" using 12:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 13 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 106511 objetos"
set yrange [63836:81688]
set output "random_vs_incremental/g3_106511.eps"
plot "random_vs_incremental/G3.tsv" using 14:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 15 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 136323 objetos"
set yrange [91455:108968]
set output "random_vs_incremental/g3_136323.eps"
plot "random_vs_incremental/G3.tsv" using 16:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 17 title "random" fill pattern 2 lc 9


set title "Tamaño de Base de Datos: 167995 objetos"
set yrange [84997:107002]
set output "random_vs_incremental/g4_167995.eps"
plot "random_vs_incremental/G4.tsv" using 2:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 3 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 194687 objetos"
set yrange [102291:139650]
set output "random_vs_incremental/g4_194687.eps"
plot "random_vs_incremental/G4.tsv" using 4:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 5 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 200375 objetos"
set yrange [66781:129481]
set output "random_vs_incremental/g4_200375.eps"
plot "random_vs_incremental/G4.tsv" using 6:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 7 title "random" fill pattern 2 lc 9

set title "Tamaño de Base de Datos: 213578 objetos"
set yrange [125521:154903]
set output "random_vs_incremental/g4_213578.eps"
plot "random_vs_incremental/G4.tsv" using 8:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 9 title "random" fill pattern 2 lc 9
