#ifndef AI_FIELDDESCRIPTION_H
#define AI_FIELDDESCRIPTION_H

#include <array>

/** Types of border */
enum class BorderType: unsigned int {
    unknown = 0,
    open,
    close,
    is_exit
};

/** Position of border */
enum class BorderPosition: unsigned int {
    start = 0,
    up = start,
    right,
    down,
    left,
    max = left
};

/** Borders around current position */
class BordersInPoint {
public:
    BorderType& operator[](BorderPosition position) { return data_[static_cast<unsigned int>(position)]; }
    BorderType operator[](BorderPosition position) const { return data_[static_cast<unsigned int>(position)]; }

private:
    std::array<BorderType, static_cast<unsigned int>(BorderPosition::max)> data_;
};

#endif //AI_FIELDDESCRIPTION_H
