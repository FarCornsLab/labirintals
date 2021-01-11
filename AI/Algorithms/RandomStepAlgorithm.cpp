#include "RandomStepAlgorithm.h"

#include <random>
#include <vector>

std::optional<StepDirection> RandomStepAlgorithm::chooseStep() {
    auto exit = map_->exit();
    if (exit.has_value()) {
        return static_cast<StepDirection>(static_cast<unsigned int>(exit.value()));
    }

    std::vector<std::pair<StepDirection, unsigned int>> possible_step;
    unsigned int attended_min = UINT_MAX;

    for (unsigned int i = static_cast<unsigned int>(StepDirection::start);
         i <= static_cast<unsigned int>(StepDirection::max); i++) {
        StepDirection step = static_cast<StepDirection>(i);

        if (map_->isCanMakeStep(step)) {
            unsigned int attended = map_->numbAttended(step);
            possible_step.emplace_back(step, attended);
            attended_min = std::min(attended_min, attended);
        }
    }

    std::vector<std::pair<StepDirection, unsigned int>> possible_min_step;
    std::copy_if (possible_step.begin(),
                  possible_step.end(),
                  std::back_inserter(possible_min_step),
                  [attended_min](const std::pair<StepDirection, unsigned int>&i){ return i.second == attended_min; } );

    static std::random_device rd;
    static std::mt19937 gen(rd());
    std::uniform_int_distribution<int> uid1(0, possible_min_step.size() - 1);
    return possible_min_step[uid1(gen)].first;
}
