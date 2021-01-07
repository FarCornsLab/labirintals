#include "Client.h"

#include <array>

bool Client::connect(unsigned int attempt_number) {
    out_ << "Trying to connect to the server..." << std::endl;
    try {
        asio::connect(tcp_socket_, resolver_.resolve(query_));
    } catch (...) {
        out_ << "Attempt failed." << std::endl;
        if (attempt_number > 0) {
            return connect(attempt_number - 1);
        }
        return false;
    }
    out_ << "Server connection established." << std::endl;
    return true;
}

std::optional<std::string> Client::request(const std::string& request) {
    return sendRequest(request) ? getAnswer() : std::nullopt;
}

bool Client::sendRequest(const std::string& request) {
    try {
        tcp_socket_.write_some(asio::buffer(request.data(), request.length()));
    } catch (...) {
        return false;
    }
    return true;
}

std::optional<std::string> Client::getAnswer() {
    std::string answer;
    try {
        std::array<char, 1024> reply;
        size_t bytes_readable = 0;
        do {
            size_t reply_length =
                    tcp_socket_.read_some(asio::buffer(reply, reply.size()));
            answer += std::string(reply.data(), reply_length);
            asio::socket_base::bytes_readable command(true);
            tcp_socket_.io_control(command);
            bytes_readable = command.get();
        } while (bytes_readable != 0);
    } catch (...) {
        return {};
    }
    return answer;
}
