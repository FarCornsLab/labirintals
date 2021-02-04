#ifndef AI_ATTENDANCEMAP_H
#define AI_ATTENDANCEMAP_H

#include <map>

#include "Position.h"

/** Map with number attend */
class AttendanceMap {
public:
    /** Return number attends in position */
    unsigned int numbAttended(const Position& position) const {
        auto it = data_.find(position);
        return it != data_.end() ? it->second : 0;
    }

    /** Increment attends in position */
    void addAttended(const Position& position) {
        auto it = data_.find(position);
        if (it == data_.end()) {
            data_[position] = 1;
        } else {
            (it->second)++;
        }
    }

    void setAttended(const Position& position, unsigned int value) {
        data_[position] = value;
    }

private:
    std::map<Position, unsigned int> data_;
};

#endif //AI_ATTENDANCEMAP_H
