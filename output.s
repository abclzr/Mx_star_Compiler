	.text

	.globl	main					 # -- Begin function main

	.p2align	2

	.type	main,@function

main:
	addi sp, sp, -496
	sw ra, 0(sp)
main.label_0:
	li t1, 10005
	sw t1, 4(sp)
# Copy %4 10005
	la t1, MAXN
	lw t2, 4(sp)
	sw t2, 0(t1)
# GStore MAXN, %4 (4 byte)
	li t1, 10005
	sw t1, 8(sp)
# Copy %8 10005
	la t1, MAXM
	lw t2, 8(sp)
	sw t2, 0(t1)
# GStore MAXM, %8 (4 byte)
	sw x0, 12(sp)
# Store sp + 12, 0 (4 byte)
	sw x0, 16(sp)
# Store sp + 16, 0 (4 byte)
	addi t1, sp, 12
	sw t1, 20(sp)
# Op %20 = sp + 12 (4 byte)
	call getInt
	sw a0, 24(sp)
# %24 = call (4B) getInt()
	lw t1, 20(sp)
	lw t2, 24(sp)
	sw t2, 0(t1)
# Store %20 + 0, %24 (4 byte)
	addi t1, sp, 16
	sw t1, 28(sp)
# Op %28 = sp + 16 (4 byte)
	call getInt
	sw a0, 32(sp)
# %32 = call (4B) getInt()
	lw t1, 28(sp)
	lw t2, 32(sp)
	sw t2, 0(t1)
# Store %28 + 0, %32 (4 byte)
	la t1, MAXN
	lw t2, 0(t1)
	sw t2, 40(sp)
# GLoad %40, MAXN (4 byte)
	li t1, 4
	sw t1, 52(sp)
# Copy %52 4
	lw t1, 40(sp)
	lw t2, 52(sp)
	mul t3, t1, t2
	sw t3, 44(sp)
# %44 = %40 * %52
	lw t1, 44(sp)
	addi t3, t1, 4
	sw t3, 48(sp)
# %48 = %44 + 4
	lw a0, 48(sp)
	call malloc
	sw a0, 56(sp)
# %56 = malloc(%48)
	lw t1, 56(sp)
	lw t2, 40(sp)
	sw t2, 0(t1)
# Store %56 + 0, %40 (4 byte)
	lw t1, 56(sp)
	addi t3, t1, 4
	sw t3, 56(sp)
# %56 = %56 + 4
	lw t1, 56(sp)
	sw t1, 60(sp)
# Copy %60 %56
	lw t1, 60(sp)
	lw t2, 44(sp)
	add t3, t1, t2
	sw t3, 64(sp)
# %64 = %60 + %44
main.label_1:
	li t1, 0
	sw t1, 68(sp)
# Copy %68 0
	lw t1, 60(sp)
	lw t2, 68(sp)
	sw t2, 0(t1)
# Store %60 + 0, %68 (4 byte)
	lw t1, 60(sp)
	lw t2, 52(sp)
	add t3, t1, t2
	sw t3, 60(sp)
# %60 = %60 + %52
	lw t1, 60(sp)
	lw t2, 64(sp)
	bgt t2, t1, main.label_1
# if (%60 < %64) go to main.label_1
main.label_2:
	lw t1, 56(sp)
	sw t1, 36(sp)
# Store sp + 36, %56 (4 byte)
	la t1, MAXN
	lw t2, 0(t1)
	sw t2, 76(sp)
# GLoad %76, MAXN (4 byte)
	li t1, 4
	sw t1, 88(sp)
# Copy %88 4
	lw t1, 76(sp)
	lw t2, 88(sp)
	mul t3, t1, t2
	sw t3, 80(sp)
# %80 = %76 * %88
	lw t1, 80(sp)
	addi t3, t1, 4
	sw t3, 84(sp)
# %84 = %80 + 4
	lw a0, 84(sp)
	call malloc
	sw a0, 92(sp)
# %92 = malloc(%84)
	lw t1, 92(sp)
	lw t2, 76(sp)
	sw t2, 0(t1)
# Store %92 + 0, %76 (4 byte)
	lw t1, 92(sp)
	addi t3, t1, 4
	sw t3, 92(sp)
# %92 = %92 + 4
	lw t1, 92(sp)
	sw t1, 96(sp)
