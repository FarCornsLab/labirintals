#ifndef AI_POSITION_H
#define AI_POSITION_H

/** Position on map */
struct Position {
    int x;
    int y;
};

Position operator+(const Position& lhs, const Position&rhs);
Position operator-(const Position& rhs, const Position& lhs);
int operator<=>(const Position& lhs, const Position& rhs);

#endif //AI_POSITION_H
