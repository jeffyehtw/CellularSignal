#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_tw_edu_nctu_cs_nems_cellularsignal_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
