#include "Commands.h"

#include <iostream>

#define REGISTER_ANSWER(STRUCT) \
{ STRUCT().cmd,       \
  [](const std::string& s){ return initObj(std::make_shared<STRUCT>(STRUCT()), s); } \
}

namespace s_cmd {
bool Player::fromJson(nlohmann::json &json) {
    try {
        name = json.at("name").get<std::string>();
        oid = json.at("oid").get<std::string>();
    }  catch (const std::exception& ex) {
        return false;
    }
    return true;
}

bool Position::fromJson(nlohmann::json &json) {
    try {
        x = json.at("x").get<int>();
        y = json.at("y").get<int>();
    }  catch (const std::exception& ex) {
        return false;
    }
    return true;
}

bool PlayerPosition::fromJson(nlohmann::json &json) {
    return player.fromJson(json.at("player")) && position.fromJson(json.at("position"));
}

bool Map::fromJson(nlohmann::json &json) {
    bool status = true;
    try {
        horizontalBorder = json.at("horizontal_border").get<std::vector<std::vector<std::string>>>();
        verticalBorder = json.at("vertical_border").get<std::vector<std::vector<std::string>>>();

        playersPosition.clear();
        for (auto& i : json.at("players_position").items()) {
            playersPosition.emplace_back();
            status = status && playersPosition.back().fromJson(i.value());
            if (!status) {
                return status;
            }
        }
    }  catch (const std::exception& ex) {
        return false;
    }
    return status;
}

bool ServerError::fromJson(nlohmann::json &json) {
    try {
        code = json.at("code").get<int>();
        // TODO:
        message = json.at("message").get<std::string>();
    }  catch (const std::exception& ex) {
        return false;
    }
    return true;
}

std::string ServerCommand::toString() {
    nlohmann::json json;
    json["cmd"] = cmd;
    auto params = toJson();
    if (!params.empty()) {
        json["params"] = toJson();
    }
    return json.dump();
}

bool ServerAnswer::fromString(const std::string& s) {
    nlohmann::json json = nlohmann::json::parse(s);
    try {
        auto read = json.at("cmd").get<std::string>();
        if (cmd != read) {
            return false;
        }

        auto it = json.find("error");
        if (it != json.end()) {
            error = ServerError();
            if (!error->fromJson(*it)) {
                return false;
            }
        }
    }  catch (const std::exception& ex) {
        return false;
    }

    if (!error.has_value()) {
        return fromJson(json.at("params"));
    }

    return true;
};

std::shared_ptr<ServerAnswer> ServerAnswer::createFromString(const std::string& s) {
    static const std::unordered_map<std::string, std::shared_ptr<ServerAnswer>(*)(const std::string& s)> factory = {
            REGISTER_ANSWER(ConnectionAnswer),
            REGISTER_ANSWER(GameParams),
            REGISTER_ANSWER(StepAnswer),
            REGISTER_ANSWER(GameResult),
            REGISTER_ANSWER(PositionAnswer)
    };

    nlohmann::json json = nlohmann::json::parse(s);
    std::string cmd;
    try {
        auto t = json.at("cmd");
        cmd = t.get<std::string>();
    }  catch (const std::exception& ex) {
        return nullptr;
    }

    auto it = factory.find(cmd);
    if (it == factory.end()) {
        return nullptr;
    }

    return it->second(s);
}

std::shared_ptr<ServerAnswer> ServerAnswer::initObj(std::shared_ptr<ServerAnswer> obj, const std::string& s) {
    if (!obj->fromString(s)) {
        return nullptr;
    }
    return obj;
}

bool ConnectionAnswer::fromJson(nlohmann::json &json) {
    nlohmann::json player_json;
    try {
        player_json = json.at("player");
    }  catch (const std::exception& ex) {
        return false;
    }
    return player.fromJson(player_json);
}

bool GameParams::fromJson(nlohmann::json &json) {
    bool status = true;
    try {
        startTime = json.at("start_time").get<uint64_t>();
        stepTime = json.at("step_time").get<int>();

        players.clear();
        for (auto& i : json.at("players").items()) {
            players.emplace_back();
            status = status && players.back().fromJson(i.value());
        }
    }  catch (const std::exception& ex) {
        return false;
    }
    return status;
}

bool StepAnswer::fromJson(nlohmann::json &json) {
    try {
        stepId = json.at("step_id").get<int>();
        stepEndTime = json.at("step_end_time").get<uint64_t>();

        auto it = json.find("set_step_type");
        if (it != json.end()) {
            setStepType = *it;
        } else {
            setStepType = "null";
        }
    }  catch (const std::exception& ex) {
        return false;
    }
    return true;
}

bool PositionAnswer::fromJson(nlohmann::json &json) {
    try {
        stepId = json.at("step_id").get<int>();
        auto it = json.find("field_unit");
        if (it != json.end()) {
            fieldUnit = *it;
        }
    }  catch (const std::exception& ex) {
        return false;
    }
    return true;
}

bool GameResult::fromJson(nlohmann::json &json) {
    bool status = true;
    try {
        stepId = json.at("step_id").get<int>();
        map.fromJson(json.at("map"));

        winners.clear();
        for (auto& i : json.at("winners").items()) {
            winners.emplace_back();
            status = status && winners.back().fromJson(i.value());
        }
    }  catch (const std::exception& ex) {
        return false;
    }
    return status;
}

}