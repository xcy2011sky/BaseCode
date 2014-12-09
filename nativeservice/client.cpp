

#define LOG_TAG "NClient"
#define LOG_NDEBUG 0
#include <stdio.h>
#include <stdlib.h>
#include <errno.h>

#include <binder/IPCThreadState.h>
#include <binder/ProcessState.h>
#include <binder/IServiceManager.h>

#include <utils/Log.h>

#include "INativeService.h"

using namespace android;
int main() {

	ALOGV("NClient onCreate");
	printf("NClient onCreate\n");
	    sp<IServiceManager> sm = defaultServiceManager();
            sp<IBinder> binder = sm->getService(String16("native.service"));
	   
            if (binder != 0) {
                Vector<String16> args;
		 sp<INativeService> nservice = interface_cast<INativeService>(binder);
		 nservice->putStr("this is xcy2011sky \n");
		return 0;
            }

   return -1;
}


