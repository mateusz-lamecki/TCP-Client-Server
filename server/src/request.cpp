#include "request.h"
#include "utils.h"

namespace sk2 {

namespace request {

std::string LoginAction::to_string() { return "LOGIN"; }
std::string RegisterAction::to_string() { return "REGISTER"; }
std::string PublishAction::to_string() { return "PUBLISH"; }
std::string SubscribeAction::to_string() { return "SUBSCRIBE"; }
std::string UnsubscribeAction::to_string() { return "UNSUBSCRIBE"; }
std::string ReadAction::to_string() { return "READ"; }
std::string InvalidAction::to_string() { return "INVALID"; }

std::string OkStatus::to_string() { return "OK"; }
std::string InvalidPasswordStatus::to_string() { return "INVALID_PASSWORD"; }
std::string LoginTakenStatus::to_string() { return "LOGIN_TAKEN"; }
std::string InvalidTokenStatus::to_string() { return "INVALID_TOKEN"; }
std::string InvalidTopicStatus::to_string() { return "INVALID_TOPIC"; }


std::unique_ptr<Action> Action::detect_action(std::string raw_content) {
    auto words = utils::split_string(raw_content, ' ');
    if(words[0] == "LOGIN") return std::unique_ptr<Action>{ new LoginAction() };
    else if(words[0] == "REGISTER")  return std::unique_ptr<Action>{ new RegisterAction() };
    else if(words[0] == "PUBLISH")  return std::unique_ptr<Action>{ new PublishAction() };
    else if(words[0] == "SUBSCRIBE")  return std::unique_ptr<Action>{ new SubscribeAction() };
    else if(words[0] == "UNSUBSCRIBE")  return std::unique_ptr<Action>{ new UnsubscribeAction() };
    else if(words[0] == "READ")  return std::unique_ptr<Action>{ new ReadAction() };
    return std::unique_ptr<Action>{ new InvalidAction() };
}

std::unique_ptr<Status> Status::detect_status(std::string raw_content) {
    auto words = utils::split_string(raw_content, ' ');
    if(words[0] == "OK") return std::unique_ptr<Status>{ new OkStatus() };
    else if(words[0] == "INVALID_PASSWORD")  return std::unique_ptr<Status>{ new InvalidPasswordStatus() };
    else if(words[0] == "LOGIN_TAKEN")  return std::unique_ptr<Status>{ new LoginTakenStatus() };
    else if(words[0] == "INVALID_TOKEN")  return std::unique_ptr<Status>{ new InvalidTokenStatus() };
    else if(words[0] == "INVALID_TOPIC")  return std::unique_ptr<Status>{ new InvalidTopicStatus() };
    else return nullptr;
}


Response::Response(std::unique_ptr<Status> status, std::string auxilary_out="") {
    this->status_str = status->to_string();
    this->auxilary_out = auxilary_out;
}

std::string Response::to_string() {
    return status_str + " " + auxilary_out;
}

} // namespace request

} // namespace sk2
