/*
 * INativeService.cpp
 *
 *  Created on: 2014年12月4日
 *      Author: chongyan_xu
 */

#define LOG_TAG "INativeService"

#include "INativeService.h"

namespace android {



class BpNativeService : public BpInterface <INativeService>
{
public:
	void putStr(const char *str)
	{
			Parcel data,reply;
			data.writeInterfaceToken(INativeService::getInterfaceDescriptor());
			data.writeCString(str);
			status_t status=remote()->transact(PUT,data,&reply);
			
			// return res;
	}
	BpNativeService(const sp<IBinder>&impl)
		:BpInterface<INativeService>(impl){}	

	
	
		
};





IMPLEMENT_META_INTERFACE(NativeService,"NativeService");


status_t BnNativeService::onTransact(uint32_t code,const Parcel & data,Parcel *reply,uint32_t flags)
{
	switch(code)
	{
	case PUT:
	{
		CHECK_INTERFACE(INativeSerice,data,reply);
		
		const char *name=data.readCString();
		
		putStr(name);
		
	    return NO_ERROR;

	}
	case PUT_STR:
	{
	    return NO_ERROR;
	}
	default:
		return BBinder::onTransact(code,data,reply,flags);
	}
}
}
