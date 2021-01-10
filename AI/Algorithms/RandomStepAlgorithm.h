#ifndef AI_RANDOMSTEPALGORITHM_H
#define AI_RANDOMSTEPALGORITHM_H

#include "ExecutorAlgorithm.h"

class RandomStepAlgorithm: public ExecutorAlgorithm {
public:
    std::optional<StepDirection> chooseStep() final;

};


#endif //AI_RANDOMSTEPALGORITHM_H
