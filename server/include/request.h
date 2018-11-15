#pragma once

#include <string>


namespace sk2 {

namespace request {

enum class Action { 
    LOGIN, REGISTER, PUBLISH,
    SUBSCRIBE, UNSUBSCRIBE, READ, INVALID
};

enum class Status {
    OK, INVALID_PASSWORD, LOGIN_TAKEN,
    INVALID_TOKEN, INVALID_TOPIC
};

class Request {
    public:
    Request(std::string raw_content);
    Action detect_action();

    protected:
    std::string raw_content;
};


} // namespace request

} // namespace sk2
