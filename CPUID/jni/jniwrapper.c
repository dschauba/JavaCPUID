#include "jniwrapper.h"
#include <stdio.h>
#include <stdlib.h>
#include <cpuid.h>

JNIEXPORT jintArray JNICALL Java_cpuid_CPUIDJNIWrapper_getCPUID
  (JNIEnv *env, jclass clazz,jint level){
	int retSize = 5;
    jintArray position=(jintArray)(*env)->NewIntArray(env,retSize);
	if(position==NULL){
		return NULL;
	}
	jint *f = calloc(retSize, sizeof(jint));
	unsigned int a=0;
	unsigned int b=1;
	unsigned int c=0;
	unsigned int d=0;
	unsigned int e=0;
	int result = __get_cpuid(level,&b,&c,&d,&e);
	f[0] = a;
	f[1] = b;
	f[2] = c;
	f[3] = d;
	f[4] = e;
	 (*env)->SetIntArrayRegion(env,position,0,retSize,(jint*)f);
	 return position;
}

JNIEXPORT jint JNICALL Java_cpuid_CPUIDJNIWrapper_getMaxCPUID
  (JNIEnv *env, jclass clazz, jint extended, jint signature){
	int sig=0;
	int max_level = __get_cpuid_max (extended, &sig);
	return max_level;
 }