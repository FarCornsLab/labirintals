#ifndef AI_MANAGEREXECUTOR_H
#define AI_MANAGEREXECUTOR_H

#include <string>

#include "ExecutorAlgorithm.h"
#include "Field.h"
#include "Manager.h"
#include "Map.h"

/** Manager for execution bot steps */
class ManagerExecutor: public Manager {
public:
    ManagerExecutor(std::shared_ptr<ExecutorAlgorithm> algorithm) : algorithm_(algorithm) {}

    friend class ManagerVisualization;

    virtual bool connect() = 0;
    virtual bool doStep() = 0;
    virtual bool isIWin() = 0;
    virtual std::optional<std::vector<std::string>> getWinners() = 0;

protected:
    /** Explored map */
    std::shared_ptr<Map> map_;
    /** Game field */
    std::shared_ptr<Field> field_;
    /** Step selection algorithm */
    std::shared_ptr<ExecutorAlgorithm> algorithm_;

    /** Return current player position on the map */
    Position getCurrentPosition() const { return map_->getPlayerPosition(); }

    /** Return borders in the position */
    BordersInPoint getBorders(const Position& position) const { return  map_->getBorders(position); }

    /** Return connection params in string */
    virtual std::string getConnectionParams() const = 0;
};

#endif //AI_MANAGEREXECUTOR_H
