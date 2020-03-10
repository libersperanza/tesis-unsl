reset
set terminal postscript eps colortext "Times Roman,24"
set encoding locale
set xlabel "Tamaño de base de datos" offset 0,graph -0.09 
set ylabel "Ratio de comparaciones"
set key right bottom
set yrange [0:1]

set xrange [1034:15964]
set xtics (1034,2943,12354,13093,15964) offset -2.5,graph -0.11 rotate by 45 
set output "same_vs_different/16p_incremental.eps"


plot "same_vs_different/16p.incremental.tsv" using ($1):($2) title "different" w lp,\
"same_vs_different/16p.incremental.tsv" using ($1):($3) title "same" w lp


set xrange [1034:15964]
set xtics (1034,2943,12354,13093,15964) offset -2.5,graph -0.11 rotate by 45 
set output "same_vs_different/16p_random.eps"


plot "same_vs_different/16p.random.tsv" using ($1):($2) title "different" w lp,\
"same_vs_different/16p.random.tsv" using ($1):($3) title "same" w lp

set xrange [1034:46530]
set xtics (1034,2943,13093,15964,19032,22421,27788,30420,34603,36957,39612,46530) offset -2.5,graph -0.11 rotate by 45 
set output "same_vs_different/64p_incremental.eps"


plot "same_vs_different/64p.incremental.tsv" using ($1):($2) title "different" w lp,\
"same_vs_different/64p.incremental.tsv" using ($1):($3) title "same" w lp


set xrange [1034:46530]
set xtics (1034,2943,13093,15964,19032,22421,27788,30420,34603,36957,39612,46530) offset -2.5,graph -0.11 rotate by 45 
set output "same_vs_different/64p_random.eps"

plot "same_vs_different/64p.random.tsv" using ($1):($2) title "different" w lp,\
"same_vs_different/64p.random.tsv" using ($1):($3) title "same" w lp