# Copy %96 %92
	lw t1, 96(sp)
	lw t2, 80(sp)
	add t3, t1, t2
	sw t3, 100(sp)
# %100 = %96 + %80
main.label_3:
	li t1, 0
	sw t1, 104(sp)
# Copy %104 0
	lw t1, 96(sp)
	lw t2, 104(sp)
	sw t2, 0(t1)
# Store %96 + 0, %104 (4 byte)
	lw t1, 96(sp)
	lw t2, 88(sp)
	add t3, t1, t2
	sw t3, 96(sp)
# %96 = %96 + %88
	lw t1, 96(sp)
	lw t2, 100(sp)
	bgt t2, t1, main.label_3
# if (%96 < %100) go to main.label_3
main.label_4:
	lw t1, 92(sp)
	sw t1, 72(sp)
# Store sp + 72, %92 (4 byte)
	la t1, MAXN
	lw t2, 0(t1)
	sw t2, 112(sp)
# GLoad %112, MAXN (4 byte)
	li t1, 4
	sw t1, 124(sp)
# Copy %124 4
	lw t1, 112(sp)
	lw t2, 124(sp)
	mul t3, t1, t2
	sw t3, 116(sp)
# %116 = %112 * %124
	lw t1, 116(sp)
	addi t3, t1, 4
	sw t3, 120(sp)
# %120 = %116 + 4
	lw a0, 120(sp)
	call malloc
	sw a0, 128(sp)
# %128 = malloc(%120)
	lw t1, 128(sp)
	lw t2, 112(sp)
	sw t2, 0(t1)
# Store %128 + 0, %112 (4 byte)
	lw t1, 128(sp)
	addi t3, t1, 4
	sw t3, 128(sp)
# %128 = %128 + 4
	lw t1, 128(sp)
	sw t1, 132(sp)
# Copy %132 %128
	lw t1, 132(sp)
	lw t2, 116(sp)
	add t3, t1, t2
	sw t3, 136(sp)
# %136 = %132 + %116
main.label_5:
	li t1, 0
	sw t1, 140(sp)
# Copy %140 0
	lw t1, 132(sp)
	lw t2, 140(sp)
	sw t2, 0(t1)
# Store %132 + 0, %140 (4 byte)
	lw t1, 132(sp)
	lw t2, 124(sp)
	add t3, t1, t2
	sw t3, 132(sp)
# %132 = %132 + %124
	lw t1, 132(sp)
	lw t2, 136(sp)
	bgt t2, t1, main.label_5
# if (%132 < %136) go to main.label_5
main.label_6:
	lw t1, 128(sp)
	sw t1, 108(sp)
# Store sp + 108, %128 (4 byte)
	sw x0, 144(sp)
# Store sp + 144, 0 (4 byte)
	sw x0, 148(sp)
# Store sp + 148, 0 (4 byte)
	addi t1, sp, 144
	sw t1, 152(sp)
# Op %152 = sp + 144 (4 byte)
	li t1, 0
	sw t1, 156(sp)
# Copy %156 0
	lw t1, 152(sp)
	lw t2, 156(sp)
	sw t2, 0(t1)
# Store %152 + 0, %156 (4 byte)
main.label_7:
	lw t1, 144(sp)
	sw t1, 160(sp)
# Load %160, sp + 144 (4 byte)
	lw t1, 16(sp)
	sw t1, 164(sp)
# Load %164, sp + 16 (4 byte)
	lw t1, 160(sp)
	lw t2, 164(sp)
	slt t3, t1, t2
	sb t3, 168(sp)
# %168 = %160 < %164
	lb t1, 168(sp)
	beqz t1, main.label_10
# if (%168 == false) go to main.label_10
main.label_8:
	lw t1, 36(sp)
	sw t1, 169(sp)
# Load %169, sp + 36 (4 byte)
	lw t1, 144(sp)
	sw t1, 173(sp)
# Load %173, sp + 144 (4 byte)
	lw t1, 173(sp)
	li t2, 4
	mul t3, t1, t2
	sw t3, 177(sp)
# %177 = %173 * 4
	lw t1, 169(sp)
	lw t2, 177(sp)
	add t3, t1, t2
	sw t3, 181(sp)
# %181 = %169 + %177
	call getInt
	sw a0, 185(sp)
