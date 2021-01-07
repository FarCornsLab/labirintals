#include "Bot.h"

int main() {
    Bot bot("conf.json");
    auto err = bot.getError();
    if (err.has_value()) {
        return -static_cast<unsigned int>(err.value());
    }

    bot.run();
    err = bot.getError();
    if (err.has_value()) {
        return -static_cast<unsigned int>(err.value());
    }

    return 0;
}
