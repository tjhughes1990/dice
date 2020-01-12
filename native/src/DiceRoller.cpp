#include "DiceRoller.h"

JNIEXPORT jobject JNICALL
Java_dice_service_roll_DiceRoller_performRolls(JNIEnv *, jclass, jobjectArray, jint) {

    printf("Hello\n");

    return nullptr;
}
