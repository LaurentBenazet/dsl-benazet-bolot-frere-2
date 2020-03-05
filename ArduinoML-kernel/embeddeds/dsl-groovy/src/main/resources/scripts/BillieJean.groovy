package scripts

track "Billie Jean"

instrument "ch" type "closed hi hat"
instrument "sd" type "acoustic snare"
instrument "bd" type "bass drum"

section "Main"
tempo 118
beats 4

instr "ch" bar "e e e e e e e e" repeat 34
instr "sd" bar "_ q _ q" repeat 34
instr "bd" bar "q _ q _" repeat 34

play "Main"
export "Billie Jean"
