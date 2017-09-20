.data                         # BEGIN Data Segment
cruxdata.x: .space 100
cruxdata.num: .space 4
cruxdata.gai: .space 4
cruxdata.gbi: .space 4
cruxdata.gbf: .space 4
cruxdata.gaf: .space 4
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
func.changearray: 

subu    $sp, $sp, 8
sw    $fp, 0($sp)
sw    $ra, 4($sp)
addi    $fp, $sp, 8
subu    $sp, $sp, 12
addi    $t0, $fp, -12
subu    $sp, $sp, 4
sw    $t0, 0($sp)
add    $t0, $zero, $zero
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sw    $t1, 0($t0)
addi    $t0, $fp, -16
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
addi    $t0, $fp, -20
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
la    $t0, cruxdata.num
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
label.0: 

addi    $t0, $fp, -12
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sle    $t2, $t0, $zero
subu    $sp, $sp, 4
sw    $t2, 0($sp)
lw    $t0, 0($sp)
addi    $sp, $sp, 4
beq    $t0, $zero, label.1
la    $t0, cruxdata.x
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $fp, -20
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t7, $zero, 20
lw    $t8, 0($sp)
addi    $sp, $sp, 4
mult    $t7, $t8
mflo    $t2
lw    $t3, 0($sp)
addi    $sp, $sp, 4
add    $t4, $t3, $t2
subu    $sp, $sp, 4
sw    $t4, 0($sp)
addi    $t0, $fp, -16
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
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
la    $t0, cruxdata.num
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sw    $t1, 0($t0)
la    $t0, cruxdata.num
subu    $sp, $sp, 4
sw    $t0, 0($sp)
la    $t0, cruxdata.num
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t0, $zero, 1
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
add    $t2, $t0, $t1
subu    $sp, $sp, 4
sw    $t2, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sw    $t1, 0($t0)
addi    $t0, $fp, -16
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $fp, -16
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t0, $zero, 1
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
add    $t2, $t0, $t1
subu    $sp, $sp, 4
sw    $t2, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sw    $t1, 0($t0)
addi    $t0, $fp, -16
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t0, $zero, 3
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
seq    $t2, $t0, $t1
subu    $sp, $sp, 4
sw    $t2, 0($sp)
lw    $t0, 0($sp)
addi    $sp, $sp, 4
beq    $t0, $zero, label.3
label.2: 

addi    $t0, $fp, -16
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
addi    $t0, $fp, -20
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $fp, -20
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t0, $zero, 1
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
add    $t2, $t0, $t1
subu    $sp, $sp, 4
sw    $t2, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sw    $t1, 0($t0)
j    label.4
label.3: 

label.4: 

addi    $t0, $fp, -20
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t0, $zero, 3
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
seq    $t2, $t0, $t1
subu    $sp, $sp, 4
sw    $t2, 0($sp)
lw    $t0, 0($sp)
addi    $sp, $sp, 4
beq    $t0, $zero, label.6
label.5: 

addi    $t0, $fp, -12
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $zero, 1
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sw    $t1, 0($t0)
j    label.7
label.6: 

label.7: 

j    label.0
label.1: 

addi    $sp, $sp, 12
lw    $ra, 4($sp)
lw    $fp, 0($sp)
addi    $sp, $sp, 8
jr    $ra
func.add1: 

subu    $sp, $sp, 8
sw    $fp, 0($sp)
sw    $ra, 4($sp)
addi    $fp, $sp, 8
subu    $sp, $sp, 0
addi    $t0, $fp, 8
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t0, $fp, 4
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
add    $t2, $t0, $t1
subu    $sp, $sp, 4
sw    $t2, 0($sp)
addi    $t0, $fp, 0
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
add    $t2, $t0, $t1
subu    $sp, $sp, 4
sw    $t2, 0($sp)
lw    $v0, 0($sp)
addi    $sp, $sp, 4
addi    $sp, $sp, 0
lw    $ra, 4($sp)
lw    $fp, 0($sp)
addi    $sp, $sp, 8
jr    $ra
func.sub1: 

