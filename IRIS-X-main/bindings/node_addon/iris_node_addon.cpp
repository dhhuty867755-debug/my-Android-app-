#include <node_api.h>
#include <stdio.h>
#include <stdlib.h>

napi_value InitializeNativeEngine(napi_env env, napi_callback_info info) {
    napi_value result;
    napi_get_boolean(env, true, &result);
    printf("[Node-API] Initialized IRIS Native Engine for Node.js process\n");
    return result;
}

napi_value Init(napi_env env, napi_value exports) {
    napi_status status;
    napi_value fn;

    status = napi_create_function(env, NULL, 0, InitializeNativeEngine, NULL, &fn);
    if (status != napi_ok) return NULL;

    status = napi_set_named_property(env, exports, "initializeEngine", fn);
    if (status != napi_ok) return NULL;

    return exports;
}

NAPI_MODULE(NODE_GYP_MODULE_NAME, Init)
