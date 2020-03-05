package scripts

track "Johnny B Goode"

instrument "rc" type "ride cymbal"
instrument "sd" type "acoustic snare"
instrument "bd" type "bass drum"

section "Intro"
tempo 170
beats 4

instr "rc" bar "_ _ _ _" bar "_ _ _ _"
instr "sd" bar "q _ _ _" bar "_ _ _ q"
instr "bd" bar "_ _ _ _" bar "_ _ _ _"


section "Main"
tempo 170
beats 4

instr "rc" bar "e e e e e e e e" repeat 55
instr "sd" bar "_ q _ q" repeat 55
instr "bd" bar "q _ q _" repeat 55

play "Intro" "Main"
