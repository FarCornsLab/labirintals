#include "LogManagerVisualization.h"

void LogManagerVisualization::beforeConnect(const std::string& connect_params) {
    out_ << "Connection with parameters:" << connect_params << std::endl;
}

void LogManagerVisualization::afterConnect(bool connect_result) {
    out_ << "Connection " << (connect_result ? "successful" : "failed") << "." << std::endl;
}

void LogManagerVisualization::afterStep(bool step_result) {
    if (!step_result) {
        out_ << "Error in step!" << std::endl;
        return;
    }

    auto position = getCurrentPosition();
    auto borders = getBorders(position);
    out_ << "Step: "     << step_counter_++ << ";" <<
            "Position: " << position.x << ", " << position.y << "; \n" <<
            "Borders: "  << static_cast<unsigned int>(borders[BorderPosition::up])    << "u " <<
                            static_cast<unsigned int>(borders[BorderPosition::right]) << "r " <<
                            static_cast<unsigned int>(borders[BorderPosition::down])  << "d " <<
                            static_cast<unsigned int>(borders[BorderPosition::left])  << "l " <<
            std::endl;
}