subu    $sp, $sp, 8
sw    $fp, 0($sp)
sw    $ra, 4($sp)
addi    $fp, $sp, 8
subu    $sp, $sp, 0
addi    $t0, $fp, 8
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t0, $fp, 4
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sub    $t2, $t0, $t1
subu    $sp, $sp, 4
sw    $t2, 0($sp)
addi    $t0, $fp, 0
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sub    $t2, $t0, $t1
subu    $sp, $sp, 4
sw    $t2, 0($sp)
lw    $v0, 0($sp)
addi    $sp, $sp, 4
addi    $sp, $sp, 0
lw    $ra, 4($sp)
lw    $fp, 0($sp)
addi    $sp, $sp, 8
jr    $ra
func.mul1: 

subu    $sp, $sp, 8
sw    $fp, 0($sp)
sw    $ra, 4($sp)
addi    $fp, $sp, 8
subu    $sp, $sp, 0
addi    $t0, $fp, 8
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t0, $fp, 4
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
mult    $t0, $t1
mflo $t2
subu    $sp, $sp, 4
sw    $t2, 0($sp)
addi    $t0, $fp, 0
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
mult    $t0, $t1
mflo $t2
subu    $sp, $sp, 4
sw    $t2, 0($sp)
lw    $v0, 0($sp)
addi    $sp, $sp, 4
addi    $sp, $sp, 0
lw    $ra, 4($sp)
lw    $fp, 0($sp)
addi    $sp, $sp, 8
jr    $ra
func.div1: 

subu    $sp, $sp, 8
sw    $fp, 0($sp)
sw    $ra, 4($sp)
addi    $fp, $sp, 8
subu    $sp, $sp, 0
addi    $t0, $fp, 8
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t0, $fp, 4
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
div    $t2, $t0, $t1
subu    $sp, $sp, 4
sw    $t2, 0($sp)
addi    $t0, $fp, 0
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
div    $t2, $t0, $t1
subu    $sp, $sp, 4
sw    $t2, 0($sp)
lw    $v0, 0($sp)
addi    $sp, $sp, 4
addi    $sp, $sp, 0
lw    $ra, 4($sp)
lw    $fp, 0($sp)
addi    $sp, $sp, 8
jr    $ra
func.add2: 

subu    $sp, $sp, 8
sw    $fp, 0($sp)
sw    $ra, 4($sp)
addi    $fp, $sp, 8
subu    $sp, $sp, 0
addi    $t0, $fp, 8
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
addi    $t0, $fp, 4
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
l.s    $f1, 0($sp)
addi    $sp, $sp, 4
l.s    $f0, 0($sp)
addi    $sp, $sp, 4
add.s    $f2, $f0, $f1
subu    $sp, $sp, 4
s.s    $f2, 0($sp)
addi    $t0, $fp, 0
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
l.s    $f1, 0($sp)
addi    $sp, $sp, 4
l.s    $f0, 0($sp)
addi    $sp, $sp, 4
add.s    $f2, $f0, $f1
subu    $sp, $sp, 4
s.s    $f2, 0($sp)
lw    $v0, 0($sp)
addi    $sp, $sp, 4
addi    $sp, $sp, 0
lw    $ra, 4($sp)
lw    $fp, 0($sp)
addi    $sp, $sp, 8
jr    $ra
func.sub2: 

subu    $sp, $sp, 8
sw    $fp, 0($sp)
sw    $ra, 4($sp)
addi    $fp, $sp, 8
subu    $sp, $sp, 0
addi    $t0, $fp, 8
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
addi    $t0, $fp, 4
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
l.s    $f1, 0($sp)
addi    $sp, $sp, 4
l.s    $f0, 0($sp)
addi    $sp, $sp, 4
sub.s    $f2, $f0, $f1
subu    $sp, $sp, 4
s.s    $f2, 0($sp)
addi    $t0, $fp, 0
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
l.s    $f1, 0($sp)
addi    $sp, $sp, 4
l.s    $f0, 0($sp)
addi    $sp, $sp, 4
sub.s    $f2, $f0, $f1
subu    $sp, $sp, 4
s.s    $f2, 0($sp)
lw    $v0, 0($sp)
addi    $sp, $sp, 4
addi    $sp, $sp, 0
lw    $ra, 4($sp)
lw    $fp, 0($sp)
addi    $sp, $sp, 8
jr    $ra
func.mul2: 

