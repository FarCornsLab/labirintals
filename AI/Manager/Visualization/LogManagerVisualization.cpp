#include "LogManagerVisualization.h"

void LogManagerVisualization::beforeConnect(const std::string& connect_params) {
    out_ << "Connection with parameters:" << connect_params << std::endl;
}

void LogManagerVisualization::afterConnect(bool connect_result) {
    out_ << "Connection " << (connect_result ? "successful" : "failed") << "." << std::endl;
}

std::string unitToString(const BordersInPoint& unit) {
    static const std::array<std::string, static_cast<unsigned int>(BorderType::max) + 1> horizontal = {
            "~~~",
            "   ",
            "---",
            ". ."
    };

    static const std::array<std::string, static_cast<unsigned int>(BorderType::max) + 1> vertical = {
            "?",
            " ",
            "|",
            ":"
    };

    static const std::string split = "+";

    return split + horizontal[static_cast<unsigned int>(unit[BorderPosition::up])] + split + "\n" +
           vertical[static_cast<unsigned int>(unit[BorderPosition::left])] + "   " +
           vertical[static_cast<unsigned int>(unit[BorderPosition::right])] + "\n" +
           split + horizontal[static_cast<unsigned int>(unit[BorderPosition::down])] + split + "\n";

}

void LogManagerVisualization::beforeStep() {
    auto position = getCurrentPosition();
    auto borders = getBorders(position);
    out_ <<
         "Position: " << position.x << ", " << position.y << ";\n" <<
         "Borders: \n"  <<
         unitToString(borders) <<
         std::endl;
}

void LogManagerVisualization::afterStep(bool step_result) {
    if (!step_result && !getWinners().has_value()) {
        out_ << "Error in step!" << std::endl;
        return;
    }
}

void LogManagerVisualization::beforeDisconnect() {
    auto winners = getWinners();
    if (winners.has_value()) {
        out_ << "Game over!" << std::endl;
        out_ << "You " << ( isIWin() ? "win" : "lost") << "." << std::endl;
        out_ << "Winners:" << std::endl;
        for (const auto& i: winners.value()) {
            out_ << "  - " << i << std::endl;
        }
    } else {
        out_ << "Disconnected before game end." << std::endl;
    }
}

void LogManagerVisualization::afterDisconnect(bool result) {
    out_ << "Disconnection: " << (result ? "success" : "failed") << "." << std::endl;
}
