#include "ServerField.h"

#include <random>

std::optional<BordersInPoint> ServerField::connect() {
    std::mt19937 gen(time(0));
    std::uniform_int_distribution<> uid(100, 1500);

    while (true) {
        s_cmd::GetGameParams request;
        auto answer = client_->request<s_cmd::GameParams>(&request);
        if (!answer.has_value()) {
            return std::nullopt;
        }
        if (answer->startTime > 0) {
            break;
        }
        sleep(uid(gen));
    }

    while (true) {
        s_cmd::GetPosition request;
        auto answer = client_->request<s_cmd::PositionAnswer>(&request);
        if (!answer.has_value() || answer->stepId <= 0) {
            return std::nullopt;
        }

        if (answer->stepId > 0) {
            return getBorders(answer->fieldUnit);
        }

        sleep(uid(gen));
    }
}

std::optional<BordersInPoint> ServerField::getBorders(const std::vector<std::string>& data) {
    static const std::unordered_map<std::string, BorderType> name2type = {
            {"free", BorderType::open},
            {"obstacle", BorderType::close},
            {"exit", BorderType::is_exit}
    };

    if (data.size() != (static_cast<unsigned int>(BorderPosition::max) + 1)) {
        return std::nullopt;
    }

    BordersInPoint answer;
    for (unsigned int i = 0; i < (static_cast<unsigned int>(BorderPosition::max) + 1); i++) {
        auto it = name2type.find(data[i]);
        if (it == name2type.end()) {
            return std::nullopt;
        }

        answer[static_cast<BorderPosition>(i)] = it->second;
    }

    return answer;
}

std::optional<BordersInPoint> ServerField::makeStep(const std::string& direction) {
    std::mt19937 gen(time(0));
    std::uniform_int_distribution<> uid(100, 1000);

    {
        s_cmd::MakeStep request(stepId_, direction);
        auto answer = client_->request<s_cmd::StepAnswer>(&request);
        if (!answer.has_value()) {
            return std::nullopt;
        }

        stepId_ = answer->stepId;
        if (stepId_ < 0) {
            return std::nullopt;
        }
    }

    while (true) {
        sleep(uid(gen));

        s_cmd::Step request;
        auto answer = client_->request<s_cmd::StepAnswer>(&request);
        if (!answer.has_value() || answer->stepId <= 0) {
            return std::nullopt;
        }

        if (answer->stepId != stepId_) {
            stepId_ = answer->stepId;
            break;
        }
    }

    {
        s_cmd::GetPosition request;
        auto answer = client_->request<s_cmd::PositionAnswer>(&request);
        if (!answer.has_value() || answer->stepId <= 0) {
            return std::nullopt;
        }

        return getBorders(answer->fieldUnit);
    }

}
