#ifndef _RSA_G
#define _RSA_G

#include "huge.h"

struct rsa_key {
	struct huge *modulus;
	struct huge *exponent;
};

int rsa_encrypt(unsigned char *input, unsigned long len, unsigned char **output, struct rsa_key *public_key);

int rsa_decrypt(unsigned char *input, unsigned long len, unsigned char **output, struct rsa_key *private_key);

#endif
