#ifndef AI_SERVERMANAGER_H
#define AI_SERVERMANAGER_H

#include "ManagerExecutor.h"
#include "Client.h"

class ServerManager: public ManagerExecutor {
public:
    struct ServerConfig {
        std::string address;
        unsigned short port;
    };

    ServerManager(const ServerConfig& server_config, std::shared_ptr<ExecutorAlgorithm> algorithm, const std::string& bot_name);

    bool connect() override;
    bool doStep() override;
    bool isIWin() override;
    std::optional<std::vector<std::string>> getWinners() override;

protected:
    std::string getConnectionParams() const override;

private:
    std::shared_ptr<Client> client_;
    ServerConfig connection_config_;
    std::string bot_name_;
};


#endif //AI_SERVERMANAGER_H
