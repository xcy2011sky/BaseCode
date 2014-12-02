#include <jni.h>
#include <stdio.h>
#include <android/log.h>
#include "des.cpp"
#include "mylog.h"
#include "Base64.h"

#define NELEM(m) (sizeof(m) / sizeof((m)[0]))




jstring DESEncrypt(JNIEnv *pEnv, jobject pObj, jstring text) {

	//LOGI(2, "---------------------------desencrypt-----------------");

	const char * plain = pEnv->GetStringUTFChars(text,NULL);;
	//LOGI(2, "native plain: %s", plain);

	//const char * To char *
	 char tmp[128];
	memcpy(tmp,plain,128);
	//LOGI(2, "native tmp: %s", tmp);



   //create  cipher buffer
	char cipher[128];

	//DES_Encrypt
	DES_Encrypt(tmp, cipher);
	//LOGI(2, "native cipher: %s", cipher);



     //Base64 encode
	long length=NELEM(cipher);
	//LOGI(2, "encrypt :: native length : %ld", length);

	long base_size=Base64::getEncodedSize(length)+1;


	char * ccipher = new char[base_size];
	ccipher[base_size-1]='\0';

	const byte *pcipher=(byte *)cipher;


	Base64::encodeBuffer(pcipher,ccipher,length);


    //char * To jstring
	jstring jcipher;
	if((jcipher=pEnv->NewStringUTF(ccipher))==NULL) return NULL;

   //release  char refreash

  delete(ccipher);

	// release String UTF
	pEnv->ReleaseStringUTFChars(text, plain);
	return jcipher;

}

jstring DESDecrypt(JNIEnv *pEnv, jobject pObj, jstring text) {

	//LOGI(2, "---------------------------DEsdecrypt-----------------");

 //  jstring To const char *
	const char * cipher =pEnv->GetStringUTFChars(text,NULL);
	//LOGI(2, "Decrypt :: native cipher : %s", cipher);
    char swp[128];
    memcpy(swp,cipher,128);
	//base64 decodeBuffer


	long length=NELEM(swp);
	//LOGI(2, "Decrypt :: native length : %ld", length);

	long base_size=Base64::getDecodedSize(length)+1;
	byte *tmp2 = new byte[base_size];
	tmp2[base_size]='\0';

	Base64::decodeBuffer(cipher,tmp2,base_size);
	//LOGI(2, "Decrypt :: native base64 tmp2 : %s", tmp2);


	//byte * To char *
	char tmp[128];
	memcpy(tmp,tmp2,128);
   //  LOGI(2, "Decrypt :: native tmp : %s", tmp);

	// create plain Buffer
	char *plain = new char[128];
	// DEC_Dencrypt
	DES_Decrypt(tmp, plain);
	//LOGI(2, "Decrypt :: native plain : %s", plain);


	// char * To jstring
	jstring jplain;
	if((jplain=pEnv->NewStringUTF(plain))==NULL) return NULL;

	//release
    delete(plain);

    delete(tmp2);
    //release String
   	pEnv->ReleaseStringUTFChars(text, cipher);

	return jplain;
}


// JNI method register
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* pVm, void* reserved) {
	JNIEnv* env;
	if (pVm->GetEnv((void **) &env, JNI_VERSION_1_6)) {
		return -1;
	}
	JNINativeMethod nm[] = { { "encrypt",
			"(Ljava/lang/String;)Ljava/lang/String;", (void*) DESEncrypt }, {
			"decrypt", "(Ljava/lang/String;)Ljava/lang/String;",
			(void*) DESDecrypt } };

	jclass cls = env->FindClass("com/xcy/xpassword/database/DESCrypt");

	// Register methods with env->RegisterNatives.
	env->RegisterNatives(cls, nm, NELEM(nm));
	return JNI_VERSION_1_6;
}

