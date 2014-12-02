#pragma warning( disable : 4996)
#pragma warning( disable : 4101)
#pragma warning( disable : 4715)
#include<stdio.h>
#include <memory.h>
#include <time.h>
#include <stdlib.h>

//初始置换表IP
int IP_Table[64] = { 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11,
		3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7, 56, 48,
		40, 32, 24, 16, 8, 0, 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28,
		20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6 };
//逆初始置换表IP^-1
int IP_1_Table[64] = { 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22, 62,
		30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3,
		43, 11, 51, 19, 59, 27, 34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49,
		17, 57, 25, 32, 0, 40, 8, 48, 16, 56, 24 };
//扩充置换表E
int E_Table[48] = { 31, 0, 1, 2, 3, 4, 3, 4, 5, 6, 7, 8, 7, 8, 9, 10, 11, 12,
		11, 12, 13, 14, 15, 16, 15, 16, 17, 18, 19, 20, 19, 20, 21, 22, 23, 24,
		23, 24, 25, 26, 27, 28, 27, 28, 29, 30, 31, 0 };
//置换函数P
int P_Table[32] = { 15, 6, 19, 20, 28, 11, 27, 16, 0, 14, 22, 25, 4, 17, 30, 9,
		1, 7, 23, 13, 31, 26, 2, 8, 18, 12, 29, 5, 21, 10, 3, 24 };
