#ifndef AI_BORDERMAP_H
#define AI_BORDERMAP_H

#include <map>

#include "FieldDescription.h"
#include "Position.h"

/** Created border mep by player step */
class BorderMap {
private:
    typedef std::map<Position, BorderType> MapData;

public:
    /** Getter / setter all borders from a position */
    BordersInPoint get(const Position& position) const;
    void set(const Position& position, const BordersInPoint& value);

    /** Getter / setter the border from a position */
    BorderType getBorder(const Position& position, const BorderPosition& border_position) const;
    void setBorder(const Position& position, const BorderPosition& border_position, const BorderType& value);

private:
    MapData horizontal_borders_;
    MapData vertical_borders_;

    static Position getOffset(const BorderPosition& border_position);

    static BorderType getBorder(const MapData& data, const Position& position);
    static void setBorder(MapData& data, const Position& position, const BorderType& value);

    static bool isHorizontal(const BorderPosition& border_position);
};

#endif //AI_BORDERMAP_H
