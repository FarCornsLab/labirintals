#include "ServerManager.h"
#include "ServerField.h"
#include "Map.h"

ServerManager::ServerManager(const ServerConfig& server_config,
                             std::shared_ptr<ExecutorAlgorithm> algorithm,
                             const std::string& bot_name) : ManagerExecutor(algorithm),
                                                            connection_config_(server_config),
                                                            bot_name_(bot_name){
    client_ = std::make_unique<Client>(connection_config_.address, connection_config_.port);
    field_ = std::make_unique<ServerField>(client_);
    map_ = std::make_unique<Map>();
    algorithm_->setMap(map_);
}

bool ServerManager::connect() {
    client_->connect();
    //TODO: get answer
}

bool ServerManager::doStep() {
    auto step = algorithm_->chooseStep();
    if (!step.has_value()) {
        return false;
    }

    map_->makeStep(step.value(), field_->doStep(step.value()));
    return true;
}

bool ServerManager::isIWin() {
    //TODO:
    return false;
}

std::optional<std::vector<std::string>> ServerManager::getWinners() {
    //TODO:
    return {};
}

std::string ServerManager::getConnectionParams() const {
    return "Address: " + connection_config_.address + ". Port: " + std::to_string(connection_config_.port) + ".";
}
