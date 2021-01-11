#ifndef AI_EXECUTORALGORITHM_H
#define AI_EXECUTORALGORITHM_H

#include <optional>

#include "Map.h"

/** Algorithm for stepping */
class ExecutorAlgorithm {
public:
    /** Choose step direction. Return nullopt in error case. */
    virtual std::optional<StepDirection> chooseStep() = 0;

    /** Set map */
    void setMap(std::shared_ptr<const Map> map) { map_ = map; }

protected:
    /** Map of game */
    std::shared_ptr<const Map> map_ = nullptr;
};

#endif //AI_EXECUTORALGORITHM_H
