#pragma once

#include <memory>
#include <string>


namespace sk2 {

namespace request {

class Action {
    public:
    static std::unique_ptr<Action> detect_action(std::string raw_content);
    virtual std::string to_string() = 0;
};


class Status {
    public:
    static std::unique_ptr<Status> detect_status(std::string raw_content);
    virtual std::string to_string() = 0;
};


class LoginAction : public Action {
    public:
    std::string to_string() override;
};

class RegisterAction : public Action {
    public:
    std::string to_string() override;
};

class PublishAction : public Action {
    public:
    std::string to_string() override;
};

class SubscribeAction : public Action {
    public:
    std::string to_string() override;
};

class UnsubscribeAction : public Action {
    public:
    std::string to_string() override;
};

class ReadAction : public Action {
    public:
    std::string to_string() override;
};

class InvalidAction : public Action {
    public:
    std::string to_string() override;
};



class OkStatus : public Status {
    public:
    std::string to_string() override;
};

class InvalidPasswordStatus : public Status {
    public:
    std::string to_string() override;
};

class LoginTakenStatus : public Status {
    public:
    std::string to_string() override;
};

class InvalidTokenStatus : public Status {
    public:
    std::string to_string() override;
};

class InvalidTopicStatus : public Status {
    public:
    std::string to_string() override;
};


class Response {
    public:
    Response(std::unique_ptr<Status> status, std::string auxilary_out);
    std::string to_string();

    private:
    std::string status_str;
    std::string auxilary_out;
};


} // namespace request

} // namespace sk2
