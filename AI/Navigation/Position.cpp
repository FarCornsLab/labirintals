#include "Position.h"

Position operator+(const Position& lhs, const Position&rhs) {
    return { lhs.x + rhs.x, lhs.y + rhs.y };
}

Position operator-(const Position& rhs, const Position& lhs) {
    return { lhs.x - rhs.x, lhs.y - rhs.y };
}

int operator<=>(const Position& lhs, const Position& rhs) {
    return (lhs.x == rhs.x) ? lhs.y - rhs.y : lhs.x - rhs.x;
}
