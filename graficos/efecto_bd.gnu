set terminal postscript  eps color "times" 20
set xlabel "Tamaño de base de datos"
set ylabel "Ratio de comparaciones"
set key outside right top
set yrange [0:1]


set xrange [1034:15964]
set output "efecto_bd/g1_incremental_edb.eps"


plot "efecto_bd/incremental.different.G1.tsv" using ($1):($2) title "16" w lp,\
"efecto_bd/incremental.different.G1.tsv" using ($1):($3) title "32" w lp,\
"efecto_bd/incremental.different.G1.tsv" using ($1):($4) title "64" w lp,\
"efecto_bd/incremental.different.G1.tsv" using ($1):($5) title "128" w lp,\
"efecto_bd/incremental.different.G1.tsv" using ($1):($6) title "256" w lp



set xrange [19032:46530]
set output "efecto_bd/g2_incremental_edb.eps"


plot "efecto_bd/incremental.different.G2.tsv" using ($1):($2) title "64" w lp,\
"efecto_bd/incremental.different.G2.tsv" using ($1):($3) title "128" w lp,\
"efecto_bd/incremental.different.G2.tsv" using ($1):($4) title "256" w lp,\
"efecto_bd/incremental.different.G2.tsv" using ($1):($5) title "512" w lp,\
"efecto_bd/incremental.different.G2.tsv" using ($1):($6) title "1024" w lp



set xrange [57198:136323]
set output "efecto_bd/g3_incremental_edb.eps"


plot "efecto_bd/incremental.different.G3.tsv" using ($1):($2) title "256" w lp,\
"efecto_bd/incremental.different.G3.tsv" using ($1):($3) title "512" w lp,\
"efecto_bd/incremental.different.G3.tsv" using ($1):($4) title "1024" w lp,\
"efecto_bd/incremental.different.G3.tsv" using ($1):($5) title "2048" w lp,\



set xrange [167995:213578]
set output "efecto_bd/g4_incremental_edb.eps"


plot "efecto_bd/incremental.different.G4.tsv" using ($1):($2) title "512" w lp,\
"efecto_bd/incremental.different.G4.tsv" using ($1):($3) title "1024" w lp,\
"efecto_bd/incremental.different.G4.tsv" using ($1):($4) title "2048" w lp,\
"efecto_bd/incremental.different.G4.tsv" using ($1):($5) title "4096" w lp



set xrange [1034:15964]
set output "efecto_bd/g1_random_edb.eps"


plot "efecto_bd/random.different.G1.tsv" using ($1):($2) title "16" w lp,\
"efecto_bd/random.different.G1.tsv" using ($1):($3) title "32" w lp,\
"efecto_bd/random.different.G1.tsv" using ($1):($4) title "64" w lp,\
"efecto_bd/random.different.G1.tsv" using ($1):($5) title "128" w lp,\
"efecto_bd/random.different.G1.tsv" using ($1):($6) title "256" w lp



set xrange [19032:46530]
set output "efecto_bd/g2_random_edb.eps"


plot "efecto_bd/random.different.G2.tsv" using ($1):($2) title "64" w lp,\
"efecto_bd/random.different.G2.tsv" using ($1):($3) title "128" w lp,\
"efecto_bd/random.different.G2.tsv" using ($1):($4) title "256" w lp,\
"efecto_bd/random.different.G2.tsv" using ($1):($5) title "512" w lp,\
"efecto_bd/random.different.G2.tsv" using ($1):($6) title "1024" w lp



set xrange [57198:136323]
set output "efecto_bd/g3_random_edb.eps"


plot "efecto_bd/random.different.G3.tsv" using ($1):($2) title "256" w lp,\
"efecto_bd/random.different.G3.tsv" using ($1):($3) title "512" w lp,\
"efecto_bd/random.different.G3.tsv" using ($1):($4) title "1024" w lp,\
"efecto_bd/random.different.G3.tsv" using ($1):($5) title "2048" w lp,\



set xrange [167995:213578]
set output "efecto_bd/g4_random_edb.eps"


plot  "efecto_bd/random.different.G4.tsv" using ($1):($2) title "512" w lp,\
"efecto_bd/random.different.G4.tsv" using ($1):($3) title "1024" w lp,\
"efecto_bd/random.different.G4.tsv" using ($1):($4) title "2048" w lp,\
"efecto_bd/random.different.G4.tsv" using ($1):($5) title "4096" w lp