subu    $sp, $sp, 8
sw    $fp, 0($sp)
sw    $ra, 4($sp)
addi    $fp, $sp, 8
subu    $sp, $sp, 0
addi    $t0, $fp, 8
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
addi    $t0, $fp, 4
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
l.s    $f1, 0($sp)
addi    $sp, $sp, 4
l.s    $f0, 0($sp)
addi    $sp, $sp, 4
mul.s    $f2, $f0, $f1
subu    $sp, $sp, 4
s.s    $f2, 0($sp)
addi    $t0, $fp, 0
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
l.s    $f1, 0($sp)
addi    $sp, $sp, 4
l.s    $f0, 0($sp)
addi    $sp, $sp, 4
mul.s    $f2, $f0, $f1
subu    $sp, $sp, 4
s.s    $f2, 0($sp)
lw    $v0, 0($sp)
addi    $sp, $sp, 4
addi    $sp, $sp, 0
lw    $ra, 4($sp)
lw    $fp, 0($sp)
addi    $sp, $sp, 8
jr    $ra
func.div2: 

subu    $sp, $sp, 8
sw    $fp, 0($sp)
sw    $ra, 4($sp)
addi    $fp, $sp, 8
subu    $sp, $sp, 0
addi    $t0, $fp, 8
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
addi    $t0, $fp, 4
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
l.s    $f1, 0($sp)
addi    $sp, $sp, 4
l.s    $f0, 0($sp)
addi    $sp, $sp, 4
div.s    $f2, $f0, $f1
subu    $sp, $sp, 4
s.s    $f2, 0($sp)
addi    $t0, $fp, 0
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
l.s    $f1, 0($sp)
addi    $sp, $sp, 4
l.s    $f0, 0($sp)
addi    $sp, $sp, 4
div.s    $f2, $f0, $f1
subu    $sp, $sp, 4
s.s    $f2, 0($sp)
lw    $v0, 0($sp)
addi    $sp, $sp, 4
addi    $sp, $sp, 0
lw    $ra, 4($sp)
lw    $fp, 0($sp)
addi    $sp, $sp, 8
jr    $ra
func.bools: 

