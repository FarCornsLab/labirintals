#ifndef AI_BOT_H
#define AI_BOT_H

#include <optional>
#include <utility>
#include <ostream>
#include <fstream>
#include <string>

#include "Manager.h"
#include "Client.h"
#include "Executor/ServerManager.h"

class Bot {
public:
    enum class Error: unsigned int{
        init = 1,
        config,
        server_connection,
        algorithm
    };

    enum class States {
        inited,
        running,
        game_over,
        error
    };

    Bot(const std::string& config_path);

    States run();

    States state() { return state_; }

    std::optional<Error> getError() { return (state_ == States::error) ? std::optional<Error>(error_)
                                                                       : std::nullopt; };

private:
    struct GameConfig {
        std::string bot_name;
        bool is_log = false;
        std::optional<std::string> steps_log_file = std::nullopt;
    };

    void setError(Error error, const std::string& message);

    std::pair<ServerManager::ServerConfig, Bot::GameConfig> getConfig(const std::string& config_path);

    States state_ = States::inited;
    Error error_;
    std::shared_ptr<Manager> manager_;
    std::shared_ptr<std::ostream> out_;
};


#endif //AI_BOT_H
