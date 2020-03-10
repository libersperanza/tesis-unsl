reset
set terminal postscript eps colortext "Times Roman,24"
set encoding locale
set xlabel "Tamaño de base de datos" offset 0,graph -0.11 
set ylabel "Ratio de comparaciones"
set key right bottom
set yrange [0:1]


set xrange [1034:15964]
set xtics (1034,2943,12354,13093,15964) offset -2.5,graph -0.11 rotate by 45 
set output "efecto_bd/g1_incremental_edb.eps"


plot "efecto_bd/incremental.different.G1.tsv" using ($1):($2) title "16" w lp,\
"efecto_bd/incremental.different.G1.tsv" using ($1):($3) title "32" w lp,\
"efecto_bd/incremental.different.G1.tsv" using ($1):($4) title "64" w lp,\
"efecto_bd/incremental.different.G1.tsv" using ($1):($5) title "128" w lp,\
"efecto_bd/incremental.different.G1.tsv" using ($1):($6) title "256" w lp


set xrange [19032:46530]
set xtics (19032,22421,27788,30420,34603,36957,39612,46530)  offset -2.5,graph -0.11 rotate by 45 
set output "efecto_bd/g2_incremental_edb.eps"


plot "efecto_bd/incremental.different.G2.tsv" using ($1):($2) title "64" w lp,\
"efecto_bd/incremental.different.G2.tsv" using ($1):($3) title "128" w lp,\
"efecto_bd/incremental.different.G2.tsv" using ($1):($4) title "256" w lp,\
"efecto_bd/incremental.different.G2.tsv" using ($1):($5) title "512" w lp,\
"efecto_bd/incremental.different.G2.tsv" using ($1):($6) title "1024" w lp



set xrange [57198:136323]
set xtics (57198,77720,87108,93014,106511,136323) offset -2.5,graph -0.13 rotate by 45 
set output "efecto_bd/g3_incremental_edb.eps"


plot "efecto_bd/incremental.different.G3.tsv" using ($1):($2) title "256" w lp,\
"efecto_bd/incremental.different.G3.tsv" using ($1):($3) title "512" w lp,\
"efecto_bd/incremental.different.G3.tsv" using ($1):($4) title "1024" w lp,\
"efecto_bd/incremental.different.G3.tsv" using ($1):($5) title "2048" w lp,\


set xrange [167995:213578]
set xtics (167995,194687,200375,213578) offset -2.5,graph -0.13 rotate by 45 
set output "efecto_bd/g4_incremental_edb.eps"

plot "efecto_bd/incremental.different.G4.tsv" using ($1):($2) title "512" w lp,\
"efecto_bd/incremental.different.G4.tsv" using ($1):($3) title "1024" w lp,\
"efecto_bd/incremental.different.G4.tsv" using ($1):($4) title "2048" w lp,\
"efecto_bd/incremental.different.G4.tsv" using ($1):($5) title "4096" w lp



set xrange [1034:15964]
set xtics (1034,2943,12354,13093,15964) offset -2.5,graph -0.11 rotate by 45 
set output "efecto_bd/g1_random_edb.eps"


plot "efecto_bd/random.different.G1.tsv" using ($1):($2) title "16" w lp,\
"efecto_bd/random.different.G1.tsv" using ($1):($3) title "32" w lp,\
"efecto_bd/random.different.G1.tsv" using ($1):($4) title "64" w lp,\
"efecto_bd/random.different.G1.tsv" using ($1):($5) title "128" w lp,\
"efecto_bd/random.different.G1.tsv" using ($1):($6) title "256" w lp

set xrange [19032:46530]
set xtics (19032,22421,27788,30420,34603,36957,39612,46530)  offset -2.5,graph -0.11 rotate by 45
set output "efecto_bd/g2_random_edb.eps"

plot "efecto_bd/random.different.G2.tsv" using ($1):($2) title "64" w lp,\
"efecto_bd/random.different.G2.tsv" using ($1):($3) title "128" w lp,\
"efecto_bd/random.different.G2.tsv" using ($1):($4) title "256" w lp,\
"efecto_bd/random.different.G2.tsv" using ($1):($5) title "512" w lp,\
"efecto_bd/random.different.G2.tsv" using ($1):($6) title "1024" w lp



set xrange [57198:136323]
set xtics (57198,77720,87108,93014,106511,136323) offset -2.5,graph -0.13 rotate by 45 
set output "efecto_bd/g3_random_edb.eps"

plot "efecto_bd/random.different.G3.tsv" using ($1):($2) title "256" w lp,\
"efecto_bd/random.different.G3.tsv" using ($1):($3) title "512" w lp,\
"efecto_bd/random.different.G3.tsv" using ($1):($4) title "1024" w lp,\
"efecto_bd/random.different.G3.tsv" using ($1):($5) title "2048" w lp,\


set xrange [167995:213578]
set xtics (167995,194687,200375,213578) offset -2.5,graph -0.13 rotate by 45
set output "efecto_bd/g4_random_edb.eps"


plot  "efecto_bd/random.different.G4.tsv" using ($1):($2) title "512" w lp,\
"efecto_bd/random.different.G4.tsv" using ($1):($3) title "1024" w lp,\
"efecto_bd/random.different.G4.tsv" using ($1):($4) title "2048" w lp,\
"efecto_bd/random.different.G4.tsv" using ($1):($5) title "4096" w lp

