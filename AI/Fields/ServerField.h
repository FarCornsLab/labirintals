#ifndef AI_SERVERFIELD_H
#define AI_SERVERFIELD_H

#include "Field.h"
#include "Client.h"

class ServerField: public Field {
public:
    ServerField(std::shared_ptr<Client> client) : client_(client) {}

private:
    std::shared_ptr<Client> client_;
};


#endif //AI_SERVERFIELD_H
