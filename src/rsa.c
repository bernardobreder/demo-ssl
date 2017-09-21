#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "huge.h"
#include "rsa.h"

void rsa_compute(struct huge *m, struct huge *e, struct huge *n, struct huge *c) {
	struct huge counter;
	struct huge one;
	copy_huge(c, m);
	set_huge(&counter, 1);
	set_huge(&one, 1);
	while (compare(&counter, e) < 0) {
		multiply(c, m);
		add(&counter, &one);
	}
	divide(c, n, NULL);
	free_huge(&counter);
	free_huge(&one);
}

int rsa_encrypt(unsigned char *input, unsigned long len, unsigned char **output, struct rsa_key *public_key) {
	int i;
	struct huge c, m;
    *output = 0;
	long modulus_length = public_key->modulus->size;
	unsigned long block_size;
	unsigned char *padded_block = (unsigned char *) malloc(modulus_length);
	int encrypted_size = 0;
	while (len) {
		encrypted_size += modulus_length;
		block_size = len < modulus_length - 11 ? len : modulus_length - 11;
		memset(padded_block, 0, modulus_length);
		memcpy(padded_block + (modulus_length - block_size), input, block_size);
		padded_block[1] = 0x02;
		for (i = 2; i < modulus_length - block_size - 1; i++) {
			padded_block[i] = i;
		}
		load_huge(&m, padded_block, modulus_length);
		mod_pow(&m, public_key->exponent, public_key->modulus, &c);
        *output = (unsigned char*) *output ? realloc(*output, encrypted_size) : malloc(encrypted_size);
		unload_huge(&c, *output + (encrypted_size - modulus_length), modulus_length);
		len -= block_size;
		input += block_size;
		free_huge(&m);
		free_huge(&c);
	}
	free(padded_block);
	return encrypted_size;
}

int rsa_decrypt(unsigned char *input, unsigned long len, unsigned char **output, struct rsa_key *private_key) {
	int i, out_len = 0;
	struct huge c, m;
	long modulus_length = private_key->modulus->size;
	unsigned char *padded_block = (unsigned char*) malloc(modulus_length);
	*output = 0;
	while (len) {
		if (len < modulus_length) {
			free(padded_block);
			return -1;
		}
		load_huge(&c, input, modulus_length);
		mod_pow(&c, private_key->exponent, private_key->modulus, &m);
		unload_huge(&m, padded_block, modulus_length);
		if (padded_block[1] > 0x02) {
			free_huge(&c);
			free_huge(&m);
			free(padded_block);
			return -1;
		}
		i = 2;
		while (padded_block[i++]){
		}
		out_len += modulus_length - i;
		*output = realloc(*output, out_len);
		memcpy(*output + (out_len - (modulus_length - i)), padded_block + i, modulus_length - i);
		len -= modulus_length;
		input += modulus_length;
		free_huge(&c);
		free_huge(&m);
	}
	free(padded_block);
	return out_len;
}
