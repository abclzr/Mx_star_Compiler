	.text

	.globl	main					 # -- Begin function main

	.p2align	2

	.type	main,@function

main:
	addi sp, sp, -20
	sw ra, 0(sp)
main.label_0
	la t1, h
	sw t1, 4(sp)
# Op %4 = h (4 byte)
	call getInt
	sw a0, 8(sp)
# %8 = call (4B) getInt()
	lw t1, 4(sp)
	lw t2, 8(sp)
	sb t2, t1, t3
# Store %4 + 0, %8 (4 byte)
	la t1, h
	lw t2, t1
	sw t2, 12(sp)
# GLoad %12, h (4 byte)
	lw a0, 12(sp)
	call printInt
# call (0B) printInt(%12, )
	li t1, 0
	sw t1, 16(sp)
# Copy %16 0
	lw ra, 0(sp)
	lw a0, 16(sp)
	addi sp, sp, 20
	ret
# Return %16
						 # -- End function

	.section	.sdata,"aw",@progbits

	.globl	h	#@h
	.p2align	2
h:
	.word	0
