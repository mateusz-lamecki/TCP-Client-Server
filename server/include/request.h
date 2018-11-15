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

class Response {
    public:
    Response(Status status, std::string auxilary_out="");
    std::string to_string();

    private:
    Status status;
    std::string auxilary_out;
};

Action detect_action(std::string raw_content);


} // namespace request

} // namespace sk2
