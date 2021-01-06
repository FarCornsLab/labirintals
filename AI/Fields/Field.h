#ifndef AI_FIELD_H
#define AI_FIELD_H

#include <array>
#include <string>
#include <optional>

#include "FieldDescription.h"
#include "Map.h"

/** Result a step */
struct StepResult {
    /** If game over got a winner name */
    std::optional<std::vector<std::string>> names_winner;
    /** Borders around a player */
    BordersInPoint borders;
};

/** Game board interaction class */
class Field {
public:
    /** Made step */
    StepResult doStep(const StepDirection direction) {
        std::array<StepResult (Field::*)(), static_cast<unsigned int>(StepDirection::max) + 1> do_steps_func = {
                &Field::stepUp,
                &Field::stepRight,
                &Field::stepDown,
                &Field::stepLeft
        };

        return (*this.*(do_steps_func[direction]))();
    }

protected:
    virtual StepResult stepUp() = 0;
    virtual StepResult stepRight() = 0;
    virtual StepResult stepDown() = 0;
    virtual StepResult stepLeft() = 0;
};


#endif //AI_FIELD_H
