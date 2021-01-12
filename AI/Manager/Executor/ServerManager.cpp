#include <algorithm>

#include "ServerManager.h"
#include "ServerField.h"
#include "Map.h"

ServerManager::ServerManager(const ServerConfig& server_config,
                             std::shared_ptr<ExecutorAlgorithm> algorithm,
                             const std::string& bot_name) : ManagerExecutor(algorithm),
                                                            connection_config_(server_config),
                                                            bot_name_(bot_name){
    client_ = std::make_shared<Client>(connection_config_.address, connection_config_.port);
    field_ = std::make_shared<ServerField>(client_);
    map_ = std::make_shared<Map>();
    algorithm_->setMap(map_);
}

bool ServerManager::connect() {
    if (!client_->connect()) {
        return false;
    }

    s_cmd::Connection request(bot_name_);
    auto answer = client_->request<s_cmd::ConnectionAnswer>(&request);
    if (!answer.has_value()) {
        return false;
    }

    oid_ = answer->player.oid;

    auto connection_result = field_->connect();
    if (!connection_result.has_value()) {
        return false;
    }

    map_->setBorders(connection_result.value());

    return true;
}

bool ServerManager::doStep() {
    auto step = algorithm_->chooseStep();
    if (!step.has_value()) {
        return false;
    }

    auto step_result = field_->doStep(step.value());
    if (!step_result.has_value()) {
        return false;
    }
    map_->makeStep(step.value(), *step_result);
    return true;
}

bool ServerManager::isIWin() {
    s_cmd::GetGameResult request;
    auto answer = client_->request<s_cmd::GameResult>(&request);
    if (!answer.has_value()) {
        return false;
    }

    return std::find_if(answer->winners.begin(),
                        answer->winners.end(),
                        [this] (const s_cmd::Player& player) { return player.oid == oid_; }) !=
            answer->winners.end();
}

std::optional<std::vector<std::string>> ServerManager::getWinners() {
    s_cmd::GetGameResult request;
    auto server_answer = client_->request<s_cmd::GameResult>(&request);
    if (!server_answer.has_value() || server_answer->error.has_value()) {
        return std::nullopt;
    }

    std::vector<std::string> answer;
    for (auto& i: server_answer->winners) {
        answer.emplace_back(i.name + " (" + i.oid + ")");
    }

    return answer;
}

bool ServerManager::disconnect() {
    s_cmd::Disconnect request;
    bool result = client_->sendRequest(&request);
    client_->disconnect();
    return result;
}

std::string ServerManager::getConnectionParams() const {
    return "Address: " + connection_config_.address + ". Port: " + std::to_string(connection_config_.port) + ".";
}
