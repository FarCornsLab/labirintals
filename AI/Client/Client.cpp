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
    std::cout << "Try sed: " << data << std::endl;
    try {
        tcp_socket_.write_some(asio::buffer(data.data(), data.length()));
    } catch (...) {
        std::cerr << "Send failed" << std::endl;
        return false;
    }
    std::cout << "Send" << std::endl;
    return true;
}

std::optional<std::string> Client::getAnswer() {
    std::string answer;
    std::cout << "Try get" << std::endl;
    try {
        std::array<char, 1024> reply;
        size_t bytes_readable = 0;
        do {
            size_t reply_length =
                    tcp_socket_.read_some(asio::buffer(reply, reply.size() - 1));
            answer += std::string(reply.data(), reply_length);
//            asio::socket_base::bytes_readable command(true);
//            tcp_socket_.io_control(command);
//            bytes_readable = command.get();
        } while (answer.back() != '\n');
    } catch (asio::system_error& ex) {
        std::cerr << "Get from server failed: " << ex.what() << std::endl;
        return {};
    }
    std::cout << "Got: " << answer << std::endl;
    return answer;
}