//S盒
int S[8][4][16] = //S1
		{ { { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 }, { 0, 15,
				7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 }, { 4, 1, 14, 8,
				13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 }, { 15, 12, 8, 2, 4, 9,
				1, 7, 5, 11, 3, 14, 10, 0, 6, 13 } },
//S2
				{ { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 }, { 3,
						13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 }, {
						0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
						{ 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 } },
//S3
				{ { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 }, {
						13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
						{ 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
						{ 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 } },
//S4
				{ { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 }, {
						13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
						{ 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
						{ 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 } },
//S5
				{ { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 }, {
						14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
						{ 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
						{ 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 } },
//S6
				{ { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 }, {
						10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
						{ 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
						{ 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 } },
//S7
				{ { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 }, {
						13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
						{ 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
						{ 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 } },
//S8
				{ { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 }, { 1,
						15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 }, {
						7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
						{ 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } } };
//置换选择1
int PC_1[56] = { 56, 48, 40, 32, 24, 16, 8, 0, 57, 49, 41, 33, 25, 17, 9, 1, 58,
		50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 62, 54, 46, 38, 30, 22, 14,
		6, 61, 53, 45, 37, 29, 21, 13, 5, 60, 52, 44, 36, 28, 20, 12, 4, 27, 19,
		11, 3 };
//置换选择2
int PC_2[48] = { 13, 16, 10, 23, 0, 4, 2, 27, 14, 5, 20, 9, 22, 18, 11, 3, 25,
		7, 15, 6, 26, 19, 12, 1, 40, 51, 30, 36, 46, 54, 29, 39, 50, 44, 32, 46,
		43, 48, 38, 55, 33, 52, 45, 41, 49, 35, 28, 31 };

const char * key="xpassword";


//对左移次数的规定
int MOVE_TIMES[16] = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };
void ByteToBit(char ch, char bit[8]);
void BitToByte(char bit[8], char *ch);
void Char8ToBit64(char ch[8], char bit[64]);
void Bit64ToChar8(char bit[64], char ch[8]);
void DES_MakeSubKeys(char key[64], char subKeys[16][48]);
void DES_PC1_Transform(char key[64], char tempbts[56]);
void DES_PC2_Transform(char key[56], char tempbts[48]);
void DES_ROL(char data[56], int time);
void DES_IP_Transform(char data[64]);
void DES_IP_1_Transform(char data[64]);
void DES_E_Transform(char data[48]);
void DES_P_Transform(char data[32]);
void DES_SBOX(char data[48]);
void DES_XOR(char R[48], char L[48], int count);
void DES_Swap(char left[32], char right[32]);
void DES_E_D_Block(char plainBlock[8], char subKeys[16][48],
		char cipherBlock[8]);
int DES_Encrypt(char *plainFile,  char *cipherFile);
int DES_Decrypt(char *cipherFile, char *plainFile);
//字节转换成二进制
void ByteToBit(char ch, char bit[8]) {
	int cnt;
	for (cnt = 0; cnt < 8; cnt++)
		*(bit + cnt) = (ch >> cnt) & 1;
}
//二进制转换成字节
void BitToByte(char bit[8], char *ch) {
	int cnt;
	for (cnt = 0; cnt < 8; cnt++)
		*ch |= *(bit + cnt) << cnt;
}
//将长度为8的字符串转为二进制位串
void Char8ToBit64(char ch[8], char bit[64]) {
	int cnt;
	for (cnt = 0; cnt < 8; cnt++)
		ByteToBit(*(ch + cnt), bit + (cnt << 3));
}
//将二进制位串转为长度为8的字符串
void Bit64ToChar8(char bit[64], char ch[8]) {
	int cnt;
	memset(ch, 0, 8);
	for (cnt = 0; cnt < 8; cnt++)
		BitToByte(bit + (cnt << 3), ch + cnt);
}
//将二进制位串转为长度为6的字符串
void Bit48ToChar6(char bit[48], char ch[6]) {
	int cnt;
	memset(ch, 0, 6);
	for (cnt = 0; cnt < 6; cnt++)
		BitToByte(bit + (cnt << 3), ch + cnt);
}
//生成子密钥
void DES_MakeSubKeys(char key[64], char subKeys[16][48]) {
	char temp[56];
	char ch[6];
	int cnt;
	int i;
	//printf("\n以下是子密钥：\n");
	DES_PC1_Transform(key, temp); //PC1置换
	for (cnt = 0; cnt < 16; cnt++) {
		//16轮跌代，产生16个子密钥
		DES_ROL(temp, MOVE_TIMES[cnt]); //循环左移
		DES_PC2_Transform(temp, subKeys[cnt]); //PC2置换，
		Bit48ToChar6(subKeys[cnt], ch);
		for (i = 0; i < 6; i++) {
			//printf("%2X ", (unsigned char) ch[i]);
		}
	//	printf("\n");
		//产生子密钥
	}
}
//密钥置换1
void DES_PC1_Transform(char key[64], char tempbts[56]) {
	int cnt;
	for (cnt = 0; cnt < 56; cnt++)
		tempbts[cnt] = key[PC_1[cnt]];
}
//密钥置换2
void DES_PC2_Transform(char key[56], char tempbts[48]) {
	int cnt;
	for (cnt = 0; cnt < 48; cnt++)
		tempbts[cnt] = key[PC_2[cnt]];
}
//循环左移
void DES_ROL(char data[56], int time) {
	char temp[4];
//保存将要循环移动到右边的位
	memcpy(temp, data, time);
	memcpy(temp + time, data + 28, time);
//前28位移动
	memcpy(data, data + time, 28 - time);
	memcpy(data + 28 - time, temp, time);
//后28位移动
	memcpy(data + 28, data + 28 + time, 28 - time);
	memcpy(data + 56 - time, temp + time, time);
}
//IP置换
void DES_IP_Transform(char data[64]) {
	int cnt;
	char temp[64];
	for (cnt = 0; cnt < 64; cnt++)
		temp[cnt] = data[IP_Table[cnt]];
	memcpy(data, temp, 64);
}
//IP逆置换
void DES_IP_1_Transform(char data[64]) {
	int cnt;
	char temp[64];
	for (cnt = 0; cnt < 64; cnt++)
		temp[cnt] = data[IP_1_Table[cnt]];
	memcpy(data, temp, 64);
}
//扩展置换
void DES_E_Transform(char data[48]) {
	int cnt;
	char temp[48];
	for (cnt = 0; cnt < 48; cnt++)
		temp[cnt] = data[E_Table[cnt]];
	memcpy(data, temp, 48);
}
//P置换
void DES_P_Transform(char data[32]) {
	int cnt;
	char temp[32];
	for (cnt = 0; cnt < 32; cnt++)
		temp[cnt] = data[P_Table[cnt]];
	memcpy(data, temp, 32);
}
//异或
void DES_XOR(char R[48], char L[48], int count) {
	int cnt;
	for (cnt = 0; cnt < count; cnt++)
		R[cnt] ^= L[cnt];
}
//S盒置换
void DES_SBOX(char data[48]) {
	int cnt;
	int line, row, output;
	int cur1, cur2;
	for (cnt = 0; cnt < 8; cnt++) {
		cur1 = cnt * 6;
		cur2 = cnt << 2;
//计算在S盒中的行与列
		line = (data[cur1] << 1) + data[cur1 + 5];
		row = (data[cur1 + 1] << 3) + (data[cur1 + 2] << 2)
				+ (data[cur1 + 3] << 1) + data[cur1 + 4];
		output = S[cnt][line][row];
//化为2进制
		data[cur2] = (output & 0X08) >> 3;
		data[cur2 + 1] = (output & 0X04) >> 2;
		data[cur2 + 2] = (output & 0X02) >> 1;
		data[cur2 + 3] = output & 0x01;
	}
}
//交换
void DES_Swap(char left[32], char right[32]) {
	char temp[32];
	memcpy(temp, left, 32);
	memcpy(left, right, 32);
	memcpy(right, temp, 32);
}
//加解密单个分组
void DES_E_D_Block(char plainBlock[8], char subKeys[16][48],
		char cipherBlock[8]) {
	char plainBits[64], copyRight[48], zjjg[8];
	int i, cnt;
       //	printf("\n\n以下是16轮中间结果的16进制值:\n");
	Char8ToBit64(plainBlock, plainBits); //Char8ToBit64(char ch[8],char bit[64]);
	DES_IP_Transform(plainBits); //初始置换（IP置换）
	for (cnt = 0; cnt < 16; cnt++) { //16轮迭代
		memcpy(copyRight, plainBits + 32, 32);
		DES_E_Transform(copyRight); //将右半部分进行扩展置换，从32位扩展到48位
		DES_XOR(copyRight, subKeys[cnt], 48); //将右半部分与子密钥进行异或操作
		DES_SBOX(copyRight); //异或结果进入S盒，输出32位结果
		DES_P_Transform(copyRight); //P置换
		DES_XOR(plainBits, copyRight, 32); //将明文左半部分与右半部分进行异或
		if (cnt != 15) { //最终完成左右部的交换
			DES_Swap(plainBits, plainBits + 32);
		}
		Bit64ToChar8(plainBits, zjjg);
		for (i = 0; i < 8; i++) {
		//	printf("%2X ", (unsigned char) zjjg[i]);
		}
	      //	printf("\n");
	}
//逆初始置换（IP^1置换）
	DES_IP_1_Transform(plainBits);
	Bit64ToChar8(plainBits, cipherBlock);
	Bit64ToChar8(plainBits, zjjg);
     //	printf("经过逆置换过后的16进制结果：\n");
	for (i = 0; i < 8; i++) {
	   //	printf("%2X ", (unsigned char) zjjg[i]);
	}
	//printf("\n");
}


//加密文件
int DES_Encrypt(char *plain, char *cipher) {

	int count, i;
	char plainBlock[8], cipherBlock[8], keyBlock[8], bKey[64], subKeys[16][48];


	//设置密钥
	strncpy(keyBlock,key,8);
	count = sizeof(keyBlock)/sizeof(char);
	if (count < 8) {
		memset(keyBlock + count, '\0', 7 - count);
		keyBlock[7] = 8 - count;
	}
//	printf("密钥的16进制表示:\n");
	for (i = 0; i < 8; i++) {

		//   printf("%2X ", (unsigned char) keyBlock[i]);
	}
//printf("\n");
	//将密钥转换为二进制流
	Char8ToBit64(keyBlock, bKey);
	//生成子密钥
	DES_MakeSubKeys(bKey, subKeys);

	//char * p=plain;
    //printf("plain =%s\n",plain);
		while (*plain!='\0') { //每次读8个字节，并返回成功读取的字节数

				strncpy(plainBlock,plain,8);
				  //  printf("plainBlock =%s\n",plainBlock);
				DES_E_D_Block(plainBlock, subKeys, cipherBlock);
                  //      printf("cipherBlock =%s\n",cipherBlock);
				strncpy(cipher,cipherBlock,8);
				    //    printf("cipher =%s\n",cipher);
				plain=plain+8;
				cipher=cipher+8;

		}

   return 0;
}
//解密文件
int DES_Decrypt(char *cipher,char *plain) {

	int count,i,times = 0;

	char plainBlock[8],cipherBlock[8],keyBlock[8],bKey[64],subKeys[16][48],temp[48];
	strncpy(keyBlock,key,8);

	//printf("密钥的16进制表示:\n");
//	for (i = 0; i < 8; i++) {
	//	printf("%2X ", (unsigned char) keyBlock[i]);
//	}

	//printf("\n");
//将密钥转换为二进制流
	Char8ToBit64(keyBlock, bKey);
//生成子密钥
	DES_MakeSubKeys(bKey, subKeys);
//转换为解密密钥
	for (i = 0; i < 8; i++) {
		memcpy(temp, subKeys[i], 48);
		memcpy(subKeys[i], subKeys[15 - i], 48);
		memcpy(subKeys[15 - i], temp, 48);
	}
	while (*cipher!='\0') { //每次读8个字节，并返回成功读取的字节数

			strncpy(cipherBlock,cipher,8);

			DES_E_D_Block(cipherBlock, subKeys, plainBlock);

			strncpy(plain,plainBlock,8);
			plain=plain+8;
			cipher=cipher+8;
	}

	return 0;
}
