// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, the
// program clears the screen, i.e. writes "white" in every pixel.
// Put your code here.
(START)
	@color
	M=0
	@24576
	D=M
	@INIT
	D;JEQ
	@color
	M=-1
(INIT)
	@count
	M=-1
	@SCREEN
	D=A
	@address
	M=D
(DRAW)
	@count
	M=M+1
	D=M
	@8192
	D=D-A
	@START
	D;JEQ
	@color
	D=M
	@address
	A=M
	M=D
	@address
	M=M+1
	@DRAW
	0;JMP
	



