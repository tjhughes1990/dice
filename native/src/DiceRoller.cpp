#include "DiceRoller.h"

#include "DiceRollerImpl.hpp"

JNIEXPORT void JNICALL
Java_dice_service_roll_DiceRoller_performRolls(JNIEnv *env, jobject thisObj, jobjectArray diceRolls, jint diceRollsSize) {

    std::random_device rd;
    const unsigned int seed(rd());

    const DiceRollerImpl diceRoller(env, seed);
    for(int i = 0; i < diceRollsSize; i++) {
        jobject diceRoll(env->GetObjectArrayElement(diceRolls, i));
        diceRoller.rollDice(diceRoll);
    }
}
