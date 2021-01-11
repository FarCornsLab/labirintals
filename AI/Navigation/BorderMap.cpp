#include "BorderMap.h"

BordersInPoint BorderMap::get(const Position& position) const {
    BordersInPoint answer;
    BorderPosition border = BorderPosition::start;
    while (true) {
        answer[border] = getBorder(position, border);
        if (border == BorderPosition::max) {
            return answer;
        }
        border = static_cast<BorderPosition>(static_cast<unsigned int>(border) + 1);
    }
}

void BorderMap::set(const Position& position, const BordersInPoint& value) {
    BorderPosition border = BorderPosition::start;
    while (true) {
        setBorder(position, border, value[border]);
        if (border == BorderPosition::max) {
            return;
        }
        border = static_cast<BorderPosition>(static_cast<unsigned int>(border) + 1);
    }
}

BorderType BorderMap::getBorder(const Position& position, const BorderPosition& border_position) const {
    return getBorder(isHorizontal(border_position) ?  horizontal_borders_ : vertical_borders_,
                     position + getOffset(border_position));
}


void BorderMap::setBorder(const Position& position, const BorderPosition& border_position, const BorderType& value) {
    setBorder(isHorizontal(border_position) ?  horizontal_borders_ : vertical_borders_,
              position + getOffset(border_position),
              value);
}

Position BorderMap::getOffset(const BorderPosition& border_position) {
    static const std::array<Position, static_cast<unsigned int>(BorderPosition::max) + 1> offsets = {{
                                                                                                      {0, 0},
                                                                                                      {1, 0},
                                                                                                      {0, 1},
                                                                                                      {0, 0}
                                                                                              }};

    return offsets[static_cast<unsigned int>(border_position)];
}

BorderType BorderMap::getBorder(const MapData& data, const Position& position) {
    auto it = data.find(position);
    if (it != data.end()) {
        return it->second;
    }
    return BorderType::unknown;
}

void BorderMap::setBorder(MapData& data, const Position& position, const BorderType& value) {
    if (BorderType::unknown == value) {
        data.erase(position);
    } else {
        data[position] = value;
    }
}

bool BorderMap::isHorizontal(const BorderPosition& border_position) {
    return BorderPosition::up == border_position || BorderPosition::down == border_position;
}
