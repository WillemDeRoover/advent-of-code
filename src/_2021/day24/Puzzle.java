package _2021.day24;

public class Puzzle {

	//	inp w0
	//	add x 11
	//	add y 6
	//	z0 = w0 + 6

	//	inp w1
	//	add x 11
	//	add y 12
	//	z1 = 26(z0) + w1 + 12

	//	inp w2
	//	add x 15
	//	add y 8
	//	z2 = 26(z1) + w2 + 8

	//	inp w3
	//	add x -11
	//	add y 7
	//	z3 = z2/26 = z1
	//	w3 = w2 + 8 - 11
	//  w3 = w2 - 3

	//	inp w4
	//	add x 15
	//	add y 7
	//  z4 = 26(z1) + w4 + 7

	//	inp w5
	//	add x 15
	//	add y 12
	//  z5 = 26(z4) + w5 + 12

	//	inp w6
	//	add x 14
	//	add y 2
	//  z6 = 26(z5) + w6 + 2

	//	inp w7
	//	add x -7
	//	add y 15
	//  z7 = z6/26 = z5
	//  w7 = w6 + 2 - 7;
	//  w7 = w6 - 5

	//	inp w8
	//	add x 12
	//	add y 4
	//  z8 = 26(z5) + w8 + 4

	//	inp w9
	//	add x -6
	//	add y 5
	//  z9 = z8/26 = z5;
	//  w9 = w8 + 4 - 6;
	//  w9 = w8 - 2

	//	inp w10
	//	add x -10
	//	add y 12
	//  z10 = z5/26 = z4
	//	w10 = w5 + 12 - 10;
	//  w10 = w5 + 2;

	//	inp w11
	//	add x -15
	//	add y 11
	//  z11 = z4/26 = z1;
	//	w11 = w4 + 7 -15
	//  w11 = w4 - 8;

	//	inp w12
	//	add x -9
	//	add y 13
	//	z12 = z1/26 = z0;
	//	w12 = w1 + 12 - 9
	//  w12 = w1 + 3

	//	inp w13
	//	add x 0
	//	add y 7
	//  z13 = 0
	//  w13 = w0 + 6

	//  w13 = w0 + 6 => max: w0 = 3, w13 = 9; min: w0 = 1, w13 = 7
	//  w12 = w1 + 3 => max: w1 = 6, w12 = 9; min: w1 = 1, w12 = 4
	//  w3 = w2 - 3 => max: w2 = 9, w3 = 6; min: w2 = 4, w3 = 1
	//  w11 = w4 - 8;  => max: w4 = 9, w11 = 1; min: w4 = 9, w11 = 1
	//  w10 = w5 + 2; => max: w5 = 7, w10 = 9; min: w5 = 1, w10 = 3
	//  w7 = w6 - 5 => max: w6 = 9, w7 = 4; min: w6 = 6, w7 = 1
	//  w9 = w8 - 2 => max: w8 = 9, w9 = 7; min: w8 = 3, w9 = 1

	//	max: 36969794979199
	//  min: 11419161313147

}