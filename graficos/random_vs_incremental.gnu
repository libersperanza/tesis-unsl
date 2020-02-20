set terminal postscript  eps color "times" 20
set style data histogram
set style line 2 lc rgb 'black' lt 1 lw 1
set style histogram cluster gap 1
set style fill pattern border -1
set boxwidth 0.9
set grid ytics
set xtics format ""
set xlabel "Distancia"
set ylabel "Objetos"


set title "Tamaño BD: 1034 objetos"
set yrange [389:980]
set output "random_vs_incremental/1034.ps"
plot "random_vs_incremental/G1.tsv" using 2:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 3 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 2943 objetos"
set yrange [1406:2468]
set output "random_vs_incremental/2943.ps"
plot "random_vs_incremental/G1.tsv" using 4:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 5 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 12354 objetos"
set yrange [6186:8977]
set output "random_vs_incremental/12354.ps"
plot "random_vs_incremental/G1.tsv" using 6:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 7 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 13093 objetos"
set yrange [8889:12044]
set output "random_vs_incremental/13093.ps"
plot "random_vs_incremental/G1.tsv" using 8:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 9 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 15796 objetos"
set yrange [10534:14203]
set output "random_vs_incremental/15796.ps"
plot "random_vs_incremental/G1.tsv" using 10:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 11 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 15964 objetos"
set yrange [10520:14829]
set output "random_vs_incremental/15964.ps"
plot "random_vs_incremental/G1.tsv" using 12:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 13 title "random" fill pattern 2 lc 9


set title "Tamaño BD: 19032 objetos"
set yrange [8977:14207]
set output "random_vs_incremental/19032.ps"
plot "random_vs_incremental/G2.tsv" using 2:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 3 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 22421 objetos"
set yrange [10667:14837]
set output "random_vs_incremental/22421.ps"
plot "random_vs_incremental/G2.tsv" using 4:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 5 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 26903 objetos"
set yrange [14508:20140]
set output "random_vs_incremental/26903.ps"
plot "random_vs_incremental/G2.tsv" using 6:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 7 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 27788 objetos"
set yrange [10984:17128]
set output "random_vs_incremental/27788.ps"
plot "random_vs_incremental/G2.tsv" using 8:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 9 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 29426 objetos"
set yrange [13365:22271]
set output "random_vs_incremental/29426.ps"
plot "random_vs_incremental/G2.tsv" using 10:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 11 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 29475 objetos"
set yrange [16770:23797]
set output "random_vs_incremental/29475.ps"
plot "random_vs_incremental/G2.tsv" using 12:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 13 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 30420 objetos"
set yrange [17770:26695]
set output "random_vs_incremental/30420.ps"
plot "random_vs_incremental/G2.tsv" using 14:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 15 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 34603 objetos"
set yrange [18003:28008]
set output "random_vs_incremental/34603.ps"
plot "random_vs_incremental/G2.tsv" using 16:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 17 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 36280 objetos"
set yrange [20853:27834]
set output "random_vs_incremental/36280.ps"
plot "random_vs_incremental/G2.tsv" using 18:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 19 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 36957 objetos"
set yrange [20738:28596]
set output "random_vs_incremental/36957.ps"
plot "random_vs_incremental/G2.tsv" using 20:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 21 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 39612 objetos"
set yrange [18577:28974]
set output "random_vs_incremental/39612.ps"
plot "random_vs_incremental/G2.tsv" using 22:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 23 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 46530 objetos"
set yrange [28655:40759]
set output "random_vs_incremental/46530.ps"
plot "random_vs_incremental/G2.tsv" using 24:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 25 title "random" fill pattern 2 lc 9


set title "Tamaño BD: 57198 objetos"
set yrange [33929:42513]
set output "random_vs_incremental/57198.ps"
plot "random_vs_incremental/G3.tsv" using 2:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 3 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 77264 objetos"
set yrange [33567:52639]
set output "random_vs_incremental/77264.ps"
plot "random_vs_incremental/G3.tsv" using 4:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 5 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 77720 objetos"
set yrange [38953:56403]
set output "random_vs_incremental/77720.ps"
plot "random_vs_incremental/G3.tsv" using 6:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 7 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 86447 objetos"
set yrange [51212:65028]
set output "random_vs_incremental/86447.ps"
plot "random_vs_incremental/G3.tsv" using 8:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 9 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 87108 objetos"
set yrange [49306:69702]
set output "random_vs_incremental/87108.ps"
plot "random_vs_incremental/G3.tsv" using 10:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 11 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 93014 objetos"
set yrange [46598:71178]
set output "random_vs_incremental/93014.ps"
plot "random_vs_incremental/G3.tsv" using 12:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 13 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 106511 objetos"
set yrange [63836:81688]
set output "random_vs_incremental/106511.ps"
plot "random_vs_incremental/G3.tsv" using 14:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 15 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 136323 objetos"
set yrange [91455:108968]
set output "random_vs_incremental/136323.ps"
plot "random_vs_incremental/G3.tsv" using 16:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 17 title "random" fill pattern 2 lc 9


set title "Tamaño BD: 167995 objetos"
set yrange [84997:107002]
set output "random_vs_incremental/167995.ps"
plot "random_vs_incremental/G4.tsv" using 2:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 3 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 194687 objetos"
set yrange [102291:139650]
set output "random_vs_incremental/194687.ps"
plot "random_vs_incremental/G4.tsv" using 4:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 5 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 200375 objetos"
set yrange [66781:129481]
set output "random_vs_incremental/200375.ps"
plot "random_vs_incremental/G4.tsv" using 6:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 7 title "random" fill pattern 2 lc 9

set title "Tamaño BD: 213578 objetos"
set yrange [125521:154903]
set output "random_vs_incremental/213578.ps"
plot "random_vs_incremental/G4.tsv" using 8:xtic(1) title "incremental" fill pattern 5 lc 8, '' using 9 title "random" fill pattern 2 lc 9
