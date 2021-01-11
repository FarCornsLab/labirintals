#ifndef AI_FIELD_H
#define AI_FIELD_H

#include <array>
#include <string>
#include <optional>

#include "FieldDescription.h"
#include "Map.h"

/** Game board interaction class */
class Field {
public:
    /** Made step */
    std::optional<BordersInPoint> doStep(const StepDirection direction) {
        std::array<std::optional<BordersInPoint> (Field::*)(), static_cast<unsigned int>(StepDirection::max) + 1> do_steps_func = {
                &Field::stepUp,
                &Field::stepRight,
                &Field::stepDown,
                &Field::stepLeft
        };

        return (*this.*(do_steps_func[static_cast<unsigned int>(direction)]))();
    }

    /** Connect to the field. If it successful return borders */
    virtual std::optional<BordersInPoint> connect() = 0;

protected:
    virtual std::optional<BordersInPoint> stepUp() = 0;
    virtual std::optional<BordersInPoint> stepRight() = 0;
    virtual std::optional<BordersInPoint> stepDown() = 0;
    virtual std::optional<BordersInPoint> stepLeft() = 0;
};


#endif //AI_FIELD_H
