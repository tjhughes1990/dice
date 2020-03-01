/**
 * @file DiceRollerImpl implementation file.
 */
#include "DiceRollerImpl.hpp"

DiceRollerImpl::DiceRollerImpl(JNIEnv* a_env_p, const unsigned int a_seed)
    : m_env_p(a_env_p)
    , m_rollerClass_p(m_env_p->FindClass("dice/service/types/IDiceRollType"))
    , m_minMethod_p(m_env_p->GetMethodID(m_rollerClass_p, "getMinResult", "()I"))
    , m_maxMethod_p(m_env_p->GetMethodID(m_rollerClass_p, "getMaxResult", "()I"))
    , m_rollNumMethod_p(m_env_p->GetMethodID(m_rollerClass_p, "getRollNumber", "()I"))
    , m_setResultMethod_p(m_env_p->GetMethodID(m_rollerClass_p, "setSumResult", "(I)V"))
    , m_seed(a_seed) {
}

DiceRollerImpl::~DiceRollerImpl() { }

void
DiceRollerImpl::rollDice(jobject a_diceRoll_p) const {
    const jint min(m_env_p->CallIntMethod(a_diceRoll_p, m_minMethod_p));
    const jint max(m_env_p->CallIntMethod(a_diceRoll_p, m_maxMethod_p));
    const jint n(m_env_p->CallIntMethod(a_diceRoll_p, m_rollNumMethod_p));

    std::mt19937_64 rngEng(m_seed);
    std::uniform_int_distribution<int> rng(min, max);

    int sum = 0;
    for(int i = 0; i < n; i++) {
        sum += rng(rngEng);
    }

    m_env_p->CallObjectMethod(a_diceRoll_p, m_setResultMethod_p, sum);
}
