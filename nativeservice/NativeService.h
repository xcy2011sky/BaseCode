
#ifndef ANDROID_NATIVE_SERVICE_H
#define ANDROID_NATIVE_SERVICE_H

#include "INativeService.h"
#include <binder/BinderService.h>

#include <utils/Log.h>

#define LOG_TAG "NativeService"
#define LOG_NDEBUG 0

//E/ServiceManager(  204): add_service('native.service',0xd) uid=1013 - PERMISSION DENIED


namespace android {

class NativeService : public BinderService<NativeService>, public BnNativeService
{
	friend class BinderService<NativeService>;   


public :
	NativeService() : BnNativeService(){
		ALOGV("NativeSerice onCreate ");
	}
	virtual ~NativeService(){}
	

	static const char * getServiceName(){
		return "native.service";
	}
	 virtual status_t onTransact(uint32_t code, const Parcel& data, Parcel* reply,
	                                uint32_t flags)
	 {
		 return BnNativeService::onTransact(code, data, reply, flags);
	 }
	virtual void onFirstRef() { }

        void putStr(const char *str)
	    {
	    	  ALOGV(" nativeservice.str=%s\n",str);
		   printf("nativeservice.str=%s\n",str);
	    }
};
}
#endif
