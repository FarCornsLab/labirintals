#include "Client.h"

#include <array>
#include <random>
#include <thread>
#include <chrono>

#ifdef _WIN32
#include <Windows.h>
#else
#include <unistd.h>
#endif


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

void Client::disconnect() {
    //tcp_socket_.close();
}

std::optional<std::string> Client::request(const std::string& request) {
    return sendRequest(request) ? getAnswer() : std::nullopt;
}

void Client::makeDelay() {
    std::mt19937 gen(time(0));
    std::uniform_int_distribution<> uid(100, 1000);
    std::this_thread::sleep_for(std::chrono::milliseconds(uid(gen)));
}

bool Client::sendRequest(const std::string& request) {
    auto data = request + '\r' + '\n';
    try {
        tcp_socket_.write_some(asio::buffer(data.data(), data.length()));
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
                    tcp_socket_.read_some(asio::buffer(reply, reply.size() - 1));
            answer += std::string(reply.data(), reply_length);
        } while (answer.back() != '\n');
    } catch (asio::system_error& ex) {
        return {};
    }
    return answer;
}
