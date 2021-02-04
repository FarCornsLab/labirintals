#ifndef AI_MAP_H
#define AI_MAP_H

#include <stack>
#include <optional>

#include "AttendanceMap.h"
#include "BorderMap.h"
#include "Position.h"

/** Step direction */
enum class StepDirection: unsigned int {
    start = 0,
    up = start,
    right,
    down,
    left,
    max = left
};

/** Return reverse step */
StepDirection backStep(const StepDirection direction);

/** Map of the player */
class Map {
public:
    /** Return direction of a exit, if it exist in player neighborhood */
    std::optional<BorderPosition> exit() const;

    /** Check, if player can make a step */
    bool isCanMakeStep(const StepDirection direction) const;

    /** Return number attend in neighborhood unit */
    unsigned int numbAttended(const StepDirection direction) const;

    /** Set number attend */
    void setNumbAttended(unsigned int n);

    /** Made step */
    void makeStep(const StepDirection direction, const BordersInPoint& borders);

    /** Set borders in current position */
    void setBorders(const BordersInPoint& borders);

    /** Return last step taken */
    std::optional<StepDirection> lastStep() const;

    /** Return player position */
    Position getPlayerPosition() const { return player_position_; }

    /** Return borders in the position */
    BordersInPoint getBorders(const Position& position) const { return border_map_.get(position); }

protected:
    Position player_position_ = {0, 0};
    BorderMap border_map_;
    AttendanceMap attendance_map_;

    /** Track of player steps */
    std::stack<StepDirection> track_;

private:
    Position getOffsetted(const StepDirection direction) const;
};


#endif //AI_MAP_H