# %185 = call (4B) getInt()
	lw t1, 181(sp)
	lw t2, 185(sp)
	sw t2, 0(t1)
# Store %181 + 0, %185 (4 byte)
	lw t1, 72(sp)
	sw t1, 189(sp)
# Load %189, sp + 72 (4 byte)
	lw t1, 144(sp)
	sw t1, 193(sp)
# Load %193, sp + 144 (4 byte)
	lw t1, 193(sp)
	li t2, 4
	mul t3, t1, t2
	sw t3, 197(sp)
# %197 = %193 * 4
	lw t1, 189(sp)
	lw t2, 197(sp)
	add t3, t1, t2
	sw t3, 201(sp)
# %201 = %189 + %197
	call getInt
	sw a0, 205(sp)
# %205 = call (4B) getInt()
	lw t1, 201(sp)
	lw t2, 205(sp)
	sw t2, 0(t1)
# Store %201 + 0, %205 (4 byte)
main.label_9:
	addi t1, sp, 144
	sw t1, 209(sp)
# Op %209 = sp + 144 (4 byte)
	lw t1, 209(sp)
	lw t2, 0(t1)
	sw t2, 213(sp)
# Load %213, %209 + 0 (4 byte)
	lw t1, 213(sp)
	addi t3, t1, 1
	sw t3, 217(sp)
# %217 = %213 + 1
	lw t1, 209(sp)
	lw t2, 217(sp)
	sw t2, 0(t1)
# Store %209 + 0, %217 (4 byte)
	j main.label_7
# go to main.label_7
main.label_10:
	addi t1, sp, 144
	sw t1, 221(sp)
# Op %221 = sp + 144 (4 byte)
	li t1, 0
	sw t1, 225(sp)
# Copy %225 0
	lw t1, 221(sp)
	lw t2, 225(sp)
	sw t2, 0(t1)
# Store %221 + 0, %225 (4 byte)
main.label_11:
	lw t1, 144(sp)
	sw t1, 229(sp)
# Load %229, sp + 144 (4 byte)
	lw t1, 16(sp)
	sw t1, 233(sp)
# Load %233, sp + 16 (4 byte)
	lw t1, 229(sp)
	lw t2, 233(sp)
	slt t3, t1, t2
	sb t3, 237(sp)
# %237 = %229 < %233
	lb t1, 237(sp)
	beqz t1, main.label_14
# if (%237 == false) go to main.label_14
main.label_12:
	addi t1, sp, 148
	sw t1, 238(sp)
# Op %238 = sp + 148 (4 byte)
	lw t1, 72(sp)
	sw t1, 242(sp)
# Load %242, sp + 72 (4 byte)
	lw t1, 144(sp)
	sw t1, 246(sp)
# Load %246, sp + 144 (4 byte)
	lw t1, 246(sp)
	li t2, 4
	mul t3, t1, t2
	sw t3, 250(sp)
# %250 = %246 * 4
	lw t1, 242(sp)
	lw t2, 250(sp)
	add t3, t1, t2
	sw t3, 254(sp)
# %254 = %242 + %250
	lw t1, 254(sp)
	lw t2, 0(t1)
	sw t2, 258(sp)
# Load %258, %254 + 0 (4 byte)
	lw t1, 238(sp)
	lw t2, 258(sp)
	sw t2, 0(t1)
# Store %238 + 0, %258 (4 byte)
main.label_15:
	lw t1, 148(sp)
	sw t1, 262(sp)
# Load %262, sp + 148 (4 byte)
	lw t1, 12(sp)
	sw t1, 266(sp)
# Load %266, sp + 12 (4 byte)
	lw t1, 262(sp)
	lw t2, 266(sp)
	slt t3, t2, t1
	xori t3, t3, 1
	sb t3, 270(sp)
# %270 = %262 <= %266
	lb t1, 270(sp)
	beqz t1, main.label_18
# if (%270 == false) go to main.label_18
main.label_16:
	lw t1, 108(sp)
	sw t1, 271(sp)
# Load %271, sp + 108 (4 byte)
	lw t1, 148(sp)
	sw t1, 275(sp)
# Load %275, sp + 148 (4 byte)
	lw t1, 275(sp)
	li t2, 4
	mul t3, t1, t2
	sw t3, 279(sp)