subu    $sp, $sp, 8
sw    $fp, 0($sp)
sw    $ra, 4($sp)
addi    $fp, $sp, 8
subu    $sp, $sp, 0
la    $t0, cruxdata.gai
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
la    $t0, cruxdata.gbi
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sgt    $t2, $t0, $t1
subu    $sp, $sp, 4
sw    $t2, 0($sp)
jal func.printBool
addi    $sp, $sp, 4
jal func.println
la    $t0, cruxdata.gai
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
la    $t0, cruxdata.gbi
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sge    $t2, $t0, $t1
subu    $sp, $sp, 4
sw    $t2, 0($sp)
jal func.printBool
addi    $sp, $sp, 4
jal func.println
la    $t0, cruxdata.gai
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
la    $t0, cruxdata.gbi
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
slt    $t2, $t0, $t1
subu    $sp, $sp, 4
sw    $t2, 0($sp)
jal func.printBool
addi    $sp, $sp, 4
jal func.println
la    $t0, cruxdata.gai
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
la    $t0, cruxdata.gbi
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sle    $t2, $t0, $t1
subu    $sp, $sp, 4
sw    $t2, 0($sp)
jal func.printBool
addi    $sp, $sp, 4
jal func.println
la    $t0, cruxdata.gai
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
la    $t0, cruxdata.gbi
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
seq    $t2, $t0, $t1
subu    $sp, $sp, 4
sw    $t2, 0($sp)
jal func.printBool
addi    $sp, $sp, 4
jal func.println
la    $t0, cruxdata.gai
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
la    $t0, cruxdata.gbi
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sne    $t2, $t0, $t1
subu    $sp, $sp, 4
sw    $t2, 0($sp)
jal func.printBool
addi    $sp, $sp, 4
jal func.println
jal func.println
la    $t0, cruxdata.gaf
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
la    $t0, cruxdata.gbf
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
l.s    $f1, 0($sp)
addi    $sp, $sp, 4
l.s    $f0, 0($sp)
addi    $sp, $sp, 4
c.le.s    $f0, $f1
bc1t    label.9
bc1f    label.8
label.8:
addi    $t4, $zero, 1
j    label.10
label.9:
add    $t4, $zero, $zero
label.10:
subu    $sp, $sp, 4
sw    $t4, 0($sp)
jal func.printBool
addi    $sp, $sp, 4
jal func.println
la    $t0, cruxdata.gaf
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
la    $t0, cruxdata.gbf
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
l.s    $f1, 0($sp)
addi    $sp, $sp, 4
l.s    $f0, 0($sp)
addi    $sp, $sp, 4
c.lt.s    $f0, $f1
bc1t    label.12
bc1f    label.11
label.11:
addi    $t4, $zero, 1
j    label.13
label.12:
add    $t4, $zero, $zero
label.13:
subu    $sp, $sp, 4
sw    $t4, 0($sp)
jal func.printBool
addi    $sp, $sp, 4
jal func.println
la    $t0, cruxdata.gaf
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
la    $t0, cruxdata.gbf
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
l.s    $f1, 0($sp)
addi    $sp, $sp, 4
l.s    $f0, 0($sp)
addi    $sp, $sp, 4
c.lt.s    $f0, $f1
bc1t    label.14
bc1f    label.15
label.14:
addi    $t4, $zero, 1
j    label.16
label.15:
add    $t4, $zero, $zero
label.16:
subu    $sp, $sp, 4
sw    $t4, 0($sp)
jal func.printBool
addi    $sp, $sp, 4
jal func.println
la    $t0, cruxdata.gaf
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
la    $t0, cruxdata.gbf
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
l.s    $f1, 0($sp)
addi    $sp, $sp, 4
l.s    $f0, 0($sp)
addi    $sp, $sp, 4
c.le.s    $f0, $f1
bc1t    label.17
bc1f    label.18
label.17:
addi    $t4, $zero, 1
j    label.19
label.18:
add    $t4, $zero, $zero
label.19:
subu    $sp, $sp, 4
sw    $t4, 0($sp)
jal func.printBool
addi    $sp, $sp, 4
jal func.println
la    $t0, cruxdata.gaf
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
la    $t0, cruxdata.gbf
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
l.s    $f1, 0($sp)
addi    $sp, $sp, 4
l.s    $f0, 0($sp)
addi    $sp, $sp, 4
c.eq.s    $f0, $f1
bc1t    label.20
bc1f    label.21
label.20:
addi    $t4, $zero, 1
j    label.22
label.21:
add    $t4, $zero, $zero
label.22:
subu    $sp, $sp, 4
sw    $t4, 0($sp)
jal func.printBool
addi    $sp, $sp, 4
jal func.println
la    $t0, cruxdata.gaf
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
la    $t0, cruxdata.gbf
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
l.s    $f1, 0($sp)
addi    $sp, $sp, 4
l.s    $f0, 0($sp)
addi    $sp, $sp, 4
c.eq.s    $f0, $f1
bc1t    label.24
bc1f    label.23
label.23:
addi    $t4, $zero, 1
j    label.25
label.24:
add    $t4, $zero, $zero
label.25:
subu    $sp, $sp, 4
sw    $t4, 0($sp)
jal func.printBool
addi    $sp, $sp, 4
jal func.println
addi    $sp, $sp, 0
lw    $ra, 4($sp)
lw    $fp, 0($sp)
addi    $sp, $sp, 8
jr    $ra
main:
subu    $sp, $sp, 8
sw    $fp, 0($sp)
sw    $ra, 4($sp)
addi    $fp, $sp, 8
subu    $sp, $sp, 32
jal func.changearray
addi    $t0, $fp, -12
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
addi    $t0, $fp, -16
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
label.26: 

addi    $t0, $fp, -16
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t0, $zero, 5
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
slt    $t2, $t0, $t1
subu    $sp, $sp, 4
sw    $t2, 0($sp)
lw    $t0, 0($sp)
addi    $sp, $sp, 4
beq    $t0, $zero, label.27
label.28: 

