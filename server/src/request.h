#pragma once

#include <memory>
#include <string>

#include "resources.h"


namespace sk2 {

namespace request {

static const std::string DELIMITER = "@@@";
static const std::string DELIMITER2 = "$$$";


class Status {
    public:
    static std::unique_ptr<Status> detect_status(std::string raw_status);
    virtual std::string to_string() = 0;
};


class Response {
    public:
    Response(std::unique_ptr<Status> status, std::string auxilary_out="");
    std::string to_string();

    private:
    std::string status_str;
    std::string auxilary_out;
};



class Action {
    public:
    static std::unique_ptr<Action> detect_action(std::string raw_action);
    virtual Response handle(std::string request_raw, resources::Resources& res, int client_fd) = 0;
    virtual int get_n_params() = 0;
    virtual std::string to_string() = 0;
};


class LoginAction : public Action {
    public:
    const int N_PARAMS = 2;
    Response handle(std::string request_raw, resources::Resources& res, int client_fd) override;
    int get_n_params() override;
    std::string to_string() override;
};

class RegisterAction : public Action {
    public:
    const int N_PARAMS = 2;
    Response handle(std::string request_raw, resources::Resources& res, int client_fd) override;
    int get_n_params() override;
    std::string to_string() override;
};

class PublishAction : public Action {
    public:
    const int N_PARAMS = 3;
    Response handle(std::string request_raw, resources::Resources& res, int client_fd) override;
    int get_n_params() override;
    std::string to_string() override;
};

class SubscribeAction : public Action {
    public:
    const int N_PARAMS = 2;
    Response handle(std::string request_raw, resources::Resources& res, int client_fd) override;
    int get_n_params() override;
    std::string to_string() override;
};

class UnsubscribeAction : public Action {
    public:
    const int N_PARAMS = 2;
    Response handle(std::string request_raw, resources::Resources& res, int client_fd) override;
    int get_n_params() override;
    std::string to_string() override;
};

class ReadMessagesAction : public Action {
    public:
    const int N_PARAMS = 1;
    Response handle(std::string request_raw, resources::Resources& res, int client_fd) override;
    int get_n_params() override;
    std::string to_string() override;
};

class ReadTopicsAction : public Action {
    public:
    const int N_PARAMS = 1;
    Response handle(std::string request_raw, resources::Resources& res, int client_fd) override;
    int get_n_params() override;
    std::string to_string() override;
};

class InvalidAction : public Action {
    public:
    std::string to_string() override;
    Response handle(std::string request_raw, resources::Resources& res, int client_fd) override;
    int get_n_params() override;
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

class OtherErrorStatus : public Status {
    public:
    std::string to_string() override;
};



} // namespace request

} // namespace sk2