# %279 = %275 * 4
	lw t1, 271(sp)
	lw t2, 279(sp)
	add t3, t1, t2
	sw t3, 283(sp)
# %283 = %271 + %279
	lw t1, 283(sp)
	lw t2, 0(t1)
	sw t2, 287(sp)
# Load %287, %283 + 0 (4 byte)
	lw t1, 108(sp)
	sw t1, 291(sp)
# Load %291, sp + 108 (4 byte)
	lw t1, 148(sp)
	sw t1, 295(sp)
# Load %295, sp + 148 (4 byte)
	lw t1, 72(sp)
	sw t1, 299(sp)
# Load %299, sp + 72 (4 byte)
	lw t1, 144(sp)
	sw t1, 303(sp)
# Load %303, sp + 144 (4 byte)
	lw t1, 303(sp)
	li t2, 4
	mul t3, t1, t2
	sw t3, 307(sp)
# %307 = %303 * 4
	lw t1, 299(sp)
	lw t2, 307(sp)
	add t3, t1, t2
	sw t3, 311(sp)
# %311 = %299 + %307
	lw t1, 311(sp)
	lw t2, 0(t1)
	sw t2, 315(sp)
# Load %315, %311 + 0 (4 byte)
	lw t1, 295(sp)
	lw t2, 315(sp)
	sub t3, t1, t2
	sw t3, 319(sp)
# %319 = %295 - %315
	lw t1, 319(sp)
	li t2, 4
	mul t3, t1, t2
	sw t3, 323(sp)
# %323 = %319 * 4
	lw t1, 291(sp)
	lw t2, 323(sp)
	add t3, t1, t2
	sw t3, 327(sp)
# %327 = %291 + %323
	lw t1, 327(sp)
	lw t2, 0(t1)
	sw t2, 331(sp)
# Load %331, %327 + 0 (4 byte)
	lw t1, 36(sp)
	sw t1, 335(sp)
# Load %335, sp + 36 (4 byte)
	lw t1, 144(sp)
	sw t1, 339(sp)
# Load %339, sp + 144 (4 byte)
	lw t1, 339(sp)
	li t2, 4
	mul t3, t1, t2
	sw t3, 343(sp)
# %343 = %339 * 4
	lw t1, 335(sp)
	lw t2, 343(sp)
	add t3, t1, t2
	sw t3, 347(sp)
# %347 = %335 + %343
	lw t1, 347(sp)
	lw t2, 0(t1)
	sw t2, 351(sp)
# Load %351, %347 + 0 (4 byte)
	lw t1, 331(sp)
	lw t2, 351(sp)
	add t3, t1, t2
	sw t3, 355(sp)
# %355 = %331 + %351
	lw t1, 287(sp)
	lw t2, 355(sp)
	slt t3, t2, t1
	xori t3, t3, 1
	sb t3, 359(sp)
# %359 = %287 <= %355
	lb t1, 359(sp)
	beqz t1, main.label_20
# if (%359 == false) go to main.label_20
main.label_19:
	lw t1, 108(sp)
	sw t1, 360(sp)
# Load %360, sp + 108 (4 byte)
	lw t1, 148(sp)
	sw t1, 364(sp)
# Load %364, sp + 148 (4 byte)
	lw t1, 364(sp)
	li t2, 4
	mul t3, t1, t2
	sw t3, 368(sp)
# %368 = %364 * 4
	lw t1, 360(sp)
	lw t2, 368(sp)
	add t3, t1, t2
	sw t3, 372(sp)
# %372 = %360 + %368
	lw t1, 108(sp)
	sw t1, 376(sp)
# Load %376, sp + 108 (4 byte)
	lw t1, 148(sp)
	sw t1, 380(sp)
# Load %380, sp + 148 (4 byte)
	lw t1, 72(sp)
	sw t1, 384(sp)
# Load %384, sp + 72 (4 byte)
	lw t1, 144(sp)
	sw t1, 388(sp)
# Load %388, sp + 144 (4 byte)
	lw t1, 388(sp)
	li t2, 4
	mul t3, t1, t2
	sw t3, 392(sp)
# %392 = %388 * 4
	lw t1, 384(sp)
	lw t2, 392(sp)
	add t3, t1, t2
	sw t3, 396(sp)
# %396 = %384 + %392
	lw t1, 396(sp)
	lw t2, 0(t1)
	sw t2, 400(sp)