addi    $t0, $fp, -12
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t0, $zero, 5
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
slt    $t2, $t0, $t1
subu    $sp, $sp, 4
sw    $t2, 0($sp)
lw    $t0, 0($sp)
addi    $sp, $sp, 4
beq    $t0, $zero, label.29
la    $t0, cruxdata.x
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $fp, -12
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t7, $zero, 20
lw    $t8, 0($sp)
addi    $sp, $sp, 4
mult    $t7, $t8
mflo    $t2
lw    $t3, 0($sp)
addi    $sp, $sp, 4
add    $t4, $t3, $t2
subu    $sp, $sp, 4
sw    $t4, 0($sp)
addi    $t0, $fp, -16
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
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
addi    $t0, $fp, -12
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $fp, -12
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t0, $zero, 1
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
add    $t2, $t0, $t1
subu    $sp, $sp, 4
sw    $t2, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sw    $t1, 0($t0)
j    label.28
label.29: 

jal func.println
addi    $t0, $fp, -12
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
addi    $t0, $fp, -16
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $fp, -16
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t0, $zero, 1
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
add    $t2, $t0, $t1
subu    $sp, $sp, 4
sw    $t2, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sw    $t1, 0($t0)
j    label.26
label.27: 

jal func.println
jal func.println
addi    $t0, $fp, -20
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $zero, 150
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sw    $t1, 0($t0)
addi    $t0, $fp, -24
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $zero, 50
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sw    $t1, 0($t0)
addi    $t0, $fp, -28
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $zero, 30
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sw    $t1, 0($t0)
addi    $t0, $fp, -32
subu    $sp, $sp, 4
sw    $t0, 0($sp)
li.s    $f0, 25.0
subu    $sp, $sp, 4
s.s    $f0, 0($sp)
l.s    $f1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
s.s    $f1, 0($t0)
addi    $t0, $fp, -36
subu    $sp, $sp, 4
sw    $t0, 0($sp)
li.s    $f0, 4.0
subu    $sp, $sp, 4
s.s    $f0, 0($sp)
l.s    $f1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
s.s    $f1, 0($t0)
addi    $t0, $fp, -40
subu    $sp, $sp, 4
sw    $t0, 0($sp)
li.s    $f0, 5.786097
subu    $sp, $sp, 4
s.s    $f0, 0($sp)
l.s    $f1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
s.s    $f1, 0($t0)
addi    $t0, $fp, -20
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t0, $fp, -24
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t0, $fp, -28
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
jal func.add1
addi    $sp, $sp, 4
addi    $sp, $sp, 4
addi    $sp, $sp, 4
subu    $sp, $sp, 4
sw    $v0, 0($sp)
jal func.printInt
addi    $sp, $sp, 4
jal func.println
addi    $t0, $fp, -20
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t0, $fp, -24
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t0, $fp, -28
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
jal func.sub1
addi    $sp, $sp, 4
addi    $sp, $sp, 4
addi    $sp, $sp, 4
subu    $sp, $sp, 4
sw    $v0, 0($sp)
jal func.printInt
addi    $sp, $sp, 4
jal func.println
addi    $t0, $fp, -20
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t0, $fp, -24
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t0, $fp, -28
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
jal func.mul1
addi    $sp, $sp, 4
addi    $sp, $sp, 4
addi    $sp, $sp, 4
subu    $sp, $sp, 4
sw    $v0, 0($sp)
jal func.printInt
addi    $sp, $sp, 4
jal func.println
addi    $t0, $fp, -20
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t0, $fp, -24
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
addi    $t0, $fp, -28
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
lw    $t5, 0($t6)
subu    $sp, $sp, 4
sw    $t5, 0($sp)
jal func.div1
addi    $sp, $sp, 4
addi    $sp, $sp, 4
addi    $sp, $sp, 4
subu    $sp, $sp, 4
sw    $v0, 0($sp)
jal func.printInt
addi    $sp, $sp, 4
jal func.println
addi    $t0, $fp, -32
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
addi    $t0, $fp, -36
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
addi    $t0, $fp, -40
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
jal func.add2
addi    $sp, $sp, 4
addi    $sp, $sp, 4
addi    $sp, $sp, 4
subu    $sp, $sp, 4
s.s    $v0, 0($sp)
jal func.printFloat
addi    $sp, $sp, 4
jal func.println
addi    $t0, $fp, -32
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
addi    $t0, $fp, -36
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
addi    $t0, $fp, -40
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
jal func.sub2
addi    $sp, $sp, 4
addi    $sp, $sp, 4
addi    $sp, $sp, 4
subu    $sp, $sp, 4
s.s    $v0, 0($sp)
jal func.printFloat
addi    $sp, $sp, 4
jal func.println
addi    $t0, $fp, -32
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
addi    $t0, $fp, -36
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
addi    $t0, $fp, -40
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
jal func.mul2
addi    $sp, $sp, 4
addi    $sp, $sp, 4
addi    $sp, $sp, 4
subu    $sp, $sp, 4
s.s    $v0, 0($sp)
jal func.printFloat
addi    $sp, $sp, 4
jal func.println
addi    $t0, $fp, -32
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
addi    $t0, $fp, -36
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
addi    $t0, $fp, -40
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t6, 0($sp)
addi   $sp, $sp, 4
l.s    $f5, 0($t6)
subu    $sp, $sp, 4
s.s    $f5, 0($sp)
jal func.div2
addi    $sp, $sp, 4
addi    $sp, $sp, 4
addi    $sp, $sp, 4
subu    $sp, $sp, 4
s.s    $v0, 0($sp)
jal func.printFloat
addi    $sp, $sp, 4
jal func.println
jal func.println
addi    $t0, $zero, 4
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $zero, 5
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $zero, 6
subu    $sp, $sp, 4
sw    $t0, 0($sp)
jal func.mul1
addi    $sp, $sp, 4
addi    $sp, $sp, 4
addi    $sp, $sp, 4
subu    $sp, $sp, 4
sw    $v0, 0($sp)
jal func.printInt
addi    $sp, $sp, 4
jal func.println
li.s    $f0, 1.5
subu    $sp, $sp, 4
s.s    $f0, 0($sp)
li.s    $f0, 1.5
subu    $sp, $sp, 4
s.s    $f0, 0($sp)
li.s    $f0, 4.0
subu    $sp, $sp, 4
s.s    $f0, 0($sp)
jal func.mul2
addi    $sp, $sp, 4
addi    $sp, $sp, 4
addi    $sp, $sp, 4
subu    $sp, $sp, 4
s.s    $v0, 0($sp)
jal func.printFloat
addi    $sp, $sp, 4
la    $t0, cruxdata.gai
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $zero, 2
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sw    $t1, 0($t0)
la    $t0, cruxdata.gbi
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $zero, 1
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sw    $t1, 0($t0)
la    $t0, cruxdata.gaf
subu    $sp, $sp, 4
sw    $t0, 0($sp)
li.s    $f0, 2.0
subu    $sp, $sp, 4
s.s    $f0, 0($sp)
l.s    $f1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
s.s    $f1, 0($t0)
la    $t0, cruxdata.gbf
subu    $sp, $sp, 4
sw    $t0, 0($sp)
li.s    $f0, 1.0
subu    $sp, $sp, 4
s.s    $f0, 0($sp)
l.s    $f1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
s.s    $f1, 0($t0)
jal func.bools
la    $t0, cruxdata.gbi
subu    $sp, $sp, 4
sw    $t0, 0($sp)
addi    $t0, $zero, 2
subu    $sp, $sp, 4
sw    $t0, 0($sp)
lw    $t1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
sw    $t1, 0($t0)
la    $t0, cruxdata.gbf
subu    $sp, $sp, 4
sw    $t0, 0($sp)
li.s    $f0, 2.0
subu    $sp, $sp, 4
s.s    $f0, 0($sp)
l.s    $f1, 0($sp)
addi    $sp, $sp, 4
lw    $t0, 0($sp)
addi    $sp, $sp, 4
s.s    $f1, 0($t0)
jal func.bools
addi    $sp, $sp, 32
lw    $ra, 4($sp)
lw    $fp, 0($sp)
addi    $sp, $sp, 8
jr    $ra
li    $v0, 10
syscall
                              # END Code Segment
