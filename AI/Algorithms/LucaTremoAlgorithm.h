#ifndef AI_LUCATREMOALGORITHM_H
#define AI_LUCATREMOALGORITHM_H

#include <optional>

#include "ExecutorAlgorithm.h"

class LucaTremoAlgorithm: public ExecutorAlgorithm {
public:
    std::optional<StepDirection> chooseStep() final;

private:
    static StepDirection chooseRandom(const std::vector<StepDirection>& steps);
};


#endif //AI_LUCATREMOALGORITHM_H
