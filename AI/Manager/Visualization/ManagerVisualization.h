#ifndef AI_MANAGERVISUALIZATION_H
#define AI_MANAGERVISUALIZATION_H

#include <utility>

#include "Manager.h"
#include "Executor/ManagerExecutor.h"

/** Manager for visualization of step */
class ManagerVisualization: public Manager {
public:
    ManagerVisualization(std::shared_ptr<ManagerExecutor>&& executor) : executor_(std::move(executor)) {}

    /** Connected to the game */
    bool connect() final {
        beforeConnect(executor_->getConnectionParams());
        bool result = executor_->doStep();
        afterConnect(result);
        return result;
    }

    /** Did step */
    bool doStep() final {
        bool result = executor_->doStep();
        afterStep(result);
        return result;
    }

    /** Check if the player is win */
    bool isIWin() final { return executor_->isIWin(); }

    /** If game over return list of winners, else nullopt */
    std::optional<std::vector<std::string>> getWinners() final { return executor_->getWinners(); }

protected:
    /** Method executed before connection */
    virtual void beforeConnect(const std::string& connect_params) = 0;
    /** Method executed after connection */
    virtual void afterConnect(bool connect_result) = 0;

    /** Method executed after step */
    virtual void afterStep(bool step_result) = 0;

    /** Return current player position on the map */
    Position getCurrentPosition() const { return executor_->getCurrentPosition(); }

    /** Return borders in the position */
    BordersInPoint getBorders(const Position& position) const { executor_->getBorders(position); };
private:
    std::shared_ptr<ManagerExecutor> executor_;
};

#endif //AI_MANAGERVISUALIZATION_H
