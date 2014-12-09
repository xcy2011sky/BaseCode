/*
 * INativeService.h
 *
 *  Created on: 2014年12月4日
 *      Author: chongyan_xu
 */

#ifndef INATIVESERVICE_H_
#define INATIVESERVICE_H_


#include <binder/Parcel.h>
#include <binder/IInterface.h>


namespace android {

	enum {
			PUT = IBinder::FIRST_CALL_TRANSACTION,
			PUT_STR=PUT+1,
		};

class INativeService : public IInterface{
public:
	DECLARE_META_INTERFACE(NativeService);
	

	virtual void put(const char *str)=0;
	
};

class BnNativeService : public BnInterface<INativeService>
{
public :
		virtual status_t onTransact(uint32_t code,const Parcel& data,Parcel* reply,uint32_t flags=0);
};

}
#endif /* INATIVESERVICE_H_ */
