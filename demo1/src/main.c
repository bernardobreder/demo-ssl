#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include "aes.h"
#include "hex.h"
#include "rsa.h"
#include "http.h"
#include "huge.h"

#define TEST_RSA

#if defined TEST_AES

int main( int argc, const char *argv[ ] )
{
    unsigned char *key;
    unsigned char *input;
    unsigned char *iv;
    long key_len;
    long input_len;
    long iv_len;
    
    if ( argc < 5 )
    {
        fprintf( stderr, "Usage: %s [-e|-d] <key> <iv> <input>\n", argv[ 0 ] );
        exit( 0 );
    }
    
    key_len = hex_decode( (const unsigned char*) argv[ 2 ], &key );
    iv_len = hex_decode( (const unsigned char*) argv[ 3 ], &iv );
    input_len = hex_decode( (const unsigned char*) argv[ 4 ], &input );
    
    if ( !strcmp( argv[ 1 ], "-e" ) )
    {
        unsigned char *ciphertext = ( unsigned char * ) malloc( input_len );
        
        if ( key_len == 16 )
        {
            aes_128_encrypt( input, input_len, ciphertext, iv, key );
        }
        else if ( key_len == 32 )
        {
            aes_256_encrypt( input, input_len, ciphertext, iv, key );
        }
        else
        {
            fprintf( stderr, "Unsupported key length: %ld\n", key_len );
            exit( 0 );
        }
        
        show_hex( ciphertext, input_len );
        
        free( ciphertext );
    }
    else if ( !strcmp( argv[ 1 ], "-d" ) )
    {
        unsigned char *plaintext = ( unsigned char * ) malloc( input_len );
        
        if ( key_len == 16 )
        {
            aes_128_decrypt( input, input_len, plaintext, iv, key );
        }
        else if ( key_len == 32 )
        {
            aes_256_decrypt( input, input_len, plaintext, iv, key );
        }
        else
        {
            fprintf( stderr, "Unsupported key length %ld\n", key_len );
            exit( 0 );
        }
        
        show_hex( plaintext, input_len );
        free( plaintext );
    }
    else
    {
        fprintf( stderr, "Usage: %s [-e|-d] <key> <iv> <input>\n", argv[ 0 ] );
    }
    
    free( iv );
    free( key );
    free( input );
    
    return 0;
}

#elif defined TEST_RSA

const unsigned char TestModulus[] = {
    0xC4, 0xF8, 0xE9, 0xE1, 0x5D, 0xCA, 0xDF, 0x2B,
    0x96, 0xC7, 0x63, 0xD9, 0x81, 0x00, 0x6A, 0x64,
    0x4F, 0xFB, 0x44, 0x15, 0x03, 0x0A, 0x16, 0xED,
    0x12, 0x83, 0x88, 0x33, 0x40, 0xF2, 0xAA, 0x0E,
    0x2B, 0xE2, 0xBE, 0x8F, 0xA6, 0x01, 0x50, 0xB9,
    0x04, 0x69, 0x65, 0x83, 0x7C, 0x3E, 0x7D, 0x15,
    0x1B, 0x7D, 0xE2, 0x37, 0xEB, 0xB9, 0x57, 0xC2,
    0x06, 0x63, 0x89, 0x82, 0x50, 0x70, 0x3B, 0x3F
};

const unsigned char TestPrivateKey[] = {
    0x8a, 0x7e, 0x79, 0xf3, 0xfb, 0xfe, 0xa8, 0xeb,
    0xfd, 0x18, 0x35, 0x1c, 0xb9, 0x97, 0x91, 0x36,
    0xf7, 0x05, 0xb4, 0xd9, 0x11, 0x4a, 0x06, 0xd4,
    0xaa, 0x2f, 0xd1, 0x94, 0x38, 0x16, 0x67, 0x7a,
    0x53, 0x74, 0x66, 0x18, 0x46, 0xa3, 0x0c, 0x45,
    0xb3, 0x0a, 0x02, 0x4b, 0x4d, 0x22, 0xb1, 0x5a,
    0xb3, 0x23, 0x62, 0x2b, 0x2d, 0xe4, 0x7b, 0xa2, 
    0x91, 0x15, 0xf0, 0x6e, 0xe4, 0x2c, 0x41
};

const unsigned char TestPublicKey[] = { 0x01, 0x00, 0x01 };

int main( int argc, char *argv[ ] )
{
    long exponent_len;
    long modulus_len;
    long data_len;
    unsigned char *exponent;
    unsigned char *modulus;
    unsigned char *data;
    struct rsa_key public_key;
    struct rsa_key private_key;
    
    if ( argc < 3 )
    {
        fprintf( stderr, "Usage: rsa [-e|-d] [<modulus> <exponent>] <data>\n" );
        exit( 0 );
    }
    
    if ( argc == 5 )
    {
        modulus_len = hex_decode( (const unsigned char*)argv[ 2 ], &modulus );
        exponent_len = hex_decode( (const unsigned char*)argv[ 3 ], &exponent );
        data_len = hex_decode( (const unsigned char*)argv[ 4 ], &data );
    }
    else
    {
        data_len = hex_decode( (const unsigned char*)argv[ 2 ], &data );
        modulus_len = sizeof( TestModulus );
        modulus = (unsigned char*) TestModulus;
        if ( !strcmp( "-e", argv[ 1 ] ) )
        {
            exponent_len = sizeof( TestPublicKey );
            exponent = (unsigned char*) TestPublicKey;
        }
        else
        {
            exponent_len = sizeof( TestPrivateKey );
            exponent = (unsigned char*) TestPrivateKey;
        }
    }
    
    public_key.modulus = ( struct huge * ) malloc( sizeof( struct huge ) );
    public_key.exponent = ( struct huge * ) malloc( sizeof( struct huge ) );
    private_key.modulus = ( struct huge * ) malloc( sizeof( struct huge ) );
    private_key.exponent = ( struct huge * ) malloc( sizeof( struct huge ) );
    
    if ( !strcmp( argv[ 1 ], "-e" ) )
    {
        unsigned char *encrypted;
        long encrypted_len;
        
        load_huge( public_key.modulus, modulus, modulus_len );
        load_huge( public_key.exponent, exponent, exponent_len );
        
        encrypted_len = rsa_encrypt( data, data_len, &encrypted, &public_key );
        show_hex( encrypted, encrypted_len );
        free( encrypted );
    }
    else if ( !strcmp( argv[ 1 ], "-d" ) )
    {
        int decrypted_len;
        unsigned char *decrypted;
        
        load_huge( private_key.modulus, modulus, modulus_len );
        load_huge( private_key.exponent, exponent, exponent_len );
        
        decrypted_len = rsa_decrypt( data, data_len, &decrypted, &private_key );
        
        show_hex( decrypted, decrypted_len );
        
        free( decrypted );
    }
    else
    {
        fprintf( stderr, "unrecognized option flag '%s'\n", argv[ 1 ] );
    }
    
    free( data );
    if ( argc == 5 )
    {
        free( modulus );
        free( exponent );
    }
    
    return 0;
}

#else

int main(int argc, char *argv[]) {
    
    http("www.google.com", "/");
    return EXIT_SUCCESS;
}

#endif
