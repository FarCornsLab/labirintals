#include "Map.h"

#include <array>

StepDirection backStep(const StepDirection direction) {
    const std::array<StepDirection, static_cast<unsigned int>(StepDirection::max) + 1> back_step = {{
        StepDirection::down,
        StepDirection::left,
        StepDirection::up,
        StepDirection::right
    }};

    return back_step[static_cast<unsigned int>(direction)];
}

std::optional<BorderPosition> Map::exit() const {
    auto borders = border_map_.get(player_position_);
    for (BorderPosition position = BorderPosition::start;
         static_cast<unsigned int>(position) <= static_cast<unsigned int>(BorderPosition::max);
         position = static_cast<BorderPosition>(static_cast<unsigned int>(position) + 1)) {
        if (BorderType::is_exit == borders[position]) {
            return position;
        }
    }
    return {};
}

bool Map::isCanMakeStep(const StepDirection direction) const {
    auto border =
            border_map_.getBorder(player_position_, static_cast<BorderPosition>(static_cast<unsigned int>(direction)));
    return BorderType::open == border || BorderType::is_exit == border;
}

unsigned int Map::numbAttended(const StepDirection direction) const {
    return attendance_map_.numbAttended(getOffsetted(direction));
}

void Map::setNumbAttended(unsigned int n) {
    attendance_map_.setAttended(player_position_, n);
}

void Map::makeStep(const StepDirection direction, const BordersInPoint& borders) {
    attendance_map_.addAttended(player_position_);
    player_position_ = getOffsetted(direction);
    border_map_.set(player_position_, borders);

    if (!track_.empty() && backStep(track_.top()) == direction) {
        track_.pop();
    } else {
        track_.push(direction);
    }
}

void Map::setBorders(const BordersInPoint& borders) {
    border_map_.set(player_position_, borders);
}

std::optional<StepDirection> Map::lastStep() const {
    return  track_.empty() ? std::nullopt : std::optional<StepDirection>(track_.top());
}

Position Map::getOffsetted(const StepDirection direction) const {
    const std::array<Position, static_cast<unsigned int>(StepDirection::max) + 1> offset = {{
            {0, 1},
            {1, 0},
            {0, -1},
            {-1, 0}
    }};

    return player_position_ + offset[static_cast<unsigned int>(direction)];
}