# Load %400, %396 + 0 (4 byte)
	lw t1, 380(sp)
	lw t2, 400(sp)
	sub t3, t1, t2
	sw t3, 404(sp)
# %404 = %380 - %400
	lw t1, 404(sp)
	li t2, 4
	mul t3, t1, t2
	sw t3, 408(sp)
# %408 = %404 * 4
	lw t1, 376(sp)
	lw t2, 408(sp)
	add t3, t1, t2
	sw t3, 412(sp)
# %412 = %376 + %408
	lw t1, 412(sp)
	lw t2, 0(t1)
	sw t2, 416(sp)
# Load %416, %412 + 0 (4 byte)
	lw t1, 36(sp)
	sw t1, 420(sp)
# Load %420, sp + 36 (4 byte)
	lw t1, 144(sp)
	sw t1, 424(sp)
# Load %424, sp + 144 (4 byte)
	lw t1, 424(sp)
	li t2, 4
	mul t3, t1, t2
	sw t3, 428(sp)
# %428 = %424 * 4
	lw t1, 420(sp)
	lw t2, 428(sp)
	add t3, t1, t2
	sw t3, 432(sp)
# %432 = %420 + %428
	lw t1, 432(sp)
	lw t2, 0(t1)
	sw t2, 436(sp)
# Load %436, %432 + 0 (4 byte)
	lw t1, 416(sp)
	lw t2, 436(sp)
	add t3, t1, t2
	sw t3, 440(sp)
# %440 = %416 + %436
	lw t1, 372(sp)
	lw t2, 440(sp)
	sw t2, 0(t1)
# Store %372 + 0, %440 (4 byte)
main.label_20:
main.label_17:
	addi t1, sp, 148
	sw t1, 444(sp)
# Op %444 = sp + 148 (4 byte)
	lw t1, 444(sp)
	lw t2, 0(t1)
	sw t2, 448(sp)
# Load %448, %444 + 0 (4 byte)
	lw t1, 448(sp)
	addi t3, t1, 1
	sw t3, 452(sp)
# %452 = %448 + 1
	lw t1, 444(sp)
	lw t2, 452(sp)
	sw t2, 0(t1)
# Store %444 + 0, %452 (4 byte)
	j main.label_15
# go to main.label_15
main.label_18:
main.label_13:
	addi t1, sp, 144
	sw t1, 456(sp)
# Op %456 = sp + 144 (4 byte)
	lw t1, 456(sp)
	lw t2, 0(t1)
	sw t2, 460(sp)
# Load %460, %456 + 0 (4 byte)
	lw t1, 460(sp)
	addi t3, t1, 1
	sw t3, 464(sp)
# %464 = %460 + 1
	lw t1, 456(sp)
	lw t2, 464(sp)
	sw t2, 0(t1)
# Store %456 + 0, %464 (4 byte)
	j main.label_11
# go to main.label_11
main.label_14:
	lw t1, 108(sp)
	sw t1, 468(sp)
# Load %468, sp + 108 (4 byte)
	lw t1, 12(sp)
	sw t1, 472(sp)
# Load %472, sp + 12 (4 byte)
	lw t1, 472(sp)
	li t2, 4
	mul t3, t1, t2
	sw t3, 476(sp)
# %476 = %472 * 4
	lw t1, 468(sp)
	lw t2, 476(sp)
	add t3, t1, t2
	sw t3, 480(sp)
# %480 = %468 + %476
	lw t1, 480(sp)
	lw t2, 0(t1)
	sw t2, 484(sp)
# Load %484, %480 + 0 (4 byte)
	lw a0, 484(sp)
	call toString
	sw a0, 488(sp)
# %488 = call (4B) toString(%484, )
	lw a0, 488(sp)
	call print
# call (0B) print(%488, )
	li t1, 0
	sw t1, 492(sp)
# Copy %492 0
	lw ra, 0(sp)
	lw a0, 492(sp)
	addi sp, sp, 496
	ret
# Return %492
	lw ra, 0(sp)
	mv a0, x0
	addi sp, sp, 496
	ret
# Return
						 # -- End function

	.section	.sdata,"aw",@progbits

	.globl	MAXN	#@MAXN
	.p2align	2
MAXN:
	.word	0
	.globl	MAXM	#@MAXM
	.p2align	2
MAXM:
	.word	0
