/**
 * @file DiceRollerImpl header file.
 */
#ifndef DICE_ROLLLER_IMPL_HPP
#define DICE_ROLLLER_IMPL_HPP

#include <jni.h>
#include <memory>
#include <random>

/**
 * @brief Dice roller implementation class.
 */
class DiceRollerImpl {
public:
    /**
     * @brief Constructor.
     *
     * @param a_env_p the JNI environment.
     * @param a_seed the seed to use.
     */
    DiceRollerImpl(JNIEnv* a_env_p, const unsigned int a_seed);

    /**
     * @brief Destructor.
     */
    virtual ~DiceRollerImpl();

    /**
     * @brief Perform a single dice roll.
     *
     * @param a_diceRoll_p a single Java dice roll object.
     */
    void rollDice(jobject a_diceRoll_p) const;

private:
    JNIEnv* m_env_p;
    const jclass m_rollerClass_p;
    jmethodID m_minMethod_p;
    jmethodID m_maxMethod_p;
    jmethodID m_rollNumMethod_p;
    jmethodID m_setResultMethod_p;

    const unsigned int m_seed;
};

#endif
