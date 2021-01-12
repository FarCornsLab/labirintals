#ifndef AI_MANAGER_H
#define AI_MANAGER_H

#include <optional>
#include <vector>

/** Player management class */
class Manager {
public:
    /** Connected manager with the game. Return true if connection successful. */
    virtual bool connect() = 0;

    /** Return false if game over or in error state */
    virtual bool doStep() = 0;

    /** Return true if current manager win */
    virtual bool isIWin() = 0;

    /** Return list of winners after end of the game*/
    virtual std::optional<std::vector<std::string>> getWinners() = 0;

    /** Disconnected manager with the game. Return true if disconnection successful. */
    virtual bool disconnect() = 0;
};

#endif //AI_MANAGER_H
