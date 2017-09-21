/*
 * ecc.c
 *
 *  Created on: 01/09/2014
 *      Author: bernardobreder_local
 */

#include "huge.h"
#include "ecc.h"

void ecc_add_points(struct ecc_point *p1, struct ecc_point *p2, struct huge *p) {
	struct ecc_point p3;
	struct huge denominator, numerator, invdenom, lambda;
	set_huge(&denominator, 0);
	copy_huge(&denominator, &p2->x);
	subtract(&denominator, &p1->x);
	set_huge(&numerator, 0);
	copy_huge(&numerator, &p2->y);
	subtract(&numerator, &p1->y);
	set_huge(&invdenom, 0);
	copy_huge(&invdenom, &denominator);
//	inv(&invdenom, p);
}

void ecc_double_points(struct ecc_point *p1, struct huge *a, struct huge *p) {

}
