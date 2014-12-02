/**
 *	File:		Base64.cpp
 *	Author:		Alin Tomescu, tomescu.alin@gmail.com
 *	Website:	http://alinush.org
 *	Date: 		December 22nd, 2011
 *	License:	Free to use and distribute as long as this notice is kept.
 */
#include "Base64.h"

#include <cctype>
#include <cstring>




/**
 * This table maps every 6-bit number (0 through 63) to an ASCII character.
 * This table stores the base64 alphabet.
 */
const char Base64::_byteToChar[64] = 
{
	/**
	 * Uppercase letters (from 0 to 25)
	 */
	'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 
	'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
	
	/**
	 * Lowercase letters (from 26 to 51)
	 */
	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 
	'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
	
	/**
	 * Digits (from 52 to 61)
	 */
	'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	
	/**
	 * Misc. (from 62 to 63)
	 */
	'+', '/'
};

/**
 * The padding character is used to fill the remaining characters in the
 * base64 encoded block, when the input block is less than 3 bytes long.
 */
const char Base64::_paddingChar = '=';

bool Base64::isValidEncoding(const char * buffer, ulong length)
{
	//	Ensure string length is a multiple of 4
	if(length % 4)
		return false;
		
	//	Ensure string is only made up of base64 arguments
	for(uint i = 0; i < length; i++)
	{
		if(!isalnum(buffer[i]) && 
			buffer[i] != '+' && 
			buffer[i] != '/' && 
			buffer[i] != _paddingChar)
			return false;
	}
	
	if(length != 0)
	{
		//	Ensure string only has padding characters on the last 2 positions
		for(uint i = 0; i < length - 2; i++)
		{
			if(buffer[i] == _paddingChar)
				return false;
		}
		
		//	Ensure that the if the second to last character is a padding char, then the 
		//	last character is also a padding char
		if(buffer[length - 2] == _paddingChar && buffer[length - 1] != _paddingChar)
			return false;
	}
	
	return true;
}

void Base64::encodeBlock(const byte in[3], char out[4], uint inLength)
{
	/**
	 * Encoding three bytes works by splitting the 3 x 8 = 24 byte block
	 * into 4 blocks of 6 bytes. This is done using bitwise operations. Each one of the
	 * resulting 4 blocks will represent a number in the 0 to 63 range. 
	 * Each number is associated with a character in the base64 table.
	 * 
	 * Once we transform the 3 bytes into the 4 6-bit numbers, we'll replace the
	 * 4 6-bit numbers by their corresponding characters in the table and get
	 * the final base64 encoded block.
	 *
	 *	Of course, you also have to deal with padding, which involves a few checks.
	 */
	
	out[0] = byteToChar(in[0] >> 2);
	out[2] = out[3] = Base64::_paddingChar;
	
	out[1] = byteToChar(
		((in[0] & 0x03) << 4) |
		(inLength > 1 ? (((in[1] & 0xF0)) >> 4) : 0)
	);
	
	if(inLength >= 2)
	{
		out[2] = byteToChar(
			((in[1] & 0x0F) << 2) | 
			(inLength == 3 ? ((in[2] & 0xC0) >> 6) : 0)
		);
		
		if(inLength == 3)
			out[3] = byteToChar(in[2] & 0x3F);
	}
}

uint Base64::decodeBlock(const char in[4], byte out[3])
{
	/**
	 *	Decoding a 4-character blocks is just the reverse of encoding. You look at each character
	 *	and get its offset in the base64 alphabet. This will be a 6 bit number. You do this for all
	 *	4 characters and you'll get 6 x 4 = 24 bits = 3 bytes. These 3 bytes obtained by concatenating
	 *	all those 6 bits will be the decoded data.
	 *
	 *	Of course, you also have to deal with padding, which involves a few checks.
	 */
	 
	/**
	 * The length of the decoded data will be stored here
	 * and returned when the function exits.
	 */
	uint length = 0;
	
	out[0] = (charToByte(in[0]) << 2) | ((charToByte(in[1]) & 0x30) >> 4);
	out[1] = ((charToByte(in[1]) & 0x0F) << 4);
	length += 2;

	/**
	 * If the 3rd input char is not a padding char, then go ahead and
	 * decode it, storing it in the remaining part of the 2nd output byte.
	 */
	if(in[2] != Base64::_paddingChar)
	{
		out[1] |= ((charToByte(in[2]) & 0x3C) >> 2);
		
		/**
		 * If the 4th input char is also not a padding char, then go ahead and decode it,
		 * storing it in the 3rd output byte.
		 */
		if(in[3] != Base64::_paddingChar)
		{
			out[2] = ((charToByte(in[2]) & 0x03) << 6) | (charToByte(in[3]) & 0x3F);
			length += 1;
		}
	}

	
	return length;
}

byte Base64::charToByte(char ch)
{
	if(ch == '+')
		return 62;
	else if(ch == '/')
		return 63;
	else if(isdigit(ch))
		return 52 + (ch - '0');
	else if(isalpha(ch))
	{
		if(isupper(ch))
			return ch - 'A';
		else
			return 26 + (ch - 'a');
	}
  return ch;

	
}

ulong Base64::encodeBuffer(const byte * in, char * out, ulong inSize)
{
	/**
	 * Compute the number of 3 byte chunks and, if the
	 * last chunk is less than 3 bytes, compute its size also.
	 */
	uint nChunks = inSize / 3;
	uint lastChunkSize = inSize % 3;
	
	/**
	 * Get two pointers to the input and output buffers.
	 */
	const byte * inPtr = in;
	char * outPtr = out;
	
	/**
	 * For each chunk of 3 bytes, encode it in base 64,
	 * and advance the input and output pointers into the buffers.
	 */
	for(uint i = 0; i < nChunks; i++)
	{
		encodeBlock(inPtr, outPtr, 3);
		//std::cout << "In: " << inPtr[0] << inPtr[1] << inPtr[2] << std::endl;
		//std::cout << "Out: " << outPtr[0] << outPtr[1] << outPtr[2] << outPtr[3] << std::endl;
		inPtr += 3;
		outPtr += 4;
	}
	
	/**
	 * Deal with the last chunk also.
	 */
	if(lastChunkSize > 0)
	{
		encodeBlock(inPtr, outPtr, lastChunkSize);
		return (nChunks + 1) * 4;
	}
	else
		return nChunks * 4;
}

ulong Base64::decodeBuffer(const char * in, byte * out, ulong inSize)
{
	/**
	 * The length of the input base64-encoded line needs to be a multiple of 4.
	 */
	if(inSize % 4)
	{
//		std::ostringstream error;
//		error << "The length of the base64-encoded line (" << inSize << ") is not a multiple of 4.";
//		throw std::runtime_error(error.str());
	}
	
	if(!isValidEncoding(in, inSize))
	{
		//throw std::runtime_error("The input string is not a valid base64 encoding");
	}
	
	/**
	 * The number of 4-byte base64-encoded chunks.
	 */
	uint nChunks = inSize / 4;
	/**
	 * The length in bytes of the resulting decoded data.
	 */
	ulong decodedLength = 0;
	
	/**
	 * Get pointers to the input and output buffer.
	 */
	const char * inPtr = in;
	byte * outPtr = out;
	
	/**
	 * Decode each chunk, advance the pointers and keep track of
	 * the length of the decoded data.
	 */
	for(uint i = 0; i < nChunks; i++)
	{
		decodedLength += decodeBlock(inPtr, outPtr);
		inPtr += 4;
		outPtr += 3;
	}
	
	return decodedLength;
}

