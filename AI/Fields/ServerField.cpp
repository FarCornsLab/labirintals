#include "ServerField.h"

std::optional<BordersInPoint> ServerField::connect() {
    while (true) {
        s_cmd::GetGameParams request;
        auto answer = client_->request<s_cmd::GameParams>(&request);
        if (!answer.has_value()) {
            return std::nullopt;
        }
        if (answer->startTime > 0) {
            break;
        }
        Client::makeDelay();
    }

    while (true) {
        s_cmd::GetPosition request;
        auto answer = client_->request<s_cmd::PositionAnswer>(&request);
        if (!answer.has_value() || answer->stepId < 0) {
            return std::nullopt;
        }

        if (answer->stepId > 0) {
            return getBorders(answer->fieldUnit);
        }

        Client::makeDelay();
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
    while (true) {
        s_cmd::MakeStep request(stepId_, direction);
        auto answer = client_->request<s_cmd::StepAnswer>(&request);
        if (!answer.has_value()) {
            return std::nullopt;
        }

        if (answer->error.has_value() && answer->error->code == 405) {
            s_cmd::Step request;
            auto answer = client_->request<s_cmd::StepAnswer>(&request);
            if (!answer.has_value() || answer->stepId <= 0) {
                return std::nullopt;
            }

            stepId_ = answer->stepId;
            continue;
        }

        stepId_ = answer->stepId;
        if (stepId_ < 0) {
            return std::nullopt;
        } else {
            break;
        }
    }

    while (true) {
        Client::makeDelay();

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
