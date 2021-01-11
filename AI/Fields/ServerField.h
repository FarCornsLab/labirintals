#ifndef AI_SERVERFIELD_H
#define AI_SERVERFIELD_H

#include "Field.h"

#include <vector>

#include "Client.h"

class ServerField: public Field {
public:
    ServerField(std::shared_ptr<Client> client) : client_(client) {}

    std::optional<BordersInPoint> connect() final;

protected:
    std::optional<BordersInPoint> stepUp() final { return makeStep("up"); }
    std::optional<BordersInPoint> stepRight() final { return makeStep("right"); }
    std::optional<BordersInPoint> stepDown() final { return makeStep("down"); }
    std::optional<BordersInPoint> stepLeft() final { return makeStep("left"); }

private:
    std::shared_ptr<Client> client_;
    int stepId_ = 1;

    static std::optional<BordersInPoint> getBorders(const std::vector<std::string>& data);

    std::optional<BordersInPoint> makeStep(const std::string& direction);
};


#endif //AI_SERVERFIELD_H
