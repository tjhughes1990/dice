/**
 * @file DiceRoller JNI implementation file.
 */
#include "DiceRoller.h"

#include "DiceRollerImpl.hpp"
#include "SpecDefs.hpp"

#include <random>

JNIEXPORT jlong JNICALL
Java_dice_service_roll_DiceRoller_getRandomDeviceInstance(UNUSED JNIEnv *a_env, UNUSED jclass a_class) {
    std::random_device *rd = new std::random_device();
    return reinterpret_cast<jlong>(rd);
}

JNIEXPORT void JNICALL
Java_dice_service_roll_DiceRoller_performRolls(JNIEnv *a_env, UNUSED jobject a_thisObj, jlong a_randomDev,
        jobjectArray a_diceRolls, jint a_diceRollsSize) {

    std::random_device *rd = reinterpret_cast<std::random_device*>(a_randomDev);
    const DiceRollerImpl diceRoller(a_env, rd);

    for(unsigned int i = 0; i < static_cast<unsigned int>(a_diceRollsSize); i++) {
        jobject diceRoll(a_env->GetObjectArrayElement(a_diceRolls, i));
        diceRoller.rollDice(diceRoll);
    }
}
