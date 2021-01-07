#ifndef AI_COMMANDS_H
#define AI_COMMANDS_H

#include <string>
#include <optional>

#include <nlohmann/json.hpp>

namespace s_cmd {
/***** PROTOTYPE *****/
struct SendStruct {
    virtual nlohmann::json toJson() = 0;
};

struct AnsStruct {
    virtual bool fromJson(nlohmann::json& json) = 0;
};

/***** SERVER STRUCT *****/
struct Player : public AnsStruct {
    std::string name;
    int oid;

    bool fromJson(nlohmann::json& json) final;
};

struct Position : public AnsStruct {
    int x;
    int y;

    bool fromJson(nlohmann::json& json) final;
};

struct PlayerPosition : public AnsStruct {
    Player player;
    Position position;

    bool fromJson(nlohmann::json& json) final;
};

struct Map : public AnsStruct {
    std::vector<std::vector<std::string>> horizontalBorder;
    std::vector<std::vector<std::string>> verticalBorder;
    std::vector<PlayerPosition> playersPosition;

    bool fromJson(nlohmann::json& json) final;
};

struct ServerError : public AnsStruct {
    int code;
    std::string message;

    bool fromJson(nlohmann::json& json) final;
};

/***** COMMANDS PROTOTYPE *****/
struct ServerCommand: protected SendStruct {
    ServerCommand(const std::string& command) : cmd(command) {}

    const std::string cmd;

    std::string toString();
};

struct ServerAnswer : protected AnsStruct {
    ServerAnswer(const std::string& command) : cmd(command) {}

    const std::string cmd;
    std::optional<ServerError> error = std::nullopt;

    bool fromString(const std::string& s);

    static std::shared_ptr<ServerAnswer> createFromString(const std::string& s);

    template<class T>
    static std::optional<T> get(const std::string& s) {
        auto p = createFromString(s);
        if (p == nullptr) {
            return std::nullopt;
        }

        return *std::static_pointer_cast<T>(p);
    }

protected:
    /** Return null if wrong string*/
    static std::shared_ptr<ServerAnswer> initObj(std::shared_ptr<ServerAnswer> jbj, const std::string& s);
};

/***** COMMANDS *****/
struct Connection : public ServerCommand {
    Connection(const std::string& player_name) : ServerCommand("connection"),
                                                 name(player_name) {}

    std::string name;

protected:
    nlohmann::json toJson() final { return {{"name", name}}; };
};

struct ConnectionAnswer : public ServerAnswer {
    ConnectionAnswer() : ServerAnswer("connection_answer") {}

    int oid;
    int cid;

protected:
    bool fromJson(nlohmann::json& json) final;
};

struct GetGameParams : public ServerCommand {
    GetGameParams() : ServerCommand("get_game_params") {}

protected:
    nlohmann::json toJson() final { return {}; }
};

struct GameParams : public ServerAnswer {
    GameParams() : ServerAnswer("game_params") {}

    uint64_t startTime;
    int stepTime;
    std::vector<Player> players;

protected:
    bool fromJson(nlohmann::json& json) final;
};

struct MakeStep : public ServerCommand {
    MakeStep(int id, const std::string& type) : ServerCommand("make_step"),
                                                stepId(id),
                                                stepType(type) {}

    int stepId;
    std::string stepType;

protected:
    nlohmann::json toJson() final { return {{"step_id", stepId}, {"step_type", stepType}}; }
};

struct Step : public ServerCommand {
    Step() : ServerCommand("step") {}

protected:
    nlohmann::json toJson() final { return {}; }
};

struct StepAnswer : public ServerAnswer {
    StepAnswer() : ServerAnswer("step_answer") {}

    int stepId;
    std::string setStepType;
    uint64_t stepEndTime;

protected:
    bool fromJson(nlohmann::json& json) final;
};

struct GetPosition : public ServerCommand {
    GetPosition() : ServerCommand("get_position") {}

protected:
    nlohmann::json toJson() final { return {}; }
};

struct PositionAnswer : public ServerAnswer {
    PositionAnswer() : ServerAnswer("position") {}

    int stepId;
    std::vector<std::string> fieldUnit;

protected:
    bool fromJson(nlohmann::json& json) final;
};

struct GetGameResult : public ServerCommand {
    GetGameResult() : ServerCommand("get_game_result") {}

protected:
    nlohmann::json toJson() final { return {}; }
};

struct GameResult : public ServerAnswer {
    GameResult() : ServerAnswer("game_result") {}

    std::vector<Player> winners;
    int stepId;
    Map map;

protected:
    bool fromJson(nlohmann::json& json) final;
};

}

#endif //AI_COMMANDS_H
