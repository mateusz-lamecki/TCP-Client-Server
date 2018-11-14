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

    protected:
    std::string raw_content;
};

class LoginRequest : public Request {
};

class RegisterRequest : public Request {
};

class PublishRequest : public Request {
};

class SubscribeRequest : public Request {
};

class UnsubscribeRequest : public Request {
};

class ReadRequest : public Request {
};


} // namespace request

} // namespace sk2
