package scripts

track "Billie Jean with piano"

instrument "ch" type "closed hithat"
instrument "sd" type "snare drum"
instrument "bd" type "bass drum"
instrument "piano" type "Piano"

section "No piano"
tempo 118
beats 4

instr "ch" bar "e e e e e e e e" repeat 14
instr "sd" bar "_ q _ q" repeat 14
instr "bd" bar "q _ q _" repeat 14
instr "piano" bar "_ _ _ _" repeat 14

section "Main"
tempo 118
beats 4

instr "ch" bar "e e e e e e e e" repeat 8
instr "sd" bar "_ q _ q" repeat 8
instr "bd" bar "q _ q _" repeat 8
instr "piano" bar "_ _ _ _" bar "- e(D) e(D) e(D) e(D) e(C) e(D) e(C)" bar "- q(D) s(C) s(D) e(C) e(D) e(D) -"
instr "piano" bar "- e(D) e(D) e(D) e(D) e(C) e(D) e(C)" bar "- q(D) q(C) e(D) e(C#) e(E)" bar "q(E) _ _ s(E) s(F) e(D)"
instr "piano" bar "q(D) s(E) s(F) e(D) q(D) s(E) s(F) e(D)" bar "q(D) e(D) - _ _"

section "Transition"
tempo 118
beats 4

instr "ch" bar "e e e e e e e e" repeat 4
instr "sd" bar "_ q _ q" repeat 4
instr "bd" bar "q _ q _" repeat 4
instr "piano" bar "- e(E) e(D) q(C) e(D) e(C#) e(E)" bar "q(E) e(E) _ - s(E) s(F) e(D)"
instr "piano" bar "q(D) s(E) s(F) e(D) q(D) s(E) s(F) e(D)" bar "q(D) e(D) - _ _"


play "No piano" "Main" "Transition" "Main"
