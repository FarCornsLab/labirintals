#ifndef AI_LOGMANAGERVISUALIZATION_H
#define AI_LOGMANAGERVISUALIZATION_H

#include "ManagerVisualization.h"

#include <iostream>
#include <ostream>

class LogManagerVisualization: public ManagerVisualization {
public:
    LogManagerVisualization(std::shared_ptr<ManagerExecutor> executor,
                            std::ostream& out = std::cout) : ManagerVisualization(std::move(executor)),
                                                             out_(out) {}

protected:
    void beforeConnect(const std::string& connect_params) override;
    void afterConnect(bool connect_result) override;

    void beforeStep() override;
    void afterStep(bool step_result) override;

private:
    std::ostream& out_;
};


#endif //AI_LOGMANAGERVISUALIZATION_H
