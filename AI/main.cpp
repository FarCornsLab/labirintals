#include "Bot.h"

#include "Commands.h"

int main() {
//    Bot bot("conf.json");
//    auto err = bot.getError();
//    if (err.has_value()) {
//        return -static_cast<unsigned int>(err.value());
//    }
//
//    bot.run();
//    err = bot.getError();
//    if (err.has_value()) {
//        return -static_cast<unsigned int>(err.value());
//    }
//
//    return 0;


    auto ans = s_cmd::ServerAnswer::get<s_cmd::ConnectionAnswer>("{\n"
                                                        "    \"cmd\": \"connection_answer\",\n"
                                                        "    \"params\": {\n"
                                                        "        \"oid\": 2716057,\n"
                                                        "        \"сid\": 1729\n"
                                                        "    }\n"
                                                        "}");

    std::cout << ans->oid << std::endl;
}
