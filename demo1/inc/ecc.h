/*
 * ecc.h
 *
 *  Created on: 01/09/2014
 *      Author: Bernardo Breder
 */

#ifndef ECC_H_
#define ECC_H_

#include "huge.h"

struct ecc_point {
	struct huge x;
	struct huge y;
};

struct ecc_elliptic_curve {
	struct huge p;
	struct huge a;
	struct huge b;
	struct ecc_point g;
	struct huge n;
	struct huge h;
};

struct ecc_key {
	struct huge d;
	struct ecc_point q;
};

void ecc_add_points(struct ecc_point *p1, struct ecc_point *p2, struct huge *p);

void ecc_double_points(struct ecc_point *p1, struct huge *a, struct huge *p);

#endif /* ECC_H_ */
