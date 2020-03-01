/**
 * @file DiceRoller JNI implementation file.
 */
#include "DiceRoller.h"

#include "DiceRollerImpl.hpp"
#include "SpecDefs.hpp"

JNIEXPORT void JNICALL
Java_dice_service_roll_DiceRoller_performRolls(JNIEnv *env, UNUSED jobject thisObj, jobjectArray diceRolls, jint diceRollsSize) {

    std::random_device rd;
    const unsigned int seed(rd());

    const DiceRollerImpl diceRoller(env, seed);
    for(unsigned int i = 0; i < static_cast<unsigned int>(diceRollsSize); i++) {
        jobject diceRoll(env->GetObjectArrayElement(diceRolls, i));
        diceRoller.rollDice(diceRoll);
    }
}
