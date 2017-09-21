//#ifndef _HUGE_G
//#define _HUGE_G
//
//struct huge {
//	int sign;
//	unsigned int size;
//	unsigned char *rep;
//};
//
//void add(struct huge *huge1, struct huge* huge2);
//
//void add_magnitude(struct huge *huge1, struct huge* huge2);
//
//void subtract_magnitude(struct huge *h1, struct huge *h2);
//
//void subtract(struct huge *h1, struct huge *h2);
//
//void expaned(struct huge *huge1);
//
//void contract(struct huge *h);
//
//void multiply(struct huge* huge1, struct huge* huge2);
//
//void set_huge(struct huge *h, unsigned int value);
//
//void copy_huge(struct huge *target, struct huge *source);
//
//int compare(struct huge *h1, struct huge *h2);
//
//void divide(struct huge *dividend, struct huge *divisor, struct huge *quotient);
//
//void free_huge(struct huge *h);
//
//void mod_pow(struct huge *m, struct huge *exp, struct huge *n, struct huge *c);
//
//void exponentiate(struct huge *h, struct huge *exp);
//
//void load_huge(struct huge *h, const unsigned char *bytes, long length);
//
//void unload_huge(const struct huge *h, unsigned char *bytes, long length);
//
//#endif

#ifndef HUGE_H
#define HUGE_H

struct huge
{
    int sign;
    unsigned long size;
    unsigned char *rep;
};

void copy_huge( struct huge *tgt, struct huge *src );
void set_huge( struct huge *h, unsigned int val );
int compare( struct huge *h1, struct huge *h2 );
void add( struct huge *h1, struct huge *h2 );
void subtract( struct huge *h1, struct huge *h2 );
void multiply( struct huge *h1, struct huge *h2 );
void divide( struct huge *dividend, struct huge *divisor, struct huge *quotient );
void load_huge( struct huge *h, const unsigned char *bytes, long length );
void unload_huge( const struct huge *h, unsigned char *bytes, long length );
void free_huge( struct huge *h );
void mod_pow( struct huge *h1, struct huge *exp, struct huge *n, struct huge *h2 );
void inv( struct huge *z, struct huge *a );
void contract( struct huge *h );

#endif
