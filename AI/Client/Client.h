#ifndef AI_CLIENT_H
#define AI_CLIENT_H

//#define ASIO_STANDALONE

#include <optional>
#include <iostream>
#include <string>

#include <asio.hpp>

#include "Commands.h"

/** Client for server connection */
class Client {
public:
    /** Constructor */
    Client(const std::string& host, const short port,
           std::ostream& out = std::cout)
            : tcp_socket_(context_),
              resolver_(context_),
              query_({host.data(), std::to_string(port)}),
              out_(out) {}

    /** Function for connection to server. Return true if successful */
    bool connect(unsigned int attempt_number = 3);

    /** Disconnected client from server */
    void disconnect();

    /** Send request to the server and get answer */
    template<class T>
    std::optional<T> request(s_cmd::ServerCommand* cmd) {
        auto answer = request(cmd->toString());
        if (!answer.has_value()) {
            return std::nullopt;
        }

        return s_cmd::ServerAnswer::get<T>(answer.value());
    }

    /** Send request without getting answer */
    bool sendRequest(s_cmd::ServerCommand* cmd) {
        return sendRequest(cmd->toString());
    }

    static void makeDelay();

protected:
    asio::io_context context_;
    asio::ip::tcp::socket tcp_socket_;
    asio::ip::tcp::resolver resolver_;
    asio::ip::tcp::resolver::query query_;
    std::ostream& out_;

    /** Send request without getting answer */
    bool sendRequest(const std::string& request);

    /** Get answer from the server */
    std::optional<std::string> getAnswer();

    /** Send request to the server and get answer */
    std::optional<std::string> request(const std::string& request);
};


#endif //AI_CLIENT_H
