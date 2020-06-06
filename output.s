	.text

	.globl	f					 # -- Begin function f

	.p2align	2

	.type	f,@function

f:
	addi sp, sp, -4
	sw ra, 0(sp)
f.label_0:
	lw ra, 0(sp)
	mv a0, x0
	addi sp, sp, 4
	ret
# Return
						 # -- End function

	.globl	main					 # -- Begin function main

	.p2align	2

	.type	main,@function

main:
	addi sp, sp, -8
	sw ra, 0(sp)
main.label_0:
	call f
# call (0B) f()
	li t1, 0
	sw t1, 4(sp)
# Copy %4 0
	lw ra, 0(sp)
	lw a0, 4(sp)
	addi sp, sp, 8
	ret
# Return %4
	lw ra, 0(sp)
	mv a0, x0
	addi sp, sp, 8
	ret
# Return
						 # -- End function

	.section	.sdata,"aw",@progbits

