.data                         # BEGIN Data Segment
cruxdata.canary_begin: .space 4
cruxdata.x: .space 12
cruxdata.canary_end: .space 4
data.newline:      .asciiz       "\n"
data.floatquery:   .asciiz       "float?"
data.intquery:     .asciiz       "int?"
data.trueString:   .asciiz       "true"
data.falseString:  .asciiz       "false"
                              # END Data Segment
.text                         # BEGIN Code Segment
func.printBool:
lw $a0, 0($sp)
beqz $a0, label.printBool.loadFalse
la $a0, data.trueString
j label.printBool.join
label.printBool.loadFalse:
la $a0, data.falseString
label.printBool.join:
li   $v0, 4
syscall
jr $ra
func.printFloat:
l.s  $f12, 0($sp)
li   $v0,  2
syscall
jr $ra
func.printInt:
lw   $a0, 0($sp)
li   $v0, 1
syscall
jr $ra
func.println:
la   $a0, data.newline
li   $v0, 4
syscall
jr $ra
func.readFloat:
la   $a0, data.floatquery
li   $v0, 4
syscall
li   $v0, 6
syscall
mfc1 $v0, $f0
jr $ra
func.readInt:
la   $a0, data.intquery
li   $v0, 4
syscall
li   $v0, 5
syscall
jr $ra
.text                         # BEGIN Crux Program
main:
subu    $sp, $sp, 8
sw    $fp, 0($sp)
sw    $ra, 4($sp)
addi    $fp, $sp, 8
subu    $sp, $sp, 0
la    $t0, cruxdata.canary_begin
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $zero, 0
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sw    $t1, 0($t0)
la    $t0, cruxdata.canary_end
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $zero, 0
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sw    $t1, 0($t0)
la    $t0, cruxdata.x
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $zero, 0
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t7, $zero, 4
lw    $t8, 0($sp)
addi    $sp, $sp, 4
mult    $t7, $t8
mflo    $t2
lw    $t3, 0($sp)
addi    $sp, $sp, 4
add    $t4, $t3, $t2
subu    $sp, $sp, 4
sw    $t4, 0($sp)
addi    $t0, $zero, 222
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sw    $t1, 0($t0)
la    $t0, cruxdata.x
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $zero, 1
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t7, $zero, 4
lw    $t8, 0($sp)
addi    $sp, $sp, 4
mult    $t7, $t8
mflo    $t2
lw    $t3, 0($sp)
addi    $sp, $sp, 4
add    $t4, $t3, $t2
subu    $sp, $sp, 4
sw    $t4, 0($sp)
addi    $t0, $zero, 333
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sw    $t1, 0($t0)
la    $t0, cruxdata.x
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $zero, 2
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t7, $zero, 4
lw    $t8, 0($sp)
addi    $sp, $sp, 4
mult    $t7, $t8
mflo    $t2
lw    $t3, 0($sp)
addi    $sp, $sp, 4
add    $t4, $t3, $t2
subu    $sp, $sp, 4
sw    $t4, 0($sp)
addi    $t0, $zero, 444
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sw    $t1, 0($t0)
la    $t0, cruxdata.canary_begin
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
jal func.printInt
addi    $sp, $sp, 4
la    $t0, cruxdata.x
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $zero, 0
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t7, $zero, 4
lw    $t8, 0($sp)
addi    $sp, $sp, 4
mult    $t7, $t8
mflo    $t2
lw    $t3, 0($sp)
addi    $sp, $sp, 4
add    $t4, $t3, $t2
subu    $sp, $sp, 4
sw    $t4, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
jal func.printInt
addi    $sp, $sp, 4
la    $t0, cruxdata.x
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $zero, 1
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t7, $zero, 4
lw    $t8, 0($sp)
addi    $sp, $sp, 4
mult    $t7, $t8
mflo    $t2
lw    $t3, 0($sp)
addi    $sp, $sp, 4
add    $t4, $t3, $t2
subu    $sp, $sp, 4
sw    $t4, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
jal func.printInt
addi    $sp, $sp, 4
la    $t0, cruxdata.x
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $zero, 2
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t7, $zero, 4
lw    $t8, 0($sp)
addi    $sp, $sp, 4
mult    $t7, $t8
mflo    $t2
lw    $t3, 0($sp)
addi    $sp, $sp, 4
add    $t4, $t3, $t2
subu    $sp, $sp, 4
sw    $t4, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
jal func.printInt
addi    $sp, $sp, 4
la    $t0, cruxdata.canary_end
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
jal func.printInt
addi    $sp, $sp, 4
addi    $sp, $sp, 0
lw    $ra, 4($sp)
lw    $fp, 0($sp)
addi    $sp, $sp, 8
jr    $ra
li    $v0, 10
syscall
                              # END Code Segment
