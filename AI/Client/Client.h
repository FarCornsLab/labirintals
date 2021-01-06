#ifndef AI_CLIENT_H
#define AI_CLIENT_H

//#define ASIO_STANDALONE

#include <optional>
#include <iostream>
#include <string>

#include <asio.hpp>

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

    /** Function for connection to server */
    void connect(unsigned int attempt_number = 3);

    /** Send request to the server and get answer */
    std::optional<std::string> request(const std::string& request);

    /** Send request without getting answer */
    bool sendRequest(const std::string& request);

    /** Get answer from the server */
    std::optional<std::string> getAnswer();

protected:
    asio::io_context context_;
    asio::ip::tcp::socket tcp_socket_;
    asio::ip::tcp::resolver resolver_;
    asio::ip::tcp::resolver::query query_;
    std::ostream& out_;
};


#endif //AI_CLIENT_H
