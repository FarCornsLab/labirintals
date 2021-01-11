#include "LucaTremoAlgorithm.h"

#include <algorithm>
#include <iostream>
#include <random>
#include <vector>

std::optional<StepDirection> LucaTremoAlgorithm::chooseStep() {
    auto exit = map_->exit();
    if (exit.has_value()) {
        return static_cast<StepDirection>(static_cast<unsigned int>(exit.value()));
    }

    /** Get all possible steps */
    std::vector<StepDirection> possible_new_steps;
    std::vector<StepDirection> possible_old_steps;
    for (unsigned int i = static_cast<unsigned int>(StepDirection::start);
         i <= static_cast<unsigned int>(StepDirection::max); i++) {
        StepDirection step = static_cast<StepDirection>(i);

        unsigned int attended = 0;
        if (map_->isCanMakeStep(step) &&
            (map_->lastStep() != step) &&
            ((attended = map_->numbAttended(step)) <= 2)) {
            if (attended == 0) {
                possible_new_steps.push_back(step);
            } else {
                possible_old_steps.push_back(step);
            }
        }
    }

    /** If don't have possible steps, go back */
    if (possible_new_steps.empty() && possible_old_steps.empty()) {
        auto last_step = map_->lastStep();
        if (last_step.has_value()) {
            return backStep(last_step.value());
        }
        std::cerr << "Wrong situation!" << std::endl;
        return {};
    }

    return chooseRandom(possible_new_steps.empty() ? possible_old_steps : possible_new_steps);
}

StepDirection LucaTremoAlgorithm::chooseRandom(const std::vector<StepDirection>& steps) {
    static std::random_device rd;
    static std::mt19937 gen(rd());
    std::uniform_int_distribution<int> uid1(0, steps.size() - 1);
    return steps[uid1(gen)];
}
