/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#define LOG_TAG "NService"
#define LOG_NDEBUG 0
#include <stdio.h>
#include <stdlib.h>
#include <errno.h>

#include <binder/IPCThreadState.h>
#include <binder/ProcessState.h>
#include <binder/IServiceManager.h>

#include <utils/Log.h>

#include "NativeService.h"


int main() {

	ALOGV("NSerice onCreate ");
   	android::NativeService::instantiate();
	ALOGV("NativeSerice register on sm ");
        android::ProcessState::self()->startThreadPool();
        android::IPCThreadState::self()->joinThreadPool();

    while(1) {
        sleep(1000);
    }

   return 0;
}


