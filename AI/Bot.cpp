#include "Bot.h"

#include <iostream>

#include <nlohmann/json.hpp>

#include "LucaTremoAlgorithm.h"
#include "Visualization/LogManagerVisualization.h"

Bot::Bot(const std::string& config_path) {
    auto [server_config, game_config] = getConfig(config_path);
    if (States::error == state_) {
        return;
    }

    static const std::map<std::string, std::shared_ptr<ExecutorAlgorithm> (*)()> get_algo = {
            {"LucaTremo", [](){ return std::static_pointer_cast<ExecutorAlgorithm>(std::make_shared<LucaTremoAlgorithm>()); }},
            {"RandomStep", [](){ return std::static_pointer_cast<ExecutorAlgorithm>(std::make_shared<LucaTremoAlgorithm>()); }}
    };

    auto algorithm = get_algo.find(game_config.algorithm_name);
    if (algorithm == get_algo.end()) {
        setError(Bot::Error::init, "Wrong algorithm name.");
        return;
    }

    std::shared_ptr<ManagerExecutor> manager = std::make_shared<ServerManager>(server_config,
                                                                               algorithm->second(),
                                                                               game_config.bot_name);
    if (game_config.is_log) {
        if (game_config.steps_log_file.has_value()) {
            auto out_file = std::make_shared<std::ofstream>(game_config.steps_log_file.value());
            if (!(out_file->is_open())) {
                setError(Bot::Error::init, "Can't open out file.");
                return;
            }
            out_ = out_file;
        } else {
            out_ = std::shared_ptr<std::ostream>(&std::cout, [](void*) {});
        }
        manager_ = std::make_shared<LogManagerVisualization>(manager, *out_);
    } else {
        manager_ = manager;
    }
}

Bot::States Bot::run() {
    state_ = Bot::States::running;

    if (!manager_->connect()) {
        setError(Bot::Error::server_connection, "Error in connection.");
        return state_;
    }

    while (manager_->doStep()) {}

    auto winners = manager_->getWinners();
    if (!winners.has_value()) {
        setError(Bot::Error::algorithm, "Error in algorithm.");
        return state_;
    }

    state_ = Bot::States::game_over;

    manager_->disconnect();

    return state_;
}

void Bot::setError(Bot::Error error, const std::string& message) {
    std::cerr << message << std::endl;
    error_ = error;
    state_ = Bot::States::error;
}

std::pair<ServerManager::ServerConfig, Bot::GameConfig> Bot::getConfig(const std::string& config_path) {
    std::ifstream file(config_path);
    if (!file.is_open()) {
        setError(Bot::Error::config, "Config file wasn't opened.");
        return {};
    }

    ServerManager::ServerConfig server_config = {};
    Bot::GameConfig game_config = {};

    nlohmann::json json_data;
    try {
        file >> json_data;

        if (json_data.is_object()) {
            game_config.algorithm_name = json_data.at("algorithm_name").get<std::string>();
            game_config.bot_name = json_data.at("bot_name").get<std::string>();
            game_config.is_log = json_data.at("is_log").get<bool>();
            auto steps_log_it = json_data.find("steps_log_file");
            if (steps_log_it != json_data.end()) {
                game_config.steps_log_file = steps_log_it->get<std::string>();
            }

            server_config.address = json_data.at("server").at("address").get<std::string>();
            server_config.port = json_data.at("server").at("port").get<unsigned int>();
        } else {
            setError(Bot::Error::config, "Json isn't object.");
            return {};
        }
    } catch (const std::exception& ex) {
        setError(Bot::Error::config, "Error in json (file " + config_path + "):" );
        return {};
    }

    return {server_config, game_config};
}
